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

      var edges = [ 0, 1, 2, 3, 0 ];

      return {
        vertices: vertices,
        edges: edges
      };
    },

    createRing: function( options ) {
      options = options || {};

      var outerRadius   = options.outerRadius   || 1.0,
          innerRadius   = options.innerRadius   || 0.5,
          startAngle    = options.startAngle    || 0,
          endAngle      = options.endAngle      || Math.PI * 2,
          subdivisions  = options.subdivisions  || 32,
          anticlockwise = options.anticlockwise || true;

      var vertices = [];
      var edges = [];

      var angle = anticlockwise ? endAngle - startAngle
                                : startAngle - endAngle;


      var subdivAngle = angle / (subdivisions - 1 );

      // Outer radius.
      var i;
      for ( i = 0; i < subdivisions; i++ ) {
        vertices.push( outerRadius * Math.sin( startAngle + i * subdivAngle ) );
        vertices.push( outerRadius * Math.cos( startAngle + i * subdivAngle ) );

        edges.push(i);
      }

      // Edge conncting inner and outer radius.
      edges.push( subdivisions );

      // Inner radius.
      for ( i = 0; i < subdivisions ; i++ ) {
        vertices.push( innerRadius * Math.sin( endAngle - i * subdivAngle ) );
        vertices.push( innerRadius * Math.cos( endAngle - i * subdivAngle ) );

        // The plus one takes into account the edge connecting inner and outer radii.
        edges.push( i + subdivisions + 1 );
      }

      // Edge connecting inner and outer radius.
      edges.push(0);

      return {
        vertices: vertices,
        edges: edges
      };
    }
  };
}) ();
