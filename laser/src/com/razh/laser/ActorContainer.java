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

	public void trim(int newCount) {
		// Remove unwanted actors.
		if (getActors().size > newCount) {
			int count = getActors().size - newCount;
			Actor removedActor;
			while (count > 0) {
				removedActor = getActors().removeIndex(count + newCount - 1);
				removedActor.remove();

				count--;
			}
		}
	}
}
