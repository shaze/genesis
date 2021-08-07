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
 */

package pca.dialogs;

import genesisDrawable.SWTCanvas;

import java.text.DecimalFormat;

import main.UI;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import pca.PCASubject;
import pca.Subject;
import pca.drawTools.DrawIcon;
import pca.icon.Icon;

/**
 * This class creates a dialog giving the user all options related to an individual 
 * subject on a PCA Graph.
 * 
 * @author R W Buchmann
 */
public class SubjectDialog {
	/**
	 * The size of the icon
	 */
	private int size;
	/**
	 * The RGB object of the colour of the icon
	 */
	private RGB colour;
	/**
	 * The Color object for the colour of the icon
	 */
	private Color col;
	/**
	 * Whether the subject is to have all data (colours etc) cleared
	 */
	private boolean clear=false;
	/**
	 * Whether the subject's colour was changed by this dialog
	 */
	private boolean colChanged=false;
	/**
	 * Whether the subject's icon was changed by this dialog
	 */
	private boolean iconChanged=false;
	/**
	 * Whether the subject is/is to be hidden
	 */
	private boolean	deleted=false;
	/**
	 * Whether the subject is/is to be set to be on top
	 */
	private boolean onTop=false;	
	/**
	 * The default icon. This is set to be the subject's icon before the 
	 * dialog was invoked.
	 */
	private int defaultIcon;
	/**
	 * The new icon chose by this dialog
	 */
	private int icon;
	/**
	 * The relevant PCASubject
	 */
	private PCASubject subject;
	/**
	 * The coordinates of the mouse click that brought up the dialog
	 */
	private Point pos;
	/**
	 * The current selected axes
	 */
	private int x,y,z;
	/**
	 * The dialog window
	 */
	private Shell shell;
	/**
	 * The current display
	 */
	private Display display;

	/**The constuctor of the class
	 * 
	 * @param subject the subject whose options we are to view
	 * @param pos the coordinates of the mouse click that brought up the dialog
	 * @param x the current x axis
	 * @param y the current y axis
	 * @param z the current z axis (-1 if 2D)
	 */
	public SubjectDialog(Subject subject,Point pos,int x,int y,int z) {
		
		this.subject=subject.getSubj();		
		this.defaultIcon=this.subject.getGroup().getIconSymbol();
		this.pos = pos;
		this.deleted=(!subject.getSubj().getVisible());
		this.x=x;
		this.y=y;
		this.z=z;
		this.colour=subject.getColour();
		this.icon=subject.getIcon();
		this.size=subject.getIconSize();
		if(size<2){
			size=this.subject.getGroup().getIconSize();
		}
	}

	/**
	 * @return whether or not the subject is to be placed on top
	 */
	public boolean isOnTop() {
		return onTop;
	}

	/**
	 * @return whether or not the subject is to be deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}
	/**
	 * @return whether or not the subject is to have its icon information cleared
	 */
	public boolean getClear(){
		return clear;
	}
	/**
	 * @return the new colour
	 */  
	public RGB getColour(){
		return colour;
	}
	/**
	 * @return the new icon
	 */    
	public int getIcon(){		
		return icon;
	}
	/**
	 * @return the new icon size
	 */
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	/**
	 * @return whether or not the colour is to be changed
	 */
	public boolean colourWasChanged(){
		return colChanged;
	} 
	/**
	 * @return whether or not the icon is to be changed
	 */
	public boolean iconWasChanged(){
		return iconChanged;
	}

	/**Creates the Dialog
	 * @param display The current display
	 */
	public void run(Display display) {		
		this.display=display;
		shell = new Shell(display,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);
		shell.setText(subject.getName());		
		
		createContents();
		shell.pack();
		shell.open();
		setWindowLocation();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		// Dispose the colour we created for the Label
		if (col != null) {
			col.dispose();
		}		   
	}

