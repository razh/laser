package com.razh.laser;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ShaderStage extends Stage {

	private final Map<String, ShaderProgram> shaders;

	public ShaderStage() {
		this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}

	public ShaderStage(float width, float height, boolean stretch) {
		super(width, height, stretch);
		shaders = new HashMap<String, ShaderProgram>();
	}
}
