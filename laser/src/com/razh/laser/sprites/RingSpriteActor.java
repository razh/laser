package com.razh.laser.sprites;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.action;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.RelativeTemporalAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.razh.laser.ProceduralSpriteActor;

public class RingSpriteActor extends ProceduralSpriteActor {

	private float mOuterRadius;
	private float mInnerRadius;

	@Override
	public void setUniforms(ShaderProgram shaderProgram) {
		super.setUniforms(shaderProgram);
		shaderProgram.setUniformf("outerRadius", getOuterRadius());
		shaderProgram.setUniformf("innerRadius", getInnerRadius());
	}

	public float getOuterRadius() {
		return mOuterRadius;
	}

	public void setOuterRadius(float outerRadius) {
		mOuterRadius = MathUtils.clamp(outerRadius, 0.0f, 1.0f);
	}

	public float getInnerRadius() {
		return mInnerRadius;
	}

	public void setInnerRadius(float innerRadius) {
		mInnerRadius = MathUtils.clamp(innerRadius, 0.0f, mOuterRadius);
	}

	/**
	 * Actions.
	 */
	public abstract static class RingToAction extends TemporalAction {

		protected RingSpriteActor mActor;

		@Override
		protected void begin() {
			if (!(actor instanceof RingSpriteActor)) {
				throw new IllegalArgumentException("Attempted to attach RingAction to non-RingSpriteActor.");
			}

			mActor = (RingSpriteActor) actor;
		}
	}

	public abstract static class RingByAction extends RelativeTemporalAction {

		protected RingSpriteActor mActor;
		private float mAmount;

		@Override
		protected void begin() {
			super.begin();
			if (!(actor instanceof RingSpriteActor)) {
				throw new IllegalArgumentException("Attempted to attach RingAction to non-RingSpriteActor.");
			}

			mActor = (RingSpriteActor) actor;
		}

		public float getAmount() {
			return mAmount;
		}

		public void setAmount(float amount) {
			mAmount = amount;
		}
	}

	public static class OuterRadiusByAction extends RingByAction {

		@Override
		protected void updateRelative(float percentDelta) {
			mActor.setOuterRadius(mActor.getOuterRadius() + getAmount() * percentDelta);
		}
	}

	public static class OuterRadiusToAction extends RingToAction {

		private float mStart, mEnd;

		@Override
		protected void begin() {
			super.begin();
			mStart = mActor.getOuterRadius();
		}

		@Override
		protected void update(float percent) {
			mActor.setOuterRadius(mStart + (mEnd - mStart) * percent);
		}

		public float getOuterRadius() {
			return mEnd;
		}

		public void setOuterRadius(float outerRadius) {
			mEnd = outerRadius;
		}
	}

	public static class InnerRadiusByAction extends RingByAction {

		@Override
		protected void updateRelative(float percentDelta) {
			mActor.setInnerRadius(mActor.getInnerRadius() + getAmount() * percentDelta);
		}

	}

	public static class InnerRadiusToAction extends RingToAction {

		private float mStart, mEnd;

		@Override
		protected void begin() {
			super.begin();
			mStart = mActor.getInnerRadius();
		}

		@Override
		protected void update(float percent) {
			mActor.setInnerRadius(mStart + (mEnd - mStart) * percent);
		}

		public float getInnerRadius() {
			return mEnd;
		}

		public void setInnerRadius(float innerRadius) {
			mEnd = innerRadius;
		}
	}


	/**
	 * Static convenience methods for actions.
	 */
	static public OuterRadiusByAction outerRadiusBy(float amount) {
		return outerRadiusBy(amount, 0, null);
	}

	static public OuterRadiusByAction outerRadiusBy(float amount, float duration) {
		return outerRadiusBy(amount, duration, null);
	}

	static public OuterRadiusByAction outerRadiusBy(float amount, float duration, Interpolation interpolation) {
		OuterRadiusByAction action = action(OuterRadiusByAction.class);
		action.setAmount(amount);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public OuterRadiusToAction outerRadiusTo(float outerRadius) {
		return outerRadiusTo(outerRadius, 0, null);
	}

	static public OuterRadiusToAction outerRadiusTo(float outerRadius, float duration) {
		return outerRadiusTo(outerRadius, duration, null);
	}

	static public OuterRadiusToAction outerRadiusTo(float outerRadius, float duration, Interpolation interpolation) {
		OuterRadiusToAction action = action(OuterRadiusToAction.class);
		action.setOuterRadius(outerRadius);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public InnerRadiusByAction innerRadiusBy(float amount) {
		return innerRadiusBy(amount, 0, null);
	}

	static public InnerRadiusByAction innerRadiusBy(float amount, float duration) {
		return innerRadiusBy(amount, duration, null);
	}

	static public InnerRadiusByAction innerRadiusBy(float amount, float duration, Interpolation interpolation) {
		InnerRadiusByAction action = action(InnerRadiusByAction.class);
		action.setAmount(amount);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public InnerRadiusToAction innerRadiusTo(float innerRadius) {
		return innerRadiusTo(innerRadius, 0, null);
	}

	static public InnerRadiusToAction innerRadiusTo(float innerRadius, float duration) {
		return innerRadiusTo(innerRadius, duration, null);
	}

	static public InnerRadiusToAction innerRadiusTo(float innerRadius, float duration, Interpolation interpolation) {
		InnerRadiusToAction action = action(InnerRadiusToAction.class);
		action.setInnerRadius(innerRadius);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}
}
