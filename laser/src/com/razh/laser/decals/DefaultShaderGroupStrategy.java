package com.razh.laser.decals;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.razh.laser.DecalActor;

public class DefaultShaderGroupStrategy extends ShaderGroupStrategy {

	public DefaultShaderGroupStrategy() {

	}

	@Override
	public ShaderProgram getGroupShader(int group) {
		return null;
	}

	@Override
	public int decideGroup(Decal decal) {
		return 0;
	}

	public int decideGroup(DecalActor decalActor) {
		return 0;
	}

	@Override
	public void beforeGroup(int group, Array<Decal> contents) {
		return;
	}

	@Override
	public void afterGroup(int group) {
	}

	@Override
	public void beforeGroups() {
	}

	@Override
	public void afterGroups() {
	}

	@Override
	public void beforeActorGroup(int group, Array<DecalActor> contents) {
	}
}
