package com.razh.laser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.razh.laser.Geometry.GeometryData;

public class MeshActor extends EntityActor {
	private Mesh mMesh;
	private ShaderProgram mShaderProgram;

	private int mMode;

	private final Matrix4 mModelMatrix;

	// Hull variables.
	private float[] mVertices;
	private short[] mIndices;

	public MeshActor() {
		super();

		mModelMatrix = new Matrix4();

		// Default value.
		setMode(GL20.GL_TRIANGLE_FAN);
	}

	public void draw(ShaderProgram shaderProgram) {
		if (mShaderProgram != shaderProgram) {
			setShaderProgram(shaderProgram);
		}

		draw();
	}

	public void draw() {
		if (mShaderProgram == null) {
			return;
		}

		setupModelMatrix();
		mShaderProgram.setUniformMatrix("modelMatrix", mModelMatrix);
		mShaderProgram.setUniformf("color", getColor());

		if (hasMesh()) {
			getMesh().render(mShaderProgram, getMode());
		}
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && getTouchable() != Touchable.enabled) {
			return null;
		}

		if (getVertices() == null) {
			if (Math.abs(x - getX()) <= getWidth() &&
			    Math.abs(y - getY()) <= getHeight()) {
				return this;
			}

			return null;
		}

		if (contains(x, y)) {
			return this;
		}

		return null;
	}

	public boolean contains(float x, float y) {
		Vector2 point = worldToLocalCoordinates(new Vector2(x, y));
		x = point.x;
		y = point.y;

		// We're not using libgdx's Intersector.isPointInPolygon() as that requires a list.
		float[] vertices = getVertices();
		int vertexCount = vertices.length / 2;
		boolean contains = false;

		float xi, yi, xj, yj;
		int i, j;
		for (i = 0, j = vertexCount - 1; i < vertexCount; j = i++) {
			xi = vertices[2 * i];
			yi = vertices[2 * i + 1];
			xj = vertices[2 * j];
			yj = vertices[2 * j + 1];

			if (((yi > y) != (yj > y)) &&
			   (x < (xj - xi) * (y - yi) / (yj - yi) + xi)) {
				contains = !contains;
			}
		}

		return contains;
	}

	public Vector2 worldToLocalCoordinates(Vector2 worldCoords) {
		return worldCoords.cpy()
		                  .sub(getX(), getY())
		                  .rotate(-getRotation())
		                  .sub(getOriginX(), getOriginY())
		                  .div(getWidth() * getScaleX(), getHeight() * getScaleY());
	}

	public Vector2 localToWorldCoordinates(Vector2 localCoords) {
		return localCoords.cpy()
		                  .mul(getWidth() * getScaleX(), getHeight() * getScaleY())
		                  .add(getOriginX(), getOriginY())
		                  .rotate(getRotation())
		                  .add(getX(), getY());
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
		mModelMatrix.idt()
		            .translate(getX(), getY(), 0.0f)
		            .rotate(Vector3.Z, getRotation())
		            .translate(getOriginX(), getOriginY(), 0.0f)
		            .scale(getWidth() * getScaleX(), getHeight() * getScaleY(), 1.0f);
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

}
