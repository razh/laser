$(function() {
  init();
});

var _test;
function init() {
  _test = new Test();

  _test.canvas.addEventListener( 'mousemove', onMouseMove, null );

  document.addEventListener( 'keydown', function( event ) {
    if ( event.which === 81 ) {
      if ( _test.intersections.length === 0 ) {
        return;
      }

      _test.running = false;
      console.log( 'ray: ' +
                   _test.ray.x + ', ' +
                   _test.ray.y + ', ' +
                   _test.ray.dx + ', ' +
                   _test.ray.dy );
      return;
    }

    for ( var i = 0, n = _test.intersections.length; i < n; i++ ) {
      console.log( _test.intersections[i] );
    }
  }, null );


  loop();
}

function loop() {
  if ( !_test.running ) {
    return;
  }

  _test.tick();

  requestAnimationFrame( loop );
}

function onMouseMove( event ) {
  _test.ray.x = event.pageX;
  _test.ray.y = event.pageY;
}


var Shape = function() {
  this.x = 0;
  this.y = 0;

  this.vertices = [];
  this.indices = [];

  this.width = 20;
  this.height = 20;
  this.rotation = 0;

  this.fillStyle = 'rgba( 0, 127, 127, 1.0 )';
  this.velocityY = 2;
  this.angularVelocity = Math.PI / 180;

  this.ray = new Ray();
};

Shape.prototype.worldToLocalCoordinates = function() {
  var x, y;
  if ( arguments.length === 1 ) {
    x = arguments[0].x;
    y = arguments[0].y;
  } else if ( arguments.length === 2 ) {
    x = arguments[0];
    y = arguments[1];
  }

  // Translate.
  x -= this.x;
  y -= this.y;

  // Rotate.
  var rotation = -this.rotation;
  if ( rotation !== 0 ) {
    var cos = Math.cos( rotation ),
        sin = Math.sin( rotation );

    var rx = cos * x - sin * y,
        ry = sin * x + cos * y;

    x = rx;
    y = ry;
  }

  // Scale.
  x /= this.width;
  y /= this.height;

  return {
    x: x,
    y: y
  };
};

Shape.prototype.localToWorldCoordinates = function() {
  var x, y;
  if ( arguments.length === 1 ) {
    x = arguments[0].x;
    y = arguments[0].y;
  } else if ( arguments.length === 2 ) {
    x = arguments[0];
    y = arguments[1];
  }

  // Scale.
  x *= this.width;
  y *= this.height;

  // Rotate.
  var rotation = this.rotation;
  if ( rotation !== 0 ) {
    var cos = Math.cos( rotation ),
        sin = Math.sin( rotation );

    var rx = cos * x - sin * y,
        ry = sin * x + cos * y;

    x = rx;
    y = ry;
  }

  // Translate.
  x += this.x;
  y += this.y;

  return {
    x: x,
    y: y
  };
};

Shape.prototype.drawPath = function( ctx ) {
  ctx.beginPath();

  var x = this.vertices[ 2 * this.indices[0] ],
      y = this.vertices[ 2 * this.indices[0] + 1 ];

  ctx.moveTo( x, y );

  for ( var i = 1, n = this.indices.length; i < n; i++ ) {
    x = this.vertices[ 2 * this.indices[i] ];
    y = this.vertices[ 2 * this.indices[i] + 1 ];

    ctx.lineTo( x, y );
  }

  ctx.closePath();
};

