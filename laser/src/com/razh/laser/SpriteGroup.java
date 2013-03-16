package com.razh.laser;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;

public class SpriteGroup extends Group {
	private ShaderProgram mShaderProgram;

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		spriteBatch.setShader(mShaderProgram);
		drawChildren(spriteBatch, parentAlpha);
		spriteBatch.setShader(null);
	}

	public ShaderProgram getShaderProgram() {
		return mShaderProgram;
	}

	public void setShaderProgram(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;
	}
}
