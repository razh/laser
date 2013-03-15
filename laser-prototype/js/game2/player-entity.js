var PlayerEntity = function() {
  PhysicsEntity.call( this );

  this.addShape( new Shape().setGeometry( Geometry.createRectangle() )
                            .setColor( 0, 0, 0, 0.25 ) );

  this.setWidth( 75 );
  this.setHeight( 75 );

  this.setMaxAngularAcceleration( 6 * Math.PI );
  this.setMaxAngularVelocity( 3 * Math.PI );

  this._laser = new Laser();
  this._laser.setParent( this );

  this._player = null;
};

PlayerEntity.prototype = new PhysicsEntity();
PlayerEntity.prototype.constructor = PlayerEntity;

PlayerEntity.prototype.getPlayer = function() {
  return this._player;
};

PlayerEntity.prototype.setPlayer = function( player ) {
  this._player = player;
};

PlayerEntity.prototype.update = function( elapsedTime ) {
  PhysicsEntity.prototype.update.call( this, elapsedTime );

  // TODO: Hacky hacky hacky.
  var entities = _game.getEntities();
  var entity, shapes, shape;
  var i, j, il, jl;
  for ( i = 0, il = entities.length; i < il; i++ ) {
    entity = entities[i];
    if ( entity === this ) {
      continue;
    }

    shapes = entity.getShapes();
    for ( j = 0, jl = shapes.length; j < jl; j++ ) {
      shapes[j].setColor( 127, 0, 0, 1.0 );
    }
  }

  this.getLaser().project( entities );
};


PlayerEntity.prototype.draw = function( ctx ) {
  PhysicsEntity.prototype.draw.call( this, ctx );

  this.getLaser().draw( ctx );
};


PlayerEntity.prototype.getLaser = function() {
  return this._laser;
};
