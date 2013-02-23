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
        Given the parametric formula of a ray:

          r + td

        Where t >= 0. r denotes the ray origin, t is the parameter, and d is
        the direction of the ray.

        And the parametric formula of a line segment:

          x + sy

        Where 0 <= s <= 1. x and y represent the first and second points
        of the line segment respectively. s is the parameter.
      */

      // Check if lines are parallel.
      if ( Math.abs( ( dy * ( x1 - x0 ) ) -
                     ( dx * ( y1 - y0 ) ) ) < _game.EPSILON ) {
        // Check if y-intercepts are equal.
        if ( true ) {

        }
      }

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
      /* Given the parametric formula of a ray:

          x(t) = rx + t * dx
          y(t) = ry + t * dy

        Plug these into the equation of a circle: x(t) ^ 2 + y(t) ^ 2 = r ^ 2
        (first, translate coordinates to the circle's center) to get:

          ( rx + t * dx ) ^ 2 + ( ry + t * dy ) ^ 2 - r ^ 2 = 0

        which expands to:

          ( rx ^ 2  +  2rxdx * t  +  t ^ 2 * dx ^ 2 ) +
          ( ry ^ 2  +  2rydy * t  +  t ^ 2 * dy ^ 2 ) - r ^ 2 = 0

        This can be written as:

        ( dx ^ 2 + dy ^ 2 )         * t ^ 2 +
        ( 2 * ( rxdx + rydy ) )     * t     +
        ( rx ^ 2 + ry ^ 2 - r ^ 2 )

        such that the coefficients of the quadratic equation are:

          a = dx ^ 2 + dy ^ 2
          b = 2 * ( rxdx + rydy )
          c = rx ^ 2 + ry ^ 2 - r ^ 2

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
          // as well as the case where t0 and t1 < 0.
          t = t1;
        }
      }

      // Intersects the line, but not the ray.
      if ( t < 0 ) {
        return null;
      }

      // Transform ray back to
      return {
        x: cx + rx + t * dx,
        y: cy + ry + t * dy
      };
    }
  };
}) ();
