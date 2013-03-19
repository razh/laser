package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.razh.laser.ProceduralSpriteActor;

public class CircleSpriteActor extends ProceduralSpriteActor {

	@Override
	public void setUniforms(ShaderProgram shaderProgram) {
		super.setUniforms(shaderProgram);
		shaderProgram.setUniformf("size", getWidth());
	}
}
