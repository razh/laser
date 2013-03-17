package com.razh.laser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class SpriteActor extends EntityActor {
	private final Sprite mSprite;

	public SpriteActor() {
		super();

		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		Texture texture = new Texture(pixmap);
		mSprite = new Sprite(texture);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

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
			setUniforms(shaderProgram);
		}

		mSprite.draw(spriteBatch, parentAlpha);
	}

	public Sprite getSprite() {
		return mSprite;
	}

	public void setSprite(Sprite sprite) {
		mSprite.set(sprite);
	}

	public void setUniforms(ShaderProgram shaderProgram) {
		shaderProgram.setUniformf("color", mSprite.getColor());
	}
}
