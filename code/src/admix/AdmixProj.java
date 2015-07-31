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

import io.MyFont;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import genesisDrawable.SWTCanvas;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import admix.drawinfo.DrawInfo;
import drawObjects.DrawObject;
import shared.GLabel;
import shared.SavedGroupNames;
import main.UI;
import shared.WaitDialog;
import java.util.LinkedList;

public class AdmixProj  {
	private String name;
	
	private ArrayList<AdmixGraph> graphs = new ArrayList<AdmixGraph>() ;
	private ArrayList<DrawInfo> drawInfo=new ArrayList<DrawInfo>();
	
	private int id;
	private TabItem tab;
	private PaintListener paintListener;
	private boolean showPopLabels=true, showBorder=true,horizontal=true, headingUnderline;
	private String heading="";
	private int phenoColumn=2;
	private MyFont headingFont=new MyFont("Arial", 20, SWT.BOLD );
	private MyFont groupFont=new MyFont("Arial", 10, SWT.BOLD );
	private int graphHeight=-1,  subjectWidth=1 , separationDistance=50;
	private ArrayList<RGB> colours;
	private ArrayList<GLabel> labels=new ArrayList<GLabel>();
	private Label image;
	private GC gc;
	private Image img;
	private boolean fam;
	private int[] margins = new int[]{0,0,0,0};
	private ArrayList<SavedGroupNames> savedGroupNames = new ArrayList<SavedGroupNames>();
	private ArrayList<DrawObject> drawObjects = new ArrayList<DrawObject>();
	private AdmixPopulationGroup[][] groups;
        private int sortoptions=0;
	
