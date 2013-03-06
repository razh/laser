package com.razh.laser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shader {
	public static ShaderProgram createSimpleShader() {
		final String vertexName = "shaders/simple.vert";
		final String fragmentName = "shaders/simple.frag";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createDistanceFieldShader() {
		final String vertexName = "shaders/distance-field.vert";
		final String fragmentName = "shaders/distance-field.frag";

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createCircleShader() {
		final String vertexName = "shaders/circle.vert";
		final String fragmentName = "shaders/circle.frag";

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
