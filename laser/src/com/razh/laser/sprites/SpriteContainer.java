package com.razh.laser.sprites;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.ShaderStage;

public class SpriteContainer extends Actor {

	private final SnapshotArray<Actor> components = new SnapshotArray<Actor>();

	/**
	 * Adds an actor as a component of this container. Actor is not removed from
	 * its original parent group.
	 * @param actor
	 */
	public void addComponent(Actor actor) {
		components.add(actor);
	}

	/**
	 * Sets components to have the same scale, rotation, and position.
	 */
	@Override
	public void act(float delta) {
	}

	public void addTo(ShaderStage stage) {
		Actor[] actors = components.begin();

		for (int i = 0, n = components.size; i < n; i++) {
			stage.addActor(actors[i]);
		}

		components.end();
	}
}
