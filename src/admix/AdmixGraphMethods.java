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

import java.util.ArrayList;
import main.DeepCopy;
import main.UI;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import admix.drawinfo.DrawInfo;

/** This class contains all the methods for populating the {@link DrawInfo} object using 
 * information from the {@link AdmixGraph}. The relevant objects
 * are only passed in once during construction.
 */
public class AdmixGraphMethods {
    private ArrayList<AdmixGraph> graphs=new ArrayList<AdmixGraph>();
    private ArrayList<DrawInfo> drawInfo=new ArrayList<DrawInfo>();
    private AdmixProj proj;

    /**The constructor of the class.
     * @param graph the {@link AdmixGraph} object
     * @param drawInfo the Admix {@link DrawInfo} object
     * @param shell the shell of the parent
     */
    public AdmixGraphMethods(AdmixProj proj, Shell shell) {
	this.graphs = proj.getGraphs();
	this.drawInfo = proj.getDrawInfo();
	this.proj=proj;
    }

    /** The public method of the class. Takes the info from the AdmixGraph and uses the
     *  methods of the class to generate the
     * necessary info for the {@link DrawInfo} class.
     * 
     * @param size specifies the bounds on which the graph will be drawn
     * 
     */
    public void setDrawInfo(Rectangle size){
	if(proj.isHorizontal()){
	    horizontal(size);
	}else{
	    vertical(size);
	}
			
    }
			
		
    private void vertical(Rectangle size) {
	int maxElements=proj.getNoVisibleSubjects();
	if(proj.hasPheno()){
	    proj.setMembersInGroups(proj.getPhenoColumn());
	    for(int j=0; j<graphs.size(); j++){
		DrawInfo drawInfoJ=drawInfo.get(j);
		AdmixGraph graphJ=graphs.get(j);
		drawInfoJ.setNoAncestors(graphJ.getShownAncestors().length);	
					
		int[] margins = getVerticalMargins(graphJ,drawInfoJ);																																			
		Rectangle graphArea = getVerticalGraphArea(j,graphJ, drawInfoJ, margins[3], margins[1], size, margins[0]);
		drawInfoJ.setDrawArea(graphArea);	
						
						
		int totalElements=graphJ.getAdmixData().length;
					
		int width=proj.getSubjectWidth();				
		drawInfoJ.setSubjectWidth(width);
		drawInfoJ.setHeight(width*maxElements);
					
		int height=graphArea.width;
		if(proj.getCurrentGroups()!=null){
		    setVeritcalPopulationGroupMarkers(width,maxElements,height,graphJ,drawInfoJ);
		}
					
		//create array of Subject objects
		ASubject[] subjects = new ASubject[totalElements];
		int[] count = new int[proj.getCurrentGroups().length];				
		for(int i=0; i<graphJ.getAdmixData().length; i++){
		    int ix = graphJ.getPerm(i);
		    subjects[i]=new ASubject(graphJ.getAdmixData()[ix],
					     graphJ.getAdmixData()[ix].getVisible()&&graphJ.getAdmixData()[ix].getGroups()[proj.getPhenoColumn()].getVisible());
		    if(subjects[i].getVisible()){
			int gpId=graphJ.getAdmixData()[ix].getGroups()[proj.getPhenoColumn()].getID();
			Point marker = new Point(drawInfoJ.getPopGroupMarkers()[gpId].x,
						 drawInfoJ.getPopGroupMarkers()[gpId].y);
										
			Point point = new Point(marker.x,marker.y+count[gpId]);
			count[gpId]+=width;
			Rectangle position;
							
			position=new Rectangle(point.x,point.y,height,width);
			drawInfoJ.setSubjectHeight(height);
												
			Sliver[] sliv = getVerticalSlivers(position,graphJ.getAdmixData()[ix],graphJ,drawInfoJ);
			subjects[i].setSlivers(sliv);
			subjects[i].setPosition(position);
							
		    }
											
		}	
		drawInfoJ.setSubjects(subjects);
	    }
	}else{
	    for(int j=0; j<graphs.size(); j++){
		DrawInfo drawInfoJ=drawInfo.get(j);
		AdmixGraph graphJ=graphs.get(j);
		drawInfoJ.setNoAncestors(graphJ.getShownAncestors().length);	
					
		int[] margins = getVerticalMargins(graphJ,drawInfoJ);																																			
		Rectangle graphArea = getVerticalGraphArea(j,graphJ, drawInfoJ, margins[3], margins[1], size, margins[0]);
		drawInfoJ.setDrawArea(graphArea);	
						
						
		int totalElements=graphJ.getAdmixData().length;
					
		int width=proj.getSubjectWidth();				
		drawInfoJ.setSubjectWidth(width);
		drawInfoJ.setHeight(width*maxElements);
					
		int height=graphArea.width;			
					
		//create array of Subject objects
		ASubject[] subjects = new ASubject[totalElements];
		int count = 0;		
		for(int i=0; i<graphJ.getAdmixData().length; i++){
		    int ix = graphJ.getPerm(i);
		    subjects[i]=new ASubject(graphJ.getAdmixData()[ix],
					     graphJ.getAdmixData()[ix].getVisible());
		    if(subjects[i].getVisible()){
							
			Point marker = new Point(0, 1);							
			Point point = new Point(marker.x,marker.y+count);	

			count+=width;
			Rectangle position;
							
			position=new Rectangle(point.x,point.y,height,width);
			drawInfoJ.setSubjectHeight(height);
												
			Sliver[] sliv = getVerticalSlivers(position,graphJ.getAdmixData()[ix],graphJ,drawInfoJ);
			subjects[i].setSlivers(sliv);
			subjects[i].setPosition(position);
							
		    }
											
		}	
		drawInfoJ.setSubjects(subjects);
	    }
	}
			
    }


