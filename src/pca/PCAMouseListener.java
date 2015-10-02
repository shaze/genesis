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

package pca;

import java.util.ArrayList;

import main.UI;
import shared.GLabel;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PopupList;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;


import pca.dialogs.AxisDialog;
import pca.drawInfo.DrawInfo;
import pca.drawInfo.DrawInfo3D;
import drawObjects.DrawObject;

public class PCAMouseListener implements MouseListener {
	private PCAGraph graph;
	private DrawInfo drawInfo;
	private DrawInfo3D drawInfo3D;
	private PCAProj proj;

	private boolean dragging;
	private Point draggingFrom;
	private DrawObject draggingObject;

	public PCAMouseListener(PCAProj proj) {
		graph=proj.getGraph();
		drawInfo=proj.getDrawInfo();
		drawInfo3D=proj.getDrawInfo3D();
		this.proj=proj;
	}


	@Override
	public void mouseDoubleClick(MouseEvent arg0) {
		leftClick(draggingFrom, arg0);
	}

	@Override
	public void mouseDown(MouseEvent e) {
		Point relativePt = getRelativePoint();	

		if(e.button==1){
			checkDragging(relativePt);
		}

	}

	@Override
	public void mouseUp(MouseEvent e) {				
		Point relativePt = getRelativePoint();				

		if(e.button==3){
			rightClick(relativePt);
		}else{
			leftClick(relativePt, e);
		}		

		dragging=false;

	}	

	private Point getRelativePoint() {
		Point mouseLocation=UI.display.getCursorLocation();
		Point panelCorner = proj.getImg().toDisplay(new Point(0,0));
		Point relativePt = new Point(mouseLocation.x-panelCorner.x,
				mouseLocation.y-panelCorner.y);

		return relativePt;
	}


	private void rightClick(Point relativePt) {

		PopupList popup = createPCAPopup();
		String result=popup.open(getPCAPopupBounds());
		if(result==null){
			return;
		}
		if(result.equals("Create Label at Mouse Cursor")){
			Rectangle drawArea;
			if(graph.is3D()){
				drawArea = drawInfo3D.getDrawArea();
			}else{
				drawArea = drawInfo.getDrawArea();
			}
			PCAWorkflow.createPCALabelAtMouseCursor(graph,drawArea,relativePt);
		}else{
			return; //shouldnt happen
		}
	}

	private Rectangle getPCAPopupBounds() {
		Rectangle bounds = new Rectangle(UI.display.getCursorLocation().x,UI.display.getCursorLocation().y-300,
				220,280);

		return bounds;
	}



	private PopupList createPCAPopup() {
		String[] items = getPCAPopupItems();
		PopupList popup = new PopupList(UI.mainWindow);
		popup.setItems(items);
		return popup;
	}

	private String[] getPCAPopupItems() {
		ArrayList<String> items = new ArrayList<String>();
		items.add("Create Label at Mouse Cursor");

		return items.toArray(new String[items.size()]);
	}


	private void leftClick(Point relativePt, MouseEvent e) {
		Rectangle drawArea;	

		drawArea = graph.z==-1 ? drawInfo.getDrawArea() : drawInfo3D.getDrawArea();			

		if(dragging){
			if(distance(relativePt, draggingFrom)>0){		
				shiftObject(relativePt, drawArea, (e.stateMask&SWT.SHIFT) > 0);					
				return;
			}
		}

		if(clickedAnnotationAndMakeDialog(relativePt))           return;
		if(clickedDrawObjectAndMakeDialog(relativePt, drawArea)) return;
		if(checkGraphClick(relativePt, drawArea))		 return;
		if(checkAxisLabelClick(relativePt, drawArea))		 return;
		checkKeyClick(relativePt, drawArea);

	}

	private boolean clickedDrawObjectAndMakeDialog(Point relativePt, Rectangle drawArea) {

		try{
			for(DrawObject d:graph.getDrawObjects()){
				if(d.clicked(PCAWorkflow.getPointRatio(drawArea,relativePt))){					
					PCAWorkflow.drawObjectDialog(d, graph);
					return true;
				}
			}			
		}catch(NullPointerException e){

		}
		return false;
	}


	private void shiftObject(Point relativePt, Rectangle drawArea, boolean snap) {
		Point p1 = draggingFrom; //PCAWorkflow.getPointRatio(drawArea, draggingFrom);
		Point p2 = relativePt;   //PCAWorkflow.getPointRatio(drawArea, relativePt);
		draggingObject.shift(p2.x-p1.x, p2.y-p1.y, drawArea.width,drawArea.height);	
		UI.setDefaultCursor();
		if (snap) // if shift key snap object
			draggingObject.snap();

		UI.ui.drawGraph();
	}


