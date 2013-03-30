package com.razh.laser.components;

import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.ActorContainer;

/**
 * A FlockComponent consists of a group of actors which exhibit
 * flocking behavior.
 * @author raz
 *
 */
public class FlockComponent extends Component {

	private final ActorContainer mFlockActors;
	private final SnapshotArray<BoidComponent> mBoids;

	public FlockComponent() {
		mFlockActors = new ActorContainer();
		mBoids = new SnapshotArray(true, 2, BoidComponent.class);
	}

	@Override
	public void act(float delta) {
	}
}
