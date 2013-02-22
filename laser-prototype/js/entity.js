var Entity = function() {
  Object2D.call( this );

  this._shapes = [];
};

Entity.prototype.update = function( elapsedTime ) {};

Entity.prototype.draw = function( ctx ) {
  ctx.save();

  ctx.translate( this.getX(), this.getY() );
  ctx.rotate( this.getRotation() );
  ctx.scale( this.getWidth(), this.getHeight() );

  var shapes = this.getShapes();
  for ( var i = 0, n = shapes.length; i < n; i++ ) {
    shapes[i].draw( ctx );
  }

  ctx.restore();
};

Entity.prototype.getShapes = function() {
  return this._shapes;
};

Entity.prototype.addShape = function( shape ) {
  this.getShapes().push( shape );
  return this;
};

Entity.prototype.removeShape = function( shape ) {

};

// JSON.
Entity.prototype.fromJSON = function( json ) {
  Object2D.prototype.fromJSON.call( this, json );

  var jsonObject = JSON.parse( json );

  return this;
};

Entity.prototype.toJSON = function() {
  var object = {};

  return object;
};
