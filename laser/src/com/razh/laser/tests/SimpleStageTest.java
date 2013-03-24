package com.razh.laser.tests;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.color;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeBy;
import static com.razh.laser.sprites.DashedRingSpriteActor.segmentAngleTo;
import static com.razh.laser.sprites.DashedRingSpriteActor.segmentSpacingTo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.razh.laser.DecalActor;
import com.razh.laser.Geometry;
import com.razh.laser.MeshActor;
import com.razh.laser.Shader;
import com.razh.laser.ShaderGroup;
import com.razh.laser.ShaderStage;
import com.razh.laser.entities.Entity;
import com.razh.laser.entities.EntityFactory;
import com.razh.laser.sprites.ArcSpriteActor;
import com.razh.laser.sprites.CircleSpriteActor;
import com.razh.laser.sprites.DashedRingSpriteActor;
import com.razh.laser.sprites.ProceduralSpriteActor;
import com.razh.laser.sprites.SpriteActor;
import com.razh.laser.sprites.SpriteContainer;

public class SimpleStageTest extends StageTest {

	@Override
	public void load(ShaderStage stage) {
		float halfWidth = 0.5f * Gdx.graphics.getWidth();
		float halfHeight = 0.5f * Gdx.graphics.getHeight();

		Texture texture = new Texture("data/test_ship.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Entity entity = EntityFactory.createEmitter();
		stage.addActor(entity.getActor());
		entity.getActor().addAction(
			forever(
				rotateBy(360.0f, 1.0f)
			)
		);

		Entity entity2 = EntityFactory.createEmitter();
		stage.addActor(entity2.getActor());

		Entity entity3 = EntityFactory.createCircleThing();
		stage.addActor(entity3.getActor());

		stage.setColor(Color.BLACK);

		ShaderGroup circleSpriteGroup = new ShaderGroup();
		ShaderProgram circleShader = Shader.createCircleShader();
		circleSpriteGroup.setShaderProgram(circleShader);
		CircleSpriteActor circleSprite = new CircleSpriteActor();
		circleSprite.setColor(0.5f, 0.0f, 0.0f, 0.2f);
		circleSprite.setWidth(180.0f);
		circleSprite.setHeight(180.0f);
//		circleSprite.setPosition(Gdx.graphics.getWidth() * 0.1f + 128f, Gdx.graphics.getHeight() * 0.1f + 128f);
		circleSprite.setPosition(0,0);
		circleSprite.addAction(
			forever(
				parallel(
					sequence(
						color(new Color(0.75f, 0.0f, 0.0f, 0.5f), 2.0f),
						color(new Color(0.25f, 0.0f, 0.0f, 0.5f), 2.0f)
					),
					sequence(
						repeat(
							2,
							sizeBy(100f, 100f, 4.0f, Interpolation.linear)
						),
						repeat(
							2,
							sizeBy(-100f, -100f, 4.0f, Interpolation.linear)
						)
					)
				)
			)
		);
		circleSpriteGroup.addActor(circleSprite);
		stage.addActor(circleSpriteGroup);

		ShaderGroup rectSpriteGroup = new ShaderGroup();
		rectSpriteGroup.setShaderProgram(Shader.createRectangleShader());

		ProceduralSpriteActor topLeftActor = new ProceduralSpriteActor();
		topLeftActor.setColor(0.0f, 1.0f, 0.0f, 1.0f);
		topLeftActor.setWidth(40.0f);
		topLeftActor.setHeight(40.0f);
		topLeftActor.setPosition(-halfWidth, halfHeight);
//		topLeftActor.setPosition(0, 0);
		rectSpriteGroup.addActor(topLeftActor);

		ProceduralSpriteActor topRightActor = new ProceduralSpriteActor();
		topRightActor.setColor(0.0f, 1.0f, 0.0f, 1.0f);
		topRightActor.setWidth(40.0f);
		topRightActor.setHeight(40.0f);
		topRightActor.setPosition(halfWidth, halfHeight);
		rectSpriteGroup.addActor(topRightActor);

		ProceduralSpriteActor bottomLeftActor = new ProceduralSpriteActor();
		bottomLeftActor.setColor(0.0f, 1.0f, 0.0f, 1.0f);
		bottomLeftActor.setWidth(40.0f);
		bottomLeftActor.setHeight(40.0f);
		bottomLeftActor.setPosition(-halfWidth, -halfHeight);
		rectSpriteGroup.addActor(bottomLeftActor);

		ProceduralSpriteActor bottomRightActor = new ProceduralSpriteActor();
		bottomRightActor.setColor(0.0f, 1.0f, 0.0f, 1.0f);
		bottomRightActor.setWidth(40.0f);
		bottomRightActor.setHeight(40.0f);
		bottomRightActor.setPosition(halfWidth, -halfHeight);
		rectSpriteGroup.addActor(bottomRightActor);

		stage.addActor(rectSpriteGroup);

		ShaderGroup dashedRingSpriteGroup = new ShaderGroup();
		Shader.smooth = true;
		dashedRingSpriteGroup.setShaderProgram(Shader.createDashedRingShader());
		Shader.smooth = false;

		DashedRingSpriteActor dashedRing = new DashedRingSpriteActor();
		dashedRing.setColor(0.0f, 1.0f, 1.0f, 0.15f);
		dashedRing.setWidth(200.0f);
		dashedRing.setHeight(200.0f);
		dashedRing.setPosition(-0.3f * halfWidth, -0.3f * halfHeight);
		dashedRing.setOuterRadius(0.5f);
		dashedRing.setInnerRadius(0.4f);
		dashedRing.setSegmentAngle(120.0f);
		dashedRing.setSegmentSpacing(10.0f);
		dashedRing.addAction(
			sequence(
				segmentAngleTo(36.0f, 2.0f),
				segmentAngleTo(120.0f, 2.0f),
				segmentSpacingTo(22.5f, 1.0f),
				forever(
					parallel(
						rotateBy(-180.0f, 1.0f),
						sequence(
							scaleTo(2.0f, 2.0f, 2.0f),
							scaleTo(1.0f, 1.0f, 2.0f)
						)
					)
				)
			)
		);
		dashedRingSpriteGroup.addActor(dashedRing);

		stage.addActor(dashedRingSpriteGroup);

		ArcSpriteActor arcSprite = new ArcSpriteActor();
		arcSprite.setColor(1.0f, 1.0f, 1.0f, 0.7f);
		arcSprite.setWidth(300.0f);
		arcSprite.setHeight(300.0f);
		arcSprite.setOuterRadius(0.5f);
		arcSprite.setInnerRadius(0.4f);
		arcSprite.setLeftAngle(45.0f);
		arcSprite.setRightAngle(45.0f);
		arcSprite.setPosition(-0.25f * halfWidth, 0.0f * halfHeight);

		CircleSpriteActor circle = new CircleSpriteActor();
		circle.setColor(0.8f, 0.0f, 0.0f, 1.0f);
		circle.setRadius(100.0f);

		MeshActor mesh = new MeshActor();
		mesh.setMesh(Geometry.createCircle(6));
		mesh.setMode(GL20.GL_TRIANGLE_FAN);
		mesh.setColor(0.5f, 0.0f, 0.0f, 1.0f);
		mesh.setOrigin(200.0f, 0.0f);
		mesh.setWidth(20.0f);
		mesh.setHeight(20.0f);

		SpriteActor shipSprite = new SpriteActor();
		shipSprite.setPosition(-0.25f * halfWidth, 0.25f * halfHeight);
		shipSprite.setWidth(300.0f);
		shipSprite.setHeight(300.0f);

		shipSprite.setSprite(new Sprite(texture));
		stage.addActor(shipSprite);

		SpriteActor shipSprite2 = new SpriteActor();
		shipSprite2.setPosition(0.25f * halfWidth, -0.25f * halfHeight);
		shipSprite2.setWidth(300.0f);
		shipSprite2.setHeight(300.0f);
		shipSprite2.setOrigin(0.0f, 0.0f);
		shipSprite2.addAction(forever(rotateBy(180.0f, 1.0f)));

		Texture texture2 = new Texture("data/test_ship2.png");
		texture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shipSprite2.setSprite(new Sprite(texture2));
		stage.addActor(shipSprite2);

		SpriteContainer container = new SpriteContainer();
		container.addComponent(arcSprite);
		container.addComponent(circle);
		container.addComponent(mesh);

		stage.addSpriteContainer(container);
		container.addAction(
			parallel(
				forever(
					rotateBy(-1800.0f, 10.0f)
				),
				forever(
					sequence(
						moveBy(700.0f, 0.0f, 5.0f),
						moveBy(-700.0f, 0.0f, 5.0f)
					)
				),
				sequence(
					scaleTo(2, 2, 5.0f),
					scaleTo(1.0f, 1.0f, 5.0f),
					removeActor()
				)
			)
		);

		DecalActor decal = new DecalActor();
		decal.setDecal(Decal.newDecal(new TextureRegion(texture)));
		decal.setWidth(200.0f);
		decal.setHeight(200.0f);
		decal.setPosition(0.3f * halfWidth, 0.4f * halfHeight, -100.0f);
		decal.getDecal().setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		stage.addActor(decal);
	}
}
