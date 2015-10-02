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

package shared;

import io.AdmixSaveAndLoad;
import io.PCASaveAndLoad;

import java.io.IOException;

import main.UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;

import pca.PCAGraph;
import pca.PCAProj;
import admix.AdmixGraph;
import admix.AdmixProj;
import admix.AdmixSerializable;

/**
 * This class contains the listener that listens if the save button is clicked 
 * as well as the methods involved in the process.
 */
public class SaveListener implements SelectionListener{
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {}

	@Override
	public void widgetSelected(SelectionEvent arg0) {	
		try{
			PCAProj proj = UI.ui.findPCAProjByTab(UI.tabs.getSelection()[0]);
			if(proj!=null){
				savePCA(proj);
			}
			AdmixProj admixProj = UI.ui.findAdmixProjByTab(UI.tabs.getSelection()[0]);
			if(admixProj!=null){
				saveAdmix(admixProj);
				addProjectPointers(admixProj);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			//this happens when there is no tab open
		}

	}

	/**
	 * This method reassigns the proj variable (pointer) in each graph of the project that
	 * need to be removed while saving the project
	 * @param proj the project to be saved
	 */
	private void addProjectPointers(AdmixProj proj) {
		for(AdmixGraph graph:proj.getGraphs()){
			graph.setProj(proj);
		}

	}

	/**
	 * This method saves an admixture project to a path that will be requested
	 * from the user
	 * @param proj the admixture project to be saved
	 */
	private void saveAdmix(AdmixProj proj) {
		String path = admixSaveDialog(proj);
		if(path==null){
			return;
		}
		try {
			AdmixSerializable serializable = new AdmixSerializable(proj);
			AdmixSaveAndLoad.saveAdmix(serializable, path);
		}catch (IOException e) {
			e.printStackTrace();
			errorMessageBox();
			return;
		}
		admixSuccessMessageBox();
	}

	/**
	 * This method saves an PCA project to a path that will be 
	 * requested from the user
	 * @param proj the PCA project to be saved
	 */
	public void savePCA(PCAProj proj) {
		String path = pcaSaveDialog(proj);
		if(path==null){
			return;//means dialog was cancelled
		}

		try {
			PCASaveAndLoad.savePCA(proj.getGraph(), path);
		} catch (IOException e) {	
			e.printStackTrace();
			errorMessageBox();
			return;
		}
		pcaSuccessMessageBox();

	}

	/**
	 * This method creates a message box to be displayed when a PCA project is 
	 * successfully saved
	 */
	private void pcaSuccessMessageBox() {
		MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_INFORMATION);
		messageBox.setMessage("PCA Project Saved");
		messageBox.setText("Success");
		messageBox.open();	

	}

	/**
	 * This method creates a message box to be displayed when an Admix project is 
	 * successfully saved
	 */
	private void admixSuccessMessageBox() {
		MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_INFORMATION);
		messageBox.setMessage("Admix Project Saved");
		messageBox.setText("Success");
		messageBox.open();	

	}

	/**
	 * This method prompts the user to select the path to save an admixture project
	 * @param proj the admixture project to be saved
	 * @return the path to save the file
	 */
	private String admixSaveDialog(AdmixProj proj) {
		String path = saveDialog(proj.getName());

		if (path==null){
			return null;
		}

		if(UI.ui.fileExists(path)){
			if(UI.ui.mustOverWrite()){
				setAdmixProjNameToFileName(proj, path);				 
			}else{
				return admixSaveDialog(proj); 
			}
		}else{				
			setAdmixProjNameToFileName(proj, path); 
		}
		return path;

	}

	/**
	 * This method creates the SWT save dialog
	 * @param name the name of the project to be saved
	 * @return path the file is to be saved to
	 */
	private String saveDialog(String name) {
		FileDialog dialog = new FileDialog(UI.mainWindow, SWT.SAVE|SWT.APPLICATION_MODAL);
		dialog.setFilterNames(new String[] { "Genesis Graph Files" });
		dialog.setFilterExtensions(new String[] { "*.ggf" }); 			
		dialog.setFileName(name+".ggf");
		String path = dialog.open();
		return path;
	}
	/**
	 * This method sets the name variable of the given Admixture
	 * project to the name of the file just saved
	 * @param proj the Admixture project
	 * @param path the path where the file was just saved
	 */
	private void setAdmixProjNameToFileName(AdmixProj proj, String path) {
		int lastSlash = Math.max(path.lastIndexOf('\\')+1, path.lastIndexOf('/')+1);
		String name=path.substring(lastSlash, path.lastIndexOf('.'));
		proj.setName(name);		
		proj.getTab().setText(name);			
	}

	/**
	 * This method prompts the user to select the path to save a PCA project
	 * @param proj the PCA project to be saved
	 * @return the path to save the file
	 */
	private String pcaSaveDialog( PCAProj proj) {
		PCAGraph graph = proj.getGraph();
		String path = saveDialog(graph.getName());

		if (path==null){
			return null;
		}

		if(UI.ui.fileExists(path)){
			if(UI.ui.mustOverWrite()){
				setPCAProjNameToFileName(proj, path);
			}else{
				return pcaSaveDialog(proj); 
			}
		}else{				
			setPCAProjNameToFileName(proj, path);			
		}

		return path; 		 
	}

	/**
	 * This method sets the name variable of the given PCA
	 * project to the name of the file just saved
	 * @param proj the PCA project
	 * @param path the path where the file was just saved
	 */
	private void setPCAProjNameToFileName(PCAProj proj, String path) {
		int lastSlash = Math.max(path.lastIndexOf('\\')+1, path.lastIndexOf('/')+1);
		String name=path.substring(lastSlash, path.lastIndexOf('.'));
		proj.getGraph().setName(name);		
		proj.getTab().setText(name);			
	}

	/**
	 * This method creates a generic error box
	 */
	void errorMessageBox() {
		MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_ERROR);
		messageBox.setMessage("An I/O error has been encountered.");
		messageBox.setText("Sorry!");
		messageBox.open();	

	}
}

