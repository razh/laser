package com.razh.laser;

import com.badlogic.gdx.math.Vector2;

public class Intersection {

	public static Vector2 raySegment(Ray2D ray, float x0, float y0, float x1, float y1) {
		float parameter = raySegmentParameter(ray, x0, y0, x1, y1);
		if (parameter != Float.NaN && parameter >= 0 ) {
			return ray.getEndPoint(parameter);
		}

		return null;
	}

	public static float raySegmentParameter(Ray2D ray, float x0, float y0, float x1, float y1) {
		float rx = ray.origin.x;
		float ry = ray.origin.y;
		float dx = ray.direction.x;
		float dy = ray.direction.y;

		return Float.NaN;
	}
}
