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
import main.UI;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import pca.dialogs.IconDialog;
import pca.drawTools.DrawIcon;
import pca.icon.Icon;

/**
 * This class creates the dialog used to edit options of a PCA 
 * population group. The dialog created when an element in the 
 * key is clicked.
 */

public class PCAGroupDialog {
	private RGB oldColour, colour;
	private Color col;
	private boolean deleted, colChanged=false,iconChanged=false,nameChanged=false,
			border, up=false,down=false;
	private int oldIcon ,icon, size;
	private PCAPopulationGroup group;
	private String name;
	private int noGroups;
	private PCAGraph graph;
	private Button buttonDec, buttonInc;
	private Shell shell;

	/**
	 * The Constructor of the class.
	 * 
	 * @param group the population group whose options we are viewing
	 * @param graph the PCAGraph which the group is a part of
	 */
	public PCAGroupDialog(PCAPopulationGroup group,PCAGraph graph) {
		this.group=group;
		this.oldColour=group.getColour();
		this.oldIcon=group.getIconSymbol();	
		this.border=group.getBorder();
		this.icon = oldIcon;
		this.colour=oldColour;
		this.size=group.getIconSize();
		this.noGroups=PCAWorkflow.getMaxVisibleOrder(graph);				
		this.graph=graph;
	}

