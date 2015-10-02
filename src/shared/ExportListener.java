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

package shared;

import genesisDrawable.SVGCanvas;
import genesisDrawable.SWTCanvas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import main.UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.FileDialog;
import org.jfree.graphics2d.svg.SVGGraphics2D;

import com.itextpdf.text.DocumentException;

import pca.PCAGraphMethods;
import pca.PCAProj;
import pca.drawTools.DrawTools;
import admix.AdmixDrawTools;
import admix.AdmixProj;
import admix.AdmixWorkflow;


/**
 * This class creates the listener that listens for when the Export button is clicked
 * and all the relevant methods
 */
/**
 * @author RWB
 *
 */
public class ExportListener implements SelectionListener {

	public void widgetSelected(SelectionEvent arg0) {	
		try{		
			PCAProj proj = UI.ui.findPCAProjByTab(UI.tabs.getSelection()[0]);
			if(proj!=null){
				exportPCA(proj);
			}
			AdmixProj admixProj = UI.ui.findAdmixProjByTab(UI.tabs.getSelection()[0]);
			if(admixProj!=null){
				exportAdmix(admixProj);					
			}
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			UI.noProjectMessageBox();
		}

	}

	/**
	 * This method creates the dialog and calls the methods that 
	 * will export a PCA project
	 * @param proj the PCA project to be exported
	 */
	private void exportPCA(PCAProj proj) {
		int windowWidth = proj.getImg().getBounds().width;
		int windowHeight = proj.getImg().getBounds().height;
		ExportDialog dialog = new ExportDialog();
		dialog.run(UI.display);

		if(dialog.getSelection()==0){
			exportPDF(proj,windowWidth,windowHeight);			
		}else if(dialog.getSelection()==1){			
			exportPNG(proj,windowWidth,windowHeight);
		}else if(dialog.getSelection()==2){	
			exportSVG(proj,windowWidth,windowHeight);			
		}else{
			return;
		}
	}	

	/**
	 * This method creates the dialog and calls the methods that 
	 * will export an Admix project
	 * @param proj the Admix project to be exported
	 */
	private void exportAdmix(AdmixProj admixProj) {	
		ExportDialog dialog = new ExportDialog();
		dialog.run(UI.display);
		if(dialog.getSelection()==0){
			exportPDF(admixProj);			
		}else if(dialog.getSelection()==1){			
			exportPNG(admixProj);
		}else if(dialog.getSelection()==2){	
			exportSVG(admixProj);
		}else{
			return;
		}

	}


	/**
	 * This method draws the given PCA project onto an image with the given
	 * dimensions and returns the image
	 * @param proj the given PCA Project
	 * @param width the width of the image
	 * @param height the height of the image
	 * @return the SWT Image object
	 */
	private Image makePCAImage(PCAProj proj, int width, int height) {
		int dimensions;
		if(proj.getGraph().z==-1){
			dimensions=2;
		}else{
			dimensions=3;
		}

		PCAGraphMethods gm=new PCAGraphMethods(proj.getGraph(),proj.getDrawInfo(),proj.getDrawInfo3D(),UI.mainWindow);				 
		gm.setDrawInfo(new Rectangle(0,0,width, height) );

		Image img= new Image(UI.display,width,height);

		GC gc = new GC(img);		
		SWTCanvas drawable = new SWTCanvas(gc);
		DrawTools dT = new DrawTools(drawable,proj.getDrawInfo(),proj.getDrawInfo3D(),
				proj.getGraph(),dimensions,new Point(width, height),UI.display);
		dT.drawGraph();


		gc.dispose();

		return img;
	}

	/**
	 * This method draws the given PCA project onto an image with the given
	 * dimensions and returns the image
	 * @param proj the given PCA Project
	 * @param width the width of the image
	 * @param height the height of the image
	 * @param path 
	 * @return the SWT Image object
	 */
	private SVGGraphics2D  makePDFPCAImage(PCAProj proj, int width, int height, String path) {

		int dimensions;
		if(proj.getGraph().z==-1){
			dimensions=2;
		}else{
			dimensions=3;
		}

		//final int a4width=500;
		//final int a4height=750;
		PCAGraphMethods gm=new PCAGraphMethods(proj.getGraph(),proj.getDrawInfo(),proj.getDrawInfo3D(),UI.mainWindow);				 
		gm.setDrawInfo(new Rectangle(0, 0 ,width, height) );

		SVGGraphics2D gc = new SVGGraphics2D(width, height);
		SVGCanvas drawable = new SVGCanvas(gc);
		
		DrawTools dT = new DrawTools(drawable, proj.getDrawInfo(),proj.getDrawInfo3D(),
				proj.getGraph(),dimensions,new Point(width, height),UI.display);
				
		dT.drawGraph();
		
		return gc;

	}

	private SVGGraphics2D makePDFAdmixImage(AdmixProj proj, int width, int height, String path) {
		
		//final int a4width=500;
		//final int a4height=750;		

		SVGGraphics2D gc = new SVGGraphics2D(width, height);
		SVGCanvas drawable = new SVGCanvas(gc);
		
		for(int i=0;i<proj.getGraphs().size();i++){
			AdmixDrawTools dT = new AdmixDrawTools(drawable,proj.getDrawInfo().get(i),proj.getGraphs().get(i),
					new Point(width, height),UI.display,i);
			dT.drawGraph();
		}						
		
		return gc;


	}
	/**
	 * This method prompts the user for the path and 
	 * exports a PCA project to a PNG image file
	 * @param proj the PCA project  
	 * @param width the width of the image
	 * @param height the height of the image
	 */
	private void exportPNG(PCAProj proj, int width, int height) {
		String path=pngSaveDialog(proj.getGraph().getName()+".png");
		if(path==null||!confirmExport(path)){
			return;
		}		
		saveImage(proj,path,SWT.IMAGE_PNG, width, height);
	}


