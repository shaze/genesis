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
import java.io.Serializable;

import org.eclipse.swt.graphics.RGB;

import pca.icon.Icon;

/**This object contains the data about one element on the PCA plot
 * <p>
 * This includes:
 * <li>an array of floats representing the data of the various PCAs
 * <li>the name and phenotype data of the individual
 * <li>any specific icon data related to this individual
 * <li>booleans specifying whether or not the individual is 
 * 		selected, on top or hidden from the graph
 * </ul>
 * 
 * A child of  {@link PCAGraph}
 */
public class PCASubject implements Serializable {

	private static final long serialVersionUID = 6997827831785759993L;
	private float[] values; //contains the data.
	private  String name;
	private  String controlTag; //this holds that last word in the line
	private  String[] phenotypeData;	
	private Icon icon=new Icon();
	private boolean visible=true,selected=false,onTop=false;
	private boolean inFront=false;
	
	private PCAPopulationGroup group;//just a pointer, doesnt need to be saved
	
	public PCASubject(float[] values,String name){
		this.values=values;
		this.name=name;		
	}
	
	public void setVisible(boolean visible){
		this.visible=visible;
	}
	
	public boolean getVisible(){
		return visible;
	}
	
	public void setSelected(boolean selected){
		this.selected=selected;
	}
	
	public boolean getSelected(){
		return selected;
	}
	
	public void setIconSymbol(int icon){
		this.icon.setIcon(icon); 
	}
	
	public void setIconSize(int iconSize){
		this.icon.setSize(iconSize); 
	}
	
	public int getIconSymbol(){
		return this.icon.getIcon();
	}
	public int getIconSize(){
		return this.icon.getSize();
	}
	
	public String getName(){
		return name;
	}
	
	public String getControlTag(){
		return controlTag;
	}
	
	public void setPhenotypeData(String[] phenoData){
		this.phenotypeData = phenoData;
	}
	
	public String[] getPhenotypeData(){
		return phenotypeData;
	}
	
	public float[] getData(){
		return values;
	}
	
	public int getNoComponents(){
		return values.length;
	}
	
	public void setColour(RGB colour){
		this.icon.setColour(colour);
	}
	
	public RGB getColour(){
		return this.icon.getColour();
	}
	
	public boolean hasPheno(){
		if (phenotypeData==null){
			return false;
		}else {
			return true;
		}
	}

	public PCAPopulationGroup getGroup() {
		return group;
	}

	public void setGroup(PCAPopulationGroup group) {
		this.group = group;
	}

	public boolean isOnTop() {
		return onTop;
	}

	public void setOnTop(boolean onTop) {
		this.onTop = onTop;
	}

	public boolean getInFront() {
		return inFront;
	}

	public void setInFront(boolean inFront) {
		this.inFront = inFront;
	}
	
	public Icon getIcon(){
		return this.icon;
	}

	public boolean getBorder() {
		return icon.getBorder();
	}
	
	public void setBorder(boolean border){
		this.icon.setBorder(border);
	}
	
}
