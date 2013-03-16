package com.razh.laser;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteActor extends EntityActor {
	private final Sprite mSprite;

	public SpriteActor() {
		super();

		mSprite = new Sprite();
	}

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		mSprite.draw(spriteBatch, parentAlpha);
	}

	public Sprite getSprite() {
		return mSprite;
	}

	public void setSprite(Sprite sprite) {
		mSprite.set(sprite);
	}
}
