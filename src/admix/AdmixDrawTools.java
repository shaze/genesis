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

import genesisDrawable.GenesisDrawable;
import main.UI;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import admix.drawinfo.DrawInfo;
import drawObjects.Arrow;
import drawObjects.DrawObject;
import drawObjects.Line;
import shared.GLabel;


/**This class contains the methods used to draw the Admixture graph onto the right component.<p>
 * It uses data from the {@link DrawInfo}, and {@link AdmixGraph} objects
 * <p>
 * A child of {@link UIAdmixture}
 *
 *

 */
public class AdmixDrawTools {

	private AdmixProj proj;
	private DrawInfo drawInfo;
	private AdmixGraph graph;
	private GenesisDrawable drawable;
	private Display display;
	private Rectangle graphArea;
	private int graphNo;
	private int headingMargin;
	
	/**The constructor of the class
	 * @param drawable the Graphics Context on which to draw the graph
	 * @param drawInfo the DrawInfo object
	 * @param graph the AdmixGraph object
	 * @param size the size of the component on which to draw the graph
	 * @param display the current display
	 * @param graphNo the index or order of the graph
	 * 
	 * 
	 *@see DrawInfo
	 *@see AdmixGraph
	 */
	public AdmixDrawTools(GenesisDrawable drawable, DrawInfo drawInfo, AdmixGraph graph
				,Point size,Display display,int graphNo) {
		this.drawable = drawable;
		this.drawInfo = drawInfo;
		this.graph = graph;
		this.display=display;
		this.graphNo=graphNo;
		//colour background white
		if(graphNo==0){
			drawable.setColor(new Color(display, 255, 255, 255));
			drawable.fillRectangle(0,0,size.x,size.y);
		}
		this.proj = graph.getProj();

	}

	private Color white,grey,black,darkgrey,lightgrey;
	/**The only public method of the class. Draws the graph onto the given 
	 * GenesisDrawable using the given information. This includes all drawing methods 
	 * such as the ones used to draw the key/heading etc.
	 * 
	 */
	public void drawGraph(){
		white=new Color(display, 255, 255, 255);
		lightgrey=new Color(display, 200, 200, 200);
		grey=new Color(display, 154, 154, 154);
		darkgrey=new Color(display, 100, 100, 100);
		black=new Color(display, 0, 0, 0);

		graphArea = drawInfo.getDrawArea();
		drawable.setColor(white);        

		drawSubjects();
		drawBox();

		if(graphNo==proj.getGraphs().size()-1){		
			drawProjectWideParts(); //labels, heading, popGroup key
		}				

		white.dispose();
		grey.dispose();
		black.dispose();
		darkgrey.dispose();
		lightgrey.dispose();
	}

	/**
	 * This method draws the subjects on the graph using the ASubject array's data
	 */

								
	private void drawSubjects() {
	    ASubject subjs [] = drawInfo.getSubjects();
	    for(int i=0; i<subjs.length; i++) {
		ASubject s =  subjs[i];
	        drawSubject(s);
            }
	}

	private void drawSubject(ASubject s) {
		if(!s.getVisible()){
			return;
		}		
		for(Sliver sliv:s.getSlivers()){			
			drawSliver(sliv, s.getSubject().getSelected());			
		}			
	}

	private void drawSliver(Sliver sliv, boolean selected) {	
		drawable.setColor(new Color(display,  
				selected ? oppositeColour(sliv.getColour()) : sliv.getColour()));
		drawable.fillRectangle(sliv.getPosition().x, sliv.getPosition().y, 
				sliv.getPosition().width, sliv.getPosition().height);
		/*drawable.drawRectangle(sliv.getPosition().x, sliv.getPosition().y, 
				sliv.getPosition().width, sliv.getPosition().height, 
				new Color(display, sliv.getColour()));	*/
	}
	
	private RGB oppositeColour(RGB colour) {	
		return new RGB((128+colour.red)%255,(128+colour.green)%255,(128+colour.blue)%255);
	}

