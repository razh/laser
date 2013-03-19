package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ArcSpriteActor extends RingSpriteActor {

	private float mLeftAngle;
	private float mRightAngle;

	@Override
	public void setUniforms(ShaderProgram shaderProgram) {
		super.setUniforms(shaderProgram);
		shaderProgram.setUniformf("leftAngle", getLeftAngle());
		shaderProgram.setUniformf("rightAngle", getRightAngle());
	}

	public float getLeftAngle() {
		return mLeftAngle;
	}

	public void setLeftAngle(float leftAngle) {
		mLeftAngle = leftAngle;
	}

	public float getRightAngle() {
		return mRightAngle;
	}

	public void setRightAngle(float rightAngle) {
		mRightAngle = rightAngle;
	}
}
