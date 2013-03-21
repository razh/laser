package com.razh.laser.sprites;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.action;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RelativeTemporalAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class CircleSpriteActor extends ProceduralSpriteActor {

	@Override
	public void setUniforms(ShaderProgram shaderProgram) {
		super.setUniforms(shaderProgram);
		shaderProgram.setUniformf("radius", getRadius());
	}

	public float getRadius() {
		return getWidth();
	}

	public void setRadius(float radius) {
		setWidth(radius);
		setHeight(radius);
	}

	/**
	 * Actions
	 */
	public static CircleSpriteActor convertToCircleSpriteActor(Actor actor) {
		if (!(actor instanceof CircleSpriteActor)) {
			throw new IllegalArgumentException("Attempted to attach CircleAction to non-CircleSpriteActor.");
		}

		return (CircleSpriteActor) actor;
	}

	public abstract static class CircleToAction extends TemporalAction {

		protected CircleSpriteActor mActor;

		@Override
		protected void begin() {
			mActor = convertToCircleSpriteActor(actor);
		}
	}

	public abstract static class CircleByAction extends RelativeTemporalAction {

		protected CircleSpriteActor mActor;
		private float mAmount;

		@Override
		protected void begin() {
			super.begin();
			mActor = convertToCircleSpriteActor(actor);
		}

		public float getAmount() {
			return mAmount;
		}

		public void setAmount(float amount) {
			mAmount = amount;
		}
	}

	public static class RadiusByAction extends CircleByAction {

		@Override
		protected void updateRelative(float percentDelta) {
			mActor.setRadius(mActor.getRadius() + getAmount() * percentDelta);
		}
	}

	public static class RadiusToAction extends CircleToAction {

		private float mStart, mEnd;

		@Override
		protected void begin() {
			super.begin();
			mStart = mActor.getRadius();
		}

		@Override
		protected void update(float percent) {
			mActor.setRadius(mStart + (mEnd - mStart) * percent);
		}

		public float getRadius() {
			return mEnd;
		}

		public void setRadius(float radius) {
			mEnd = radius;
		}
	}


	/**
	 * Static convenience methods for actions.
	 */
	static public RadiusByAction radiusBy(float amount) {
		return radiusBy(amount, 0, null);
	}

	static public RadiusByAction radiusBy(float amount, float duration) {
		return radiusBy(amount, duration, null);
	}

	static public RadiusByAction radiusBy(float amount, float duration, Interpolation interpolation) {
		RadiusByAction action = action(RadiusByAction.class);
		action.setAmount(amount);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public RadiusToAction radiusTo(float radius) {
		return radiusTo(radius, 0, null);
	}

	static public RadiusToAction radiusTo(float radius, float duration) {
		return radiusTo(radius, duration, null);
	}

	static public RadiusToAction radiusTo(float radius, float duration, Interpolation interpolation) {
		RadiusToAction action = action(RadiusToAction.class);
		action.setRadius(radius);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}
}
