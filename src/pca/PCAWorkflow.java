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

package pca;

import genesisDrawable.SWTCanvas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import pca.dialogs.LabelDialog;
import pca.dialogs.SubjectDialog;
import pca.drawInfo.DrawInfo;
import pca.drawInfo.DrawInfo3D;
import pca.drawTools.DrawTools;
import drawObjects.Arrow;
import drawObjects.DrawObject;
import drawObjects.Line;
import shared.GLabel;
import shared.InputBox;
import shared.SavedGroupNames;

public class PCAWorkflow {

	public static PCAProj newPCA() {
		PCAWizard wiz = new PCAWizard('n');
		WizardDialog dialog = new WizardDialog(UI.display.getActiveShell(),wiz);
		dialog.open();
		if(wiz.finished){
			PCAProj proj = new PCAProj(wiz.graph);
			setAxesOptions(proj.getGraph(),wiz.options);		
			if(wiz.keyFont!=null){
				proj.getGraph().setKeyFont(wiz.keyFont);
			}
			if(wiz.headingFont!=null){
				proj.getGraph().setHeadingFont(wiz.headingFont);
				proj.getGraph().setHeadingUnderline(wiz.headingUnderline);
			}		
			if(wiz.scaleFont!=null){
				proj.getGraph().setScaleFont(wiz.scaleFont);
			}
			if(wiz.axisFont!=null){
				proj.getGraph().setAxisFont(wiz.axisFont);
			}
			proj.getGraph().setHeading(wiz.heading);
			proj.getGraph().setKeyPostition(wiz.keyPos);
			if(wiz.phenoColumn!=proj.getGraph().getPhenoColumn()){
				proj.getGraph().setPhenoColumn(wiz.phenoColumn);				
			}
			createPopGroups(proj.getGraph());

			return proj;
		}else{
			return null;
		}

	}

	public static void setAxesOptions(PCAGraph graph, boolean[] options) {
		graph.setShowAxes(options[0]);
		graph.setShowAxisLabels(options[1]);
		graph.setShowBorder(options[2]);
		graph.setShowGrid(options[3]);
		graph.setShowMarkers(options[4]);
		graph.setScalebyEigen(options[5]);

	}

	public static void createPopGroups(PCAGraph graph){

		RGB[] oldColours=getOldGraphColours(graph);
		int[] oldIcons=getOldGraphIcons(graph);
		int[] oldOrders=getOldGraphOrders(graph);
		boolean[] oldIconBorders=getOldIconBorders(graph);
		boolean[] oldVisibilities=getOldVisibilities(graph);
		String[] oldDisplayNames = getOldDisplayNames(graph);
		String[] oldGroupNames = getOldGroupNames(graph);

		boolean unphenod=false; 
		LinkedList<String> names = new LinkedList<String>();
		for(PCASubject p:graph.getPCAData()){
			try{
				String gpName = p.getPhenotypeData()[graph.getPhenoColumn()];
				if(gpName.equals("No phenotype data.")){
					unphenod=true;
					continue;
				}
				if(!names.contains(gpName)){
					names.add(gpName);
				}
			}catch(NullPointerException e){
				p.setPhenotypeData(new String[]{p.getName().split(" ")[0],p.getName().split(" ")[1],"No phenotype data."});										
				unphenod=true;
			}catch(ArrayIndexOutOfBoundsException e){
				p.setPhenotypeData(new String[]{p.getName().split(" ")[0],p.getName().split(" ")[1],"No phenotype data."});										
				unphenod=true;				
			}
		}
		if(unphenod){
			names.add("No phenotype data.");
		}
		PCAPopulationGroup[] popGroups = new PCAPopulationGroup[names.size()];
		Random r = new Random();
		for(int i=0;i<names.size();i++){
			if(i>=oldColours.length){
				popGroups[i]=new PCAPopulationGroup(names.get(i), defaultColour(i),i,r.nextInt(4));
			}else{												
				popGroups[i]=new PCAPopulationGroup(names.get(i), oldColours[i],i,oldIcons[i]);
				popGroups[i].setBorder(oldIconBorders[i]);
				if(names.get(i)==oldGroupNames[i]){														
					popGroups[i].setVisible(oldVisibilities[i]);
					popGroups[i].setOrder(oldOrders[i]);
					popGroups[i].setDisplayName(oldDisplayNames[i]);	
				}

			}


			if(names.get(i).equals("No phenotype data.")){
				popGroups[i].setDisplayName("No Group");
			}
		}
		graph.setGroups(popGroups);
		setToGroups(graph);

		if(!checkSavedGroups(graph)){
			PCAWorkflow.updateSavedGroups(graph);
		}

	}

	private static boolean checkSavedGroups(PCAGraph graph){
		ArrayList<SavedGroupNames> savedNames = graph.getSavedGroupNames();
		for(SavedGroupNames saved: savedNames){
			if(saved.column==graph.getPhenoColumn()){
				loadSavedNames(graph.getGroups(), saved);
				return true;
			}
		}
		return false;
	}

