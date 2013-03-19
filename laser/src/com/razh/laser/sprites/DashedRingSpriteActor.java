package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

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
}
