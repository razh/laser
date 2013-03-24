package com.razh.laser;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;

public class DecalActor extends Actor3D {
	private Decal mDecal;

	@Override
	public void act(float delta) {
		super.act(delta);

		mDecal.setPosition(getX(), getY(), getZ());
		mDecal.setDimensions(getWidth(), getHeight());
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
