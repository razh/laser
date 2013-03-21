package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.razh.laser.EntityActor;

public class SpriteActor extends EntityActor {

	private Sprite mSprite;

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		updateSprite();
		mSprite.draw(spriteBatch, parentAlpha);
	}

	public Sprite getSprite() {
		return mSprite;
	}

	public void setSprite(Sprite sprite) {
		mSprite = sprite;
	}

	public void updateSprite() {
		float width = getWidth();
		float height = getHeight();

		float halfWidth = 0.5f * width;
		float halfHeight = 0.5f * height;

		mSprite.setRotation(getRotation());
		mSprite.setScale(getScaleX(), getScaleY());
		mSprite.setSize(width, height);
		mSprite.setOrigin(halfWidth, halfHeight);
		mSprite.setPosition(getX() - halfWidth, getY() - halfHeight);
		mSprite.setColor(getColor());
	}
}