	private int distance(Point p1, Point p2) {			
		return Math.abs(p1.x-p2.x)+Math.abs(p1.y-p2.y);
	}


	private boolean checkKeyClick(Point relativePt, Rectangle drawArea) {
		int keyWidth=drawInfo.getKeyWidth();

		if(graph.getGroups()!=null&&PCAWorkflow.getNoVisibleGroups(graph)>0){
			if(graph.getKeyPosition()==0){
				int keyHeight=PCAWorkflow.getRightKeyHeight(graph);  
				if((relativePt.x>drawArea.width+drawArea.x+15)&&(relativePt.x<drawArea.width+drawArea.x+15+keyWidth)&&
						(relativePt.y>(drawArea.height+drawArea.y-keyHeight)/2)&&(relativePt.y<(drawArea.height+drawArea.y-keyHeight)/2+keyHeight)){				//
					rightKeyClick(new Point(relativePt.x-(drawArea.width+drawArea.x+15),relativePt.y-((drawArea.height+drawArea.y-keyHeight)/2)));
					return true;
				}
			}else if(graph.getKeyPosition()==1){

				if((relativePt.x>drawInfo.getKeyValues()[0][0])&&(relativePt.x<drawInfo.getKeyValues()[drawInfo.getKeyValues().length-1][1])&&
						(relativePt.y>(drawArea.y + drawArea.height + 35)&&(relativePt.y<(drawArea.y + drawArea.height + 35 + PCAWorkflow.getBottomKeyHeight(graph))))){
					bottomKeyClick(relativePt);
					return true;
				}					
			}
		}	

		return false;
	}


	private boolean checkAxisLabelClick(Point relativePt, Rectangle drawArea) {
		//TODO must check with multiple font sizes 
		if(graph.getShowAxisLabels()&&graph.z==-1){
			if(check2DAxisClick(relativePt, drawArea)){
				return true;
			}		
		}else if(graph.getShowAxisLabels()){
			if(check3DAxisClick(relativePt, drawArea)){
				return true;
			}
		}
		return false;
	}


	private boolean check3DAxisClick(Point relativePt, Rectangle drawArea) {
		try{
			for(int i=0;i<3;i++){						
				Rectangle rect = drawInfo3D.getAxisLabels()[i];
				if(relativePt.x>rect.x&&relativePt.x<rect.x+rect.width&&
						relativePt.y>rect.y&&relativePt.y<rect.y+rect.height){
					axisClick(i,relativePt);
					return true;
				}
			}
			return false;
		}catch(NullPointerException err){
			//somehow this happened once?
			return false;
		}

	}


	private boolean check2DAxisClick(Point relativePt, Rectangle drawArea) {
		int fontHeight = PCAWorkflow.getAxisLabelFontHeight(graph);

		if((relativePt.x>drawArea.x+(drawArea.width/2)-45)&&(relativePt.x<drawArea.x+(drawArea.width/2)+45)
				&&(relativePt.y>drawArea.y+(drawArea.height)+3)&&(relativePt.y<drawArea.y+(drawArea.height)+fontHeight)){
			axisClick(0, relativePt);
			return true;
		}else if((relativePt.x>drawArea.x-fontHeight&(relativePt.x<drawArea.x-2))
				&&(relativePt.y>drawArea.y+(drawArea.height/2)-100)&&(relativePt.y<drawArea.y+(drawArea.height/2)+190)){ 
			axisClick(1, relativePt);
			return true;
		}else{
			return false;
		}

	}


	private boolean checkGraphClick(Point relativePt, Rectangle drawArea) {
		if((relativePt.x>drawArea.x)&&(relativePt.x<drawArea.x+drawArea.width)//check x
				&&(relativePt.y>drawArea.y)&&(relativePt.y<drawArea.y+drawArea.height)){ //check y	
			graphClick(relativePt);
			return true;
		}else{
			return false;
		}

	}



	private boolean clickedAnnotationAndMakeDialog(Point relativePt) {
		for(GLabel l:graph.getLabels()){
			if(l.clicked(relativePt)){
				PCAWorkflow.pcaLabelDialog(proj, l);
				UI.setDefaultCursor();
				return true;
			}
		}
		return false;
	}

