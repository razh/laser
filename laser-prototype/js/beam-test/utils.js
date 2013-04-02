Array.prototype.addFirst = function( object ) {
  this.splice( 0, 0, object );
};

Array.prototype.removeLast = function() {
  return this.splice( this.length - 1, 1 );
};
