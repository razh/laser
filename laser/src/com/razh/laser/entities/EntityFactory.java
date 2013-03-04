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
		actor.setMesh(Geometry.createRing(1.0f, 0.8f, 0, 180 * MathUtils.degreesToRadians, 64, true));
//		actor.setMesh(Geometry.createCircle(64));
		actor.setMode(GL20.GL_TRIANGLE_STRIP);

		actor.setGeometry(Geometry.createRingHull(1.0f, 0.8f, 0, 180 * MathUtils.degreesToRadians, 4, true));
		actor.setColor(Color.RED);
		actor.setPosition(100, 200);
		actor.setWidth(100);
		actor.setHeight(100);

		Entity entity = new Entity();
		entity.setActor(actor);
		actor.setEntity(entity);

		return entity;
	}
}