    private Rectangle getVerticalGraphArea(int j, AdmixGraph graph, DrawInfo drawInfo, int yRightMargin, 
					   int yMargin, Rectangle size, int xMargin ) {
	int graphThickness = graph.getProj().getGraphHeight();
	int seperation = graph.getProj().getSeparationDistance();
	Rectangle graphArea=new Rectangle(xMargin+j*(graphThickness+seperation), yMargin, 
					  graphThickness, size.height-yRightMargin-yMargin);	
			
	return graphArea;
    }

    private void horizontal(Rectangle size) {
	int maxElements=proj.getNoVisibleSubjects();
	if(proj.hasPheno()){
	    proj.setMembersInGroups(proj.getPhenoColumn());
	    for(int j=0; j<graphs.size(); j++){	  	
		DrawInfo drawInfoJ = drawInfo.get(j);
		AdmixGraph graphJ = graphs.get(j);
		drawInfoJ.setNoAncestors(graphJ.getShownAncestors().length);
													
		int[] margins = getHorizontalMargins(graphJ,drawInfoJ);				
		Rectangle graphArea = getHorizontalGraphArea(j, margins[0], margins[1], margins[2], size);						
		drawInfoJ.setDrawArea(graphArea);
										
		int totalElements=graphJ.getAdmixData().length;
																							
		int width=proj.getSubjectWidth();				
		drawInfoJ.setSubjectWidth(width);
		drawInfoJ.setWidth(width*maxElements);
					
		int height=graphArea.height;
		if(proj.getCurrentGroups()!=null){
		    setHorizontalPopulationGroupMarkers(width,maxElements,height,graphJ,drawInfoJ);
		}											
					
		ASubject[] subjects = new ASubject[totalElements];
					
					
		int[] count = new int[proj.getCurrentGroups().length];				
		for(int i=0;i<graphJ.getAdmixData().length;i++){
		    int ix = graphJ.getPerm(i);
		    subjects[i]=new ASubject(graphJ.getAdmixData()[ix],
					     graphJ.getAdmixData()[ix].getVisible()&&graphJ.getAdmixData()[ix].getGroups()[proj.getPhenoColumn()].getVisible());
		    if(subjects[i].getVisible()){
							
			int gpId = graphJ.getAdmixData()[ix].getGroups()[proj.getPhenoColumn()].getID();
			Point marker = new Point(drawInfoJ.getPopGroupMarkers()[gpId].x,
						 drawInfoJ.getPopGroupMarkers()[gpId].y);
			Point point = new Point(marker.x+count[gpId],marker.y);
			count[gpId]+=width;
			Rectangle position;
							
			position=new Rectangle(point.x,point.y,width,height);
			drawInfoJ.setSubjectHeight(height);
												
			Sliver[] sliv = getHorizontalSlivers(position,graphJ.getAdmixData()[ix],graphJ,drawInfoJ);
			subjects[i].setSlivers(sliv);
			subjects[i].setPosition(position);
		    }
						
						
		}	
		drawInfoJ.setSubjects(subjects);
	    }
	}else{				
	    for(int j=0; j<graphs.size(); j++){			
		DrawInfo drawInfoJ = drawInfo.get(j);
		AdmixGraph graphJ = graphs.get(j);
		drawInfoJ.setNoAncestors(graphJ.getShownAncestors().length);
													
		int[] margins = getHorizontalMargins(graphJ,drawInfoJ);				
		Rectangle graphArea = getHorizontalGraphArea(j, margins[0], margins[1], margins[2], size);						
		drawInfoJ.setDrawArea(graphArea);
									
		int totalElements=graphJ.getAdmixData().length;
																							
		int width=proj.getSubjectWidth();				
		drawInfoJ.setSubjectWidth(width);
		drawInfoJ.setWidth(width*maxElements);
					
		int height=graphArea.height;													
					
		ASubject[] subjects = new ASubject[totalElements];
					
					
		int count=0;			
		for(int i=0;i<graphJ.getAdmixData().length;i++){
		    int ix = graphJ.getPerm(i);
		    subjects[i]=new ASubject(graphJ.getAdmixData()[ix], graphJ.getAdmixData()[ix].getVisible());
		    if(subjects[i].getVisible()){
													
			Point marker = new Point(1, 0);
											
			Point point = new Point(marker.x+count,marker.y);
			count+=width;
			Rectangle position;
							
			position=new Rectangle(point.x,point.y,width,height);
			drawInfoJ.setSubjectHeight(height);
												
			Sliver[] sliv = getHorizontalSlivers(position,graphJ.getAdmixData()[ix],graphJ,drawInfoJ);
			subjects[i].setSlivers(sliv);
			subjects[i].setPosition(position);
		    }
						
						
		}	
		drawInfoJ.setSubjects(subjects);
	    }
	}
			
				
			
    }
			
		
    private Rectangle getHorizontalGraphArea(int index, int xMargin, int yMargin, 
					     int xRightMargin, Rectangle size) {
	int graphThickness = proj.getGraphHeight();
	int seperation = proj.getSeparationDistance();
	Rectangle graphArea=new Rectangle(xMargin, yMargin+index*(graphThickness+seperation), 
					  size.width-xRightMargin-xMargin,graphThickness);		
			
	return graphArea;
    }

