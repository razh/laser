package com.razh.laser.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.ActorContainer;
import com.razh.laser.sprites.SpriteActor;

public class MissilePathComponent extends Component {

	private Sprite mPathSprite;

	private final ActorContainer mPathActors;

	private int mSegmentCount;

	private float mPathWidth;
	private float mSegmentLength;

	public MissilePathComponent() {
		mPathActors = new ActorContainer();
		mSegmentCount = 2;

		mPathWidth = 10.0f;
		mSegmentLength = 20.0f;

		allocatePathActors();
	}

	@Override
	public void act(float delta) {}

	/**
	 * Pre-allocates path actors.
	 */
	public void allocatePathActors() {
		SnapshotArray<Actor> actors = mPathActors.getActors();
		int size = actors.size;

		SpriteActor spriteActor;
		for (int i = size; i < mSegmentCount; i++) {
			spriteActor = new SpriteActor();
			spriteActor.setSprite(mPathSprite);
			spriteActor.setVisible(false);

			actors.add(spriteActor);
		}
	}

	public Actor getPathActorAtIndex(int index) {
		SnapshotArray<Actor> pathActors = mPathActors.getActors();
		if (pathActors.size <= index) {
			return null;
		}

		Actor[] actors = pathActors.begin();
		Actor actor = actors[index];
		pathActors.end();

		return actor;
	}

	public Actor getFirstPathActor() {
		return getPathActorAtIndex(0);
	}

	public Actor getLastPathActor() {
		return getPathActorAtIndex(mPathActors.getActors().size - 1);
	}

	public ActorContainer getPathActors() {
		return mPathActors;
	}

	public int getSegmentCount() {
		return mSegmentCount;
	}

	public void setSegmentCount(int segmentCount) {
		mSegmentCount = segmentCount;
		mPathActors.getActors().ensureCapacity(mSegmentCount);
	}

	public Sprite getPathSprite() {
		return mPathSprite;
	}

	public void setPathSprite(Sprite sprite) {
		mPathSprite = sprite;
	}
}
