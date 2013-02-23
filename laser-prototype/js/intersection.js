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
     * Calculates the intersection of the ray given by r + td, where t >= 0,
     * and the circle given by ( x - cx ) ^ 2 + ( y - cy ) ^ 2 <= r.
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

        Plug these into the equation of a circle: x(t)^2 + y(t)^2 = r^2 (first,
        translate coordinates to the center) to get:

          ( rx + t * dx ) ^ 2 + ( ry + + t * dy ) ^ 2 - r ^ 2 = 0

        which expands to:

          ( rx ^ 2 + 2 * rx * t * dx + t ^ 2 * dx ^ 2)
          [x^2 + i^2 - 2ix + (y - j)^2]t^2 + [2(ix + yj - i^2 - j^2)]t + i^2 + j^2 - r^2

        such that the coefficients of the quadratic equation are:

          a = x^2 + i^2 - 2ix + (y - j)^2
          b = 2(xi + yj - i^2 - j^2)
          c = i^2 + j^2 - r^2

        The discriminant is thus: b^2 - 4ac.

        If the discriminant = 0, there is one intersection point.
        If the discriminant > 0, there are two intersection points.
        If the discriminant < 0, there are no intersection points.

        These intersection points lie on the line segment if 0 <= t <= 1.
      */

      // Transform ray to circle space.

    }
  };
}) ();
