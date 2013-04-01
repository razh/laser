package com.razh.laser.components;

import com.badlogic.gdx.math.Vector2;

public class TransformComponent extends Component {

	private final Vector2 mPosition;
	private float mRotation;

	public TransformComponent() {
		mPosition = new Vector2();
	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub

	}

	/**
	 * Position.
	 */
	public float getX() {
		return mPosition.x;
	}

	public void setX(float x) {
		mPosition.x = x;
	}

	public float getY() {
		return mPosition.y;
	}

	public void setY(float y) {
		mPosition.y = y;
	}

	public Vector2 getPosition() {
		return mPosition;
	}

	public void setPosition(Vector2 position) {
		mPosition.set(position);
	}

	public void setPosition(float x, float y) {
		mPosition.set(x, y);
	}

	/**
	 * Rotation.
	 */
	public float getRotation() {
		return mRotation;
	}

	public void setRotation(float rotation) {
		mRotation = rotation;
	}

	public void rotate(float angle) {
		setRotation(getRotation() + angle);
	}
}
