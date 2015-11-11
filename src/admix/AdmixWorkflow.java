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

import genesisDrawable.SWTCanvas;

import java.util.ArrayList;
import java.util.LinkedList;

import main.UI;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PopupList;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
//import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
//import org.eclipse.swt.graphics.GC;
//import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
//import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import admix.drawinfo.DrawInfo;
import drawObjects.Arrow;
import drawObjects.DrawObject;
import drawObjects.Line;
import shared.GLabel;
import shared.InputBox;

public class AdmixWorkflow {



	/*public static void createPopGroups(AdmixGraph graph){

      boolean unphenod=false;
      LinkedList<String> names = new LinkedList<String>();

      for(AdmixSubject a:graph.getAdmixData()){
      try{
      String gpName = a.getPhenotypeData()[graph.getProj().getPhenoColumn()]; 

      if(!names.contains(gpName)){
      names.add(gpName);				
      }
      }catch(NullPointerException e){
      a.setPhenotypeData(new String[]{a.getName().split(" ")[0],a.getName().split(" ")[1],"No group"});
      if (!unphenod){										
      unphenod=true;
      }
      }catch(ArrayIndexOutOfBoundsException e){
      a.setPhenotypeData(new String[]{a.getName().split(" ")[0],a.getName().split(" ")[1],"No group"});
      if (!unphenod){										
      unphenod=true;
      }
      }
      }
      if(unphenod){
      names.add("No group");
      }
      AdmixPopulationGroup[] popGroups = new AdmixPopulationGroup[names.size()];
      for(int i=0;i<names.size();i++){
      popGroups[i]=new AdmixPopulationGroup(i, names.get(i),i);
      }


      graph.setGroups(popGroups);
      setToGroups(proj, phenoColumn);

      if(!checkSavedGroups(graph.getProj())){
      AdmixWorkflow.updateSavedGroups(graph.getProj());
      }

      }*/






	/*private static boolean checkSavedGroups(AdmixProj proj){
      ArrayList<SavedGroupNames> savedNames = proj.getSavedGroupNames();
      for(SavedGroupNames saved: savedNames){
      if(saved.column==proj.getPhenoColumn()){
      loadSavedNames(proj.getGraphs().get(0).getGroups(), saved);
      return true;
      }
      }
      return false;
      }

      private static void loadSavedNames(AdmixPopulationGroup[] groups, SavedGroupNames saved) {
      for(int i=0; i<groups.length; i++){
      groups[i].setDisplayName(saved.names[i]);
      groups[i].setOrder(saved.order[i]);
      }

      }

      private static void updateSavedGroups(AdmixProj proj) {		
      ArrayList<SavedGroupNames> savedNames = proj.getSavedGroupNames();		

      //remove old entries of this column
      Iterator<SavedGroupNames> iter = savedNames.iterator();
      while (iter.hasNext()) {
      SavedGroupNames names = iter.next();

      if(names.column==proj.getPhenoColumn()){
      iter.remove();
      }
      }

      //add new entry
      String[] names=createSavedGroupNamesList(proj);
      int[] orders=createSavedGroupOrdersList(proj);
      savedNames.add(new SavedGroupNames(proj.getPhenoColumn(), names, orders));		

      }



      private static String[] createSavedGroupNamesList(AdmixProj proj) {
      AdmixPopulationGroup[] popGroups = proj.getGraphs().get(0).getGroups();
      String[] names=new String[popGroups.length];
      for(int i=0;i<popGroups.length;i++){
      names[i]=popGroups[i].getDisplayName();
      }		
      return names;
      }

      private static int[] createSavedGroupOrdersList(AdmixProj proj) {
      AdmixPopulationGroup[] popGroups = proj.getGraphs().get(0).getGroups();
      int[] orders=new int[popGroups.length];
      for(int i=0;i<popGroups.length;i++){
      orders[i]=popGroups[i].getOrder();
      }		
      return orders;
      }*/


	public static boolean popDialog(AdmixPopulationGroup group, AdmixProj proj){

		PopDialog pd=new PopDialog(group, proj);			
		pd.run(UI.display);						
		if(pd.getCancelled()){
			return false;
		}
		setNewName(group,proj,pd.getName());

		setGroupVisibility(group, proj, pd.getDeleted());
		/*AdmixWorkflow.updateSavedGroups(graphs.get(0).getProj());*/

		return true;


	}

