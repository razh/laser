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

	private float mZ;

	private final Vector2 mPosition2D;
	private final Vector3 mPosition;

	private float mRotationX;
	private float mRotationY;
	private float mRotationZ;

	public Actor3D() {
		super();

		mPosition2D = new Vector2();
		mPosition = new Vector3();
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

	public float getRotationX() {
		return mRotationX;
	}

	public void setRotationX(float rotationX) {
		mRotationX = rotationX;
	}

	public float getRotationY() {
		return mRotationY;
	}

	public void setRotationY(float rotationY) {
		mRotationY = rotationY;
	}

	public float getRotationZ() {
		return mRotationZ;
	}

	public void setRotationZ(float rotationZ) {
		mRotationZ = rotationZ;
	}

	public void setRotation(float rotationX, float rotationY, float rotationZ) {
		setRotationX(rotationX);
		setRotationY(rotationY);
		setRotationZ(rotationZ);
	}

	public void rotate(float rotationX, float rotationY, float rotationZ) {
		rotateX(rotationX);
		rotateY(rotationY);
		rotateZ(rotationZ);
	}

	public void rotateX(float angle) {
		mRotationX += angle;
	}

	public void rotateY(float angle) {
		mRotationY += angle;
	}

	public void rotateZ(float angle) {
		mRotationZ += angle;
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
			setZ(z);
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

		@Override
		protected void begin() {
			super.begin();
			mActor = convertToActor3D(actor);
		}

		@Override
		protected void updateRelative(float percentDelta) {
			mActor.rotate(getAmountX() * percentDelta,
			              getAmountY() * percentDelta,
			              getAmountZ() * percentDelta);
		}

		/**
		 * Necessary to update rotation quaternion on change.
		 */
		public void setAmount(float amountX, float amountY, float amountZ) {
			setAmountX(amountX);
			setAmountY(amountY);
			setAmountZ(amountZ);
		}

		public float getAmountX() {
			return mAmountX;
		}

		public void setAmountX(float amountX) {
			mAmountX = amountX;
		}

		public float getAmountY() {
			return mAmountY;
		}

		public void setAmountY(float amountY) {
			mAmountY = amountY;
		}

		public float getAmountZ() {
			return mAmountZ;
		}

		public void setAmountZ(float amountZ) {
			mAmountZ = amountZ;
		}
	}

	public static class RotateToAction3D extends RotateToAction {

		protected Actor3D mActor;

		private float mStartRotationX;
		private float mStartRotationY;
		private float mStartRotationZ;

		private float mEndRotationX;
		private float mEndRotationY;
		private float mEndRotationZ;

		@Override
		protected void begin() {
			super.begin();

			mActor = convertToActor3D(actor);
			mStartRotationX = mActor.getRotationX();
			mStartRotationY = mActor.getRotationY();
			mStartRotationZ = mActor.getRotationZ();
		}

		@Override
		protected void update(float percent) {
			super.update(percent);

			mActor.setRotation(mStartRotationX + (mEndRotationX - mStartRotationX) * percent,
			                   mStartRotationY + (mEndRotationY - mStartRotationY) * percent,
			                   mStartRotationZ + (mEndRotationZ - mStartRotationZ) * percent);
		}

		public void setRotation(float rotationX, float rotationY, float rotationZ) {
			setRotationX(rotationX);
			setRotationY(rotationY);
			setRotationZ(rotationZ);
		}

		public float getRotationX() {
			return mEndRotationX;
		}

		public void setRotationX(float rotationX) {
			mEndRotationX = rotationX;
		}

		public float getRotationY() {
			return mEndRotationY;
		}

		public void setRotationY(float rotationY) {
			mEndRotationY = rotationY;
		}

		public float getRotationZ() {
			return mEndRotationZ;
		}

		public void setRotationZ(float rotationZ) {
			mEndRotationZ = rotationZ;
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
