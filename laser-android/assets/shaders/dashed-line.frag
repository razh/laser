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

  if (sector > segment_length) {
    discard;
  }

  gl_FragColor = color;
}
