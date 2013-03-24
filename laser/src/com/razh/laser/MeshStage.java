package com.razh.laser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;

public class MeshStage extends ShaderStage {
	private MeshGroup mRoot;
	private ShaderProgram mShaderProgram;

	private MeshGroup mTestGroup;

	public MeshStage() {
		this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}

	public MeshStage(float width, float height, boolean stretch) {
		super(width, height, stretch);

		mRoot = new MeshGroup();
		mRoot.setStage(this);

		setColor(Color.BLACK);

		if (LaserGame.DEBUG) {
			mTestGroup = new MeshGroup();
			float x, y;
			MeshActor actor;
			int i, j;
			int xCount = 20;
			int yCount = 20;
			float dx = (Gdx.graphics.getWidth() - 60) / xCount;
			float dy = (Gdx.graphics.getHeight() - 60) / yCount;
			for (i = 0; i < xCount; i++ ) {
				for (j = 0; j < yCount; j++ ) {
					x = -120 + i * dx;
					y = 30 + j * dy;

					actor = new MeshActor();
					actor.setPosition(x, y);
					actor.setWidth(3);
					actor.setHeight(3);
					actor.setMesh(Geometry.createRectangle());
					actor.setMode(GL20.GL_TRIANGLE_STRIP);
					actor.setColor(Color.RED);

					mTestGroup.addActor(actor);
				}
			}
		}
	}

	@Override
	public void draw() {
		super.draw();

		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);

		if (LaserGame.DEBUG) {
			SnapshotArray<Actor> children = mTestGroup.getChildren();
			Actor[] actors = children.begin();
			Actor actor;
			for (int i = 0, n = children.size; i < n; i++) {
				actor = actors[i];
				if (hit(actor.getX(), actor.getY(), true) != null) {
					actor.setColor(Color.GREEN);
				} else {
					actor.setColor(Color.RED);
				}
			}
			children.end();
		}

		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
	}

	@Override
	public Actor hit(float stageX, float stageY, boolean touchable) {
		return getMeshRoot().hit(stageX, stageY, touchable);
	}

	@Override
	public void addActor(Actor actor) {
		super.addActor(actor);
	}

	public void addMeshActor(MeshActor actor) {
		mRoot.addActor(actor);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		mRoot.act(delta);
	}

	public ShaderProgram getShaderProgram() {
		return mShaderProgram;
	}

	public void setShaderProgram(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;
	}

	public MeshGroup getMeshRoot() {
		return mRoot;
	}

	@Override
	public void dispose() {
		super.dispose();
		mShaderProgram.dispose();
	}
}
