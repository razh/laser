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

  var emitter = new Emitter();
  var fighter = EnemyFactory.createFighter();
  fighter.setTarget( emitter );

  var tempFighter;
  for ( var i = 0; i < 60; i++ ) {
    tempFighter = EnemyFactory.createFighter();
    tempFighter.setPosition( Math.random() * _game.WIDTH,
                             Math.random() * _game.HEIGHT );
    tempFighter.setTarget( emitter );
    _game.addEntity( tempFighter );
  }

  _game.addEntity( fighter );
  _game.addEntity( emitter );

  _game._canvas.addEventListener( 'mousedown', onMouseDown, null );
  _game._canvas.addEventListener( 'mousemove', onMouseMove, null );
  _game._canvas.addEventListener( 'mouseup', onMouseUp, null );

  document.addEventListener( 'keydown', onKeyDown, null );

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

  this._player = new Player();
  this._entities = [];
  
  this._camera = new Camera();
  // Setup camera.
  this._camera.setViewport( 0, 0, 960, 720, this.WIDTH, this.HEIGHT );

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

  if ( elapsedTime > 1e3 ) {
    elapsedTime = 1e3;
  }

  var entities = this.getEntities();
  for ( var i = 0, n = entities.length; i < n; i++ ) {
    entities[i].update( elapsedTime );
  }
};

Game.prototype.draw = function() {
  this._canvas.style.backgroundColor = this.getBackgroundColor().toHexString();

  var ctx = this.getCtx();
  ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

  ctx.save();
  // Origin is at bottom left corner in OpenGL.
  ctx.translate( 0, this.HEIGHT );
  ctx.scale( 1, -1 );
  
  // Translate to camera position.
  var camera = this.getCamera();
  ctx.translate( camera.getX() - 0.5 * camera.getWidth(), camera.getY() - 0.5 * camera.getHeight );
  ctx.scale( camera.getWidth() / this.WIDTH, camera.getHeight() / this.HEIGHT );  

  var entities = this.getEntities();
  for ( var i = 0, n = entities.length; i < n; i++ ) {
    entities[i].draw( ctx );
  }

  ctx.restore();
};

Game.prototype.hit = function( x, y ) {
  var entities = this.getEntities();
  for ( var i = 0, n = entities.length; i < n; i++ ) {
    if ( entities[i].contains( x, y ) ) {
      return entities[i];
    }
  }

  return null;
};

Game.prototype.getCtx = function() {
  return this._ctx;
};

Game.prototype.getCamera = function() {
  return this._camera;
};

Game.prototype.getEntities = function() {
  return this._entities;
};

Game.prototype.addEntity = function( entity ) {
  this.getEntities().push( entity );
};

Game.prototype.getPlayer = function() {
  return this._player;
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
