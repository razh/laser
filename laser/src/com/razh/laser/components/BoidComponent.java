package com.razh.laser.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * Credit goes to Craig Reynolds for the original boids algorithm and
 * Daniel Shiffman for his implementation in The Nature of Code.
 * @author raz
 *
 */
public class BoidComponent extends PhysicsComponent {

	private static final Vector2 mTempVector = new Vector2();
	private static final Vector2 mTempVector2 = new Vector2();

	@Override
	public void act(float delta) {
		super.act(delta);

		setAcceleration(0.0f, 0.0f);
	}

	public void flock(Array<Actor> boids) {
		Vector2 separation = separate(boids);
		Vector2 align = align(boids);
		Vector2 cohesion = cohesion(boids);

		// Arbitrary weights.
		separation.mul(1.5f);
		align.mul(1.0f);
		cohesion.mul(1.0f);

		setAcceleration(getAcceleration().add(separation.add(cohesion).add(align)));
	}

	protected Vector2 seek(Vector2 target) {
		// Get desired direction to target. Normalize, and scale to maximum speed.
		mTempVector.set(target)
		           .sub(getX(), getY())
		           .nor()
		           .mul(getMaxSpeed());

		// steering = desired - velocity.
		mTempVector.sub(getVelocity())
		           .limit(getMaxAcceleration());

		return mTempVector.cpy();
	}

	protected Vector2 separate(Array<Actor> boids) {
		return null;
	}

	protected Vector2 align(Array<Actor> boids) {
		return null;
	}

	// Find centroid of all nearest boids. Calculate steering location to that centroid.
	protected Vector2 cohesion(Array<Actor> boids) {
		float neighborDistance = 50.0f;
		mTempVector2.set(Vector2.Zero);
		int count = 0;

		float distance;
		for (Actor boid : boids) {
			if (boid == this.getActor()) {
				continue;
			}

			distance = mTempVector.set(boid.getX(), boid.getY())
			                      .dst(getPosition());

			if (0 < distance && distance < neighborDistance) {
				mTempVector2.add(boid.getX(), boid.getY());
				count++;
			}
		}

		if (count > 0) {
			mTempVector2.div(count);
			return seek(mTempVector2);
		} else {
			return mTempVector2.set(Vector2.Zero);
		}
	}
}