	private static void setGroupVisibility(AdmixPopulationGroup group, AdmixProj proj, boolean deleted) {
		proj.findPopGroupByOrder(group.getOrder()).setVisible(!deleted);		
	}

	private static void setNewName(AdmixPopulationGroup group, AdmixProj proj, String name) {			
		proj.findPopGroupByOrder(group.getOrder()).setDisplayName(name);
	}



	public static void groupDialog(Ancestor ancestor, AdmixGraph graph,RGB[] customCols) {	

		AdmixGroupDialog gd=new AdmixGroupDialog(ancestor,graph, customCols);

		gd.run(UI.display);
		if(gd.getCancelled()){
			return;
		}
		if(gd.colourWasChanged()){
			ancestor.setColour(gd.getColour());
		}
		if(gd.nameWasChanged()){
			ancestor.setDisplayName(gd.getName());
		}



	}

	public static Ancestor findAncestorWithOrder(int order,AdmixGraph graph){
		for(Ancestor p:graph.getAncestors()){
			if(p.getOrder()==order){
				return p;
			}
		}
		return null;
	}


	public static Ancestor findAncestorWithID(int ID,AdmixGraph graph){
		for(Ancestor p:graph.getAncestors()){
			if(p.getID()==ID){
				return p;
			}
		}
		return null;
	}


	//this increases the order field of the anc group
	public static void shiftAnc(Ancestor group,AdmixGraph graph,  int delta){
		boolean found=false;
		int     order;
		order=group.getOrder();
		Ancestor swap;
		int count=order;
		swap=null;
		while(! found){ // loop until we get a visible group
			if ((delta<0) && (order==0) ||
					(delta>0) && (order==graph.getMaxVisibleOrder())) return;
			order=order+delta;
			swap= findAncestorWithOrder(order,graph);
			for(int i:graph.getShownAncestors())
				if(i==swap.getID())
					found=true;
		}
		group.setOrder(count);
		swap.setOrder(order);
	}


	//this increases the order field of the anc group
	public static void shiftAncUp(Ancestor group,AdmixGraph graph){
		int order=group.getOrder();
		boolean found;
		Ancestor swap=null;

		if(order==0) return ;  //already highest order
		int count=order;
		found = false;
		while(! found){				
			count--;
			swap= findAncestorWithOrder(count,graph);
			for(int i:graph.getShownAncestors()){
				if(i==swap.getID()){
					found=true;
					break;
				}					
			}
			if(count<-50){
				return; //shouldnt happen but just in case
			}
		}

		group.setOrder(count);
		swap.setOrder(order);
	}




	//this decreases the order field of the anc group
	public static void shiftAncDown(Ancestor group,AdmixGraph graph){
		int  count, order;
		boolean found=false;
		Ancestor swap=null;//need to init because although swap 
		// wll always get a value in the while loop the compiler is
		// is not clever enough to see this

		order = count = group.getOrder();
		if(order==graph.getAncestors().length-1){ //already lowest order
			return;
		}

		while(! found){				
			count++;				
			swap= findAncestorWithOrder(count,graph);
			for(int i:graph.getShownAncestors()){
				if(i==swap.getID()){
					found=true;
					break;
				}					
			}
			if(count>70)  return; //shouldnt happen but just in case
		}

		group.setOrder(count);
		swap.setOrder(order);
	}




	//this increases the order field of the pop group





	static void shiftPopUp(AdmixPopulationGroup group,AdmixProj proj){
		int order=group.getOrder();
		if(order==0){ //already highest order
			return;
		}
		AdmixPopulationGroup swap = proj.findPopGroupByOrder(order-1);
		group.setOrder(order-1);
		swap.setOrder(order);
	}


	//this decreases the order field of the pop group
	static void shiftPopDown(AdmixPopulationGroup group,AdmixProj proj){
		int order=group.getOrder();
		if(order==proj.getGroups()[proj.getPhenoColumn()].length-1){ //already lowest order3
			return;
		}
		AdmixPopulationGroup swap = proj.findPopGroupByOrder(order+1);			
		group.setOrder(order+1);
		swap.setOrder(order);

	}

