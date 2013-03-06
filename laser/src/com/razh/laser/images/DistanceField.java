package com.razh.laser.images;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

/**
 * In-game version of libgdx's DistanceFieldGenerator class.
 * @author razh
 * @author Thomas ten Cate - Original author.
 */
public class DistanceField {

	private final Color mColor = new Color(Color.WHITE);
	private int mDownscale = 1;
	private float mSpread = 1;

	public Color getColor() {
		return mColor;
	}

	/**
	 * Sets color to be used for the output image. Alpha component is ignored.
	 * Defaults to white, which is convenient for multiplying by a color value
	 * at runtime.
	 * @param color
	 */
	public void setColor(Color color) {
		mColor.set(color);
	}

	public int getDownscale() {
		return mDownscale;
	}

	/**
	 * Sets the factor by which to downscale the image during processing.
	 * The output image will be smaller than the input image by this factor,
	 * rounded downwards.
	 *
	 * <p> For greater accuracy, images to be used as input for a distance
	 * field are often generated at higher resolution.
	 *
	 * @param downscale
	 * @throws IllegalArgumentException If downscale is not positive.
	 */
	public void setDownscale(int downscale) {
		if (downscale <= 0) {
			throw new IllegalArgumentException("Downscale must be positive.");
		}

		mDownscale = downscale;
	}

	public float getSpread() {
		return mSpread;
	}

	/**
	 * Sets the spread of the distance field. The spread is the maximum distance
	 * in pixels that will be scanned for a nearby edge. The resulting distance
	 * is also normalized by the spread.
	 *
	 * @param spread
	 * @throws IllegalArgumentException If spread is not positive.
	 */
	public void setSpread(float spread) {
		if (spread <= 0) {
			throw new IllegalArgumentException("Spread must be positive.");
		}

		mSpread = spread;
	}

	private static int distanceSquared(final int x0, final int y0, final int x1, final int y1) {
		final int dx = x1 - x0;
		final int dy = y1 - y0;
		return dx * dx + dy * dy;
	}

	/**
	 * Process the image into a distance field.
	 *
	 * Input image is recommended to be binary (black/white), but if not, see
	 * {@link #isInside(int)}.
	 *
	 * The returned image is a factor of {@code downscale} smaller than
	 * {@code inImage}.
	 *
	 * Opaque pixels more than {@code spread} away in the output image from
	 * white pixels remain opaque. Transparent pixels more than {@code spread}
	 * away in the output image from black remain transparent. In between, we
	 * get a smooth transition from opaque to transparent, with an alpha value
	 * of 0.5 when we we are exactly on the edge.
	 *
	 * @param inImage
	 * @return Pixmap Distance field image.
	 */
	public Pixmap generateDistanceField(Pixmap inImage) {
		int downscale = getDownscale();

		final int inWidth = inImage.getWidth();
		final int inHeight = inImage.getHeight();
		final int outWidth = inWidth / downscale;
		final int outHeight = inHeight / downscale;
		final Pixmap outImage = new Pixmap(outHeight, outHeight, Pixmap.Format.RGBA8888);

		// Reverse coordinates for better memory access (?).
		final boolean[][] bitmap = new boolean[inHeight][inWidth];
		int x, y;
		for (y = 0; y < inHeight; y++) {
			for (x = 0; x < inWidth; x++) {
				bitmap[y][x] = isInside(inImage.getPixel(x, y));
			}
		}

		int centerX, centerY;
		float signedDistance;
		Color color = getColor();
		for (y = 0; y < outHeight; y++) {
			for (x = 0; x < outWidth; x++) {
				centerX = (x * downscale) + (downscale / 2);
				centerY = (y * downscale) + (downscale / 2);
				signedDistance = findSignedDistance(centerX, centerY, bitmap);

				outImage.setColor(color.r, color.g, color.b, distanceToRGB(signedDistance));
				outImage.drawPixel(x, y);
			}
		}

		return outImage;
	}

	/**
	 * Returns {@code true} if the color is considered as the "inside" of the
	 * image, {@code false} if considered "outside".
	 *
	 * <p> Any color with one of its color channels >= 128
	 * <em>and</em> its alpha channel >= 128 is considered "inside".
	 * @param rgba
	 * @return boolean
	 */
	private boolean isInside(int rgba) {
		return (rgba & 0x80808000) != 0 && (rgba & 0x00000080) != 0;
	}

	/**
	 * For a distance as returned by {@link #findSignedDistance}, returns the
	 * corresponding alpha value.
	 *
	 * @param signedDistance Signed distance of a pixel.
	 * @return float alpha
	 */
	private float distanceToRGB(float signedDistance) {
		float alpha = 0.5f + 0.5f * (signedDistance / getSpread());
		return Math.min(1, Math.max(0, alpha)); // Compensate for rounding errors.
	}

	/**
	 * Returns the signed distance for a given point.
	 *
	 * For points "inside", this is the distance to the closest "outside" pixel.
	 * For points "outside", this is the <em>negative</em> distance to the
	 * closest "inside" pixel.
	 *
	 * If no pixel of different color is found within a radius of
	 * {@code spread}, returns the {@code -spread} or {@code spread},
	 * respectively.
	 *
	 * @param centerX x-coordinate of the center point.
	 * @param centerY y-coordinate of the center point.
	 * @param bitmap Array representation of an image, {@code true} representing "inside".
	 * @return float Signed distance.
	 */
	private float findSignedDistance(final int centerX, final int centerY, boolean[][] bitmap) {
		final int width = bitmap[0].length;
		final int height = bitmap.length;
		final boolean base = bitmap[centerY][centerX];

		final int delta = (int) Math.ceil(getSpread());
		final int startX = Math.max(0, centerX - delta);
		final int endX = Math.min(width - 1, centerX + delta);
		final int startY = Math.max(0, centerY - delta);
		final int endY = Math.min(height - 1, centerY + delta);

		int closestDistanceSquared = delta * delta;

		int x, y;
		int distanceSquared;
		for (y = startY; y <= endY; y++) {
			for (x = startX; x <= endX; x++) {
				if (base != bitmap[y][x]) {
					distanceSquared = distanceSquared(centerX, centerY, x, y);
					if (distanceSquared < closestDistanceSquared) {
						closestDistanceSquared = distanceSquared;
					}
				}
			}
		}

		float closestDistance = (float) Math.sqrt(closestDistanceSquared);
		return (base ? 1 : -1) * Math.min(closestDistance, getSpread());
	}
}
