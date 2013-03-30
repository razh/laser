package com.razh.laser.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Credit goes to Craig Reynolds for the original boids algorithm and
 * Daniel Shiffman for his implementation in The Nature of Code.
 * @author raz
 *
 */
public class BoidComponent extends PhysicsComponent {

	private static final Vector2 mTempVector = new Vector2();

	private static final Vector2 mSteerSeparate = new Vector2();
	private static final Vector2 mSteerAlign = new Vector2();
	private static final Vector2 mSteerCohesion = new Vector2();

	public void flock(Array<BoidComponent> boids) {
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
		           .sub(getPosition())
		           .nor()
		           .mul(getMaxSpeed());

		// steering = desired - velocity.
		mTempVector.sub(getVelocity())
		           .limit(getMaxAcceleration());

		return mTempVector;
	}

	/**
	 * Moves away from nearby boids.
	 * @param boids
	 * @return
	 */
	protected Vector2 separate(Array<BoidComponent> boids) {
		float desiredSeparation = 25.0f;
		mSteerSeparate.set(Vector2.Zero);
		int count = 0;

		float distance;
		for (BoidComponent boid : boids) {
			if (boid == this) {
				continue;
			}

			distance = mTempVector.set(getPosition())
			                      .dst(boid.getPosition());

			if (0 < distance && distance < desiredSeparation) {
				mTempVector.sub(boid.getPosition())
				           .nor()
				           .div(distance);

				mSteerSeparate.add(mTempVector);
				count++;
			}
		}

		// Determine average steering.
		if (count > 0) {
			mSteerSeparate.div(count);
		}

		if (mSteerSeparate.len2() > 0) {
			mSteerSeparate.nor()
			              .mul(getMaxSpeed())
			              .sub(getVelocity())
			              .limit(getMaxAcceleration());
		}

		return mSteerSeparate;
	}

	protected Vector2 align(Array<BoidComponent> boids) {
		float neighborDistance = 50.0f;
		mSteerAlign.set(Vector2.Zero);
		int count = 0;

		float distance;
		for (BoidComponent boid : boids) {
			if (boid == this) {
				continue;
			}

			distance = mTempVector.set(getPosition())
			                      .dst(boid.getPosition());

			if (0 < distance && distance < neighborDistance) {
				mSteerAlign.add(boid.getPosition());
				count++;
			}
		}

		if (count > 0) {
			mSteerAlign.div(count)
			           .nor()
			           .mul(getMaxSpeed())
			           .sub(getVelocity())
			           .limit(getMaxAcceleration());

			return mSteerAlign;
		} else {
			return mSteerAlign.set(Vector2.Zero);
		}
	}

	// Find centroid of all nearest boids. Calculate steering location to that centroid.
	protected Vector2 cohesion(Array<BoidComponent> boids) {
		float neighborDistance = 50.0f;
		mSteerCohesion.set(Vector2.Zero);
		int count = 0;

		float distance;
		for (BoidComponent boid : boids) {
			if (boid == this) {
				continue;
			}

			distance = mTempVector.set(getPosition())
			                      .dst(boid.getPosition());

			if (0 < distance && distance < neighborDistance) {
				mSteerCohesion.add(boid.getPosition());
				count++;
			}
		}

		if (count > 0) {
			mSteerCohesion.div(count);
			return mSteerCohesion.set(seek(mSteerCohesion));
		} else {
			return mSteerCohesion.set(Vector2.Zero);
		}
	}
}
