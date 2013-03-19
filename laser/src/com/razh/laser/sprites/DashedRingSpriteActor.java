package com.razh.laser.sprites;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.action;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.RelativeTemporalAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class DashedRingSpriteActor extends RingSpriteActor {

	private float mSegmentAngle;
	private float mSegmentSpacing;

	@Override
	public void setUniforms(ShaderProgram shaderProgram) {
		super.setUniforms(shaderProgram);
		shaderProgram.setUniformf("segmentAngle", getSegmentAngle());
		shaderProgram.setUniformf("segmentSpacing", getSegmentSpacing());
	}

	public float getSegmentAngle() {
		return mSegmentAngle;
	}

	public void setSegmentAngle(float segmentAngle) {
		mSegmentAngle = segmentAngle;
	}

	public float getSegmentSpacing() {
		return mSegmentSpacing;
	}

	public void setSegmentSpacing(float segmentSpacing) {
		mSegmentSpacing = segmentSpacing;
	}

	/**
	 * Actions.
	 */
	public abstract static class DashedRingToAction extends TemporalAction {

		protected DashedRingSpriteActor mActor;

		@Override
		protected void begin() {
			if (!(actor instanceof DashedRingSpriteActor)) {
				throw new IllegalArgumentException("Attempted to attach DashedRingAction to non-DashedRingSpriteActor.");
			}

			mActor = (DashedRingSpriteActor) actor;
		}
	}

	public abstract static class DashedRingByAction extends RelativeTemporalAction {

		protected DashedRingSpriteActor mActor;
		private float mAmount;

		@Override
		protected void begin() {
			super.begin();
			if (!(actor instanceof DashedRingSpriteActor)) {
				throw new IllegalArgumentException("Attempted to attach DashedRingAction to non-DashedRingSpriteActor.");
			}

			mActor = (DashedRingSpriteActor) actor;
		}

		public float getAmount() {
			return mAmount;
		}

		public void setAmount(float amount) {
			mAmount = amount;
		}
	}

	public static class SegmentAngleByAction extends DashedRingByAction {

		@Override
		protected void updateRelative(float percentDelta) {
			mActor.setSegmentAngle(mActor.getSegmentAngle() + getAmount() * percentDelta);
		}
	}

	public static class SegmentAngleToAction extends DashedRingToAction {

		private float mStart, mEnd;

		@Override
		protected void begin() {
			super.begin();
			mStart = mActor.getSegmentAngle();
		}

		@Override
		protected void update(float percent) {
			mActor.setSegmentAngle(mStart + (mEnd - mStart) * percent);
		}

		public float getSegmentAngle() {
			return mEnd;
		}

		public void setSegmentAngle(float segmentAngle) {
			mEnd = segmentAngle;
		}
	}

	public static class SegmentSpacingByAction extends DashedRingByAction {

		@Override
		protected void updateRelative(float percentDelta) {
			mActor.setSegmentSpacing(mActor.getSegmentSpacing() + getAmount() * percentDelta);
		}

	}

	public static class SegmentSpacingToAction extends DashedRingToAction {

		private float mStart, mEnd;

		@Override
		protected void begin() {
			super.begin();
			mStart = mActor.getSegmentSpacing();
		}

		@Override
		protected void update(float percent) {
			mActor.setSegmentSpacing(mStart + (mEnd - mStart) * percent);
		}

		public float getSegmentSpacing() {
			return mEnd;
		}

		public void setSegmentSpacing(float segmentSpacing) {
			mEnd = segmentSpacing;
		}
	}

	/**
	 * Static convenience methods for actions.
	 */
	/**
	 * Static convenience methods for actions.
	 */
	static public SegmentAngleByAction segmentAngleBy(float amount) {
		return segmentAngleBy(amount, 0, null);
	}

	static public SegmentAngleByAction segmentAngleBy(float amount, float duration) {
		return segmentAngleBy(amount, duration, null);
	}

	static public SegmentAngleByAction segmentAngleBy(float amount, float duration, Interpolation interpolation) {
		SegmentAngleByAction action = action(SegmentAngleByAction.class);
		action.setAmount(amount);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public SegmentAngleToAction segmentAngleTo(float segmentAngle) {
		return segmentAngleTo(segmentAngle, 0, null);
	}

	static public SegmentAngleToAction segmentAngleTo(float segmentAngle, float duration) {
		return segmentAngleTo(segmentAngle, duration, null);
	}

	static public SegmentAngleToAction segmentAngleTo(float segmentAngle, float duration, Interpolation interpolation) {
		SegmentAngleToAction action = action(SegmentAngleToAction.class);
		action.setSegmentAngle(segmentAngle);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public SegmentSpacingByAction segmentSpacingBy(float amount) {
		return segmentSpacingBy(amount, 0, null);
	}

	static public SegmentSpacingByAction segmentSpacingBy(float amount, float duration) {
		return segmentSpacingBy(amount, duration, null);
	}

	static public SegmentSpacingByAction segmentSpacingBy(float amount, float duration, Interpolation interpolation) {
		SegmentSpacingByAction action = action(SegmentSpacingByAction.class);
		action.setAmount(amount);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public SegmentSpacingToAction segmentSpacingTo(float segmentSpacing) {
		return segmentSpacingTo(segmentSpacing, 0, null);
	}

	static public SegmentSpacingToAction segmentSpacingTo(float segmentSpacing, float duration) {
		return segmentSpacingTo(segmentSpacing, duration, null);
	}

	static public SegmentSpacingToAction segmentSpacingTo(float segmentSpacing, float duration, Interpolation interpolation) {
		SegmentSpacingToAction action = action(SegmentSpacingToAction.class);
		action.setSegmentSpacing(segmentSpacing);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}
}
