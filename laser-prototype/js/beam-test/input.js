function onKeyDown( event ) {
  _game.input.keys[ event.which.toString() ] = true;

  switch ( event.which ) {
    // Q.
    case 81:
      quit();
      break;

    default:
      console.log( event.which );
      break;
  }
}

function onKeyUp( event ) {
  _game.input.keys[ event.which.toString() ] = false;
}

function transformCoords( x, y ) {
  return {
    x: x - _game._canvas.offsetLeft,
    y: _game.HEIGHT - ( y - _game._canvas.offsetTop )
  };
}
