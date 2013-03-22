package com.razh.laser.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.razh.laser.sprites.DashedRingSpriteActor.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.razh.laser.Geometry;
import com.razh.laser.LaserGame;
import com.razh.laser.MeshActor;
import com.razh.laser.MeshStage;
import com.razh.laser.Shader;
import com.razh.laser.entities.Entity;
import com.razh.laser.entities.EntityFactory;
import com.razh.laser.input.GameInputProcessor;
import com.razh.laser.sprites.ArcSpriteActor;
import com.razh.laser.sprites.CircleSpriteActor;
import com.razh.laser.sprites.DashedRingSpriteActor;
import com.razh.laser.sprites.ProceduralSpriteActor;
import com.razh.laser.sprites.ProceduralSpriteGroup;
import com.razh.laser.sprites.SpriteContainer;

public class GameScreen extends BasicScreen {

	private ShaderProgram circleShader;
	private ProceduralSpriteGroup circleSpriteGroup;
	private CircleSpriteActor circleSprite;

	public GameScreen(LaserGame game) {
		super(game);

		setStage(new MeshStage());

		Entity entity = EntityFactory.createEmitter();
		getMeshStage().addActor(entity.getActor());
		entity.getActor().addAction(
			forever(
				rotateBy(360.0f, 1.0f)
			)
		);

		Entity entity2 = EntityFactory.createEmitter();
		getMeshStage().addActor(entity2.getActor());

		Entity entity3 = EntityFactory.createCircleThing();
		getMeshStage().addActor(entity3.getActor());

		getMeshStage().setShaderProgram(Shader.createSimpleMeshShader());
		getMeshStage().setColor(Color.BLACK);

		circleSpriteGroup = new ProceduralSpriteGroup();
		circleShader = Shader.createCircleShader();
		circleSpriteGroup.setShaderProgram(circleShader);
		System.out.println(circleSpriteGroup.getShaderProgram().hasUniform("color"));
		circleSprite = new CircleSpriteActor();
		circleSprite.setColor(0.5f, 0.0f, 0.0f, 1.0f);
		circleSprite.setWidth(180.0f);
		circleSprite.setHeight(180.0f);
//		circleSprite.setPosition(Gdx.graphics.getWidth() * 0.1f + 128f, Gdx.graphics.getHeight() * 0.1f + 128f);
		circleSprite.setPosition(0,0);
		circleSprite.addAction(
			forever(
				parallel(
					sequence(
						color(new Color(0.75f, 0.0f, 0.0f, 1.0f), 2.0f),
						color(new Color(0.25f, 0.0f, 0.0f, 1.0f), 2.0f)
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
		getStage().addActor(circleSpriteGroup);

		float halfWidth = 0.5f * Gdx.graphics.getWidth();
		float halfHeight = 0.5f * Gdx.graphics.getHeight();

		ProceduralSpriteGroup rectSpriteGroup = new ProceduralSpriteGroup();
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

		getStage().addActor(rectSpriteGroup);

		ProceduralSpriteGroup dashedRingSpriteGroup = new ProceduralSpriteGroup();
		dashedRingSpriteGroup.setShaderProgram(Shader.createDashedRingShader());

		DashedRingSpriteActor dashedRing = new DashedRingSpriteActor();
		dashedRing.setColor(0.0f, 1.0f, 1.0f, 1.0f);
		dashedRing.setWidth(200.0f);
		dashedRing.setHeight(200.0f);
		dashedRing.setPosition(-0.5f * halfWidth, -0.5f * halfHeight);
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

		getStage().addActor(dashedRingSpriteGroup);

		ArcSpriteActor arcSprite = new ArcSpriteActor();
		arcSprite.setColor(1.0f, 1.0f, 1.0f, 1.0f);
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

		SpriteContainer container = new SpriteContainer();
		container.addComponent(arcSprite);
		container.addComponent(circle);
		container.addComponent(mesh);

		getMeshStage().addSpriteContainer(container);
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
					scaleTo(1.0f, 1.0f, 5.0f)
				)
			)
		);

		GameInputProcessor gameInputProcessor = new GameInputProcessor();
		gameInputProcessor.setStage(getStage());
		gameInputProcessor.setPlayer(getGame().getPlayer());

		addInputProcessor(getStage());
		addInputProcessor(gameInputProcessor);
	}

	@Override
	public void render(float delta) {
		getStage().act(delta);

		Color backgroundColor = getMeshStage().getColor();

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl20.glEnable(GL20.GL_BLEND);
		getStage().draw();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}
}
