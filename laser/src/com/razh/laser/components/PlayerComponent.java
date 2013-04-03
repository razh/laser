package com.razh.laser.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class PlayerComponent extends PhysicsComponent {

	public float DEFAULT_ANGULAR_VELOCITY = 90.0f * 1e2f;

	@Override
	public void act(float delta) {
		System.out.println(delta * 1e2f);
		super.act(delta);

		boolean turningLeft = Gdx.input.isKeyPressed(Keys.LEFT);
		boolean turningRight = Gdx.input.isKeyPressed(Keys.RIGHT);

		if (turningLeft) {
			setAngularVelocity(DEFAULT_ANGULAR_VELOCITY * delta);
			System.out.println("LEFT");
		}

		if (turningRight) {
			setAngularVelocity(-DEFAULT_ANGULAR_VELOCITY * delta);
		}

		if (!turningLeft && !turningRight) {
			setAngularVelocity(0.0f);
		}
	}
}
