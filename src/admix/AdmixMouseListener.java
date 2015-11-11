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

package admix;



import main.UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;

import admix.drawinfo.DrawInfo;
import drawObjects.DrawObject;
import shared.GLabel;

class AdmixMouseListener implements MouseListener{
    AdmixProj proj;	
    ScrolledComposite compGraph;	

    private boolean dragging;
    private Point draggingFrom;
    private DrawObject draggingObject;

    public AdmixMouseListener(AdmixProj proj, ScrolledComposite compGraph){
	this.proj=proj;
	this.compGraph=compGraph;

    }



    @Override
	public void mouseDown(MouseEvent e) {
	Point relativePt = findPositionOfClick();	

	if(e.button==1){
	    checkDrag(relativePt);
	}
    }

    @Override
    public void mouseUp(MouseEvent e) {
    	proj = UI.ui.findAdmixProjByTab(UI.tabs.getSelection()[0]);
    	Point relativePt = findPositionOfClick();
    	int graphClicked = findIndexOfGraphClicked(relativePt);

    	if(e.button==3){
    		rightClick(relativePt, graphClicked);
    	}else{
    		leftClick(relativePt, graphClicked, e);
    	}

    }



	
    @Override
	public void mouseDoubleClick(MouseEvent e) {
	proj = UI.ui.findAdmixProjByTab(UI.tabs.getSelection()[0]);
	Point relativePt = findPositionOfClick();
	int graphClicked = findIndexOfGraphClicked(relativePt);

	relativePt.x = draggingFrom.x;
	relativePt.y = draggingFrom.y;
	leftClick(relativePt, graphClicked, e);

    }
	
	
	
    private void rightClick(Point relativePt, int graphClicked) {				
	Menu popupMenu=createAdmixPopupMenu(graphClicked,relativePt);
	popupMenu.setEnabled(true);
	popupMenu.setVisible(true);		
    }


    private void popGroupOptions(int graphClicked) {
	AdmixGraphOptions options = new AdmixGraphOptions(proj.getGraphs().get(graphClicked),UI.ui);
	options.run(UI.display);	

    }


    private void baseColourOptions(int graphClicked, int oth) {
    	// SH: this is used to allow user to recolour a graph to 
    	// match the colouring of another graph
    	AdmixGraph curr  = proj.getGraphs().get(graphClicked);
    	AdmixGraph other = proj.getGraphs().get(oth);
    	curr.baseColoursOn(other,proj.getPhenoColumn());
    	UI.ui.drawGraph();
    }


    private void deleteGraph(int index) {
	proj.getGraphs().remove(index);	
	proj.getDrawInfo().remove(index);


    }

    private void promoteGraph(int index) {
	swapGraphs(index,index-1);		

    }

    private void promoteToTop(int index) {
	promoteGraph(index);
	if(index-1!=0){
	    promoteToTop(index-1);
	}
    }

    private void demoteToBottom(int index) {
	demoteGraph(index);
	if(index+1!=proj.getGraphs().size()-1){
	    demoteToBottom(index+1);
	}				
    }

    private void demoteGraph(int graphClicked) {
	swapGraphs(graphClicked,graphClicked+1);		

    }

    private void swapGraphs(int i, int j) {
	AdmixGraph temp = proj.getGraphs().get(i);
	proj.getGraphs().set(i, proj.getGraphs().get(j));
	proj.getGraphs().set(j, temp);
	DrawInfo temp2 = proj.getDrawInfo().get(i);
	proj.getDrawInfo().set(i, proj.getDrawInfo().get(j));
	proj.getDrawInfo().set(j, temp2);

    }

    private Menu createAdmixPopupMenu(int graphClicked, Point relativePt){
	Menu popupMenu = new Menu(UI.mainWindow);
	addLabelMenuItem(popupMenu,relativePt);
	if(graphClicked!=-1){
	    addOptionsMenuItem(popupMenu,graphClicked);
	    if(graphClicked>0) 
		addBaseColorOption(popupMenu,graphClicked,"previous",-1);
	    if(graphClicked+1<proj.getGraphs().size()) 
		addBaseColorOption(popupMenu,graphClicked,"next",1);
	    if(proj.getGraphs().size()>1){
		addOrderMenuItem(popupMenu,graphClicked);				
		addDeleteMenuItem(popupMenu,graphClicked);				
	    }
	}

	return popupMenu;

    }

