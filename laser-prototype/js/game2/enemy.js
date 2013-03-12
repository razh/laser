/*
  Enemy types:
    - Type 0. Light scout vehicle.
    - Type 1. Fighter vehicle.
    - Type 2. Bomber type vehicle.

  How would this work?
 */

var EnemyFactory = (function() {
  return {
    createFighter: function() {
      var fighter = new Enemy().setPosition( 100, 100 )
                               .setWidth( 20 )
                               .setHeight( 20 )
                               .setMaxSpeed( 400.0 )
                               .setMaxAcceleration( 100.0 );

      fighter.addShape( new Shape().setGeometry( Geometry.createRectangle() )
                                   .setColor( new Color( 127, 0, 0, 1.0 ) ) );

      return fighter;
    }
  };
}) ();


var Enemy = function() {
  PhysicsEntity.call( this );

  this._target = null;
};

Enemy.prototype = new PhysicsEntity();
Enemy.prototype.constructor = Enemy;

Enemy.prototype.update = function( elapsedTime ) {
  PhysicsEntity.prototype.update.call( this, elapsedTime );

  var target = this.getTarget();
  if ( target === null ) {
    return;
  }

  var dx = target.getX() - this.getX(),
      dy = target.getY() - this.getY();
  var length = dx * dx + dy * dy;
  if ( length === 0 ) {
    return;
  }

  length = Math.sqrt( length );
  if ( length > this.getMaxAcceleration() ) {
    length = this.getMaxAcceleration() / length;
    dx *= length;
    dy *= length;
  }

  this.setAcceleration( dx, dy );

  // Normalize velocity.
  var velocity = this.getVelocity();
  var speed = velocity.x * velocity.x + velocity.y * velocity.y;
  if ( speed === 0 ) {
    return;
  }

  speed = Math.sqrt( speed );
  if ( speed > this.getMaxSpeed() ) {
    speed = this.getMaxSpeed() / speed;
    velocity.x *= speed;
    velocity.y *= speed;

    this.setVelocity( velocity );
  }
};

Enemy.prototype.getTarget = function() {
  return this._target;
};

Enemy.prototype.setTarget = function( target ) {
  this._target = target;
};
