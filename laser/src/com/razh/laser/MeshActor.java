package com.razh.laser;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MeshActor extends Actor {
	private Mesh mMesh;
	private ShaderProgram mShaderProgram;

	private Matrix4 mModelMatrix;

	// Hull variables.
	private float[] mVertices;
	private short[] mIndices;

	public void draw(ShaderProgram shaderProgram) {
		if (mShaderProgram != shaderProgram) {
			setShaderProgram(shaderProgram);
		}

		draw();
	}

	public void draw() {

	}

	public Mesh getMesh() {
		return mMesh;
	}

	public void setMesh(Mesh mesh) {
		mMesh = mesh;
	}

	public ShaderProgram getShaderProgram() {
		return mShaderProgram;
	}

	public void setShaderProgram(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;
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
}
