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

import java.io.Serializable;

import org.eclipse.swt.graphics.RGB;


/**
 * This object holds the data related to one internal population group
 * <p>
 * This includes :
 * <ul>
 * <li>the group's name
 * <li> ID
 * <li> order to be displayed in the graph
 * <li> colour of the ancestor
 * </ul>
 *
 *This is a child of {@link AdmixGraph}
 */
public class Ancestor implements Serializable{

	private static final long serialVersionUID = 1735871888270696937L;
	private String name,displayName;
	private int ID,order;
	private RGB colour; //dunno how we are dealing with colours so int for now.	
	
	public Ancestor(int ID,String name, int order, RGB colour) {
		this.name = name;
		this.order = order;
		this.colour = colour;
		this.ID = ID;
		this.displayName=name;
	}



    public String toString() {
	return ("N: "+name+" DN: "+displayName+" ID:"+ID+
                           " OR:"+order+"  COL:"+colour);
    }

       public void deepCopy(Ancestor src) {
	  name=src.name;
  	  displayName=src.displayName;
	  order = src.order;
	  colour= src.colour;
       }
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public RGB getColour() {
		return colour;
	}
	public void setColour(RGB colour) {
		this.colour = colour;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	

}
