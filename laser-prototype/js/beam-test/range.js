function Range() {
  this._start = 0.0;
  this._end = 0.0;
}

Range.prototype.getStart = function() {
  return this._start;
};

Range.prototype.setStart = function( start ) {
  this._start = start;
  return this;
};

Range.prototype.getEnd = function() {
  return this._end;
};

Range.prototype.setEnd = function( end ) {
  this._end = end;
  return this;
};

Range.prototype.set = function() {
  if ( arguments.length === 1 ) {
    this.setStart( arguments[0].getStart() );
    this.setEnd( arguments[0].getEnd() );
  } else if ( arguments.length === 2 ) {
    this.setStart( arguments[0] );
    this.setEnd( arguments[1] );
  }

  return this;
};

Range.prototype.random = function() {
  return this._start + Math.random() * ( this._end - this._start );
};
