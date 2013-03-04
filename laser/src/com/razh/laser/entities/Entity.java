package com.razh.laser.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.razh.laser.LaserGame;

public class Entity {
	private Actor mActor;
	private Body mBody;

	public void act(float delta) {
		if (mBody == null) {
			return;
		}

		Vector2 position = mBody.getPosition();
		position.mul(LaserGame.PTM_RATIO);
		mActor.setPosition(position.x, position.y);
		mActor.setRotation(mBody.getAngle());
	}

	public Actor getActor() {
		return mActor;
	}

	public void setActor(Actor actor) {
		mActor = actor;
	}

	public Body getBody() {
		return mBody;
	}

	public void setBody(Body body) {
		mBody = body;
		body.setUserData(this);
	}

	public void setPosition(float x, float y) {
		mBody.setTransform(x / LaserGame.PTM_RATIO, y * LaserGame.PTM_RATIO, mBody.getAngle());
		mActor.setPosition(x, y);
	}
}
