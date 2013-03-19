package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
}
