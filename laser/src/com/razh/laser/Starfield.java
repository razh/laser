package com.razh.laser;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.math.Range;

public class Starfield extends ActorContainer {

	private TextureRegion[] mStarTextures;
	private int mStarCount;

	private final Range mStarSize;
	private final Range mStarAlpha;

	private final Range mStarPositionX;
	private final Range mStarPositionY;
	private final Range mStarPositionZ;

	// Do we want to have star movement?

	public Starfield(TextureRegion[] starTextures, int starCount) {
		mStarSize = new Range();
		mStarAlpha = new Range();

		mStarPositionX = new Range();
		mStarPositionY = new Range();
		mStarPositionZ = new Range();

		mStarTextures = starTextures;
		setStarCount(starCount);
	}

	public int getStarCount() {
		return mStarCount;
	}

	public void setStarCount(int starCount) {
		trim(starCount);

		mStarCount = starCount;
		// Add any necessary actors.
		getActors().ensureCapacity(mStarCount);

		allocateStarActors();
	}

	/**
	 * Randomly positions, resizes, and rotates all actors.
	 */
	public void randomize() {
		SnapshotArray<Actor> actorArray = getActors();
		Actor[] actors = actorArray.begin();

		DecalActor actor;
		float size;
		for (int i = 0, n = actorArray.size; i < n; i++) {
			actor = (DecalActor) actors[i];

			size = mStarSize.random();
			actor.setWidth(size);
			actor.setHeight(size);
			actor.setPosition(mStarPositionX.random(),
			                  mStarPositionY.random(),
			                  mStarPositionZ.random());
			actor.setRotation(MathUtils.random(360.0f),
			                  MathUtils.random(360.0f),
			                  MathUtils.random(360.0f));
		}

		actorArray.end();
	}

	public void allocateStarActors() {
		SnapshotArray<Actor> actors = getActors();
		int size = actors.size;

		DecalActor actor;
		for (int i = size; i < mStarCount; i++) {
			actor = new DecalActor();

			// Set random sprite.
			actor.setDecal(Decal.newDecal(mStarTextures[MathUtils.random(mStarTextures.length - 1)], true));
			actor.setBillboard(true);

			addActor(actor);
		}
	}

	public Range getStarSize() {
		return mStarSize;
	}

	public Range getStarAlpha() {
		return mStarAlpha;
	}

	public Range getStarPositionX() {
		return mStarPositionX;
	}

	public Range getStarPositionY() {
		return mStarPositionY;
	}

	public Range getStarPositionZ() {
		return mStarPositionZ;
	}
}
