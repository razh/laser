package com.razh.laser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shader {

	public static ShaderProgram createSimpleShader() {
		String vertexName = "shaders/simple.vert";
		String fragmentName = "shaders/simple.frag";

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
