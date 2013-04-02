var beam;

function init() {
  _game = new BeamTest();

  beam = new Beam();
  beam.setPosition( 200, 100 );
  beam.setColor( 200, 50, 50, 0.25 );

  beam.getBeamWidth().set( -10, 10 );
  beam.setBeamLength( 300 );

  beam.getSegmentWidth().set( 5, 20 );
  beam.getSegmentLength().set( 100, 200 );
  beam.getSegmentVelocity().set( 100, 200 );
  beam.getSegmentInterval().set( 2, 10 );

  beam.setLayerCount( 30 );
  beam.setRotationInDegrees( 45 );

  _game.addEntity( beam );

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
}

BeamTest.prototype = new Game();
BeamTest.prototype.constructor = BeamTest;

BeamTest.prototype.update = function() {
  Game.prototype.update.call( this );

  // this._entities[0].rotate( 1 * Math.PI / 180 );
};
