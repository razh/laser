#ifdef GL_ES
precision mediump float;
#endif

uniform float radius;
uniform vec4 color;

varying vec2 v_texCoord;

void main() {
  float x = v_texCoord.x - 0.5;
  float y = v_texCoord.y - 0.5;
  float distance_squared = x * x + y * y;
  if (distance_squared > 0.25) {
    discard;
  }

  float stroke_width = 2.56 / radius;
  float stroke_radius = (0.5 - stroke_width) * (0.5 - stroke_width);
  gl_FragColor = color;
  gl_FragColor.a = mix(color.a, 0.0, smoothstep(stroke_radius, 0.25, distance_squared));
}
