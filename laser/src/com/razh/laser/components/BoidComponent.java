package com.razh.laser.components;

import com.badlogic.gdx.math.MathUtils;
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

	private float mSeparation;
	private float mAlignDistance;
	private float mCohesionDistance;

	public BoidComponent() {
		setSeparation(100.0f);
		setAlignDistance(50.0f);
		setCohesionDistance(50.0f);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		Vector2 velocity = getVelocity();
		getActor().setRotation((float) Math.atan2(velocity.y, velocity.x) * MathUtils.radiansToDegrees);
		setAcceleration(Vector2.Zero);
	}

	public void flock(Array<BoidComponent> boids) {
		Vector2 separation = separate(boids);
		Vector2 align = align(boids);
		Vector2 cohesion = cohesion(boids);

		// Arbitrary weights.
		separation.mul(1.5f);
		align.mul(1.0f);
		cohesion.mul(1.0f);

		setAcceleration(getAcceleration().add(separation.add(cohesion).add(align)));
		setAcceleration(getAcceleration().sub(mTempVector.set(getPosition()).limit(500.0f)));
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
		mSteerSeparate.set(Vector2.Zero);
		int count = 0;

		float distance;
		for (BoidComponent boid : boids) {
			if (boid == this) {
				continue;
			}

			distance = mTempVector.set(getPosition())
			                      .dst(boid.getPosition());

			if (0 < distance && distance < mSeparation) {
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
		mSteerAlign.set(Vector2.Zero);
		int count = 0;

		float distance;
		for (BoidComponent boid : boids) {
			if (boid == this) {
				continue;
			}

			distance = mTempVector.set(getPosition())
			                      .dst(boid.getPosition());

			if (0 < distance && distance < mAlignDistance) {
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
		mSteerCohesion.set(Vector2.Zero);
		int count = 0;

		float distance;
		for (BoidComponent boid : boids) {
			if (boid == this) {
				continue;
			}

			distance = mTempVector.set(getPosition())
			                      .dst(boid.getPosition());

			if (0 < distance && distance < mCohesionDistance) {
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

	public float getSeparation() {
		return mSeparation;
	}

	public void setSeparation(float separation) {
		mSeparation = separation;
	}

	public float getAlignDistance() {
		return mAlignDistance;
	}

	public void setAlignDistance(float alignDistance) {
		mAlignDistance = alignDistance;
	}

	public float getCohesionDistance() {
		return mCohesionDistance;
	}

	public void setCohesionDistance(float cohesionDistance) {
		mCohesionDistance = cohesionDistance;
	}
}
