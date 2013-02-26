var Laser = function() {

};

Laser.prototype.draw = function( ctx ) {
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

      if ( shape.intersectsRay( rayOrigin.x, rayOrigin.y,
                                rayDirection.x, rayDirection.y ) !== null ) {
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
