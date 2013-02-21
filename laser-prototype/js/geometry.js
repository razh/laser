var Geometry = (function() {
  return {
    createRectangle: function() {

    },

    createRing: function( options ) {
      var outerRadius   = options.outerRadius   || 1.0,
          innerRadius   = options.innerRadius   || 0.5,
          startAngle    = options.startAngle    || 0,
          endAngle      = options.endAngle      || Math.PI * 2,
          subdivisions  = options.subdivisions  || 8,
          anticlockwise = options.anticlockwise || true;

      var vertices = [];
      var edges = [];

      var angle = anticlockwise ? endAngle - startAngle
                                : startAngle - endAngle;


      var subdivAngle = angle / subdivisions;

      // Outer radius.
      var i;
      for ( i = 0; i < subdivisions; i++ ) {
        vertices.push( outerRadius * Math.sin( startAngle + i * subdivAngle ) );
        vertices.push( outerRadius * Math.cos( startAngle + i * subdivAngle ) );

        edges.push(i);
      }

      // Inner radius.
      for ( i = subdivisions - 1; i >= 0; i-- ) {
        vertices.push();
        vertices.push();
      }

      return {
        vertices: vertices,
        edges: edges
      };
    }
  };
}) ();
