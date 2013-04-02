package com.razh.laser.components;

import java.util.Deque;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.ActorContainer;
import com.razh.laser.math.Range;
import com.razh.laser.sprites.SpriteActor;
import com.razh.laser.sprites.SpriteActor.Origin;

public class LaserComponent extends TransformComponent {

	private static final Vector2 mTempVector = new Vector2();

	private Sprite mLaserSprite;
	private final Color mLaserColor;

	private final Range mSegmentWidth;
	private final Range mSegmentLength;

	private final Range mSegmentVelocity;
	private final Range mSegmentInterval;

	private int mLayerCount;

	private final ActorContainer mLaserActors;
	private final Deque<Actor> mLaserPathActors;

	public LaserComponent() {
		mLaserColor = new Color();
		mLaserActors = new ActorContainer();
		mLaserPathActors = new LinkedList<Actor>();

		mSegmentWidth = new Range();
		mSegmentLength = new Range();

		mSegmentVelocity = new Range();
		mSegmentInterval = new Range();
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		Actor actor = getActor();
		mTempVector.set(actor.getX(), actor.getY());
	}

	public int getLayerCount() {
		return mLayerCount;
	}

	public void setLayerCount(int layerCount) {
		// Remove unwanted actors.
		if (mLayerCount > layerCount) {
			int count = mLayerCount - layerCount;
			Actor removedActor;
			while (count > 0) {
				removedActor = mLaserActors.getActors().removeIndex(count + layerCount - 1);
				removedActor.remove();
				count--;
			}
		}

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
			spriteActor.setWidth(mSegmentLength.random());
			spriteActor.setHeight(mSegmentWidth.random());

			actors.add(spriteActor);
		}
	}

	public Sprite getLaserSprite() {
		return mLaserSprite;
	}

	public void setLaserSprite(Sprite sprite) {
		mLaserSprite = sprite;

		SnapshotArray<Actor> laserActorArray = mLaserActors.getActors();
		Actor[] laserActors = laserActorArray.begin();

		Actor laserActor;
		for (int i = 0, n = laserActorArray.size; i < n; i++) {
			laserActor = laserActors[i];

			if (laserActor instanceof SpriteActor) {
				((SpriteActor) laserActor).setSprite(mLaserSprite);
			}
		}

		laserActorArray.end();
	}

	/**
	 * Segment width.
	 */
	public Range getSegmentWidth() {
		return mSegmentWidth;
	}

	public void setSegmentWidth(float startWidth, float endWidth) {
		mSegmentWidth.set(startWidth, endWidth);
	}

	/**
	 * Segment length.
	 */
	public Range getSegmentLength() {
		return mSegmentLength;
	}

	public void setSegmentLength(float startLength, float endLength) {
		mSegmentLength.set(startLength, endLength);
	}

	/**
	 * Segment velocity.
	 */
	public Range getSegmentVelocity() {
		return mSegmentVelocity;
	}

	public void setSegmentVelocity(float startVelocity, float endVelocity) {
		mSegmentVelocity.set(startVelocity, endVelocity);
	}

	/**
	 * Segment interval. Timings at which new segments are spawned.
	 */
	public Range getSegmentInterval() {
		return mSegmentInterval;
	}

	public void setSegmentInterval(float startInterval, float endInterval) {
		mSegmentInterval.set(startInterval, endInterval);
	}

	public ActorContainer getLaserActors() {
		return mLaserActors;
	}
}
