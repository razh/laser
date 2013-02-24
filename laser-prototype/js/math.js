
var vec2 = {};

vec2.FLOAT_EPSILON = 1e-5;

// Most of this is taken from toji's gl-matrix.js with some slight changes.
// Mostly to use objects rather than arrays.
vec2.create = function( x, y ) {
  return {
    x: x,
    y: y
  };
};

vec2.add = function( a, b, dest ) {
  if ( !dest ) { dest = b; }
  dest.x = a.x + b.x;
  dest.y = a.y + b.y;
  return dest;
};

vec2.subtract = function( a, b, dest ) {
  if ( !dest ) { dest = b; }
  dest.x = a.x - b.x;
  dest.y = a.y - b.y;
  return dest;
};

vec2.multiply = function( a, b, dest ) {
  if ( !dest ) { dest = b; }
  dest.x = a.x * b.x;
  dest.y = a.y * b.y;
  return dest;
};

vec2.divide = function( a, b, dest ) {
  if ( !dest ) { dest = b; }
  dest.x = a.x / b.x;
  dest.y = a.y / b.y;
  return dest;
};

vec2.scale = function( vec, scalar, dest ) {
  if ( !dest ) { dest = vec; }
  dest.x = vec.x * scalar;
  dest.y = vec.y * scalar;
  return dest;
};

vec2.dist = function( a, b ) {
  var x = b.x - a.x,
      y = b.y - a.y;
  return Math.sqrt( x * x + y * y );
};

vec2.set = function( vec, dest ) {
  dest.x = vec.x;
  dest.y = vec.y;
  return dest;
};

vec2.equal = function( a, b ) {
  return a === b || (
    Math.abs( a.x - b.x ) < FLOAT_EPSILON &&
    Math.abs( a.y - b.y ) < FLOAT_EPSILON
  );
};

vec2.negate = function( vec, dest ) {
  if ( !dest ) { dest = vec; }
  dest.x = -vec.x;
  dest.y = -vec.y;
  return dest;
};

vec2.normalize = function( vec, dest ) {
  if ( !dest ) { dest = vec; }

  var length = vec2.length( vec );
  if ( length > 0 ) {
    length = 1 / Math.sqrt( length );
    dest.x = vec.x * length;
    dest.y = vec.y * length;
  } else {
    dest.x = dest.y = 0;
  }

  return dest;
};

vec2.length = function( vec ) {
  return Math.sqrt( vec2, lengthSquared( vec ) );
};

vec2.lengthSquared = function( vec ) {
  return vec.x * vec.x + vec.y * vec.y;
};

vec2.dot = function( a, b ) {
  return a.x * b.x + a.y * b.y;
};

vec2.direction = function( a, b, dest ) {
  if ( !dest ) { dest = a; }

  vec2.sub( b, a, dest );
  return vec2.normalize( dest );
};

vec2.lerp = function( a, b, lerp, dest ) {
  if ( !dest ) { dest = a; }
  dest.x = a.x + lerp * ( b.x - a.x );
  dest.y = a.y + lerp * ( b.y - a.y );
  return dest;
};
