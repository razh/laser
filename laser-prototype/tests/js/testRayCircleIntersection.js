var Circle = function() {
  this.x = 0;
  this.y = 0;
  this.radius = 100;

  this.fillStyle = 'rgba( 0, 127, 127, 1.0 )';
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

  this.circles = [];
  this.generateCircles();

  this.ray = new Ray();
  this.ray.x = 400;
  this.ray.y = 200;

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

  // Update circle locations.
  var i, n;
  var circle;
  for ( i = 0, n = this.circles.length; i < n; i++ ) {
    circle = this.circles[i];

    circle.y += circle.velocityY;

    if ( circle.y - circle.radius < 0 ||
         circle.y + circle.radius > this.HEIGHT ) {
      circle.velocityY = -circle.velocityY;
    }
  }

  // Intersection.
  this.intersections = [];
  var intersection = null;
  for ( i = 0, n = this.circles.length; i < n; i++ ) {
    circle = this.circles[i];

    intersection = Intersection.rayCircle( this.ray.x, this.ray.y,
                                           this.ray.dx, this.ray.dy,
                                           circle.x, circle.y,
                                           circle.radius );
    if ( intersection !== null ) {
      circle.fillStyle = 'rgba( 0, 127, 0, 1.0 )';
      this.intersections.push( intersection );
    } else {
      circle.fillStyle = 'rgba( 0, 0, 127, 1.0 )';
    }
  }
};

Test.prototype.draw = function() {
  this.canvas.style.backgroundColor = '#ddd';
  this.ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

  // Draw circles.
  var circle;
  var i, n;
  for ( i = 0, n = this.circles.length; i < n; i++ ) {
    circle = this.circles[i];

    this.ctx.beginPath();
    this.ctx.arc( circle.x, circle.y, circle.radius, 0, Math.PI * 2, true );
    this.ctx.closePath();

    this.ctx.fillStyle = circle.fillStyle;
    this.ctx.fill();
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
  for ( i = 0, n = this.intersections.length; i < n; i++ ) {
    this.ctx.fillRect( this.intersections[i].x - 3, this.intersections[i].y - 3, 6, 6 );
  }
};

Test.prototype.generateCircles = function() {
  var originX = 50;
  var originY = 150;

  var circle;
  var count = 9;
  var dx = ( this.WIDTH ) / count;
  var dy = ( this.HEIGHT - 200 ) / count;
  for ( var i = 0; i < count; i++ ) {
    circle = new Circle();
    circle.x = originX + i * dx;
    circle.y = originY + i * dy;
    circle.radius = 35 + i * 3;
    this.circles.push( circle );
  }
};