    /**This converts the bounds of an admixture column and its respective
     * {@link AdmixSubject} into an array of {@link Sliver}
     * 
     * @param position bounds of an admixture column
     * @param subject the respective AdmixSubject
     * @return the array of Slivers
     */
    private Sliver[] getHorizontalSlivers(Rectangle position, AdmixSubject subject,
					  AdmixGraph graph, DrawInfo drawInfo){
	Sliver[] slivers = new Sliver[drawInfo.getNoAncestors()];
	int y = drawInfo.getDrawArea().y + position.y;
	int[] order = getAncestorOrder(drawInfo,graph);
			
	float total=0;
	for(int i:graph.getShownAncestors()){
	    total+=subject.getPercent(i);
	}
			
	int count=0;
	for(int i : order){		
	    float ratio = subject.getPercent(i);
	    int slivX=position.x+drawInfo.getDrawArea().x;
	    //because of the rounding up with Math.ceil, sometimes y can be below the bottom
	    //of the graph so we check for that in the following line
	    int slivY=Math.min(y, drawInfo.getDrawArea().y+drawInfo.getDrawArea().height);
	    //to calculate the height of the sliver, we check the expected height (*) but
	    //becuase of the rounding ups with Math.Ceil, sometimes the expected height can 
	    //be 1 or 2 pixels too long and thus we account for that with **.
	    int slivHeight = Math.min(
				      (int) Math.ceil((ratio/total)*(float)position.height), //*
				      drawInfo.getSubjectHeight()-slivY+drawInfo.getDrawArea().y); //**
								
	    slivers[count]=new Sliver();
	    slivers[count].setColour(getAncestorByID(graph,i).getColour());				
	    slivers[count].setPosition(new Rectangle(slivX,slivY,position.width,slivHeight));
				
	    y+=(Math.ceil((ratio/total)*(float)position.height));
				
	    count++;
	}				
			
					
	return slivers;
    }
		
