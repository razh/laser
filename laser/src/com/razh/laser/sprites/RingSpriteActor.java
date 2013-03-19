package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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

		@Override
		protected void begin() {
			if (!(actor instanceof RingSpriteActor)) {
				throw new IllegalArgumentException("Attempted to attach RingAction to non-RingSpriteActor.");
			}
		}
	}

	public abstract static class RingByAction extends RelativeTemporalAction {

		private float mAmount;

		@Override
		protected void begin() {
			super.begin();
			if (!(actor instanceof RingSpriteActor)) {
				throw new IllegalArgumentException("Attempted to attach RingAction to non-RingSpriteActor.");
			}
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
			RingSpriteActor ring = (RingSpriteActor) actor;
			float outerRadius = ring.getOuterRadius();
			ring.setOuterRadius(outerRadius + outerRadius * getAmount() * percentDelta);
		}
	}

	public static class OuterRadiusToAction extends RingToAction {

		private float mStart, mEnd;

		@Override
		protected void begin() {
			super.begin();
			mStart = ((RingSpriteActor) actor).getOuterRadius();
		}

		@Override
		protected void update(float percent) {
			((RingSpriteActor) actor).setOuterRadius(mStart + (mEnd - mStart) * percent);
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
			RingSpriteActor ring = (RingSpriteActor) actor;
			float innerRadius = ring.getInnerRadius();
			ring.setInnerRadius(innerRadius + innerRadius * getAmount() * percentDelta);
		}

	}

	public static class InnerRadiusToAction extends RingToAction {

		private float mStart, mEnd;

		@Override
		protected void begin() {
			super.begin();
			mStart = ((RingSpriteActor) actor).getInnerRadius();
		}

		@Override
		protected void update(float percent) {
			((RingSpriteActor) actor).setInnerRadius(mStart + (mEnd - mStart) * percent);
		}

		public float getInnerRadius() {
			return mEnd;
		}

		public void setInnerRadius(float innerRadius) {
			mEnd = innerRadius;
		}
	}
}
