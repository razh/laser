var AABB = function() {
  this.x0 = 0;
  this.y0 = 0;
  this.x1 = 0;
  this.y1 = 0;

  this.color = 'rgba( 127, 0, 0, 1.0 )';
  this.velocityY = 2;
};

var Ray = function() {
  this.x = 0;
  this.y = 0;
  this.dx = 1;
  this.dy = 0;
};

$(function() {
  init();
});

var _test;
function init() {
  _test = new Test();

  _test.canvas.addEventListener( 'mousemove', onMouseMove, null );

  loop();
}

function loop() {
  _test.tick();

  requestAnimationFrame( loop );
}

function onMouseMove( event ) {
  _test.ray.x = event.pageX;
  _test.ray.y = event.pageY;
}

function closeEnough( x0, y0, x1, y1 ) {
  if ( Math.abs( ( x1 - x0 ) * ( x1 - x0 ) -
                 ( y1 - y0 ) * ( y1 - y0 ) ) < 1e-5 ) {
    return true;
  }

  return false;
}

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

  this.aabbs = [];
  this.intersections = [];

  this.ray = new Ray();
  this.ray.x = 400;
  this.ray.y = 200;

  this.tests = [];
  this.testSlope();

  this.generateAABBs();
};

Test.prototype.tick = function() {
  this.update();
  this.draw();
};

var rotation = 0;
Test.prototype.update = function() {
  this.currTime = Date.now();
  var elapsedTime = this.currTime - this.prevTime;
  this.prevTime = this.currTime;

  rotation += 0.01;
  var sin = Math.sin( rotation );
  var cos = Math.cos( rotation );
  this.ray.dx = sin;
  this.ray.dy = cos;

  // Update AABB locations.
  var i, n;
  var aabb;
  for ( i = 0, n = this.aabbs.length; i < n; i++ ) {
    aabb = this.aabbs[i];

    aabb.y0 += aabb.velocityY;
    aabb.y1 += aabb.velocityY;

    if ( aabb.y0 < 150 ||
         aabb.y1 > this.HEIGHT - 150 ) {
      aabb.velocityY = -aabb.velocityY;
    }
  }

  // Intersection.
  this.intersections = [];
  var intersection = null;
  for ( i = 0, n = this.aabbs.length; i < n; i++ ) {
    aabb = this.aabbs[i];

    intersection = Intersection.rayAABB( this.ray.x, this.ray.y,
                                         this.ray.dx, this.ray.dy,
                                         aabb.x0, aabb.y0,
                                         aabb.x1, aabb.y1 );
    if ( intersection !== null ) {
      this.intersections.push( intersection );
      aabb.color = 'rgba( 0, 127, 0, 1.0 )';
    } else {
      aabb.color = 'rgba( 127, 0, 0, 1.0 )';
    }
  }
};

Test.prototype.draw = function() {
  this.canvas.style.backgroundColor = '#ddd';
  this.ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

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

  // Draw aabbs.
  var i, n;
  var width, height;
  for ( i = 0, n = this.aabbs.length; i < n; i++ ) {
    width = this.aabbs[i].x1 - this.aabbs[i].x0;
    height = this.aabbs[i].y1 - this.aabbs[i].y0;

    this.ctx.fillStyle = this.aabbs[i].color;
    this.ctx.fillRect( this.aabbs[i].x0, this.aabbs[i].y0, width, height );
    this.ctx.fill();
  }

  // Draw intersection points.
  this.ctx.fillStyle = 'rgba( 0, 0, 127, 1.0 )';
  for ( i = 0, n = this.intersections.length; i < n; i++ ) {
    this.ctx.fillRect( this.intersections[i].x - 2, this.intersections[i].y - 2, 4, 4 );
  }

  // Hacky way of running tests.
  // Check if tests have completed successfully.
  for ( i = 0, n = this.tests.length; i < n; i++ ) {
    if ( this.tests[i] ) {
      this.ctx.fillStyle = 'rgba( 0, 127, 0, 1.0 )';
    } else {
      this.ctx.fillStyle = 'rgba( 127, 0, 0, 1.0 )';
    }
    this.ctx.fillRect( 50 + i * 100, 50, 50, 50 );
  }
};

