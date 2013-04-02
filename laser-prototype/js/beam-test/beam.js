function Beam() {
  PhysicsEntity.call( this );

  this._color = new Color( 255, 255, 255, 1.0 );

  this._segmentLengths = [];
  this._segmentVelocities = [];

  this._layerCount = 0;

  // Interval where we spawn segments.
  this._beamWidth = new Range();
  // Maximum length of beam.
  this._beamLength = 500;

  this._segmentWidth = new Range();
  this._segmentLength = new Range();

  this._segmentVelocity = new Range();
  this._segmentInterval = new Range();
}

Beam.prototype = new PhysicsEntity();
Beam.prototype.constructor = Beam;

Beam.prototype.draw = function( ctx ) {
  ctx.globalCompositeOperation = 'lighter';
  PhysicsEntity.prototype.draw.call( this, ctx );
};

Beam.prototype.update = function( elapsedTime ) {
  PhysicsEntity.prototype.update.call( this, elapsedTime );

  // Convert from milliseconds to seconds.
  elapsedTime *= 1e-3;

  var segment;
  for ( var i = 0, n = this._shapes.length; i < n; i++ ) {
    // If the segment length is less than the desired segment length, increase
    // the length by velocity * dt.
    segment = this._shapes[i];
    if ( segment.getWidth() < this._segmentLengths[i] ) {
      segment.setWidth( segment.getWidth() + this._segmentVelocities[i] * elapsedTime );
    } else {
      // Otherwise, move the segment along.
      segment.translateX( this._segmentVelocities[i] * elapsedTime );
    }

    // Reset if more than beam length.
    if ( segment.getX() > this._beamLength ) {
      console.log( 'reset')
      segment.setWidth(0);
      segment.setX(0);
    }
  }
};

Beam.prototype.getLayerCount = function() {
  return this._layerCount;
};

Beam.prototype.setLayerCount = function( layerCount ) {
  if ( this._layerCount > layerCount ) {
    var count = this._layerCount - layerCount;
    while ( count > 0 ) {
      this._shapes.splice( this._shapes.length - 1, 1  );
    }
  }

  this._layerCount = layerCount;
  this.allocateLayerActors();
  return this;
};

Beam.prototype.allocateLayerActors = function() {
  var rect;
  for ( var i = this._shapes.length, n = this._layerCount; i < n; i++ ) {
    rect = new Rectangle().setY( this._beamWidth.random() )
                          .setWidth(0) // Initial width of each rectangle is zero.
                          .setHeight( this._segmentWidth.random() )
                          .setColor( this.getColor() );

    this._shapes.push( rect );
    this._segmentLengths.push( this._segmentLength.random() );
    this._segmentVelocities.push( this._segmentVelocity.random() );
  }
};

Beam.prototype.getBeamWidth = function() {
  return this._beamWidth;
};

Beam.prototype.getBeamLength = function() {
  return this._beamLength;
};

Beam.prototype.setBeamLength = function( beamLength ) {
  this._beamLength = beamLength;
};

Beam.prototype.getSegmentWidth = function() {
  return this._segmentWidth;
};

Beam.prototype.getSegmentLength = function() {
  return this._segmentLength;
};

Beam.prototype.getSegmentVelocity = function() {
  return this._segmentVelocity;
};

Beam.prototype.getSegmentInterval = function() {
  return this._segmentInterval;
};

Beam.prototype.getColor = function() {
  return this._color;
};

Beam.prototype.setColor = function() {
  this.getColor().set.apply( this.getColor(), arguments );
  return this;
};

