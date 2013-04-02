function init() {
  _game = new TestGame2();

  var playerEntity = _game._playerEntity;
  playerEntity.setPosition( 0.5 * _game.WIDTH, 0.5 * _game.HEIGHT );
  _game.addEntity( playerEntity );

  var tempFighter;
  for ( var i = 0; i < 20; i++ ) {
    tempFighter = EnemyFactory.createFighter();
    tempFighter.setPosition( Math.random() * _game.WIDTH,
                             Math.random() * _game.HEIGHT );
    tempFighter.setTarget( playerEntity );
    _game.addEntity( tempFighter );
  }

  _game._canvas.addEventListener( 'mousedown', onMouseDown, null );
  _game._canvas.addEventListener( 'mousemove', onMouseMove, null );
  _game._canvas.addEventListener( 'mouseup', onMouseUp, null );

  document.addEventListener( 'keydown', onKeyDown, null );
  document.addEventListener( 'keyup', onKeyUp, null );

  loop();
}

function TestGame2() {
  Game.call( this );
    $( 'body' ).append( '<canvas>' );

  this._canvas = $( 'canvas' )[0];

  this._canvas.width = this.WIDTH;
  this._canvas.height = this.HEIGHT;

  this._ctx = this._canvas.getContext( '2d' );

  this._player = new Player();
  this._playerEntity = new PlayerEntity();

  this._camera = new Camera();
  // Setup camera.
  this._camera.setViewport( 0, 0, 960, 720, this.WIDTH, this.HEIGHT );
  this._cameraController = new CameraController( this, this._camera );

  this.input = {
    keys: {}
  };

  this._frameCount = 0;
}

TestGame2.prototype = new Game();
TestGame2.prototype.constructor = TestGame2;

TestGame2.prototype.update = function() {
  this._currTime = Date.now();
  var elapsedTime = this._currTime - this._prevTime;
  this._prevTime = this._currTime;

  if ( elapsedTime > 1e3 ) {
    elapsedTime = 1e3;
  }

  this.processInput( elapsedTime );
  this._cameraController.update( elapsedTime );

  // Add entity.
  if ( this._frameCount % 30 === 0 ) {
    var fighter = EnemyFactory.createFighter();
    fighter.setPosition( Math.random() * _game.WIDTH,
                         Math.random() * _game.HEIGHT );
    fighter.setTarget( this._playerEntity );
    _game.addEntity( fighter );
  }

  this._frameCount++;
  if ( this._frameCount > 1e6 ) {
    this._frameCount = 0;
  }

  var deadEntities = [];
  var entities = this.getEntities();
  var entity;
  var i, n;
  for ( i = 0, n = entities.length; i < n; i++ ) {
    entity = entities[i];

    // Update and delete.
    entity.update( elapsedTime );
    if ( entity instanceof Enemy ) {
      if ( entity.isDead() ) {
        deadEntities.push( entity );
      }
    }
  }

  for ( i = 0, n = deadEntities.length; i < n; i++ ) {
    this.removeEntity( deadEntities[i] );
  }
};

TestGame2.prototype.draw = function() {
  this._canvas.style.backgroundColor = this.getBackgroundColor().toHexString();

  var ctx = this.getCtx();
  ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

  ctx.save();
  // Origin is at bottom left corner in OpenGL.
  ctx.translate( 0, this.HEIGHT );
  ctx.scale( 1, -1 );

  this.getCamera().draw( ctx );
  // Translate to camera position.
  // var camera = this.getCamera();
  // ctx.translate( camera.getX(), camera.getY() );
  // ctx.scale( camera.getWidth() / this.WIDTH, camera.getHeight() / this.HEIGHT );

  var entities = this.getEntities();
  for ( var i = 0, n = entities.length; i < n; i++ ) {
    entities[i].draw( ctx );
  }

  ctx.restore();
};

TestGame2.prototype.hit = function( x, y ) {
  var entities = this.getEntities();
  for ( var i = 0, n = entities.length; i < n; i++ ) {
    if ( entities[i].contains( x, y ) ) {
      return entities[i];
    }
  }

  return null;
};

TestGame2.prototype.processInput = function( elapsedTime ) {
  var angularAcceleration = 6 * Math.PI * elapsedTime;
  var dAngularVelocity = 10 * Math.PI / 180 * elapsedTime;

  var turningLeft = this.input.keys[ '37' ],
      turningRight = this.input.keys[ '39' ];

  if ( turningLeft ) {
    // this._playerEntity.setAngularAcceleration( this._playerEntity.getAngularAcceleration() + angularAcceleration );
    this._playerEntity.setAngularVelocity( dAngularVelocity );
  }
  if ( turningRight ) {
    // this._playerEntity.setAngularAcceleration( this._playerEntity.getAngularAcceleration() - angularAcceleration );
    this._playerEntity.setAngularVelocity( -dAngularVelocity );
  }

  if ( !turningLeft && !turningRight ) {
    this._playerEntity.setAngularAcceleration(0);

    var angularVelocity = 0.75 * this._playerEntity.getAngularVelocity();
    if ( Math.abs( angularVelocity ) < 1e-3 ) {
      angularVelocity = 0;
    }

    this._playerEntity.setAngularVelocity( angularVelocity );
  }
};

TestGame2.prototype.getCtx = function() {
  return this._ctx;
};

TestGame2.prototype.getCamera = function() {
  return this._camera;
};

TestGame2.prototype.removeEntity = function( entity ) {
  var entities = this.getEntities();
  var index = entities.indexOf( entity );
  if ( index !== -1 ) {
    entities.splice( index, 1 );
  }
};

TestGame2.prototype.getPlayer = function() {
  return this._player;
};
