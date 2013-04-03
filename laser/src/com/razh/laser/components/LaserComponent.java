package com.razh.laser.components;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.ActorContainer;
import com.razh.laser.math.Range;
import com.razh.laser.sprites.SpriteActor;
import com.razh.laser.sprites.SpriteActor.Origin;

public class LaserComponent extends TransformComponent {

	private static final Vector2 mTempVector = new Vector2();
	private static final Vector2 mTempVector2 = new Vector2();

	private Sprite mLaserSprite;
	private final Color mLaserColor;

	private final Range mBeamWidth;
	private float mBeamLength;

	private final Range mSegmentWidth;
	private final Range mSegmentLength;

	private final Range mSegmentVelocity;

	private int mLayerCount;

	private final ActorContainer mLaserActors;
	private final Deque<Actor> mLaserPathActors;

	private final ArrayList<Float> mSegmentPositionsX;
	private final ArrayList<Float> mSegmentPositionsY;
	private final ArrayList<Float> mSegmentLengths;
	private final ArrayList<Float> mSegmentVelocities;

	public LaserComponent() {
		mLaserColor = new Color();
		mLaserActors = new ActorContainer();
		mLaserPathActors = new LinkedList<Actor>();

		mBeamWidth = new Range();

		mSegmentWidth = new Range();
		mSegmentLength = new Range();

		mSegmentVelocity = new Range();

		mSegmentPositionsX = new ArrayList<Float>();
		mSegmentPositionsY = new ArrayList<Float>();
		mSegmentLengths = new ArrayList<Float>();
		mSegmentVelocities = new ArrayList<Float>();
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		Actor actor = getActor();
		float actorX = actor.getX();
		float actorY = actor.getY();
		mTempVector.set(actor.getX(), actor.getY());

		float rotation = getRotation();
		float cos = (float) Math.cos(rotation * MathUtils.degreesToRadians);
		float sin = (float) Math.sin(rotation * MathUtils.degreesToRadians);

		SnapshotArray<Actor> actorArray = mLaserActors.getActors();
		Actor[] actors = actorArray.begin();

		Actor segment;
		float x, y;
		for (int i = 0, n = actorArray.size; i < n; i++) {
			segment = actors[i];
			// If the current segment length is less than the desired segment length,
			// increase the length by velocity * dt.
			if (segment.getWidth() < mSegmentLengths.get(i)) {
				segment.setWidth(segment.getWidth() + mSegmentVelocities.get(i) * delta);
			} else {
				// Otherwise move the segment along the beam.
				mSegmentPositionsX.set(i, mSegmentPositionsX.get(i) + mSegmentVelocities.get(i) * delta);
			}

			// Reset if past beam length.
			if (mSegmentPositionsX.get(i) > mBeamLength) {
				segment.setWidth(0.0f);
				mSegmentPositionsX.set(i, 0.0f);
			}

			// Transform position.
			x = actorX + cos * mSegmentPositionsX.get(i) + sin * mSegmentPositionsY.get(i);
			y = actorY + sin * mSegmentPositionsX.get(i) + cos * mSegmentPositionsY.get(i);

			segment.setPosition(x, y);
			segment.setRotation(rotation);
		}

		actorArray.end();
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

				// Remove positions, lengths, and velocities.
				mSegmentPositionsX.remove(mSegmentPositionsX.size() - 1);
				mSegmentPositionsY.remove(mSegmentPositionsY.size() - 1);
				mSegmentLengths.remove(mSegmentLengths.size() - 1);
				mSegmentVelocities.remove(mSegmentVelocities.size() - 1);

				count--;
			}
		}

		mLayerCount = layerCount;
		// Add any necessary actors.
		mLaserActors.getActors().ensureCapacity(mLayerCount);

		mSegmentPositionsX.ensureCapacity(mLayerCount);
		mSegmentPositionsY.ensureCapacity(mLayerCount);
		mSegmentLengths.ensureCapacity(mLayerCount);
		mSegmentVelocities.ensureCapacity(mLayerCount);

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
			spriteActor.setWidth(0);
			spriteActor.setHeight(mSegmentWidth.random());
			spriteActor.setColor(mLaserColor);
			spriteActor.setBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

			actors.add(spriteActor);

			mSegmentPositionsX.add(MathUtils.random(mBeamLength));
			mSegmentPositionsY.add(mBeamWidth.random());
			mSegmentLengths.add(mSegmentLength.random());
			mSegmentVelocities.add(mSegmentVelocity.random());
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

	public Color getLaserColor() {
		return mLaserColor;
	}

	public Range getBeamWidth() {
		return mBeamWidth;
	}

	public float getBeamLength() {
		return mBeamLength;
	}

	public void setBeamLength(float beamLength) {
		mBeamLength = beamLength;
	}

	public Range getSegmentWidth() {
		return mSegmentWidth;
	}

	public Range getSegmentLength() {
		return mSegmentLength;
	}

	public Range getSegmentVelocity() {
		return mSegmentVelocity;
	}

	public ActorContainer getLaserActors() {
		return mLaserActors;
	}
}
