package com.razh.laser;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MeshActor extends Actor {
	private Mesh mMesh;
	private ShaderProgram mShaderProgram;

	// Perimeter variables.
	private float[] mVertices;
	private short[] mIndices;

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
