/*************************************************************************
 * Genesis � program for creating structure and PCA plots of genotype data
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
 */

package pca.drawTools;

import genesisDrawable.GenesisDrawable;
import main.UI;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;

/**
 * This class contains the methods needed to draw PCA icons onto 
 * a GenesisDrawable component (swt canvas or svg canvas).
 * 
 * @author robertwbuchmann
 */
public class DrawIcon {

	/**
	 * This method draws an icon on the given GenesisDrawable with the
	 * given parameters.
	 * 
	 * @param drawable the GenesisDrawable
	 * @param coords the coordinates where the icon is to be drawn
	 * @param icon the icon (0=circle, 1=square, 2=triangle, 3=diamond,
	 * 						 4=cross, 5=plus, watch this space)
	 * @param border whether the icon has a border
	 * @param colour the (unselected) colour of the icon
	 * @param iconSize the size of the icon
	 * @param selected whether the icon is selected
	 */
	public static void drawIcon(GenesisDrawable drawable, Point coords,
			int icon, boolean border, RGB colour, int iconSize, boolean selected){
		
		Color line=setFillAndLineColours(colour, drawable, selected, border);
		switch (icon){
			case 0:
				drawCircle(drawable,coords.x,coords.y,iconSize,line);	
				break;
			case 1:
				drawSquare(drawable,coords.x,coords.y,iconSize,line);	
				break;
			case 2:
				drawTriangle(drawable,coords.x,coords.y,iconSize,line);
				break;
			case 3:
				drawDiamond(drawable,coords.x,coords.y,iconSize,line);	
				break;
			case 4:
				drawCross(drawable,coords.x,coords.y,iconSize,line);	
				break;
			case 5:
				drawPlus(drawable,coords.x,coords.y,iconSize,line);	
				break;		
		}		

	}	
	
