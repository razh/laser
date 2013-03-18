package com.razh.laser;

public class DecalActor extends Actor3D {
	private Decal mDecal;

	public DecalActor() {
		super();

		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		Texture texture = new Texture(pixmap);
		TextureRegion region = new TextureRegion(texture);
		setDecal(Decal.newDecal(region, true));
	}

	public Decal getDecal() {
		return mDecal;
	}

	public void setDecal(Decal decal) {
		mDecal = decal;
	}
}
