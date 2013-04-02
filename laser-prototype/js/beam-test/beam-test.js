function init() {
  _game = new BeamTest();

  _game.addEntity( new Rectangle().setPosition( 200, 300 )
                                  .setWidth( 300 )
                                  .setHeight( 200 ) );

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

BeamTest.prototype.update = function( elapsedTime ) {
  Game.prototype.update.call( this, elapsedTime );

  this._entities[0].rotate( 2 * Math.PI / 180 );
};
