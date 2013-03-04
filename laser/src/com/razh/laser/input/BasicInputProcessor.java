package com.razh.laser.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.razh.laser.Player;

public abstract class BasicInputProcessor implements InputProcessor {
	private Stage mStage;
	private Player mPlayer;

	public Stage getStage() {
		return mStage;
	}

	public void setStage(Stage stage) {
		mStage = stage;
	}

	public Player getPlayer() {
		return mPlayer;
	}

	public void setPlayer(Player player) {
		mPlayer = player;
	}
}
