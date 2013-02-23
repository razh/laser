var Object2D = function() {
  this._position = {
    x: 0.0,
    y: 0.0
  };

  this._width = 1.0;
  this._height = 1.0;
  this._rotation = 0.0;
};

Object2D.prototype.getX = function() {
  return this.getPosition().x;
};

Object2D.prototype.setX = function( x ) {
  this._position.x = x;
  return this;
};

Object2D.prototype.getY = function() {
  return this.getPosition().y;
};

Object2D.prototype.setY = function( y ) {
  this._position.y = y;
  return this;
};

Object2D.prototype.getPosition = function() {
  return this._position;
};

Object2D.prototype.setPosition = function() {
  if ( arguments.length === 1 ) {
    this.setX( arguments[0].x );
    this.setY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.setX( arguments[0] );
    this.setY( arguments[1] );
  }

  return this;
};

Object2D.prototype.translateX = function( translateX ) {
  this.setX( this.getX() + translateX );
  return this;
};

Object2D.prototype.translateY = function( translateY ) {
  this.setY( this.getY() + translateY );
  return this;
};

Object2D.prototype.translate = function() {
  if ( arguments.length === 1 ) {
    this.translateX( arguments[0].x );
    this.translateY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.translateX( arguments[0] );
    this.translateY( arguments[1] );
  }

  return this;
};

Object2D.prototype.getWidth = function() {
  return this._width;
};

Object2D.prototype.setWidth = function( width ) {
  this._width = width;
  return this;
};

Object2D.prototype.getHeight = function() {
  return this._height;
};

Object2D.prototype.setHeight = function( height ) {
  this._height = height;
  return this;
};

Object2D.prototype.scale = function() {
  if ( arguments.length === 1 ) {
    this.setWidth( this.getWidth() * arguments[0] );
    this.setHeight( this.getHeight() * arguments[0] );
  } else if ( arguments.length === 2 ) {
    this.setWidth( this.getWidth() * arguments[0] );
    this.setHeight( this.getHeight() * arguments[1] );
  }

  return this;
};

Object2D.prototype.getRotation = function() {
  return this._rotation;
};

Object2D.prototype.setRotation = function( rotation ) {
  this._rotation = rotation;
  return this;
};

Object2D.prototype.rotate = function( angle ) {
  this._rotation += angle;
  return this;
};

Object2D.prototype.getRotationInDegrees = function() {
  return this._rotation * 180 / Math.PI;
};

Object2D.prototype.setRotationInDegrees = function( rotation ) {
  this._rotation = rotation * Math.PI / 180;
  return this;
};

// Coordinate transforms.
Object2D.prototype.worldToLocalCoordinates = function() {
  var x, y;
  if ( arguments.length === 1 ) {
    x = arguments[0].x;
    y = arguments[0].y;
  } else if ( arguments.length === 2 ) {
    x = arguments[0];
    y = arguments[1];
  }

  // Translate.
  x -= this.getX();
  y -= this.getY();

  // Rotate.
  var rotation = -this.getRotation();
  if ( rotation !== 0 ) {
    var cos = Math.cos( rotation ),
        sin = Math.sin( rotation );

    var rx = cos * x - sin * y,
        ry = sin * x + cos * y;

    x = rx;
    y = ry;
  }

  // Scale.
  x /= this.getWidth();
  y /= this.getHeight();

  return {
    x: x,
    y: y
  };
};

Object2D.prototype.localToWorldCoordinates = function() {
  var x, y;
  if ( arguments.length === 1 ) {
    x = arguments[0].x;
    y = arguments[0].y;
  } else if ( arguments.length === 2 ) {
    x = arguments[0];
    y = arguments[1];
  }

  // Scale.
  x *= this.getWidth();
  y *= this.getHeight();

  // Rotate.
  var rotation = this.getRotation();
  if ( rotation !== 0 ) {
    var cos = Math.cos( rotation ),
        sin = Math.sin( rotation );

    var rx = cos * x - sin * y,
        ry = sin * x + cos * y;

    x = rx;
    y = ry;
  }

  // Translate.
  x += this.getX();
  y += this.getY();

  return {
    x: x,
    y: y
  };
};

// JSON.
Object2D.prototype.fromJSON = function( json ) {
  var jsonObject = JSON.parse( json );

  return this.setX( jsonObject.x || 0 )
             .setY( jsonObject.y || 0 )
             .setWidth( jsonObject.width || 1 )
             .setHeight( jsonObject.height || 1 )
             .setRotationInDegrees( jsonObject.rotation || 0 );
};

Object2D.prototype.toJSON = function() {
  var object = {};

  object.x = this.getX();
  object.y = this.getY();
  object.width = this.getWidth();
  object.height = this.getHeight();

  // Round rotation in increments of 0.01 degrees.
  object.rotation = parseFloat( this.getRotationInDegrees().toFixed(2) );

  return object;
};

