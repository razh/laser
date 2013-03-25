package com.razh.laser.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.razh.laser.entities.Entity;

public abstract class Component {
	private Entity mEntity;

	public abstract void act(float delta);

	public Entity getEntity() {
		return mEntity;
	}

	public void setEntity(Entity entity) {
		mEntity = entity;
	}

	public Actor getActor() {
		return mEntity.getActor();
	}
}
