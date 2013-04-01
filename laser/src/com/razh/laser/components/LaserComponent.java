package com.razh.laser.components;

import java.util.Deque;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.ActorContainer;
import com.razh.laser.sprites.SpriteActor;
import com.razh.laser.sprites.SpriteActor.Origin;

public class LaserComponent extends TransformComponent {

	private Sprite mLaserSprite;
	private final Color mLaserColor;

	private float mLaserWidth;
	private float mLaserHeight;

	private int mLayerCount;

	private final ActorContainer mLaserActors;
	private final Deque<Actor> mLaserPathActors;

	public LaserComponent() {
		mLaserColor = new Color();
		mLaserActors = new ActorContainer();
		mLaserPathActors = new LinkedList<Actor>();
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
			Actor removedActor;
			while (count-- > 0) {
				removedActor = mLaserActors.getActors().removeIndex(count + layerCount);
				removedActor.remove();
			}
		}
		System.out.println(layerCount + ", " + mLaserActors.getActors().size);

		mLayerCount = layerCount;
		// Add any necessary actors.
		mLaserActors.getActors().ensureCapacity(mLayerCount);
		allocateLayerActors();
	}

	public void allocateLayerActors() {
		SnapshotArray<Actor> actors = mLaserActors.getActors();
		int size = actors.size;

		SpriteActor spriteActor;
		for (int i = size; i < mLayerCount; i++) {
			spriteActor = new SpriteActor();
			spriteActor.setSprite(mLaserSprite);
			spriteActor.setOrigin(Origin.LEFT);
			spriteActor.setWidth(mLaserWidth);
			spriteActor.setHeight(mLaserHeight);

			actors.add(spriteActor);
		}
	}

	public float getLaserWidth() {
		return mLaserWidth;
	}

	public void setLaserWidth(float laserWidth) {
		mLaserWidth = laserWidth;
	}

	public float getLaserHeight() {
		return mLaserHeight;
	}

	public void setLaserHeight(float laserHeight) {
		mLaserHeight = laserHeight;
	}
}
