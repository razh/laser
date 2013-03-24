package com.razh.laser.sprites;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.EntityActor;

public class SpriteContainer extends EntityActor {

	private final SnapshotArray<Actor> mComponents = new SnapshotArray<Actor>(Actor.class);

	/**
	 * Sets components to the same transformation: scale, rotation, and position.
	 */
	@Override
	public void act(float delta) {
		Actor[] actors = mComponents.begin();

		Actor actor;
		for (int i = 0, n = mComponents.size; i < n; i++) {
			actor = actors[i];

			actor.setRotation(getRotation());
			actor.setScale(getScaleX(), getScaleY());
			actor.setPosition(getX(), getY());
		}

		mComponents.end();
		super.act(delta);
	}

	/**
	 * Adds an actor as a component of this container. Actor is not removed from
	 * its original parent group.
	 * @param actor
	 */
	public void addComponent(Actor actor) {
		mComponents.add(actor);
	}

	public SnapshotArray<Actor> getComponents() {
		return mComponents;
	}

	@Override
	public boolean remove() {
		if (!super.remove()) {
			return false;
		}

		// Remove components as well.
		Actor[] actors = mComponents.begin();

		for (int i = 0, n = mComponents.size; i < n; i++) {
			actors[i].remove();
		}

		mComponents.end();

		return true;
	}
}
