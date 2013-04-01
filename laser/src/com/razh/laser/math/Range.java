package com.razh.laser.math;

import com.badlogic.gdx.math.MathUtils;

public class Range {

	private float mStart;
	private float mEnd;

	public Range() {}

	public Range(float start, float end) {
		set(start, end);
	}

	public float getStart() {
		return mStart;
	}

	public void setStart(float start) {
		mStart = start;
	}

	public float getEnd() {
		return mEnd;
	}

	public void setEnd(float end) {
		mEnd = end;
	}

	public void set(float start, float end) {
		setStart(start);
		setEnd(end);
	}

	public void set(Range range) {
		set(range.getStart(), range.getEnd());
	}

	public float random() {
		return MathUtils.random(mStart, mEnd);
	}
}
