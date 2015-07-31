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

package pca.drawTools;

import genesisDrawable.GenesisDrawable;

import main.UI;
import shared.GLabel;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import pca.PCAGraph;
import pca.PCAPopulationGroup;
import pca.PCAWorkflow;
import pca.drawInfo.DrawInfo;
import pca.drawInfo.DrawInfo3D;
import drawObjects.Arrow;
import drawObjects.DrawObject;
import drawObjects.Line;

/**This class contains the methods used to draw the PCA graph onto the right component/canvas.
 * It uses data from the the drawinfo and pca graph objects
 * 
 * Created by {@link UIPCA}
 *
 */
public class DrawTools {

	static DrawInfo drawInfo;
	static DrawInfo3D drawInfo3D;
	static PCAGraph graph;
	static int dimensions;
	static Point size;
	static GenesisDrawable drawable;
	static Display display;	

	/**The constructor of the class
	 * @param drawable the Graphics Context on which to draw the graph
	 * @param drawInfo the drawinfo object
	 * @param drawInfo3D the drawinfo3D object
	 * @param graph the pca graph object
	 * @param dimensions the dimensions of the graph (2 or 3)
	 * @param size the size of the canvas on which to draw the graph
	 * @param display the current display
	 */
	public DrawTools(GenesisDrawable drawable, DrawInfo drawInfo, DrawInfo3D drawInfo3D,
			PCAGraph graph,int dimensions,Point size,Display display) {
		DrawTools.drawable = drawable;
		DrawTools.drawInfo = drawInfo;
		DrawTools.drawInfo3D = drawInfo3D;
		DrawTools.graph = graph;
		DrawTools.dimensions = dimensions;
		DrawTools.display=display;
		DrawTools.size=size;
	}

	public static Color white, grey, black, darkgrey, lightgrey;
	
	/**The only public method of the class. Draws the graph onto the given 
	 * GenesisDrawable using the given information. This includes all drawing methods 
	 * such as the ones used to draw the key/heading etc.
	 */
	public void drawGraph(){  
		initialiseColors();

		clearBackground();

		if(dimensions==2){ 
			DrawTools2D.draw2DPCA();
		}else{//3D Plot
			DrawTools3D.draw3DPCA();
		}
		
	}

	private void initialiseColors() {
		white=new Color(UI.display, new RGB(255,255,255));
		grey=new Color(UI.display, new RGB(127,127,127));
		black=new Color(UI.display, new RGB(0,0,0));
		darkgrey=new Color(UI.display, new RGB(63,63,63));
		lightgrey=new Color(UI.display, new RGB(190,190,190));
		
	}

	/**
	 * Clears the background of the canvas by covering it in a white rectangle.
	 */
	private void clearBackground() {
		drawable.setColor(white);
		drawable.fillRectangle(0,0,size.x,size.y);		
	}

	
	
	

	static void drawObjects(Rectangle drawArea) {
		for(DrawObject dO:graph.getDrawObjects()){
			if(dO.getSelected()){
				drawable.setColor(new Color(display, 255, 0, 0));
				drawable.setColor(new Color(display, 255, 0, 0));
			}else{
				drawable.setColor(black);
				drawable.setColor(black);
			}
			try{
				if(dO.getType()==0){
					drawLineObject((Line)dO, drawArea);
				}else if(dO.getType()==1){
					drawArrowObject((Arrow)dO, drawArea);
				}
			}catch(NullPointerException e){
				graph.getDrawObjects().remove(dO);
			}
		}

	}


	static void drawArrow(int x1, int y1, int x2, int y2) throws NullPointerException {        
		Point tail=new Point(x1,y1);
		Point head=new Point(x2,y2);           

		drawable.drawLine(x1, y1, x2, y2);        
		drawArrowHead(drawable, head, tail );

	}


	private static void drawArrowObject(Arrow arrow , Rectangle drawArea) throws NullPointerException{
		drawArrow(arrow.start.x*drawArea.width/1000, arrow.start.y*drawArea.height/1000,
				arrow.end.x*drawArea.width/1000, arrow.end.y*drawArea.height/1000);

	}

