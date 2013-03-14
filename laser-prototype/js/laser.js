var Laser = function() {
  // Ray origins.
  this._origins = [];
  // Ray directions.
  this._directions = [];

  this._parent = null;

  this._lineWidth = 3;
  this._color = new Color( 255, 0, 0, 0.25 );

  this._reflectionLimit = 16;
};

Laser.prototype.clear = function() {
  this._origins = [];
  this._directions = [];

  this.addOrigin( this.getParent().getPosition() );

  var rotation = this.getParent().getRotation();
  this.addDirection({
    x: Math.cos( rotation ),
    y: Math.sin( rotation )
  });
};

Laser.prototype.draw = function( ctx ) {
  var origins = this.getOrigins();
  var directions = this.getDirections();

  if ( origins.length <= 0 || directions.length <= 0 ) {
    return;
  }

  // ctx.beginPath();
  var lastPoint = origins[0];
  ctx.moveTo( lastPoint.x, lastPoint.y );

  for ( var i = 1; i < origins.length; i++ ) {
    lastPoint = origins[i];
    ctx.lineTo( lastPoint.x, lastPoint.y );
  }

  ctx.lineTo( lastPoint.x + 1000 * directions[ directions.length - 1 ].x,
              lastPoint.y + 1000 * directions[ directions.length - 1 ].y );

  ctx.strokeStyle = this.getColor().toString();
  ctx.lineWidth = this.getLineWidth();

  ctx.stroke();
};

Laser.prototype.getOrigins = function() {
  return this._origins;
};

Laser.prototype.addOrigin = function( origin ) {
  this._origins.push( origin );
};

Laser.prototype.getDirections = function() {
  return this._directions;
};

Laser.prototype.addDirection = function( direction ) {
  this._directions.push( direction );
};

Laser.prototype.addRay = function() {
  // For arguments: ({origin: [], direction: []}).
  if ( arguments.length === 1 ) {
    this.addOrigin( arguments[0].origin );
    this.addDirection( arguments[0].direction );
  }
  // For arguments: ( origin, direction ).
  else if ( arguments.length === 2 ) {
    this.addOrigin( arguments[0] );
    this.addDirection( arguments[1] );
  }
  // For arguments: ( rx, ry, dx, dy ).
  else if ( arguments.length === 4 ) {
    this.addOrigin({
      x: arguments[0],
      y: arguments[1]
    });
    this.addDirection({
      x: arguments[2],
      y: arguments[3]
    });
  }
};

Laser.prototype.getLastRay = function() {
  return {
    origin: this.getOrigins()[ this.getOrigins().length - 1 ],
    direction: this.getDirections()[ this.getDirections().length - 1 ]
  };
};

Laser.prototype.getParent = function() {
  return this._parent;
};

Laser.prototype.setParent = function( parent ) {
  this._parent = parent;
};

Laser.prototype.getLineWidth = function() {
  return this._lineWidth;
};

Laser.prototype.setLineWidth = function( lineWidth ) {
  this._lineWidth = lineWidth;
};

Laser.prototype.getColor = function() {
  return this._color;
};

Laser.prototype.setColor = function( color ) {
  this.getColor().set( color );
};

Laser.prototype.getReflectionLimit = function() {
  return this._reflectionLimit;
};

Laser.prototype.setReflectionLimit = function( reflectionLimit ) {
  this._reflectionLimit = reflectionLimit;
};

