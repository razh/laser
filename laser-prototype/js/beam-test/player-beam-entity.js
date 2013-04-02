function PlayerBeamEntity() {
  PhysicsEntity.call( this );
}

PlayerBeamEntity.prototype = new PhysicsEntity();
PlayerBeamEntity.prototype.constructor = PlayerBeamEntity;
