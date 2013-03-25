package com.razh.laser.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * PhysicsComponent in two dimensions.
 * Should probably switch to Box2D.
 * @author raz
 */
public class PhysicsComponent extends Component {

	private final Vector2 mPosition;
	private float mRotation;

	private final Vector2 mVelocity;
	private final Vector2 mAcceleration;

	private float mMaxSpeed;
	private float mMaxAcceleration;

	private float mAngularVelocity;
	private float mAngularAcceleration;

	private float mMaxAngularVelocity;
	private float mMaxAngularAcceleration;

	private final Vector2 mTempVector;

	public PhysicsComponent() {
		mPosition = new Vector2();
		mVelocity = new Vector2();
		mAcceleration = new Vector2();

		mTempVector = new Vector2();

		mMaxSpeed = Float.NaN;
		mMaxAcceleration = Float.NaN;

		mMaxAngularVelocity = Float.NaN;
		mMaxAngularAcceleration = Float.NaN;
	}

	@Override
	public void act(float delta) {
		Actor actor = getActor();
		mPosition.set(actor.getX(), actor.getY());

		mVelocity.add(mTempVector.set(mAcceleration).mul(delta)).limit(mMaxSpeed);
		mPosition.add(mTempVector.set(mVelocity).mul(delta));

		mRotation = actor.getRotation();
		mAngularVelocity += mAngularAcceleration * delta;
		mAngularVelocity = mAngularVelocity > mMaxAngularVelocity ? mAngularVelocity : mMaxAngularVelocity;
		mRotation += mAngularVelocity * delta;

		actor.setPosition(mPosition.x, mPosition.y);
		actor.setRotation(mRotation);
	}

	/**
	 * Velocity.
	 */
	public float getVelocityX() {
		return mVelocity.x;
	}

	public void setVelocityX(float velocityX) {
		mVelocity.x = velocityX;
	}

	public float getVelocityY() {
		return mVelocity.y;
	}

	public void setVelocityY(float velocityY) {
		mVelocity.y = velocityY;
	}

	public Vector2 getVelocity() {
		return mVelocity;
	}

	public void setVelocity(Vector2 velocity) {
		mVelocity.set(velocity);
	}

	/**
	 * Maximum speed.
	 */
	public float getMaxSpeed() {
		return mMaxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		mMaxSpeed = maxSpeed;
	}

	/**
	 * Acceleration.
	 */
	public float getAccelerationX() {
		return mAcceleration.x;
	}

	public void setAccelerationX(float accelerationX) {
		mAcceleration.x = accelerationX;
	}

	public float getAccelerationY() {
		return mAcceleration.y;
	}

	public void setAccelerationY(float accelerationY) {
		mAcceleration.y = accelerationY;
	}

	public Vector2 getAcceleration() {
		return mAcceleration;
	}

	public void setAcceleration(Vector2 acceleration) {
		mAcceleration.set(acceleration);
	}

	/**
	 * Maximum acceleration.
	 */
	public float getMaxAcceleration() {
		return mMaxAcceleration;
	}

	public void setMaxAcceleration(float maxAcceleration) {
		mMaxAcceleration = maxAcceleration;
	}

	/**
	 * Angular velocity.
	 */
	public float getAngularVelocity() {
		return mAngularVelocity;
	}

	public void setAngularVelocity(float angularVelocity) {
		mAngularVelocity = angularVelocity;
	}

	/**
	 * Maximum angular velocity.
	 */
	public float getMaxAngularVelocity() {
		return mMaxAngularVelocity;
	}

	public void setMaxAngularVelocity(float maxAngularVelocity) {
		mMaxAngularVelocity = maxAngularVelocity;
	}

	/**
	 * Angular acceleration.
	 */
	public float getAngularAcceleration() {
		return mAngularAcceleration;
	}

	public void setAngularAcceleration(float angularAcceleration) {
		mAngularAcceleration = angularAcceleration;
	}

	/**
	 * Maximum angular acceleration.
	 */
	public float getMaxAngularAcceleration() {
		return mMaxAngularAcceleration;
	}

	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		mMaxAngularAcceleration = maxAngularAcceleration;
	}
}