	private void drawBox() {
		if(proj.getShowBorder()){	        	
			drawable.setLineWidth(2);
			drawable.setColor(black);
			if(proj.isHorizontal()){
				drawable.drawRectangle(graphArea.x,graphArea.y-1,graphArea.width+2,graphArea.height+2,black);
			}else{
				drawable.drawRectangle(graphArea.x-1,graphArea.y,graphArea.width+2,graphArea.height+2,black);
			}
			
			drawable.setLineWidth(1);
		}

	}

	private void drawProjectWideParts() {
		if(proj.getShowPopLabels()&&proj.hasPheno()){
			drawPopLabels();
		}	

		if(!proj.getHeading().equals("")){
			drawHeading(drawable);
		}

		drawAnnotations();	
		
		drawObjects();
	}
	
	private void drawObjects() {
		Rectangle drawArea=new Rectangle(0,0, 
				proj.getImageWidth(),
				proj.getImageHeight());
		for(DrawObject dO:proj.getDrawObjects()){
			if(dO.getSelected()){
				drawable.setColor(new Color(display, 255, 0, 0));				
			}else{
				drawable.setColor(black);				
			}
			
			try{
				if(dO.getType()==0){
					drawLineObject((Line)dO, drawArea);
				}else if(dO.getType()==1){
					drawArrowObject((Arrow)dO, drawArea);
				}
			}catch(NullPointerException e){
				proj.getDrawObjects().remove(dO);
			}
		}

	}


	void drawArrow(int x1, int y1, int x2, int y2) throws NullPointerException{        
		Point tail=new Point(x1,y1);
		Point head=new Point(x2,y2);           

        drawable.drawLine(tail.x, tail.y, head.x, head.y);        
        drawArrowHead(drawable, head, tail );
       
    }


	private void drawArrowObject(Arrow arrow , Rectangle drawArea) throws NullPointerException{
		drawArrow(arrow.start.x*drawArea.width/1000, arrow.start.y*drawArea.height/1000,
				arrow.end.x*drawArea.width/1000, arrow.end.y*drawArea.height/1000);
		
	}

