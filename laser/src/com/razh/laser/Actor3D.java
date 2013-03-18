package com.razh.tiling;

public class Actor3D extends EntityActor {

	private static final Quaternion mRotator = new Quaternion(0, 0, 0, 0);

	private float mZ;

	private final Vector2 mPosition2D;
	private final Vector3 mPosition;

	private final Quaternion mRotation;

	public Actor3D() {
		super();

		mPosition2D = new Vector2();
		mPosition = new Vector3();

		mRotation = new Quaternion();
	}

	public float getZ() {
		return mZ;
	}

	public void setZ(float z) {
		mZ = z;
	}

	public Vector2 getPosition2D() {
		return mPosition2D.set(getX(), getY());
	}

	public Vector3 getPosition() {
		return mPosition.set(getX(), getY(), getZ());
	}

	public void setPosition(float x, float y) {
		super.setPosition(x, y);
	}

	public void setPosition(float x, float y, float z) {
		super.setPosition(x, y);
		setZ(z);
	}

	public void translate(float x, float y, float z) {
		super.translate(x, y);
		setZ(getZ() + z);
	}

	public void setRotationX(float angle) {
		mRotation.set(X_AXIS, angle);
	}

	public void setRotationY(float angle) {
		mRotation.set(Y_AXIS, angle);
	}

	public void setRotationZ(float angle) {
		mRotation.set(Z_AXIS, angle);
	}

	public void rotateX(float angle) {
		mRotator.set(X_AXIS, angle);
		mRotation.mul(rotator);
	}

	public void rotateY(float angle) {
		mRotator.set(Y_AXIS, angle);
		mRotation.mul(rotator);
	}

	public void rotateZ(float angle) {
		mRotator.set(Z_AXIS, angle);
		mRotation.mul(rotator);
	}

	public void setRotation(Vector3 axis, float angle) {
		mRotation.set(axis, angle);
	}

	public Quaternion getRotation() {
		return mRotation;
	}
}
