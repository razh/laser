package com.razh.laser;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

/**
 * Two-dimensional version of libgdx's Ray class.
 * @author raz
 *
 */
public class Ray2D {
	public final Vector2 origin = new Vector2();
	public final Vector2 direction = new Vector2();

	public Ray2D(Vector2 origin, Vector2 direction) {
		this.origin.set(origin);
		this.direction.set(direction);
	}

	public Ray2D cpy() {
		return new Ray2D(this.origin, this.direction);
	}

	public Vector2 getEndPoint(float distance) {
		return direction.cpy().mul(distance).add(origin);
	}

	static Vector2 tmp = new Vector2();

	public Ray2D mul(Matrix3 matrix) {
		tmp.set(origin).add(direction);
		tmp.mul(matrix);
		origin.mul(matrix);
		direction.set(tmp.sub(origin));
		return this;
	}

	@Override
	public String toString() {
		return "ray [" + origin + ":" + direction + "]";
	}

	public Ray2D set(Vector2 origin, Vector2 direction) {
		this.origin.set(origin);
		this.direction.set(direction);
		return this;
	}

	public Ray2D set(float x, float y, float dx, float dy) {
		this.origin.set(x, y);
		this.direction.set(dx, dy);
		return this;
	}

	public Ray2D set(Ray2D ray) {
		this.origin.set(ray.origin);
		this.direction.set(ray.direction);
		return this;
	}
}
