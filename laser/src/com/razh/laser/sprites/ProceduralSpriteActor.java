package com.razh.laser.sprites;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ProceduralSpriteActor extends SpriteActor {

	private static Sprite sSprite;

	public ProceduralSpriteActor() {
		super();

		if (sSprite == null) {
			createSprite();
		}

		setSprite(sSprite);
	}

	private void createSprite() {
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		Texture texture = new Texture(pixmap);
		sSprite = new Sprite(texture);
		texture.dispose();
		pixmap.dispose();
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

		sSprite.draw(spriteBatch, parentAlpha);
	}

	public void setUniforms(ShaderProgram shaderProgram) {
		shaderProgram.setUniformf("color", sSprite.getColor());
	}
}