    /**This converts the bounds of an admixture column and its respective
     * {@link AdmixSubject} into an array of {@link Sliver}
     * 
     * @param position bounds of an admixture column
     * @param subject the respective AdmixSubject
     * @return the array of Slivers
     */
    private Sliver[] getVerticalSlivers(Rectangle position, AdmixSubject subject,
					AdmixGraph graph, DrawInfo drawInfo){
								
	Sliver[] slivers = new Sliver[drawInfo.getNoAncestors()];
	int x = drawInfo.getDrawArea().x + position.x;
	int[] order = getAncestorOrder(drawInfo,graph);
			
	float total=0;
	for(int i:graph.getShownAncestors()){
	    total+=subject.getPercent(i);
	}
			
	int count=0;
	for(int i : order){	
	    float ratio = subject.getPercent(i);
				
	    int slivY=position.y+drawInfo.getDrawArea().y;
	    //because of the rounding up with Math.ceil, sometimes x can be right of the 
	    //rightmost of the graph so we check for that in the following line
	    int slivX=Math.min(x, drawInfo.getDrawArea().x+drawInfo.getDrawArea().width);
	    //to calculate the width of the sliver, we check the expected height (*) but
	    //becuase of the rounding ups with Math.Ceil, sometimes the expected height can 
	    //be 1 or 2 pixels too long and thus we account for that with **.
	    int slivWidth = Math.min(
				     (int) Math.ceil((ratio/total)*(float)position.width), //*
				     drawInfo.getSubjectHeight()-slivX+drawInfo.getDrawArea().x); //**
											
	    Rectangle slivPos=new Rectangle(slivX,slivY,slivWidth,position.height);								

	    slivers[count]=new Sliver();
	    slivers[count].setColour(getAncestorByID(graph,i).getColour());														
	    slivers[count].setPosition(slivPos);
				
	    x+=(Math.ceil((ratio/total)*(float)position.width));
				
	    count++;
	}				
								
	return slivers;
    }
		
    private Ancestor getAncestorByID(AdmixGraph graph,int ID){
	for(Ancestor a:graph.getAncestors()){
	    if (a.getID()==ID){
		return a;
	    }
	}
	return null;
    }
		