	/**
	 * Sets the fill colour and line width of the GenesisDrawable according
	 * to the given parameters. Then returns the colour of the border which
	 * is to be used no later. Note that no border is simply represented by
	 * a border that is the same colour as the fill colour.
	 * 
	 * @param colour the (unselected) colour of the icon
	 * @param drawable the genesis drawable 
	 * @param selected whether the icon has been selected
	 * @param border whether the icon has a border
	 * @return the border/line colour
	 */
	private static Color setFillAndLineColours(RGB colour, GenesisDrawable drawable, boolean selected, boolean border) {
		Color line, fill;
		if(selected){			
			line=new Color(UI.display, 255, 0, 0); //red
			fill=new Color(UI.display, 255, 255, 255); //white
			drawable.setLineWidth(2);  
		}else if(border){
			line = new Color(UI.display, 0,0,0);//black
			fill=new Color(UI.display, colour);
			drawable.setLineWidth(1);  
		}else{
			fill=new Color(UI.display, colour);
			line=fill;
			drawable.setLineWidth(1);  
		}
		
		drawable.setColor(fill);
		return line;
		
	}
	/**
	 * Draws a circle icon on the given GenesisDrawable at the given 
	 * coordinates and drawn the given size
	 * @param drawable 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param iconSize the icon size
	 * @param line the colour of the border
	 */
	private static void drawCircle(GenesisDrawable drawable, int x, int y,
			int iconSize, Color line) {		
		if(drawable instanceof genesisDrawable.SWTCanvas){
			//note the "+1"s on the following lines. This is done because the 
			//swt fillOval function sometimes misses a few spots and the +1 
			//amazingly fixes that 
			drawable.fillOval(x-iconSize, y-iconSize, iconSize*2+1, iconSize*2+1);	
		}else{
			drawable.fillOval(x-iconSize, y-iconSize, iconSize*2, iconSize*2);
		}			  				
		drawable.drawOval(x-iconSize, y-iconSize, iconSize*2, iconSize*2, line);  
		
	}
	/**
	 * Draws a square icon on the given GenesisDrawable at the given 
	 * coordinates and drawn the given size
	 * @param drawable 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param iconSize the icon size
	 * @param line the colour of the border
	 */
	private static void drawSquare(GenesisDrawable drawable, int x, int y,
			int iconSize, Color line) {
		drawable.fillRectangle(x-iconSize, y-iconSize, iconSize*2, iconSize*2);    		
		drawable.drawRectangle(x-iconSize, y-iconSize, iconSize*2, iconSize*2, line);
		
	}
	/**
	 * Draws a triangle icon on the given GenesisDrawable at the given 
	 * coordinates and drawn the given size
	 * @param drawable 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param iconSize the icon size
	 * @param line the colour of the border
	 */
	private static void drawTriangle(GenesisDrawable drawable, int x, int y,
			int iconSize, Color line) {
		drawable.fillPolygon(getTrianglePolygon(x,y,iconSize));
		drawable.drawPolygon(getTrianglePolygon(x,y,iconSize),line);  	
	}
	/**
	 * Gets the coordinates to draw the triangle icon in the various sizes.
	 * @param x the x coordinate of the icon
	 * @param y the y coordinate of the icon
	 * @param size the size of the icon
	 * @return the polygon in the format (x1,y1, x2,y2, ... )
	 */
	private static int[] getTrianglePolygon(int x, int y, int size){
		return new int[]{x-size, y+size, x,y-size, x+size,y+size};
	}
	/**
	 * Draws a diamond icon on the given GenesisDrawable at the given 
	 * coordinates and drawn the given size
	 * @param drawable 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param iconSize the icon size
	 * @param line the colour of the border
	 */
	private static void drawDiamond(GenesisDrawable drawable, int x, int y,
			int iconSize, Color line) {
		drawable.fillPolygon(getDiamondPolygon(x,y,iconSize));
		drawable.drawPolygon(getDiamondPolygon(x,y,iconSize),line);	
	}
	/**
	 * Gets the coordinates to draw the diamond icon in the various sizes.
	 * @param x the x coordinate of the icon
	 * @param y the y coordinate of the icon
	 * @param size the size of the icon
	 * @return the polygon in the format (x1,y1, x2,y2, ... )
	 */
	private static int[] getDiamondPolygon(int x, int y, int size){
		return new int[]{x-size,y, x,y-size, x+size,y, x,y+size};
	}
	/**
	 * Draws a cross icon on the given GenesisDrawable at the given 
	 * coordinates and drawn the given size
	 * @param drawable 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param iconSize the icon size
	 * @param line the colour of the border
	 */
	private static void drawCross(GenesisDrawable drawable, int x, int y,
			int iconSize, Color line) {
		drawable.fillPolygon(getCrossPolygon(x,y,iconSize));
		drawable.drawPolygon(getCrossPolygon(x,y,iconSize),line);    	
		
	}	
	/**
	 * Gets the coordinates to draw the cross icon in the various sizes.
	 * @param x the x coordinate of the icon
	 * @param y the y coordinate of the icon
	 * @param size the size of the icon
	 * @return the polygon in the format (x1,y1, x2,y2, ... )
	 */
	private static int[] getCrossPolygon(int x, int y, int size){
		return new int[]{x-size,y-(size+(size/2)), x-(size+(size/2)),y-size, 
				x-(size/2),y, x-(size+(size/2)),y+size, x-size,y+(size+(size/2)), 
				x,y+(size/2), x+size,y+(size+(size/2)), x+(size+(size/2)),y+size,
				x+(size/2),y, x+(size+(size/2)),y-size, x+size,y-(size+(size/2)),
				x,y-(size/2)};
	}
	/**
	 * Draws a plus icon on the given GenesisDrawable at the given 
	 * coordinates and drawn the given size
	 * @param drawable 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param iconSize the icon size
	 * @param line the colour of the border
	 */
	private static void drawPlus(GenesisDrawable drawable, int x, int y, int iconSize, Color line) {
		drawable.fillPolygon(getPlusPolygon(x,y,iconSize));
		drawable.drawPolygon(getPlusPolygon(x,y,iconSize),line); 			
	}
	/**
	 * Gets the coordinates to draw the plus icon in the various sizes.
	 * @param x the x coordinate of the icon
	 * @param y the y coordinate of the icon
	 * @param size the size of the icon
	 * @return the polygon in the format (x1,y1, x2,y2, ... )
	 */
	private static int[] getPlusPolygon(int x, int y, int size){
		return new int[]{x-size/2,y-size, x-size/2,y-size/2, x-size,y-size/2, 
				x-size,y+size/2, x-size/2,y+size/2, x-size/2,y+size,
				x+size/2,y+size, x+size/2,y+size/2, x+size,y+size/2,
				x+size,y-size/2, x+size/2,y-size/2, x+size/2,y-size};
	}		
}

