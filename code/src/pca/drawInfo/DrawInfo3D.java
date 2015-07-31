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
import org.eclipse.swt.graphics.Rectangle;

import pca.Subject;
import pca.graphElements.AxisLine3D;
/**
 * This class contains information about a 3D PCA graph. 
 * The information is necessary (although not sufficient --
 * other information is contained in the PCA graph object)
 * to draw the 3D PCA graph but not to store or describe it.
 * 
 * The fields of this class are filled by the pca graph methods
 * 
 * @author R W Buchmann
 */
public class DrawInfo3D {
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
	 * The x axismarker can be thought of in a similar sense to a unit vector.
	 * it represents a vector that goes along the x axis that is measured
	 * against to plot points onto the graph
	 */
	private AxisMarker xMarker;
	/**
	 * The y axismarker can be thought of in a similar sense to a unit vector.
	 * it represents a vector that goes along the y axis that is measured
	 * against to plot points onto the graph
	 */
	private AxisMarker yMarker;
	/**
	 * The z axismarker can be thought of in a similar sense to a unit vector.
	 * it represents a vector that goes along the z axis that is measured
	 * against to plot points onto the graph
	 */
	private AxisMarker zMarker;	
	/**
	 * Stores the two positions (on the canvas) of where the 
	 * x-axis intercepts the border
	 */
	private Point[] xIntercepts=new Point[2];
	/**
	 * Stores the two positions (on the canvas) of where the 
	 * y-axis intercepts the border
	 */
	private Point[] yIntercepts=new Point[2];
	/**
	 * Stores the two positions (on the canvas) of where the 
	 * z-axis intercepts the border
	 */
	private Point[] zIntercepts=new Point[2];
	/**
	 * The lines along the x-axis for the scale
	 */
	private AxisLine3D[] xLines;
	/**
	 * The lines along the y-axis for the scale
	 */
	private AxisLine3D[] yLines;
	/**
	 * The lines along the z-axis for the scale
	 */
	private AxisLine3D[] zLines;	
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
	/**
	 * The previous choice of z axis. 
	 * Used to check if the axes have been changed
	 */
	public int oldz=-1;
	/**
	 * The positions of the labels for each axis. Index 0 holds
	 * the x-axis label position, 1 holds the y-axis and 2 holds
	 * the z-axis label position.
	 */
	private Rectangle[] axisLabels;
	
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
	 * Gets the x-axis marker. This can be thought of in a similar 
	 * sense to a unit vector. it represents a vector that goes along 
	 * the x axis that is measured against to plot points onto the graph
	 * @return the x-axis marker
	 */
	public AxisMarker getxMarker() {
		return xMarker;
	}
	/**
	 * Sets the x-axis marker. This can be thought of in a similar 
	 * sense to a unit vector. it represents a vector that goes along 
	 * the x-axis that is measured against to plot points onto the graph
	 * @param point the point that marks where the end of the 'unit 
	 * 				vector' lies (on the canvas)
	 * @param value the value along the axis where the marker lies
	 */
	public void setXMarker(int[] point,float value){
		xMarker=new AxisMarker(new Point(point[0],point[1]), value);
	}
	/**
	 * Gets the y-axis marker. This can be thought of in a similar 
	 * sense to a unit vector. it represents a vector that goes along 
	 * the y axis that is measured against to plot points onto the graph
	 * @return the y-axis marker
	 */
	public AxisMarker getyMarker() {
		return yMarker;
	}
	/**
	 * Sets the y-axis marker. This can be thought of in a similar 
	 * sense to a unit vector. it represents a vector that goes along 
	 * the y-axis that is measured against to plot points onto the graph
	 * @param point the point that marks where the end of the 'unit 
	 * 				vector' lies (on the canvas)
	 * @param value the value along the axis where the marker lies
	 */
	public void setYMarker(int[] point,float value){
		yMarker=new AxisMarker(new Point(point[0],point[1]), value);		
	}
	/**
	 * Gets the z-axis marker. This can be thought of in a similar 
	 * sense to a unit vector. it represents a vector that goes along 
	 * the y axis that is measured against to plot points onto the graph
	 * @return the z-axis marker
	 */
	public AxisMarker getzMarker() {
		return zMarker;
	}	
	/**
	 * Sets the z-axis marker. This can be thought of in a similar 
	 * sense to a unit vector. it represents a vector that goes along 
	 * the z-axis that is measured against to plot points onto the graph
	 * @param point the point that marks where the end of the 'unit 
	 * 				vector' lies (on the canvas)
	 * @param value the value along the axis where the marker lies
	 */
	public void setZMarker(int[] point,float value){
		zMarker=new AxisMarker(new Point(point[0],point[1]), value);
	}		
	/**
	 * Gets the two positions (on the canvas) of where the 
	 * x-axis intercepts the border
	 * @return the intercepts
	 */
	public Point[] getxIntercepts() {
		return xIntercepts;
	}
	/**
	 * Sets the two positions (on the canvas) of where the 
	 * x-axis intercepts the border
	 * @param xIntercepts the intercepts
	 */
	public void setxIntercepts(Point[] xIntercepts) {
		this.xIntercepts = xIntercepts;
	}	
	/**
	 * Gets the two positions (on the canvas) of where the 
	 * y-axis intercepts the border
	 * @return the intercepts
	 */
	public Point[] getyIntercepts() {
		return yIntercepts;
	}
	/**
	 * Sets the two positions (on the canvas) of where the 
	 * y-axis intercepts the border
	 * @param yIntercepts the intercepts
	 */
	public void setyIntercepts(Point[] yIntercepts) {
		this.yIntercepts = yIntercepts;
	}
	/**
	 * Gets the two positions (on the canvas) of where the 
	 * z-axis intercepts the border
	 * @return the intercepts
	 */
	public Point[] getzIntercepts() {
		return zIntercepts;
	}
	/**
	 * Sets the two positions (on the canvas) of where the 
	 * z-axis intercepts the border
	 * @param zIntercepts the intercepts
	 */
	public void setzIntercepts(Point[] zIntercepts) {
		this.zIntercepts = zIntercepts;
	}
	/**
	 * Gets the lines along the x-axis for the scale
	 * @return the lines along the x-axis for the scale
	 */
	public AxisLine3D[] getxLines() {
		return xLines;
	}
	/**
	 * Sets the lines along the x-axis for the scale
	 * @param xLines the lines along the x-axis for the scale
	 */
	public void setxLines(AxisLine3D[] xLines) {
		this.xLines = xLines;
	}
	/**
	 * Gets the lines along the y-Axis for the scale
	 * @return the lines along the y-Axis for the scale
	 */
	public AxisLine3D[] getyLines() {
		return yLines;
	}
	/**
	 * Sets the lines along the y-Axis for the scale
	 * @param yLines the lines along the y-Axis for the scale
	 */
	public void setyLines(AxisLine3D[] yLines) {
		this.yLines = yLines;
	}
	/**
	 * Gets the lines along the y-Axis for the scale
	 * @return the lines along the y-Axis for the scale
	 */
	public AxisLine3D[] getzLines() {
		return zLines;
	}
	/**
	 * Sets the lines along the z-Axis for the scale
	 * @param zLines the lines along the z-Axis for the scale
	 */
	public void setzLines(AxisLine3D[] zLines) {
		this.zLines = zLines;
	}
	/**
	 * Gets the positions of the labels for each axis. Index 0 holds
	 * the x-axis label position, 1 holds the y-axis and 2 holds
	 * the z-axis label position.
	 *
	 * @return the label positions
	 */
	public Rectangle[] getAxisLabels() {
		return axisLabels;
	}
	/**
	 * Sets the positions of the labels for each axis. Index 0 holds
	 * the x-axis label position, 1 holds the y-axis and 2 holds
	 * the z-axis label position.
	 *
	 * @param axisLabels the label positions
	 */
	public void setAxisLabels(Rectangle[] axisLabels) {
		this.axisLabels = axisLabels;
	}
	
}


