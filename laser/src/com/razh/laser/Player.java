package com.razh.laser;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player {
	private int mScore;
	private Actor mSelected;

	public int getScore() {
		return mScore;
	}

	public void setScore(int score) {
		mScore = score;
	}

	public Actor getSelected() {
		return mSelected;
	}

	public void setSelected(Actor selected) {
		mSelected = selected;
	}

	public boolean hasSelected() {
		return mSelected != null;
	}
}
