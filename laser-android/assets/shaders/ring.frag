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

  float stroke_width = 2.56 / size;
  float outer_stroke = (outerRadius - stroke_width) * (outerRadius - stroke_width);
  float inner_stroke = (innerRadius - stroke_width) * (innerRadius - stroke_width);

  float outer = outerRadius * outerRadius;
  float inner = innerRadius * innerRadius;

  if (inner_stroke > distance_squared) {
    discard;
  }

  if (distance_squared > outer) {
    discard;
  }

  gl_FragColor = color;
  if (distance_squared < inner) {
    gl_FragColor = mix(vec4(0.0), color, smoothstep(inner_stroke, inner, distance_squared));
  }

  if (distance_squared > outer_stroke) {
    gl_FragColor = mix(color, vec4(0.0), smoothstep(outer_stroke, outer, distance_squared));
  }
}
