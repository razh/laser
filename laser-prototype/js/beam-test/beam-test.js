function init() {
  _game = new BeamTest();

  _game._beam.setPosition( 200, 100 );
  _game._beam.setMaxAngularVelocity( 1000 );
  _game._beam.setColor( 200, 100, 100, 0.25 );

  _game._beam.getBeamWidth().set( -5, 5 );
  _game._beam.setBeamLength( 750 );

  _game._beam.getSegmentWidth().set( 5, 20 );
  _game._beam.getSegmentLength().set( 200, 300 );
  _game._beam.getSegmentVelocity().set( 1000, 3000 );
  _game._beam.getSegmentInterval().set( 2, 10 );

  _game._beam.setLayerCount( 30 );
  _game._beam.setRotationInDegrees( 45 );

  _game.addEntity( _game._beam );

  document.addEventListener( 'keydown', onKeyDown, null );
  document.addEventListener( 'keyup', onKeyUp, null );

  loop();
}

function BeamTest() {
  Game.call( this );

  $( 'body' ).append( '<canvas>' );

  this._canvas = $( 'canvas' )[0];

  this._canvas.width = this.WIDTH;
  this._canvas.height = this.HEIGHT;

  this._ctx = this._canvas.getContext( '2d' );

  this._player = new Player();
  this._playerEntity = new PlayerBeamEntity();

  this._beam = new Beam();

  this.input = {
    keys: {}
  };

  this.setBackgroundColor( 50, 50, 50, 1.0 );
}

BeamTest.prototype = new Game();
BeamTest.prototype.constructor = BeamTest;

BeamTest.prototype.update = function() {
  // Need elapsedTime here, so no calling Game.proto.update().
  this._currTime = Date.now();
  var elapsedTime = this._currTime - this._prevTime;
  this._prevTime = this._currTime;

  if ( elapsedTime > 1e3 ) {
    elapsedTime = 1e3;
  }

  this.processInput( elapsedTime );

  var entities = this.getEntities();
  for ( var i = 0, n = entities.length; i < n; i++ ) {
    entities[i].update( elapsedTime );
  }

   // this._entities[0].rotate( 1 * Math.PI / 180 );
};

BeamTest.prototype.processInput = function( elapsedTime ) {
  elapsedTime *= 1e-3;
  var dAngularVelocity = 5000 * Math.PI / 180 * elapsedTime;

  var turningLeft = this.input.keys[ '37' ],
      turningRight = this.input.keys[ '39' ];

  if ( turningLeft ) {
    this._beam.setAngularVelocity( dAngularVelocity );
  }
  if ( turningRight ) {
    this._beam.setAngularVelocity( -dAngularVelocity );
  }
  // console.log( this._beam.getAngularVelocity() );

  if ( !turningLeft && !turningRight ) {
    this._beam.setAngularVelocity(0);
  }
};
