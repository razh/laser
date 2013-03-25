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

	public Component getComponentOfType(Class<?> type) {
		if (!Component.class.isAssignableFrom(type)) {
			return null;
		}

		Component[] components = mComponents.begin();

		Component component;
		for (int i = 0, n = mComponents.size; i < n; i++) {
			component = components[i];
			if (component.getClass() == type) {
				mComponents.end();
				return component;
			}
		}

		mComponents.end();

		return null;
	}

	public void addComponent(Component component) {
		component.setEntity(this);
		mComponents.add(component);
	}
}
