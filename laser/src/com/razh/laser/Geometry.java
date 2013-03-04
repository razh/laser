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
		int vertexCount = (subdivisions + 1) * 4;
		int indexCount = (subdivisions + 1) * 2;

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
			// Anticlockwise arcs have positive sweepAngles.
			// Clockwise arcs have negative sweepAngles.
			// If the the above is false, flip the sweepAngle to the correct sign.
			if (anticlockwise && sweepAngle < 0) {
				sweepAngle += 2 * Math.PI;
			} else if (!anticlockwise && sweepAngle > 0) {
				sweepAngle -= 2 * Math.PI;
			}
		}

		float subdivAngle = sweepAngle / subdivisions;

		int vtxIndex = 0;
		int idxIndex = 0;

		float cos, sin;
		for (int i = 0; i < subdivisions + 1; i++) {
			cos = (float) Math.cos(startAngle + i * subdivAngle);
			sin = (float) Math.sin(startAngle + i * subdivAngle);
			// Inner radius.
			vertices[vtxIndex++] = innerRadius * cos;
			vertices[vtxIndex++] = innerRadius * sin;
			// Outer radius.
			vertices[vtxIndex++] = outerRadius * cos;
			vertices[vtxIndex++] = outerRadius * sin;

			indices[idxIndex++] = (short) (2 * i);
			indices[idxIndex++] = (short) (2 * i + 1);
		}

		mesh.setVertices(vertices);
		mesh.setIndices(indices);

		return mesh;
	}

	public static Mesh createCircle(int subdivisions) {
		return createCircle(1.0f, subdivisions);
	}

	public static Mesh createCircle(float radius, int subdivisions) {
		// Subdivisions and center vertex with two components each.
		int vertexCount = (subdivisions + 1) * 2;
		// Center vertex and one connecting edge between start and end vertices.
		int indexCount = subdivisions + 2;

		Mesh mesh = new Mesh(Mesh.VertexDataType.VertexBufferObject,
		                     true, vertexCount, indexCount,
		                     new VertexAttribute(Usage.Position, 2,
		                                         ShaderProgram.POSITION_ATTRIBUTE));

		float subdivAngle = (float) (2 * Math.PI / subdivisions);

		float[] vertices = new float[vertexCount];
		short[] indices = new short[indexCount];

		int vtxIndex = 0;
		int idxIndex = 0;

		vertices[vtxIndex++] = 0.0f;
		vertices[vtxIndex++] = 0.0f;

		// Center.
		indices[idxIndex++] = 0;

		for (int i = 0; i < subdivisions; i++) {
			vertices[vtxIndex++] = (float) (radius * Math.cos(i * subdivAngle));
			vertices[vtxIndex++] = (float) (radius * Math.sin(i * subdivAngle));

			indices[idxIndex++] = (short) (i + 1);
		}

		// Close triangle fan.
		indices[idxIndex++] = 1;

		mesh.setVertices(vertices);
		mesh.setIndices(indices);

		return mesh;
	}

	public static GeometryData createRingHull(float outerRadius, float innerRadius,
	                                          float startAngle, float endAngle,
                                              int subdivisions, boolean anticlockwise) {
		float[] vertices = new float[(subdivisions + 1) * 4];
		// Two radii, plus connecting edge.
		short[] indices = new short[(subdivisions + 1) * 2 + 1];

		float sweepAngle = endAngle - startAngle;

		// Circle check.
		if (Math.abs(sweepAngle) >= 2 * Math.PI) {
			endAngle = (float) (startAngle + 2 * Math.PI);
		} else {
			if (anticlockwise && sweepAngle < 0) {
				sweepAngle += 2 * Math.PI;
			} else if (!anticlockwise && sweepAngle > 0) {
				sweepAngle -= 2 * Math.PI;
			}
		}

		float subdivAngle = sweepAngle / subdivisions;

		int vtxIndex = 0;
		int idxIndex = 0;

		// Outer radius.
		for (int i = 0; i < subdivisions + 1; i++) {
			vertices[vtxIndex++] = (float) (outerRadius * Math.cos(startAngle + i * subdivAngle));
			vertices[vtxIndex++] = (float) (outerRadius * Math.sin(startAngle + i * subdivAngle));

			indices[idxIndex++] = (short) i;
		}

		// Inner radius.
		for (int i = 0; i < subdivisions + 1; i++) {
			vertices[vtxIndex++]= (float) (innerRadius * Math.cos(endAngle - i * subdivAngle));
			vertices[vtxIndex++]= (float) (innerRadius * Math.cos(endAngle - i * subdivAngle));

			indices[idxIndex++] = (short) (i + subdivisions + 1);
		}

		// Edge connecting inner and outer radius.
		indices[idxIndex++] = 0;

		GeometryData data = new GeometryData();
		data.vertices = vertices;
		data.indices = indices;

		return data;
	}

	public static class GeometryData {
		public float[] vertices;
		public short[] indices;
	}
}