    private int[] getAncestorOrder(DrawInfo drawInfo,AdmixGraph graph){
	int[] order = new int[drawInfo.getNoAncestors()];
	for(int i=0;i<drawInfo.getNoAncestors();i++){
				
	}
			
	Ancestor[] shownAnc=new Ancestor[drawInfo.getNoAncestors()];
	int count=0;
	for(Ancestor a:graph.getAncestors()){
	    for(int i : graph.getShownAncestors()){
		if(a.getID()==i){
		    shownAnc[count]=a;
		    count++;
		    break;
		}
	    }						
	}

			
	for(int i=0;i<drawInfo.getNoAncestors()-1;i++){
	    for(int j=i+1;j<drawInfo.getNoAncestors();j++){
		if (shownAnc[i].getOrder()>shownAnc[j].getOrder()){					
		    Ancestor temp = shownAnc[i];
		    shownAnc[i]=shownAnc[j];
		    shownAnc[j]=temp;
		}
	    }
	}
			
	count=0;
	for(Ancestor a:shownAnc){
	    order[count]=a.getID();
	    count++;
	}
			
				
	return order;
    }


		
    /** This calculates and sets where the first individual of each 
     * {@link AdmixPopulationGroup} will be located in the {@link DrawInfo}
     * @param noInEachRow the number of elements in each row (only one row for now)
     * @param width the width of each admix column
     * @param maxElements the maximum number of elements in any row (only one row for now)
     * @param height the height of each admix column
     */
    private void setHorizontalPopulationGroupMarkers(int width,int maxElements,int height, 
						     AdmixGraph graph, DrawInfo drawInfo) {		
    int[] count = getStartingPos(width,maxElements,drawInfo);		
	Rectangle[] markers = new Rectangle[proj.getCurrentGroups().length];
	for(int i=0;i<proj.getCurrentGroups().length;i++){	
	    AdmixPopulationGroup pop = proj.findPopGroupByOrder(i);
	    markers[pop.getID()]=new Rectangle(count[pop.getRow()],pop.getRow()*height,
					       width*pop.getNoVisibleMembers(),height);
	    count[pop.getRow()]+=pop.getNoVisibleMembers()*width;	
	}	
	drawInfo.setPopGroupMarkers(markers);
    }
		
    private void setVeritcalPopulationGroupMarkers(int width,int maxElements,int height,AdmixGraph graph,DrawInfo drawInfo) {		
	int[] count = getStartingPos(width,maxElements,drawInfo);		
	Rectangle[] markers = new Rectangle[proj.getCurrentGroups().length];
	for(int i=0;i<proj.getCurrentGroups().length;i++){
	    AdmixPopulationGroup pop = proj.findPopGroupByOrder(i);				
	    markers[pop.getID()]=new Rectangle(pop.getRow()*height, count[pop.getRow()],
					       height, width*pop.getNoVisibleMembers());
	    count[pop.getRow()]+=pop.getNoVisibleMembers()*width;	
				
	}	
	drawInfo.setPopGroupMarkers(markers);
    }
		
    private int[] getStartingPos(int width,int maxElements,DrawInfo drawInfo){
	int[] result = new int[1];
			
	result[0]=(drawInfo.getDrawArea().width-((maxElements*drawInfo.getDrawArea().width)/maxElements))/2+1;			
			
	return result;
    }
		
		
		
	
		
    /**This method gets size of the space to be left on the sides of
     * the graph to draw the key etc. does not include space for the heading
     * 
     * @return int array [left margin, top margin, right margin, bottom margin]
     */
    private int[] getHorizontalMargins(AdmixGraph graph,DrawInfo drawInfo){			
	int headingMargin = getHeadingMargin(graph);
			
	int[] margins = DeepCopy.copyIntArray(graph.getProj().getMargins());
	margins[0]+=30;
	margins[1]+=headingMargin;
	margins[2]+=30;
	margins[3]+=30;						
						
	//extra space for pop label at bottom 
	if(graph.getProj().getShowPopLabels()){
	    GC gc = new GC(UI.display);
	    gc.setFont(new Font(UI.display,graph.getProj().getGroupFont()));
	    margins[3]+=gc.textExtent("p").y;
	    gc.dispose();
	}

	return margins;	
			
    }
		
    private int[] getVerticalMargins(AdmixGraph graph,DrawInfo drawInfo){			
	int headingMargin = getHeadingMargin(graph);
			
	int[] margins = DeepCopy.copyIntArray(graph.getProj().getMargins());
	margins[0]+=30;
	margins[1]+=headingMargin;
	margins[2]+=30;
	margins[3]+=30;									

	return margins;	
			
    }
		
		
		
    private int getHeadingMargin(AdmixGraph graph) {
	int margin;
	if(graph.getProj().getHeading().equals("")){
	    margin=10;
	}else{
	    GC gc = new GC(UI.mainWindow);
	    gc.setFont(new Font(gc.getDevice(),graph.getProj().getHeadingFont()));
	    margin = gc.textExtent(graph.getProj().getHeading()).y + 10;
	    gc.dispose();
	}			
			
	return margin;
    }
}
