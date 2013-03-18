#ifdef GL_ES
precision mediump float;
#endif;

uniform vec4 color;

uniform float width;
uniform float height;
uniform float segmentLength;
uniform float segmentSpacing;

// TODO: Offset is not implemented.
// Just use v_texCoord - transformed_offset.
uniform float offset;

varying vec2 v_texCoord;

void main() {
  // Convert to local coordinate system.
  float inverse_width = 1.0 / width;
  float segment_length = segmentLength * inverse_width;
  float segment_spacing = segmentSpacing * inverse_width;

  float total_length = segment_length + segment_spacing;
  float sector = mod(v_texCoord.x, total_length);

  float stroke_width = 2.56 / width;
  float stroke_height = 2.56 / height;

  if (sector > segment_length) {
    discard;
  }

  gl_FragColor = color;

  // Top side of segment.
  float segment_top = 1.0 - stroke_height;
  if (v_texCoord.y > segment_top) {
    gl_FragColor.a = mix(gl_FragColor.a, 0.0, smoothstep(segment_top, 1.0, v_texCoord.y));
  }

  // Bottom side of segment.
  if (v_texCoord.y < stroke_height) {
    gl_FragColor.a = mix(0.0, gl_FragColor.a, smoothstep(0.0, stroke_height, v_texCoord.y));
  }

  // Right side of segment.
  float segment_right = segment_length - stroke_width;
  if (sector > segment_right) {
    gl_FragColor.a = mix(gl_FragColor.a, 0.0, smoothstep(segment_right, segment_length, sector));
  }

  // Left side of segment.
  if (sector < stroke_width) {
    gl_FragColor.a = mix(0.0, gl_FragColor.a, smoothstep(0.0, stroke_width, sector));
  }
}
