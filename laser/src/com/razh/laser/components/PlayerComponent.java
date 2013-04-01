package com.razh.laser.components;

public class PlayerComponent extends PhysicsComponent {

	private final LaserComponent mLaserComponent;

	public PlayerComponent() {
		mLaserComponent = new LaserComponent();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}
}
