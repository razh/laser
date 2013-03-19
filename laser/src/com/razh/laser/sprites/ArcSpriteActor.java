package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.actions.RelativeTemporalAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class ArcSpriteActor extends RingSpriteActor {

	private float mLeftAngle;
	private float mRightAngle;

	@Override
	public void setUniforms(ShaderProgram shaderProgram) {
		super.setUniforms(shaderProgram);
		shaderProgram.setUniformf("leftAngle", getLeftAngle());
		shaderProgram.setUniformf("rightAngle", getRightAngle());
	}

	public float getLeftAngle() {
		return mLeftAngle;
	}

	public void setLeftAngle(float leftAngle) {
		mLeftAngle = leftAngle;
	}

	public float getRightAngle() {
		return mRightAngle;
	}

	public void setRightAngle(float rightAngle) {
		mRightAngle = rightAngle;
	}

	/**
	 * Actions.
	 */
	public abstract static class ArcToAction extends TemporalAction {

		protected ArcSpriteActor mActor;

		@Override
		protected void begin() {
			if (!(actor instanceof ArcSpriteActor)) {
				throw new IllegalArgumentException("Attempted to attach ArcAction to non-ArcSpriteActor.");
			}

			mActor = (ArcSpriteActor) actor;
		}
	}


	public abstract static class ArcByAction extends RelativeTemporalAction {

		protected ArcSpriteActor mActor;
		private float mAmount;

		@Override
		protected void begin() {
			if (!(actor instanceof ArcSpriteActor)) {
				throw new IllegalArgumentException("Attempted to attach ArcAction to non-ArcSpriteActor.");
			}

			mActor = (ArcSpriteActor) actor;
		}

		public float getAmount() {
			return mAmount;
		}

		public void setAmount(float amount) {
			mAmount = amount;
		}
	}

	public static class LeftAngleByAction extends ArcByAction {

		@Override
		protected void updateRelative(float percentDelta) {
			mActor.setLeftAngle(mActor.getLeftAngle() + getAmount() * percentDelta);
		}
	}

	public static class LeftAngleToAction extends ArcToAction {

		private float mStart, mEnd;

		@Override
		protected void begin() {
			super.begin();
			mStart = mActor.getLeftAngle();
		}

		@Override
		protected void update(float percent) {
			mActor.setLeftAngle(mStart + (mEnd - mStart) * percent);
		}

		public float getLeftAngle() {
			return mEnd;
		}

		public void setLeftAngle(float leftAngle) {
			mEnd = leftAngle;
		}
	}

	public static class RightAngleByAction extends ArcByAction {

		@Override
		protected void updateRelative(float percentDelta) {
			mActor.setRightAngle(mActor.getRightAngle() + getAmount() * percentDelta);
		}
	}

	public static class RightAngleToAction extends ArcToAction {

		private float mStart, mEnd;

		@Override
		protected void begin() {
			super.begin();
			mStart = mActor.getRightAngle();
		}

		@Override
		protected void update(float percent) {
			mActor.setRightAngle(mStart + (mEnd - mStart) * percent);
		}

		public float getRightAngle() {
			return mEnd;
		}

		public void setRightAngle(float rightAngle) {
			mEnd = rightAngle;
		}
	}
}
