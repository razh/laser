package com.razh.laser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.razh.laser.sprites.SpriteActor;
import com.razh.laser.textures.TextureUtils;

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
	 * @param sheet Using texture instead of file path because we want to control/dispose assets.
	 * @param frameCount Number of frames.
	 * @param frameTime Time spent per frame.
	 * @param rows Number of rows in the sprite sheet.
	 * @param columns Number of columns in the sprite sheet.
	 * @return AnimationActor
	 */
	public static AnimationActor create(Texture sheet, int frameCount, float frameTime, int rows, int columns) {
		TextureRegion[][] regions = TextureRegion.split(sheet, sheet.getWidth() / columns, sheet.getHeight() / rows);
		TextureRegion[] frames = TextureUtils.flatten(regions, frameCount, rows, columns);

		AnimationActor actor = new AnimationActor();
		actor.setSprite(new Sprite());
		actor.setAnimation(new Animation(frameTime, frames));

		return actor;
	}
}
