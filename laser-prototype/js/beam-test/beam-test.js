function init() {
  _game = new BeamTest();

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
}

BeamTest.prototype = new Game();
BeamTest.prototype.constructor = BeamTest;
