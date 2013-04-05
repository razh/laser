package com.razh.laser.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class PlayerComponent extends PhysicsComponent {

	public float DEFAULT_ANGULAR_VELOCITY = 90.0f * 1e2f;

	private boolean mTurningLeft;
	private boolean mTurningRight;

	@Override
	public void act(float delta) {
		super.act(delta);

		boolean turningLeft = mTurningLeft || Gdx.input.isKeyPressed(Keys.LEFT);
		boolean turningRight = mTurningRight || Gdx.input.isKeyPressed(Keys.RIGHT);

		float angularVelocity = 0.0f;
		if (turningLeft) {
			angularVelocity += DEFAULT_ANGULAR_VELOCITY * delta;
		}

		if (turningRight) {
			angularVelocity += -DEFAULT_ANGULAR_VELOCITY * delta;
		}

		if (!turningLeft && !turningRight) {
			angularVelocity = 0.0f;
		}

		setAngularVelocity(angularVelocity);
	}

	public void turnLeft(boolean turnLeft) {
		mTurningLeft = turnLeft;
		// TODO: Probably a better way to do this.
		mTurningRight = !turnLeft;
	}

	public void turnRight(boolean turnRight) {
		mTurningRight = turnRight;
		mTurningLeft = !turnRight;
	}
}
