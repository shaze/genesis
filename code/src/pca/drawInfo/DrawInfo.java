/************************************************************************
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
import org.eclipse.swt.graphics.Rectangle;

import pca.Subject;
import pca.graphElements.AxisLine;

/**
 * This class contains information about a 2D PCA graph. 
 * The information is necessary (although not sufficient --
 * other information is contained in the PCA graph object)
 * to draw the 2D PCA graph but not to store or describe it.
 * 
 * The fields of this class are filled by the pca graph methods
 * 
 * @author R W Buchmann
 */
public class DrawInfo {
	//fields		
	/**
	 * The location (on the canvas) of the origin of the graph
	 */
	private Point origin;
	/**
	 * The array of subjects that will be drawn on the graph 
	 */
	private Subject[] points;	
	/**
	 * The area where the graph will be drawn. This is not 
	 * including the heading, key etc. but is essentially the
	 * rectangle around which the border of the graph is drawn.
	 */
	private Rectangle drawArea;	
	/**
     * The lines along the x-axis for the scale
	 */
	private AxisLine[] xLines;
	/**
	 * The lines along the y-axis for the scale
	 */
	private AxisLine[] yLines;		
	/**
	 * The width of the key
	 */
	private int keyWidth;
	/**
	 * This variable is used when a key is drawn on the bottom of the 
	 * graph and it represents the x-coordinates (on the canvas) of the
	 * elements in the key.
	 * 
	 * In the form [[x11,x12],[x21,x22],...] where xi1 is the x-coordinate
	 * of the beginning of the ith element of the key (the left side of the
	 * icon) and xi2 is the x-coordinate of the right end of the ith element 
	 * (the end of the text)
	 * 
	 * Used by the mouselistener to detect which element of the key is 
	 * clicked.
	 * 
	 */
	private int[][] keyValues;
	
	/**
	 * The previous choice of x axis. 
	 * Used to check if the axes have been changed
	 */
	public int oldx=-1;
	/**
	 * The previous choice of y axis. 
	 * Used to check if the axes have been changed
	 */
	public int oldy=-1;
	
	
	//methods
	
	/**
	 * Gets the location (on the canvas) of the origin of the graph
	 * @return the location of the origin of the graph
	 */
	public Point getOrigin() {
		return origin;
	}
	/**
	 * Sets the location (on the canvas) of the origin of the graph
	 * @param origin the location of the origin of the graph
	 */
	public void setOrigin(Point origin) {
		this.origin = origin;
	}
	/**
	 * Gets the array of subjects that will be drawn on the graph 
	 * @return the array of subjects
	 */
	public Subject[] getPoints() {
		return points;
	}
	/**
	 * Sets the array of subjects that will be drawn on the graph 
	 * @param points the array of subjects
	 */
	public void setPoints(Subject[] points) {
		this.points = points;
	}
	/**
	 * Gets the area where the graph will be drawn. This is not 
	 * including the heading, key etc. but is essentially the
	 * rectangle around which the border of the graph is drawn.
	 * @return the area where the graph will be drawn
	 */
	public Rectangle getDrawArea() {
		return drawArea;
	}
	/**
	 * Sets the area where the graph will be drawn. This is not 
	 * including the heading, key etc. but is essentially the
	 * rectangle around which the border of the graph is drawn.	
	 * @param drawArea the area where the graph will be drawn
	 */
	public void setDrawArea(Rectangle drawArea) {
		this.drawArea = drawArea;
	}
	/**
	 * Gets the lines along the x-axis for the scale
	 * @return the lines along the x-axis for the scale
	 */
	public AxisLine[] getxLines() {
		return xLines;
	}
	/**
	 * Sets the lines along the x-axis for the scale
	 * @param xLines the lines along the x-axis for the scale
	 */
	public void setxLines(AxisLine[] xLines) {
		this.xLines = xLines;
	}
	/**
	 * Gets the lines along the y-Axis for the scale
	 * @return the lines along the y-Axis for the scale
	 */
	public AxisLine[] getyLines() {
		return yLines;
	}
	/**
	 * Sets the lines along the y-Axis for the scale
	 * @param yLines the lines along the y-Axis for the scale
	 */
	public void setyLines(AxisLine[] yLines) {
		this.yLines = yLines;
	}
	/**
	 * Gets the width of the key
	 * @return the width of the key
	 */
	public int getKeyWidth() {
		return keyWidth;
	}
	/**
	 * Sets the width of the key
	 * @param keyWidth the width of the key
	 */
	public void setKeyWidth(int keyWidth) {
		this.keyWidth = keyWidth;
	}
	/**
	 * Gets the x-coordinates (on the canvas) of the elements in the key.
	 * 
	 * In the form [[x11,x12],[x21,x22],...] where xi1 is the x-coordinate
	 * of the beginning of the ith element of the key (the left side of the
	 * icon) and xi2 is the x-coordinate of the right end of the ith element 
	 * (the end of the text).
	 * 
	 * @return the x-coordinates (on the canvas) of the elements in the key.
	 */
	public int[][] getKeyValues() {
		return keyValues;
	}
	/**
	 * Sets the x-coordinates (on the canvas) of the elements in the key.
	 * 
	 * In the form [[x11,x12],[x21,x22],...] where xi1 is the x-coordinate
	 * of the beginning of the ith element of the key (the left side of the
	 * icon) and xi2 is the x-coordinate of the right end of the ith element 
	 * (the end of the text).
	 * 
	 * @param keyValues the x-coordinates (on the canvas) of the elements in 
	 * 				    the key.
	 */
	public void setKeyValues(int[][] keyValues) {
		this.keyValues = keyValues;
	}

}