	public AdmixProj() {
		super();		
	}
	
	
	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}



    private  void setMembersInGroup
	(AdmixPopulationGroup populationGroup, int phenoColumn) {
	int count=0,countVisible=0;
	for(AdmixSubject a: getGraphs().get(0).getAdmixData()){
	    if(a.getGroups()[phenoColumn].getID()==populationGroup.getID()){
		count++;
		if(a.getVisible()&&a.getGroups()[phenoColumn].getVisible()){
		    countVisible++;
		}
	    }
	}

	populationGroup.setNoMembers(count);
	populationGroup.setNoVisibleMembers(countVisible);

    }

    public void setMembersInGroups(int phenoColumn) {
	for(AdmixPopulationGroup p:getGroups()[phenoColumn]){
	    setMembersInGroup(p,phenoColumn);
	}
    }


    int getNoVisibleSubjects() {
	AdmixGraph graph = getGraphs().get(0);
	int count=0;
	for(AdmixSubject a:graph.getAdmixData()){
	    if(graph.getProj().hasPheno()){
		if(a.getVisible()&&a.getGroups()[graph.getProj().getPhenoColumn()].getVisible()){
		    count++;
		}
	    }else{
		if(a.getVisible()){
		    count++;
		}
	    }
	}
	return count;
    }




    int getHorizontalPopMargin() {
	GC gc = new GC(UI.display);
	gc.setFont(new Font(UI.display,this.getGroupFont()));
	int result = gc.textExtent("p").y;
	gc.dispose();
	return result;
    }

    private int getHeadingMargin() {
	int margin;

	GC gc = new GC(UI.mainWindow);
	gc.setFont(new Font(gc.getDevice(),this.getHeadingFont()));
	margin = gc.textExtent(this.getHeading()).y + 10;
	gc.dispose();			
	return margin;
    }

    public  void draw() {
	ScrolledComposite comp = (ScrolledComposite)this.getTab().getControl();
	Point origin=comp.getOrigin();

	try{
	    comp.removePaintListener(this.getPaintListener());
	}catch(IllegalArgumentException e){}	


	AdmixGraphMethods gm=new AdmixGraphMethods(this,UI.mainWindow);		
	gm.setDrawInfo(comp.getClientArea());

	createImageLabel(comp);

	resetComp(comp,origin);
    }






      public void createPopGroups(){		
		int maxGroups=getMaxGroups();
		setGroups(new AdmixPopulationGroup[maxGroups][]);
		resetGroups(maxGroups);
		for(int i=0;i<maxGroups;i++){
			createPopgroupsForColumn(i);
		}
	}

	private void createPopgroupsForColumn(int phenoColumn) {

		LinkedList<String> names = createGroupNameArray(getGraphs().get(0), phenoColumn);

		AdmixPopulationGroup[] popGroups = new AdmixPopulationGroup[names.size()];
		for(int i=0;i<names.size();i++){
			popGroups[i]=new AdmixPopulationGroup(i, names.get(i),i);
		}

		getGroups()[phenoColumn]=popGroups;

		setToGroups(phenoColumn);

	}



	private static LinkedList<String> createGroupNameArray(AdmixGraph graph, int phenoColumn) {
		LinkedList<String> names= new LinkedList<String>();
		boolean unphenod=false;
		for(AdmixSubject a:graph.getAdmixData()){
			try{
				String gpName = a.getPhenotypeData()[phenoColumn]; 

				if(!names.contains(gpName)){
					names.add(gpName);				
				}
			}catch(NullPointerException e){
				a.setPhenotypeData(new String[]{a.getName().split(" ")[0],a.getName().split(" ")[1],"No group"});								
				unphenod=true;
			}catch(ArrayIndexOutOfBoundsException e){
				a.setPhenotypeData(new String[]{a.getName().split(" ")[0],a.getName().split(" ")[1],"No group"});												
				unphenod=true;				
			}
		}
		if(unphenod){			
			names.add("No group");
		}

		return names;

	}



	public void setToGroups(int phenoColumn) {
		for(AdmixGraph graph:getGraphs()){
			for(AdmixSubject a:graph.getAdmixData()){
				try{
					a.getGroups()[phenoColumn]=findPopGroup(a.getPhenotypeData()[phenoColumn],phenoColumn);

				}catch(ArrayIndexOutOfBoundsException e){
					a.getGroups()[phenoColumn]=findPopGroup(a.getPhenotypeData()[2],phenoColumn);
				}
			}		
			setMembersInGroups(phenoColumn);
		}
	}



	public AdmixPopulationGroup findPopGroup(String gpName){
		for(AdmixPopulationGroup p:getCurrentGroups()){
			if(p.getName().equals(gpName)){
				return p;
			}
		}
		return null;
	}

	private AdmixPopulationGroup findPopGroup(String gpName, int phenoColumn) {
		for(AdmixPopulationGroup p:getGroups()[phenoColumn]){
			if(p.getName().equals(gpName)){
				return p;
			}
		}
		return null;
	}




	private void resetGroups(int maxGroups) {
		for(AdmixGraph graph:getGraphs()){
			for(AdmixSubject a:graph.getAdmixData()){			
				a.setGroup(new AdmixPopulationGroup[maxGroups]);
			}								
		}
	}

    //public static void resetGroups(AdmixGraph graph) {
    //	int maxGroups=getMaxGroups();
    //	for(AdmixSubject a:graph.getAdmixData()){			
    //		a.setGroup(new AdmixPopulationGroup[maxGroups]);
    //	}								
    //}


	private int getMaxGroups() {
		int max=Integer.MIN_VALUE;
		for(int i=0;i<getGraphs().get(0).getAdmixData().length;i++){
			if(getGraphs().get(0).getAdmixData()[i].getPhenotypeData().length>max){
				max=getGraphs().get(0).getAdmixData()[i].getPhenotypeData().length;
			}
		}
		return max;
	}



    private void createImageLabel(Control canvas) {

	WaitDialog.start();

	ScrolledComposite comp=(ScrolledComposite)canvas;
	int imageWidth = getImageWidth();	
	int imageHeight = getImageHeight(); 
	Image img= new Image(UI.display,imageWidth,imageHeight);

	GC gc = new GC(img);
	SWTCanvas drawable = new SWTCanvas(gc);
	for(int i=0;i< getGraphs().size();i++){
	    AdmixGraph g;
	    g=getGraphs().get(i);
	    AdmixDrawTools dT = new AdmixDrawTools(drawable,getDrawInfo().get(i),g,
						   canvas.getSize(),UI.display,i);
	    dT.drawGraph();
	}

	Label imgLabel = new Label(comp,SWT.NONE);
	imgLabel.setImage(img);
	imgLabel.setSize(imgLabel.computeSize(SWT.DEFAULT,SWT.DEFAULT));
	imgLabel.addMouseListener(new AdmixMouseListener(this, comp));
	comp.setContent(imgLabel);
	setImageLabel(imgLabel);
	setGC(gc);
	setImg(img);

	WaitDialog.end();

    }

    private static void resetComp(ScrolledComposite comp, Point origin) {
	comp.getParent().layout(false, true);
	comp.redraw();			
	comp.update();		
	comp.setOrigin(origin);

    }



    public int getImageWidth() {	
	if(this.isHorizontal()){		
	    int noSubjects = getNoVisibleSubjects();
	    int subjectWidth = this.getDrawInfo().get(0).getSubjectWidth();
	    int margin =60 + this.getMargins()[0]+this.getMargins()[2];
	    return (noSubjects*subjectWidth)+margin;	
	}else{
	    int imageWidth=(this.getNoRows()*(this.getGraphHeight()+this.getSeparationDistance()))+50;
	    if(this.getShowPopLabels()){
		imageWidth+=120;
	    }
	    int margin =this.getMargins()[0]+this.getMargins()[2];
	    return imageWidth+margin;
	}

    }

    public int getImageHeight() {
	if(isHorizontal()){
	    int imageHeight=(this.getNoRows()*(this.getGraphHeight()+this.getSeparationDistance()))+15;
	    if(!this.getHeading().equals("")){
		imageHeight+=getHeadingMargin();
	    }
	    if(this.getShowPopLabels()){
		imageHeight+=getHorizontalPopMargin();
	    }
	    int margin =this.getMargins()[1]+this.getMargins()[3];
	    return imageHeight+margin;
	}else{
	    int noSubjects = getNoVisibleSubjects();
	    int subjectWidth = this.getDrawInfo().get(0).getSubjectWidth();
	    int margin =70 + this.getMargins()[1]+this.getMargins()[3];
	    return (noSubjects*subjectWidth)+margin;	
	}

    }



    public int getSortOption(){
	return sortoptions;
    }


    public void setSortOption(int newval){
	sortoptions=newval;
    }



    void shiftPop(AdmixPopulationGroup group, int delta){

	int order, oldorder;
	int N=getGroups()[getPhenoColumn()].length-1;
	AdmixPopulationGroup swap;
	oldorder=order=group.getOrder();
	do {
	    if((delta<0)&&(order==0) || ((delta>0)&&(order==N))) return;
	    order=order+delta;
	    swap = findPopGroupByOrder(order);
	} while (!swap.getVisible());
        group.setOrder(order);
	swap.setOrder(oldorder);
    }


	public AdmixPopulationGroup findPopGroupByID(int id){		
		for(AdmixPopulationGroup p:getCurrentGroups()){
			if(p.getID()==id){
				return p;
			}
		}
		return null;
	}

	public AdmixPopulationGroup findPopGroupByID(int id,int phenoCol){
		for(AdmixPopulationGroup p:getGroups()[phenoCol]){
			if(p.getID()==id){
				return p;
			}
		}
		return null;
	}

        public AdmixPopulationGroup findPopGroupByOrder(int order){
		for(AdmixPopulationGroup p:getCurrentGroups()){
			if(p.getOrder()==order){
				return p;
			}
		}
		return null;
	}

	public AdmixPopulationGroup findPopGroupByOrder(int order, int phenoCol){
		for(AdmixPopulationGroup p:getGroups()[phenoCol]){
			if(p.getOrder()==order){
				return p;
			}
		}
		return null;
	}





	public int getNoRows() {
		return graphs.size();
	}

	public int getPhenoColumn() {
		return phenoColumn;
	}

	public void setPhenoColumn(int phenoColumn) {
		this.phenoColumn = phenoColumn;
	}

	public FontData getHeadingFont() {
		return headingFont.toFontData();
	}
	
	public MyFont getRawHeadingFont(){
		return headingFont;
	}

	public void setRawHeadingFont(MyFont font){
		headingFont = font;
	}
	
	public void setHeadingFont(FontData headingFont) {
		this.headingFont.setName(headingFont.getName());
		this.headingFont.setSize(headingFont.getHeight());
		this.headingFont.setStyle(headingFont.getStyle());
	}


	public void setGraphs(ArrayList<AdmixGraph> graphs) {
		this.graphs = graphs;
	}

	public void setDrawInfo(ArrayList<DrawInfo> drawInfo) {
		this.drawInfo = drawInfo;
	}


	public PaintListener getPaintListener() {
		return paintListener;
	}
	
	public void setPaintListener(PaintListener paintListener) {
		this.paintListener = paintListener;
	}
	
	public void setTab(TabItem tab) {
		this.tab = tab;
	}
	public TabItem getTab() {
		return tab;
	}
	
	public ArrayList<AdmixGraph> getGraphs() {
		return graphs;
	}

	public void addGraph(AdmixGraph graph) {
		graphs.add(graph);
		drawInfo.add(new DrawInfo());
	}

	public ArrayList<DrawInfo> getDrawInfo() {
		return drawInfo;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getShowBorder() {
		return showBorder;
	}

	public void setShowBorder(boolean showBorder) {
		this.showBorder = showBorder;
	}

	public boolean getShowPopLabels() {
		return showPopLabels;
	}

	public void setShowPopLabels(boolean showPopLabels) {
		this.showPopLabels = showPopLabels;
	}

	public int getGraphHeight() {
		return graphHeight;
	}

	public void setGraphHeight(int graphHeight) {
		this.graphHeight = graphHeight;
	}

	public ArrayList<RGB> getColours() {
		return colours;
	}

	public void setColours(ArrayList<RGB> colours) {
		this.colours = colours;
	}

	public ArrayList<GLabel> getLabels() {
		return labels;
	}

	public void setLabels(ArrayList<GLabel> labels) {
		this.labels = labels;
	}


	public Label getImage() {
		return image;
	}


	public void setImageLabel(Label imgLabel) {
		this.image = imgLabel;
		
	}


	public int getSubjectWidth() {
		return subjectWidth;
	}


	public void setSubjectWidth(int subjectWidth) {
		this.subjectWidth = subjectWidth;
	}


	public boolean isHorizontal() {
		return horizontal;
	}


	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}


	public int getSeparationDistance() {
		return separationDistance;
	}


	public void setSeparationDistance(int separationDistance) {
		this.separationDistance = separationDistance;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public GC getGC() {
		return gc;
	}


	public void setGC(GC gc) {
		this.gc = gc;
	}


	public Image getImg() {
		return img;
	}


	public void setImg(Image img) {
		this.img = img;
	}


	public FontData getGroupFont() {	
		return groupFont.toFontData();
	}
	
	public void setGroupFont(FontData groupFont){
		this.groupFont.setName(groupFont.getName());
		this.groupFont.setSize(groupFont.getHeight());
		this.groupFont.setStyle(groupFont.getStyle());
	}


	public MyFont getRawGroupFont() {
		return groupFont;
	}


	public void setRawGroupFont(MyFont groupFont) {
		this.groupFont=groupFont;
		
	}
	public boolean setFam(boolean b){
		return fam=b;
	}
	
	public boolean hasFam(){
		return fam;
	}


	public boolean getHeadingUnderline() {
		return headingUnderline;
	}


	public void setHeadingUnderline(boolean headingUnderline) {
		this.headingUnderline = headingUnderline;
	}


	public int[] getMargins() {
		return margins;
	}


	public void setMargins(int[] margins) {
		this.margins = margins;
	}


	public ArrayList<SavedGroupNames> getSavedGroupNames() {
		return savedGroupNames;
	}


	public ArrayList<DrawObject> getDrawObjects() {
		return drawObjects;
	}


	public void setDrawObjects(ArrayList<DrawObject> drawObjects) {
		this.drawObjects = drawObjects;
	}


	public AdmixPopulationGroup[][] getGroups() {
		return groups;
	}


	public void setGroups(AdmixPopulationGroup[][] groups) {
		this.groups = groups;
	}
	
	public AdmixPopulationGroup[] getCurrentGroups(){
		return groups[phenoColumn];
	}


	public boolean hasPheno() {
		return (this.getGraphs().get(0).getAdmixData()[0].getPhenotypeData()!= null 
				? true : false);		
	}

	
}
