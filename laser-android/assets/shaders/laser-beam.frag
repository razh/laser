#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float radius;
uniform float time;

varying vec4 v_color;
varying vec2 v_texCoord;

float rand(vec2 seed){
    return fract(sin(dot(seed, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
  vec4 color = texture2D(u_texture, v_texCoord);
  float x = v_texCoord.x;
  float y = v_texCoord.y;
  float halfWidth = rand(vec2(time)) * 0.1;
  if (y < 0.3) {
    color = mix(vec4(1.0, 0.0, 0.0, 0.0), vec4(1.0, 0.0, 0.0, 0.5), smoothstep(0.0, 0.3, y));
  } else if (y < 0.5 - halfWidth) {
    color = mix(vec4(1.0, 0.0, 0.0, 0.5), color, smoothstep(0.3, 0.5 - halfWidth, y));
  } else if (y > 0.7) {
    color = mix(vec4(1.0, 0.0, 0.0, 0.5), vec4(1.0, 0.0, 0.0, 0.0), smoothstep(0.7, 1.0, y));
  } else if (y > 0.5 + halfWidth) {
    color = mix(color, vec4(1.0, 0.0, 0.0, 0.5), smoothstep(0.5 + halfWidth, 0.7, y));
  }
  gl_FragColor = color;
}
