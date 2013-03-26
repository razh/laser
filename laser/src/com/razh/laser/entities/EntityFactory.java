package com.razh.laser.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.razh.laser.ActorContainer;
import com.razh.laser.Geometry;
import com.razh.laser.MeshActor;
import com.razh.laser.TransformActorContainer;
import com.razh.laser.components.MissileComponent;
import com.razh.laser.components.MissilePathComponent;
import com.razh.laser.sprites.SpriteActor;

public class EntityFactory {
	public static Entity createEmitter() {
		MeshActor actor = new MeshActor();
		actor.setMesh(Geometry.createRing(1.0f, 0.8f, 0, 180 * MathUtils.degreesToRadians, 32, true));
//		actor.setMesh(Geometry.createCircle(64));
		actor.setMode(GL20.GL_TRIANGLE_STRIP);

		actor.setGeometry(Geometry.createRingHull(1.0f, 0.8f, 0, 180 * MathUtils.degreesToRadians, 32, true));
		actor.setColor(1.0f, 0.0f, 0.0f, 0.5f);
//		actor.setPosition((float) (Gdx.graphics.getWidth() * Math.random()),
//		                  (float) (Gdx.graphics.getHeight() * Math.random()));
		actor.setPosition(Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.2f);
		actor.setWidth(100);
		actor.setHeight(100);

		Entity emitter = new Entity();
		emitter.setActor(actor);
		actor.setEntity(emitter);

		return emitter;
	}

	public static Entity createCircleThing() {
		MeshActor actor = new MeshActor();
		actor.setMesh(Geometry.createCircle(8));
		actor.setMode(GL20.GL_TRIANGLE_FAN);

//		actor.setGeometry(Geometry.createRingHull(1.0f, 0.8f, 0, 180 * MathUtils.degreesToRadians, 32, true));
		actor.setColor(0.75f, 0.75f, 0.75f, 1.0f);
//		actor.setPosition((float) (Gdx.graphics.getWidth() * Math.random()),
//		                  (float) (Gdx.graphics.getHeight() * Math.random()));
		actor.setPosition(Gdx.graphics.getWidth() * 0.2f, Gdx.graphics.getHeight() * 0.1f);
		actor.setWidth(128);
		actor.setHeight(128);

		Entity entity = new Entity();
		entity.setActor(actor);
		actor.setEntity(entity);

		return entity;
	}

	public static Entity createCircleSprite() {
		// ShaderGroups?
		return null;
	}

	public static Entity createMissile(int segmentCount) {
		ActorContainer missileContainer = new TransformActorContainer();
		missileContainer.setPosition(Gdx.graphics.getWidth() * 0.4f, Gdx.graphics.getHeight() * 0.2f);

		Texture texture = new Texture("data/missile.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		SpriteActor missile = new SpriteActor();
		missile.setSprite(new Sprite(texture));
		missile.setWidth(128.0f);
		missile.setHeight(128.0f);

		missileContainer.addActor(missile);

		// Add missile logic.
		MissileComponent missileComponent = new MissileComponent();
		missileComponent.setMaxAcceleration(1000.0f);
		missileComponent.setMaxSpeed(2000.0f);

		// Add missile path.
		Texture missilePathTexture = new Texture("data/missile_path.png");
		missilePathTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		MissilePathComponent missilePath = new MissilePathComponent();
		missilePath.setPathSprite(new Sprite(missilePathTexture));

		Entity entity = new Entity();
		entity.setActor(missileContainer);
		entity.addComponent(missileComponent);
		entity.addComponent(missilePath);
		missileContainer.setEntity(entity);

		return entity;
	}
}
