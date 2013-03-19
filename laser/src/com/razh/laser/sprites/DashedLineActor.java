package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.razh.laser.ProceduralSpriteActor;

public class DashedLineActor extends ProceduralSpriteActor {

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
}
