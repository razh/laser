package com.razh.laser;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.razh.laser.entities.Entity;

public class EntityActor extends Actor {

	private Entity mEntity;

	public EntityActor() {
		super();
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (hasEntity()) {
			getEntity().act(delta);
		}
	}

	public Entity getEntity() {
		return mEntity;
	}

	public void setEntity(Entity entity) {
		mEntity = entity;
	}

	public boolean hasEntity() {
		return mEntity != null;
	}
}
