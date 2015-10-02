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
 * 
 */

package genesisDrawable;




import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.jfree.graphics2d.svg.SVGGraphics2D;

public class SVGCanvas implements GenesisDrawable{

	public SVGGraphics2D gc;
	

	public SVGCanvas(SVGGraphics2D gc) {
		super();
		this.gc = gc;
		gc.setBackground(java.awt.Color.WHITE);
	}
	

	private java.awt.Font swtFontToAwtFont(Font swtFont) {
		FontData oldFont = swtFont.getFontData()[0];
		String name = oldFont.getName();
		int style = getAWTStyle(oldFont.getStyle());
		int size = oldFont.getHeight();

		return new java.awt.Font(name,0,0).deriveFont(style, (float)size*1.2f);

	}


	private int getAWTStyle(int swtStyle) {
		int style=0;

		if(swtStyle%2==1){ //SWT.BOLD=1
			style=style|java.awt.Font.BOLD;
		}
		if(swtStyle>=2){ //SWT.ITALIC=2
			style=style|java.awt.Font.ITALIC;
		}

		return style;
	}


	private java.awt.Color swtColToAwtCol(Color col) {
		return new java.awt.Color(col.getRed(), col.getGreen(), col.getBlue()) ;
	}



	@Override
	public void setLineWidth(int width) {java.awt.BasicStroke stroke=new java.awt.BasicStroke(
			width, java.awt.BasicStroke.CAP_SQUARE, java.awt.BasicStroke.JOIN_MITER);
	gc.setStroke(stroke);
		
	}

	@Override
	public void fillRectangle(int x, int y, int width, int height) {
		gc.fillRect(x, y, width, height);
		
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height, Color col) {
		gc.setColor(swtColToAwtCol(col));
		gc.drawPolygon(new int[]{x,x+width,x+width,x,x},
				new int[]{y,y,y+height,y+height,y},5);
		
	}

	@Override
	public void fillPolygon(int[] poly) {
		gc.fillPolygon(swtToAWTPolygon(poly));
		
	}
	
	private java.awt.Polygon swtToAWTPolygon(int[] poly) {
		int n=poly.length/2+1;
		int[] xPoints = new int[n];
		int[] yPoints = new int[n];

		for(int i=0;i<n-1;i++){
			xPoints[i]=poly[i*2];
			yPoints[i]=poly[i*2+1];
		}
		xPoints[n-1]=xPoints[0];
		yPoints[n-1]=yPoints[0];

		return new java.awt.Polygon(xPoints, yPoints, n);
	}

	@Override
	public void drawPolygon(int[] poly, Color col) {
		gc.setColor(swtColToAwtCol(col));
		gc.drawPolygon(swtToAWTPolygon(poly));
		
	}

	@Override
	public void drawTransparentText(String string, int x, int y) {
		gc.drawString(string, x, y+gc.getFontMetrics().getAscent());
		
	}

	@Override
	public void drawText(String string, int x, int y) {
		gc.clearRect(x, y,  gc.getFontMetrics().stringWidth(string),
				gc.getFontMetrics().getAscent());
		gc.drawString(string, x, y+gc.getFontMetrics().getAscent());
		
	}

	@Override
	public void drawVerticalLabel(String string, int y, int x) {
		java.awt.geom.AffineTransform orig = gc.getTransform();
		
		gc.rotate(-Math.PI/2);
		gc.drawString(string,-x,y+gc.getFontMetrics().getAscent());
		
		gc.setTransform(orig);
		
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		gc.drawLine(x1, y1, x2, y2);
		
	}

	@Override
	public void setFont(Font font) {
		gc.setFont(swtFontToAwtFont(font));
	}

	@Override
	public void setColor(Color col) {
		gc.setColor(swtColToAwtCol(col));
		
	}

	@Override
	public Point textExtent(String string) {
		java.awt.FontMetrics fm = gc.getFontMetrics();
		int width=fm.stringWidth(string);
		int height=fm.getHeight();

		return new Point(width, height);
	}

	@Override
	public void drawPoint(int x, int y, Color col) {
		//do nothing with SVGCanvas(only used with SWTCanvas)
	}

	@Override
	public void drawOval(int x, int y, int width, int height, Color col) {
		gc.setColor(swtColToAwtCol(col));
		gc.drawOval(x,y,width,height);	
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		gc.fillOval(x,y,width,height);	
		
	}

}
