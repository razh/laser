package com.razh.laser.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.razh.laser.entities.Entity;

/**
 * Statically composed of a PhysicsComponent and a TargetComponent.
 * @author raz
 *
 */
public class MissileComponent extends Component {

	private final PhysicsComponent mPhysicsComponent;
	private final TargetComponent mTargetComponent;

	public MissileComponent() {
		super();

		mPhysicsComponent = new PhysicsComponent();
		mTargetComponent = new TargetComponent();
	}

	@Override
	public void act(float delta) {
		mPhysicsComponent.act(delta);
		mTargetComponent.act(delta);

		Vector2 toTarget = mTargetComponent.getVectorToTarget();
		if (toTarget == null) {
			return;
		}

		mPhysicsComponent.setAcceleration(mTargetComponent.getVectorToTarget());

		Vector2 velocity = mPhysicsComponent.getVelocity();
		getActor().setRotation((float) Math.atan2(velocity.y, velocity.x) * MathUtils.radiansToDegrees);
	}

	@Override
	public void setEntity(Entity entity) {
		super.setEntity(entity);

		mPhysicsComponent.setEntity(entity);
		mTargetComponent.setEntity(entity);
	}

	public PhysicsComponent getPhysicsComponent() {
		return mPhysicsComponent;
	}

	public TargetComponent getTargetComponent() {
		return mTargetComponent;
	}
}
