#ifdef GL_ES
precision mediump float;
#endif

uniform vec4 color;

varying vec2 v_texCoord;

void main() {
  float x = v_texCoord.x - 0.5;
  float y = v_texCoord.y - 0.5;
  float distance_squared = x * x + y * y;
  gl_FragColor = mix(color, vec4(0.0), smoothstep(0.24, 0.25, distance_squared));
}
