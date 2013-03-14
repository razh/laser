var LivingEntity = function() {
  PhysicsEntity.call( this );

  this._health = Number.NaN;
  this._maxHealth = Number.NaN;
};

LivingEntity.prototype = new PhysicsEntity();
LivingEntity.prototype.constructor = LivingEntity;

// Health.
LivingEntity.prototype.getHealth = function() {
  return this._health;
};

LivingEntity.prototype.setHealth = function( health ) {
  // Clamp health to [ 0, maxHealth ].
  if ( health < 0 ) {
    health = 0;
  }

  var maxHealth = this.getMaxHealth();
  if ( health > maxHealth ) {
    health = maxHealth;
  }

  this._health = health;
  return this;
};

LivingEntity.prototype.addHealth = function( health ) {
  this.setHealth( this.getHealth() + health );
  return this;
};

LivingEntity.prototype.damage = function( damage ) {
  return this.addHealth( -damage );
};

LivingEntity.prototype.heal = function( heal ) {
  return this.addHealth( heal );
};

// Max health.
LivingEntity.prototype.getMaxHealth = function() {
  return this._maxHealth;
};

LivingEntity.prototype.setMaxHealth = function( maxHealth ) {
  this._maxHealth = maxHealth;
  return this;
};
