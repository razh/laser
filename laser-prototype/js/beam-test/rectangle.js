function Rectangle() {
  Object2D.call( this );
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

  ctx.drawRect();

  ctx.fillStyle = this.getColor().toString();
  ctx.fill();

  ctx.restore();
};
