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

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

/**This class contains the information of one part (ie. one colour/ancestor) of an admixture column.
 * <p>
 * It stores the colour of the sliver and the bounds of the sliver.
 * <p>
 * It is a child of {@link ASubject}
 *
 */
public class Sliver {
	private Rectangle position;
	private RGB colour;
	
	public RGB getColour() {
		return colour;
	}
	public void setColour(RGB colour) {
		this.colour = colour;
	}
	public Rectangle getPosition() {
		return position;
	}
	public void setPosition(Rectangle position) {
		this.position = position;
	}
}
