#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float time;

varying vec4 v_color;
varying vec2 v_texCoord;

float rand(vec2 seed){
    return fract(sin(dot(seed, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
  vec4 color = texture2D(u_texture, v_texCoord);
  float x = v_texCoord.x - 0.5;
  float y = v_texCoord.y - 0.5;
  float distance_squared = x * x + y * y;
  float radius = rand(vec2(time)) * 0.1;
  radius *= radius;
  if (distance_squared < 0.04) {
    color = mix(color, vec4(1.0, 0.0, 0.0, 0.5), smoothstep(radius, 0.04, distance_squared));
  } else  {
    color = mix(vec4(1.0, 0.0, 0.0, 0.5), vec4(1.0, 0.0, 0.0, 0.0), smoothstep(0.04, 0.25, distance_squared));
  }
  gl_FragColor = color;
}
