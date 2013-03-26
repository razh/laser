package com.razh.laser;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Unlike an ActorContainer, which just holds various Actors together,
 * a TransformActorContainer sets position, rotation, and scale of each
 * child actor on update.
 * @author raz
 *
 */
public class TransformActorContainer extends ActorContainer {

	/**
	 * Sets components to the same transformation: scale, rotation, and position.
	 */
	@Override
	public void act(float delta) {
		super.act(delta);

		SnapshotArray<Actor> actorArray = getActors();
		Actor[] actors = actorArray.begin();

		Actor actor;
		for (int i = 0, n = actorArray.size; i < n; i++) {
			actor = actors[i];

			actor.setRotation(getRotation());
			actor.setScale(getScaleX(), getScaleY());
			actor.setPosition(getX(), getY());
		}

		actorArray.end();
	}
}
