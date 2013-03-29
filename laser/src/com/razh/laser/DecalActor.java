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

		mDecal.setPosition(getX() + getOriginX(),
				           getY() + getOriginY(),
				           getZ() + getOriginZ());
		mDecal.setDimensions(getWidth(), getHeight());
		mDecal.setScale(getScaleX(), getScaleY());

		mDecal.getRotation().idt();
		mDecal.rotateX(getRotationX());
		mDecal.rotateY(getRotationY());
		mDecal.rotateZ(getRotation() + getRotationZ());
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