	private static void loadSavedNames(PCAPopulationGroup[] groups, SavedGroupNames saved) {
		for(int i=0; i<groups.length; i++){
			groups[i].setDisplayName(saved.names[i]);
			groups[i].setOrder(saved.order[i]);
		}

	}

	private static void updateSavedGroups(PCAGraph graph) {		
		ArrayList<SavedGroupNames> savedNames = graph.getSavedGroupNames();		

		//remove old entries of this column
		Iterator<SavedGroupNames> iter = savedNames.iterator();
		while (iter.hasNext()) {
			SavedGroupNames names = iter.next();

			if(names.column==graph.getPhenoColumn()){
				iter.remove();
			}
		}

		//add new entry
		String[] names=createSavedGroupNamesList(graph);
		int[] orders=createSavedGroupOrdersList(graph);
		savedNames.add(new SavedGroupNames(graph.getPhenoColumn(), names, orders));		

	}



	private static String[] createSavedGroupNamesList(PCAGraph graph) {
		PCAPopulationGroup[] popGroups = graph.getGroups();
		String[] names=new String[popGroups.length];
		for(int i=0;i<popGroups.length;i++){
			names[i]=popGroups[i].getDisplayName();
		}		
		return names;
	}

	private static int[] createSavedGroupOrdersList(PCAGraph graph) {
		PCAPopulationGroup[] popGroups = graph.getGroups();
		int[] orders=new int[popGroups.length];
		for(int i=0;i<popGroups.length;i++){
			orders[i]=popGroups[i].getOrder();
		}		
		return orders;
	}


	private static String[] getOldGroupNames(PCAGraph graph) {
		try{
			String[] result = new String[graph.getGroups().length];
			for(int i=0;i<graph.getGroups().length;i++){
				result[i]=graph.getGroups()[i].getName();	
			}

			return result;
		}catch (NullPointerException e){
			//this happens when the graph is a new graph
			return new String[0];
		}
	}

	private static String[] getOldDisplayNames(PCAGraph graph) {
		try{
			String[] result = new String[graph.getGroups().length];
			for(int i=0;i<graph.getGroups().length;i++){
				result[i]=graph.getGroups()[i].getDisplayName();
			}
			return result;
		}catch (NullPointerException e){
			//this happens when the graph is a new graph
			return new String[0];
		}
	}

	private static int[] getOldGraphIcons(PCAGraph graph) {
		try{
			int[] result = new int[graph.getGroups().length];
			for(int i=0;i<graph.getGroups().length;i++){
				result[i]=graph.getGroups()[i].getIconSymbol();
			}
			return result;
		}catch (NullPointerException e){
			//this happens when the graph is a new graph
			return new int[0];
		}
	}

	private static int[] getOldGraphOrders(PCAGraph graph) {
		try{
			int[] result = new int[graph.getGroups().length];
			for(int i=0;i<graph.getGroups().length;i++){
				result[i]=graph.getGroups()[i].getOrder();
			}
			return result;
		}catch (NullPointerException e){
			//this happens when the graph is a new graph
			return new int[0];
		}
	}

	private static boolean[] getOldIconBorders(PCAGraph graph) {
		try{
			boolean[] result = new boolean[graph.getGroups().length];
			for(int i=0;i<graph.getGroups().length;i++){
				result[i]=graph.getGroups()[i].getBorder();
			}
			return result;
		}catch (NullPointerException e){
			//this happens when the graph is a new graph
			return new boolean [0];
		}
	}

	private static boolean[] getOldVisibilities(PCAGraph graph) {
		try{
			boolean[] result = new boolean[graph.getGroups().length];
			for(int i=0;i<graph.getGroups().length;i++){
				result[i]=graph.getGroups()[i].getVisible();
			}
			return result;
		}catch (NullPointerException e){
			//this happens when the graph is a new graph
			return new boolean [0];
		}
	}

	private static RGB[] getOldGraphColours(PCAGraph graph)  {
		try{
			RGB[] result = new RGB[graph.getGroups().length];

			for(int i=0;i<graph.getGroups().length;i++){
				result[i]=graph.getGroups()[i].getColour();
			}
			return result;
		}catch (NullPointerException e){
			//this happens when the graph is a new graph
			return new RGB[0];
		}
	}

	public static void setToGroups(PCAGraph graph) {
		for(PCASubject p:graph.getPCAData()){			
			try{
				p.setGroup(findPopGroup(p.getPhenotypeData()[graph.getPhenoColumn()],graph));
			}catch(ArrayIndexOutOfBoundsException e){
				p.setGroup(findPopGroup(p.getPhenotypeData()[2],graph));
			}

		}		
	}

