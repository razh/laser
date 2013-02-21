var Entity = function() {
  this._position = {
    x: 0.0,
    y: 0.0
  };

  this._width = 0.0;
  this._height = 0.0;
  this._rotation = 0.0;

  this._color = new Color( 255, 255, 255, 1.0 );

  this._vertices = [];
  this._edges = [];
};

Entity.prototype.update = function( elapsedTime ) {};

Entity.prototype.drawPath = function( ctx ) {
  ctx.beginPath();

  var x = this._vertices[ 2 * this._edges[0] ],
      y = this._vertices[ 2 * this._edges[0] + 1 ];
  ctx.moveTo( x, y );

  for ( var i = 1, n = this._edges.length; i < n; i++ ) {
    x = this._vertices[ 2 * this._edges[i] ];
    y = this._vertices[ 2 * this._edges[i] + 1 ];

    ctx.lineTo( x, y );
  }

  ctx.closePath();
};

Entity.prototype.draw = function( ctx, stroke, altColor ) {
  stroke = stroke || 0;

  ctx.save();
  ctx.translate( this.getX(), this.getY() );
  ctx.rotate( this.getRotation() );
  ctx.scale( this.getWidth()  - stroke,
             this.getHeight() - stroke );

  this.drawPath( ctx );

  if ( !altColor ) {
    ctx.fillStyle = this.getColor().toString();
  } else {
    ctx.fillStyle = this.getAltColor().toString();
  }
  ctx.fill();

  ctx.restore();
};
