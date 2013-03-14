var PhysicsEntity = function() {
  Entity.call( this );

  this._velocity = {
    x: 0,
    y: 0
  };

  this._acceleration = {
    x: 0,
    y: 0
  };

  this._maxSpeed = 0;
  this._maxAcceleration = 0;

  this._angularVelocity = 0;
  this._angularAcceleration = 0;

  this._maxAngularVelocity = 0;
  this._maxAngularAcceleration = 0;
};

PhysicsEntity.prototype = new Entity();
PhysicsEntity.prototype.constructor = PhysicsEntity;

PhysicsEntity.prototype.update = function( elapsedTime ) {
  Entity.prototype.update.call( this, elapsedTime );

  // Convert from milliseconds to seconds.
  elapsedTime *= 1e-3;

  // Handle translation.
  this.accelerate( this.getAccelerationX() * elapsedTime,
                   this.getAccelerationY() * elapsedTime );
  this.translate( this.getVelocityX() * elapsedTime,
                  this.getVelocityY() * elapsedTime );

  // Handle rotation.
  this.angularAccelerate( this.getAngularAcceleration() * elapsedTime );
  this.rotate( this.getAngularVelocity() * elapsedTime );
};

// Velocity.
PhysicsEntity.prototype.getVelocityX = function() {
  return this.getVelocity().x;
};

PhysicsEntity.prototype.setVelocityX = function( velocityX ) {
  this._velocity.x = velocityX;
  return this;
};

PhysicsEntity.prototype.getVelocityY = function() {
  return this.getVelocity().y;
};

PhysicsEntity.prototype.setVelocityY = function( velocityY ) {
  this._velocity.y = velocityY;
  return this;
};

PhysicsEntity.prototype.getVelocity = function() {
  return this._velocity;
};

PhysicsEntity.prototype.setVelocity = function() {
  if ( arguments.length === 1 ) {
    this.setVelocityX( arguments[0].x );
    this.setVelocityY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.setVelocityX( arguments[0] );
    this.setVelocityY( arguments[1] );
  }

  return this;
};

// Acceleration.
PhysicsEntity.prototype.getAccelerationX = function() {
  return this.getAcceleration().x;
};

PhysicsEntity.prototype.setAccelerationX = function( accelerationX ) {
  this._acceleration.x = accelerationX;
  return this;
};

PhysicsEntity.prototype.getAccelerationY = function() {
  return this.getAcceleration().y;
};

PhysicsEntity.prototype.setAccelerationY = function( accelerationY ) {
  this._acceleration.y = accelerationY;
  return this;
};

PhysicsEntity.prototype.getAcceleration = function() {
  return this._acceleration;
};

PhysicsEntity.prototype.setAcceleration = function() {
  if ( arguments.length === 1 ) {
    this.setAccelerationX( arguments[0].x );
    this.setAccelerationY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.setAccelerationX( arguments[0] );
    this.setAccelerationY( arguments[1] );
  }

  return this;
};

PhysicsEntity.prototype.accelerateX = function( accelerateX ) {
  this.setVelocityX( this.getVelocityX() + accelerateX );
  return this;
};

PhysicsEntity.prototype.accelerateY = function( accelerateY ) {
  this.setVelocityY( this.getVelocityY() + accelerateY );
  return this;
};

PhysicsEntity.prototype.accelerate = function() {
  if ( arguments.length === 1 ) {
    this.accelerateX( arguments[0].x );
    this.accelerateY( arguments[0].y );
  } else if ( arguments.length === 2) {
    this.accelerateX( arguments[0] );
    this.accelerateY( arguments[1] );
  }

  return this;
};

// Max speed.
PhysicsEntity.prototype.getMaxSpeed = function() {
  return this._maxSpeed;
};

PhysicsEntity.prototype.setMaxSpeed = function( maxSpeed ) {
  this._maxSpeed = maxSpeed;
  return this;
};

// Max acceleration.
PhysicsEntity.prototype.getMaxAcceleration = function() {
  return this._maxAcceleration;
};

PhysicsEntity.prototype.setMaxAcceleration = function( maxAcceleration ) {
  this._maxAcceleration = maxAcceleration;
  return this;
};

// Angular velocity.
PhysicsEntity.prototype.getAngularVelocity = function() {
  return this._angularVelocity;
};

PhysicsEntity.prototype.setAngularVelocity = function( angularVelocity ) {
  var maxAngularVelocity = this.getMaxAngularVelocity();

  if ( angularVelocity > maxAngularVelocity ) {
    angularVelocity = maxAngularVelocity;
  } else if ( angularVelocity < -maxAngularVelocity ) {
    angularVelocity = -maxAngularVelocity;
  }

  this._angularVelocity = angularVelocity;
  return this;
};

// Angular acceleration.
PhysicsEntity.prototype.getAngularAcceleration = function() {
  return this._angularAcceleration;
};

PhysicsEntity.prototype.setAngularAcceleration = function( angularAcceleration ) {
  var maxAngularAcceleration = this.getMaxAngularVelocity();

  if ( angularAcceleration > maxAngularAcceleration ) {
    angularAcceleration = maxAngularAcceleration;
  } else if ( angularAcceleration < -maxAngularAcceleration ) {
    angularAcceleration = -maxAngularAcceleration;
  }

  this._angularAcceleration = angularAcceleration;
  return this;
};

PhysicsEntity.prototype.angularAccelerate = function( accelerate ) {
  this.setAngularVelocity( this.getAngularVelocity() + accelerate );
  return this;
};

// Max angular velocity.
PhysicsEntity.prototype.getMaxAngularVelocity = function() {
  return this._maxAngularVelocity;
};

PhysicsEntity.prototype.setMaxAngularVelocity = function( maxAngularVelocity ) {
  this._maxAngularVelocity = maxAngularVelocity;
  return this;
};

// Max angular acceleration.
PhysicsEntity.prototype.getMaxAngularAcceleration = function() {
  return this._maxAngularAcceleration;
};

PhysicsEntity.prototype.setMaxAngularAcceleration = function( maxAngularAcceleration ) {
  this._maxAngularAcceleration = maxAngularAcceleration;
  return this;
};
