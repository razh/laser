function Rectangle() {
  Object2D.call( this );

  this._color = new Color( 255, 255, 255, 1.0 );
}

Rectangle.prototype = new Object2D();
Rectangle.prototype.constructor = Rectangle;

Rectangle.prototype.draw = function( ctx ) {
  if ( !this.isVisible() ) {
    return;
  }

  ctx.save();

  ctx.translate( this.getX(), this.getY() );
  ctx.rotate( this.getRotation() );
  ctx.scale( this.getWidth(), this.getHeight() );

  ctx.fillStyle = this.getColor().toString();

  // Rectangle with an origin in the mid-left.
  ctx.fillRect( 0, -0.5, 1, 1 );

  ctx.restore();
};


Rectangle.prototype.update = function( elapsedTime ) {};

Rectangle.prototype.getColor = function() {
  return this._color;
};

Rectangle.prototype.setColor = function() {
  this.getColor().set.apply( this.getColor(), arguments );
  return this;
};