Laser.prototype.project = function( entities ) {
  this.clear();

  var reflectionLimit = this.getReflectionLimit();
  var reflectionCount = 0;

  var ray;
  var minEntityRay;

  var parent = this.getParent();
  var entity, shapes, shape;
  var entityIndex, shapeIndex;
  var min = -1;
  var intersection;
  // Variables of intersection in shape local space.
  var point, normal;
  var shapePoint;
  var dx, dy;
  var rayLength, normalLength;
  var dot, angle;
  var cos, sin;
  var direction;
  var i, j, il, jl;
  while ( reflectionCount < reflectionLimit ) {
    // Reset nearest index.
    entityIndex = -1;
    shapeIndex = -1;
    min = -1;

    // Find the entity with the minimum parameter.
    for ( i = 0, il = entities.length; i < il; i++ ) {
      ray = this.getLastRay();

      entity = entities[i];
      if ( entity === parent ) {
        continue;
      }

      ray.direction = entity.worldToLocalCoordinates( ray.origin.x + ray.direction.x,
                                                      ray.origin.y + ray.direction.y );
      ray.origin = entity.worldToLocalCoordinates( ray.origin.x, ray.origin.y );

      ray.direction.x -= ray.origin.x;
      ray.direction.y -= ray.origin.y;

      // Find the shape with the minimum parameter.
      shapes = entity.getShapes();
      for ( j = 0, jl = shapes.length; j < jl; j++ ) {
        shape = shapes[j];

        intersection = shape.intersectsRay( ray.origin.x, ray.origin.y,
                                            ray.direction.x, ray.direction.y );

        if ( intersection === null || intersection.parameter < 0 ) {
          continue;
        }

        // We've found a positive parameter smaller than current min.
        if ( min < 0 || intersection.parameter < min ) {
          minEntityRay = ray;
          min = intersection.parameter;
          point = Intersection.projectRayParameter( ray.origin.x, ray.origin.y,
                                                    ray.direction.x, ray.direction.y, min );
          normal = intersection.normal;
          entityIndex = i;
          shapeIndex = j;
        }
      }
    }

    if ( entityIndex < 0 || shapeIndex < 0 ) {
      return;
    }

    entity = entities[ entityIndex ];
    shape = entity.getShapes()[ shapeIndex ];
    shape.setColor( new Color( 0, 127, 0, 1.0 ) );

    // Transform normal to entity space (which is what the minEntityRay is in).
    // First, transform the intersection point to shape local space.
    shapePoint = shape.worldToLocalCoordinates( point.x, point.y );
    // Then, use that point to transform normal to entity local space.
    normal = shape.localToWorldCoordinates( shapePoint.x + normal.x,
                                            shapePoint.y + normal.y );

    normal.x -= point.x;
    normal.y -= point.y;
    shape.debugNormals.push({
      x: shapePoint.x,
      y: shapePoint.y,
      dx: normal.x,
      dy: normal.y
    });

    // Find angle of incidence between normal and ray (dot product).
    // Magnitude of ray.
    dx = minEntityRay.origin.x - point.x;
    dy = minEntityRay.origin.y - point.y;

    rayLength = Math.sqrt( dx * dx + dy * dy );
    normalLength = Math.sqrt( normal.x * normal.x + normal.y * normal.y );

    dot = dx * normal.x + dy * normal.y;
    cos = dot / ( rayLength * normalLength );
    // Math.acos can only handle values between [-1, 1].
    if ( cos > 1 ) {
      cos = 1;
    } else if ( cos < -1 ) {
      cos = -1;
    }
    angle = Math.acos( cos );

    // Determine which side of the normal the ray lies on.
    var side = dy * normal.x - dx * normal.y;
    // On left side, need to subtract angle, rather than add.
    if ( side > 0 ) {
      angle = -angle;
    }

    // The angle of incidence equals the angle of reflectance.
    cos = Math.cos( angle );
    sin = Math.sin( angle );

    // Rotate normal by angle.
    dx = cos * normal.x - sin * normal.y;
    dy = sin * normal.x + cos * normal.y;

    // Transform new direction to world space.
    direction = entity.localToWorldCoordinates( point.x + dx, point.y + dy );
    point = entity.localToWorldCoordinates( point.x, point.y );

    direction.x -= point.x;
    direction.y -= point.y;

    // New direction of ray.
    this.addRay({
      origin: {
        x: point.x + direction.x * 1e-5,
        y: point.y + direction.y * 1e-5
      },
      direction: direction
    });

    this.getParent().points.push( point );

    reflectionCount++;
  }

  return;
};

// Laser emitter.
var Emitter = function() {
  PhysicsEntity.call( this );

  this.addShape( new Shape().setGeometry( Geometry.createRectangle() )
                            .setColor( 0, 0, 0, 0.25 ) );

  this.setWidth( 100 );
  this.setHeight( 100 );

  this._laser = new Laser();
  this._laser.setParent( this );

  this.points = [];
};

Emitter.prototype = new PhysicsEntity();
Emitter.prototype.constructor = Emitter;

Emitter.prototype.update = function( elapsedTime ) {
  PhysicsEntity.prototype.update.call( this, elapsedTime );

  this.points = [];
  var entities = _game.getEntities();
  var entity, shapes, shape;
  var i, j, il, jl;
  for ( i = 0, il = entities.length; i < il; i++ ) {
    entity = entities[i];
    shapes = entity.getShapes();
    for ( j = 0, jl = shapes.length; j < jl; j++ ) {
      shape = shapes[j];
      shape.debugNormals = [];
      shape.setColor( new Color( 127, 0, 0, 1.0 ) );
    }
  }
  this.getLaser().project( _game.getEntities() );
};

Emitter.prototype.draw = function( ctx ) {
  PhysicsEntity.prototype.draw.call( this, ctx );

  this.getLaser().draw( ctx );

  ctx.fillStyle = 'rgba( 0, 0, 127, 0.25 )';
  for ( var i = 0, n = this.points.length; i < n; i++ ) {
    ctx.fillRect( this.points[i].x - 3, this.points[i].y - 3, 6, 6 );
  }
};

Emitter.prototype.getLaser = function() {
  return this._laser;
};
