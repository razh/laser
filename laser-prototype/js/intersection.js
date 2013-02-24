var Intersection = (function() {
  return {
    /**
     * Calculates the intersection of the ray given by r + td, where t >= 0,
     * and the line segment given by x + sy, where 0 <= s <= 1.
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

        and det( A ) = dx * ( j - y ) - dy * ( i - x )

        Since we are only solving for t, we only need to calculate the top half
        of the matrix inverse.
      */

      // Compute determinant.
      var det = dx * ( y1 - y0 ) - dy * ( x1 - x0 );

      // Parameter.
      var t;
      // If determinant is 0, ray and line segment are parallel.
      if ( Math.abs( det ) < _game.EPSILON ) {
        // Parameters of line segment points.
        var t0, t1;
        // Check if the start and end point of the line segment lie on the ray.
        if ( Math.abs( dx ) > _game.EPSILON ) {
          t0 = ( x0 - rx ) / dx;
          t1 = ( x1 - rx ) / dx;
        } else if ( Math.abs( dy ) > _game.EPSILON ) {
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
      } else {
        var detInverse = 1 / det;

        // Top half of inverse matrix.
        var a = detInverse *  ( y1 - y0 ),
            b = detInverse * -( x1 - x0 );

        // Matrix multiplication.
        t = a * ( x1 - rx ) + b * ( y1 - ry );
      }

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
      if ( d === 0 ) {
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
    }
  };
}) ();