	//this generates the default colours
	public static RGB defaultColour(int index){
		RGB[] colours = new RGB[]{new RGB(255,0,0),//red
				new RGB(0,255,0),//green
				new RGB(0,0,255),//blue
				new RGB(255,255,0),//yellow
				new RGB(255,0,255),//pink
				new RGB(0,255,255),//turquoise
				new RGB(255,108,0),//orange
				new RGB(229,229,229),//light grey
				new RGB(0,0,150),//etc
				new RGB(105,105,105),//etc
				new RGB(205,102,29),
				new RGB(205,173,0),
				new RGB(85,107,47),
				new RGB(150,150,150),
				new RGB(50,50,50),
				new RGB(240,128,128),
				new RGB(255,193,193),
				new RGB(139,125,123),
				new RGB(0,0,0),
				new RGB(135,0,0),
				new RGB(178,34,34),
				new RGB(255,160,122),
				new RGB(240,10,10),
				new RGB(20,20,240),
				new RGB(20,240,20),
				new RGB(238,149,114),
				new RGB(230,30,30),
				new RGB(25,25,140),
				new RGB(160,30,30),
				new RGB(205,129,98)};
		index = index % colours.length;
		return colours[index];	
	}

	public static PCAPopulationGroup findPopGroup(String gpName,PCAGraph graph){
		for(PCAPopulationGroup p:graph.getGroups()){
			if(p.getName().equals(gpName)){
				return p;
			}
		}
		return null;
	}

	public static Subject findSubjectByName(String name,PCAGraph graph,DrawInfo drawInfo,DrawInfo3D drawInfo3D){
		if(graph.z==-1){
			for(Subject s:drawInfo.getPoints()){
				if(s.getSubj().getName().equals(name)){
					return s;					
				}
			}			
		}else{
			for(Subject s:drawInfo3D.getPoints()){
				if(s.getSubj().getName().equals(name)){
					return s;					
				}
			}			
		}
		return null;
	}

	public static PCAPopulationGroup findPopWithOrder(int order,PCAGraph graph){
		for(PCAPopulationGroup p:graph.getGroups()){
			if(p.getOrder()==order){
				return p;
			}
		}
		return null;
	}

	public static void groupDialog(PCAPopulationGroup gp,PCAGraph graph){
		PCAGroupDialog dialog = new PCAGroupDialog(gp, graph);

		dialog.run();
		if(dialog.isUp()){
			shiftPop(gp,graph,-1);
		}else if(dialog.isDown()){
			shiftPop(gp,graph,+1);
		}

		if(dialog.nameWasChanged()){
			gp.setDisplayName(dialog.getName());
		}

		gp.setIconSymbol(dialog.getIcon());
		gp.setBorder(dialog.getBorder());		
		gp.setColour(dialog.getColour());	
		gp.setIconSize(dialog.getSize());
		gp.setVisible(!dialog.getDeleted());	

		updateSavedGroups(graph);
	}

	//this increases the order field of the pop group
	public static void shiftPop(PCAPopulationGroup group,PCAGraph graph,
			int delta){
		int oldorder,order,N;
		oldorder = order =group.getOrder();
		N=graph.getGroups().length-1;
		PCAPopulationGroup swap; 	
		do {
			if ((delta<0) &&(order==0) || (delta>0)&&(order==N)) return;
			swap = findPopWithOrder(order+delta,graph);
			order=order+delta;
		} while (! swap.getVisible());
		group.setOrder(order);
		swap.setOrder(oldorder);
	}


	/**This method creates the subject dialog used to edit parameters about individuals
	 * on the PCA graph.
	 * 
	 * @param s The subject in question	 
	 * @param point the location of the click or where to display the dialog
	 * @param graph the PCAGraph
	 */
	public static void PCASubjectDialog(Subject s,Point point,PCAGraph graph){

		SubjectDialog dialog = new SubjectDialog(s,point,graph.x,graph.y,graph.z);
		dialog.run(UI.display);
		//the subject has been removed from the plot
		if(dialog.isDeleted()){
			s.getSubj().setVisible(false);
			s.getSubj().setColour(null);
			s.getSubj().setIconSymbol(-1);						

		}else{
			s.getSubj().setVisible(true);			
		}                                           
		//the icon has been modified		
		if(dialog.getClear()){
			s.getSubj().setColour(null);
			s.getSubj().setIconSymbol(-1);
		}else{
			if(dialog.colourWasChanged()){
				s.getSubj().setColour(dialog.getColour());	
				s.setColour(dialog.getColour());
			}
			if(dialog.iconWasChanged()){				
				s.getSubj().setIconSymbol(dialog.getIcon());
				s.setIcon(dialog.getIcon());
				if(dialog.getIcon()==6){
					s.getSubj().setIconSymbol(-1);
				}
			}			
			s.getSubj().setIconSize(dialog.getSize());		
		}
		//the subject has been put on top
		if(dialog.isOnTop()){
			s.getSubj().setOnTop(true);
		}else{
			s.getSubj().setOnTop(false);
		}
		s.getSubj().setSelected(false);
	}

