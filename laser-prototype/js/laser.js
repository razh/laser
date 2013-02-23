var Laser = function() {

};

Laser.prototype.draw = function( ctx ) {
};


// Laser emitter.
var Emitter = function() {
  Entity.call( this );

  this.addShape( new Shape().setGeometry( Geometry.createRectangle() )
                            .setColor( 127, 0, 0, 1.0 ) );

  this.setWidth( 100 );
  this.setHeight( 100 );

  this._laser = new Laser();
};

Emitter.prototype = new Entity();
Emitter.prototype.constructor = new Emitter();

Emitter.prototype.update = function( elapsedTime ) {
  Entity.prototype.update.call( this, elapsedTime );

  var rotation = this.getRotation();
  var sin = Math.sin( rotation );
  var cos = Math.cos( rotation );

  var entities = _game.getEntities();
  var entity, shapes, shape;
  var center, shapeCenter;
  var i, j, il, jl;
  for ( i = 0, il = entities.length; i < il; i++ ) {
    entity = entities[i];
    if ( entity === this ) {
      continue;
    }

    entityScale = Math.max( entity.getWidth(), entity.getHeight() );
    shapes = entity.getShapes();
    for ( j = 0, jl = shapes.length; j < jl; j++ ) {
      shape = shapes[j];
      shapeCenter = shape.localToWorldCoordinates( shape.getX(), shape.getY() );
      center = entity.localToWorldCoordinates( shapeCenter.x, shapeCenter.y );
      if ( Intersection.rayCircle( this.getX(), this.getY(),
                                   sin, cos,
                                   center.x, center.y,
                                   entityScale * shape.getRadius() ) !== null ) {
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
  ctx.lineTo( 0, 1000 );
  ctx.strokeStyle = new Color( 255, 0, 0, 1.0 ).toString();
  ctx.lineWidth = 5;

  ctx.stroke();

  ctx.restore();
};