	private static void drawArrowHead(GenesisDrawable drawable, Point head, Point tail) throws NullPointerException {  		
		Point corner1=null;
		Point corner2=null;

		int length = 10;
		double phi = Math.toRadians(25);
		double theta = Math.atan2(head.y - tail.y, head.x - tail.x);  	        
		double alpha = theta + phi;  
		for(int j = 0; j < 2; j++){  
			int ix = (int)Math.round(head.x - length * Math.cos(alpha));  
			int iy = (int)Math.round(head.y - length * Math.sin(alpha));  		
			alpha = theta - phi;  
			if(j==0){
				corner1=new Point(ix,iy);
			}else{
				corner2=new Point(ix,iy);
			}				
		}  
		try{
			drawable.fillPolygon(new int[]{head.x, head.y, corner1.x, corner1.y, corner2.x, corner2.y});	
		}catch(NullPointerException e){
			e.printStackTrace();
		}

	}  


	private static void drawLineObject(Line line, Rectangle drawArea) throws NullPointerException {
		drawable.drawLine(line.start.x*drawArea.width/1000, line.start.y*drawArea.height/1000,
				line.end.x*drawArea.width/1000, line.end.y*drawArea.height/1000);


	}

	

	static void drawAnnotations(Rectangle graphArea) {
		drawable.setColor(black);
		drawable.setColor(white);


		for(GLabel lbl : graph.getLabels()){	

			setLabelFont(lbl);	
			int width = drawable.textExtent(lbl.getText()).x;
			int height = drawable.textExtent(lbl.getText()).y;

			Rectangle drawArea;

			if(graph.is3D()){
				drawArea = drawInfo3D.getDrawArea();
			}else{
				drawArea = drawInfo.getDrawArea();
			}

			int imageWidth = (drawArea.width);
			int imageHeight = (drawArea.height);

			int labelX = (imageWidth*lbl.getRatio().x/1000) - width/2;
			int labelY = (imageHeight*lbl.getRatio().y/1000) - height/2;

			drawable.setFont(new Font(display, lbl.getFont()));
			drawable.drawTransparentText(lbl.getText(), labelX, labelY);
			lbl.setPosition(new Rectangle(labelX,labelY,width,height));

			if(lbl.getUnderlined()){
				underlineLabel(labelX, labelY, drawable.textExtent(lbl.getText()).x,
						drawable.textExtent(lbl.getText()).y);
			}
		}

	}

	private static void underlineLabel(int startX, int startY, int length, int fontHeight) {				
		drawable.setLineWidth(2);
		drawable.drawLine(startX, startY+fontHeight-1, startX+length, startY+fontHeight-1);				
	}



	private static void setLabelFont(GLabel lbl) {
		drawable.setFont(new Font(display,lbl.getFont()));
		drawable.setColor(black);

	}

	



	static void drawHeading(GenesisDrawable drawable, Rectangle graphArea) {
		setGenesisDrawableToHeadingFont();				
		int center = graphArea.x + (graphArea.width/2);
		int fontWidth = drawable.textExtent(graph.getHeading()).x;
		int fontHeight = drawable.textExtent(graph.getHeading()).y;
		drawable.drawText(graph.getHeading(), center-(fontWidth/2), 8);
		underLineheading(center-(fontWidth/2), fontWidth, fontHeight);

	}


	private static void underLineheading(int startX, int length, int fontHeight) {
		if(graph.getHeadingUnderline()){
			drawable.setLineWidth(2);
			drawable.drawLine(startX, 8+fontHeight-2, startX+length, 8+fontHeight-2);
		}

	}



	private static void setGenesisDrawableToHeadingFont() {
		drawable.setColor(black);    	
		FontData f = graph.getHeadingFont();		
		Font font = new Font(display, f);				
		drawable.setFont(font);

	}



