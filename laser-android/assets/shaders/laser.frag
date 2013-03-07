#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float radius;

varying vec4 v_color;
varying vec2 v_texCoord;

void main() {
  float y = v_texCoord.y;
  vec4 color = texture2D(u_texture, v_texCoord);
  if (y < 0.4) {
    color = mix(vec4(1.0, 0.0, 0.0, 0.0), color, smoothstep(0.0, 0.4, y));
  } else if (y > 0.6) {
    color = mix(color, vec4(1.0, 0.0, 0.0, 0.0), smoothstep(0.6, 1.0, y));
  }
  gl_FragColor = color;
}
