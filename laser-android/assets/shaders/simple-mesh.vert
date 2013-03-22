uniform mat4 u_projTrans;
uniform mat4 modelMatrix;

attribute vec2 a_position;

void main() {
  gl_Position = u_projTrans * modelMatrix * vec4(a_position, 0.0, 1.0);
}
