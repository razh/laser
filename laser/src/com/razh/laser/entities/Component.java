package com.razh.laser.entities;

public abstract class Component {
	private Entity mEntity;

	public abstract void act(float delta);

	public Entity getEntity() {
		return mEntity;
	}

	public void setEntity(Entity entity) {
		mEntity = entity;
	}
}
