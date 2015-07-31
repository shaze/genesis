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

package genesisDrawable;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;

/**
 * This interface contains the methods that are used to draw the PCA/Admixture plots 
 * onto the various formats.
 * 
 * At the moment the only formats are the SWT canvas used to draw the plots onto
 * the SWT widgets on the screen and the SVG canvas which can be drawn upon and then 
 * exported as a SVG file or attached to a PDF file.
 * 
 * 
 * @author R W Buchmann
 */
public interface GenesisDrawable {
	void setLineWidth(int width);
	void fillRectangle(int x, int y, int width, int height);
	void drawRectangle(int x, int y, int width, int height, Color col) ;
	/**
	 * Draws a polygon in the current colour with no border 
	 * @param poly an 2n array specifying the points of the polygon
	 * 				in the format (x1, y1 , ..., xn, yn)
	 */
	void fillPolygon(int[] poly);
	/**
	 * Draws a polygon in the current colour with a border with no fill colour
	 * @param poly an 2n array specifying the points of the polygon
	 * 				in the format (x1, y1 , ..., xn, yn)	
	 * @param border the colour of the border
	 */
	void drawPolygon(int[] poly, Color border);
	void drawTransparentText(String string, int x, int y);
	void drawText(String string, int x, int y);
	void drawVerticalLabel(String string, int x, int y);
	void drawLine(int x1, int y1, int x2, int y2);
	void setFont(Font font);
	void setColor(Color col);
	/**
	 * Gets the length (x) and height (y) of a string in the current font
	 * @param string the string
	 * @return the length and height of the string in the current font
	 */
	Point textExtent(String string);
	void drawPoint(int x, int y, Color fill);
	void drawOval(int x, int y, int width, int height, Color col);
	void fillOval(int x, int y, int width, int height);

}
