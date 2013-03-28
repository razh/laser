package com.razh.laser.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.razh.laser.LaserGame;
import com.razh.laser.ShaderStage;
import com.razh.laser.input.GameInputProcessor;
import com.razh.laser.tests.SimpleStageTest;
import com.razh.laser.tests.StageTest;

public class GameScreen extends BasicScreen {

	public GameScreen(LaserGame game) {
		super(game);

		setStage(new ShaderStage());
		StageTest test = new SimpleStageTest();
		test.load(getShaderStage());

		GameInputProcessor gameInputProcessor = new GameInputProcessor();
		gameInputProcessor.setStage(getStage());
		gameInputProcessor.setPlayer(getGame().getPlayer());

		addInputProcessor(getStage());
		addInputProcessor(gameInputProcessor);
	}

	@Override
	public void render(float delta) {
		if (delta > 0.25f) {
			delta = 0.25f;
		}

		getStage().act(delta);

		Color backgroundColor = getShaderStage().getColor();

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
