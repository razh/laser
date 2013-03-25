package com.razh.laser.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MissileComponent extends PhysicsComponent {

	private final Vector2 mActorPosition;
	private final Vector2 mTargetPosition;

	private Actor mTarget;

	public MissileComponent() {
		mActorPosition = new Vector2();
		mTargetPosition = new Vector2();
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		Actor actor = getActor();
		mActorPosition.set(actor.getX(), actor.getY());
		mTargetPosition.set(mTarget.getX(), mTarget.getY());
	}

	public Actor getTarget() {
		return mTarget;
	}

	public void setTarget(Actor actor) {
		mTarget = actor;
	}
}
