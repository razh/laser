package com.razh.laser;

import com.badlogic.gdx.math.Vector2;

public class Intersection {

	public static final float EPSILON = 1e-5f;

	/**
	 * Returns the nearest intersection point of the ray and the line segment x + sy, where
	 * 0 <= s <= 1. Need to convert this point back into world space.
	 * @param ray
	 * @param x0 x-coordinate of first point in line segment.
	 * @param y0 y-coordinate of first point in line segment.
	 * @param x1 x-coordinate of second point in line segment.
	 * @param y1 y-coordinate of second point in line segment.
	 * @return Vector2 Coordinate of intersection, or null if no intersection.
	 */
	public static Vector2 raySegment(final Ray2D ray, final float x0, final float y0, final float x1, final float y1) {
		float parameter = raySegmentParameter(ray, x0, y0, x1, y1);
		if (parameter != Float.NaN || parameter < 0 ) {
			return null;
		}

		return ray.getEndPoint(parameter);
	}

	/**
	 * Returns the parameter of the nearest intersection point of the ray and the
	 * line segment x + sy, where 0 <= s <= 1.
	 * @param ray
	 * @param x0 x-coordinate of first point in line segment.
	 * @param y0 y-coordinate of first point in line segment.
	 * @param x1 x-coordinate of second point in line segment.
	 * @param y1 y-coordinate of second point in line segment.
	 * @return float Parameter of intersection point along ray, or Float.NaN if no intersection.
	 */
	public static float raySegmentParameter(final Ray2D ray, final float x0, final float y0, final float x1, final float y1) {
		/*
			Given the parametric equation of a ray:

				x(t) = rx + t * dx;
				y(t) = ry + t * dy;

			Where t >= 0. r denotes the ray origin, t is the parameter, and d is
			the direction of the ray.

			And the parametric equation of a line segment given by (x, y) and
			(i, j):

				x(s) = sx + (1 - s)i;
				y(s) = sy + (1 - s)j;

			Where the parameter 0 <= s <= 1.

			Set them equal to each other:

				x(t) = rx + t * dx = sx + (1 - s)i

			Which can be written as:

				rx + t * dx       = sx + i - si
				t * dx - sx + si  = i - rx
				t * dx + (i - x)s = i - rx

			Such that, with a similar calculation for the y-component, we get the
			following system of equations:

				dx * t + (i - x) * s = i - rx
				dy * t + (j - y) * s = j - ry

			In matrix form (Ax = b), this is:

				[ dx  (i - x) ] [ t ]     [ i - rx ]
				[ dy  (j - y) ] [ s ]  =  [ j - ry ]

			To solve for t, we multiply the right-hand side by the matrix inverse,
			where:

				A^-1 =      1         [ (j - y)  -(i - x) ]
				       ----------  *  [  -dy        dx    ]
				         det(A)

			and det(A) = dx * (j - y) - dy * (i - x).
		*/
		float rx = ray.origin.x;
		float ry = ray.origin.y;
		float dx = ray.direction.x;
		float dy = ray.direction.y;

		// Compute determinant.
		float det = dx * (y1 - y0) - dy * (x1 - x0);

		// Parameter.
		float t;
		// If determinant is 0, ray and line segment are parallel.
		if (Math.abs(det) < EPSILON) {
			float t0, t1;
			// Check if the start and end point of the line segment lie on the ray.
			if (Math.abs(dx) > EPSILON) {
				t0 = (x0 - rx) / dx;
				t1 = (x1 - rx) / dx;
			} else if (Math.abs(dy) > EPSILON) {
				t0 = (y0 - rx) / dx;
				t1 = (y1 - ry) / dx;
			} else {
				return Float.NaN;
			}

			// Get lowest positive parameter.
			if (t0 >= 0 && t1 >= 0) {
				t = Math.min(t0, t1);
			} else if (t0 >= 0) {
				t = t0;
			} else if (t1 >= 0) {
				t = t1;
			} else {
				return Float.NaN;
			}

			// Find parameter along line segment.
			Vector2 point = ray.getEndPoint(t);
			float sx = (point.x - x1) / (x0 - x1);
			float sy = (point.y - y1) / (y0 - y1);

			// Discard if parameter is outside of line segment.
			// If line segment is vertical/horizontal, either sx/sy will be NaN.
			// The following check will not quit if this is the case.
			// If one is NaN, then difference between sx and sy will be NaN which
			// is not greater than EPSILON.
			if (0 > sx || sx > 1 ||
			    0 > sy || sy > 1 ||
			    Math.abs(sx - sy) > EPSILON) {
				return Float.NaN;
			}
		} else {
			// Otherwise, use the method described above.
			float detInverse = 1.0f / det;

			// Matrix inverse.
			float a = detInverse *  (y1 - y0);
			float b = detInverse * -(x1 - x0);
			float c = detInverse * -dy;
			float d = detInverse *  dx;

			// Calculate s first to check if intersection is on line segment.
			float s = c * (x1 - rx) + d * (y1 - ry);
			// Intersection point is not on segment.
			if (0 > s || s > 1) {
				return Float.NaN;
			}

			t = a * (x1 - rx) + b * (y1 - ry);
		}

		// Intersection is on segment, but not the ray.
		if (t < 0) {
			return Float.NaN;
		}

		return t;
	}