	public static PCAPopulationGroup findPCAPopGroup(PCAGraph graph,String gpName){
		for(PCAPopulationGroup p:graph.getGroups()){
			if(p.getName().equals(gpName)){
				return p;
			}
		}
		return null;
	}

	public static void drawPCA(PCAProj proj){
		ScrolledComposite comp = (ScrolledComposite)proj.getTab().getControl();
		try{
			comp.removePaintListener(proj.getPaintListener());
		}catch(IllegalArgumentException e){}

		int dimensions;
		if(proj.getGraph().z==-1){
			dimensions=2;
		}else{
			dimensions=3;
		}

		PCAGraphMethods gm=new PCAGraphMethods(proj.getGraph(),proj.getDrawInfo(),proj.getDrawInfo3D(),UI.mainWindow);				 
		gm.setDrawInfo(comp.getClientArea());

		createImageLabel(comp,proj,dimensions);

	}

	private static void createImageLabel(Control canvas, PCAProj proj, int dimensions) {
		//WaitDialog.start();
		ScrolledComposite comp=(ScrolledComposite)canvas;
		int imageWidth = comp.getClientArea().width;
		int imageHeight = comp.getClientArea().height;
		Image img= new Image(UI.display,imageWidth,imageHeight);

		GC gc = new GC(img);
		SWTCanvas drawable = new SWTCanvas(gc);
		DrawTools dT = new DrawTools(drawable,proj.getDrawInfo(),proj.getDrawInfo3D(),
				proj.getGraph(),dimensions,canvas.getSize(),UI.display);
		dT.drawGraph();

		Label imgLabel = new Label(comp,SWT.NONE);
		imgLabel.setImage(img);
		imgLabel.setSize(imgLabel.computeSize(SWT.DEFAULT,SWT.DEFAULT));
		removeMouseListener(imgLabel);
		imgLabel.addMouseListener(new PCAMouseListener(proj));
		comp.setContent(imgLabel);
		proj.setImg(imgLabel);
		gc.dispose();

		//	WaitDialog.end();

	}

	public static void OptionsWizard(PCAProj proj) {	
		PCAWizard wiz = new PCAWizard('o',proj.getGraph());
		WizardDialog dialog = new WizardDialog(UI.display.getActiveShell(),wiz);
		dialog.open();
		if(wiz.finished){		
			setAxesOptions(proj.getGraph(),wiz.options);		
			if(wiz.keyFont!=null){
				proj.getGraph().setKeyFont(wiz.keyFont);
			}
			if(wiz.headingFont!=null){
				proj.getGraph().setHeadingFont(wiz.headingFont);
				proj.getGraph().setHeadingUnderline(wiz.headingUnderline);
			}				
			if(wiz.scaleFont!=null){
				proj.getGraph().setScaleFont(wiz.scaleFont);
			}
			if(wiz.axisFont!=null){
				proj.getGraph().setAxisFont(wiz.axisFont);
			}
			proj.getGraph().setHeading(wiz.heading);
			proj.getGraph().setKeyPostition(wiz.keyPos);			
			//createPopGroups(proj.getGraph());		
			UI.ui.drawGraph();			

		}

	}

	public static void pcaSearch(PCAProj pcaProj){
		PCAGraph graph = pcaProj.getGraph();
		DrawInfo drawInfo = pcaProj.getDrawInfo();
		DrawInfo3D drawInfo3D = pcaProj.getDrawInfo3D();

		String search="";
		InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(),
				"", "Enter Subject name", "", new PCAWorkflow.LengthValidator());
		if (dlg.open() == Window.OK) {
			// User clicked OK; update the label with the input
			search=(dlg.getValue());
		}else{
			return;
		}


