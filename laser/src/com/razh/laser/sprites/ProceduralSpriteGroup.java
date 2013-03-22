package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.MeshActor;

public class ProceduralSpriteGroup extends Group {
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

			if (child instanceof ProceduralSpriteActor) {
				((ProceduralSpriteActor) child).draw(spriteBatch, parentAlpha, mShaderProgram);
			} else if (child instanceof MeshActor){
				((MeshActor) child).draw(mShaderProgram);
			}
		}

		children.end();
	}

	public ShaderProgram getShaderProgram() {
		return mShaderProgram;
	}

	public void setShaderProgram(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;
	}
}
