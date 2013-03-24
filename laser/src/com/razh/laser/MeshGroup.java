package com.razh.laser;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.SnapshotArray;

public class MeshGroup extends Group {

	private ShaderProgram mShaderProgram;

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {}

	public void draw(ShaderProgram shaderProgram) {
		if (mShaderProgram != shaderProgram) {
			setShaderProgram(shaderProgram);
		}

		drawChildren();
	}

	public void draw() {
		if (mShaderProgram == null) {
			return;
		}

		drawChildren();
	}

	public void drawChildren() {
		SnapshotArray<Actor> children = getChildren();
		Actor[] actors = children.begin();

		Actor child;
		for (int i = 0, n = children.size; i < n; i++) {
			child = actors[i];

			if (!child.isVisible()) {
				return;
			}

			if (child instanceof MeshActor) {
				((MeshActor) child).draw(mShaderProgram);
			} else if (child instanceof MeshGroup) {
				((MeshGroup) child).draw(mShaderProgram);
			}
		}

		children.end();
	}

	@Override
	public void act(float delta) {
		/*
		 * The original order of Group.act() has the Group Actor acting first,
		 * and then its children Actors acting. If a child Actor is removed, this results
		 * in a null reference to the given Actor. The order is reversed here as
		 * a fix.
		 */

		// Actor.act().
		for (int i = 0, n = getActions().size; i < n; i++) {
			Action action = getActions().get(i);
			if (action.act(delta)) {
				getActions().removeIndex(i);
				action.setActor(null);
				i--;
				n--;
			}
		}

		// Group.act().
		Actor[] actors = getChildren().begin();
		for (int i = 0; i < getChildren().size; i++) {
			actors[i].act(delta);
		}
		getChildren().end();
	}

	@Override
	public void addAction(Action action) {
		SnapshotArray<Actor> children = getChildren();
		Actor[] actors = children.begin();

		for (int i = 0, n = children.size; i < n; i++) {
			actors[i].addAction(ActionFactory.createAction(action));
		}

		children.end();
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		// The same as Group.hit(), except we do not transform coordinates.
		if (touchable && getTouchable() == Touchable.disabled) {
			return null;
		}

		SnapshotArray<Actor> children = getChildren();
		Actor[] actors = children.begin();
		for (int i = 0, n = children.size; i < n; i++) {
			Actor child = actors[i];
			if (!child.isVisible()) {
				continue;
			}

			Actor hit = child.hit(x, y, touchable);

			if (hit != null) {
				children.end();
				return hit;
			}
		}
		children.end();

		return null;
	}

	public Actor getActorAt(int index) {
		SnapshotArray<Actor> children = getChildren();
		if (0 <= index && index < children.size) {
			Actor[] actors = children.begin();
			Actor actor = actors[index];
			children.end();

			return actor;
		}

		return null;
	}

	public ShaderProgram getShaderProgram() {
		return mShaderProgram;
	}

	public void setShaderProgram(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;
	}
}
