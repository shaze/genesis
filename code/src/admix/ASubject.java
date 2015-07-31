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

package admix;

import org.eclipse.swt.graphics.Rectangle;

import admix.drawinfo.DrawInfo;

/** This object contains information pertaining to one subject to be
 * drawn on the Admixture Graph
 * <p>
 * This includes <ul>
 * <li> a pointer to the {@link AdmixSubject} that it represents
 * <li> an array of {@link Sliver}
 * <li> the total bounds of the column on the admixture graph.
 * </ul>
 * This is a child of the {@link DrawInfo} class. An array of 
 * 	ASubject is contained in {@link DrawInfo}
 */
public class ASubject {
	private AdmixSubject subject;
	private Sliver[] slivers;
	private Rectangle position;
	private boolean visible;
	
	public ASubject(AdmixSubject subject,  boolean visible) {
		this.subject = subject;
		this.visible=visible;
	}

	public AdmixSubject getSubject() {
		return subject;
	}


	public Sliver[] getSlivers() {
		return slivers;
	}

	public void setSlivers(Sliver[] slivers) {
		this.slivers = slivers;
	}

	public Rectangle getPosition() {
		return position;
	}

	public void setPosition(Rectangle position) {
		this.position = position;
	}

	public boolean getVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	
	
	
	
}
