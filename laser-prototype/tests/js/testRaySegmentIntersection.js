var Segment = function() {
  this.x0 = 0;
  this.y0 = 0;
  this.x1 = 0;
  this.y1 = 0;

  this.color = 'rgba( 127, 0, 0, 1.0 )';
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
  if ( ( x1 - x0 ) * ( x1 - x0 ) +
       ( y1 - y0 ) * ( y1 - y0 ) < 1e-5 ) {
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

  this.segments = [];
  this.intersections = [];

  this.ray = new Ray();
  this.ray.x = 400;
  this.ray.y = 200;

  this.tests = [];
  this.testSlope();

  this.generateSegments();
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

  // Intersection.
  this.intersections = [];
  var intersection = null;
  for ( var i = 0, n = this.segments.length; i < n; i++ ) {
    intersection = Intersection.raySegment( this.ray.x, this.ray.y,
                                            this.ray.dx, this.ray.dy,
                                            this.segments[i].x0, this.segments[i].y0,
                                            this.segments[i].x1, this.segments[i].y1 );
    if ( intersection !== null ) {
      this.intersections.push( intersection );
      this.segments[i].color = 'rgba( 0, 127, 0, 1.0 )';
    } else {
      this.segments[i].color = 'rgba( 127, 0, 0, 1.0 )';
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

  // Draw segments.
  var i, n;
  for ( i = 0, n = this.segments.length; i < n; i++ ) {
    this.ctx.beginPath();
    this.ctx.moveTo( this.segments[i].x0, this.segments[i].y0 );
    this.ctx.lineTo( this.segments[i].x1, this.segments[i].y1 );
    this.ctx.closePath();

    this.ctx.strokeStyle = this.segments[i].color;
    this.ctx.lineWidth = 1;
    this.ctx.stroke();
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

/**
 * Generates segments in an arc.
 */
Test.prototype.generateSegments = function() {
  var originX = 400;
  var originY = 400;

  var outerRadius = 100;
  var innerRadius = 50;
  var sin, cos;
  var segment;
  var count = 36;
  for ( var i = 0; i < count; i++ ) {
    sin = Math.sin( ( Math.PI / 180 ) * ( i * 360 / count ) );
    cos = Math.cos( ( Math.PI / 180 ) * ( i * 360 / count ) );
    segment = new Segment();
    segment.x0 = originX + innerRadius * cos;
    segment.y0 = originY + innerRadius * sin;
    segment.x1 = originX + outerRadius * cos;
    segment.y1 = originY + outerRadius * sin;
    this.segments.push( segment );
  }
};

Test.prototype.testSlope = function() {
  var angle45 = Math.PI / 180 * 45;
  var cos = Math.cos( angle45 );
  var sin = Math.sin( angle45 );
  // Test if ray with same slope and y-intercept as segment intersects correctly.
  var ray = new Ray();
  ray.x = 200;
  ray.y = 200;
  ray.dx = cos;
  ray.dy = sin;

  var segment = new Segment();
  segment.x0 = 200;
  segment.y0 = 200;
  segment.x1 = 200 + cos;
  segment.y1 = 200 + sin;

  this.tests.push(
    Intersection.raySegment(
      ray.x, ray.y,
      ray.dx, ray.dy,
      segment.x0, segment.y0,
      segment.x1, segment.y1
    ) !== null
  );

  // Reverse segment.
  this.tests.push(
    Intersection.raySegment(
      ray.x, ray.y,
      ray.dx, ray.dy,
      segment.x1, segment.y1,
      segment.x0, segment.y0
    ) !== null
  );

  // Segment behind ray.
  segment.x0 = 199;
  segment.y0 = 199;
  segment.x1 = 199 - cos;
  segment.x2 = 199 - sin;

  this.tests.push(
    Intersection.raySegment(
      ray.x, ray.y,
      ray.dx, ray.dy,
      segment.x0, segment.y0,
      segment.x1, segment.y1
    ) === null
  );

  // Reverse segment.
  this.tests.push(
    Intersection.raySegment(
      ray.x, ray.y,
      ray.dx, ray.dy,
      segment.x1, segment.y1,
      segment.x0, segment.y0
    ) === null
  );

  // Vertical slope.
  ray.dx = 0;
  ray.dy = 1;
  segment.x0 = 200;
  segment.y0 = 300;
  segment.x1 = 200;
  segment.y1 = 400;
  this.tests.push(
    function() {
      var point = Intersection.raySegment(
        ray.x, ray.y,
        ray.dx, ray.dy,
        segment.x0, segment.y0,
        segment.x1, segment.y1
      );
      return closeEnough( point.x, point.y, segment.x0, segment.y0 );
    }
  );

  // Reverse segment.
  this.tests.push(
    function() {
      var point = Intersection.raySegment(
        ray.x, ray.y,
        ray.dx, ray.dy,
        segment.x1, segment.y1,
        segment.x0, segment.y0
      );
      return closeEnough( point.x, point.y, segment.x0, segment.y0 );
    }
  );

  // Horizontal slope.
  ray.dx = 1;
  ray.dy = 0;
  segment.x0 = 300;
  segment.y0 = 200;
  segment.x1 = 400;
  segment.y1 = 200;
  this.tests.push(
    function() {
      var point = Intersection.raySegment(
        ray.x, ray.y,
        ray.dx, ray.dy,
        segment.x0, segment.y0,
        segment.x1, segment.y1
      );
      return closeEnough( point.x, point.y, segment.x0, segment.y0 );
    }
  );

  // Reverse segment.
  this.tests.push(
    function() {
      var point = Intersection.raySegment(
        ray.x, ray.y,
        ray.dx, ray.dy,
        segment.x1, segment.y1,
        segment.x0, segment.y0
      );
      return closeEnough( point.x, point.y, segment.x0, segment.y0 );
    }
  );
};

