package com.razh.laser;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Lightweight version of a Group.
 * @author raz
 *
 */
public class ActorContainer extends EntityActor {

	private final SnapshotArray<Actor> mActors = new SnapshotArray<Actor>(true, 4, Actor.class);

	/**
	 * Sets components to the same transformation: scale, rotation, and position.
	 */
	@Override
	public void act(float delta) {
		Actor[] actors = mActors.begin();

		Actor actor;
		for (int i = 0, n = mActors.size; i < n; i++) {
			actor = actors[i];

			actor.setRotation(getRotation());
			actor.setScale(getScaleX(), getScaleY());
			actor.setPosition(getX(), getY());
		}

		mActors.end();
		super.act(delta);
	}

	/**
	 * Adds an actor as to this container. Actor is not removed from
	 * its original parent group.
	 * @param actor
	 */
	public void addActor(Actor actor) {
		mActors.add(actor);
	}

	public SnapshotArray<Actor> getActors() {
		return mActors;
	}

	@Override
	public boolean remove() {
		if (!super.remove()) {
			return false;
		}

		// Remove all child actors as well.
		Actor[] actors = mActors.begin();

		for (int i = 0, n = mActors.size; i < n; i++) {
			actors[i].remove();
		}

		mActors.end();

		return true;
	}
}
