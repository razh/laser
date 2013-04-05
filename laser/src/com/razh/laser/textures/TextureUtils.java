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
			return flatten(regions, regions.length * regions[0].length, regions.length, regions[0].length);
		}

		return null;
	}

	public static TextureRegion[] flatten(TextureRegion[][] regions, int count, int rows, int columns) {
		TextureRegion[] frames = new TextureRegion[count];

		int index = 0;
		int i, j;
		for (i = 0; i < rows; i++) {
			for (j = 0; j < columns; j++) {
				frames[index++] = regions[i][j];

				// Stop when we've added all the frames we want.
				if (index >= count) {
					return frames;
				}
			}
		}

		return frames;
	}
}
