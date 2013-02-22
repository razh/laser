$(
  function() {
   init();
  }
);

window.requestAnimFrame = (function() {
  return window.requestAnimationFrame       ||
         window.webkitRequestAnimationFrame ||
         window.mozRequestAnimationFrame    ||
         window.oRequestAnimationFrame      ||
         window.msRequestAnimationFrame     ||
         function( callback ) {
            window.setTimeout( callback, 1000 / 60 );
         };
}) ();

var _game;

function init() {
  _game = new Game();

  var test = new Entity().setPosition( 200, 300 )
                         .setWidth( 200 )
                         .setHeight( 200 );

  var shape = new Shape().setGeometry( Geometry.createRing({
                            subdivisions: 64,
                            startAngle: Math.PI / 180 * 20,
                            endAngle: Math.PI / 180 * 160,
                            anticlockwise: false
                          }))
                         .setColor( new Color( 255, 0, 0, 1.0 ) );

  test.addShape( shape );
  _game.addEntity( test );

  loop();
}

function loop() {
  if ( !_game.isRunning() ) {
    return;
  }

  _game.tick();
  requestAnimFrame( loop );
}

function quit() {
  _game.stop();
}

var Game = function() {
  this.WIDTH = window.innerWidth;
  this.HEIGHT = window.innerHeight;

  $( 'body' ).append( '<canvas>' );

  this._canvas = $( 'canvas' )[0];

  this._canvas.width = this.WIDTH;
  this._canvas.height = this.HEIGHT;

  this._ctx = this._canvas.getContext( '2d' );

  this._backgroundColor = new Color( 127, 127, 127, 1.0 );

  this._prevTime = Date.now();
  this._currTime = this._prevTime;
  this._running = true;

  this._entities = [];

  this.EPSILON = 1e-5;
};

Game.prototype.tick = function() {
  this.update();
  this.draw();
};

Game.prototype.update = function() {
  this._currTime = Date.now();
  var elapsedTime = this._currTime - this._prevTime;
  this._prevTime = this._currTime;

  var entities = this.getEntities();
  for ( var i = 0, n = entities.length; i < n; i++ ) {
    entities[i].update( elapsedTime );
  }
};

Game.prototype.draw = function() {
  this._canvas.style.backgroundColor = this.getBackgroundColor().toHexString();

  this._ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

  var entities = this.getEntities();
  for ( var i = 0, n = entities.length; i < n; i++ ) {
    entities[i].draw( this._ctx );
  }
};

Game.prototype.getEntities = function() {
  return this._entities;
};

Game.prototype.addEntity = function( entity ) {
  this.getEntities().push( entity );
};

// Background color.
Game.prototype.getBackgroundColor = function() {
  return this._backgroundColor;
};

Game.prototype.setBackgroundColor = function() {
  this.getBackgroundColor().set.apply( this.getBackgroundColor(), arguments );
};

Game.prototype.isRunning = function() {
  return this._running;
};

Game.prototype.stop = function() {
  this._running = false;
};
