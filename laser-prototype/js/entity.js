var Entity = function() {
  this._position = {
    x: 0.0,
    y: 0.0
  };

  this._width = 0.0;
  this._height = 0.0;
  this._rotation = 0.0;

  this._color = new Color( 255, 255, 255, 1.0 );

  this._vertices = [];
  this._edges = [];
};

Entity.prototype.update = function( elapsedTime ) {};

Entity.prototype.drawPath = function( ctx ) {
  ctx.beginPath();

  var x = this._vertices[ 2 * this._edges[0] ],
      y = this._vertices[ 2 * this._edges[0] + 1 ];
  ctx.moveTo( x, y );

  for ( var i = 1, n = this._edges.length; i < n; i++ ) {
    x = this._vertices[ 2 * this._edges[i] ];
    y = this._vertices[ 2 * this._edges[i] + 1 ];

    ctx.lineTo( x, y );
  }

  ctx.closePath();
};

Entity.prototype.draw = function( ctx ) {
  ctx.save();

  ctx.translate( this.getX(), this.getY() );
  ctx.rotate( this.getRotation() );
  ctx.scale( this.getWidth(), this.getHeight() );

  this.drawPath( ctx );

  ctx.fillStyle = this.getColor().toString();
  ctx.fill();

  ctx.restore();
};

Entity.prototype.hit = function( x, y ) {
  if ( this.contains( x, y ) ) {
    return this;
  }

  return null;
};

Entity.prototype.contains = function( x, y ) {
  var point = this.worldToLocalCoordinates( x, y );
  x = point.x;
  y = point.y;

  var vertexCount = this._vertices.length / 2;
  var contains = false;
  var xi, yi, xj, yj;
  var i, j;
  for ( i = 0, j = vertexCount - 1; i < vertexCount; j = i++ ) {
    xi = this._vertices[ 2 * i ];
    yi = this._vertices[ 2 * i + 1 ];
    xj = this._vertices[ 2 * j ];
    yj = this._vertices[ 2 * j + 1 ];

    if ( ( ( yi > y ) != ( yj > y ) ) &&
         ( x < ( xj - xi ) * ( y - yi ) / ( yj - yi ) + xi ) ) {
      contains = !contains;
    }
  }

  return contains;
};

Entity.prototype.getX = function() {
  return this.getPosition().x;
};

Entity.prototype.setX = function( x ) {
  this._position.x = x;
  return this;
};

Entity.prototype.getY = function() {
  return this.getPosition().y;
};

Entity.prototype.setY = function( y ) {
  this._position.y = y;
  return this;
};

Entity.prototype.getPosition = function() {
  return this._position;
};

Entity.prototype.setPosition = function() {
  if ( arguments.length === 1 ) {
    this.setX( arguments[0].x );
    this.setY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.setX( arguments[0] );
    this.setY( arguments[1] );
  }

  return this;
};

Entity.prototype.translateX = function( translateX ) {
  this.setX( this.getX() + translateX );
  return this;
};

Entity.prototype.translateY = function( translateY ) {
  this.setY( this.getY() + translateY );
  return this;
};

Entity.prototype.translate = function() {
  if ( arguments.length === 1 ) {
    this.translateX( arguments[0].x );
    this.translateY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.translateX( arguments[0] );
    this.translateY( arguments[1] );
  }

  return this;
};

Entity.prototype.getWidth = function() {
  return this._width;
};

Entity.prototype.setWidth = function( width ) {
  this._width = width;
  return this;
};

Entity.prototype.getHeight = function() {
  return this._height;
};

Entity.prototype.setHeight = function( height ) {
  this._height = height;
  return this;
};

Entity.prototype.scale = function() {
  if ( arguments.length === 1 ) {
    this.setWidth( this.getWidth() * arguments[0] );
    this.setHeight( this.getHeight() * arguments[0] );
  } else if ( arguments.length === 2 ) {
    this.setWidth( this.getWidth() * arguments[0] );
    this.setHeight( this.getHeight() * arguments[1] );
  }

  return this;
};

Entity.prototype.getRotation = function() {
  return this._rotation;
};

Entity.prototype.setRotation = function( rotation ) {
  this._rotation = rotation;
  return this;
};

Entity.prototype.rotate = function( angle ) {
  this._rotation += angle;
  return this;
};

Entity.prototype.getRotationInDegrees = function() {
  return this._rotation * 180 / Math.PI;
};

Entity.prototype.setRotationInDegrees = function( rotation ) {
  this._rotation = rotation * Math.PI / 180;
  return this;
};

Entity.prototype.getColor = function() {
  return this._color;
};

Entity.prototype.setColor = function() {
  this.getColor().set.apply( this.getColor(), arguments );
  return this;
};

// Geometry.
Entity.prototype.getVertices = function() {
  return this._vertices;
};

Entity.prototype.setVertices = function( vertices ) {
  this._vertices = vertices;
  return this;
};

Entity.prototype.getEdges = function() {
  return this._edges;
};

Entity.prototype.setEdges = function( edges ) {
  this._edges = edges;
  return this;
};

Entity.prototype.getGeometry = function() {
  return {
    vertices: this._vertices,
    edges: this._edges
  };
};

// Coordinate transforms.
Entity.prototype.worldToLocalCoordinates = function( x, y ) {
  // Translate.
  x -= this.getX();
  y -= this.getY();

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

  // Scale.
  x /= this.getWidth();
  y /= this.getHeight();

  return {
    x: x,
    y: y
  };
};

Entity.prototype.localToWorldCoordinates = function( x, y ) {
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
Entity.prototype.fromJSON = function( json ) {
  var jsonObject = JSON.parse( json );

  var color = new Color().fromJSON( JSON.stringify( jsonObject.color ) );

  return this.setX( jsonObject.x || 0 )
             .setY( jsonObject.y || 0 )
             .setWidth( jsonObject.width || 1 )
             .setHeight( jsonObject.height || 1 )
             .setRotationInDegrees( jsonObject.rotation || 0 )
             .setVertices( jsonObject.vertices || [] )
             .setEdges( jsonObject.edges || [] )
             .setColor( color );
};

Entity.prototype.toJSON = function() {
  var object = {};

  object.x = this.getX();
  object.y = this.getY();
  object.width = this.getWidth();
  object.height = this.getHeight();

  // Round rotation in increments of 0.01 degrees.
  object.rotation = parseFloat( this.getRotationInDegrees().toFixed(2) );

  object.color = this.getColor().toJSON();

  return object;
};
