package com.razh.laser;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;

public class DecalGroup extends Group {

	/**
	 * Draws all decals (sends them to decalBatch).
	 * @param decalBatch
	 */
	public void draw(DecalBatch decalBatch) {
		SnapshotArray<Actor> children = getChildren();
		Actor[] actors = children.begin();

		Actor child;
		for (int i = 0, n = children.size; i < n; i++) {
			child = actors[i];

			if (!child.isVisible()) {
				continue;
			}

			((DecalActor) child).draw(decalBatch);
		}

		children.end();
		decalBatch.flush();
	}

	/**
	 * Draws decals behind the stage (negative z-coordinate).
	 * @param decalBatch
	 */
	public void drawBehind(DecalBatch decalBatch) {
		SnapshotArray<Actor> children = getChildren();
		Actor[] actors = children.begin();

		DecalActor child;
		for (int i = 0, n = children.size; i < n; i++) {
			child = (DecalActor) actors[i];

			if (!child.isVisible()) {
				continue;
			}

			if (child.getZ() < 0) {
				child.draw(decalBatch);
			}
		}

		children.end();
		decalBatch.flush();
	}

	/**
	 * Draws decals after the stage (zero or positive z-coordinate).
	 * @param decalBatch
	 */
	public void drawAfter(DecalBatch decalBatch) {
		SnapshotArray<Actor> children = getChildren();
		Actor[] actors = children.begin();

		DecalActor child;
		for (int i = 0, n = children.size; i < n; i++) {
			child = (DecalActor) actors[i];

			if (!child.isVisible()) {
				continue;
			}

			if (child.getZ() >= 0) {
				child.draw(decalBatch);
			}
		}

		children.end();
		decalBatch.flush();
	}

	/*
	 * Do nothing if given a SpriteBatch.
	 * @see com.badlogic.gdx.scenes.scene2d.Group#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch, float)
	 */
	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {}

	/**
	 * Only add DecalActors to DecalGroup.
	 */
	@Override
	public void addActor(Actor actor) {
		if (!(actor instanceof DecalActor)) {
			return;
		}

		super.addActor(actor);
	}
}
