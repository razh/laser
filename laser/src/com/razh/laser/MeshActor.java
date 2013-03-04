package com.razh.laser;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.razh.laser.Geometry.GeometryData;
import com.razh.laser.entities.Entity;

public class MeshActor extends Actor {
	private Mesh mMesh;
	private ShaderProgram mShaderProgram;

	private int mMode;

	private Matrix4 mModelMatrix;

	// Hull variables.
	private float[] mVertices;
	private short[] mIndices;

	private Entity mEntity;

	public MeshActor() {
		super();

		mModelMatrix = new Matrix4();

		// Default value.
		setMode(GL20.GL_TRIANGLE_FAN);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (hasEntity()) {
			getEntity().act(delta);
		}
	}

	public void draw(ShaderProgram shaderProgram) {
		if (mShaderProgram != shaderProgram) {
			setShaderProgram(shaderProgram);
		}

		draw();
	}

	public void draw() {
		if (mShaderProgram == null || !isVisible()) {
			return;
		}

		setupModelMatrix();
		mShaderProgram.setUniformMatrix("modelMatrix", mModelMatrix);
		mShaderProgram.setUniformf("color", getColor());

		if (hasMesh()) {
			getMesh().render(mShaderProgram, getMode());
		}
	}

	public Mesh getMesh() {
		return mMesh;
	}

	public void setMesh(Mesh mesh) {
		mMesh = mesh;
	}

	public boolean hasMesh() {
		return getMesh() != null;
	}

	public ShaderProgram getShaderProgram() {
		return mShaderProgram;
	}

	public void setShaderProgram(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;
	}

	public int getMode() {
		return mMode;
	}

	public void setMode(int mode) {
		mMode = mode;
	}

	public Matrix4 getModelMatrix() {
		return mModelMatrix;
	}

	public void setupModelMatrix() {
		getModelMatrix().idt()
		                .translate(getX(), getY(), 0.0f)
		                .rotate(Vector3.Z, getRotation())
		                .scale(getWidth(), getHeight(), 1.0f);
	}

	public float[] getVertices() {
		return mVertices;
	}

	public void setVertices(float[] vertices) {
		mVertices = vertices;
	}

	public short[] getIndices() {
		return mIndices;
	}

	public void setIndices(short[] indices) {
		mIndices = indices;
	}

	public void setGeometry(GeometryData geometry) {
		setVertices(geometry.vertices);
		setIndices(geometry.indices);
	}

	public Entity getEntity() {
		return mEntity;
	}

	public void setEntity(Entity entity) {
		mEntity = entity;
	}

	public boolean hasEntity() {
		return mEntity != null;
	}
}
