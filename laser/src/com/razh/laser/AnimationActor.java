package com.razh.laser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.razh.laser.sprites.SpriteActor;

public class AnimationActor extends SpriteActor {

	private Animation mAnimation;
	private float mStateTime;

	public AnimationActor() {
		super();
		mStateTime = 0.0f;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		mStateTime += delta;
	}

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		TextureRegion frame = mAnimation.getKeyFrame(mStateTime, true);
		getSprite().setRegion(frame);

		super.draw(spriteBatch, parentAlpha);
	}

	public Animation getAnimation() {
		return mAnimation;
	}

	public void setAnimation(Animation animation) {
		mAnimation = animation;
	}

	/**
	 * Creates an AnimationActor.
	 * @param internalPath Path of sprite sheet.
	 * @param frameCount Number of frames.
	 * @param frameTime Time spent per frame.
	 * @param rows Number of rows in the sprite sheet.
	 * @param columns Number of columns in the sprite sheet.
	 * @return AnimationActor
	 */
	public static AnimationActor create(String internalPath, int frameCount, float frameTime, int rows, int columns) {
		Texture sheet = new Texture(Gdx.files.internal(internalPath));
		TextureRegion[][] regions = TextureRegion.split(sheet, sheet.getWidth() / columns, sheet.getHeight() / rows);
		TextureRegion[] frames = new TextureRegion[frameCount];

		// Add frames to animation.
		int index = 0;
		int i = 0;
		int j;
		while (frameCount > 0) {
			j = 0;
			while (frameCount > 0) {
				frames[index++] = regions[i][j];
				frameCount--;
				j++;
			}

			i++;
		}

		for (i = 0; i < rows; i++) {
			for (j = 0; j < columns; j++) {
				frames[index++] = regions[i][j];
			}
		}

		AnimationActor actor = new AnimationActor();
		actor.setAnimation(new Animation(frameTime, frames));

		sheet.dispose();

		return actor;
	}
}