	/**
	 * Sets the position of the window on the screen	 
	 */
	private void setWindowLocation() {
		
		Point panelCorner = UI.mainWindow.toDisplay(new Point(0,0));
		pos.x+=panelCorner.x;		
		int xLoc=Math.min(((pos.x+50)+UI.mainWindow.getLocation().x),display.getBounds().width-330);
		int yLoc=Math.min(((pos.y-75)+UI.mainWindow.getLocation().y),display.getBounds().height-550);	
		if(windowCoveringPoint(xLoc)){
			xLoc=pos.x-shell.getBounds().width-10;
		}
		shell.setLocation(new Point(xLoc,yLoc));
		
	}

	/**
	 * Checks if the dialog is covering the relevant point on the graph
	 * @param x the proposed x coordinate of the window
	 * @return whether the dialog is covering the relevant point on the graph
	 */
	private boolean windowCoveringPoint(int x) {		
		return pos.x>x-15&&pos.x<x+shell.getBounds().width+15;
	}

	/**
	 * Creates the contents (buttons etc.) for this dialog 
	 */
	private void createContents() {		
		shell.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(SWT.FILL,SWT.FILL,true,true);
		//heading
		Label heading = new Label(shell, SWT.CENTER);
		heading.setLayoutData(gd);
		heading.setText(subject.getName());	
		//position data
		Label coords = new Label(shell, SWT.CENTER);
		coords.setLayoutData(gd);
		DecimalFormat df = new DecimalFormat("#.####");
		if(z!=-1){
			coords.setText("("+df.format(subject.getData()[x])+", "+df.format(subject.getData()[y])+", " 
					+ df.format(subject.getData()[z])+")");
		}else{
			coords.setText("("+df.format(subject.getData()[x])+", "+df.format(subject.getData()[y])+")");	
		}
		//blank
		Label labelBlank3 = new Label(shell, SWT.CENTER);
		labelBlank3.setLayoutData(gd);
		labelBlank3.setText("--------------------");

		if (subject.hasPheno()){
			//pheno labels
			Label labelPheno = new Label(shell, SWT.CENTER);
			labelPheno.setLayoutData(gd);
			labelPheno.setText("Phenotype Data:");

			Composite comp = new Composite(shell, SWT.BORDER);
			GridData compositeImportData = new GridData(SWT.FILL, SWT.FILL, true, false,
					subject.getPhenotypeData().length+1,1);
			comp.setLayoutData(compositeImportData);
			comp.setLayout(new GridLayout(1, false));

			//pheno data
			//LinkedList<Label> phenoLabels =new LinkedList<Label>();

			for(String str:subject.getPhenotypeData()){
				Label n = new Label(comp, SWT.LEFT);
				n.setLayoutData(gd);
				n.setText("      "+str);
			}

			//blank
			Label labelBlank6 = new Label(shell, SWT.CENTER);
			labelBlank6.setLayoutData(gd);
			labelBlank6.setText("             ");	
		}
		// delete individual checkbox
		Button deleteBox = new Button(shell, SWT.CHECK);
		deleteBox.setText("Hide this Individual from the Plot");
		if(!subject.getVisible()){
			deleteBox.setSelection(true);
		}
		//onTop checkBox
		Button onTopBox = new Button(shell, SWT.CHECK);
		onTopBox.setText("Place this Individual on top");
		if(subject.isOnTop()){
			onTopBox.setSelection(true);
		}


		// clear data checkbox
		Button clearCheckBox = new Button(shell, SWT.CHECK);
		clearCheckBox.setText("Clear all icon data specific to this individual");

		// blank label for layout purposes
		Label labelBlank = new Label(shell, SWT.CENTER);
		labelBlank.setLayoutData(gd);
		labelBlank.setText("             ");	

		// Start with old clour
		col = new Color(shell.getDisplay(),colour);

		//creates grid for buttons
		Composite buttonlayout = new Composite(shell, SWT.BORDER);
		GridData compositeImportData = new GridData(SWT.FILL, SWT.FILL, true, false);
		buttonlayout.setLayoutData(compositeImportData);
		GridLayout gl=new GridLayout(2, false);
		gl.marginTop=35;
		buttonlayout.setLayout(gl);

		// Use a label full of spaces to show the colour

		final Label color = new Label(buttonlayout, SWT.PUSH);
		color.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));		    
		color.setText("   ");
		color.setBackground(new Color(display,255,255,255));		    
		color.addPaintListener(new paintListener(color));

		//create change colour buttons
		Button buttonCol = new Button(buttonlayout, SWT.NONE);
		buttonCol.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
		buttonCol.setText("Change Icon");

		//create done button		    
		Composite compButtons= new Composite(shell, SWT.BORDER);
		compositeImportData = new GridData(SWT.FILL, SWT.FILL, true, false);
		compButtons.setLayoutData(compositeImportData);
		gl=new GridLayout(2, false);
		gl.marginTop=35;
		compButtons.setLayout(gl);

		Button donebutton = new Button(compButtons, SWT.NONE);
		donebutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
		donebutton.setText("Done");
		
		Button canbutton = new Button(compButtons, SWT.NONE);
		canbutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
		canbutton.setText("Cancel");

		clearCheckBox.addSelectionListener(new checkBoxListener(clearCheckBox,deleteBox,buttonCol));
		deleteBox.addSelectionListener(new checkBoxListener(clearCheckBox,deleteBox,buttonCol));		 

		canbutton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				colChanged=false;
				iconChanged=false;
				shell.dispose();
			}
		});

		buttonCol.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				IconDialog dialog = new IconDialog(
						new Icon(icon, size, colour), subject.getGroup().getIcon());
				dialog.run(display);
				if(!dialog.getCancelled()){
					icon = dialog.getIcon().getIcon();
					colour = dialog.getIcon().getColour();
					size = dialog.getIcon().getSize();		
					color.redraw();
				}
				 
			}
		});	  				

		donebutton.addSelectionListener(new DoneButton(shell,clearCheckBox,deleteBox,onTopBox));
	}

	

	/**
	 * Acts as a listener for the delete and clear checkboxes. 
	 * Disables the colour/icon selection combo boxes if the subject is to 
	 * be deleted or its icon data specific to this individual is to be cleared.
	 */
	private class checkBoxListener extends SelectionAdapter{
		Button clearCheckBox,deleteBox;
		Button buttonCol;

		public checkBoxListener(Button clearCheckBox,Button deleteBox,Button button) {				
			this.clearCheckBox=clearCheckBox;
			this.deleteBox=deleteBox;

			this.buttonCol = button;
		}

		public void widgetSelected(SelectionEvent e) {	
			if (clearCheckBox.getSelection()||deleteBox.getSelection()){
				buttonCol.setEnabled(false);
			}else{
				buttonCol.setEnabled(true);
			}
		}
	}

	/**
	 * Acts as the listener for the done button.
	 * Sets all the variables and closes the shell.
	 */
	private class DoneButton extends SelectionAdapter{
		Shell shell;
		Button checkBox,deleteBox, onTopBox;

		public DoneButton(Shell shell,Button checkBox,Button deleteBox,Button onTopBox) {				
			this.deleteBox=deleteBox;
			this.shell = shell;
			this.checkBox = checkBox;
			this.onTopBox = onTopBox;
		}

		public void widgetSelected(SelectionEvent e) {	
			if(deleteBox.getSelection()){
				colChanged=false;
				deleted=true;

			}else{
				deleted=false;
				iconChanged=true;
				colChanged=true;
				if(checkBox.getSelection()){				
					colChanged=false;
					clear=true;					
				}
				if(onTopBox.getSelection()){
					onTop = true;
				}
			}
			shell.dispose();
		}						
	}	
	
	
	/**
	 *	The paint listener for the icon preview
	 */
	private class paintListener implements PaintListener{		
		Label label;
		public paintListener(Label label) {
			this.label = label;
		}

		@Override
		public void paintControl(PaintEvent e) {
			int x=(label.getSize().x)/2;
			int y=(label.getSize().y)/2;
			int selectedIcon = icon;
			selectedIcon = selectedIcon==6 ? defaultIcon : selectedIcon;
									
			
			SWTCanvas canvas = new SWTCanvas(e.gc);
			
			DrawIcon.drawIcon(canvas, new Point(x, y), selectedIcon, 
					subject.getGroup().getBorder(), colour, size, false);
		}
		
	}

}



