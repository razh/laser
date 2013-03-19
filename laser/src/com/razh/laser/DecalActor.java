package com.razh.laser;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 *  This class may not work as we cannot explictly pass uniforms to a given shader.
 *  Need to write a custom DecalBatch which would allow us to do this.
 *
 *  Perhaps we can use a custom DecalMaterial instead. With decalMaterial.set(), add decalMaterial.set(shader)
 *
 *  How about customDecalBatch.render(ShaderProgram shader, Array<DecalActor> decalActors)?
 *  This might work.
 *
 *  Same as equivalent code in DecalBatch, but this time looping over the decalActors array, setting uniforms.
 *
 */
public class DecalActor extends Actor3D {
	private Decal mDecal;

	public DecalActor() {
		super();

		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		Texture texture = new Texture(pixmap);
		TextureRegion region = new TextureRegion(texture);
		setDecal(Decal.newDecal(region, true));
	}

	public void draw(DecalBatch decalBatch) {
		draw(decalBatch, null);
	}

	public void draw(DecalBatch decalBatch, ShaderProgram shaderProgram) {
	}

	public void setUniforms(ShaderProgram shaderProgram) {
		shaderProgram.setUniformf("color", getColor());
	}

	public Decal getDecal() {
		return mDecal;
	}

	public void setDecal(Decal decal) {
		mDecal = decal;
	}
}
