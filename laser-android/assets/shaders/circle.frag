#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float radius;

varying vec4 v_color;
varying vec2 v_texCoord;

void main() {
  gl_FragColor = texture2D(u_texture, v_texCoord);
  float x = v_texCoord.x - 0.5;
  float y = v_texCoord.y - 0.5;
  float distance_squared = x * x + y * y;
  vec4 color = v_color;
  if (distance_squared > 0.4 * 0.4) {
    color = mix(color, vec4(0.0, 0.5, 0.0, 1.0), smoothstep(0.16, 0.164, distance_squared));
  }
  gl_FragColor = mix(color, vec4(0.0), smoothstep(0.24, 0.25, distance_squared));
}