	/**This method checks if anything on the graph has been clicked on following
	 * a mouse click event on CompositeGraph
	 * 
	 * @param coords The coordinates of the click
	 */
	private void graphClick(Point coords){
		ArrayList<Subject> subjects=new ArrayList<Subject>();
		Subject[] points;
		if(graph.z!=-1){
			points=drawInfo3D.getPoints();
		}else{
			points=drawInfo.getPoints();
		}

		int count=0;			

		//checks if a subject was clicked
		for(Subject s:points){
			if((Math.abs(s.getPoint().x-coords.x)+(Math.abs(s.getPoint().y-coords.y))<
					(s.getIconSize()+2))){
				if(s.getSubj().getVisible()){
					count++;
					subjects.add(s);
				}												
			}
		}

		if(count==1){
			subjects.get(0).getSubj().setSelected(true);
			UI.ui.drawGraph();
			PCAWorkflow.PCASubjectDialog(subjects.get(0),coords,graph);	
			UI.ui.drawGraph();

			//if more than one subject was clicked 
		}else if (count>1){
			String[] names = new String[subjects.size()];
			for(int x=0;x<subjects.size();x++){
				names[x]=subjects.get(x).getSubj().getName();
			}

			PopupList list = new PopupList(UI.mainWindow);
			list.setItems(names);

			Subject s = PCAWorkflow.findSubjectByName(list.open(new Rectangle(coords.x,coords.y,120,200)),graph,drawInfo,drawInfo3D);
			if(s!=null){
				s.getSubj().setSelected(true);	
				UI.ui.drawGraph();
				PCAWorkflow.PCASubjectDialog(s,coords,graph);
				UI.ui.drawGraph();

			}							
		}
	}

	private void axisClick(int x,Point relativePt) {
		AxisDialog dialog;
		if(x==0){
			dialog = new AxisDialog(graph.getxLabel(),0,relativePt);
			dialog.run(UI.display);
			graph.setxLabel(dialog.getName());
			UI.ui.drawGraph();
		}else if(x==1){
			dialog = new AxisDialog(graph.getyLabel(),1,relativePt);
			dialog.run(UI.display);
			graph.setyLabel(dialog.getName());
			UI.ui.drawGraph();
		}else{
			dialog = new AxisDialog(graph.getzLabel(),2,relativePt);
			dialog.run(UI.display);
			graph.setzLabel(dialog.getName());
			UI.ui.drawGraph();
		}

	}

	private void rightKeyClick(Point modifiedPt) {
		int fontHeight=PCAWorkflow.getKeyFontHeight(graph);	
		int spacing = (fontHeight+6);

		if((modifiedPt.y%spacing>fontHeight/4)&&(modifiedPt.y%spacing<fontHeight)){
			int gpNum= modifiedPt.y/spacing;
			PCAPopulationGroup gp = PCAWorkflow.findVisiblePopWithOrder(gpNum,graph);
			PCAWorkflow.groupDialog(gp,graph);
			UI.ui.drawGraph();
		}


	}

	private void checkDragging(Point relativePt) {
		draggingFrom=relativePt;
		draggingObject = clickedAnnotation(relativePt);
		if(draggingObject!=null){
			dragging=true;
			draggingFrom=relativePt;
			UI.setDragCursor();
		}else{
			dragging=false;
		}

	}



	protected DrawObject clickedAnnotation(Point relativePt) {
		for(GLabel l:graph.getLabels()){
			if(l.clicked(relativePt)){
				return l;
			}
		}

		Rectangle drawArea  = graph.z==-1 ? drawInfo.getDrawArea() : drawInfo3D.getDrawArea();			
		for(DrawObject d:graph.getDrawObjects()){
			if(d.clicked(PCAWorkflow.getPointRatio(drawArea,relativePt))){					
				return d;
			}
		}			
		return null;
	}





	private void bottomKeyClick(Point relativePt) {
		int[][] keyVals=drawInfo.getKeyValues();
		int gpNum=-1;
		for(int i=0;i<keyVals.length;i++){
			if(relativePt.x>keyVals[i][0]&&relativePt.x<keyVals[i][1]){
				gpNum=i;
				break;
			}
		}
		if(gpNum==-1){
			return;
		}

		PCAPopulationGroup gp = PCAWorkflow.findVisiblePopWithOrder(gpNum,graph);				
		PCAWorkflow.groupDialog(gp,graph);

		UI.ui.drawGraph();

	}







}
