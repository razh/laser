var Shape = function() {
  Object2D.call( this );

  this._color = new Color( 255, 255, 255, 1.0 );

  this._vertices = [];
  this._indices = [];

  this._aabb = {
    xmin: 0.0,
    ymin: 0.0,
    xmax: 0.0,
    ymax: 0.0
  };

  this._radius = 0.0;

  this.debugRay = {
    x: 0,
    y: 0,
    dx: 0,
    dy: 0
  };
  this.debugNormals = [];
};

Shape.prototype = new Object2D();
Shape.prototype.constructor = Shape;

Shape.prototype.drawPath = function( ctx ) {
  ctx.beginPath();

  var x = this._vertices[ 2 * this._indices[0] ],
      y = this._vertices[ 2 * this._indices[0] + 1 ];

  ctx.moveTo( x, y );

  for ( var i = 1, n = this._indices.length; i < n; i++ ) {
    x = this._vertices[ 2 * this._indices[i] ];
    y = this._vertices[ 2 * this._indices[i] + 1 ];

    ctx.lineTo( x, y );
  }

  ctx.closePath();
};

Shape.prototype.draw = function( ctx ) {
  if ( !this.isVisible() ) {
    return;
  }

  ctx.save();

  ctx.translate( this.getX(), this.getY() );
  ctx.rotate( this.getRotation() );
  ctx.scale( this.getWidth(), this.getHeight() );

  this.drawPath( ctx );

  ctx.fillStyle = this.getColor().toString();
  ctx.fill();

  // Draw debug circle.
  ctx.beginPath();
  // ctx.moveTo( 0, 0 );
  ctx.arc( 0, 0, this.getRadius(), 0, Math.PI * 2, true );
  // Draw debug AABB.
  var aabb = this.getAABB();
  ctx.moveTo( aabb.xmin, aabb.ymin );
  ctx.lineTo( aabb.xmax, aabb.ymin );
  ctx.lineTo( aabb.xmax, aabb.ymax );
  ctx.lineTo( aabb.xmin, aabb.ymax );
  ctx.lineTo( aabb.xmin, aabb.ymin );

  // Draw debug ray.
  // var ray = this.debugRay;
  // ctx.moveTo( ray.x, ray.y );
  // ctx.lineTo( ray.x + 1000 * ray.dx, ray.y + 1000 * ray.dy );
  // // console.log( ray.x + ', ' + (ray.x + 1000 * ray.dx) + ', ' + ray.y + ', ' + (ray.y + 1000 * ray.dy))

  ctx.strokeStyle = this.getColor().toString();
  ctx.lineWidth = 0.005;
  ctx.closePath();
  ctx.stroke();

  // Draw debug normal.
  var normal;
  ctx.beginPath();
  for ( var i = 0, n = this.debugNormals.length; i < n; i++ ) {
    normal = this.debugNormals[i];
    ctx.moveTo( normal.x, normal.y );
    ctx.lineTo( normal.x + 0.1 * normal.dx, normal.y + 0.1 * normal.dy );
    ctx.strokeStyle = 'rgba( 0, 255, 127, 1.0 )';
    ctx.stroke();
  }

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

Shape.prototype.getIndices = function() {
  return this._indices;
};

Shape.prototype.setIndices = function( indices ) {
  this._indices = indices;
  return this;
};

Shape.prototype.getGeometry = function() {
  return {
    vertices: this._vertices,
    indices: this._indices
  };
};

Shape.prototype.setGeometry = function() {
  if ( arguments.length === 1 ) {
    this.setVertices( arguments[0].vertices );
    this.setIndices( arguments[0].indices );
  } else if ( arguments.length === 2 ) {
    this.setVertices( arguments[0] );
    this.setIndices( arguments[1] );
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
  var indices = this.getIndices();

  var x, y;
  // Handle degenerate case.
  if ( vertices.length === 0 || indices.length === 0 ) {
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
  for ( var i = indices.length - 1; i >= 0; i-- ) {
    x = vertices[ 2 * indices[i] ];
    y = vertices[ 2 * indices[i] + 1 ];

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

/**
 * Precalculates the radius of the given shape.
 */
Shape.prototype.calculateRadius = function() {
  var distanceSquared = 0;

  var vertices = this.getVertices();
  var indices = this.getIndices();

  var x, y;
  for ( var i = indices.length - 1; i >= 0; i-- ) {
    x = vertices[ 2 * indices[i] ];
    y = vertices[ 2 * indices[i] + 1 ];

    distanceSquared = Math.max( distanceSquared, x * x + y * y );
  }

  this._radius = Math.sqrt( distanceSquared );
};

/**
 * Returns the nearest intersection point and geometry normal of this shape with
 * the ray given by r + td, where t >= 0.
 * @param  {number} rx x-coordinate of ray origin in world/parent space.
 * @param  {number} ry y-coordinate of ray origin in world/parent space.
 * @param  {number} dx x-direction of ray in world/parent space.
 * @param  {number} dy y-direction of ray in world/parent space.
 * @return {{intersection: {x: number, y: number},  Coordinate of intersection
 *           normal:       {x: number, y: number}}} and geometry normal, or
 *                                                  null if no intersecton.
 */
Shape.prototype.intersectsRay = function( rx, ry, dx, dy ) {
  var rayOrigin = this.worldToLocalCoordinates( rx, ry );
  var rayDirection = this.worldToLocalCoordinates( rx + dx, ry + dy );

  rayDirection.x -= rayOrigin.x;
  rayDirection.y -= rayOrigin.y;

  this.debugRay = {
    x: rayOrigin.x,
    y: rayOrigin.y,
    dx: rayDirection.x,
    dy: rayDirection.y
  };

  // Check against bounding circle.
  var t = Intersection.rayCircleParameter( rayOrigin.x, rayOrigin.y,
                                           rayDirection.x, rayDirection.y,
                                           0, 0,
                                           this.getRadius() );

  if ( t === null || t < 0 ) {
    return null;
  }

  // Check against bounding box.
  var aabb = this.getAABB();
  t = Intersection.rayAABBParameter( rayOrigin.x, rayOrigin.y,
                                     rayDirection.x, rayDirection.y,
                                     aabb.xmin, aabb.ymin,
                                     aabb.xmax, aabb.ymax );

  if ( t === null || t < 0 ) {
    return null;
  }

  return Intersection.rayGeometryParameter( rayOrigin.x, rayOrigin.y,
                                            rayDirection.x, rayDirection.y,
                                            this.getGeometry() );
};

// JSON.
Shape.prototype.fromJSON = function( json ) {
  Object2D.prototype.fromJSON.call( this, json );

  var jsonObject = JSON.parse( json );

  var color = new Color().fromJSON( JSON.stringify( jsonObject.color || {} ) );
  return this.setColor( color )
             .setGeometry( jsonObject.vertices || [],
                           jsonObject.indices || [] );
};

Shape.prototype.toJSON = function() {
  var object = Object2D.prototype.toJSON.call( this );

  object.color = this.getColor().toJSON();

  object.vertices = this.getVertices();
  object.indices = this.getIndices();

  return object;
};