Shape.prototype.drawNormals = function( ctx ) {
  ctx.beginPath();

  var x0, y0, x1, y1;
  var hx, hy; // Line segment bisection point.
  var nx, ny; // Normal.
  var length;
  for ( var i = 0, n = this.indices.length - 1; i < n; i++ ) {
    x0 = this.vertices[ 2 * this.indices[i] ];
    y0 = this.vertices[ 2 * this.indices[i] + 1 ];
    x1 = this.vertices[ 2 * this.indices[ i + 1 ] ];
    y1 = this.vertices[ 2 * this.indices[ i + 1 ] + 1 ];

    hx = 0.5 * ( x0 + x1 );
    hy = 0.5 * ( y0 + y1 );

    nx = x1 - x0,
    ny = y1 - y0;

    length = Math.sqrt( nx * nx + ny * ny );
    if ( Math.abs( length ) < 1e-5 ) {
      continue;
    }

    length = 0.05 / length;

    nx *= length;
    ny *= length;

    ctx.moveTo( hx, hy );
    ctx.lineTo( hx + ny, hy - nx );
  }

  ctx.closePath();
};

Shape.prototype.drawRay = function( ctx ) {
  ctx.beginPath();

  ctx.moveTo( this.ray.x, this.ray.y );
  ctx.lineTo( this.ray.x + 1000 * this.ray.dx, this.ray.y + 1000 * this.ray.dy );
  ctx.lineWidth = 0.01;
  ctx.strokeStyle = 'rgba( 0, 0, 255, 1.0 )';
  ctx.stroke();

  ctx.closePath();
};

Shape.prototype.draw = function( ctx ) {
  ctx.save();

  ctx.translate( this.x, this.y );
  ctx.rotate( this.rotation );
  ctx.scale( this.width, this.height );

  this.drawNormals( ctx );
  ctx.strokeStyle = 'rgba( 0, 127, 0, 1.0 )';
  ctx.lineWidth = 0.01;
  ctx.stroke();

  ctx.fillStyle = this.fillStyle;
  ctx.strokeStyle = this.fillStyle;
  this.drawPath( ctx );
  ctx.lineWidth = 0.01;
  ctx.stroke();
  // ctx.fill();

  this.drawRay( ctx );

  ctx.restore();
};

var Ray = function() {
  this.x = 0;
  this.y = 0;
  this.dx = 1;
  this.dy = 0;
};

var Test = function() {
  this.WIDTH = window.innerWidth;
  this.HEIGHT = window.innerHeight;

  $( 'body' ).append( '<canvas>' );

  this.canvas = $( 'canvas' )[0];

  this.canvas.width = this.WIDTH;
  this.canvas.height = this.HEIGHT;

  this.ctx = this.canvas.getContext( '2d' );

  this.prevTime = Date.now();
  this.currTime = this.prevTime;
  this.running = true;

  this.shapes = [];
  this.generateShapes();

  this.ray = new Ray();
  this.ray.x = 400;
  this.ray.y = 200;

  // Careful: This contains both intersections and normals.
  this.intersections = [];
};

Test.prototype.tick = function() {
  this.update();
  this.draw();
};

var velocityX = 1;
var rotation = 0;
Test.prototype.update = function() {
  this.currTime = Date.now();
  var elapsedTime = this.currTime - this.prevTime;
  this.prevTime = this.currTime;

  rotation += 0.01;
  var cos = Math.cos( rotation );
  var sin = Math.sin( rotation );
  this.ray.dx = cos;
  this.ray.dy = sin;

  // Update shape locations.
  var i, n;
  var shape;
  for ( i = 0, n = this.shapes.length; i < n; i++ ) {
    shape = this.shapes[i];

    // shape.y += shape.velocityY;
    // shape.rotation += shape.angularVelocity;

    if ( shape.y - shape.height < 0 ||
         shape.y + shape.height > this.HEIGHT ) {
      shape.velocityY = -shape.velocityY;
    }
  }

  // Intersection.
  var rayOrigin, rayDirection;

  this.intersections = [];
  var intersection = null;
  for ( i = 0, n = this.shapes.length; i < n; i++ ) {
    shape = this.shapes[i];

    rayOrigin = shape.worldToLocalCoordinates( this.ray.x, this.ray.y );
    rayDirection = shape.worldToLocalCoordinates( this.ray.x + this.ray.dx,
                                                  this.ray.y + this.ray.dy );

    rayDirection.x -= rayOrigin.x;
    rayDirection.y -= rayOrigin.y;

    intersection = Intersection.rayGeometry( rayOrigin.x, rayOrigin.y,
                                             rayDirection.x, rayDirection.y,
                                             { vertices: shape.vertices,
                                               indices: shape.indices });

    shape.ray.x = rayOrigin.x;
    shape.ray.y = rayOrigin.y;
    shape.ray.dx = rayDirection.x;
    shape.ray.dy = rayDirection.y;

    if ( intersection !== null ) {
      shape.fillStyle = 'rgba( 0, 127, 0, 1.0 )';
      intersection.normal = shape.localToWorldCoordinates( intersection.normal.x + intersection.intersection.x,
                                                           intersection.normal.y + intersection.intersection.y );
      intersection.intersection = shape.localToWorldCoordinates( intersection.intersection );
      intersection.normal.x -= intersection.intersection.x;
      intersection.normal.y -= intersection.intersection.y;

      this.intersections.push( intersection );
    } else {
      shape.fillStyle = 'rgba( 127, 0, 0, 1.0 )';
    }
  }
};

