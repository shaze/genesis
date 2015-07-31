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

/**This class contains the information about one PopulationGroup object,
 * including:
 * <ul>
 * <li>The colour and shape of the icon related to this group.
 * <li> the order of the group in the key
 * <li> name and display name of the group
 * </ul>
 */
public class PCAPopulationGroup implements Serializable{
	
	private static final long serialVersionUID = -908207224860737477L;
		public final static int circle = 0, triangle = 1, square = 2, diamond = 3;
		private String name,displayName;
		private int order;
		private boolean visible=true;
		private Icon icon= new Icon();
		
		public PCAPopulationGroup( String name, RGB colour,int order,int icon){
			this.name=name;
			this.displayName=name;			
			this.order = order;
			this.icon.setColour(colour);
			this.icon.setIcon(icon); 
			this.icon.setSize(3);
			this.icon.setBorder(true);
		}
		
		public int getIconSize(){
			return this.icon.getSize();
		}
		
		public void setIconSize(int iconSize){
			this.icon.setSize(iconSize);
		}
		
		public String getDisplayName() {
			return displayName;
		}
		
		public String getName() {
			return name;
		}
		

		public void setDisplayName(String name) {
			this.displayName = name;
		}

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}
		
		public RGB getColour(){			
			return this.icon.getColour();
		}
		
		public void setColour(RGB colour) {			
			this.icon.setColour(colour);
		}
		
		public int getIconSymbol(){			
			return this.icon.getIcon();
		}
		
		public void setIconSymbol(int icon) {
			this.icon.setIcon(icon);
		}

		public boolean getVisible() {
			return visible;
		}

		public void setVisible(boolean visible) {
			this.visible = visible;
		}

		public boolean getBorder() {
			return this.icon.getBorder();
		}

		public void setBorder(boolean border) {
			this.icon.setBorder(border);
		}

		public Icon getIcon() {
			return this.icon;
		}

}
