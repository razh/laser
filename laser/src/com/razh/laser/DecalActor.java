package com.razh.laser;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;

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

	@Override
	public void act(float delta) {
		super.act(delta);

		mDecal.setPosition(getX(), getY(), getZ());
	}

	public void draw(DecalBatch decalBatch) {
		decalBatch.add(mDecal);
	}

	public Decal getDecal() {
		return mDecal;
	}

	public void setDecal(Decal decal) {
		mDecal = decal;
	}
}
