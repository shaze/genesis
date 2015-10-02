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

package pca;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;

import pca.drawInfo.DrawInfo;

/**
 * This class contains all the information relating to one point on the PCA graph including 
 * <ul>
 * <li> the coordinates where the subject is to be drawn
 * <li> a pointer to the relevant {@link PCASubject} 
 * <li>information about the colour and icon of the point
 * </ul>
 * <p> 
 *it is created as a child of the {@link DrawInfo} object
 *
 *
 */
public class Subject{
	private Point point;
	private PCASubject subj;
	private RGB colour;
	private int icon,iconSize;
	
	
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public PCASubject getSubj() {
		return subj;
	}
	public void setSubj(PCASubject subj) {
		this.subj = subj;
	}
	public RGB getColour() {
		return colour;
	}
	public void setColour(RGB colour) {
		this.colour = colour;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public int getIconSize() {
		return iconSize;
	}
	public void setIconSize(int iconSize) {
		this.iconSize = iconSize;
	}
	/**
	 * @return whether or not the subject is in front of or behind the 3D axes
	 */
	public boolean isInFront() {
		return subj.getInFront();
	}
	/**
	 * This method sets whether or not the subject is in front of or behind the 3D axes
	 * @param inFront whether or not the subject is in front of or behind the 3D axes
	 */
	public void setInFront(boolean inFront) {
		this.subj.setInFront(inFront);
	}
	
	
}