	static void drawKey(int keyPosition, Rectangle graphArea) {

		if(keyPosition==0){ //draw key on right
			drawKeyOnRight(graphArea);    		
		}else if(keyPosition==1){//draw key at bottom
			drawKeyOnBottom(graphArea);    		        
		}			
	}


	private static void drawKeyOnBottom(Rectangle graphArea) {        	
		drawable.setFont( new Font(display,graph.getKeyFont()));

		int keyHeight=PCAWorkflow.getBottomKeyHeight(graph);    	   
		int keyWidth = getBottomKeyWidth(drawable);
		int keyY = graphArea.y + graphArea.height + 35;
		int keyX = graphArea.x + (graphArea.width/2)-keyWidth/2;

		setGenesisDrawableAttributesForKey(drawable);	        		        
		int center = graphArea.x + (graphArea.width/2);
		//draw the box holding the key
		drawable.drawRectangle(keyX,keyY,keyWidth,keyHeight,black);
		//draw the icons and labels for each group
		int countX=center-keyWidth/2;
		int[][] keyValues = new int[PCAWorkflow.getNoVisibleGroups(graph)][2];
		int count=0;
		for(int i=0;i<graph.getGroups().length;i++){	
			PCAPopulationGroup pop = findPopWithOrder(i);
			if(!pop.getVisible()){
				continue;
			}
			countX = drawGroupInBottomKey(pop, countX, keyValues, keyY,keyHeight, count);
			count++;
		}
		drawInfo.setKeyValues(keyValues);

	}






	private static void drawKeyOnRight(Rectangle graphArea) {
		int fontHeight = PCAWorkflow.getKeyFontHeight(graph);   

		int keyHeight=PCAWorkflow.getRightKeyHeight(graph);    	    	   	   
		int keyWidth = drawInfo.getKeyWidth();
		int keyX=graphArea.width+graphArea.x+10;
		if(graph.z!=-1){
			keyX+=PCAWorkflow.getAxisLabelHeight(graph);
		}
		int keyY=(graphArea.height+graphArea.y-keyHeight)/2+4;    		

		DrawTools.setGenesisDrawableAttributesForKey(drawable);

		drawable.drawRectangle(keyX,keyY-5,keyWidth,keyHeight+8, black);
		//draw the icons and labels for each group
		int count = 0;
		for(int i=0;i<graph.getGroups().length;i++){
			PCAPopulationGroup pop = findPopWithOrder(i);  
			if(!pop.getVisible()){
				continue;
			}
			drawGroupInRightKey(pop, count, keyWidth, keyHeight, keyX, fontHeight, graphArea);
			count++;
		}	

	}

	private static int  drawGroupInBottomKey(PCAPopulationGroup pop, int countX,
			int[][] keyValues, int keyY, int keyHeight, int count) {		
		int fontHeight = PCAWorkflow.getKeyFontHeight(graph);
		int iSize;
		final int GAP = 16;
		countX+=GAP;
		keyValues[count][0]=countX-5;

		int iconY = keyY+keyHeight/2;
		Point iconPoint = new Point(countX,iconY);
		iSize = adjustedIconSize(pop, fontHeight);
		DrawIcon.drawIcon(drawable, iconPoint, pop.getIconSymbol(), pop.getBorder(), pop.getColour(), iSize,false);
		setGenesisDrawableAttributesForKey(drawable);	    
		countX+=GAP;
		drawable.drawText(pop.getDisplayName(),countX, iconY-fontHeight/2);
		countX+=(drawable.textExtent(pop.getDisplayName()).x);
		keyValues[count][1]=countX;
		return countX;
	}

	private static int adjustedIconSize(PCAPopulationGroup pop, int fontHeight) {
		int iSize;
		final int cutoff = fontHeight/2+4;
		iSize = pop.getIconSize();
		if (iSize > cutoff)
		    iSize = cutoff;
		else 
			if (iSize <5) 
				iSize =5;
		return iSize;
	}


