#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

varying vec4 v_color;
varying vec2 v_texCoord;
varying float distance_squared;

void main() {
  gl_FragColor = texture2D(u_texture, v_texCoord);
  float x = v_texCoord.x - 0.5;
  float y = v_texCoord.y - 0.5;
  float distance_squared = x * x + y * y;
  gl_FragColor = mix(v_color, vec4(0.0), smoothstep(0.24, 0.25, distance_squared));
}
