var Geometry = (function() {
  return {
    createRectangle: function() {
      var halfWidth = 0.5;
      var halfHeight = 0.5;

      var vertices = [
        -halfWidth, -halfHeight,
         halfWidth, -halfHeight,
         halfWidth,  halfHeight,
        -halfWidth,  halfHeight
      ];

      var indices = [ 0, 1, 2, 3, 0 ];

      return {
        vertices: vertices,
        indices: indices
      };
    },

    createRing: function( options ) {
      options = options || {};

      var outerRadius   = options.outerRadius || 1.0,
          innerRadius   = options.innerRadius,
          startAngle    = options.startAngle,
          endAngle      = options.endAngle,
          subdivisions  = options.subdivisions || 32;
          anticlockwise = options.anticlockwise;

      // Allow false/zero values to be passed in.
      if ( typeof innerRadius === 'undefined' ) { innerRadius = 0.5; }
      if ( typeof startAngle === 'undefined' ) { startAngle = 0; }
      if ( typeof endAngle === 'undefined' ) { endAngle = 2 * Math.PI; }
      if ( typeof anticlockwise === 'undefined' ) { anticlockwise = true; }

      var vertices = [];
      var indices = [];

      var sweepAngle = endAngle - startAngle;

      // Check for circle.
      if ( Math.abs( sweepAngle ) >= 2 * Math.PI ) {
        endAngle = startAngle + 2 * Math.PI;
      } else {
        // Anticlockwise arcs have positive sweepAngles.
        // Clockwise arcs have negative sweepAngles.
        // If the above is false, move the sweepAngle to the correct sign.
        if ( anticlockwise && sweepAngle < 0 ) {
          sweepAngle += 2 * Math.PI;
        } else if ( !anticlockwise && sweepAngle > 0 ) {
          sweepAngle -= 2 * Math.PI;
        }
      }

      var subdivAngle = sweepAngle / subdivisions;

      // Outer radius.
      var i;
      for ( i = 0; i < subdivisions + 1; i++ ) {
        vertices.push( outerRadius * Math.cos( startAngle + i * subdivAngle ) );
        vertices.push( outerRadius * Math.sin( startAngle + i * subdivAngle ) );

        indices.push(i);
      }

      // Inner radius.
      for ( i = 0; i < subdivisions + 1; i++ ) {
        vertices.push( innerRadius * Math.cos( endAngle - i * subdivAngle ) );
        vertices.push( innerRadius * Math.sin( endAngle - i * subdivAngle ) );

        // The plus one takes into account the loop variable in outerRadius loop.
        indices.push( i + subdivisions + 1 );
      }

      // Edge connecting inner and outer radius.
      indices.push(0);

      return {
        vertices: vertices,
        indices: indices
      };
    }
  };
}) ();
