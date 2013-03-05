package com.razh.laser.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.razh.laser.Geometry;
import com.razh.laser.MeshActor;

public class EntityFactory {
	public static Entity createLaserSource() {
		MeshActor actor = new MeshActor();
		actor.setMesh(Geometry.createRing(1.0f, 0.8f, 0, 180 * MathUtils.degreesToRadians, 32, true));
//		actor.setMesh(Geometry.createCircle(64));
		actor.setMode(GL20.GL_TRIANGLE_STRIP);

		actor.setGeometry(Geometry.createRingHull(1.0f, 0.8f, 0, 180 * MathUtils.degreesToRadians, 32, true));
		actor.setColor(Color.RED);
//		actor.setPosition((float) (Gdx.graphics.getWidth() * Math.random()),
//		                  (float) (Gdx.graphics.getHeight() * Math.random()));
		actor.setPosition(Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.2f);
		actor.setWidth(100);
		actor.setHeight(100);

		Entity entity = new Entity();
		entity.setActor(actor);
		actor.setEntity(entity);

		return entity;
	}
}
