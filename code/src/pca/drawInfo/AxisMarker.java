/*************************************************************************
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
 */
package pca.drawInfo;

import org.eclipse.swt.graphics.Point;
/**
 * This object stores a point which marks where the 'unit vector' of an axis
 * of the 3D graph lies (on the canvas) and the value of that point. Points 
 * are plotted by comparing their value for each axis the the value of the 
 * AxisMarker for the relevant axis 
 * 
 * These 'vectors' are not of length 1 in any sense - generally they are the 
 * longer than the length of the highest value for any co-ordinate along the 
 * relevant axis and are used and often would be located off the screen. They
 * are used to plot where each point will lie by measuring against them and 
 * using them as a scale for their axis.
 *
 * @author R W Buchmann
 */
public class AxisMarker {
	/**
	 * The point (coordinate) where the end of the 'unit vector' lies
	 * (on the canvas)
	 */
	private Point point;
	/**
	 * The value (along the relevant axis) that the point represents
	 */
	private float value;
	/**
	 * The constructor sets the point that marks where the end of 
	 * the 'unit vector' lies and the value along the axis where 
	 * the marker lies
	 * @param point the point that marks where the end of the 'unit 
	 * 				vector' lies (on the canvas)
	 * @param value the value along the axis where the marker lies
	 */
	public AxisMarker(Point point, float value){
		this.point = point;
		this.value = value;
	}
	/**
	 * Get the point that marks where the end of the 'unit vector' lies
	 * (on the canvas)
	 * @return the point
	 */
	public Point getPoint() {
		return point;
	}
	/**
	 * Set the point that marks where the end of the 'unit vector' lies
	 * (on the canvas)
	 * @return the point
	 */
	public void setPoint(Point point) {
		this.point=point;	
	}
	/**
	 * Get the value along the axis where the marker lies
	 * @return the value
	 */
	public float getValue() {
		return value;
	}


}