	/**
	 * This method prompts the user for the path and 
	 * exports a PCA project to a PDF image file
	 * @param proj the PCA project  
	 * @param width the width of the image
	 * @param height the height of the image
	 */
	private void exportPDF(PCAProj proj, int width, int height) {
		String path=pdfSaveDialog(proj.getGraph().getName()+".pdf");
		if(path==null||!confirmExport(path)){
			return;
		}

		SVGGraphics2D gc = makePDFPCAImage(proj,width,height,path);		
		
		savePDF(path, width, height, gc);
	}
	
	private void exportSVG(PCAProj proj, int width, int height) {
		String path=svgSaveDialog(proj.getGraph().getName()+".svg");
		if(path==null||!confirmExport(path)){
			return;
		}

		SVGGraphics2D gc = makePDFPCAImage(proj,width,height,path);		
		
		saveSVG(path, gc);
	}

	/**
	 * This method creates and saves the image onto the hard disk
	 * or other physical location at the path
	 * @param admixProj the project to be saved
	 * @param path the path to save the image 
	 * @param imageStyle the imageStyle
	 */
	private void saveImage(AdmixProj admixProj, String path, int imageStyle) {
		Image image = admixProj.getImg();
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] {image.getImageData()};
		loader.save(path, imageStyle);

	}

	/**
	 * This method prompts the user for the path and 
	 * exports a Admix project to a PNG image file
	 * @param proj the Admix project  
	 */
	private void exportPNG(AdmixProj admixProj) {
		String path=pngSaveDialog(admixProj.getName()+".png");
		if(path==null||!confirmExport(path)){
			return;
		}

		saveImage(admixProj,path,SWT.IMAGE_PNG);

	}

	/**
	 * This method prompts the user for the path and 
	 * exports a Admix project to a PDF file
	 * @param proj the Admix project  
	 */
	private void exportPDF(AdmixProj proj) {
		String path=pdfSaveDialog(proj.getName()+".pdf");
		if(path==null||!confirmExport(path)){
			return;
		}		

		int width=proj.getImageWidth();
		int height=proj.getImageHeight();
		
		SVGGraphics2D gc = makePDFAdmixImage(proj, width, height, path);
		
		savePDF(path, width, height, gc);

	}
	
	private void exportSVG(AdmixProj proj) {
		String path=svgSaveDialog(proj.getName()+".svg");
		if(path==null||!confirmExport(path)){
			return;
		}		

		int width=proj.getImageWidth();
		int height=proj.getImageHeight();
		
		SVGGraphics2D gc = makePDFAdmixImage(proj, width, height, path);

		saveSVG(path, gc);
	}

	/**
	 * This method creates and saves the image onto the hard disk
	 * or other physical location at the path
	 * @param PCAProj the project to be saved
	 * @param path the path to save the image 
	 * @param imageStyle the imageStyle
	 */
	private void saveImage(PCAProj proj, String path, int imageType,int width, int height) {
		Image image = makePCAImage(proj, width, height);
		ImageData imgData = image.getImageData();
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] {imgData};
		loader.save(path, imageType);

	}


	/**
	 * This method checks if the file at the path exists and if so, 
	 * asks the user whether to overwrite
	 * @return whether confirmation was reached
	 */
	private boolean confirmExport(String path) {
		if(UI.ui.fileExists(path)){
			if(UI.ui.mustOverWrite()){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	
	private void saveSVG(String path, SVGGraphics2D gc) {
		WaitDialog.start();
		BufferedWriter bw;
		try {

			bw = new BufferedWriter(new FileWriter(path));
			bw.write(gc.getSVGDocument());
			bw.close();
		} catch (IOException e) {
			UI.errorMessage("Unable to save SVG");
			WaitDialog.end();
		}
		WaitDialog.end();

	}
	
	private void savePDF(String path, int width, int height, SVGGraphics2D gc) {		
		WaitDialog.start();
		try {
			new SVGtoPDF().createPdf(gc.getSVGDocument(), new Point(width, height), path);
		} catch (IOException e) {
			UI.errorMessage("Unable to save PDF. "+e.getMessage());
			WaitDialog.end();
		} catch (DocumentException e) {
			UI.errorMessage("Unable to save PDF. "+e.getMessage());
			WaitDialog.end();
		}
		WaitDialog.end();

	}

	private String pdfSaveDialog(String fileName) {
		FileDialog saveDialog = new FileDialog(UI.mainWindow, SWT.SAVE|SWT.APPLICATION_MODAL);
		saveDialog.setFilterNames(new String[] { "PDF Files" });
		saveDialog.setFilterExtensions(new String[] { "*.PDF" }); 
		saveDialog.setFileName(fileName);
		return saveDialog.open();		
	}

	private String pngSaveDialog(String fileName) {
		FileDialog saveDialog = new FileDialog(UI.mainWindow, SWT.SAVE|SWT.APPLICATION_MODAL);
		saveDialog.setFilterNames(new String[] { "PNG Files" });
		saveDialog.setFilterExtensions(new String[] { "*.PNG" }); 
		saveDialog.setFileName(fileName);
		return saveDialog.open();		
	}
	
	private String svgSaveDialog(String fileName) {
		FileDialog saveDialog = new FileDialog(UI.mainWindow, SWT.SAVE|SWT.APPLICATION_MODAL);
		saveDialog.setFilterNames(new String[] { "SVG Files" });
		saveDialog.setFilterExtensions(new String[] { "*.SVG" }); 
		saveDialog.setFileName(fileName);
		return saveDialog.open();		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {}
}
