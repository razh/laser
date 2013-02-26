var Laser = function() {
  // Ray origins.
  this._origins = [];
  // Ray directions.
  this._directions = [];

  this._lineWidth = 1;
  this._color = new Color( 255, 0, 0, 1.0 );
};

Laser.prototype.clear = function() {
  this._origins = [];
  this._directions = [];
};

Laser.prototype.draw = function( ctx ) {
  var origins = this.getOrigins();
  var directions = this.getDirections();

  if ( origins.length <= 0 || directions.length <= 0 ) {
    return;
  }

  ctx.moveTo( origins[0].x, origins[0].y );

  for ( var i = 1; i < origins.length; i++ ) {
    ctx.lineTo( origins[1].x, origins[1].y );
  }

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

// Laser emitter.
var Emitter = function() {
  Entity.call( this );

  this.addShape( new Shape().setGeometry( Geometry.createRectangle() )
                            .setColor( 127, 0, 0, 0.25 ) );

  this.setWidth( 100 );
  this.setHeight( 100 );

  this._laser = new Laser();
};

Emitter.prototype = new Entity();
Emitter.prototype.constructor = new Emitter();

Emitter.prototype.update = function( elapsedTime ) {
  Entity.prototype.update.call( this, elapsedTime );

  var rotation = this.getRotation();
  var cos = Math.cos( rotation );
  var sin = Math.sin( rotation );

  var rayOrigin, rayDirection;
  var shapeRayOrigin, shapeRayDirection;

  var entities = _game.getEntities();
  var entity, shapes, shape;
  var i, j, il, jl;
  for ( i = 0, il = entities.length; i < il; i++ ) {
    entity = entities[i];
    if ( entity === this ) {
      continue;
    }

    shapes = entity.getShapes();

    rayOrigin = entity.worldToLocalCoordinates( this.getX(), this.getY() );
    rayDirection = entity.worldToLocalCoordinates( this.getX() + cos, this.getY() + sin );

    rayDirection.x -= rayOrigin.x;
    rayDirection.y -= rayOrigin.y;

    for ( j = 0, jl = shapes.length; j < jl; j++ ) {
      shape = shapes[j];

      var point = shape.intersectsRay( rayOrigin.x, rayOrigin.y,
                                       rayDirection.x, rayDirection.y );
      if ( point !== null ) {
        shape.setColor( 0, 127, 0, 1.0 );
      } else {
        shape.setColor( 127, 0, 0, 1.0 );
      }
    }
  }
};

Emitter.prototype.draw = function( ctx ) {
  Entity.prototype.draw.call( this, ctx );

  ctx.save();

  ctx.translate( this.getX(), this.getY() );
  ctx.rotate( this.getRotation() );

  ctx.moveTo( 0, 0 );
  ctx.lineTo( 1000, 0 );
  ctx.strokeStyle = new Color( 255, 0, 0, 1.0 ).toString();
  ctx.lineWidth = 1;

  ctx.stroke();

  ctx.restore();
};

Emitter.prototype.getLaser = function() {
  return this._laser;
};
