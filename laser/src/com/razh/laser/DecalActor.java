package com.razh.laser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;

public class DecalActor extends Actor3D {
	private Decal mDecal;

	@Override
	public void act(float delta) {
		super.act(delta);

		Color color = getColor();
		mDecal.setColor(color.r, color.g, color.b, color.a);

		mDecal.setPosition(getX(), getY(), getZ());
		mDecal.setDimensions(getWidth(), getHeight());
		mDecal.setScale(getScaleX(), getScaleY());
		// Hacky, since we're not supposed to modify this quaternion.
		mDecal.getRotation().set(getRotationQuaternion());
		// Force update to rotation quaternion of decal.
		mDecal.rotateX(0.0f);
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
