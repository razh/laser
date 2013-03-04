package com.razh.laser.entities;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Entity {
	private Actor mActor;

	public void act(float delta) {}

	public Actor getActor() {
		return mActor;
	}

	public void setActor(Actor actor) {
		mActor = actor;
	}
}
