var Circle = function() {
  this.x = 0;
  this.y = 0;
  this.radius = 100;

  this.fillStyle = 'rgba( 127, 0, 0, 1.0 )';
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

  this.circle1 = new Circle();
  this.circle2 = new Circle();

  this.ray = new Ray();
  this.ray.x = 400;
  this.ray.y = 200;

  this.circle1.y = 200;
  this.circle1.fillStyle = 'rgba( 0, 0, 127, 1.0 )';

  this.circle2.x = 200;
  this.circle2.y = 400;
  this.circle2.radius = 200;
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

  this.circle2.x += velocityX;
  if ( 100 > this.circle2.x || this.circle2.x > 600 ) {
    velocityX = -velocityX;
  }

  rotation += 0.01;
  var sin = Math.sin( rotation );
  var cos = Math.cos( rotation );
  this.ray.dx = sin;
  this.ray.dy = cos;

  // Intersection.
  var intersectCircle1 = Intersection.rayCircle( this.ray.x, this.ray.y,
                                                 this.ray.dx, this.ray.dy,
                                                 this.circle1.x, this.circle1.y,
                                                 this.circle1.radius );
  if ( intersectCircle1 !== null ) {
    this.circle1.fillStyle = 'rgba( 0, 127, 0, 1.0 )';
  } else {
    this.circle1.fillStyle = 'rgba( 0, 0, 127, 1.0 )';
  }

  var intersectCircle2 = Intersection.rayCircle( this.ray.x, this.ray.y,
                                                 this.ray.dx, this.ray.dy,
                                                 this.circle2.x, this.circle2.y,
                                                 this.circle2.radius );
  if ( intersectCircle2 !== null ) {
    this.circle2.fillStyle = 'rgba( 0, 127, 0, 1.0 )';
  } else {
    this.circle2.fillStyle = 'rgba( 127, 0, 0, 1.0 )';
  }
};

Test.prototype.draw = function() {
  this.canvas.style.backgroundColor = '#ddd';
  this.ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

  this.ctx.beginPath();
  this.ctx.arc( this.circle1.x, this.circle1.y, this.circle1.radius, 0, Math.PI * 2, true );
  this.ctx.closePath();

  this.ctx.fillStyle = this.circle1.fillStyle;
  this.ctx.fill();

  this.ctx.beginPath();
  this.ctx.arc( this.circle2.x, this.circle2.y, this.circle2.radius, 0, Math.PI * 2, true );
  this.ctx.closePath();

  this.ctx.fillStyle = this.circle2.fillStyle;
  this.ctx.fill();

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

};
