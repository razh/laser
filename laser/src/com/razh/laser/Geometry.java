package com.razh.laser;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Geometry {

	public static Mesh createRing(float outerRadius, float innerRadius,
	                              float startAngle, float endAngle,
	                              int subdivisions, boolean anticlockwise) {

		// Two vertices on outer and inner radii with two components each.
		int vertexCount = subdivisions * 4;
		int indexCount = subdivisions * 2;

		Mesh mesh = new Mesh(Mesh.VertexDataType.VertexBufferObject,
		                     true, vertexCount, indexCount,
		                     new VertexAttribute(Usage.Position, 2,
		                                         ShaderProgram.POSITION_ATTRIBUTE));

		float[] vertices = new float[vertexCount];
		short[] indices = new short[indexCount];

		float sweepAngle = endAngle - startAngle;

		// Check if circle.
		if (Math.abs(sweepAngle) >= 2 * Math.PI) {
			endAngle = (float) (startAngle + 2 * Math.PI);
		} else {
			// Anticlockwise arcs have negative sweepAngles.
			// Clockwise arcs have positive sweepAngles.
			// If the the above is false, flip the sweepAngle to the correct sign.
			if (anticlockwise && sweepAngle > 0) {
				sweepAngle -= 2 * Math.PI;
			} else if (!anticlockwise && sweepAngle < 0) {
				sweepAngle += 2 * Math.PI;
			}
		}
		
		float subdivAngle = sweepAngle / subdivisions;
		
		for (int i = 0; i < subdivisions; i++) {
			
		}
		
		return mesh;
	}
}
