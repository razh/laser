package com.razh.laser;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.sprites.ProceduralSpriteActor;

public class ShaderGroup extends Group {
	private ShaderProgram mShaderProgram;

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		spriteBatch.setShader(mShaderProgram);
		drawChildren(spriteBatch, parentAlpha);
		spriteBatch.setShader(null);
	}

	@Override
	public void drawChildren(SpriteBatch spriteBatch, float parentAlpha) {
		parentAlpha *= getColor().a;
		SnapshotArray<Actor> children = getChildren();
		Actor[] actors = children.begin();

		Actor child;
		for (int i = 0, n = children.size; i < n; i++) {
			child = actors[i];

			if (!child.isVisible()) {
				continue;
			}

			drawChild(child, spriteBatch, parentAlpha);
		}

		children.end();
	}

	public void drawChild(Actor actor, SpriteBatch spriteBatch, float parentAlpha) {
		if (actor instanceof ProceduralSpriteActor) {
			((ProceduralSpriteActor) actor).draw(spriteBatch, parentAlpha, mShaderProgram);
		} else if (actor instanceof MeshActor) {
			((MeshActor) actor).draw(mShaderProgram);
		} else {
			actor.draw(spriteBatch, parentAlpha);
		}
	}

	public ShaderProgram getShaderProgram() {
		return mShaderProgram;
	}

	public void setShaderProgram(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;
	}
}
