package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
		mOuterRadius = outerRadius;
	}

	public float getInnerRadius() {
		return mInnerRadius;
	}

	public void setInnerRadius(float innerRadius) {
		mInnerRadius = innerRadius;
	}
}