	private void drawArrowHead(GenesisDrawable drawable, Point head, Point tail) throws NullPointerException {  		
		Point corner1=null;
		Point corner2=null;
		
		int length = 10;
		double phi = Math.toRadians(30);
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



	private void drawLineObject(Line line, Rectangle drawArea) throws NullPointerException {
		drawable.drawLine(line.start.x*drawArea.width/1000, line.start.y*drawArea.height/1000,
				line.end.x*drawArea.width/1000, line.end.y*drawArea.height/1000);


	}

	private void drawAnnotations() {	
		try{
			for(GLabel lbl:proj.getLabels()){	
				setLabelFont(lbl);	
				int width = drawable.textExtent(lbl.getText()).x;
				int height = drawable.textExtent(lbl.getText()).y;

				int imageWidth = proj.getImageWidth();
				int imageHeight = proj.getImageHeight();

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
		}catch(NullPointerException e){}

	}
	
	private void underlineLabel(int startX, int startY, int length, int fontHeight) {				
		drawable.setLineWidth(2);
		drawable.drawLine(startX, startY+fontHeight-1, startX+length, startY+fontHeight-1);				
	}



	private void setLabelFont(GLabel lbl) {
		drawable.setFont(new Font(display,lbl.getFont()));
		drawable.setColor(black);

	}

	private void drawPopLabels() {
		headingMargin = getHeadingMargin();
	    Font useFont = new Font(display,proj.getGroupFont());
	    drawable.setFont(useFont);
	    drawable.setColor(black);
	    for(int i=0;i<proj.getCurrentGroups().length;i++){	
	       drawPopLabelAndLines(i);
	    }
	}

	private void drawPopLabelAndLines(int i) {
		AdmixPopulationGroup pop = proj.getCurrentGroups()[i];
		if(!pop.getVisible()){
			return;
		}
		Rectangle marker = drawInfo.getPopGroupMarkers()[i];

		Point position = getLabelPos(pop,marker);

	    Font useFont = new Font(display,proj.getGroupFont());
	    drawable.setFont(useFont);
	    drawable.setColor(black);
		drawable.drawTransparentText(pop.getDisplayName(), position.x, position.y);
		if(proj.getCurrentGroups()[i].getOrder()!=0){
			drawPopLines(position.x,position.y,marker);			
		}

	}

	private Point getLabelPos(AdmixPopulationGroup pop, Rectangle marker ) {
		if (proj.isHorizontal()){
			return getHorizontalLabelPos(pop,marker);
		}else{
			return getVerticalLabelPos(pop,marker);
		}

	}

	private Point getHorizontalLabelPos(AdmixPopulationGroup pop, Rectangle marker ) {
		int labelWidth = drawable.textExtent(pop.getDisplayName()).x; 		
		int x = drawInfo.getDrawArea().x+marker.x+(marker.width-labelWidth)/2;
		int y = graphArea.height+16+proj.getMargins()[1]+
				(proj.getGraphHeight()+proj.getSeparationDistance())*(proj.getNoRows()-1);		
		if(!proj.getHeading().equals("")){
			y+=headingMargin;
		}else{
			y+=5;
		}
		return new Point(x,y);

	}

	private Point getVerticalLabelPos(AdmixPopulationGroup pop, Rectangle marker) {
		int labelHeight = drawable.textExtent(pop.getDisplayName()).y; 		
		int x=graphArea.width+46+proj.getMargins()[0]+
				(proj.getGraphHeight()+proj.getSeparationDistance())*(proj.getNoRows()-1);
		int y = drawInfo.getDrawArea().y+marker.y+(marker.height-labelHeight)/2;

		

		return new Point(x,y);

	}
	
	private int getHeadingMargin() {
		int margin;
		if(proj.getHeading().equals("")){
			margin=5;
		}else{			
			drawable.setFont(new Font(UI.display,proj.getHeadingFont()));
			margin = drawable.textExtent(proj.getHeading()).y + 10;
		}			
		
		return margin;
	}

	private void drawPopLines(int x,int y,Rectangle marker) {
		if(proj.isHorizontal()){
			drawHorizontalPopLines(y,marker);
		}else{
			drawVerticalPopLines(x,marker);
		}


	}

	private void drawVerticalPopLines(int x, Rectangle marker) {
		int separation = proj.getSeparationDistance();
		int X=x-(proj.getGraphHeight()+separation);
		int leftBound=50;
		
		if(marker.y==1){
			return;
		}
		
		leftBound+=getHeadingMargin()+proj.getMargins()[0];;
		
		while(X>leftBound){

			drawable.drawLine(X+separation-20,graphArea.y+marker.y, X-10, graphArea.y+marker.y);
			X-=proj.getGraphHeight()+separation;
		}

		drawable.drawLine(x-6, graphArea.y+marker.y, x+20, graphArea.y+marker.y);

	}

	private void drawHorizontalPopLines(int y, Rectangle marker) {
		int separation = proj.getSeparationDistance();
		int Y=y-(proj.getGraphHeight()+separation);
		int topBound=50;
		
		if(marker.x==1){
			return;
		}
		
		topBound+=getHeadingMargin()+proj.getMargins()[1];
		
		if(proj.getHeading().equals("")){
			Y+=4;
		}
		
		while(Y>topBound){		
			drawable.drawLine(graphArea.x+marker.x, Y+separation-20, graphArea.x+marker.x, Y-10);
			Y-=proj.getGraphHeight()+separation;
		}

		drawable.drawLine(graphArea.x+marker.x, y-6, graphArea.x+marker.x, y+20);

	}

	private void drawHeading(GenesisDrawable drawable) {
		setGenesisDrawableToHeadingFont();				
		int center = graphArea.x + (graphArea.width/2);
		int fontWidth = drawable.textExtent(proj.getHeading()).x;
		int fontHeight = drawable.textExtent(proj.getHeading()).y;
		drawable.drawText(proj.getHeading(), center-(fontWidth/2), 6);
		underLineheading(center-(fontWidth/2), fontWidth, fontHeight);		
	}
	
	private void underLineheading(int startX, int length, int fontHeight) {
		if(proj.getHeadingUnderline()){
			drawable.setLineWidth(2);
			drawable.drawLine(startX, 6+fontHeight-2, startX+length, 6+fontHeight-2);
		}
		
	}
	
	private void setGenesisDrawableToHeadingFont() {
    	drawable.setColor(black);    	
    	FontData f = proj.getHeadingFont();    	
	Font font = new Font(display, f);				
	drawable.setFont(font);
		
	}




}

