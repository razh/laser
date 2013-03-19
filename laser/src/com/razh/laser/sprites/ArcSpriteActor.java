package com.razh.laser.sprites;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.action;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
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
		mLeftAngle = MathUtils.clamp(leftAngle, 0.0f, MathUtils.PI);
	}

	public float getRightAngle() {
		return mRightAngle;
	}

	public void setRightAngle(float rightAngle) {
		mRightAngle = MathUtils.clamp(rightAngle, 0.0f, MathUtils.PI);
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
			super.begin();
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

	/**
	 * Static convenience methods for actions.
	 */
	static public LeftAngleByAction leftAngleBy(float amount) {
		return leftAngleBy(amount, 0, null);
	}

	static public LeftAngleByAction leftAngleBy(float amount, float duration) {
		return leftAngleBy(amount, duration, null);
	}

	static public LeftAngleByAction leftAngleBy(float amount, float duration, Interpolation interpolation) {
		LeftAngleByAction action = action(LeftAngleByAction.class);
		action.setAmount(amount);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public LeftAngleToAction leftAngleTo(float leftAngle) {
		return leftAngleTo(leftAngle, 0, null);
	}

	static public LeftAngleToAction leftAngleTo(float leftAngle, float duration) {
		return leftAngleTo(leftAngle, duration, null);
	}

	static public LeftAngleToAction leftAngleTo(float leftAngle, float duration, Interpolation interpolation) {
		LeftAngleToAction action = action(LeftAngleToAction.class);
		action.setLeftAngle(leftAngle);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public RightAngleByAction rightAngleBy(float amount) {
		return rightAngleBy(amount, 0, null);
	}

	static public RightAngleByAction rightAngleBy(float amount, float duration) {
		return rightAngleBy(amount, duration, null);
	}

	static public RightAngleByAction rightAngleBy(float amount, float duration, Interpolation interpolation) {
		RightAngleByAction action = action(RightAngleByAction.class);
		action.setAmount(amount);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public RightAngleToAction rightAngleTo(float rightAngle) {
		return rightAngleTo(rightAngle, 0, null);
	}

	static public RightAngleToAction rightAngleTo(float rightAngle, float duration) {
		return rightAngleTo(rightAngle, duration, null);
	}

	static public RightAngleToAction rightAngleTo(float rightAngle, float duration, Interpolation interpolation) {
		RightAngleToAction action = action(RightAngleToAction.class);
		action.setRightAngle(rightAngle);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}
}
