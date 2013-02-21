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

  this._prevTime = Date.now();
  this._currTime = this._prevTime;
  this._running = true;

  this._objects = [];
};

Game.prototype.tick = function() {
  this.update();
  this.draw();
};

Game.prototype.update = function() {
  this._currTime = Date.now();
  var elapsedTime = this._currTime - this._prevTime;
  this._prevTime = this._currTime;

  for ( var i = 0, n = this._entities.length; i < n; i++ ) {
    this._objects.update( elapsedTime );
  }
};

Game.prototype.draw = function() {
  this._canvas.style.backgroundColor = '#ddd';

  this._ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

  for ( var i = 0, n = this._objects.length; i < n; i++ ) {
    this._objects.draw( this._ctx );
  }
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
