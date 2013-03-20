package com.razh.laser.sprites;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;

public class SpriteContainer extends Actor {

	private final SnapshotArray<Actor> components = new SnapshotArray<Actor>();

	/**
	 * Adds an actor as a component of this container. Actor is not removed from
	 * its original parent group.
	 * @param actor
	 */
	public void addActor(Actor actor) {
		actor.remove();
		components.add(actor);
	}

	/**
	 * Sets components to have the same scale, rotation, and position.
	 */
	@Override
	public void act(float delta) {
	}

	public void addTo(ShaderStage stage) {

	}
}
