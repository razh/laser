package com.razh.laser.textures;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureUtils {

	/**
	 * Converts the given two-dimensional array of TextureRegions to
	 * a one-dimensional array.
	 * @param regions
	 * @return
	 */
	public static TextureRegion[] flatten(TextureRegion[][] regions) {
		if (regions.length > 0 && regions[0].length > 0) {
			return flatten(regions, regions.length * regions[0].length);
		}

		return null;
	}

	public static TextureRegion[] flatten(TextureRegion[][] regions, int count) {
		return null;
	}
}
