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

import main.UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

public class SWTCanvas implements GenesisDrawable {

	GC gc;
	
	public SWTCanvas(GC gc) {
		super();
		this.gc = gc;
	}

	
	@Override
	public void setLineWidth(int width) {
		gc.setLineWidth(width);

	}

	@Override
	public void fillRectangle(int x, int y, int width, int height) {
		gc.fillRectangle(x,y,width,height);

	}

	@Override
	public void drawRectangle(int x, int y, int width, int height, Color col) {
		gc.setForeground(col);
		gc.drawRectangle(x,y,width,height);

	}

	@Override
	public void fillPolygon(int[] poly) {
		gc.fillPolygon(poly);

	}

	@Override
	public void drawPolygon(int[] poly, Color col) {
		gc.setForeground(col);
		gc.drawPolygon(poly);
		

	}

	@Override
	public void drawTransparentText(String string, int x, int y) {
		gc.drawText(string, x, y,SWT.DRAW_TRANSPARENT);

	}

	@Override
	public void drawText(String string, int x, int y) {
		gc.setBackground(new Color(UI.display, 255, 255, 255));
		gc.drawText(string, x, y);

	}

	@Override
	public void drawVerticalLabel(String string, int x, int y) {
		Point size = gc.textExtent(string);
		Image label = new Image(UI.display, size.x, size.y);

		GC newDrawer = new GC(label);

		newDrawer.setForeground(gc.getForeground());
		newDrawer.setBackground(new Color(UI.display, 255, 255, 255));
		newDrawer.setFont(gc.getFont());		    		   

		newDrawer.drawText(string, 0, 0);

		ImageData oldData = label.getImageData();
		ImageData newData = new ImageData(oldData.height, oldData.width, oldData.depth, oldData.palette);		   
		for (int i = 0; i < oldData.width; i++) {
			for (int j = 0; j < oldData.height; j++) {		       
				newData.setPixel(j, oldData.width-i-1, oldData.getPixel(i, j));
			}
		}
		label= new Image(UI.display, newData);
		gc.drawImage(label, x, y);
		newDrawer.dispose();
		label.dispose();

	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		gc.drawLine(x1, y1, x2, y2);

	}

	@Override
	public void setFont(Font font) {
		gc.setFont(font);

	}

	@Override
	public void setColor(Color col) {
		gc.setForeground(col);
		gc.setBackground(col);

	}

	@Override
	public Point textExtent(String string) {
		return gc.textExtent(string);
	}

	@Override
	public void drawPoint(int x, int y, Color col) {
		gc.setForeground(col);
		gc.drawPoint(x, y);

	}

	@Override
	public void drawOval(int x, int y, int width, int height, Color col) {
		gc.setForeground(col);
		gc.drawOval(x, y, width, height);
		

	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		gc.fillOval(x, y, width, height);
		gc.setBackground(new Color(UI.display, 120, 200,0));
		/*gc.drawPoint(x+4,y+5);
		gc.drawPoint(x+6,y+5);*/

	}


}
