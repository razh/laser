package com.razh.laser.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.sprites.SpriteActor;

public class MissilePathComponent extends Component {

	private Sprite mPathSprite;

	private final SnapshotArray<Actor> mPathActors;
	private int mSegmentCount;

	public MissilePathComponent() {
		mPathActors = new SnapshotArray<Actor>(true, 2, Actor.class);
		mSegmentCount = 2;

		allocatePathActors();
	}

	@Override
	public void act(float delta) {

	}

	/**
	 * Pre-allocates path actors.
	 */
	public void allocatePathActors() {
		int size = mPathActors.size;

		SpriteActor spriteActor;
		for (int i = size; i < mSegmentCount; i++) {
			spriteActor = new SpriteActor();
			spriteActor.setSprite(mPathSprite);
			spriteActor.setVisible(false);

			mPathActors.add(spriteActor);
		}
	}

	public int getSegmentCount() {
		return mSegmentCount;
	}

	public void setSegmentCount(int segmentCount) {
		mSegmentCount = segmentCount;
		mPathActors.ensureCapacity(mSegmentCount);
	}

	public Sprite getPathSprite() {
		return mPathSprite;
	}

	public void setPathSprite(Sprite sprite) {
		mPathSprite = sprite;
	}
}
