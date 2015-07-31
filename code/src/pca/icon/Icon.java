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
 * 
 */

package pca.icon;

import java.io.Serializable;

import org.eclipse.swt.graphics.RGB;

/**
 * An object of this class represents an icon to be drawn on 
 * a PCA plot
 * 
 * @author robertwbuchmann
 */
public class Icon implements Serializable{

	private static final long serialVersionUID = -8832611135959540646L;
	public Icon(int icon, int size, RGB col, boolean border){
		this.icon=icon;
		this.size=size;
		this.colour=col;
		this.border=border;
	}
	
	public Icon(int icon, int size, RGB col){
		this(icon, size, col, true);
	}
	
	public Icon(){
		this(-1, -1, null, true);
	}
	/**
	 * The integer representing which icon to draw
	 * 0=circle
	 * 1=square
	 * 2=triangle
	 * 3=diamond
	 * 4=cross
	 * 5=plus
	 */
	private int icon=-1;
	/**
	 * The size of the icon, should be >= 2
	 */
	private int size=-1;
	/**
	 * Whether a border is to be drawn around this icon
	 */
	private boolean border;
	/**
	 * The colour of the icon
	 */
	private RGB colour;
	/**
	 * Gets the integer representing which icon to draw
	 * 0=circle
	 * 1=square
	 * 2=triangle
	 * 3=diamond
	 * 4=cross
	 * 5=plus
	 *
	 * @return the integer representing the icon
	 */
	public int getIcon() {
		return icon;
	}
	/**
	 * Sets the integer representing which icon to draw
	 * 0=circle
	 * 1=square
	 * 2=triangle
	 * 3=diamond
	 * 4=cross
	 * 5=plus
	 * 
	 * @param icon the integer representing the icon
	 */
	public void setIcon(int icon) {
		this.icon = icon;
	}
	/**
	 * Gets the size of the icon, should be >= 2
	 * @return the size of the icon
	 */
	public int getSize() {
		return size;
	}
	/**
	 * Sets the size of the icon, should be >=2
	 * @param size the size of the icon
	 */
	public void setSize(int size) {
		this.size = size;
	}
	/**
	 * Gets whether a border is to be drawn around this icon
	 * @return whether a border is to be drawn around this icon
	 */
	public boolean getBorder() {
		return border;
	}
	/**
	 * Sets whether a border is to be drawn around this icon
	 * @param border whether a border is to be drawn around this icon
	 */
	public void setBorder(boolean border) {
		this.border = border;
	}
	/**
	 * Get the colour of the icon
	 * @return the colour of the icon
	 */
	public RGB getColour() {
		return colour;
	}
	/**
	 * Set the colour of the icon
	 * @param colour the colour of the icon
	 */
	public void setColour(RGB colour) {
		this.colour = colour;
	}
}
