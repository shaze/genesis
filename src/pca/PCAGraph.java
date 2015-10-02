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

import io.MyFont;

import java.io.Serializable;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;

import drawObjects.DrawObject;
import shared.GLabel;
import shared.SavedGroupNames;


/**
 * This class contains all the information needed to describe and store a pca graph including
 *  <ul>
 * <li>an array of {@link PCASubject}s describing the individuals on the graph
 * <li>an array of {@link PCAPopulationGroup}s describing the population groups
 * <li>the current selected axes
 * <li>all user specified options (showgrid, showborder etc.)
 * </ul>
 * 
 * 
 */
public class PCAGraph  implements Serializable {
	
	private static final long serialVersionUID = 7597496157557148961L;
	
	private boolean showAxes=true, showAxisLabels=true,showBorder=true,showGrid=true,showMarkers=true,
			scalebyEigen=false,relFile, headingUnderline;
	private PCASubject[] data;
	private ArrayList<GLabel> labels = new ArrayList<GLabel>();
	private String heading="",xLabel,yLabel,zlabel="";
	private MyFont headingFont=new MyFont("Arial", 20, SWT.BOLD  );
	private MyFont keyFont=new MyFont("Arial", 12,SWT.BOLD  );
	private MyFont scaleFont=new MyFont("Arial", 10,SWT.BOLD  );
	private MyFont axisFont=new MyFont("Arial", 12,SWT.BOLD  );
	private PCAPopulationGroup[] groups;
	private int keyPosition=-1; //-1=not visible, 0=right, 1=bottom
	private int rotation=3,phenoColumn=2;
	private String name;
	private ArrayList<SavedGroupNames> savedGroupNames = new ArrayList<SavedGroupNames>();
	private ArrayList<DrawObject> drawObjects = new ArrayList<DrawObject>();
	
	
	
	public int x=-1,y=-1,z=-1;
		
	
	public boolean getShowAxes() {
		return showAxes;
	}

	public void setShowAxes(boolean showAxes) {
		this.showAxes = showAxes;
	}

	public boolean getShowAxisLabels() {
		return showAxisLabels;
	}

	public void setShowAxisLabels(boolean showAxisLabels) {
		this.showAxisLabels = showAxisLabels;
	}

	public FontData getKeyFont() {
		return keyFont.toFontData();
	}

	public void setKeyFont(FontData keyFont) {
		this.keyFont.setName(keyFont.getName());
		this.keyFont.setSize(keyFont.getHeight());
		this.keyFont.setStyle(keyFont.getStyle());
	}
	
	public FontData getScaleFont() {
		return scaleFont.toFontData();
	}

	public void setScaleFont(FontData scaleFont) {
		this.scaleFont.setName(scaleFont.getName());
		this.scaleFont.setSize(scaleFont.getHeight());
		this.scaleFont.setStyle(scaleFont.getStyle());
	}
	
	public FontData getAxisFont() {
		return axisFont.toFontData();
	}

	public void setAxisFont(FontData axisFont) {
		this.axisFont.setName(axisFont.getName());
		this.axisFont.setSize(axisFont.getHeight());
		this.axisFont.setStyle(axisFont.getStyle());
	}

	
	public int[] getMargins(){
		if(keyPosition==0){
			return new int[]{30,30,250,30};
		}if(keyPosition==-1){
			return new int[]{30,30,30,30};
		}else{
			return new int[]{30,30,30,130};
		}
	}
	
	public int getKeyPosition(){		
		return keyPosition;	
	}
	
	public FontData getHeadingFont() {
		return headingFont.toFontData();
	}

	public void setHeadingFont(FontData headingFont) {
		this.headingFont.setName(headingFont.getName());
		this.headingFont.setSize(headingFont.getHeight());
		this.headingFont.setStyle(headingFont.getStyle());
	}

	public void setKeyPostition(int keyPosition){
		this.keyPosition=keyPosition;
	}
	
	public void setPCAData(PCASubject[] data){
		this.data = data;
	}
	
	public PCASubject[] getPCAData(){
		return data;
	}
	
	public void setHeading(String heading){
		this.heading = heading;
	}
	
	public String getHeading(){
		return heading;
	}
	
	public PCAPopulationGroup[] getGroups(){
		return groups;
	}
	
	public void setGroups(PCAPopulationGroup[] groups){
		this.groups= groups;
	}

	public String getxLabel() {
		return xLabel;
	}

	public void setxLabel(String xLabel) {
		this.xLabel = xLabel;
	}

	public String getyLabel() {
		return yLabel;
	}

	public void setyLabel(String yLabel) {
		this.yLabel = yLabel;
	}

	public boolean getShowBorder() {
		return showBorder;
	}

	public void setShowBorder(boolean showBorder) {
		this.showBorder = showBorder;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public boolean isShowGrid() {
		return showGrid;
	}

	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
	}

	public boolean isShowMarkers() {
		return showMarkers;
	}

	public void setShowMarkers(boolean showMarkers) {
		this.showMarkers = showMarkers;
	}

	public String getzLabel() {
		return zlabel;
	}

	public void setzLabel(String zlabel) {
		this.zlabel = zlabel;
	}

	public int getPhenoColumn() {
		return phenoColumn;
	}

	public void setPhenoColumn(int phenoColumn) {
		this.phenoColumn = phenoColumn;
	}

	public ArrayList<GLabel> getLabels() {
		return labels;
	}

	public void setLabels(ArrayList<GLabel> labels) {
		this.labels = labels;
	}

	public boolean isScalebyEigen() {
		return scalebyEigen;
	}

	public void setScalebyEigen(boolean scalebyEigen) {
		this.scalebyEigen = scalebyEigen;
	}

	public boolean isRelFile() {
		return relFile;
	}

	public void setRelFile(boolean relFile) {
		this.relFile = relFile;
	}
	
	public boolean is3D(){
		return (!(this.z==-1));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getHeadingUnderline() {
		return headingUnderline;
	}

	public void setHeadingUnderline(boolean headingUnderline) {
		this.headingUnderline = headingUnderline;
	}

	public ArrayList<SavedGroupNames> getSavedGroupNames() {
		return savedGroupNames;
	}

	public ArrayList<DrawObject> getDrawObjects() {
		return drawObjects;
	}




}

