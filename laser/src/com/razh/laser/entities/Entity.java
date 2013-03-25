package com.razh.laser.entities;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.components.Component;

public class Entity {

	private Actor mActor;
	private SnapshotArray<Component> mComponents = new SnapshotArray<Component>(true, 4, Component.class);

	public void act(float delta) {
		Component[] components = mComponents.begin();

		for (int i = 0, n = mComponents.size; i < n; i++) {
			components[i].act(delta);
		}

		mComponents.end();
	}

	public Actor getActor() {
		return mActor;
	}

	public void setActor(Actor actor) {
		mActor = actor;
	}

	public SnapshotArray<Component> getComponents() {
		return mComponents;
	}

	public void addComponent(Component component) {
	}
}
