package com.razh.laser.decals;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.DefaultGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SortedIntList;
import com.razh.laser.DecalActor;

public class ShaderDecalBatch extends DecalBatch {

	private final SortedIntList<Array<DecalActor>> mGroupList;
	private ShaderGroupStrategy mGroupStrategy;

	public ShaderDecalBatch() {
		super();

		mGroupList = new SortedIntList<Array<DecalActor>>();

		mGroupStrategy = new DefaultShaderGroupStrategy();
		setGroupStrategy(mGroupStrategy);
	}

	@Override
	protected void render() {
		mGroupStrategy.beforeGroups();
		for (SortedIntList.Node<Array<DecalActor>> group : mGroupList) {
			mGroupStrategy.beforeActorGroup(group.index, group.value);
		}
		mGroupStrategy.afterGroups();
	}

	protected void render(ShaderProgram shader, Array<DecalActor> decalActors) {

	}

	@Override
	public void add(Decal decal) {

	}

	public GroupStrategy getGroupStrategy() {
		return mGroupStrategy;
	}
}
