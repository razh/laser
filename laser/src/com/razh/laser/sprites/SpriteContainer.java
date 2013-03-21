package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.EntityActor;
import com.razh.laser.ShaderStage;

public class SpriteContainer extends EntityActor {

	private final SnapshotArray<Actor> mComponents = new SnapshotArray<Actor>();

	/**
	 * Adds an actor as a component of this container. Actor is not removed from
	 * its original parent group.
	 * @param actor
	 */
	public void addComponent(Actor actor) {
		mComponents.add(actor);
	}

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		draw(spriteBatch, parentAlpha, null);
	}

	/**
	 * Draws components with the same transformation: scale, rotation, and position.
	 */
	public void draw(SpriteBatch spriteBatch, float parentAlpha, ShaderProgram shaderProgram) {
		Actor[] actors = mComponents.begin();

		Actor actor;
		for (int i = 0, n = mComponents.size; i < n; i++) {
			actor = actors[i];

			actor.setRotation(getRotation());
			actor.setScale(getScaleX(), getScaleY());
			actor.setPosition(getX(), getY());

			if (actor instanceof ProceduralSpriteActor) {
				((ProceduralSpriteActor) actor).draw(spriteBatch, parentAlpha, shaderProgram);
			} else {
				actor.draw(spriteBatch, parentAlpha);
			}
		}

		mComponents.end();
	}

	public SnapshotArray<Actor> getComponents() {
		return mComponents;
	}
}
