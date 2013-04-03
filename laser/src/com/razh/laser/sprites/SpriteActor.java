package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.razh.laser.EntityActor;

public class SpriteActor extends EntityActor {

	/**
	 * Where the origin of the Sprite is located.
	 * Not sure how it interacts with getOriginX/Y().
	 */
	public enum Origin {
		BOTTOM_LEFT,
		LEFT,
		CENTER,
		CUSTOM
	}

	private Sprite mSprite;
	private Origin mOrigin;

	private int mSrcFactor;
	private int mDstFactor;

	public SpriteActor() {
		super();
		mOrigin = Origin.CENTER;

		setBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		updateSprite();
		spriteBatch.setBlendFunction(mSrcFactor, mDstFactor);
		mSprite.draw(spriteBatch, parentAlpha);
	}

	public void updateSprite() {
		float width = getWidth();
		float height = getHeight();

		float halfWidth = 0.5f * width;
		float halfHeight = 0.5f * height;

		mSprite.setRotation(getRotation());
		mSprite.setScale(getScaleX(), getScaleY());
		mSprite.setSize(width, height);

		switch (mOrigin) {
			case BOTTOM_LEFT:
				mSprite.setOrigin(getOriginX(), getOriginY());
				mSprite.setPosition(getX(), getY());
				break;

			case LEFT:
				mSprite.setOrigin(getOriginX(), getOriginY() + halfHeight);
				mSprite.setPosition(getX(), getY() - halfHeight);
				break;

			case CENTER:
				mSprite.setOrigin(getOriginX() + halfWidth, getOriginY() + halfHeight);
				mSprite.setPosition(getX() - halfWidth, getY() - halfHeight);
				break;

			case CUSTOM:
				mSprite.setOrigin(getOriginX(), getOriginY());
				mSprite.setPosition(getX() - halfWidth, getY() - halfHeight);
				break;
		}

		mSprite.setColor(getColor());
	}

	public Sprite getSprite() {
		return mSprite;
	}

	public void setSprite(Sprite sprite) {
		mSprite = sprite;
	}

	public Origin getOrigin() {
		return mOrigin;
	}

	public void setOrigin(Origin origin) {
		mOrigin = origin;
	}

	@Override
	public void setOrigin(float originX, float originY) {
		super.setOrigin(originX, originY);
		mOrigin = Origin.CUSTOM;
	}

	public int getSrcFactor() {
		return mSrcFactor;
	}

	public int getDstFactor() {
		return mDstFactor;
	}

	public void setBlendFunc(int srcFactor, int dstFactor) {
		mSrcFactor = srcFactor;
		mDstFactor = dstFactor;
	}
}
