package com.razh.laser.components;

public class PlayerComponent extends TransformComponent {

	private final LaserComponent mLaserComponent;

	public PlayerComponent() {
		mLaserComponent = new LaserComponent();
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		mLaserComponent.setPosition(getPosition());
		mLaserComponent.setRotation(getRotation());
	}

	public LaserComponent getLaserComponent() {
		return mLaserComponent;
	}
}
