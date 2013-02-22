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

function transformCoords( x, y ) {
  return {
    x: x - _game._canvas.offsetLeft,
    y: _game.HEIGHT - ( y - _game._canvas.offsetTop )
  };
}
