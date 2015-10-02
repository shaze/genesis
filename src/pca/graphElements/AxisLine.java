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

package pca.graphElements;

/**
 * This object contains the location and value of a line of the scale
 * on the 2D graph. The marker gives the coordinate (x or y depending
 * on the relevant axis) along the panel/image the graph is being 
 * drawn upon.
 * 
 * @author R W Buchmann
 */
public class AxisLine {
	/**
	 * How far along the axis the line is
	 */
	private int marker;
	/**
	 * The value the line represents
	 */
	private float value;

	/**
	 * Gets the marker variable.
	 * The marker gives the coordinate (x or y depending on the 
	 * relevant axis) along the canvas that the graph is being 
	 * drawn upon.
	 * @return the marker
	 */
	public int getMarker() { 
		return marker;
	}
	/**
	 * Sets the marker variable.
	 * The marker gives the coordinate (x or y depending on the 
	 * relevant axis) along the canvas the graph is being drawn upon.
	 * 
	 * @param marker the marker
	 */
	public void setMarker(int marker) {
		this.marker = marker;
	}
	/**
	 * Gets the value represented by the marker
	 * @return the value
	 */
	public float getValue() {
		return value;
	}
	/**
	 * Sets the value represented by the marker
	 * @param value the value
	 */
	public void setValue(float value) {
		this.value = value;
	}


}
