function onMouseDown( event ) {
  var input = transformCoords( event.pageX, event.pageY );

  var player = _game.getPlayer();
  player.setSelected( _game.hit( input.x, input.y ) );
  if ( player.hasSelected() ) {
    var selected = player.getSelected();
    player.setOffset( selected.getX() - input.x,
                      selected.getY() - input.y );
  }
}

function onMouseMove( event ) {
  var input = transformCoords( event.pageX, event.pageY );

  var player = _game.getPlayer();
  if ( player.hasSelected() ) {
    player.getSelected().setPosition( input.x + player.getOffsetX(),
                                      input.y + player.getOffsetY() );
  }
}

function onMouseUp( event ) {
  var player = _game.getPlayer();
  if ( player.hasSelected() ) {
    player.setSelected( null );
  }
}

function onKeyDown( event ) {
  switch ( event.which ) {
    // Q.
    case 81:
      quit();
      break;

    // LEFT.
    case 37:
    // RIGHT.
    case 39:
      var player = _game.getPlayer();
      var angle = Math.PI / 180 * 10;
      if ( player.hasSelected() ) {
        if ( event.which === 37 ) {
          player.getSelected().rotate( angle );
        } else {
          player.getSelected().rotate( -angle );
        }
      }
      break;

    // SPACE.
    case 32:
      test();
      break;

    default:
      console.log( event.which );
      break;
  }
}

function transformCoords( x, y ) {
  return {
    x: x - _game._canvas.offsetLeft,
    y: _game.HEIGHT - ( y - _game._canvas.offsetTop )
  };
}

var testing = false;
function test() {
  testing = true;
}
