package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.razh.laser.ProceduralSpriteActor;

public class CircleSpriteActor extends ProceduralSpriteActor {

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha, ShaderProgram shaderProgram) {
		super.draw(spriteBatch, parentAlpha, shaderProgram);
	}

	@Override
	public void setUniforms(ShaderProgram shaderProgram) {
		super.setUniforms(shaderProgram);
		shaderProgram.setUniformf("size", Math.max(getWidth(), getHeight()));
	}
}
