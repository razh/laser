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
import com.razh.laser.components.BoidComponent;
import com.razh.laser.components.FlockComponent;
import com.razh.laser.components.LaserComponent;
import com.razh.laser.components.MissileComponent;
import com.razh.laser.components.MissilePathComponent;
import com.razh.laser.components.PlayerComponent;
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
		missileComponent.getPhysicsComponent().setMaxAcceleration(1000.0f);
		missileComponent.getPhysicsComponent().setMaxSpeed(2000.0f);

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

	public static Entity createFlock() {
		Texture texture = new Texture("data/boid.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		FlockComponent flock = new FlockComponent();
		ActorContainer boids = flock.getBoidActors();

		SpriteActor boid;
		Entity boidEntity;
		BoidComponent boidComponent;
		for (int i = 0; i < 30; i++ ) {
			boid = new SpriteActor();
			boid.setSprite(new Sprite(texture));
			boid.setPosition((float) Math.random() * -100.0f + 250.0f, (float) Math.random() * -100.0f + 250.0f);
//			boid.setRotation((float) Math.random() * 360.0f);
			boid.setWidth(32.0f);
			boid.setHeight(32.0f);

			boidComponent = new BoidComponent();
			boidComponent.setMaxSpeed(1000.0f);
			boidComponent.setMaxAcceleration(750.0f);

			boidEntity = new Entity();
			boidEntity.setActor(boid);
			boidEntity.addComponent(boidComponent);
			boid.setEntity(boidEntity);

			flock.addBoid(boidComponent);
		}

		Entity entity = new Entity();
		entity.setActor(boids);
		entity.addComponent(flock);
		boids.setEntity(entity);
		return entity;
	}

	public static Entity createPlayer() {
		Entity playerEntity = new Entity();

		PlayerComponent playerComponent = new PlayerComponent();
		LaserComponent laserComponent = playerComponent.getLaserComponent();
		laserComponent.setLaserSprite(new Sprite(new Texture("data/laser.png")));
		laserComponent.getLaserColor().set(0.75f, 0.5f, 0.5f, 0.75f);

		laserComponent.getBeamWidth().set(-10.0f, 10.0f);
		laserComponent.setBeamLength(3000.0f);

		laserComponent.getSegmentWidth().set(10.0f, 30.0f);
		laserComponent.getSegmentLength().set(700.0f, 1500.0f);

		laserComponent.getSegmentVelocity().set(6000.0f, 10000.0f);

		laserComponent.setLayerCount(30);

		SpriteActor playerActor = new SpriteActor();
		playerActor.setSprite(new Sprite(new Texture("data/boid.png")));
		playerActor.setWidth(256.0f);
		playerActor.setHeight(256.0f);

		playerEntity.setActor(playerActor);
		playerEntity.addComponent(laserComponent);
		playerActor.setEntity(playerEntity);

		return playerEntity;
	}
}
