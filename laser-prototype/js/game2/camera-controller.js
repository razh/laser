/**
 * This thing controls the camera, updating it so that it's in
 * the right position, with every visible entity fitting inside.
 */

var CameraController = function( game, camera ) {
  this._game = game || new Game();
  this._camera = camera || new Camera();
  this._padding = 100;
};

CameraController.prototype.update = function() {
  var camera = this.getCamera();

  var entities = this.getGame().getEntities();
  // Determine bounding box for the entities.
  var xmin = Number.MAX_VALUE,
      ymin = Number.MAX_VALUE,
      xmax = Number.MIN_VALUE,
      ymax = Number.MIN_VALUE;
  var x, y;
  for ( var i = 0, n = entities.length; i < n; i++ ) {
    x = entities[i].getX();
    y = entities[i].getY();

    if ( x < xmin ) {
      xmin = x;
    }
    if ( y < ymin ) {
      ymin = y;
    }
    if ( x > xmax ) {
      xmax = x;
    }
    if ( y > ymax ) {
      ymax = y;
    }
  }

  var dx = xmax - xmin,
      dy = ymax - ymin;

  var center = {
    x: xmin + 0.5 * dx,
    y: ymin + 0.5 * dy
  };

  if ( dy / dx < camera.getAspectRatioAsFloat() ) {

  }

  var padding = this.getPadding();
  camera.setViewport( xmin - padding, ymin - padding, dx + 2 * padding, dy + 2 * padding, this.getGame().WIDTH, this.getGame().HEIGHT );
};

CameraController.prototype.getGame = function() {
  return this._game;
};

CameraController.prototype.setGame = function( game ) {
  this._game = game;
};

CameraController.prototype.getCamera = function() {
  return this._camera;
};

CameraController.prototype.setCamera = function( camera ) {
  this._camera = camera;
};

CameraController.prototype.getPadding = function() {
  return this._padding;
};

CameraController.prototype.setPadding = function( padding ) {
  this._padding = padding;
};
