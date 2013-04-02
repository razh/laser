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
