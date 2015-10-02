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
 */

package admix.drawinfo;

import org.eclipse.swt.graphics.Rectangle;

import admix.ASubject;
/**
 * This class contains information a single admixture graph.
 * A project with multiple admixture graphs will require as many
 * of these objects.
 * The information is necessary (although not sufficient --
 * other information is contained in the PCA graph object)
 * to draw the admixture graph but not to store or describe it.
 * 
 * The fields of this class are filled by the pca graph methods
 * 
 * @author R W Buchmann
 */
public class DrawInfo {	
	/**
	 * The height of a single subject (y length of a subject on a horizontal 
	 * graph or x length on a vertical)
	 */
	private int subjectHeight;
	/**
	 * The width of a single subject (x length of a subject on a horizontal 
	 * graph or y length on a vertical)
	 */
	private int subjectWidth;
	/**
	 * The number of ancestors (population groups) on the graph 
	 */
	private int noAncestors;	
	/**
	 * The area where the graph will be drawn. This is not 
	 * including the heading, group labels etc. but is the space
	 * where one graph can be drawn.
	 */
	private Rectangle drawArea;	
	/**
	 * The array of ASubject. These contain the data of how a subject will
	 * be drawn on the graph as well as a pointer to the relevant subject.
	 */
	private ASubject[] subjects;
	/**
	 * Specifies the positions of the population groups.
	 * Each Rectangle corresponds to the area of one population group 
	 * given as : {xstart, ystart, xlength, ylength} 
	 */
	private Rectangle[] popGroupMarkers;		
	/**
	 * Gets the height of a single subject (y length of a subject on a horizontal 
	 * graph or x length on a vertical)
	 * @return the height of a single subject 
	 */
	public int getSubjectHeight() {
		return subjectHeight;
	}
	/**
	 * Sets the height of a single subject (y length of a subject on a horizontal 
	 * graph or x length on a vertical)
	 * @param subjectHeight the height of a single subject 
	 */
	public void setSubjectHeight(int subjectHeight) {
		this.subjectHeight = subjectHeight;
	}
	/**
	 * Gets the width of a single subject (x length of a subject on a horizontal 
	 * graph or y length on a vertical)
	 * @return the width of a single subject
	 */
	public int getSubjectWidth() {
		return subjectWidth;
	}
	/**
	 * Sets the width of a single subject (x length of a subject on a horizontal 
	 * graph or y length on a vertical)
	 * @param subjectWidth the width of a single subject
	 */
	public void setSubjectWidth(int subjectWidth) {
		this.subjectWidth = subjectWidth;
	}
	/**
	 * Gets the area where the graph will be drawn. This is not 
	 * including the heading, group labels etc. but is the space
	 * where one graph can be drawn.
	 * @return the area where the graph will be drawn
	 */
	public Rectangle getDrawArea() {
		return drawArea;
	}
	/**
	 * Sets the area where the graph will be drawn. This is not 
	 * including the heading, group labels etc. but is the space
	 * where one graph can be drawn.
	 * @param drawArea the area where the graph will be drawn
	 */
	public void setDrawArea(Rectangle drawArea) {
		this.drawArea = drawArea;
	}

	/**
	 * Gets the array of ASubject. These contain the data of how a subject will
	 * be drawn on the graph as well as a pointer to the relevant subject.
	 * @return the array of ASubject
	 */
	public ASubject[] getSubjects() {
		return subjects;
	}
	/**
	 * Sets the array of ASubject. These contain the data of how a subject will
	 * be drawn on the graph as well as a pointer to the relevant subject.
	 * @param subjects the array of ASubject
	 */
	public void setSubjects(ASubject[] subjects) {
		this.subjects = subjects;
	}
	/**
	 * Gets the positions of the population groups.
	 * Each Rectangle corresponds to the area of one population group 
	 * given as : {xstart, ystart, xlength, ylength}.
	 * @return the positions of the population group markers
	 */
	public Rectangle[] getPopGroupMarkers() {
		return popGroupMarkers;
	}
	/**
	 * Sets the positions of the population groups.
	 * Each Rectangle corresponds to the area of one population group 
	 * given as : {xstart, ystart, xlength, ylength}.
	 * @param popGroupMarkers the positions of the population group markers
	 */
	public void setPopGroupMarkers(Rectangle[] popGroupMarkers) {
		this.popGroupMarkers = popGroupMarkers;
	}
	/**
	 * Gets the number of ancestors in the graph
	 * @return the number of ancestors in the graph
	 */
	public int getNoAncestors() {
		return noAncestors;
	}
	/**
	 * Sets the number of ancestors in the graph
	 * @param noAncestors the number of ancestors in the graph
	 */
	public void setNoAncestors(int noAncestors) {
		this.noAncestors = noAncestors;
	}
	/**
	 * Sets the height of the graph
	 * @param height the height of the graph
	 */
	public void setHeight(int height){
		drawArea.height=height;
	}
	/**
	 * Sets the width of the graph
	 * @param width the width of the graph
	 */
	public void setWidth(int width){
		drawArea.width=width;
	}
	
	
		
}
