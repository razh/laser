package com.razh.laser.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TargetComponent extends Component {

	private static final Vector2 mActorPosition = new Vector2();
	private static final Vector2 mTargetPosition = new Vector2();

	private Actor mTarget;

	@Override
	public void act(float delta) {}

	public Actor getTarget() {
		return mTarget;
	}

	public void setTarget(Actor actor) {
		mTarget = actor;
	}

	public Vector2 getVectorToTarget() {
		Actor actor = getActor();
		if (actor == null || mTarget == null) {
			return null;
		}

		mActorPosition.set(actor.getX(), actor.getY());
		mTargetPosition.set(mTarget.getX(), mTarget.getY());
		return mTargetPosition.sub(mActorPosition);
	}
}
