#ifdef GL_ES
precision mediump float;
#endif

uniform vec4 color;
uniform float outerRadius;
uniform float innerRadius;

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
  if (abs(atan(y, x)) < 20.0 * 3.1415 / 180.0) {
    discard;
  }
  // gl_FragColor = mix(color, vec4(0.0), smoothstep(0.24, 0.25, distance_squared));
  gl_FragColor = color;
}
