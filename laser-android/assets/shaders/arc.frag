#ifdef GL_ES
precision mediump float;
#endif

const float RADIANS_TO_DEGREES = 0.01745;
const float EPSILON = 0.001;

uniform vec4 color;

uniform float outerRadius;
uniform float innerRadius;

uniform float leftAngle;
uniform float rightAngle;

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

  if (distance_squared > outer) {
    discard;
  }

  float angle = atan(y, x);

  float left_angle = leftAngle * RADIANS_TO_DEGREES;
  float right_angle = rightAngle * RADIANS_TO_DEGREES;

  // Inner.
  if (distance_squared < inner) {
    discard;
  }

  // Outer.
  if (distance_squared > outer) {
    discard;
  }

  // Left terminal.
  if (angle < EPSILON && angle > -left_angle) {
    discard;
  }

  // Right terminal.
  if (angle > EPSILON && angle < right_angle) {
    discard;
  }

  gl_FragColor = color;
}
