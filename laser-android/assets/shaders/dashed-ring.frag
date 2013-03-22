#ifdef GL_ES
precision mediump float;
#endif

const float DEGREES_TO_RADIANS = 57.29578;

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

  float outer = outerRadius * outerRadius;
  float inner = innerRadius * innerRadius;

  if (inner > distance_squared) {
    discard;
  }

  if (distance_squared > outer ) {
    discard;
  }

  float angle = atan(y, x);
  // 180 / PI ~= 57.2958.
  float sector = mod(angle * DEGREES_TO_RADIANS, segmentAngle);

  // Segment left.
  if (sector < segmentSpacing) {
    discard;
  }

  // Segment right.
  if (sector > segmentAngle - segmentSpacing) {
    discard;
  }

  gl_FragColor = color;
}
