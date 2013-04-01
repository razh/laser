package com.razh.laser.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.razh.laser.ActorContainer;

public class LaserComponent extends TransformComponent {

	private Sprite mLaserSprite;
	private final Color mLaserColor;

	private float mLaserWidth;
	private float mLaserHeight;

	private int mLayerCount;

	private final ActorContainer mLaserActors;

	public LaserComponent() {
		mLaserColor = new Color();
		mLaserActors = new ActorContainer();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	public int getLayerCount() {
		return mLayerCount;
	}

	public void setLayerCount(int layerCount) {
		// Remove unwanted actors.
		if (mLayerCount < layerCount) {
			int count = layerCount - mLayerCount;
			while (count-- > 0) {
				mLaserActors.getActors().removeIndex(count + layerCount);
			}
		}

		mLayerCount = layerCount;
		mLaserActors.getActors().ensureCapacity(mLayerCount);
		allocateLayerActors();
		// Add any necessary actors
	}

	public void allocateLayerActors() {

	}

}
