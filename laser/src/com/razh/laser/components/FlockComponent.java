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

	private final ActorContainer mBoidActors;
	private final SnapshotArray<BoidComponent> mBoidComponents;

	public FlockComponent() {
		mBoidActors = new ActorContainer();
		mBoidComponents = new SnapshotArray<BoidComponent>(true, 2, BoidComponent.class);
	}

	@Override
	public void act(float delta) {
		BoidComponent[] components = mBoidComponents.begin();

		for (int i = 0, n = mBoidComponents.size; i < n; i++) {
			components[i].flock(mBoidComponents);
		}

		mBoidComponents.end();
	}

	public SnapshotArray<BoidComponent> getBoidComponents() {
		return mBoidComponents;
	}

	public ActorContainer getBoidActors() {
		return mBoidActors;
	}

	/**
	 * Adds the given BoidComponent and its respective Actor.
	 * @param boid
	 */
	public void addBoid(BoidComponent boid) {
		mBoidActors.addActor(boid.getActor());
		mBoidComponents.add(boid);
	}
}
