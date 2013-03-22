package com.razh.laser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.razh.laser.sprites.ArcSpriteActor;
import com.razh.laser.sprites.CircleSpriteActor;
import com.razh.laser.sprites.DashedLineSpriteActor;
import com.razh.laser.sprites.DashedRingSpriteActor;
import com.razh.laser.sprites.RingSpriteActor;

public class Shader {

	public static boolean smooth = false;

	public static ShaderProgram getShaderForType(Class<?> type) {
		if (type == ArcSpriteActor.class) {
			return createArcShader();
		} else if (type == CircleSpriteActor.class) {
			return createCircleShader();
		} else if (type == DashedLineSpriteActor.class) {
			return createDashedLineShader();
		} else if (type == DashedRingSpriteActor.class) {
			return createDashedRingShader();
		} else if (type == RingSpriteActor.class) {
			return createRingShader();
		} else if (type == MeshActor.class) {
			return createSimpleMeshShader();
		} else {
			return null;
		}
	}

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
		final String fragmentName;
		if (smooth) {
			fragmentName = "shaders/smooth/circle-smooth.frag";
		} else {
			fragmentName = "shaders/circle.frag";
		}

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createRingShader() {
		final String vertexName = "shaders/spritebatch.vert";
		final String fragmentName;
		if (smooth) {
			fragmentName = "shaders/smooth/ring-smooth.frag";
		} else {
			fragmentName = "shaders/ring.frag";
		}

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createDashedRingShader() {
		final String vertexName = "shaders/spritebatch.vert";
		final String fragmentName;
		if (smooth) {
			fragmentName = "shaders/smooth/dashed-ring-smooth.frag";
		} else {
			fragmentName = "shaders/dashed-ring.frag";
		}

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createDashedLineShader() {
		final String vertexName = "shaders/spritebatch.vert";
		final String fragmentName;
		if (smooth) {
			fragmentName = "shaders/smooth/dashed-line-smooth.frag";
		} else {
			fragmentName = "shaders/dashed-line.frag";
		}

		return compileShader(Gdx.files.internal(vertexName).readString(),
		                     Gdx.files.internal(fragmentName).readString());
	}

	public static ShaderProgram createArcShader() {
		final String vertexName = "shaders/spritebatch.vert";
		final String fragmentName;
		if (smooth) {
			fragmentName = "shaders/smooth/arc-smooth.frag";
		} else {
			fragmentName = "shaders/arc.frag";
		}

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
