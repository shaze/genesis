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

package shared;

import java.io.Serializable;

/**
 * This class contains the names of population groups for a previous phenotype column
 */
public class SavedGroupNames implements Serializable {

	private static final long serialVersionUID = 4515641180647636458L;
	public int column;
	public String[] names;
	public int[] order;
	
	public SavedGroupNames(int column, String[] names, int[] order){
		this.names=names;
		this.column=column;
		this.order=order;
	}
}
