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

package drawObjects;

import java.io.Serializable;

import org.eclipse.swt.graphics.Point;

public interface DrawObject extends Serializable{		
    public int getType(); //0=line, 1=arrow
    public boolean clicked(Point point);
    public boolean getSelected();
    public void setSelected(boolean selected);
    public void shift(int deltax, int deltay, int imageWidth, int imageHeight);


    public void snap(); // Snap position of object to grid lines
	

    public final static String[] objectNames = new String[]{"Line", "Arrow"};
 

}
