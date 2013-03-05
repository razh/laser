package com.razh.laser.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.razh.laser.Player;

public class GameInputProcessor extends BasicInputProcessor {
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

		Vector2 point = getStage().screenToStageCoordinates(new Vector2(screenX, screenY));

		Actor hit = getStage().hit(point.x, point.y, true);
		if (hit != null) {
			mOffset.set(point).sub(hit.getX(), hit.getY());

			getPlayer().setSelected(hit);
			hit.setPosition(point.x - mOffset.x, point.y - mOffset.y);
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Player player = getPlayer();
		if (player == null) {
			return false;
		}

		if (player.hasSelected()) {
			player.setSelected(null);
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (getStage() == null || getPlayer() == null) {
			return false;
		}

		Vector2 point = getStage().screenToStageCoordinates(new Vector2(screenX, screenY));

		if (getPlayer().hasSelected()) {
			getPlayer().getSelected().setPosition(point.x - mOffset.x, point.y - mOffset.y);
		}

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

}
