package com.razh.laser.components;

import com.razh.laser.ActorContainer;

/**
 * A FlockComponent consists of a group of actors which exhibit
 * flocking behavior.
 * @author raz
 *
 */
public class FlockComponent extends Component {

	private final ActorContainer mFlockActors;

	public FlockComponent() {
		mFlockActors = new ActorContainer();
	}

	@Override
	public void act(float delta) {

	}
}
