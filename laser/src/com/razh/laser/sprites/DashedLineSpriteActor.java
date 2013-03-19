package com.razh.laser.sprites;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.action;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.RelativeTemporalAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.razh.laser.ProceduralSpriteActor;

public class DashedLineSpriteActor extends ProceduralSpriteActor {

	private float mSegmentLength;
	private float mSegmentSpacing;

	@Override
	public void setUniforms(ShaderProgram shaderProgram) {
		super.setUniforms(shaderProgram);
		shaderProgram.setUniformf("segmentLength", getSegmentLength());
		shaderProgram.setUniformf("segmentSpacing", getSegmentSpacing());
	}

	public float getSegmentLength() {
		return mSegmentLength;
	}

	public void setSegmentLength(float segmentLength) {
		mSegmentLength = segmentLength;
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
	public abstract static class DashedLineToAction extends TemporalAction {

		protected DashedLineSpriteActor mActor;

		@Override
		protected void begin() {
			if (!(actor instanceof DashedLineSpriteActor)) {
				throw new IllegalArgumentException("Attempted to attach DashedLineAction to non-DashedLineSpriteActor.");
			}

			mActor = (DashedLineSpriteActor) actor;
		}
	}

	public abstract static class DashedLineByAction extends RelativeTemporalAction {

		protected DashedLineSpriteActor mActor;
		private float mAmount;

		@Override
		protected void begin() {
			super.begin();
			if (!(actor instanceof DashedLineSpriteActor)) {
				throw new IllegalArgumentException("Attempted to attach DashedLineAction to non-DashedLineSpriteActor.");
			}

			mActor = (DashedLineSpriteActor) actor;
		}

		public float getAmount() {
			return mAmount;
		}

		public void setAmount(float amount) {
			mAmount = amount;
		}
	}

	public static class SegmentLengthByAction extends DashedLineByAction {

		@Override
		protected void updateRelative(float percentDelta) {
			mActor.setSegmentLength(mActor.getSegmentLength() + getAmount() * percentDelta);
		}
	}

	public static class SegmentLengthToAction extends DashedLineToAction {

		private float mStart, mEnd;

		@Override
		protected void begin() {
			super.begin();
			mStart = mActor.getSegmentLength();
		}

		@Override
		protected void update(float percent) {
			mActor.setSegmentLength(mStart + (mEnd - mStart) * percent);
		}

		public float getSegmentLength() {
			return mEnd;
		}

		public void setSegmentLength(float segmentLength) {
			mEnd = segmentLength;
		}
	}

	public static class SegmentSpacingByAction extends DashedLineByAction {

		@Override
		protected void updateRelative(float percentDelta) {
			mActor.setSegmentSpacing(mActor.getSegmentSpacing() + getAmount() * percentDelta);
		}
	}

	public static class SegmentSpacingToAction extends DashedLineToAction {

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
	static public SegmentLengthByAction segmentLengthBy(float amount) {
		return segmentLengthBy(amount, 0, null);
	}

	static public SegmentLengthByAction segmentLengthBy(float amount, float duration) {
		return segmentLengthBy(amount, duration, null);
	}

	static public SegmentLengthByAction segmentLengthBy(float amount, float duration, Interpolation interpolation) {
		SegmentLengthByAction action = action(SegmentLengthByAction.class);
		action.setAmount(amount);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}


	static public SegmentLengthToAction segmentLengthTo(float segmentLength) {
		return segmentLengthTo(segmentLength, 0, null);
	}

	static public SegmentLengthToAction segmentLengthTo(float segmentLength, float duration) {
		return segmentLengthTo(segmentLength, duration, null);
	}

	static public SegmentLengthToAction segmentLengthTo(float segmentLength, float duration, Interpolation interpolation) {
		SegmentLengthToAction action = action(SegmentLengthToAction.class);
		action.setSegmentLength(segmentLength);
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
