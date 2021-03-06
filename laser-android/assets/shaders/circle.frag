#ifdef GL_ES
precision mediump float;
#endif

uniform vec4 color;

varying vec2 v_texCoord;

void main() {
  float x = v_texCoord.x - 0.5;
  float y = v_texCoord.y - 0.5;
  float distance_squared = x * x + y * y;
  if (distance_squared > 0.25) {
    discard;
  }

  gl_FragColor = color;
}
