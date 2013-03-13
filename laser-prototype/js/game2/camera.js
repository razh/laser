var Camera = function() {
  Entity.call( this );

  this._widthRatio = 4;
  this._heightRatio = 3;
};

Camera.prototype = new Entity();
Camera.prototype.constructor = Camera;

Camera.prototype.getWidthRatio = function() {
  return this._widthRatio;
};

Camera.prototype.setWidthRatio = function( widthRatio ) {
  this._widthRatio = widthRatio;
  return this;
};

Camera.prototype.getHeightRatio = function() {
  return this._heightRatio;
};

Camera.prototype.setHeightRatio = function( heightRatio ) {
  this._heightRatio = heightRatio;
  return this;
};

Camera.prototype.getAspectRatio = function() {
  return {
    width: this.getWidthRatio(),
    height: this.getHeightRatio()
  };
};

/**
 * Not entirely accurate, as it's height / width rather than the inverse.
 */
Camera.prototype.getAspectRatioAsFloat = function() {
  return this.getHeightRatio() / this.getWidthRatio();
};

Camera.prototype.setAspectRatio = function() {
  var width, height;
  if ( arguments.length === 1 ) {
    width = arguments[0].width;
    height = arguments[0].height;
  } else if ( arguments.length === 2 ) {
    width = arguments[0];
    height = arguments[1];
  }
  
  var dividend = width,
      divisor = height,
      modulo, gcd;
  while ( divisor !== 0 ) {
    modulo = dividend % divisor;
    dividend = gcd;
    gcd = modulo;
  }
  
  this.setWidthRatio( width / gcd );
  this.setHeightRatio( height / gcd );
  
  return this;
};

/**
 * Pass in screen width and height, as well as desired width and height.
 */
Camera.prototype.setViewport = function( x, y, width, height, screenWidth, screenHeight ) {
  var screenTransform,
      viewportTransform,
      deviceWidth,
      deviceHeight,
      lengthen;

  // Resize to fit screen viewport.
  if ( screenHeight / screenWidth < height / width ) {
    screenTransform = screenHeight / height;
    viewportTransform = height / screenHeight;
    deviceWidth = width * screenTransform;
    lengthen = ( screenWidth - deviceWidth ) * viewportTransform;
    
    this.setWidth( width + lengthen );
    this.setHeight( height );
  } else {
    screenTransform = screenWidth / width;
    viewportTransform = width / screenWidth;
    deviceHeight = height * screenTransform;
    lengthen = ( screenHeight - deviceHeight ) * viewportTransform;
    
    this.setHeight( height + lengthen );
    this.setWidth( width );
  }
  
  this.setPosition( x + 0.5 * this.getWidth(),
                    y + 0.5 * this.getHeight() );
  this.setAspectRatio( this.getWidth(), this.getheight() );
  
  return this;
};
