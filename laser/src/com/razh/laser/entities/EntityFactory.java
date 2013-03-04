package com.razh.laser.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.razh.laser.Geometry;
import com.razh.laser.LaserGame;
import com.razh.laser.MeshActor;

public class EntityFactory {
	public static Entity createLaserSource(World world) {
		MeshActor actor = new MeshActor();
		actor.setMesh(Geometry.createRing(1.0f, 0.8f, 0, 180 * MathUtils.degreesToRadians, 32, true));
//		actor.setMesh(Geometry.createCircle(64));
		actor.setMode(GL20.GL_TRIANGLE_STRIP);

		actor.setGeometry(Geometry.createRingHull(1.0f, 0.8f, 0, 180 * MathUtils.degreesToRadians, 4, true));
		actor.setColor(Color.RED);
//		actor.setPosition((float) (Gdx.graphics.getWidth() * Math.random()),
//		                  (float) (Gdx.graphics.getHeight() * Math.random()));
		actor.setPosition(Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.2f);
		actor.setWidth(100);
		actor.setHeight(100);

		BodyDef bodyDef = new BodyDef();
		bodyDef.linearDamping = 1e10f;
		bodyDef.allowSleep = true;
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(actor.getX() / LaserGame.PTM_RATIO, actor.getY() / LaserGame.PTM_RATIO);
		Body body = world.createBody(bodyDef);

		CircleShape circle = new CircleShape();
		circle.setRadius(actor.getWidth() / LaserGame.PTM_RATIO);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.friction = 1.0f;
		// No bounce.
		fixtureDef.restitution = 0.0f;

		body.createFixture(fixtureDef);

		// Set initial mass to very high.
		MassData massData = body.getMassData();
		massData.mass = 1e12f;
		body.setMassData(massData);

		circle.dispose();

		Entity entity = new Entity();
		entity.setActor(actor);
		entity.setBody(body);
		actor.setEntity(entity);

		return entity;
	}
}
