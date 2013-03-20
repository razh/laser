package com.razh.laser;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ShaderStage extends Stage {

	private final Map<Class<?>, ProceduralSpriteGroup> mGroups;

	public ShaderStage() {
		this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}

	public ShaderStage(float width, float height, boolean stretch) {
		super(width, height, stretch);
		mGroups = new HashMap<Class<?>, ProceduralSpriteGroup>();
	}

	public void addSpriteActor(Actor actor) {
		mGroups.get(actor.getClass()).addActor(actor);
	}

	public void addSpriteGroup(Class<?> type) {
		ProceduralSpriteGroup group = new ProceduralSpriteGroup();
		group.setShaderProgram(Shader.getShaderForType(type));
		mGroups.put(type, group);
		addActor(group);
	}
}
