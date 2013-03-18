#ifdef GL_ES
precision mediump float;
#endif

uniform float size;
uniform vec4 color;

uniform float outerRadius;
uniform float innerRadius;

uniform float leftAngle;
uniform float rightAngle;

varying vec2 v_texCoord;

void main() {
  float x = v_texCoord.x - 0.5;
  float y = v_texCoord.y - 0.5;
  float distance_squared = x * x + y * y;

  float stroke_width = 2.56 / size;

  float outer_stroke = outerRadius * outerRadius;
  float inner_stroke = (innerRadius - stroke_width) * (innerRadius - stroke_width);

  float outer = (outerRadius - stroke_width) * (outerRadius - stroke_width);
  float inner = innerRadius * innerRadius;

  /**
   * Layer order:
   *
   *   inner_stroke | inner | outer | outer_stroke
   */

  if (inner_stroke > distance_squared) {
    discard;
  }

  if (distance_squared > outer_stroke) {
    discard;
  }

  float angle = atan(y, x);
  float stroke_angle = 2.0 * stroke_width;

  gl_FragColor = color;

  // Inner.
  if (distance_squared < inner) {
    gl_FragColor.a = mix(0.0, color.a, smoothstep(inner_stroke, inner, distance_squared));
  }

  // Outer.
  if (distance_squared > outer) {
    gl_FragColor.a = mix(color.a, 0.0, smoothstep(outer, outer_stroke, distance_squared));
  }

  // Left terminal.
  if (angle < 0.0 && angle > -leftAngle) {
    gl_FragColor.a = mix(gl_FragColor.a, 0.0, smoothstep(-leftAngle, -leftAngle + stroke_angle, angle));
  }

  // Right terminal.
  if (angle > 0.0 && angle < rightAngle) {
    gl_FragColor.a = mix(gl_FragColor.a, 0.0, smoothstep(rightAngle, rightAngle - stroke_angle, angle));
  }
}
