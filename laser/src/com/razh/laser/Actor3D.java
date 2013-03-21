package com.razh.laser;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

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

	@Override
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
		mRotation.set(Vector3.X, angle);
	}

	public void setRotationY(float angle) {
		mRotation.set(Vector3.Y, angle);
	}

	public void setRotationZ(float angle) {
		mRotation.set(Vector3.Z, angle);
	}

	public void rotateX(float angle) {
		mRotator.set(Vector3.X, angle);
		mRotation.mul(mRotator);
	}

	public void rotateY(float angle) {
		mRotator.set(Vector3.Y, angle);
		mRotation.mul(mRotator);
	}

	public void rotateZ(float angle) {
		mRotator.set(Vector3.Z, angle);
		mRotation.mul(mRotator);
	}

	public void setRotationQuaternion(Vector3 axis, float angle) {
		mRotation.set(axis, angle);
	}

	public Quaternion getRotationQuaternion() {
		return mRotation;
	}

	/**
	 * Actions.
	 */
	public static Actor3D convertToActor3D(Actor actor) {
		if (!(actor instanceof Actor3D)) {
			throw new IllegalArgumentException("Attempted to attach Actor3DAction to non-Actor3D.");
		}

		return (Actor3D) actor;
	}

	public static class MoveBy3DAction extends MoveByAction {

		protected Actor3D mActor;
		private float mAmountZ;

		@Override
		protected void begin() {
			super.begin();
			mActor = convertToActor3D(actor);
		}

		@Override
		protected void updateRelative(float percentDelta) {
			mActor.translate(getAmountX() * percentDelta,
			                 getAmountY() * percentDelta,
			                 getAmountZ() * percentDelta);
		}

		public void setAmount(float x, float y, float z) {
			super.setAmount(x, y);
		}

		public float getAmountZ() {
			return mAmountZ;
		}

		public void setAmountZ(float z) {
			mAmountZ = z;
		}
	}

	public static class MoveTo3DAction extends MoveToAction {

		protected Actor3D mActor;

		private float mStartZ;
		private float mEndZ;

		@Override
		protected void begin() {
			super.begin();
			mActor = convertToActor3D(actor);
			mStartZ = mActor.getZ();
		}

		@Override
		protected void update(float percent) {
			super.update(percent);
			mActor.setZ(mStartZ + (mEndZ - mStartZ) * percent);
		}

		public void setPosition(float x, float y, float z) {
			super.setPosition(x, y);
			mEndZ = z;
		}

		public float getZ() {
			return mEndZ;
		}

		public void setZ(float z) {
			mEndZ = z;
		}
	}

	/**
	 * Static convenience methods for actions.
	 */
}
