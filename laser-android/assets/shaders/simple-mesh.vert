uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

attribute vec2 a_position;

void main() {
  gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(a_position, 0.0, 1.0);
}
