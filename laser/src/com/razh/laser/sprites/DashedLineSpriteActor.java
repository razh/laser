package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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

		@Override
		protected void begin() {
			if (!(actor instanceof DashedLineSpriteActor)) {
				throw new IllegalArgumentException("Attempted to attach DashedLineAction to non-DashedLineSpriteActor.");
			}
		}
	}
}
