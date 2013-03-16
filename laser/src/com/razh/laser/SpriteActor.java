package com.razh.laser;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class SpriteActor extends EntityActor {
	private final Sprite mSprite;

	public SpriteActor() {
		super();

		mSprite = new Sprite();
	}

	@Override
	public void act(float delta) {
		mSprite.setRotation(getRotation());
		mSprite.setSize(getWidth(), getHeight());
		mSprite.setPosition(getX(), getY());
		mSprite.setColor(getColor());
	}

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		draw(spriteBatch, parentAlpha, null);
	}

	public void draw(SpriteBatch spriteBatch, float parentAlpha, ShaderProgram shaderProgram) {
		if (shaderProgram != null) {
			shaderProgram.setUniformf("color", getColor());
			shaderProgram.setUniformf("size", getWidth());
		}

		mSprite.draw(spriteBatch, parentAlpha);
	}

	public Sprite getSprite() {
		return mSprite;
	}

	public void setSprite(Sprite sprite) {
		mSprite.set(sprite);
	}
}
