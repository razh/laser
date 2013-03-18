#ifdef GL_ES
precision mediump float;
#endif

uniform float size;
uniform vec4 color;

uniform float outerRadius;
uniform float innerRadius;

uniform float segmentAngle;
uniform float segmentSpacing;

varying vec2 v_texCoord;

void main() {
  float x = v_texCoord.x - 0.5;
  float y = v_texCoord.y - 0.5;
  float distance_squared = x * x + y * y;

  float stroke_width = 2.56 / size;
  float outer_stroke = (outerRadius - stroke_width) * (outerRadius - stroke_width);
  float inner_stroke = (innerRadius - stroke_width) * (innerRadius - stroke_width);

  float outer = outerRadius * outerRadius;
  float inner = innerRadius * innerRadius;

  if (inner_stroke > distance_squared) {
    discard;
  }

  if (distance_squared > outer) {
    discard;
  }

  float angle = atan(y, x);
  // 180 / PI ~= 57.2958.
  float sector = mod(angle * 57.2958, segmentAngle);
  float stroke_angle = (2.0 * stroke_width) * 57.2958;

  gl_FragColor = color;

  // Inner.
  if (distance_squared < inner) {
    gl_FragColor.a = mix(0.0, gl_FragColor.a, smoothstep(inner_stroke, inner, distance_squared));
  }

  // Outer.
  if (distance_squared > outer_stroke) {
    gl_FragColor.a = mix(gl_FragColor.a, 0.0, smoothstep(outer_stroke, outer, distance_squared));
  }

  // Segment left.
  if (sector < segmentSpacing) {
    gl_FragColor.a = mix(0.0, gl_FragColor.a, smoothstep(segmentSpacing - stroke_angle, segmentSpacing, sector));
  }

  // Segment right.
  float segment_right = segmentAngle - segmentSpacing;
  if (sector > segment_right) {
    gl_FragColor.a = mix(gl_FragColor.a, 0.0, smoothstep(segment_right, segment_right + stroke_angle, sector));
  }
}
