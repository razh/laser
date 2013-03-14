var Player = function() {
  this._selected = null;

  // Mouse offset from selected entity.
  this._offset = {
    x: 0,
    y: 0
  };
};

Player.prototype.getSelected = function() {
  return this._selected;
};

Player.prototype.setSelected = function( selected ) {
  this._selected = selected;
};

Player.prototype.hasSelected = function() {
  return this._selected !== null;
};

// Offset. Distance from mouse to selected entity.
Player.prototype.getOffsetX = function() {
  return this.getOffset().x;
};

Player.prototype.setOffsetX = function( offsetX ) {
  this._offset.x = offsetX;
};

Player.prototype.getOffsetY = function() {
  return this.getOffset().y;
};

Player.prototype.setOffsetY = function( offsetY ) {
  this._offset.y = offsetY;
};

Player.prototype.getOffset = function() {
  return this._offset;
};

Player.prototype.setOffset = function() {
  if ( arguments.length === 1 ) {
    this.setOffsetX( arguments[0].x );
    this.setOffsetY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.setOffsetX( arguments[0] );
    this.setOffsetY( arguments[1] );
  }
};

var PlayerEntity = function() {
  Emitter.call( this );

  this._player = null;
};

PlayerEntity.prototype = new Emitter();
PlayerEntity.prototype.constructor = PlayerEntity;

PlayerEntity.prototype.getPlayer = function() {
  return this._player;
};

PlayerEntity.prototype.setPlayer = function( player ) {
  this._player = player;
};
