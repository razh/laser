package com.razh.laser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;

public class MeshStage extends Stage {
	private MeshGroup mRoot;
	private ShaderProgram mShaderProgram;

	// Allows us to set colors with actions.
	private Actor mColorActor;

	private MeshGroup mTestGroup;

	public MeshStage() {
		this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}

	public MeshStage(float width, float height, boolean stretch) {
		super(width, height, stretch);
		System.out.println(getCamera().combined);
		setCamera(new PerspectiveCamera(90.0f, width, height));
		getCamera().position.set(0, 0, 400f);
//		getCamera().position.set(0.5f * width, 0.5f * height, 500f);
		getCamera().far = 1e5f;
		getCamera().lookAt(0.0f, 0.0f, 0.0f);
		System.out.println(getCamera().combined);
//		if (width != LaserGame.WIDTH || height != LaserGame.HEIGHT) {
//			setViewport(LaserGame.WIDTH, LaserGame.HEIGHT, true);
//			getCamera().position.set(0.5f * LaserGame.WIDTH,
//			                         0.5f * LaserGame.HEIGHT,
//			                         0.0f);
//		}

		mRoot = new MeshGroup();
		mRoot.setStage(this);

		mColorActor = new Actor();
		mColorActor.setColor(Color.BLACK);

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
		Camera camera = getCamera();
//		camera.rotateAround(Vector3.Zero, Vector3.X, 0.1f);
//		camera.rotateAround(Vector3.Zero, Vector3.Y, -0.1f);
////		camera.rotate(Vector3.Y, 0.2f);
//		camera.lookAt(0, 0, 0);

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

//
//		if (mShaderProgram != null) {
//			mShaderProgram.begin();
//
//			mShaderProgram.setUniformMatrix("projectionMatrix", camera.projection);
//			mShaderProgram.setUniformMatrix("viewMatrix", camera.view);
//
//			mRoot.draw(mShaderProgram);
//			if (LaserGame.DEBUG) {
//				mTestGroup.draw(mShaderProgram);
//			}
//
//			mShaderProgram.end();
//		}

		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
	}

	@Override
	public Actor hit(float stageX, float stageY, boolean touchable) {
		return getMeshRoot().hit(stageX, stageY, touchable);
	}

	public void addMeshActor(Actor actor) {
		mRoot.addActor(actor);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		mRoot.act(delta);
		mColorActor.act(delta);
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

	public Color getColor() {
		return mColorActor.getColor();
	}

	public void setColor(Color color) {
		mColorActor.setColor(color);
	}

	@Override
	public void addAction(Action action) {
		mColorActor.addAction(action);
	}

	@Override
	public void dispose() {
		super.dispose();
		mShaderProgram.dispose();
	}
}