Test.prototype.draw = function() {
  this.canvas.style.backgroundColor = '#ddd';
  this.ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

  // Draw shapes.
  var i, n;
  for ( i = 0, n = this.shapes.length; i < n; i++ ) {
    this.shapes[i].draw( this.ctx );
  }

  // Draw ray.
  this.ctx.fillStyle = 'rgba( 255, 0, 0, 1.0 )';
  this.ctx.fillRect( -10 + this.ray.x, -10 + this.ray.y, 20, 20 );
  this.ctx.beginPath();
  this.ctx.moveTo( this.ray.x, this.ray.y );
  this.ctx.lineTo( this.ray.x + 1000 * this.ray.dx,
                   this.ray.y + 1000 * this.ray.dy );
  this.ctx.closePath();
  this.ctx.strokeStyle = 'rgba( 255, 0, 0, 1.0 )';
  this.ctx.lineWidth = 1;
  this.ctx.stroke();

  // Draw intersection points.
  this.ctx.fillStyle = 'rgba( 0, 0, 255, 1.0 )';
  var intersection, normal;
  for ( i = 0, n = this.intersections.length; i < n; i++ ) {
    intersection = this.intersections[i].intersection;
    normal = this.intersections[i].normal;
    this.ctx.fillRect( intersection.x - 3, intersection.y - 3, 6, 6 );

    // Draw normal.
    this.ctx.beginPath();
    this.ctx.moveTo( intersection.x, intersection.y );
    this.ctx.lineTo( intersection.x + 10 * normal.x, intersection.y + 10 * normal.y );
    this.ctx.closePath();

    this.ctx.strokeStyle = 'rgba( 0, 0, 127, 1.0 )';
    this.ctx.stroke();
  }
};

Test.prototype.generateShapes = function() {
  var originX = 100;
  var originY = 150;

  var geometry;
  var shape;
  var count = 9;
  var dx = ( this.WIDTH - 100 ) / count;
  var dy = ( this.HEIGHT - 200 ) / count;
  for ( var i = 0; i < count; i++ ) {
    shape = new Shape();
    shape.x = originX + i * dx;
    shape.y = originY + i * dy;
    shape.width = 100 + i * 3;
    shape.height = 100 + i * 2;

    var startAngle = ( Math.PI / 180 ) * ( i + 1 ) * 4;
    var endAngle = ( Math.PI / 180 ) * ( ( i + 1 ) * 4 + ( i + 1 ) * 30 );
    geometry = Geometry.createRing({
      startAngle: startAngle,
      endAngle: endAngle,
      subdivisions: 4
    });
    console.log( shape.x + ', ' +
                 shape.y + ', ' +
                 startAngle * 180 / Math.PI + ', ' +
                 endAngle * 180 / Math.PI );

    shape.vertices = geometry.vertices;
    shape.indices = geometry.indices;

    // if ( i === 8 )
    this.shapes.push( shape );
  }
};
