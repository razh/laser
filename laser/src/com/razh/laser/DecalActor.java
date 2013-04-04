package com.razh.laser;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;

public class DecalActor extends Actor3D {

	private static final Vector3 mTempVector = new Vector3();

	private Decal mDecal;
	private boolean mBillboard;

	@Override
	public void act(float delta) {
		super.act(delta);

		Color color = getColor();
		mDecal.setColor(color.r, color.g, color.b, color.a);

		if (mBillboard) {
			Camera camera = getStage().getCamera();
			mDecal.lookAt(camera.position, camera.up);
		} else {
			mDecal.getRotation().idt();
			mDecal.rotateX(getRotationX());
			mDecal.rotateY(getRotationY());
			mDecal.rotateZ(getRotation() + getRotationZ());
		}

		// Set tempVector to origin.
		mTempVector.set(getOriginX(), getOriginY(), getOriginZ());
		// Transform it to local space.
		mDecal.getRotation().transform(mTempVector);
		// Add it to position (separate parameters instead of Vector3 (not really faster)).
		mTempVector.add(getX(), getY(), getZ());

		mDecal.setPosition(mTempVector.x, mTempVector.y, mTempVector.z);

		mDecal.setDimensions(getWidth(), getHeight());
		mDecal.setScale(getScaleX(), getScaleY());
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

	public boolean isBillboard() {
		return mBillboard;
	}

	public void setBillboard(boolean billboard) {
		mBillboard = billboard;
	}
}
