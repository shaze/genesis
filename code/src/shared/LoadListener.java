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

import io.AdmixSaveAndLoad;
import io.PCASaveAndLoad;

import java.io.FileNotFoundException;
import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

import main.UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolItem;

import pca.PCAGraph;
import pca.PCAMouseListener;
import pca.PCAProj;
import admix.AdmixProj;
import admix.AdmixSerializable;


public class LoadListener implements SelectionListener{
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
	    System.gc();
		String path = loadDialog();
		boolean loaded=false;
		if(path==null){
			return;
		}

		try {
			loaded=loadPCA(path);
			if(!loaded){
				loadAdmix(path);
			}

		}catch (NotSerializableException e) {				
			invalidFileMessageBox();
			return;

		}catch(StreamCorruptedException e){
			invalidFileMessageBox();
			return;
		}catch (FileNotFoundException e) {
			fileNotFoundMessageBox();
			return;

		}catch (Exception e) {	
			e.printStackTrace();
			errorMessageBox();
			return;

		}			     			 

	}

	private void loadAdmix(String path) throws Exception {
		AdmixSerializable serializable = AdmixSaveAndLoad.loadAdmix(path);
		AdmixProj proj = serializable.toProj();

		UI.ui.enableButtons();
		TabItem newTab=UI.ui.newTab(proj.getName());
		proj.setTab(newTab);		 
		UI.admixProjects.add(proj);
		UI.ui.drawGraph();			
		UI.ui.enableAdmixControls();
	}

	private String loadDialog() {
		FileDialog dialog = new FileDialog(UI.mainWindow, SWT.APPLICATION_MODAL);
		dialog.setFilterNames(new String[] { "Genesis Graph Files","All Files" });
		dialog.setFilterExtensions(new String[] { "*.ggf","*.*" }); 
		String path = dialog.open();
		return path;
	}

	private boolean loadPCA(String path) throws Exception{			
		try {
			PCAGraph graph = PCASaveAndLoad.loadPCA(path);

			PCAProj proj = new PCAProj(graph);

			UI.ui.enableButtons();
			TabItem newTab=UI.ui.newTab(graph.getName());
			proj.setTab(newTab);
			ScrolledComposite comp = (ScrolledComposite)newTab.getControl();			 
			UI.pcaProjects.add(proj);
			UI.ui.drawGraph();			 
			UI.ui.enablePCAControls();

			if(proj.getGraph().z==-1){
				for(ToolItem t : UI.toBeEnabledIf3D){
					t.setEnabled(false);
				}
			}else{
				for(ToolItem t : UI.toBeEnabledIf3D){
					t.setEnabled(true);
				} 
			}

			comp.addMouseListener(new PCAMouseListener(proj));

		}catch(ClassCastException e){
			return false;
		}

		return true;


	}
	
	private void fileNotFoundMessageBox() {
		MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_ERROR);
		messageBox.setMessage("File Not Found.");
		messageBox.setText("Sorry!");
		messageBox.open();	
	}

	void errorMessageBox() {
		MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_ERROR);
		messageBox.setMessage("An error has been encountered.");
		messageBox.setText("Sorry!");
		messageBox.open();	
	}

	private void invalidFileMessageBox() {
		MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_ERROR);
		messageBox.setMessage("Invalid File.");
		messageBox.setText("Sorry!");
		messageBox.open();	
	}

}