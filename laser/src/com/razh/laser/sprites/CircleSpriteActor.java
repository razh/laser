package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.razh.laser.SpriteActor;

public class CircleSpriteActor extends SpriteActor {

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha, ShaderProgram shaderProgram) {
		super.draw(spriteBatch, parentAlpha, shaderProgram);
//		System.out.println(getColor() + ", " + getX() + ", " + getY() + ", " + getWidth() + ", " + getHeight());
	}

	@Override
	public void setUniforms(ShaderProgram shaderProgram) {
		super.setUniforms(shaderProgram);
		shaderProgram.setUniformf("size", Math.max(getWidth(), getHeight()));
	}
}
