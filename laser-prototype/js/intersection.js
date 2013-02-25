var Intersection = (function() {
  var EPSILON = 1e-5;
  return {
    /**
     * Calculates the nearest intersection point of the ray given by r + td,
     * where t >= 0, and the line segment given by x + sy, where 0 <= s <= 1.
     * @param  {number} rx x-coordinate of ray origin.
     * @param  {number} ry y-coordinate of ray origin.
     * @param  {number} dx x-direction of ray.
     * @param  {number} dy y-direction of ray.
     * @param  {number} x0 x-coordinate of first point in line segment.
     * @param  {number} y0 y-coordinate of first point in line segment.
     * @param  {number} x1 x-coordinate of second point in line segment.
     * @param  {number} y1 y-coordinate of second point in line segment.
     * @return {x: number, y: number} Coordinate of intersection, or null if no intersection.
     */
    raySegment: function( rx, ry, dx, dy,
                          x0, y0, x1, y1 ) {
      /*
        Given the parametric equation of a ray:

          x(t) = rx + t * dx;
          y(t) = ry + t * dy;

        Where t >= 0. r denotes the ray origin, t is the parameter, and d is
        the direction of the ray.

        And the parametric equation of a line segment given by ( x, y ) and
        ( i, j ):

          x(s) = sx + ( 1 - s )i;
          y(s) = sy + ( 1 - s )j;

        Where the parameter 0 <= s <= 1.

        Set them equal to each other:

        x(t) = rx + t * dx = sx + ( 1 - s )i

        Which can be written as:

        rx + t * dx       = sx + i - si
        t * dx - sx + si  = i - rx
        t * dx + (i - x)s = i - rx

        Such that, with a similar calculation for the y-component, we get the
        following system of equations:

        dx * t + ( i - x ) * s = i - rx
        dy * t + ( j - y ) * s = j - ry

        In matrix form (Ax = b), this is:

        [  dx   ( i - x )  ] [  t  ]     [  i - rx  ]
        [  dy   ( j - y )  ] [  s  ]  =  [  j - ry  ]

        To solve for t, we multiply the right-hand side by the matrix inverse,
        where:

        A^-1 =      1         [  ( j - y )   -( i - x )  ]
               ----------  *  [    -dy           dx      ]
                det( A )

        and det( A ) = dx * ( j - y ) - dy * ( i - x ).
      */

      // Compute determinant.
      var det = dx * ( y1 - y0 ) - dy * ( x1 - x0 );

      // Parameter.
      var t;
      // If determinant is 0, ray and line segment are parallel.
      if ( Math.abs( det ) < EPSILON ) {
        // Parameters of line segment points.
        var t0, t1;
        // Check if the start and end point of the line segment lie on the ray.
        if ( Math.abs( dx ) > EPSILON ) {
          t0 = ( x0 - rx ) / dx;
          t1 = ( x1 - rx ) / dx;
        } else if ( Math.abs( dy ) > EPSILON ) {
          t0 = ( y0 - ry ) / dy;
          t1 = ( y1 - ry ) / dy;
        } else {
          return null;
        }

        // Get lowest non-negative parameter.
        if ( t0 >= 0 && t1 >= 0 ) {
          t = Math.min( t0, t1 );
        } else if ( t0 >= 0 ) {
          t = t0;
        } else {
          t = t1;
        }
      }
      // Otherwise use the method described above.
      else {
        var detInverse = 1 / det;

        // Matrix inverse.
        var a = detInverse *  ( y1 - y0 ),
            b = detInverse * -( x1 - x0 ),
            c = detInverse * -dy,
            d = detInverse *  dx;

        // Calculate s first to check if intersection is on line segment.
        s = c * ( x1 - rx ) + d * ( y1 - ry );
        // Intersection point is not on segment.
        if ( 0 > s || s > 1 ) {
          return null;
        }

        t = a * ( x1 - rx ) + b * ( y1 - ry );
      }

      // Intersection is on segment, but not the ray.
      if ( t < 0 ) {
        return null;
      }

      return {
        x: rx + t * dx,
        y: ry + t * dy
      };
    },

    /**
     * Calculates the nearest intersection point of the ray given by r + td,
     * where t >= 0, and the axis-aligned bounding-box given by
     * [ ( x0, y0 ), ( x1, y1 ) ].
     * @param  {number} rx x-coordinate of ray origin.
     * @param  {number} ry y-coordinate of ray origin.
     * @param  {number} dx x-direction of ray.
     * @param  {number} dy y-direction of ray.
     * @param  {[type]} x0 x-coordinate of first point in AABB.
     * @param  {[type]} y0 y-coordinate of first point in AABB.
     * @param  {[type]} x1 x-coordinate of second point in AABB.
     * @param  {[type]} y1 y-coordinate of second point in AABB.
     * @return {x: number, y: number} Coordinate of intersection, or null if no intersection.
     */
    rayAABB: function( rx, ry, dx, dy, x0, y0, x1, y1 ) {
      // Project the ray on to each line segment (assuming ( x0, y0 ) is min,
      // although it doesn't matter).
      var points = [];
      // Left.
      points.push( Intersection.raySegment( rx, ry, dx, dy, x0, y0, x0, y1 ) );
      // Right.
      points.push( Intersection.raySegment( rx, ry, dx, dy, x1, y0, x1, y1 ) );
      // Top.
      points.push( Intersection.raySegment( rx, ry, dx, dy, x0, y1, x1, y1 ) );
      // Bottom.
      points.push( Intersection.raySegment( rx, ry, dx, dy, x0, y0, x1, y0 ) );

      var point;
      // Determine minimum postive parameter of intersection points.
      var min = -1;
      // Default value (if all four intersections are null, return null).
      var t;
      for ( var i = 0, n = points.length; i < n; i++ ) {
        if ( points[i] === null ) {
          continue;
        }

        point = points[i];
        // Find the parameter where the intersection lies.
        if ( Math.abs( dx ) > EPSILON ) {
          t = ( point.x - rx ) / dx;
        } else if ( Math.abs( dy ) > EPSILON ) {
          t = ( point.y - ry ) / dy;
        } else {
          continue;
        }

        // If min is negative, the value has not been initialized.
        // We want t's >= 0 and less than min.
        if ( min < 0 || ( t >= 0 && t < min ) ) {
          min = t;
        }
      }

      t = min;
      if ( t < 0 ) {
        return null;
      }

      return {
        x: rx + t * dx,
        y: ry + t * dy
      };
    },

    /**
     * Calculates the nearest intersection point of the ray given by r + td,
     * where t >= 0, and the circle given by
     * ( x - cx ) ^ 2 + ( y - cy ) ^ 2 <= r.
     * @param  {number} rx x-coordinate of ray origin.
     * @param  {number} ry y-coordinate of ray origin.
     * @param  {number} dx x-direction of ray.
     * @param  {number} dy y-direction of ray.
     * @param  {number} cx x-coordinate of circle center.
     * @param  {number} cy y-coordinate of circle center.
     * @param  {number} r  radius of circle.
     * @return {x: number, y: number} Coordinate of intersection, or null if no intersection.
     */
    rayCircle: function( rx, ry, dx, dy, cx, cy, r ) {
      /* Given the parametric equation of a ray:

          x(t) = rx + t * dx
          y(t) = ry + t * dy

        Plug these into the equation of a circle: x(t) ^ 2 + y(t) ^ 2 = r ^ 2
        (first, translate coordinates to the circle's center) to get:

          ( rx + t * dx )^2 + ( ry + t * dy )^2 - r^2 = 0

        which expands to:

          ( rx^2 + 2rxdx * t + t^2 * dx^2 ) +
          ( ry^2 + 2rydy * t + t^2 * dy^2 ) - r^2 = 0

        This can be written as:

        ( dx^2 + dy^2 )         * t^2 +
        ( 2 * ( rxdx + rydy ) ) * t     +
        ( rx^2 + ry^2 - r^2 )

        such that the coefficients of the quadratic equation are:

          a = dx^2 + dy^2
          b = 2 * ( rxdx + rydy )
          c = rx^2 + ry^2 - r^2

        The discriminant is thus: b^2 - 4ac.

        If the discriminant = 0, there is one intersection point.
        If the discriminant > 0, there are two intersection points.
        If the discriminant < 0, there are no intersection points.

        These intersection points lie on the ray if t >= 0.
      */

      // Transform ray to circle space.
      rx -= cx;
      ry -= cy;

      // Compute coefficients.
      var a = ( dx * dx ) + ( dy * dy );
      var b = 2 * ( rx * dx + ry * dy );
      var c = ( rx * rx ) + ( ry * ry ) - ( r * r );

      // Compute discriminant.
      var d = b * b - 4 * a * c;

      if ( d < 0 ) {
        return null;
      }

      var t;
      // Near zero or zero discriminant.
      if ( Math.abs( d ) < EPSILON ) {
        t = -b / ( 2 * a );
      } else {
        // The lowest, non-negative parameter gives us the intersection point
        // relative to the ray.
        var t0 = ( -b - Math.sqrt(d) ) / ( 2 * a ),
            t1 = ( -b + Math.sqrt(d) ) / ( 2 * a );

        // Both parameters are >= 0, so use the lower one.
        if ( t0 >= 0 && t1 >= 0 ) {
          t = Math.min( t0, t1 );
        } else if ( t0 >= 0 ) {
          // Only need to check if t0 is positive (t1 must be negative).
          t = t0;
        } else {
          // Handles case were t1 >= 0 and t0 < 0
          // as well as the case where t0 and t1 both < 0.
          t = t1;
        }
      }

      // The circle intersects the line, but not the ray.
      if ( t < 0 ) {
        return null;
      }

      // Transform ray back to world space.
      return {
        x: cx + rx + t * dx,
        y: cy + ry + t * dy
      };
    },

    /**
     * Calculates the nearest intersection point of the ray given by r + td,
     * where t >= 0, and a geometry object: a collection of vertices and indices.
     * @param  {number} rx x-coordinate of ray origin.
     * @param  {number} ry y-coordinate of ray origin.
     * @param  {number} dx x-direction of ray.
     * @param  {number} dy y-direction of ray.
     * @param  {{vertices: [], indices: []}} geometry
     * @return {{intersection: {x: number, y: number},  Coordinate of intersection
     *           normal:       {x: number, y: number}}} and geometry normal, or
     *                                                  null if no intersecton.
     *
     */
    rayGeometry: function( rx, ry, dx, dy, geometry ) {
      var vertices = geometry.vertices || [];
      var indices = geometry.indices || [];

      var point;
      // Index of edge where the nearest intersection lies.
      var edgeIndex = null;
      // Parameters.
      var min = -1;
      var t;
      var x0, y0, x1, y1;
      for ( var i = 0, n = indices.length - 1; i < n; i++ ) {
        x0 = vertices[ 2 * indices[i] ];
        y0 = vertices[ 2 * indices[i] + 1 ];
        x1 = vertices[ 2 * indices[ i + 1 ] ];
        y1 = vertices[ 2 * indices[ i + 1 ] + 1 ];

        point = Intersection.raySegment( rx, ry, dx, dy,
                                         x0, y0, x1, y1 );

        if ( point === null ) {
          continue;
        }

        // Calculate parameter.
        if ( Math.abs( dx ) > EPSILON ) {
          t = ( point.x - rx ) / dx;
        } else if ( Math.abs( dy ) > EPSILON ) {
          t = ( point.y - ry ) / dy;
        } else {
          continue;
        }

        // If min is not yet defined, or if t < min.
        if ( min < 0 || ( t >= 0 && t < min ) ) {
          min = t;
          edgeIndex = i;
        }
      }

      t = min;
      if ( t < 0 || edgeIndex === null ) {
        return null;
      }
      console.log( 't, idx: ' + t + ', ' + edgeIndex );
      // Calculate normal of edge (in the 'right' direction).
      x0 = vertices[ 2 * indices[ edgeIndex ] ];
      y0 = vertices[ 2 * indices[ edgeIndex ] + 1 ];
      x1 = vertices[ 2 * indices[ edgeIndex + 1 ] ];
      y1 = vertices[ 2 * indices[ edgeIndex + 1 ] + 1 ];
      console.log( ( 2 * indices[ edgeIndex ] ) + ', ' +
                   ( 2 * indices[ edgeIndex ] + 1 ) + ', ' +
                   ( 2 * indices[ edgeIndex + 1 ] ) + ', ' +
                   ( 2 * indices[ edgeIndex + 1 ] + 1 ) );

      var sx = x1 - x0,
          sy = y1 - y0;

      // Normalize normal.
      var length = Math.sqrt( sx * sx + sy * sy );
      if ( Math.abs( length ) < EPSILON ) {
        return null;
      }

      length = 1 / length;

      return {
        intersection: {
          x: rx + t * dx,
          y: ry + t * dy
        },
        normal: {
          x:  sy * length,
          y: -sx * length
        }
      };
    }
  };
}) ();
