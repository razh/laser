package com.razh.laser;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.sprites.ProceduralSpriteActor;
import com.razh.laser.sprites.ProceduralSpriteGroup;
import com.razh.laser.sprites.SpriteContainer;

public class ShaderStage extends Stage {

	private final Map<Class<?>, ProceduralSpriteGroup> mGroups;

	public ShaderStage() {
		this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}

	public ShaderStage(float width, float height, boolean stretch) {
		super(width, height, stretch);

		setCamera(new PerspectiveCamera(90.0f, width, height));
		getCamera().position.set(0, 0, 400f);
		getCamera().far = 1e5f;
		getCamera().lookAt(0.0f, 0.0f, 0.0f);

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

	public void addSpriteContainer(SpriteContainer container) {
		SnapshotArray<Actor> components = container.getComponents();
		Actor[] actors = components.begin();

		Actor actor;
		for (int i = 0, n = components.size; i < n; i++) {
			actor = actors[i];

			if (actor instanceof ProceduralSpriteActor) {
				addSpriteActor(actor);
			} else {
				addActor(actor);
			}
		}

		components.end();
	}
}
