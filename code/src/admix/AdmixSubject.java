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


/**This object contains the data about one element on the admixture plot
 * <p>
 * This includes:
 * <li>an array of floats representing the ratios of the ancestors
 * <li>the name and phenotype data of the individual
 * <li>a pointer to this individual's external population group
 * </ul>
 * 
 * @see AdmixGraph
 */
public class AdmixSubject implements Serializable{ 
	private static final long serialVersionUID = 6612552057155898150L;
	private float[] ratio; 
	private AdmixPopulationGroup[] groups;
	private String name;		
	private String[] phenotypeData; 
	private boolean receivedPheno=false;
	private boolean selected,visible=true;
	
	public float[] getRatio(){ //returns the whole ratio array
		return ratio;		
	}
	
	public void setPhenotypeData(String[] phenoData){
		this.phenotypeData = phenoData;
		setReceivedPheno(true);
	}
	
	public String[] getPhenotypeData(){
		return phenotypeData;
	}
	
	public int getNoAncestors(){ 
		return ratio.length;		
	}
	
	public float getPercent(int x){ // returns the xth element of the ratio array
		return ratio[x];		
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	
	AdmixSubject(float[] ratio){
		this.ratio = ratio;
	}

	public AdmixPopulationGroup[] getGroups() {
		return groups;
	}

	public void setGroup(AdmixPopulationGroup[] groups) {
		this.groups = groups;
	}

	public boolean isReceivedPheno() {
		return receivedPheno;
	}

	public void setReceivedPheno(boolean receivedPheno) {
		this.receivedPheno = receivedPheno;
	}
	
	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean getVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}	
	
}
