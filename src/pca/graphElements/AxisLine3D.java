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
 */
package pca.graphElements;

import org.eclipse.swt.graphics.*;

/**
 * This object contains the location and value of a line representing
 * a point along the scale on a 3D pca graph. The marker represents the
 * actual point along the scale and firstLineEnd and secondLineEnd represent 
 * the end points of the lines intersecting the marker and forming the grid. All 
 * these points are coordinates on the panel/image to be drawn upon.
 * 
 * @author R W Buchmann
 */
public class AxisLine3D {
	/**
	 * The location of the point where the axis intesects
	 * the axis line. The marker represents the
	 * actual point along the scale
	 */
	private Point marker;
	/**
	 * The location of the end of the first line coming from the marker
	 */
	private Point firstLineEnd;
	/**
	 * The location of the end of the second line coming from the marker
	 */
	private Point secondLineEnd;	
	/**
	 * How much the marker represents
	 */
	private float value;
	
	/**
	 * Gets the location of the marker (
	 * @return the location of the marker
	 */
	public Point getMarker() {
		return marker;
	}
	/**
	 * Sets the location of the marker
	 * @param marker the location of the marker
	 */
	public void setMarker(Point marker) {
		this.marker = marker;
	}
	/**
	 * Gets the value of the point along the scale reprented
	 * by this axisline
	 * @return the value
	 */
	public float getValue() {
		return value;
	}
	/**
	 * Set the value of the point along the scale reprented
	 * by this axisline
	 * @param value the value
	 */
	public void setValue(float value) {
		this.value = value;
	}
	/**
	 * Gets the end of the second line
	 * @return the second line end
	 */
	public Point getSecondLineEnd() {
		return secondLineEnd;
	}
	/**
	 * Sets the end of the second line
	 * @param secondLineEnd the second line end
	 */
	public void setSecondLineEnd(Point secondLineEnd) {
		this.secondLineEnd = secondLineEnd;
	}
	/**
	 * Gets the end of the first line
	 * @return the first line end
	 */
	public Point getFirstLineEnd() {
		return firstLineEnd;
	}
	/**
	 * Sets the end of the first line
	 * @param firstLineEnd the first line end
	 */
	public void setFirstLineEnd(Point firstLineEnd) {
		this.firstLineEnd = firstLineEnd;
	}
}
	