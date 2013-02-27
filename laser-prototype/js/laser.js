var Laser = function() {
  // Ray origins.
  this._origins = [];
  // Ray directions.
  this._directions = [];

  this._parent = null;

  this._lineWidth = 1;
  this._color = new Color( 255, 0, 0, 1.0 );

  this._reflectionLimit = 1;
};

Laser.prototype.clear = function() {
  this._origins = [];
  this._directions = [];

  this.addOrigin({ x: this.getParent().getX(), y: this.getParent().getY() });

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

  ctx.beginPath();
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

      ray.origin = entity.worldToLocalCoordinates( ray.origin.x, ray.origin.y );
      ray.direction = entity.worldToLocalCoordinates( ray.origin.x + ray.direction.x,
                                                      ray.origin.y + ray.direction.y );

      ray.direction.x -= ray.origin.x;
      ray.direction.y -= ray.origin.y;
      // console.log( ray.origin.x + ', ' + ray.origin.y )

      // Find the shape with the minimum parameter.
      shapes = entity.getShapes();
      for ( j = 0, jl = shapes.length; j < jl; j++ ) {
        shape = shapes[j];
        shape.setColor( new Color( 127, 0, 0, 1.0 ) );

        intersection = shape.intersectsRay( ray.origin.x, ray.origin.y,
                                            ray.direction.x, ray.direction.y );

        if ( intersection === null ) {
          continue;
        }

        // We've found a positive parameter smaller than current min.
        if ( min < 0 || ( intersection.parameter > 0 &&
                          intersection.parameter < min ) ) {
          minEntityRay = ray;
          min = intersection.parameter;
          point = Intersection.projectRayParameter( ray.origin.x, ray.origin.y,
                                                    ray.direction.x, ray.direction.y );
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

    // Find angle of incidence between normal and ray (dot product).
    // Magnitude of ray.
    dx = minEntityRay.origin.x - point.x;
    dy = minEntityRay.origin.y - point.y;
    // console.log( 'point: ' + point.x + ', ' + point.y)

    rayLength = Math.sqrt( dx * dx + dy * dy );
    normalLength = Math.sqrt( normal.x * normal.x + normal.y * normal.y );

    dot = dx * normal.x + dy * normal.y;
    angle = Math.acos( dot / ( rayLength * normalLength ) );

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
      origin: point,
      direction: direction
    });

    reflectionCount++;
  }
};

// Laser emitter.
var Emitter = function() {
  Entity.call( this );

  this.addShape( new Shape().setGeometry( Geometry.createRectangle() )
                            .setColor( 0, 0, 0, 0.25 ) );

  this.setWidth( 100 );
  this.setHeight( 100 );

  this._laser = new Laser();
  this._laser.setParent( this );
};

Emitter.prototype = new Entity();
Emitter.prototype.constructor = Emitter;

Emitter.prototype.update = function( elapsedTime ) {
  Entity.prototype.update.call( this, elapsedTime );

  this.getLaser().project( _game.getEntities() );

  // var rotation = this.getRotation();
  // var cos = Math.cos( rotation );
  // var sin = Math.sin( rotation );

  // var rayOrigin, rayDirection;

  // var entities = _game.getEntities();
  // var entity, shapes, shape;
  // var i, j, il, jl;
  // for ( i = 0, il = entities.length; i < il; i++ ) {
  //   entity = entities[i];
  //   if ( entity === this ) {
  //     continue;
  //   }

  //   rayOrigin = entity.worldToLocalCoordinates( this.getX(), this.getY() );
  //   rayDirection = entity.worldToLocalCoordinates( this.getX() + cos, this.getY() + sin );

  //   rayDirection.x -= rayOrigin.x;
  //   rayDirection.y -= rayOrigin.y;

  //   shapes = entity.getShapes();
  //   for ( j = 0, jl = shapes.length; j < jl; j++ ) {
  //     shape = shapes[j];

  //     var point = shape.intersectsRay( rayOrigin.x, rayOrigin.y,
  //                                      rayDirection.x, rayDirection.y );
  //     if ( point !== null ) {
  //       shape.setColor( 0, 127, 0, 1.0 );
  //     } else {
  //       shape.setColor( 127, 0, 0, 1.0 );
  //     }
  //   }
  // }
};

Emitter.prototype.draw = function( ctx ) {
  Entity.prototype.draw.call( this, ctx );

  this.getLaser().draw( ctx );
  // ctx.save();

  // ctx.translate( this.getX(), this.getY() );
  // ctx.rotate( this.getRotation() );

  // ctx.moveTo( 0, 0 );
  // ctx.lineTo( 1000, 0 );
  // ctx.strokeStyle = new Color( 255, 0, 0, 1.0 ).toString();
  // ctx.lineWidth = 1;

  // ctx.stroke();

  // ctx.restore();
};

Emitter.prototype.getLaser = function() {
  return this._laser;
};
