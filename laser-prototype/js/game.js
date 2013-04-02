function init() {
  _game = new TestGame();

  var test = new Entity().setPosition( 200, 300 )
                         .setWidth( 200 )
                         .setHeight( 200 );

  var shape = new Shape().setGeometry( Geometry.createRing({
                            innerRadius: 0.80,
                            subdivisions: 4,
                            startAngle: Math.PI / 180 * 90,
                            endAngle: Math.PI / 180 * 250,
                            anticlockwise: true
                          }))
                         // .setGeometry( Geometry.createRectangle() )
                         .setColor( new Color( 127, 0, 0, 1.0 ) );

  var shape2 = new Shape().setGeometry( Geometry.createRing({
                             innerRadius: 0.80,
                             subdivisions: 4,
                             startAngle: Math.PI / 180 * 290,
                             endAngle: Math.PI / 180 * 90,
                             anticlockwise: true
                          }))
                          .setPosition( 2.0, 0.0 )
                          .setColor( new Color( 0, 0, 127, 1.0 ) );
  test.addShape( shape );
  test.addShape( shape2 );
  _game.addEntity( test );
  _game.addEntity( new Emitter() );

  console.log( shape2.worldToLocalCoordinates( test.worldToLocalCoordinates( 200, 0 ) ) );

  _game._canvas.addEventListener( 'mousedown', onMouseDown, null );
  _game._canvas.addEventListener( 'mousemove', onMouseMove, null );
  _game._canvas.addEventListener( 'mouseup', onMouseUp, null );

  document.addEventListener( 'keydown', onKeyDown, null );

  loop();
}

function TestGame() {
  Game.call( this );

  $( 'body' ).append( '<canvas>' );

  this._canvas = $( 'canvas' )[0];

  this._canvas.width = this.WIDTH;
  this._canvas.height = this.HEIGHT;

  this._ctx = this._canvas.getContext( '2d' );

  this._player = new Player();
}

TestGame.prototype = new Game();
TestGame.prototype.constructor = TestGame;

TestGame.prototype.tick = function() {
  this.update();
  this.draw();

  if (testing) {
    var ctx = _game._ctx;
    var x, y;
    var xCount = 60;
    var yCount = 60;
    var dx = ( this.WIDTH - 40 ) / xCount;
    var dy = ( this.HEIGHT - 40 ) / yCount;
    for ( var i = 0; i < xCount; i++ ) {
      for ( var j = 0; j < yCount; j++ ) {
        x = 20 + i * dx;
        y = 20 + j * dy;

        var coords = transformCoords( x, y );
        if ( _game.hit( coords.x, coords.y ) !== null ) {
          ctx.fillStyle = 'rgba( 0, 255, 0, 1.0 )';
        } else {
          ctx.fillStyle = 'rgba( 255, 0, 0, 1.0 )';
        }
        ctx.fillRect( x - 1, y - 1, 2, 2 );
      }
    }
  }
};

TestGame.prototype.draw = function() {
  this._canvas.style.backgroundColor = this.getBackgroundColor().toHexString();

  this._ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

  this._ctx.save();
  // Origin is at bottom left corner in OpenGL.
  this._ctx.translate( 0, this.HEIGHT );
  this._ctx.scale( 1, -1 );

  var entities = this.getEntities();
  for ( var i = 0, n = entities.length; i < n; i++ ) {
    entities[i].draw( this._ctx );
  }

  this._ctx.restore();
};

TestGame.prototype.hit = function( x, y ) {
  var entities = this.getEntities();
  for ( var i = 0, n = entities.length; i < n; i++ ) {
    if ( entities[i].contains( x, y ) ) {
      return entities[i];
    }
  }

  return null;
};

TestGame.prototype.getPlayer = function() {
  return this._player;
};
