package admix;

import io.MyFont;

import java.io.Serializable;
import java.util.ArrayList;

import org.eclipse.swt.graphics.RGB;

import admix.drawinfo.DrawInfo;
import drawObjects.DrawObject;
import shared.GLabel;

public class AdmixSerializable implements Serializable {


	private static final long serialVersionUID = 8027110314593941368L;
	
	private ArrayList<AdmixGraph> graphs;
	private boolean showPopLabels, showBorder,horizontal,headingUnderline;
	private String heading,name;
	private int phenoColumn;
	private MyFont headingFont, groupFont;
	private int graphHeight,  subjectWidth=1 , separationDistance=50;;
	private ArrayList<RGB> colours;
	private ArrayList<GLabel> labels;
	private ArrayList<DrawObject> drawObjects;
	private int[] margins;
	private boolean fam;
	private AdmixPopulationGroup[][] groups;
	
	public AdmixSerializable(AdmixProj proj){
		graphs=proj.getGraphs();
		removeProjectPointers();
		showPopLabels=proj.getShowPopLabels();
		showBorder=proj.getShowBorder();
		heading=proj.getHeading();
		phenoColumn=proj.getPhenoColumn();
		headingFont=proj.getRawHeadingFont();
		groupFont=proj.getRawGroupFont();
		graphHeight=proj.getGraphHeight();
		subjectWidth=proj.getSubjectWidth();
		separationDistance=proj.getSeparationDistance();
		colours=proj.getColours();
		labels=proj.getLabels();
		name=proj.getName();
		horizontal=proj.isHorizontal();
		headingUnderline=proj.getHeadingUnderline();
		drawObjects=proj.getDrawObjects();
		margins=proj.getMargins();
		fam=proj.hasFam();
		groups=proj.getGroups();

	}
	
	private void removeProjectPointers() {
		for(AdmixGraph graph:graphs){
			graph.setProj(null);
		}
		
	}

	public AdmixProj toProj(){
		AdmixProj proj = new AdmixProj();
		
		proj.setGraphs(graphs);
		proj.setDrawInfo(generateDrawInfo());
		proj.setShowPopLabels(showPopLabels);
		proj.setShowBorder(showBorder);
		proj.setHeading(heading);
		proj.setPhenoColumn(phenoColumn);
		proj.setRawHeadingFont(headingFont);
		proj.setRawGroupFont(groupFont);
		proj.setGraphHeight(graphHeight);
		proj.setSeparationDistance(separationDistance);
		proj.setSubjectWidth(subjectWidth);
		proj.setColours(colours);
		proj.setLabels(labels);
		proj.setName(name);
		proj.setHorizontal(horizontal);
		proj.setHeadingUnderline(headingUnderline);
		proj.setDrawObjects(drawObjects);
		proj.setMargins(margins);
		proj.setFam(fam);
		proj.setGroups(groups);
		
		addProjectPointers(proj);
		
		return proj;
	}

	private void addProjectPointers(AdmixProj proj) {
		for(AdmixGraph graph:graphs){
			graph.setProj(proj);
		}
		
	}

	private ArrayList<DrawInfo> generateDrawInfo() {
		ArrayList<DrawInfo> di = new ArrayList<DrawInfo>();
		for(@SuppressWarnings("unused") AdmixGraph g:graphs){
			di.add(new DrawInfo());
		}
		
		return di;
	}

}
