var Shape = function() {
  Object2D.call( this );

  this._color = new Color( 255, 255, 255, 1.0 );

  this._vertices = [];
  this._edges = [];

  this._aabb = {
    xmin: 0.0,
    ymin: 0.0,
    xmax: 0.0,
    ymax: 0.0
  };

  this._radius = 0.0;

  this.ray = {
    x: 0,
    y: 0,
    dx: 0,
    dy: 0
  };
};

Shape.prototype = new Object2D();
Shape.prototype.constructor = Shape;

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

  // Draw debug circle.
  ctx.beginPath();
  ctx.moveTo( 0, 0 );
  ctx.arc( 0, 0, this.getRadius(), 0, Math.PI * 2, true );
  // Draw debug AABB.
  var aabb = this.getAABB();
  ctx.moveTo( aabb.xmin, aabb.ymin );
  ctx.lineTo( aabb.xmax, aabb.ymin );
  ctx.lineTo( aabb.xmax, aabb.ymax );
  ctx.lineTo( aabb.xmin, aabb.ymax );
  ctx.lineTo( aabb.xmin, aabb.ymin );

  // Draw debug ray.
  var ray = this.ray;
  ctx.moveTo( ray.x, ray.y );
  ctx.lineTo( ray.x + 1000 * ray.dx, ray.y + 1000 * ray.dy );

  ctx.strokeStyle = this.getColor().toString();
  ctx.lineWidth = 0.005;
  ctx.stroke();

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
    this.setEdges( arguments[1] );
  }

  this.calculateAABB();
  this.calculateRadius();

  return this;
};

Shape.prototype.getAABB = function() {
  return this._aabb;
};

Shape.prototype.calculateAABB = function() {
  var vertices = this.getVertices();
  var edges = this.getEdges();

  var x, y;
  // Handle degenerate case.
  if ( vertices.length === 0 || edges.length === 0 ) {
    x = this.getX();
    y = this.getY();

    this._aabb = {
      xmin: x,
      ymin: y,
      xmax: x,
      ymax: y
    };

    return;
  }

  var xmin = Number.MAX_VALUE,
      ymin = Number.MAX_VALUE,
      xmax = Number.MIN_VALUE,
      ymax = Number.MIN_VALUE;

  // Only check vertices on an edge.
  for ( var i = edges.length - 1; i >= 0; i-- ) {
    x = vertices[ 2 * edges[i] ];
    y = vertices[ 2 * edges[i] + 1 ];

    if ( x < xmin ) {
      xmin = x;
    }
    if ( xmax < x ) {
      xmax = x;
    }
    if ( y < ymin ) {
      ymin = y;
    }
    if ( ymax < y ) {
      ymax = y;
    }
  }

  this._aabb = {
    xmin: xmin,
    ymin: ymin,
    xmax: xmax,
    ymax: ymax
  };
};

Shape.prototype.getRadius = function() {
  return this._radius;
};

Shape.prototype.calculateRadius = function() {
  var distanceSquared = 0;

  var vertices = this.getVertices();
  var edges = this.getEdges();

  var x, y;
  for ( var i = edges.length - 1; i >= 0; i-- ) {
    x = vertices[ 2 * edges[i] ];
    y = vertices[ 2 * edges[i] + 1 ];

    distanceSquared = Math.max( distanceSquared, x * x + y * y );
  }

  this._radius = Math.sqrt( distanceSquared );
};

// JSON.
Shape.prototype.fromJSON = function( json ) {
  Object2D.prototype.fromJSON.call( this, json );

  var jsonObject = JSON.parse( json );

  var color = new Color().fromJSON( JSON.stringify( jsonObject.color || {} ) );
  return this.setColor( color )
             .setGeometry( jsonObject.vertices || [],
                           jsonObject.edges || [] );
};

Shape.prototype.toJSON = function() {
  var object = Object2D.prototype.toJSON.call( this );

  object.color = this.getColor().toJSON();

  object.vertices = this.getVertices();
  object.edges = this.getEdges();

  return object;
};
