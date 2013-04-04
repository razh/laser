package com.razh.laser;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.math.Range;

public class Starfield extends ActorContainer {

	private TextureRegion[] mStars;
	private int mStarCount;

	private final Range mStarWidth;
	private final Range mStarHeight;
	private final Range mStarAlpha;

	// Do we want to have star movement?

	public Starfield(int starCount) {
		mStarWidth = new Range();
		mStarHeight = new Range();
		mStarAlpha = new Range();
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

	public void allocateStarActors() {
		SnapshotArray<Actor> actors = getActors();
		int size = actors.size;

		DecalActor actor;
		for (int i = size; i < mStarCount; i++) {
			actor = new DecalActor();

			// Set random sprite.
			actor.setDecal(Decal.newDecal(mStars[MathUtils.random(mStars.length)]));
			actor.setWidth(mStarWidth.random());
			actor.setHeight(mStarHeight.random());

			addActor(actor);
		}
	}

	public Range getStarWidth() {
		return mStarWidth;
	}

	public Range getStarHeight() {
		return mStarHeight;
	}

	public Range getStarAlpha() {
		return mStarAlpha;
	}
}