	public static boolean checkValidRatio(Point ratio) {
		if(ratio.x>1000||ratio.x<0||ratio.y>1000||ratio.y<0){
			return false;
		}

		return true;
	}

	static void createAdmixLabelAtMouseCursor(AdmixProj proj, Point point) {		
		InputBox ib= new InputBox(UI.mainWindow);
		ib.run();
		String name = ib.getName();
		FontData font = ib.getFont();
		Boolean underlined = ib.getUnderlined();

		if(name==null||name.trim().equals("")){
			return;
		}
		name=name.trim();
		Point ratio = getLabelRatio(proj, point);
		if(checkValidRatio(ratio)){
			createAndAddAdmixLabel(proj, name, font,underlined,ratio);			
			UI.ui.drawGraph();
		}else{
			invalidPlacementMessageBox();
		}



	}

	private static void invalidPlacementMessageBox() {
		MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_ERROR);
		messageBox.setMessage("Invalid Label Placement.");
		messageBox.setText("Sorry!");
		messageBox.open();	

	}



	private static void createAndAddAdmixLabel(AdmixProj proj, String name, 
			FontData font, Boolean underlined, Point ratio) {
		GLabel lbl = new GLabel();
		lbl.setText(name);
		lbl.setRatio(new Point(ratio.x,ratio.y));
		lbl.setFont(font);
		lbl.setUnderlined(underlined);

		proj.getLabels().add(lbl);

	}

	static Point getLabelRatio(AdmixProj proj, Point point){
		int imageWidth = proj.getImageWidth();	
		int imageHeight = proj.getImageHeight();

		int xRatio = (1000*(point.x))/imageWidth;
		int yRatio = (1000*(point.y))/imageHeight;
		return new Point(xRatio,yRatio);
	}


	public static AdmixProj newAdmix() {
		AdmixWizard wiz = new AdmixWizard('n');
		WizardDialog dialog = new WizardDialog(UI.display.getActiveShell(),wiz);
		dialog.open();
		if(wiz.finished){
			AdmixProj proj = wiz.proj;
			setAdmixAxesOptions(proj,wiz.options);		

			if(wiz.headingFont!=null){
				proj.setHeadingFont(wiz.headingFont);
				proj.setHeadingUnderline(wiz.headingUnderline);
			}	
			if(wiz.popFont!=null){
				proj.setGroupFont(wiz.popFont);
			}
			proj.setHeading(wiz.heading);
			proj.setGraphHeight(wiz.graphHeight);
			proj.setSeparationDistance(wiz.separationDistance);
			proj.setSubjectWidth(wiz.subjectWidth);
			proj.setHorizontal(wiz.horizontal);
			proj.setMargins(wiz.margins);
			if(wiz.phenoColumn!=proj.getPhenoColumn()){
				proj.setPhenoColumn(wiz.phenoColumn);	
			}

			//show ALL ancestors
			for(AdmixGraph graph:proj.getGraphs()){
				int[] anc = new int[graph.getAncestors().length];
				for(int i=0;i<anc.length;i++){
					anc[i]=i;
				}
				graph.setShownAncestors(anc);
			}


			return proj;
		}else{
			return null;
		}
	}

	private static void setAdmixAxesOptions(AdmixProj proj, boolean[] options) {
		proj.setShowBorder(options[0]);
		proj.setShowPopLabels(options[1]);
		for(AdmixGraph g : proj.getGraphs()){
			g.setDrawBox(options[0]);
			g.setGroupLabels(options[1]);
		}

	}

	public static void OptionsWizard(AdmixProj proj) {

		AdmixWizard wiz = new AdmixWizard('o',proj);
		WizardDialog dialog = new WizardDialog(UI.display.getActiveShell(),wiz);
		dialog.open();
		if(wiz.finished){
			AdmixProj admixProj = wiz.proj;
			setAdmixAxesOptions(admixProj,wiz.options);	

			if(wiz.headingFont!=null){
				admixProj.setHeadingFont(wiz.headingFont);
				proj.setHeadingUnderline(wiz.headingUnderline);
			}		
			if(wiz.popFont!=null){
				admixProj.setGroupFont(wiz.popFont);
			}
			admixProj.setGraphHeight(wiz.graphHeight);
			admixProj.setSeparationDistance(wiz.separationDistance);
			admixProj.setSubjectWidth(wiz.subjectWidth);
			admixProj.setHeading(wiz.heading);			
			admixProj.setHorizontal(wiz.horizontal);
			admixProj.setMargins(wiz.margins);			
			if(wiz.phenoColumn!=admixProj.getPhenoColumn()){				
				changePhenoColumn(admixProj, wiz.phenoColumn);								
			}

			//show ALL ancestors
			for(AdmixGraph graph:admixProj.getGraphs()){
				int[] anc = new int[graph.getAncestors().length];
				for(int i=0;i<anc.length;i++){
					anc[i]=i;
				}
				graph.setShownAncestors(anc);
			}
			UI.ui.drawGraph();

		}


	}

	/**
	 * Opens the Data Options menu for the given {@link AdmixProj} and
	 * sets all the changes when the menu is closed
	 * @param proj the given {@link AdmixProj}
	 */
	public static void dataOptions(AdmixProj proj) {
		AdmixWizard wizard = new AdmixWizard('i',proj);
		WizardDialog dialog = new WizardDialog(UI.display.getActiveShell(),wizard);
		dialog.open();
		if(wizard.finished){	

			if(wizard.phenoColumn!=proj.getPhenoColumn()){				
				changePhenoColumn(proj, wizard.phenoColumn);								
			}
			for(AdmixGraph graph:proj.getGraphs()){
				int[] anc = new int[graph.getAncestors().length];
				for(int i=0;i<anc.length;i++){
					anc[i]=i;
				}
				graph.setShownAncestors(anc);
			}
			UI.ui.drawGraph();	
		}

	}

	private static void changePhenoColumn(AdmixProj proj, int newCol) {
		int oldCol = proj.getPhenoColumn();
		proj.setPhenoColumn(newCol);
		int[] orders = getNewOrdering(proj, oldCol, newCol);	
		setNewOrders(proj, orders, newCol);

	}

	private static int[] getNewOrdering(AdmixProj proj, int oldCol, int newCol) {
		ArrayList<Integer> orders = new ArrayList<Integer>();
		int p=0;
		for(int i=0; p < proj.getGroups()[newCol].length; i++) {
			// Find the i-th group in current ordering of old phenotype
			AdmixPopulationGroup group = proj.findPopGroupByOrder(i, oldCol);
			// Find an individual of that type
			for (AdmixSubject s : proj.getGraphs().get(0).getAdmixData()) {
				if (s.getGroups()[oldCol].equals(group)) {
					// Get group of that person according to new phenotype
					if (!orders.contains(s.getGroups()[newCol].getID())) {
						orders.add(s.getGroups()[newCol].getID());
						p++;
					}
				}
			}
		}

		Integer[] boxedOrders= orders.toArray(new Integer[orders.size()]);
		return unbox(boxedOrders);
	}

	private static int[] unbox(Integer[] boxedOrders) {
		int[] orders = new int[boxedOrders.length];
		for(int i=0; i<boxedOrders.length; i++){
			orders[i]=boxedOrders[i];
		}
		return orders;
	}

	private static void setNewOrders(AdmixProj proj, int[] orders, int newCol) {	
		for(int i=0;i<orders.length;i++){
			int id=orders[i];
			proj.findPopGroupByID(id, newCol).setOrder(i);
		}
	}

	static Point getLabelRatio(Rectangle drawArea, Point point){
		int imageWidth = drawArea.width;	
		int imageHeight = drawArea.height;

		int xRatio = (1000*(point.x))/imageWidth;
		int yRatio = (1000*(point.y))/imageHeight;
		return new Point(xRatio,yRatio);
	}

	private static class AdmixDragArrowPosListener implements MouseListener{	
		private AdmixProj proj;
		private Point start;
		private Point end;
		private ScrolledComposite compGraph;


		public AdmixDragArrowPosListener(AdmixProj proj){
			this.proj=proj;			
			this.compGraph= (ScrolledComposite)proj.getTab().getControl();

		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {

		}

		@Override
		public void mouseUp(MouseEvent e) {

			Point relativePt = findPositionOfClick();	

			end = getLabelRatio(proj, relativePt);


			if ((e.stateMask&SWT.SHIFT) > 0) {
				// If mouse button pressed then want vertical or horizonal line only
				// work out which
				if (Math.abs(start.x-end.x) < Math.abs(start.y-end.y)) {
					// x coords should be the same
					end.x = start.x ;
				} else
					end.y = start.y ;
			}

			proj.getDrawObjects().add(new Arrow(start, end));
			removeMouseListener(proj.getImage());
			UI.setDefaultCursor();
			UI.ui.drawGraph();

		}

		@Override
		public void mouseDown(MouseEvent e) {		
			Point relativePt = findPositionOfClick();	

			UI.setCrossCursor();
			start = getLabelRatio(proj, relativePt);
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
	}

	private static class AdmixDragLinePosListener implements MouseListener{	
		private AdmixProj proj;
		private Point start;
		private Point end;
		private ScrolledComposite compGraph;


		public AdmixDragLinePosListener(AdmixProj proj){
			this.proj=proj;			
			this.compGraph= (ScrolledComposite)proj.getTab().getControl();

		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {

		}

		@Override
		public void mouseUp(MouseEvent e) {

			Point relativePt = findPositionOfClick();	

			end = getLabelRatio(proj, relativePt);

			if ((e.stateMask&SWT.SHIFT) > 0) {
				// If mouse button pressed then want vertical or horizonal line only
				// work out which
				if (Math.abs(start.x-end.x) < Math.abs(start.y-end.y)) {
					// x coords should be the same
					end.x = start.x ;
				} else
					end.y = start.y ;
			}


			proj.getDrawObjects().add(new Line(start, end));
			removeMouseListener(proj.getImage());
			UI.ui.drawGraph();
			UI.setDefaultCursor();

		}

		@Override
		public void mouseDown(MouseEvent e) {		
			Point relativePt = findPositionOfClick();	
			UI.setCrossCursor();				
			start = getLabelRatio(proj, relativePt);


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
	}




	public static AdmixSubject findAdmixSubject(AdmixGraph g, String name) {
		for(AdmixSubject s : g.getAdmixData()){
			if(s.getName().equals(name)){
				return s;
			}
		}
		return null;
	}



	public static void SubjectDialog(ASubject subj, AdmixGraph graph) {

		admix.SubjectDialog sd=new admix.SubjectDialog(subj);

		sd.run(UI.display);
		if(!sd.isCancelled()){
			checkDeleted(subj, graph,sd.isDeleted());
		}


	}



	private static void checkDeleted(ASubject subj, AdmixGraph graph, boolean deleted) {
		boolean oldVis = subj.getSubject().getVisible();
		if(oldVis==deleted){
			for(AdmixGraph g: graph.getProj().getGraphs()){
				AdmixSubject subject=findSubjectByName(subj.getSubject().getName(),g);
				subject.setVisible(!deleted);
				if(mustRecalculateVisibleMembers(oldVis,deleted)){
					graph.getProj().setMembersInGroups(graph.getProj().getPhenoColumn());
				}				
			}								

		}
	}


	private static boolean mustRecalculateVisibleMembers(boolean oldVis, boolean deleted) {		
		return (oldVis==true&&deleted)||(!oldVis==true&&!deleted);
	}

	private static AdmixSubject findSubjectByName(String name, AdmixGraph g) {
		for(AdmixSubject s:g.getAdmixData()){
			if(s.getName()==name){
				return s;
			}
		}
		return null;
	}

	private static ASubject findASubjectByName(String name, DrawInfo d) {
		for(ASubject s:d.getSubjects()){
			if(s.getSubject().getName()==name){
				return s;
			}
		}
		return null;
	}

	public static void admixSearch(AdmixProj proj) {


		String search="";
		InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(),
				"", "Enter Subject name", "", new LengthValidator());
		if (dlg.open() == Window.OK) {
			// User clicked OK; update the label with the input
			search=(dlg.getValue());
		}else{
			return;
		}

		ASubject[] subjects= proj.getDrawInfo().get(0).getSubjects();

		ArrayList<ASubject> subjs = getMatchingSubjects(search,subjects);

		if(subjs.size()==1){		
			selectSubject(subjs.get(0),proj);			
			return;	
		}else if(subjs.size()>1){
			String[] names = new String[subjs.size()];
			for(int x=0;x<subjs.size();x++){
				names[x]=subjs.get(x).getSubject().getName();
			}

			PopupList list = new PopupList(UI.mainWindow);
			list.setItems(names);

			String chosen=list.open(new Rectangle(UI.display.getBounds().width/2-50,
					UI.display.getBounds().height/2-50,250,50));
			ASubject s = findASubjectByName(chosen,proj.getDrawInfo().get(0));
			if(s!=null){
				selectSubject(s,proj);					
			}
		}else{
			MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_INFORMATION);
			messageBox.setMessage("Couldn't find "+search+" in the data.");
			messageBox.setText("Sorry!");
			messageBox.open();		
		}

	}


	private static void selectSubject(ASubject s,AdmixProj proj) {
		int index=getIndexOfSubject(s, proj);
		setAllGraphsSubjectSelected(true, index, proj);				
		UI.ui.drawGraph();
		SubjectDialog(s, proj.getGraphs().get(0));
		setAllGraphsSubjectSelected(false, index, proj);
		UI.ui.drawGraph();

	}

	private static int getIndexOfSubject(ASubject s, AdmixProj proj) {
		for(int i=0;i<proj.getDrawInfo().get(0).getSubjects().length;i++){
			if(s.equals(proj.getDrawInfo().get(0).getSubjects()[i])){
				return i;
			}
		}
		return -1;
	}

	private static ArrayList<ASubject> getMatchingSubjects(String search,
			ASubject[] subjects) {
		ArrayList<ASubject> subjs= new ArrayList<ASubject>();

		for(ASubject s:subjects){				
			try{					
				if(s.getSubject().getName().substring(0).equalsIgnoreCase(search)){
					subjs.add(s);
					continue;
				}
				String[] split = s.getSubject().getName().split(" ");
				for(String str : split){
					if(str.equalsIgnoreCase(search)){
						subjs.add(s);
						break;
					}
				}
			}catch(StringIndexOutOfBoundsException error){}

		}

		return subjs;
	}


	static class LengthValidator implements IInputValidator {

		public String isValid(String newText) {
			int len = newText.length();

			// Determine if input is too short
			if (len < 1) return "";				    
			return null;
		}

	}

	static void setAllGraphsSubjectSelected(boolean b, int index, AdmixProj proj) {

		for(DrawInfo d:proj.getDrawInfo()){
			ASubject subj = d.getSubjects()[index];
			subj.getSubject().setSelected(b);
		}	

	}

	public static int getNoVisibleGroups(AdmixProj proj) {
		int count = 0;
		for(AdmixPopulationGroup grp:proj.getCurrentGroups()){
			if(grp.getVisible()){
				count++;
			}
		}
		return count;
	}





	// public static void shiftLabel(GLabel label, int x, int y, AdmixProj proj) {
	// 	int imageWidth = getAdmixImageWidth(proj);	
	// 	int imageHeight = getAdmixImageHeight(proj);	
	// 	Point ratio = label.getRatio();
	// 	ratio.x=label.getRatio().x+(1000*x)/imageWidth;
	// 	ratio.y=label.getRatio().y+(1000*y)/imageHeight;
	// 	if(checkValidRatio(ratio)){
	// 		label.setRatio(ratio);	
	// 		UI.ui.drawGraph();
	// 	}

	// }

	static void removeMouseListener(Label img) {		
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

	public static void drawArrow(AdmixProj proj) {
		UI.setDrawCursor();
		removeMouseListener(proj.getImage());
		proj.getImage().addMouseListener(new AdmixDragArrowPosListener(proj));

	}

	public static void drawLine(AdmixProj proj) {
		UI.setDrawCursor();
		removeMouseListener(proj.getImage());
		proj.getImage().addMouseListener(new AdmixDragLinePosListener(proj));

	}

	public static void drawObjectDialog(DrawObject d, AdmixProj proj) {		
		d.setSelected(true);
		UI.ui.drawGraph();
		shared.DrawObjectDialog dialog = new shared.DrawObjectDialog(d);
		dialog.run(UI.display);
		if (dialog.isDeleted()){
			proj.getDrawObjects().remove(d);
		}else{
			d.setSelected(false);
		}
		UI.ui.drawGraph();


	}


}