	private static void drawGroupInRightKey(PCAPopulationGroup pop, int i, int keyWidth,
			int keyHeight, int keyX, int fontHeight, Rectangle graphArea) {		

		int iSize;
		
		drawable.setFont(new Font(UI.display,graph.getKeyFont()));
		int width = drawable.textExtent(pop.getDisplayName()).x;
		int iconX = keyX+(keyWidth-width)/2-3;
		int iconY = (graphArea.height+graphArea.y-keyHeight)/2+5+fontHeight/2+(fontHeight+6)*i;
		Point pt = new Point(iconX,iconY);
		iSize = adjustedIconSize(pop, fontHeight);
		DrawIcon.drawIcon(drawable,pt,pop.getIconSymbol(),pop.getBorder(), pop.getColour(),iSize,false);
		drawable.setColor(white); 
		drawable.setColor(black);
		drawable.drawText(pop.getDisplayName(),iconX+21,iconY-fontHeight/2);	

	}



	private static void setGenesisDrawableAttributesForKey(GenesisDrawable drawable) {
		drawable.setColor(white);
		drawable.setColor(black);
		drawable.setLineWidth(1);


	}

	static int getBottomKeyWidth(GenesisDrawable drawable){
		int sum=0;
		for(PCAPopulationGroup pop:graph.getGroups()){
			if(pop.getVisible()){
				sum+=32;
				sum+=drawable.textExtent(pop.getDisplayName()).x;	
			}    		
		}
		sum+=15;
		return sum;
	}

	//this draws the given point on the graph in the given style 
	/*static void drawIcon(GenesisDrawable drawable,Point point,int icon,boolean border,RGB colour,int iconSize,boolean selected){
		int x = point.x, y = point.y;

		int[] plus = new int[]{x-1,y-4, x-1,y-1, x-4,y-1, x-4,y+1, x-1,y+1, x-1,y+4,
				x+1,y+4, x+1,y+1, x+4,y+1, x+4,y-1, x+1,y-1, x+1,y-4};
		int[] cross = new int[]{x-3,y-4, x-4,y-3, x-1,y, x-4,y+3, x-3,y+4, x,y+1,
				x+3,y+4, x+4,y+3, x+1,y, x+4,y-3, x+3,y-4, x,y-1}; 
		int[] diamond = new int[]{x-3,y, x,y-3, x+3,y, x,y+3};
		int[] triangle = new int[]{x-3, y+2, x,y-4, x+3,y+2};

		Color fill=new Color(display, colour);
		Color line=black;	
		drawable.setLineWidth(1);  

		if(selected){
			line=new Color(UI.display, 255, 0, 0);
			fill=white;
			drawable.setLineWidth(2);  
		}else if(border){
			line = black;
		}else{
			line=fill;
		}
		drawable.setColor(fill);

		if(icon==0){       //drawcircle 	    		
			drawable.fillOval(x-3, y-3, 6, 6);    		
			drawable.drawOval(x-3, y-3, 6, 6, line);        	
			//colours 2 dots that get missed by the SWT drawer
			if(iconSize==3){
				drawable.drawPoint(x+2,y+1,fill);
				drawable.drawPoint(x+1,y+2,fill);				
			}
		}else if(icon==1){//drawsquare
			drawable.fillRectangle(x-3, y-3, 6, 6);    		
			drawable.drawRectangle(x-3, y-3, 6, 6, line);
		}else if(icon==2){//drawtriangle
			drawable.fillPolygon(triangle);
			drawable.drawPolygon(triangle, line);        	
		}else if(icon==3){//diamond
			drawable.fillPolygon(diamond);
			drawable.drawPolygon(diamond, line);    		
		}else if(icon==4){//cross 
			drawable.fillPolygon(cross);
			drawable.drawPolygon(cross, line);        	
		}else if(icon==5){//plus
			drawable.fillPolygon(plus);
			drawable.drawPolygon(plus, line);    		
		}

	}
*/
	private static PCAPopulationGroup findPopWithOrder(int order){
		for(PCAPopulationGroup p:graph.getGroups()){
			if(p.getOrder()==order){
				return p;
			}
		}
		return null;
	}




}
