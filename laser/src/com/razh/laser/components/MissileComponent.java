package com.razh.laser.components;

import com.badlogic.gdx.math.MathUtils;
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
		if (actor == null || mTarget == null) {
			return;
		}

		mActorPosition.set(actor.getX(), actor.getY());
		mTargetPosition.set(mTarget.getX(), mTarget.getY());

		setAcceleration(mTargetPosition.sub(mActorPosition));
		actor.setRotation((float) Math.atan2(getVelocityY(), getVelocityX()) * MathUtils.radiansToDegrees + 90.0f);
	}

	public Actor getTarget() {
		return mTarget;
	}

	public void setTarget(Actor actor) {
		mTarget = actor;
	}
}
