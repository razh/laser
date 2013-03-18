package com.razh.laser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shader {
	public static ShaderProgram createSimpleMeshShader() {
		final String vertexName = "shaders/simple-mesh.vert";
		final String fragmentName = "shaders/simple.frag";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createProceduralSpriteShader(String fragmentName) {
		final String vertexName = "shaders/spritebatch.vert";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createProceduralDecalShader(String fragmentName) {
		final String vertexName = "shaders/decalbatch.vert";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createRectangleShader() {
		final String vertexName = "shaders/spritebatch.vert";
		final String fragmentName = "shaders/simple.frag";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createDistanceFieldShader() {
		final String vertexName = "shaders/spritebatch.vert";
		final String fragmentName = "shaders/distance-field.frag";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createCircleShader() {
		final String vertexName = "shaders/spritebatch.vert";
		final String fragmentName = "shaders/circle.frag";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createRingShader() {
		final String vertexName = "shaders/spritebatch.vert";
		final String fragmentName = "shaders/ring.frag";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createDashedRingShader() {
		final String vertexName = "shaders/spritebatch.vert";
		final String fragmentName = "shaders/dashed-ring.frag";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createDashedLineShader() {
		final String vertexName = "shaders/spritebatch.vert";
		final String fragmentName = "shaders/dashed-line.frag";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createArcShader() {
		final String vertexName = "shaders/spritebatch.vert";
		final String fragmentName = "shaders/arc.frag";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createLaserBeamShader() {
		final String vertexName = "shaders/spritebatch.vert";
		final String fragmentName = "shaders/laser-beam.frag";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createLaserGlowShader() {
		final String vertexName = "shaders/spritebatch.vert";
		final String fragmentName = "shaders/laser-glow.frag";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram compileShader(String vertex, String fragment) {
		ShaderProgram shader = new ShaderProgram(vertex, fragment);
		if (!shader.isCompiled()) {
			System.out.println(vertex);
			System.out.println(fragment);
		}

		return shader;
	}
}
