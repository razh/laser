var Shape = function() {
  Object2D.call( this );

  this._color = new Color( 255, 255, 255, 1.0 );

  this._vertices = [];
  this._edges = [];
};

Shape.prototype = new Object2D();
Shape.prototype.constructor = Shape;

Shape.prototype.update = function( elapsedTime ) {};

Shape.prototype.drawPath = function( ctx ) {
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

Shape.prototype.draw = function( ctx ) {
  ctx.save();

  ctx.translate( this.getX(), this.getY() );
  ctx.rotate( this.getRotation() );
  ctx.scale( this.getWidth(), this.getHeight() );

  this.drawPath( ctx );

  ctx.fillStyle = this.getColor().toString();
  ctx.fill();

  ctx.restore();
};

Shape.prototype.hit = function( x, y ) {
  if ( this.contains( x, y ) ) {
    return this;
  }

  return null;
};

Shape.prototype.contains = function( x, y ) {
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

Shape.prototype.getColor = function() {
  return this._color;
};

Shape.prototype.setColor = function() {
  this.getColor().set.apply( this.getColor(), arguments );
  return this;
};

// Geometry.
Shape.prototype.getVertices = function() {
  return this._vertices;
};

Shape.prototype.setVertices = function( vertices ) {
  this._vertices = vertices;
  return this;
};

Shape.prototype.getEdges = function() {
  return this._edges;
};

Shape.prototype.setEdges = function( edges ) {
  this._edges = edges;
  return this;
};

Shape.prototype.getGeometry = function() {
  return {
    vertices: this._vertices,
    edges: this._edges
  };
};

Shape.prototype.setGeometry = function() {
  if ( arguments.length === 1 ) {
    this.setVertices( arguments[0].vertices );
    this.setEdges( arguments[0].edges );
  } else if ( arguments.length === 2 ) {
    this.setVertices( arguments[0] );
    this.setVertices( arguments[1] );
  }

  return this;
};

// JSON.
Shape.prototype.fromJSON = function( json ) {
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

Shape.prototype.toJSON = function() {
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
