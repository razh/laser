package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ProceduralSpriteActor extends SpriteActor {

	public ProceduralSpriteActor() {
		super();

		setSprite(createSprite());
	}

	private Sprite createSprite() {
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		Texture texture = new Texture(pixmap);
		Sprite sprite = new Sprite(texture);

		texture.dispose();
		pixmap.dispose();

		return sprite;
	}

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		draw(spriteBatch, parentAlpha, null);
	}

	public void draw(SpriteBatch spriteBatch, float parentAlpha, ShaderProgram shaderProgram) {
		updateSprite();

		if (shaderProgram != null) {
			setUniforms(shaderProgram);
		}

		getSprite().draw(spriteBatch, parentAlpha);
	}

	public void setUniforms(ShaderProgram shaderProgram) {
		shaderProgram.setUniformf("color", getSprite().getColor());
	}
}
