package com.razh.laser.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * PhysicsComponent in two dimensions.
 * Should probably switch to Box2D.
 * @author raz
 */
public class PhysicsComponent extends Component {

	private static final Vector2 mTempVector = new Vector2();
	private static final Vector2 mTempVector2 = new Vector2();

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

	public PhysicsComponent() {
		mPosition = new Vector2();
		mVelocity = new Vector2();
		mAcceleration = new Vector2();

		mMaxSpeed = Float.NaN;
		mMaxAcceleration = Float.NaN;

		mMaxAngularVelocity = Float.NaN;
		mMaxAngularAcceleration = Float.NaN;
	}

	@Override
	public void act(float delta) {
		Actor actor = getActor();

		mPosition.set(actor.getX(), actor.getY());
		accelerate(mTempVector.set(mAcceleration).mul(delta));
		translate(mTempVector.set(mVelocity).mul(delta));

		mRotation = actor.getRotation();
		angularAccelerate(mAngularAcceleration * delta);
		rotate(mAngularVelocity * delta);

		actor.setPosition(mPosition.x, mPosition.y);
		actor.setRotation(mRotation);
	}

	/**
	 * Position.
	 */
	public float getX() {
		return mPosition.x;
	}

	public void setX(float x) {
		mPosition.x = x;
	}

	public float getY() {
		return mPosition.y;
	}

	public void setY(float y) {
		mPosition.y = y;
	}

	public Vector2 getPosition() {
		return mPosition;
	}

	public void setPosition(Vector2 position) {
		mPosition.set(position);
	}

	public void setPosition(float x, float y) {
		mPosition.set(x, y);
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
		mVelocity.set(velocity).limit(mMaxSpeed);
	}

	public void setVelocity(float velocityX, float velocityY) {
		mVelocity.set(velocityX, velocityY).limit(mMaxSpeed);
	}

	public void translate(Vector2 translation) {
		translate(translation.x, translation.y);
	}

	public void translate(float translationX, float translationY) {
		mTempVector2.set(mPosition);
		setPosition(mTempVector2.add(translationX, translationY));
	}

	/**
	 * Maximum speed.
	 */
	public float getMaxSpeed() {
		return mMaxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		mMaxSpeed = Math.abs(maxSpeed);
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
		mAcceleration.set(acceleration).limit(mMaxAcceleration);
	}

	public void setAcceleration(float accelerationX, float accelerationY) {
		mAcceleration.set(accelerationX, accelerationY).limit(mMaxAcceleration);
	}

	public void accelerate(Vector2 acceleration) {
		accelerate(acceleration.x, acceleration.y);
	}

	public void accelerate(float accelerationX, float accelerationY) {
		mTempVector2.set(getVelocity());
		setVelocity(mTempVector2.add(accelerationX, accelerationY));
	}

	/**
	 * Maximum acceleration.
	 */
	public float getMaxAcceleration() {
		return mMaxAcceleration;
	}

	public void setMaxAcceleration(float maxAcceleration) {
		mMaxAcceleration = Math.abs(maxAcceleration);
	}

	/**
	 * Rotation.
	 */
	public float getRotation() {
		return mRotation;
	}

	public void setRotation(float rotation) {
		mRotation = rotation;
	}

	/**
	 * Angular velocity.
	 */
	public float getAngularVelocity() {
		return mAngularVelocity;
	}

	public void setAngularVelocity(float angularVelocity) {
		mAngularVelocity = MathUtils.clamp(angularVelocity, -mMaxAngularVelocity, mMaxAngularVelocity);
	}

	public void rotate(float angle) {
		setRotation(getRotation() + angle);
	}

	/**
	 * Maximum angular velocity.
	 */
	public float getMaxAngularVelocity() {
		return mMaxAngularVelocity;
	}

	public void setMaxAngularVelocity(float maxAngularVelocity) {
		mMaxAngularVelocity = Math.abs(maxAngularVelocity);
	}

	/**
	 * Angular acceleration.
	 */
	public float getAngularAcceleration() {
		return mAngularAcceleration;
	}

	public void setAngularAcceleration(float angularAcceleration) {
		mAngularAcceleration = MathUtils.clamp(angularAcceleration, -mMaxAngularAcceleration, mMaxAngularAcceleration);
	}

	public void angularAccelerate(float angularAcceleration) {
		setAngularVelocity(mAngularVelocity + angularAcceleration);
	}

	/**
	 * Maximum angular acceleration.
	 */
	public float getMaxAngularAcceleration() {
		return mMaxAngularAcceleration;
	}

	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		mMaxAngularAcceleration = Math.abs(maxAngularAcceleration);
	}
}