	public static Vector2 rayCircle(final Ray2D ray, final float cx, final float cy, final float r) {
		float parameter = rayCircleParameter(ray, cx, cy, r);
		if (parameter != Float.NaN || parameter < 0) {
			return null;
		}

		return ray.getEndPoint(parameter);
	}

	public static Vector2 rayAABB(final Ray2D ray, final float x0, final float y0, final float x1, final float y1) {
		float parameter = rayAABBParameter(ray, x0, y0, x1, y1);
		if (parameter != Float.NaN || parameter < 0) {
			return null;
		}

		return ray.getEndPoint(parameter);
	}

	/**
	 * Returns the parameter of the nearest intersection point of the ray and
	 * the axis-aligned bounding box given by [(x0, y0), (x1, y1)].
	 * @param ray
	 * @param x0 x-coordinate of first point in AABB.
	 * @param y0 y-coordinate of first point in AABB.
	 * @param x1 x-coordinate of second point in AABB.
	 * @param y1 y-coordinate of second point point in AABB.
	 * @return float Parameter of intersection point along ray, Float.NaN if no intersection.
	 */
	public static float rayAABBParameter(final Ray2D ray, final float x0, final float y0, final float x1, final float y1) {
		// Project the ray on to each line segment( assumng (x0, y0 ) is min,
		// though it does't matter).
		float[] parameters = new float[4];
		// Left.
		parameters[0] = Intersection.raySegmentParameter(ray, x0, y0, x0, y1);
		// Right.
		parameters[1] = Intersection.raySegmentParameter(ray, x1, y0, x1, y1);
		// Top.
		parameters[2] = Intersection.raySegmentParameter(ray, x0, y1, x1, y1);
		// Bottom.
		parameters[3] = Intersection.raySegmentParameter(ray, x0, y0, x1, y0);

		// Determine minimum positive parameter of intersection points.
		float min = -1.0f;
		// Default value (if all four intersections are Float.NaN, return Float.NaN).
		float t;
		for (int i = 0, n = parameters.length; i < n; i++) {
			t = parameters[i];
			if (t == Float.NaN || t < 0) {
				continue;
			}

			// If min is negative, the value has not been initialized.
			// We want t's >= 0 and less than min.
			if (min < 0 || t < min) {
				min = t;
			}
		}

		t = min;
		if (t < 0) {
			return Float.NaN;
		}

		return t;
	}

	/**
	 * Returns the parameter of the nearest intersection point of the ray with
	 * the circle given by (x - cx)^2 + (y - cy)^2 <= r.
	 * @param ray
	 * @param cx x-coordinate of circle center.
	 * @param cy y-coordinate of circle center.
	 * @param r  radius of circle.
	 * @return float Parameter of intersection point along ray, or Float.NaN if no intersection.
	 */
	public static float rayCircleParameter(final Ray2D ray, final float cx, final float cy, final float r) {
		/* Given the parametric equation of a ray:

			x(t) = rx + t * dx
			y(t) = ry + t * dy

		Plug these into the equation of a circle: x(t) ^ 2 + y(t) ^ 2 = r ^ 2
		(first, translate coordinates to the circle's center) to get:

			(rx + t * dx)^2 + (ry + t * dy)^2 - r^2 = 0

		which expands to:

			(rx^2 + 2rxdx * t + t^2 * dx^2) +
			(ry^2 + 2rydy * t + t^2 * dy^2) - r^2 = 0

		This can be written as:

			(dx^2 + dy^2)       * t^2 +
			(2 * (rxdx + rydy)) * t     +
			(rx^2 + ry^2 - r^2)

		such that the coefficients of the quadratic equation are:

			a = dx^2 + dy^2
			b = 2 * (rxdx + rydy)
			c = rx^2 + ry^2 - r^2

		The discriminant is thus: b^2 - 4ac.

		If the discriminant = 0, there is one intersection point.
		If the discriminant > 0, there are two intersection points.
		If the discriminant < 0, there are no intersection points.

		These intersection points lie on the ray if t >= 0.
		*/

		float rx = ray.origin.x;
		float ry = ray.origin.y;
		float dx = ray.direction.x;
		float dy = ray.direction.y;

		// Translate ray to circle space.
		rx -= cx;
		ry -= cy;

		// Compute coefficients.
		float a = (dx * dx) + (dy * dy);
		float b = 2 * (rx * dx + ry * dy);
		float c  = (rx * rx) + (ry * ry) - (r * r);

		// Compute discriminant.
		float d = b * b - 4 * a * c;

		if (d < 0) {
			return Float.NaN;
		}

		float t;
		// Near zero or sero discriminent.
		if (Math.abs(d) < EPSILON) {
			t = -b / (2 * a);
		} else {
			// The lowest positive parameter gives us the intersection point relative to the ray.
			float t0 = (float) ((-b - Math.sqrt(d)) / (2 * a));
			float t1 = (float) ((-b + Math.sqrt(d)) / (2 * a));

			// Both parameters are >= 0, so use the lower one.
			if (t0 >= 0 && t1 >= 0) {
				t = Math.min(t0, t1);
			} else if (t0 >= 0) {
				// Only need to check if t0 is positive (t1 must be negative).
				t = t0;
			} else {
				// Handles case where t1 >= 0 and t0 < 0
				// as well as the case where both t0 and t1 are < 0.
				t = t1;
			}
		}

		// The circle intersects the line but not the ray.
		if (t < 0) {
			return Float.NaN;
		}

		return t;
	}
}
