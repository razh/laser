package com.razh.laser.input;

import com.badlogic.gdx.math.Vector2;
import com.razh.laser.MeshActor;
import com.razh.laser.Player;

public class GameInputProcessor extends BasicInputProcessor {

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

		MeshActor hit = (MeshActor) getStage().hit(point.x, point.y, true);
		if (hit != null) {
			getPlayer().setSelected(hit);
			if (hit.hasEntity()) {
				hit.getEntity().setPosition(point.x, point.y);
			}
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
			((MeshActor) getPlayer().getSelected()).getEntity().setPosition(point.x, point.y);
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
