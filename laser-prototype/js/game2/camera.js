var Camera = function() {
  Object2D.call( this );

  this._widthRatio = 4;
  this._heightRatio = 3;
};

Camera.prototype = new Object2D();
Camera.prototype.constructor = Camera;

Camera.prototype.getWidthRatio = function() {
  return this._widthRatio;
};

Camera.prototype.setWidthRatio = function( widthRatio ) {
  this._widthRatio = widthRatio;
  return this;
};

Camera.prototype.getHeightRatio = function() {
  return this._heightRatio;
};

Camera.prototype.setHeightRatio = function( heightRatio ) {
  this._heightRatio = heightRatio;
  return this;
};