Test.prototype.generateAABBs = function() {
  var originX = 50;
  var originY = 150;

  var aabb;
  var count = 10;
  var dx = ( this.WIDTH - 100 ) / count;
  var dy = ( this.HEIGHT - 300 ) / count;
  for ( var i = 0; i < count; i++ ) {
    aabb = new AABB();
    aabb.x0 = originX + i * dx;
    aabb.y0 = originY + i * dy;
    aabb.x1 = aabb.x0 + 50;
    aabb.y1 = aabb.y0 + 40 + i * 3;
    this.aabbs.push( aabb );
  }
};

Test.prototype.testSlope = function() {
  var angle45 = Math.PI / 180 * 45;
  var cos = Math.cos( angle45 );
  var sin = Math.sin( angle45 );
  // Test if ray with same slope and y-intercept as aabb intersects correctly.
  var ray = new Ray();
  ray.x = 200;
  ray.y = 200;
  ray.dx = cos;
  ray.dy = sin;

  var aabb = new AABB();
  aabb.x0 = 200;
  aabb.y0 = 200;
  aabb.x1 = 200 + cos;
  aabb.y1 = 200 + sin;

  this.tests.push(
    Intersection.rayAABB(
      ray.x, ray.y,
      ray.dx, ray.dy,
      aabb.x0, aabb.y0,
      aabb.x1, aabb.y1
    ) !== null
  );

  // Reverse aabb.
  this.tests.push(
    Intersection.rayAABB(
      ray.x, ray.y,
      ray.dx, ray.dy,
      aabb.x1, aabb.y1,
      aabb.x0, aabb.y0
    ) !== null
  );

  // AABB behind ray.
  aabb.x0 = 199;
  aabb.y0 = 199;
  aabb.x1 = 199 - cos;
  aabb.x2 = 199 - sin;

  this.tests.push(
    Intersection.rayAABB(
      ray.x, ray.y,
      ray.dx, ray.dy,
      aabb.x0, aabb.y0,
      aabb.x1, aabb.y1
    ) === null
  );

  // Reverse aabb.
  this.tests.push(
    Intersection.rayAABB(
      ray.x, ray.y,
      ray.dx, ray.dy,
      aabb.x1, aabb.y1,
      aabb.x0, aabb.y0
    ) === null
  );

  // Vertical slope.
  ray.dx = 0;
  ray.dy = 1;
  aabb.x0 = 200;
  aabb.y0 = 300;
  aabb.x1 = 200;
  aabb.y1 = 400;
  this.tests.push(
    (function() {
      var point = Intersection.rayAABB(
        ray.x, ray.y,
        ray.dx, ray.dy,
        aabb.x0, aabb.y0,
        aabb.x1, aabb.y1
      );
      return closeEnough( point.x, point.y, aabb.x0, aabb.y0 );
    }) ()
  );

  // Reverse aabb.
  this.tests.push(
    (function() {
      var point = Intersection.rayAABB(
        ray.x, ray.y,
        ray.dx, ray.dy,
        aabb.x1, aabb.y1,
        aabb.x0, aabb.y0
      );
      return closeEnough( point.x, point.y, aabb.x0, aabb.y0 );
    }) ()
  );

  // Horizontal slope.
  ray.dx = 1;
  ray.dy = 0;
  aabb.x0 = 300;
  aabb.y0 = 200;
  aabb.x1 = 400;
  aabb.y1 = 200;
  this.tests.push(
    (function() {
      var point = Intersection.rayAABB(
        ray.x, ray.y,
        ray.dx, ray.dy,
        aabb.x0, aabb.y0,
        aabb.x1, aabb.y1
      );
      return closeEnough( point.x, point.y, aabb.x0, aabb.y0 );
    }) ()
  );

  // Reverse aabb.
  this.tests.push(
    (function() {
      var point = Intersection.rayAABB(
        ray.x, ray.y,
        ray.dx, ray.dy,
        aabb.x1, aabb.y1,
        aabb.x0, aabb.y0
      );
      return closeEnough( point.x, point.y, aabb.x0, aabb.y0 );
    }) ()
  );
};

