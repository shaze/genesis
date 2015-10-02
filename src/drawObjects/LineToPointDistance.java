/**
 * Genesis -- program for creating structure and PCA plots of genotype data
 * Copyright (C) 2014. Robert W Buchmann, University of the Witwatersrand, Johannesburg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package drawObjects;

import org.eclipse.swt.graphics.Point;

/**
 * 	This class contains the static methods used to calculate the distance between
 *  a point p and a line segment with start v and end w			
 *
 *  @author R W Buchmann
 */
public class LineToPointDistance {
	/**
	 * Returns the square of the given parameter
	 * @param x x
	 * @return x squared
	 */
	private static int sqr(int x) { 
		return x * x; 
	}

	/**
	 * Returns the sum of the square distances between the x
	 * and y coordinates of the given points
	 * @param v the first point
	 * @param w the second point
	 * @return the sum of the square distances between the x
	 *				and y coordinates of the given points
	 */
	private static int dist2(Point v, Point w) { 
		return sqr(v.x - w.x) + sqr(v.y - w.y) ;
	}
	
	/**
	 * Gets the square distance between a point p and a line
	 * segment specified by its start v and end w.
	 * @param p the point
	 * @param v the beginning of the line sigment
	 * @param w the end of the line segment
	 * @return the square distance between a point p and a line
	 * 				segment
	 */
	private static int distToSegmentSquared(Point p, Point v, Point w) {
		int l2 = dist2(v, w);
		if (l2 == 0) {
			return dist2(p, v);
		}
		double t = (double)((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2;
		if (t < 0) {
			return dist2(p, v);
		}
		if (t > 1) {
			return dist2(p, w);
		}
		
		return dist2(p, new Point((int)Math.round(v.x + t * (w.x - v.x)), (int)Math.round(v.y + t * (w.y - v.y)) ) );
	}
	
	/**
	 * This method returns the shortest distance between a line and a point segment 
	 * rounded off to the nearest integer
	 * @param p the point
	 * @param v the start of the line segment
	 * @param w the end of the line segment
	 * 
	 * @return the distance between the line segment and point
	 */

	static int distToSegment(Point p, Point v, Point w) { 
		return (int) Math.round(Math.sqrt(distToSegmentSquared(p, v, w))); 
	}
}
