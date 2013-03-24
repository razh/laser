package com.razh.laser.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.razh.laser.LaserGame;
import com.razh.laser.ShaderStage;

public abstract class BasicScreen implements Screen {
	private LaserGame mGame;
	private Stage mStage;
	private InputMultiplexer mInputMultiplexer;

	public BasicScreen(LaserGame game) {
		setGame(game);
		setInputMultiplexer(new InputMultiplexer());
	}

	public LaserGame getGame() {
		return mGame;
	}

	public void setGame(LaserGame game) {
		mGame = game;
	}

	public Stage getStage() {
		return mStage;
	}

	public ShaderStage getShaderStage() {
		if (getStage() instanceof ShaderStage) {
			return (ShaderStage) getStage();
		}

		return null;
	}

	public void setStage(Stage stage) {
		mStage = stage;
	}

	public InputMultiplexer getInputMultiplexer() {
		return mInputMultiplexer;
	}

	public void setInputMultiplexer(InputMultiplexer inputMultiplexer) {
		mInputMultiplexer = inputMultiplexer;
	}

	public void addInputProcessor(InputProcessor inputProcessor) {
		mInputMultiplexer.addProcessor(inputProcessor);
	}

	public void removeInputProcessor(InputProcessor inputProcessor) {
		mInputMultiplexer.removeProcessor(inputProcessor);
	}

	@Override
	public void show() {
		getGame().getInputMultiplexer().addProcessor(getInputMultiplexer());
	}

	@Override
	public void hide() {
		getGame().getInputMultiplexer().removeProcessor(getInputMultiplexer());
	}

	@Override
	public void dispose() {
		getStage().dispose();
	}
}
