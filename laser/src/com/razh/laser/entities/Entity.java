package com.razh.laser.entities;

import com.razh.laser.MeshActor;

public abstract class Entity {
	private MeshActor mActor;

	public abstract void act(float delta);

	public MeshActor getActor() {
		return mActor;
	}

	public void setActor(MeshActor actor) {
		mActor = actor;
	}
}
