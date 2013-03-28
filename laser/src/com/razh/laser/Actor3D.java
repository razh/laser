package com.razh.laser;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.action;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;

public class Actor3D extends EntityActor {

	private static final Quaternion mTempQuaternion = new Quaternion();
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

	public void setRotation(Vector3 axis, float angle) {
		mRotation.set(axis, angle);
	}

	public void setRotation(Quaternion rotationQuaternion) {
		mRotation.set(rotationQuaternion);
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

	public static class MoveByAction3D extends MoveByAction {

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
			setAmountZ(z);
		}

		public float getAmountZ() {
			return mAmountZ;
		}

		public void setAmountZ(float z) {
			mAmountZ = z;
		}
	}

	public static class MoveToAction3D extends MoveToAction {

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

	public static class RotateByAction3D extends RotateByAction {

		protected Actor3D mActor;

		private float mAmountX;
		private float mAmountY;
		private float mAmountZ;

		private Quaternion mStartRotation;
		private Quaternion mEndRotation;

		@Override
		protected void begin() {
			super.begin();
			mActor = convertToActor3D(actor);
		}

		@Override
		protected void updateRelative(float percentDelta) {
			mTempQuaternion.set(mStartRotation).slerp(mEndRotation, percentDelta);
			mActor.setRotation(mTempQuaternion);
		}

		public Quaternion getRotationQuaternion() {
			return mEndRotation;
		}

		public void updateRotationQuaternion() {
			mEndRotation.set(mStartRotation);

			// Rotate x.
			mRotator.set(Vector3.X, mAmountX);
			mEndRotation.mul(mRotator);

			// Rotate y.
			mRotator.set(Vector3.Y, mAmountY);
			mEndRotation.mul(mRotator);

			// Rotate z.
			mRotator.set(Vector3.Z, mAmountZ);
			mEndRotation.mul(mRotator);
		}

		/**
		 * Necessary to update rotation quaternion on change.
		 */
		public void setAmount(float amountX, float amountY, float amountZ) {
			mAmountX = amountX;
			mAmountY = amountY;
			mAmountZ = amountZ;
			updateRotationQuaternion();
		}

		public float getAmountX() {
			return mAmountX;
		}

		public void setAmountX(float amountX) {
			setAmount(amountX, mAmountY, mAmountZ);
		}

		public float getAmountY() {
			return mAmountY;
		}

		public void setAmountY(float amountY) {
			setAmount(mAmountX, amountY, mAmountZ);
		}

		public float getAmountZ() {
			return mAmountZ;
		}

		public void setAmountZ(float amountZ) {
			setAmount(mAmountX, mAmountY, amountZ);
		}
	}

	public static class RotateToAction3D extends RotateToAction {

		protected Actor3D mActor;

		private Quaternion mStartRotation;
		private Quaternion mEndRotation;

		private float mRotationX;
		private float mRotationY;
		private float mRotationZ;

		@Override
		protected void begin() {
			super.begin();
			mActor = convertToActor3D(actor);
			mStartRotation = mActor.getRotationQuaternion();
		}

		@Override
		protected void update(float percent) {
			super.update(percent);
			mTempQuaternion.set(mStartRotation).slerp(mEndRotation, percent);
			mActor.setRotation(mTempQuaternion);
		}

		public Quaternion getRotationQuaternion() {
			return mEndRotation;
		}

		public void updateRotationQuaternion() {
			mEndRotation.idt();

			// Rotate x.
			mRotator.set(Vector3.X, mRotationX);
			mEndRotation.mul(mRotator);

			// Rotate y.
			mRotator.set(Vector3.Y, mRotationY);
			mEndRotation.mul(mRotator);

			// Rotate z.
			mRotator.set(Vector3.Z, mRotationZ);
			mEndRotation.mul(mRotator);
		}

		public void setRotation(float rotationX, float rotationY, float rotationZ) {
			mRotationX = rotationX;
			mRotationY = rotationY;
			mRotationZ = rotationZ;
			updateRotationQuaternion();
		}

		public float getRotationX() {
			return mRotationX;
		}

		public void setRotationX(float rotationX) {
			setRotation(rotationX, mRotationY, mRotationZ);
		}

		public float getRotationY() {
			return mRotationY;
		}

		public void setRotationY(float rotationY) {
			setRotation(mRotationX, rotationY, mRotationZ);
		}

		public float getRotationZ() {
			return mRotationZ;
		}

		public void setRotationZ(float rotationZ) {
			setRotation(mRotationX, mRotationY, rotationZ);
		}
	}

	/**
	 * Static convenience methods for actions.
	 */
	static public MoveByAction3D moveBy3D(float amountX, float amountY, float amountZ) {
		return moveBy3D(amountX, amountY, amountZ, 0, null);
	}

	static public MoveByAction3D moveBy3D(float amountX, float amountY, float amountZ, float duration) {
		return moveBy3D(amountX, amountY, amountZ, duration, null);
	}

	static public MoveByAction3D moveBy3D(float amountX, float amountY, float amountZ, float duration, Interpolation interpolation) {
		MoveByAction3D action = action(MoveByAction3D.class);
		action.setAmount(amountX, amountY, amountZ);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public MoveToAction3D moveTo3D(float x, float y, float z) {
		return moveTo3D(x, y, z, 0, null);
	}

	static public MoveToAction3D moveTo3D(float x, float y, float z, float duration) {
		return moveTo3D(x, y, z, duration, null);
	}

	static public MoveToAction3D moveTo3D(float x, float y, float z, float duration, Interpolation interpolation) {
		MoveToAction3D action = action(MoveToAction3D.class);
		action.setPosition(x, y, z);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public RotateByAction3D rotateBy3D(float amountX, float amountY, float amountZ) {
		return rotateBy3D(amountX, amountY, amountZ, 0, null);
	}

	static public RotateByAction3D rotateBy3D(float amountX, float amountY, float amountZ, float duration) {
		return rotateBy3D(amountX, amountY, amountZ, duration, null);
	}

	static public RotateByAction3D rotateBy3D(float amountX, float amountY, float amountZ, float duration, Interpolation interpolation) {
		RotateByAction3D action = action(RotateByAction3D.class);
		action.setAmount(amountX, amountY, amountZ);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public RotateToAction3D rotateTo3D(float rotationX, float rotationY, float rotationZ) {
		return rotateTo3D(rotationX, rotationY, rotationZ, 0, null);
	}

	static public RotateToAction3D rotateTo3D(float rotationX, float rotationY, float rotationZ, float duration) {
		return rotateTo3D(rotationX, rotationY, rotationZ, duration, null);
	}

	static public RotateToAction3D rotateTo3D(float rotationX, float rotationY, float rotationZ, float duration, Interpolation interpolation) {
		RotateToAction3D action = action(RotateToAction3D.class);
		action.setRotation(rotationX, rotationY, rotationZ);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}
}