	public boolean getDeleted() {
		return deleted;
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
	 * @return the new name
	 */
	public String getName(){
		return name;
	}

	/**
	 * @return whether or not the name was changed
	 */
	public boolean nameWasChanged(){
		return nameChanged;
	} 
	/**
	 * @return whether or not the colour was changed
	 */
	public boolean colourWasChanged(){
		return colChanged;
	} 
	/**
	 * @return whether or not the icon was changed
	 */
	public boolean iconWasChanged(){
		return iconChanged;
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
	
	public boolean getBorder(){
		return border;
	}

	/**
	 * Begins the dialog
	 * @param display the current display
	 */
	public void run() {	
		
		shell = new Shell(UI.display,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);
		shell.setText(group.getName());		
		createContents(shell);

		shell.pack();
		shell.open();
		centreOnScreen();
		while (!shell.isDisposed()) {
			if (!UI.display.readAndDispatch()) {
				UI.display.sleep();
			}
		}
		// Dispose the colour we created for the Label
		if (col != null) {
			col.dispose();
		}		   

	}
	
	Combo Shape;
	private void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(1, false));

		GridData gd = new GridData(SWT.FILL,SWT.FILL,true,true);
		Label heading = new Label(shell, SWT.CENTER);

		heading.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		heading.setText("Group: "+group.getDisplayName()+"");
		col = new Color(shell.getDisplay(),oldColour);

		//creates grid for buttons
		Composite buttonlayout = new Composite(shell, SWT.BORDER);
		GridData compositeImportData = new GridData(SWT.FILL, SWT.FILL, true, false);
		buttonlayout.setLayoutData(compositeImportData);
		GridLayout gl=new GridLayout(2, false);
		gl.marginTop=35;
		gl.marginBottom=0;
		buttonlayout.setLayout(gl);

		// Use a label full of spaces to show the colour

		final Label color = new Label(buttonlayout, SWT.PUSH);
		color.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));		    
		color.setText("   ");
		color.setBackground(new Color(UI.display,255,255,255));		    
		color.addPaintListener(new paintListener(color));

		//create change colour buttons
		Button button = new Button(buttonlayout, SWT.NONE);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); //*
		button.setText("Change Icon");

		Label blank = new Label(shell, SWT.CENTER);
		blank.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		blank.setText("                                                           " +
				"          ");//this sets the width of the window lol

		Label changeName = new Label(shell, SWT.CENTER);
		changeName.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		changeName.setText("Enter a Display Name:");

		Text textName=new Text(shell, SWT.CENTER);
		textName.setLayoutData(gd);
		textName.setText(group.getDisplayName());

		Button deleteBox = new Button(shell, SWT.CHECK);
		deleteBox.setText("Hide this Group from the graph");		
		deleteBox.setSelection(!group.getVisible());
		deleted=!group.getVisible();
		
		Composite compOrder= new Composite(shell, SWT.BORDER);		    
		compOrder.setLayoutData(compositeImportData);
		gl=new GridLayout(2, false);
		gl.marginTop=35;
		gl.marginBottom=0;
		compOrder.setLayout(gl);

		

		
		buttonInc = new Button(compOrder, SWT.NONE);
		buttonInc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
		buttonDec = new Button(compOrder, SWT.NONE);
		buttonDec.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 

		buttonInc.addSelectionListener(new ShiftButtons(true));		
		buttonDec.addSelectionListener(new ShiftButtons(false));
		
		checkShiftButtonsEnabled();

		if(graph.getKeyPosition()==0){
			buttonInc.setText("Shift Up");
			buttonDec.setText("Shift Down");
		}else{
			buttonInc.setText("Shift Left");
			buttonDec.setText("Shift Right");
		}

		Composite compButtons= new Composite(shell, SWT.BORDER);		    
		compButtons.setLayoutData(compositeImportData);
		gl=new GridLayout(2, false);
		gl.marginTop=35;
		gl.marginBottom=0;
		compButtons.setLayout(gl);


		//create done button
		Button donebutton = new Button(compButtons, SWT.NONE);
		donebutton.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true)); //*
		donebutton.setText("Done");

		Button canbutton = new Button(compButtons, SWT.NONE);
		canbutton.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true)); //*
		canbutton.setText("Cancel");
		canbutton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				colChanged=false;
				iconChanged=false;
				shell.dispose();
			}
		});

		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				
				IconDialog dialog = new IconDialog(
						new Icon(icon, size, colour), group.getIcon(), true);
				dialog.run(UI.display);
				if(!dialog.getCancelled()){
					icon = dialog.getIcon().getIcon();
					colour = dialog.getIcon().getColour();
					size = dialog.getIcon().getSize();
					border=dialog.getIcon().getBorder();
					color.redraw();					
				}
			}
		});		

		//if OK button is pressed, closes window
		donebutton.addSelectionListener(new DoneButton(Shape,shell,textName,deleteBox,UI.display));

	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}
	public void setDown(boolean down) {
		this.down = down;
	}


    private class DoneButton extends SelectionAdapter{
	private Shell shell;
	private Text text;
	private Button deleteBox;

	public DoneButton(Combo shape,Shell shell,Text textBox, Button deleteBox ,Display display) {				
	    this.deleteBox=deleteBox;
	    this.shell = shell;
	    this.text=textBox;
	}

	public void widgetSelected(SelectionEvent e) {
	    deleted=deleteBox.getSelection();	
	    if(!group.getName().equals(text.getText().trim())){
		name = text.getText().trim();
		nameChanged=true;
	    }
	    iconChanged=true;
	    shell.dispose();
	}


    }

	
    private void checkShiftButtonsEnabled() {
	buttonInc.setEnabled(group.getOrder()>0);
	buttonDec.setEnabled(group.getOrder()<noGroups);
	// This is not correct -- if we hide intermediate groups then the
	// condition could be true!
	//if(group.getOrder()==noGroups-1){
	//    buttonDec.setEnabled(false);
	//}else{
	//    buttonDec.setEnabled(true);
	//}
    }

    private class paintListener implements PaintListener{

	Label label;			

	public paintListener(Label label){
	    this.label = label;
	}
			        		        		  
	@Override
	    public void paintControl(PaintEvent e) {
	    int x=(label.getSize().x)/2;
	    int y=(label.getSize().y)/2;		
			
	    SWTCanvas canvas = new SWTCanvas(e.gc);			
	    DrawIcon.drawIcon(canvas, new Point(x, y), icon, border, colour, size, false);
	}

    }
	
    private class ShiftButtons extends SelectionAdapter{
	private boolean up ;
		
	public ShiftButtons(boolean up){
	    this.up = up;
	}
	
	public void widgetSelected(SelectionEvent event) {					
	    int delta;
	    delta = up ? -1 : +1;
	    PCAWorkflow.shiftPop(group, graph, delta);
	    checkShiftButtonsEnabled();
	    UI.ui.drawGraph();
	    bringToFront(shell);
	}

	private void bringToFront(final Shell shell) {
	    shell.getDisplay().asyncExec(new Runnable() {
		    public void run() {		            
			shell.forceActive();
		    }
		});
	}
		
    }
		
    /**
     * Centers the dialog on the screen.
     */
    private void centreOnScreen() {
	int x=(UI.display.getBounds().width-shell.getBounds().width)/2;
	int y=(UI.display.getBounds().height-shell.getBounds().height)/2;;

	shell.setLocation(new Point(x,y));

    }


}



