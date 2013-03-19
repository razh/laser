package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.actions.RelativeTemporalAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.razh.laser.sprites.RingSpriteActor.RingByAction;
import com.razh.laser.sprites.RingSpriteActor.RingToAction;

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
}
