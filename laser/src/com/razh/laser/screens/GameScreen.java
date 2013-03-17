package com.razh.laser.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.razh.laser.LaserGame;
import com.razh.laser.MeshStage;
import com.razh.laser.Shader;
import com.razh.laser.SpriteGroup;
import com.razh.laser.entities.Entity;
import com.razh.laser.entities.EntityFactory;
import com.razh.laser.input.GameInputProcessor;
import com.razh.laser.sprites.CircleSpriteActor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class GameScreen extends BasicScreen {

	private SpriteGroup circleSpriteGroup;
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

		for (int i = 0; i < 20; i++) {
			Entity entity3 = EntityFactory.createCircleThing();
			getMeshStage().addMeshActor(entity3.getActor());
		}

		getMeshStage().setShaderProgram(Shader.createSimpleMeshShader());
		getMeshStage().setColor(Color.DARK_GRAY);

		circleSpriteGroup = new SpriteGroup();
		circleSpriteGroup.setShaderProgram(Shader.createSimpleShader());
		System.out.println(circleSpriteGroup.getShaderProgram().hasUniform("color"));
		circleSprite = new CircleSpriteActor();
		circleSprite.setColor(0.5f, 0.0f, 0.0f, 1.0f);
		circleSprite.setWidth(1000.0f);
		circleSprite.setHeight(1000.0f);
		circleSprite.setPosition(200f, 200f);
		circleSpriteGroup.addActor(circleSprite);
		getStage().addActor(circleSpriteGroup);

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
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		getStage().draw();
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
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