		Subject[] subjects;
		if(graph.z==-1){
			subjects=drawInfo.getPoints();
		}else{
			subjects=drawInfo3D.getPoints();
		}
		ArrayList<Subject> subjs = new ArrayList<Subject>();
		for(Subject s:subjects){				
			try{					
				if(s.getSubj().getName().substring(0).equalsIgnoreCase(search)){
					subjs.add(s);
					continue;
				}
				String[] split = s.getSubj().getName().split(" ");
				for(String str : split){
					if(str.equalsIgnoreCase(search)){
						subjs.add(s);
						break;
					}
				}
			}catch(StringIndexOutOfBoundsException error){

			}

		}
		if(subjs.size()==1){
			subjs.get(0).getSubj().setSelected(true);
			UI.ui.drawGraph();

			PCAWorkflow.PCASubjectDialog(subjs.get(0),subjs.get(0).getPoint(),graph);
			UI.ui.drawGraph();
			return;	
		}else if(subjs.size()>1){
			String[] names = new String[subjs.size()];
			for(int x=0;x<subjs.size();x++){
				names[x]=subjs.get(x).getSubj().getName();
			}

			PopupList list = new PopupList(UI.mainWindow);
			list.setItems(names);

			Subject s = PCAWorkflow.findSubjectByName(list.open(new Rectangle(UI.display.getBounds().width/2-50,
					UI.display.getBounds().height/2-50,250,50)),graph,drawInfo,drawInfo3D);
			if(s!=null){
				s.getSubj().setSelected(true);	
				UI.ui.drawGraph();
				PCAWorkflow.PCASubjectDialog(s,s.getPoint(),graph);
				UI.ui.drawGraph();

			}
		}else{
			MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_INFORMATION);
			messageBox.setMessage("Couldn't find "+search+" in the data.");
			messageBox.setText("Sorry!");
			messageBox.open();		
		}
	}

	/**
	 * This method creates a label on a PCA graph at the given position
	 * @param graph the relevant graph
	 * @param drawArea the area of the graph
	 * @param point the point on the graph to create the label
	 */
	static void createPCALabelAtMouseCursor(PCAGraph graph,Rectangle drawArea, Point point) {
		InputBox ib= new InputBox(UI.mainWindow);
		ib.run();
		String name = ib.getName();
		FontData font = ib.getFont();	
		Boolean underlined = ib.getUnderlined();

		if(name==null||name.trim().equals("")){
			return;
		}
		name=name.trim();
		Point ratio = getPointRatio(drawArea, point);
		if(checkValidRatio(ratio)){
			createAndAddPCALabel(graph, name, font, underlined,ratio);			
			UI.ui.drawGraph();
		}else{
			invalidPlacementMessageBox();
		}
	}

	/**
	 * This method creates a message box informing the user that the 
	 * label has been placed in an invalid position
	 */
	private static void invalidPlacementMessageBox() {
		MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_ERROR);
		messageBox.setMessage("Invalid Label Placement.");
		messageBox.setText("Sorry!");
		messageBox.open();	
	}

	static void pcaLabelDialog(PCAProj proj, GLabel l) {
		LabelDialog dlg = new LabelDialog(l);
		dlg.run(UI.display);
		if(dlg.isChanged()){
			l.setText(dlg.getText());
		}
		if(dlg.isDeleted()){
			proj.getGraph().getLabels().remove(l);
		}
		if(dlg.isMoved()){
			moveLabel(l, proj);
		}
		if(dlg.getFontChanged()){
			l.setFont(dlg.getFont());			
		}
		l.setUnderlined(dlg.getUnderlined());
		if(dlg.isChanged()||dlg.isDeleted()||dlg.getFontChanged()){
			UI.ui.drawGraph();
		}

	}

	private static void moveLabel(GLabel l, PCAProj proj) {
		removeMouseListener(proj.getImg());
		moveLabelDialog();
		PCAMoveLabelMouseListener temp = new PCAMoveLabelMouseListener(proj, l);
		proj.getImg().addMouseListener(temp);

	}

	private static void moveLabelDialog() {
		MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_INFORMATION);
		messageBox.setMessage("Please click where you would like to move the label to.");
		messageBox.setText("Move Label");
		messageBox.open();	

	}

	static void removeMouseListener(Composite compGraph) {		
		int[] eventTypes = {SWT.MouseDown, SWT.MouseUp, SWT.MouseDoubleClick,
				SWT.MouseMove, SWT.MouseEnter, SWT.MouseExit, SWT.MouseHover,
				SWT.MouseWheel };


		for (int eventType : eventTypes) {

			Listener[] listeners = compGraph.getListeners(eventType);
			for (Listener listener : listeners) {
				compGraph.removeListener(eventType, listener);
			}
		}

	}

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

	/**
	 * This method counts the number of groups that are visible on
	 * the given pca graph
	 * @param graph the relevant PCA graph
	 * @return the number of groups that are visible on
	 * the given pca graph
	 */
	public static int getNoVisibleGroups(PCAGraph graph) {
		int count = 0;
		for(PCAPopulationGroup grp:graph.getGroups()){
			if(grp.getVisible()){
				count++;
			}
		}
		return count;
	}

	/**
	 * This method gets the order of the biggest order of the currently
	 * visible groups
	 * @param graph the relevant PCA graph
	 * @return biggest visitble order
	 */
	public static int getMaxVisibleOrder(PCAGraph graph) {
		int count = 0;
		for(PCAPopulationGroup grp:graph.getGroups())
			if(grp.getVisible())
				if (grp.getOrder()>count)
					count = grp.getOrder();
		return count;
	}



	/**
	 * This method calculates the height of the font of the key
	 * on the given PCA Graph
	 * @param graph the relevant graph
	 * @return the height of the font in the key in pixels
	 */
	public static int getKeyFontHeight(PCAGraph graph) {
		FontData fd=graph.getKeyFont();
		GC gc = new GC(UI.display);
		gc.setFont(new Font(UI.display,fd));
		int height = gc.textExtent("pP").y;
		gc.dispose();
		return height;
	}

	/**
	 * This method calculates the height of the axis labels
	 * on the given PCA Graph
	 * @param graph the relevant graph
	 * @return the height of the font in the axis labels
	 */
	public static int getAxisLabelFontHeight(PCAGraph graph) {
		FontData fd=graph.getAxisFont();
		GC gc = new GC(UI.display);
		gc.setFont(new Font(UI.display,fd));
		int height = gc.textExtent("pP").y;
		gc.dispose();
		return height;
	}

	/**this method checks to see that a ratio for a label's postion
	 * is valid i.e. that the label will not be off the graph 
	 * @param ratio the ratio to check
	 * @return whether or not it is a valid ratio
	 */
	public static boolean checkValidRatio(Point ratio) {
		if(ratio.x>1150||ratio.x<0||ratio.y>1150||ratio.y<0){
			return false;
		}
		return true;
	}

	/**
	 * This method creates and adds a label on the pca graph
	 * @param graph the relevant pca graph
	 * @param name the text for the label
	 * @param font 
	 * @param underlined 
	 * @param ratio the position of the label as a ratio
	 */
	private static void createAndAddPCALabel(PCAGraph graph, String name, 
			FontData font, Boolean underlined, Point ratio) {
		GLabel lbl = new GLabel();
		lbl.setText(name);
		lbl.setRatio(new Point(ratio.x,ratio.y));
		lbl.setFont(font);
		lbl.setUnderlined(underlined);		

		graph.getLabels().add(lbl);

	}




	/**
	 * This method calculates the position of the label on the current
	 * graph and returns it as a ratio. i.e. if the label is positioned
	 * 40 percent across the graph area and 60 percent down, it will 
	 * return {400,600}.
	 * @param drawArea the current area of the graph
	 * @param point the point where the label was placed
	 * @return the ratio of the position of the label
	 */
	static Point getPointRatio(Rectangle drawArea, Point point){
		int imageWidth = drawArea.width;		
		int imageHeight = drawArea.height;	

		int xRatio = (1000*(point.x))/imageWidth;
		int yRatio = (1000*(point.y))/imageHeight;
		return new Point(xRatio,yRatio);
	}



	static int getRatio(PCAGraph graph, int x, int length){

		int ratio = (1000*(x))/length;
		return ratio;

	}


	/**
	 * This class simply checks if the string is the empty string
	 */
	static class LengthValidator implements IInputValidator {

		public String isValid(String newText) {
			int len = newText.length();

			// Determine if input is too short
			if (len < 1) return "";				    
			return null;
		}

	}

	/**
	 * This method will calculate the height of the key in the given 
	 * graph when the key is on the right
	 * @param graph the relevant graph
	 * @return the height of the key on the right
	 */
	public static int getRightKeyHeight(PCAGraph graph) {
		int fontHeight = PCAWorkflow.getKeyFontHeight(graph); 
		return PCAWorkflow.getNoVisibleGroups(graph)*(fontHeight+6);		
	}

	/**
	 * This method will calculate the height of the key in the given 
	 * graph when the key is on the bottom
	 * @param graph the relevant graph
	 * @return the height of the key on the bottom
	 */
	public static int getBottomKeyHeight(PCAGraph graph) {
		int fontHeight = PCAWorkflow.getKeyFontHeight(graph); 
		return (fontHeight+11);		
	}

	/**
	 * This method finds the {@link PCAPopulationGroup} that is visible
	 * with the given order. ie it will return the {@link PCAPopulationGroup}
	 * in the order-th position in the key on the graph
	 * 
	 * @param order the order of the {@link PCAPopulationGroup} in the key
	 * @param graph the relevant graph
	 * @return the {@link PCAPopulationGroup} in the order-th position in the key
	 */
	public static PCAPopulationGroup findVisiblePopWithOrder(int order,
			PCAGraph graph) {
		PCAPopulationGroup[] groups = getVisiblePopGroups(graph);	
		sortByOrder(groups);

		return groups[order];

	}

	/**
	 * This method sorts the given array of {@link PCAPopulationGroup} by
	 * their order (using bubble sort O(n^2) since the arrays will be small
	 * @param groups the array to be sorted
	 */
	private static void sortByOrder(PCAPopulationGroup[] groups) {
		for(int i=0; i<groups.length-1; i++){
			for(int j=i+1; j<groups.length; j++){
				if(groups[i].getOrder()>groups[j].getOrder()){
					PCAPopulationGroup temp = groups[i];
					groups[i]=groups[j];
					groups[j]=temp;
				}
			}
		}

	}

	/**
	 * This method returns an array of {@link PCAPopulationGroup} who are 
	 * have not been hidden from the graph
	 * @param graph the relevant graph
	 * @return the array of {@link PCAPopulationGroup}
	 */
	private static PCAPopulationGroup[] getVisiblePopGroups(PCAGraph graph) {
		ArrayList<PCAPopulationGroup> gps = new ArrayList<PCAPopulationGroup>();
		for(PCAPopulationGroup p:graph.getGroups()){
			if(p.getVisible()){
				gps.add(p);
			}
		}
		return gps.toArray(new PCAPopulationGroup[gps.size()]);
	}



	/**This method will get the default name for the PCA project. 
	 * The default name will be "PCA x" where x is the highest number
	 * of any other currently active PCAProj + 1
	 * @return the default name
	 */
	public static String getDefaultProjName() {			
		return "PCA "+ getProjNum();
	}

	/**This method will get the default number for the PCA project's name. 
	 * The default number will be the highest number of any other currently 
	 * active PCAProj + 1
	 * @return the default number
	 */
	private static int getProjNum() {
		int max = 0;
		for(PCAProj g: UI.pcaProjects){
			int projNum=extractProjectNum(g.getGraph().getName());
			if(max<projNum){
				max=projNum;
			}
		}
		return max+1;

	}

	/**This method will retrieve the number from a {@link PCAProj}'s name. 
	 * ie. it will return 6 if the {@link PCAProj}'s name is "PCA 6" .
	 * @param name the name of the project
	 * @return the number of the project. Will return 0 if the {@link PCAProj}
	 * 			has been named differently.
	 */
	private static int extractProjectNum(String name) {
		try{
			if(name.substring(0, 4).equals("PCA ")){
				return Integer.parseInt(name.substring(4));
			}
		}catch(NumberFormatException e){}

		return 0;
	}

	private static class PCAMoveLabelMouseListener implements MouseListener{
		GLabel lbl;	
		boolean moved;
		PCAGraph graph;
		DrawInfo drawInfo;
		DrawInfo3D drawInfo3D;
		PCAProj proj;

		public PCAMoveLabelMouseListener(PCAProj proj ,GLabel lbl){
			this.proj=proj;
			this.graph = proj.getGraph();
			this.drawInfo = proj.getDrawInfo();
			this.drawInfo3D = proj.getDrawInfo3D();
			this.lbl=lbl;
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {

		}

		@Override
		public void mouseUp(MouseEvent e) {
			if(moved){
				UI.ui.drawGraph();
			}
			removeMouseListener(proj.getImg());
			proj.getImg().addMouseListener(new PCAMouseListener(proj));

		}

		@Override
		public void mouseDown(MouseEvent e) {		
			Rectangle drawArea;
			if(graph.is3D()){
				drawArea=drawInfo3D.getDrawArea();
			}else{
				drawArea=drawInfo.getDrawArea();
			}

			Point relativePt = findPositionOfClick(drawArea);	

			Point ratio = PCAWorkflow.getPointRatio(drawArea, relativePt);
			if(PCAWorkflow.checkValidRatio(ratio)){
				lbl.setRatio(ratio);
				moved=true;
			}


		}

		private Point findPositionOfClick(Rectangle drawArea) {

			Point a=UI.display.getCursorLocation();
			Point panelCorner = proj.getImg().toDisplay(new Point(0,0));
			Point relativePt = new Point(a.x-panelCorner.x,a.y-panelCorner.y);

			return relativePt;
		}
	}


	private static class PCADragArrowPosListener implements MouseListener{	
		private PCAGraph graph;
		private DrawInfo drawInfo;
		private DrawInfo3D drawInfo3D;
		private PCAProj proj;
		private Point start;
		private Point end;

		public PCADragArrowPosListener(PCAProj proj){
			this.proj=proj;
			this.graph = proj.getGraph();
			this.drawInfo = proj.getDrawInfo();
			this.drawInfo3D = proj.getDrawInfo3D();

		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {

		}

		@Override
		public void mouseUp(MouseEvent e) {
			Rectangle drawArea;
			if(graph.is3D()){
				drawArea=drawInfo3D.getDrawArea();
			}else{
				drawArea=drawInfo.getDrawArea();
			}

			Point relativePt = findPositionOfClick(drawArea);	

			end = PCAWorkflow.getPointRatio(drawArea, relativePt);

			if ((e.stateMask&SWT.SHIFT) > 0) {
				// If mouse button pressed then want vertical or horizonal line only
				// work out which
				if (Math.abs(start.x-end.x) < Math.abs(start.y-end.y)) {
					// x coords should be the same
					end.x = start.x ;
				} else
					end.y = start.y ;
			}

			graph.getDrawObjects().add(new Arrow(start, end));
			removeMouseListener(proj.getImg());
			UI.setDefaultCursor();
			UI.ui.drawGraph();

		}

		@Override
		public void mouseDown(MouseEvent e) {		
			Rectangle drawArea;
			if(graph.is3D()){
				drawArea=drawInfo3D.getDrawArea();
			}else{
				drawArea=drawInfo.getDrawArea();
			}

			Point relativePt = findPositionOfClick(drawArea);				
			UI.setCrossCursor();
			start = PCAWorkflow.getPointRatio(drawArea, relativePt);


		}

		private Point findPositionOfClick(Rectangle drawArea) {

			Point a=UI.display.getCursorLocation();
			Point panelCorner = proj.getImg().toDisplay(new Point(0,0));
			Point relativePt = new Point(a.x-panelCorner.x,a.y-panelCorner.y);

			return relativePt;
		}
	}

	private static class PCADragLinePosListener implements MouseListener{	
		private PCAGraph graph;
		private DrawInfo drawInfo;
		private DrawInfo3D drawInfo3D;
		private PCAProj proj;
		private Point start;
		private Point end;

		public PCADragLinePosListener(PCAProj proj){
			this.proj=proj;
			this.graph = proj.getGraph();
			this.drawInfo = proj.getDrawInfo();
			this.drawInfo3D = proj.getDrawInfo3D();

		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {

		}

		@Override
		public void mouseUp(MouseEvent e) {
			Rectangle drawArea;
			if(graph.is3D()){
				drawArea=drawInfo3D.getDrawArea();
			}else{
				drawArea=drawInfo.getDrawArea();
			}

			Point relativePt = findPositionOfClick(drawArea);	

			end = PCAWorkflow.getPointRatio(drawArea, relativePt);		

			if ((e.stateMask&SWT.SHIFT) > 0) {
				// If mouse button pressed then want vertical or horizonal line only
				// work out which
				if (Math.abs(start.x-end.x) < Math.abs(start.y-end.y)) {
					// x coords should be the same
					end.x = start.x ;
				} else
					end.y = start.y ;
			}


			graph.getDrawObjects().add(new Line(start, end));
			UI.setDefaultCursor();
			removeMouseListener(proj.getImg());
			UI.ui.drawGraph();

		}

		@Override
		public void mouseDown(MouseEvent e) {		
			Rectangle drawArea;
			if(graph.is3D()){
				drawArea=drawInfo3D.getDrawArea();
			}else{
				drawArea=drawInfo.getDrawArea();
			}
			UI.setCrossCursor();
			Point relativePt = findPositionOfClick(drawArea);	

			start = PCAWorkflow.getPointRatio(drawArea, relativePt);


		}

		private Point findPositionOfClick(Rectangle drawArea) {

			Point a=UI.display.getCursorLocation();
			Point panelCorner = proj.getImg().toDisplay(new Point(0,0));
			Point relativePt = new Point(a.x-panelCorner.x,a.y-panelCorner.y);

			return relativePt;
		}
	}

	// public static void shiftLabel(GLabel label, int x, int y, Rectangle drawArea) {
	// 	int imageWidth = drawArea.width;	
	// 	int imageHeight = drawArea.height;		
	// 	Point ratio = label.getRatio();
	// 	ratio.x=label.getRatio().x+(1000*x)/imageWidth;
	// 	ratio.y=label.getRatio().y+(1000*y)/imageHeight;
	// 	if(checkValidRatio(ratio)){
	// 		label.setRatio(ratio);	
	// 		UI.ui.drawGraph();
	// 	}

	// }


	public static void drawArrow(PCAProj proj) {
		UI.setDrawCursor();
		removeMouseListener(proj.getImg());
		proj.getImg().addMouseListener(new PCADragArrowPosListener(proj));

	}

	public static void drawLine(PCAProj proj) {
		UI.setDrawCursor();
		removeMouseListener(proj.getImg());
		proj.getImg().addMouseListener(new PCADragLinePosListener(proj));

	}

	public static void drawObjectDialog(DrawObject d, PCAGraph graph) {		
		d.setSelected(true);
		UI.ui.drawGraph();
		shared.DrawObjectDialog dialog = new shared.DrawObjectDialog(d);
		dialog.run(UI.display);
		if (dialog.isDeleted()){
			graph.getDrawObjects().remove(d);
		}
		d.setSelected(false);
		UI.ui.drawGraph();


	}

	public static int getRightKeyWidth(PCAGraph graph) {
		final int PADDING = 55; 
		GC gc = new GC(UI.display);
		gc.setFont(new Font(gc.getDevice(),graph.getKeyFont()));
		int widest=0;
		for(PCAPopulationGroup gp:graph.getGroups()){
			if(gc.textExtent(gp.getDisplayName()).x>widest){
				widest=gc.textExtent(gp.getDisplayName()).x;
			}
		}
		gc.dispose();
		return widest+PADDING;

	}

	public static int getAxisLabelHeight(PCAGraph graph) {
		GC gc = new GC(UI.display);
		gc.setFont(new Font(gc.getDevice(),graph.getAxisFont()));
		int height = gc.textExtent("Pp4").y ;
		gc.dispose();
		return height;
	}



}

