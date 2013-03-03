package com.razh.laser.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.razh.laser.LaserGame;
import com.razh.laser.MeshStage;

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

	public MeshStage getMeshStage() {
		if (getStage() instanceof MeshStage) {
			return (MeshStage) getStage();
		}

		return null;
	}

	public void setStage(Stage stage) {
		mStage = stage;
	}

	public InputMultiplexer getInputMultiplexer() {
		return mInputMultiplexer;
	}

	public void setInputMUltiplexer(InputMultiplexer inputMultiplexer) {
		mInputMultiplexer = inputMultiplexer;
	}

	public void addInputProcessor(InputProcessor inputProcessor) {
	}
}
