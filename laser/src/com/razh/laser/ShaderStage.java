package com.razh.laser;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.sprites.ProceduralSpriteActor;
import com.razh.laser.sprites.SpriteContainer;

public class ShaderStage extends Stage {

	private final Map<Class<?>, ShaderGroup> mGroups;

	// Decals.
	private final DecalBatch mDecalBatch;
	private final DecalGroup mDecalGroup;

	// Allows us to set colors with actions.
	private Actor mColorActor;

	public ShaderStage() {
		this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}

	public ShaderStage(float width, float height, boolean stretch) {
		super(width, height, stretch);

		Camera camera = new PerspectiveCamera(90.0f, width, height);
		setCamera(camera);
		camera.position.set(0, 0, 400f);
		camera.far = 1e5f;
		camera.lookAt(0.0f, 0.0f, 0.0f);

		mGroups = new HashMap<Class<?>, ShaderGroup>();

		mColorActor = new Actor();
		super.addActor(mColorActor);

		mDecalBatch = new DecalBatch(new CameraGroupStrategy(camera));
		// Create DecalGroup and add it to Groups container.
		mDecalGroup = new DecalGroup();
//		mGroups.put(DecalActor.class, mDecalGroup);
		addActor(mDecalGroup);
	}

	@Override
	public void draw() {
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
		super.draw();
		mDecalGroup.draw(mDecalBatch);
	}

	public float time = 0.0f;
	@Override
	public void act(float delta) {
		time += 0.01f;
		Camera camera = getCamera();
		camera.position.x = (float) (200.0f * Math.cos(time));
		camera.position.y = (float) (200.0f * Math.sin(time)) - 200.0f;
		camera.position.z = (float) (200.0f * Math.abs(Math.cos(time))) + 400.0f;
		camera.lookAt(0, 0, 0);

		super.act(delta);
	}

	@Override
	public void addActor(Actor actor) {
		// If actor is a group (ShaderGroup or otherwise), we don't need to add
		// it to a group.
		if (actor instanceof Group) {
			super.addActor(actor);
			return;
		}

		Group group = mGroups.get(actor.getClass());
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
		System.out.println(type.getName());
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

			if (actor instanceof ProceduralSpriteActor ||
				actor instanceof MeshActor ||
				actor instanceof DecalActor ) {
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
