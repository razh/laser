package com.razh.laser.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.razh.laser.Player;
import com.razh.laser.components.PlayerComponent;

public class GameInputProcessor extends BasicInputProcessor {

	private PlayerComponent mPlayerComponent;
	// Offsets from touch position to object position.
	private final Vector2 mOffset;

	public GameInputProcessor() {
		mOffset = new Vector2();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (getStage() == null || getPlayer() == null) {
			return false;
		}

		boolean anticlockwise = screenX < 0.5f * Gdx.graphics.getWidth() ? true : false;
		System.out.println(anticlockwise ? "left" : "right");
		if (mPlayerComponent != null) {
			if (anticlockwise) {
				mPlayerComponent.turnLeft(true);
			} else {
				mPlayerComponent.turnRight(true);
			}
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		boolean anticlockwise = screenX < 0.5f * Gdx.graphics.getWidth() ? true : false;
		if (mPlayerComponent != null) {
			if (!Gdx.input.isTouched()) {
				mPlayerComponent.setAngularVelocity(0.0f);
			}
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void setPlayer(Player player) {
		super.setPlayer(player);
		PlayerComponent playerComponent = (PlayerComponent) player.getEntity().getComponentOfType(PlayerComponent.class);
		if (playerComponent != null) {
			mPlayerComponent = playerComponent;
		}
	}

}