    private void addOrderMenuItem(Menu popupMenu, int graphClicked){
	MenuItem orderItem = new MenuItem(popupMenu, SWT.CASCADE);
	orderItem.setText("Order");
	orderItem.setMenu(createOrderMenu(graphClicked));
    }

    private Menu createOrderMenu(int graphClicked) {
	Menu orderMenu = new Menu(UI.mainWindow,SWT.DROP_DOWN);
	if(graphClicked!=0){
	    addPromoteMenuItem(orderMenu,graphClicked);
	    addPromoteToTopMenuItem(orderMenu,graphClicked);
	}
	if(graphClicked!=proj.getGraphs().size()-1){
	    addDemoteMenuItem(orderMenu,graphClicked);
	    addDemoteToBottomMenuItem(orderMenu,graphClicked);
	}
	return orderMenu;
    }

    private void addLabelMenuItem(Menu popupMenu, final Point relativePt) {
	MenuItem labelItem = new MenuItem(popupMenu, SWT.NONE);
	labelItem.setText("Create Label at Mouse Cursor");
	labelItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
		    AdmixWorkflow.createAdmixLabelAtMouseCursor(proj,relativePt);
		}
	    });
    }

    private void addOptionsMenuItem(Menu popupMenu, final int graphClicked) {
	MenuItem labelItem = new MenuItem(popupMenu, SWT.NONE);
	labelItem.setText("Population Group Options (Colours)");
	labelItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
		    popGroupOptions(graphClicked);
		}
	    });

    }


    private void addBaseColorOption(Menu popupMenu, final int graphClicked, String strlab, final int delta) {
    	MenuItem labelItem = new MenuItem(popupMenu, SWT.NONE);
    	labelItem.setText("Colour this graph similar to "+strlab);
    	labelItem.addSelectionListener(new SelectionAdapter() {
    		public void widgetSelected(SelectionEvent event) {
    			baseColourOptions(graphClicked,graphClicked+delta);
    		}
    	});

    }




    private void addDeleteMenuItem(Menu popupMenu, final int graphClicked) {
	MenuItem labelItem = new MenuItem(popupMenu, SWT.NONE);
	labelItem.setText("Delete Graph");
	labelItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
		    deleteGraph(graphClicked);
		    UI.ui.drawGraph();
		}
	    });
    }

    private void addDemoteMenuItem(Menu popupMenu, final int graphClicked) {
	MenuItem labelItem = new MenuItem(popupMenu, SWT.NONE);
	if(proj.isHorizontal()){
	    labelItem.setText("Shift Graph Down");
	}else{
	    labelItem.setText("Shift Graph Right");
	}
	labelItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
		    demoteGraph(graphClicked);
		    UI.ui.drawGraph();
		}
	    });
    }

    private void addDemoteToBottomMenuItem(Menu popupMenu, final int graphClicked) {
	MenuItem labelItem = new MenuItem(popupMenu, SWT.NONE);
	if(proj.isHorizontal()){
	    labelItem.setText("Shift Graph To Bottom");
	}else{
	    labelItem.setText("Shift Graph To Right");
	}
	labelItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
		    demoteToBottom(graphClicked);
		    UI.ui.drawGraph();
		}
	    });
    }

    private void addPromoteMenuItem(Menu popupMenu, final int graphClicked) {
	MenuItem labelItem = new MenuItem(popupMenu, SWT.NONE);
	if(proj.isHorizontal()){
	    labelItem.setText("Shift Graph Up");
	}else{
	    labelItem.setText("Shift Graph Left");
	}

	labelItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
		    promoteGraph(graphClicked);
		    UI.ui.drawGraph();
		}
	    });
    }

    private void addPromoteToTopMenuItem(Menu popupMenu, final int graphClicked) {
	MenuItem labelItem = new MenuItem(popupMenu, SWT.NONE);
	if(proj.isHorizontal()){
	    labelItem.setText("Shift Graph to Top");
	}else{
	    labelItem.setText("Shift Graph to Left");
	}

	labelItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
		    promoteToTop(graphClicked);
		    UI.ui.drawGraph();
		}
	    });
    }

	
    private int distance(Point p1, Point p2) {
	return Math.abs(p1.x-p2.x)+Math.abs(p1.y-p2.y);
    }

    private void leftClick(Point relativePt, int graphClicked, MouseEvent e) {	

	if(dragging){
	    if(distance(relativePt, draggingFrom)>0){		
		shiftObject(relativePt, (e.stateMask&SWT.SHIFT) > 0);
		UI.setDefaultCursor();
		return;
	    }
	    UI.setDefaultCursor();
	}

	if(clickedAnnotationAndMakeDialog(relativePt)){
	    return;
	}
	if(clickedDrawObjectAndMakeDialog(relativePt)){
	    return;
	}
	if(graphClicked!=-1){
	    graphClick(proj.getGraphs().get(graphClicked), proj.getDrawInfo().get(graphClicked),relativePt);
	    return;
	}
	if(clickedPopLabels(relativePt)){
	    popLabelsClick(relativePt);
	}

    }

    private boolean clickedDrawObjectAndMakeDialog(Point relativePt) {
	try{
	    for(DrawObject d:proj.getDrawObjects()){
		if(d.clicked(AdmixWorkflow.getLabelRatio(proj, relativePt))){					
		    AdmixWorkflow.drawObjectDialog(d, proj);
		    return true;
		}
	    }			
	}catch(NullPointerException e){

	}
	return false;
    }

    //private int distance(Point p1, Point p2) {			
    //	return Math.abs(p1.x-p2.x)+Math.abs(p1.y-p2.y);
    //}

    private void shiftObject(Point relativePt, boolean snap) {
	draggingObject.shift(relativePt.x-draggingFrom.x, 
			    relativePt.y-draggingFrom.y,
			    proj.getImageWidth(),proj.getImageHeight()); 
	UI.setDefaultCursor();
	if (snap) draggingObject.snap();
	UI.ui.drawGraph();

    }

    private boolean clickedAnnotationAndMakeDialog(Point relativePt) {
	try{
	    for(GLabel l:proj.getLabels()){
		if(l.getPosition().contains(relativePt)){					
		    admixLabelDialog(l);
		    return true;
		}
	    }			
	}catch(NullPointerException e){

	}
	return false;
    }





    protected DrawObject clickedAnnotation(Point relativePt) {
	for(GLabel l:proj.getLabels()){
	    if(l.clicked(relativePt)){
		return l;
	    }
	}

	for(DrawObject d:proj.getDrawObjects()){
	    if(d.clicked(AdmixWorkflow.getLabelRatio(proj,relativePt))){					
		return d;
	    }
	}			
	return null;
    }



    private void admixLabelDialog(GLabel l) {
	AdmixLabelDialog dlg = new AdmixLabelDialog(l);
	dlg.run(UI.display);
	if(dlg.isChanged()){
	    l.setText(dlg.getText());
	}
	if(dlg.isDeleted()){
	    proj.getLabels().remove(l);
	}
	if(dlg.isMoved()){
	    moveLabel(l);
	}
	if(dlg.isFontChanged()){
	    l.setFont(dlg.getFont());
	    l.setUnderlined(dlg.getUnderlined());
	}
	if(dlg.isChanged()||dlg.isDeleted()||dlg.isFontChanged()){
	    UI.ui.drawGraph();
	}

    }



    private void moveLabel(GLabel l) {
	removeMouseListener();
	moveLabelDialog();
	AdmixMoveLabelMouseListener temp = new AdmixMoveLabelMouseListener(l,compGraph);
	proj.getImage().addMouseListener(temp);

    }

    private void moveLabelDialog() {
	MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_INFORMATION);
	messageBox.setMessage("Please click where you would like to move the label to.");
	messageBox.setText("Move Label");
	messageBox.open();	

    }

    private void removeMouseListener() {
	Label img = proj.getImage();

	int[] eventTypes = {SWT.MouseDown, SWT.MouseUp, SWT.MouseDoubleClick,
			    SWT.MouseMove, SWT.MouseEnter, SWT.MouseExit, SWT.MouseHover,
			    SWT.MouseWheel };


	for (int eventType : eventTypes) {

	    Listener[] listeners = img.getListeners(eventType);
	    for (Listener listener : listeners) {
		img.removeListener(eventType, listener);
	    }
	}

    }

    private void popLabelsClick(Point relativePt) {
	AdmixGraph graph = proj.getGraphs().get(0);
	DrawInfo drawInfo = proj.getDrawInfo().get(0);

	if(proj.isHorizontal()){
	    horizontalPopLabelsClick(relativePt,graph,drawInfo);
	}else{
	    verticalPopLabelsClick(relativePt,graph,drawInfo);
	}




    }

    private void horizontalPopLabelsClick(Point relativePt,	AdmixGraph graph, DrawInfo drawInfo) {
	if(!proj.hasPheno()){
	    return;
	}
		
	int x = relativePt.x-31;

	for(int i=0;i<proj.getCurrentGroups().length;i++){
	    AdmixPopulationGroup pop =proj.getCurrentGroups()[i];
	    Rectangle marker = drawInfo.getPopGroupMarkers()[i];
	    if(marker.x+marker.width>=x&&marker.x<x){
		if(AdmixWorkflow.popDialog(pop,proj)){
		    UI.ui.drawGraph();
		}
		break;
	    }
	}

    }

    private void verticalPopLabelsClick(Point relativePt, AdmixGraph graph, DrawInfo drawInfo) {
	if(!proj.hasPheno()){
	    return;
	}
		
	int y = relativePt.y-getYMargin(graph);

	for(int i=0;i<proj.getCurrentGroups().length;i++){
	    AdmixPopulationGroup pop = proj.getCurrentGroups()[i];
	    Rectangle marker = drawInfo.getPopGroupMarkers()[i];
	    if(marker.y+marker.height>=y&&marker.y<y){
		if(AdmixWorkflow.popDialog(pop,proj)){
		    UI.ui.drawGraph();
		}
		break;
	    }
	}

    }

    private int getYMargin(AdmixGraph graph) {
	int margin=31;
	if(!graph.getProj().getHeading().equals("")){
	    margin+=30;
	}
	return margin;
    }

    private boolean clickedPopLabels(Point relativePt) {
	if(!proj.getShowPopLabels()){
	    return false;
	}

	if(proj.isHorizontal()){
	    return clickedHorizontalPopLabels(relativePt);
	}else{
	    return clickedVerticalPopLabels(relativePt);
	}



    }

    private boolean clickedVerticalPopLabels(Point relativePt) {
	int y = relativePt.y-31;
	Rectangle rightGraphArea = proj.getDrawInfo().get(proj.getDrawInfo().size()-1).getDrawArea();
	int rightOfGraphs = rightGraphArea.x+rightGraphArea.width;

	if((relativePt.x>rightOfGraphs+5&&relativePt.x<rightOfGraphs+80)&&
	   (relativePt.y>rightGraphArea.y&&y<=rightGraphArea.y+rightGraphArea.height)){
	    return true;
	}else{
	    return false;
	}

    }

    private boolean clickedHorizontalPopLabels(Point relativePt) {
	int x = relativePt.x-31;
	Rectangle bottomGraphArea = proj.getDrawInfo().get(proj.getDrawInfo().size()-1).getDrawArea();
	int bottomOfGraphs = bottomGraphArea.y+bottomGraphArea.height;
	int labelWidth=proj.getHorizontalPopMargin();

	if((relativePt.y>bottomOfGraphs+5&&relativePt.y<bottomOfGraphs+labelWidth+15)&&
	   (relativePt.x>bottomGraphArea.x&&x<=bottomGraphArea.x+bottomGraphArea.width)){
	    return true;
	}else{
	    return false;
	}

    }

    private void graphClick(AdmixGraph graph, DrawInfo drawInfo, Point relativePt) {

	Point shiftedPoint = new Point(relativePt.x-drawInfo.getDrawArea().x-1,
				       relativePt.y-drawInfo.getDrawArea().y); 


	for(int i =0; i<drawInfo.getSubjects().length;i++){
	    ASubject s=drawInfo.getSubjects()[i];
	    if(!s.getVisible()){
		continue;
	    }

	    Rectangle block = s.getPosition();				

	    if(block.contains(shiftedPoint)){		
		AdmixWorkflow.setAllGraphsSubjectSelected(true,i,proj);				
		UI.ui.drawGraph();
		subjDialog(s,graph);
		AdmixWorkflow.setAllGraphsSubjectSelected(false,i,proj);				
		UI.ui.drawGraph();
		return;
	    }


	}

    }



    private void subjDialog(ASubject subj, AdmixGraph graph) {
	AdmixWorkflow.SubjectDialog(subj,graph);		

    }

    private int findIndexOfGraphClicked(Point relativePt) {

	for(int i=0; i<proj.getDrawInfo().size();i++){
	    Rectangle bounds = proj.getDrawInfo().get(i).getDrawArea();
	    if((relativePt.x>bounds.x&&relativePt.x<=bounds.x+bounds.width+2)&&
	       (relativePt.y>bounds.y&&relativePt.y<bounds.y+bounds.height+2)){
		return i;
	    }
	}
	return -1;
    }

    private Point findPositionOfClick() {
	int imageWidth = proj.getImageWidth();	
	int imageHeight = proj.getImageHeight();

	long xShift = (((long)imageWidth*(long)compGraph.getHorizontalBar().getSelection()*100)/(compGraph.getHorizontalBar().getMaximum()*100));
	long yShift = (((long)imageHeight*(long)compGraph.getVerticalBar().getSelection()*100)/(compGraph.getVerticalBar().getMaximum()*100));
	Point a=UI.display.getCursorLocation();
	Point panelCorner = compGraph.toDisplay(new Point(0,0));
	Point relativePt = new Point((int)xShift+a.x-panelCorner.x+1, (int)yShift+a.y-panelCorner.y);

	return relativePt;
    }

    private void checkDrag(Point relativePt) {
	draggingFrom   = relativePt;
	draggingObject = clickedAnnotation(relativePt);
	if(draggingObject!=null){
	    dragging=true;
	    draggingFrom=relativePt;
	    UI.setDragCursor();
	}else{
	    dragging=false;
	}

    }

    private class AdmixMoveLabelMouseListener implements MouseListener{
	GLabel lbl;	
	ScrolledComposite compGraph;	
	AdmixProj proj;
	boolean moved;

	public AdmixMoveLabelMouseListener(GLabel lbl, ScrolledComposite compGraph){
	    this.lbl=lbl;
	    this.compGraph=compGraph;
	}

	@Override
	    public void mouseDoubleClick(MouseEvent e) {

	}

	@Override
	    public void mouseUp(MouseEvent e) {
	    if(moved){
		UI.ui.drawGraph();
	    }

	}

	@Override
	    public void mouseDown(MouseEvent e) {
	    proj = UI.ui.findAdmixProjByTab(UI.tabs.getSelection()[0]);
	    Point relativePt = findPositionOfClick();			
	    Point ratio = AdmixWorkflow.getLabelRatio(proj, relativePt);
	    if(AdmixWorkflow.checkValidRatio(ratio)){
		lbl.setRatio(ratio);
		moved=true;
	    }

	}

	private Point findPositionOfClick() {
	    int imageWidth = proj.getImageWidth();	
	    int imageHeight = proj.getImageHeight();

	    int xShift = ((imageWidth*compGraph.getHorizontalBar().getSelection()*100)/(compGraph.getHorizontalBar().getMaximum()*100));
	    int yShift = ((imageHeight*compGraph.getVerticalBar().getSelection()*100)/(compGraph.getVerticalBar().getMaximum()*100));
	    Point a=UI.display.getCursorLocation();
	    Point panelCorner = compGraph.toDisplay(new Point(0,0));
	    Point relativePt = new Point(xShift+a.x-panelCorner.x, yShift+a.y-panelCorner.y);

	    return relativePt;
	}
    }

}
