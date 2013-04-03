package com.razh.laser;

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
}
