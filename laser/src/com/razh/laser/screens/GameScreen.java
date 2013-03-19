package com.razh.laser.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.razh.laser.LaserGame;
import com.razh.laser.MeshStage;
import com.razh.laser.ProceduralSpriteActor;
import com.razh.laser.Shader;
import com.razh.laser.ProceduralSpriteGroup;
import com.razh.laser.entities.Entity;
import com.razh.laser.entities.EntityFactory;
import com.razh.laser.input.GameInputProcessor;
import com.razh.laser.sprites.CircleSpriteActor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class GameScreen extends BasicScreen {

	private ShaderProgram circleShader;
	private ProceduralSpriteGroup circleSpriteGroup;
	private CircleSpriteActor circleSprite;

	public GameScreen(LaserGame game) {
		super(game);

		setStage(new MeshStage());

		Entity entity = EntityFactory.createEmitter();
		getMeshStage().addMeshActor(entity.getActor());
		entity.getActor().addAction(
			forever(
				rotateBy(360.0f, 1.0f)
			)
		);

		Entity entity2 = EntityFactory.createEmitter();
		getMeshStage().addMeshActor(entity2.getActor());

		Entity entity3 = EntityFactory.createCircleThing();
		getMeshStage().addMeshActor(entity3.getActor());

		getMeshStage().setShaderProgram(Shader.createSimpleMeshShader());
		getMeshStage().setColor(Color.DARK_GRAY);

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
					sizeBy(100f, 100f, 4.0f, Interpolation.linear)
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
