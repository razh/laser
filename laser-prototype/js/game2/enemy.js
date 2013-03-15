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
                               .setMaxSpeed( Math.random() * 300 + 300 )
                               .setMaxAcceleration( Math.random() * 500.0 + 500.0 )
                               .setHealth( 60 )
                               .setMaxHealth( 60 );

      fighter.addShape( new Shape().setGeometry( Geometry.createRectangle() )
                                   .setColor( new Color( 127, 0, 0, 1.0 ) ) );

      return fighter;
    }
  };
}) ();


var Enemy = function() {
  LivingEntity.call( this );

  this._target = null;
  this._takingFire = false;
};

Enemy.prototype = new LivingEntity();
Enemy.prototype.constructor = Enemy;

Enemy.prototype.update = function( elapsedTime ) {
  LivingEntity.prototype.update.call( this, elapsedTime );

  var target = this.getTarget();
  if ( target === null ) {
    return;
  }

  // Set acceleration to distance from target.
  var dx = target.getX() - this.getX(),
      dy = target.getY() - this.getY();
  var length = dx * dx + dy * dy;
  if ( length === 0 ) {
    return;
  }

  length = this.getMaxAcceleration() / Math.sqrt( length );
  dx *= length;
  dy *= length;

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

  this.setRotation( Math.atan2( velocity.y, velocity.x ) );
};

Enemy.prototype.getTarget = function() {
  return this._target;
};

Enemy.prototype.setTarget = function( target ) {
  this._target = target;
};

// Probably unnecessary.
Enemy.prototype.takeFire = function( damage ) {
  if ( !this.isTakingFire() ) {
    return;
  }

  this.damage( damage );
  this.setTakingFire( false );
};

Enemy.prototype.isTakingFire = function() {
  return this._takingFire;
};

Enemy.prototype.setTakingFire = function( takingFire ) {
  this._takingFire = !!takingFire;
};
