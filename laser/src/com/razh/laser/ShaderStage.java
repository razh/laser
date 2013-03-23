package com.razh.laser;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.sprites.ProceduralSpriteActor;
import com.razh.laser.sprites.SpriteContainer;

public class ShaderStage extends Stage {

	private final Map<Class<?>, ShaderGroup> mGroups;

	// Allows us to set colors with actions.
	private Actor mColorActor;

	public ShaderStage() {
		this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}

	public ShaderStage(float width, float height, boolean stretch) {
		super(width, height, stretch);

		setCamera(new PerspectiveCamera(90.0f, width, height));
		getCamera().position.set(0, 0, 400f);
		getCamera().far = 1e5f;
		getCamera().lookAt(0.0f, 0.0f, 0.0f);

		mGroups = new HashMap<Class<?>, ShaderGroup>();

		mColorActor = new Actor();
		addActor(mColorActor);
	}

	@Override
	public void addActor(Actor actor) {
		ShaderGroup group = mGroups.get(actor.getClass());
		// Add if the group exists.
		if (group != null) {
			group.addActor(actor);
		} else {
			// Otherwise, create a new group.
			addShaderGroup(actor.getClass()).addActor(actor);
		}
	}

	/**
	 * Create ShaderGroup for the given type. Returns it for chaining.
	 * @param type
	 * @return ShaderGroup
	 */
	public ShaderGroup addShaderGroup(Class<?> type) {
		ShaderGroup group = new ShaderGroup();
		group.setShaderProgram(Shader.getShaderForType(type));

		mGroups.put(type, group);
		super.addActor(group);

		return group;
	}

	public void addSpriteContainer(SpriteContainer container) {
		SnapshotArray<Actor> components = container.getComponents();
		Actor[] actors = components.begin();

		Actor actor;
		for (int i = 0, n = components.size; i < n; i++) {
			actor = actors[i];

			if (actor instanceof ProceduralSpriteActor || actor instanceof MeshActor) {
				addActor(actor);
			} else {
				super.addActor(actor);
			}
		}

		components.end();
		super.addActor(container);
	}

	public Color getColor() {
		return mColorActor.getColor();
	}

	public void setColor(Color color) {
		mColorActor.setColor(color);
	}

	@Override
	public void addAction(Action action) {
		mColorActor.addAction(action);
	}
}
