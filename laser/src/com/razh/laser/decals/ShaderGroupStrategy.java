package com.razh.laser.decals;

import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.utils.Array;
import com.razh.laser.DecalActor;

public abstract class ShaderGroupStrategy implements GroupStrategy {

	/**
	 * Invoked directly before rendering the contents of a group.
	 * Used instead of beforeGroup() in order to let us pass in an Array of DecalActors.
	 * @param group
	 * @param contents List of entries
	 */
	public abstract void beforeActorGroup(int group, Array<DecalActor> contents);
}
