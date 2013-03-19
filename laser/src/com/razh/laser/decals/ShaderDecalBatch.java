package com.razh.laser.decals;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;
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
		ShaderProgram shader;
		for (SortedIntList.Node<Array<DecalActor>> group : mGroupList) {
			mGroupStrategy.beforeActorGroup(group.index, group.value);
			shader = mGroupStrategy.getGroupShader(group.index);
			render(shader, group.value);
			mGroupStrategy.afterGroup(group.index);
		}
		mGroupStrategy.afterGroups();
	}

	/**
	 * Copy of DecalBatch->render(), with an Array of DecalActors passed in instead.
	 * @param shader
	 * @param decalActors
	 */
	protected void render(ShaderProgram shader, Array<DecalActor> decalActors) {
		DecalMaterial lastMaterial = null;
		int idx = 0;
		for (DecalActor decalActor : decalActors) {
			if (lastMaterial == null || !lastMaterial.equals(decalActor.getDecal().getMaterial())) {
				if (idx > 0) {
					flush(shader, idx);
					idx = 0;
				}

				decalActor.getDecal().getMaterial().set();

			}
		}
	}

	@Override
	public void add(Decal decal) {

	}

	public GroupStrategy getGroupStrategy() {
		return mGroupStrategy;
	}
}
