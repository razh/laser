#ifdef GL_ES
precision mediump float;
#endif

uniform float size;
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

  // Inner.
  if (distance_squared < inner) {
    discard;
  }

  // Outer.
  if (distance_squared > outer) {
    discard;
  }

  gl_FragColor = color;
}
