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



import main.UI;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This class creates the dialog for the user to change options concerning the 
 * {@link Ancestor} in the admixture graph. Created by {@link UIAdmixture}
 * 
 *
 */
public class AdmixGroupDialog {
	Shell shell;
	
	private RGB oldColour, colour;
	private RGB[] customCols;
	private Color col;
	private boolean colChanged=false,nameChanged=false,
			horizontal, cancelled=false;
	private Ancestor group;
	private String name;
	private int noGroups;
	private AdmixGraph graph; 




	/**The constructor 
	 * @param group the Ancestor the dialog is concerned with
	 * @param pos the coordinates of the mouse click that brought up the dialog
	 * @param noGroups the total amount of selected ancestors in the Admixture Graph
	 * @param relOrder the order of the ancestor relative to the other shown ancestors
	 */
	public AdmixGroupDialog(Ancestor group, AdmixGraph graph, RGB[] customCols) {
		this.group=group;
		this.oldColour=group.getColour();
		this.colour=oldColour;
		this.noGroups =  graph.getAncestors().length;
		this.customCols=customCols;
		this.horizontal=graph.getProj().isHorizontal();
		this.graph = graph;
	}

	public RGB getColour(){
		return colour;
	}


	public String getName(){
		return name;
	}

	public boolean nameWasChanged(){
		return nameChanged;
	} 

	public boolean colourWasChanged(){
		return colChanged;
	} 

	/**Begins the dialog.
	 * @param display the current display
	 */
	public void run(Display display) {		    

		shell = new Shell(display,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);
		shell.setText(group.getName());	

		createContents();

		shell.pack();
		shell.open();
		centreOnScreen();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		if (col != null) {
			col.dispose();
		}		   

	}
	
	private Button buttonInc, buttonDec;
	
	private void createContents() {
   	        GridLayout gl = new GridLayout(1, false);
		shell.setLayout(gl);

		GridData gd = new GridData(GridData.FILL,GridData.FILL,false,false);
		Label heading = new Label(shell, SWT.CENTER);

		heading.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false));
		heading.setText("Ancestor: "+group.getDisplayName()+"");
		//This creates a label
		Label Label1 = new Label(shell,GridData.FILL);
		Label1.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false));
		Label1.setText("                  Select Colour for Ancestor                   ");

		// Start with previous colour
		col = new Color(shell.getDisplay(),oldColour);
		Composite buttonlayout = new Composite(shell, SWT.BORDER);
		GridData compositeImportData
		    = new GridData(GridData.FILL, GridData.FILL, true, false);
		buttonlayout.setLayoutData(compositeImportData);
		GridLayout layout= new GridLayout(2, false);
		layout.marginTop=25;
		layout.marginBottom=0;				
		buttonlayout.setLayout(layout);

		final Label labelColour = new Label(buttonlayout, SWT.PUSH);
		labelColour.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));		    
		labelColour.setText("   ");
		labelColour.setBackground(new Color(UI.display,oldColour));		    

		Button button = new Button(buttonlayout, SWT.NONE);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false)); 
		button.setText("Select Colour");

		Label blank = new Label(shell, SWT.CENTER);
		blank.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		blank.setText("                                                  ");

		Label changeName = new Label(shell, SWT.CENTER);
		changeName.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false));
		changeName.setText("Enter a Display Name:");

		Text textBox=new Text(shell, SWT.CENTER|SWT.BORDER);

		textBox.setLayoutData(gd);
		textBox.setText(group.getDisplayName());

		Composite compOrder= new Composite(shell, SWT.BORDER);		    
		compositeImportData
		    = new GridData(GridData.FILL, GridData.FILL, true, false);
		compOrder.setLayoutData(compositeImportData);
		layout= new GridLayout(2, false)
;		layout.marginTop=25;
		layout.marginBottom=0;		
		compOrder.setLayout(layout);

		buttonInc = new Button(compOrder, SWT.NONE);
		buttonInc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,false));    
		buttonDec = new Button(compOrder, SWT.NONE);
		buttonDec.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		checkShiftButtonsEnabled();
		
		buttonInc.addSelectionListener(new ShiftButtons(true));
		buttonDec.addSelectionListener(new ShiftButtons(false));
		if(horizontal){
			buttonInc.setText("Shift Up");
			buttonDec.setText("Shift Down");
		}else{
			buttonInc.setText("Shift Left");
			buttonDec.setText("Shift Right");
		}
		Composite compButtons= new Composite(shell, SWT.BORDER);		    
		compositeImportData
		    = new GridData(GridData.FILL, GridData.FILL, true, false);
		compButtons.setLayoutData(compositeImportData);
		layout= new GridLayout(2, false);
		layout.marginTop=25;
		layout.marginBottom=0;				
		compButtons.setLayout(layout);

		Button donebutton = new Button(compButtons, SWT.NONE);
		donebutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false)); //*
		donebutton.setText("Done");

		Button canbutton = new Button(compButtons, SWT.NONE);
		canbutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false)); //*
		canbutton.setText("Cancel");
		canbutton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				cancelled=true;
				colChanged=false;
				shell.dispose();
			}
		});

		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				ColorDialog dlgbox = new ColorDialog(shell);
				dlgbox.setRGBs(customCols);
				dlgbox.setRGB(labelColour.getBackground().getRGB());
				dlgbox.setText("Choose a Colour");
				RGB rgb = dlgbox.open();

				if (rgb != null) {
					col.dispose();
					// Dispose the old colour, create the new one, and set into the label
					colChanged=true;
					colour = rgb;
					col = new Color(UI.display,rgb);		         		        			        	
					labelColour.setBackground(col);
					col.dispose();
				}
			}
		});

		donebutton.addSelectionListener(new DoneButton(shell,textBox));

	}

	private void centreOnScreen() {
		int x=(UI.display.getBounds().width-shell.getBounds().width)/2;
		int y=(UI.display.getBounds().height-shell.getBounds().height)/2;;

		shell.setLocation(new Point(x,y));

	}

	public boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	private void checkShiftButtonsEnabled() {
		buttonInc.setEnabled(!(group.getOrder()==0));
		buttonDec.setEnabled(!(group.getOrder()==noGroups-1));	
	}

	private class DoneButton extends SelectionAdapter{
		Shell shell;
		Text text;
		public DoneButton(Shell shell,Text textBox) {				
			this.shell = shell;
			this.text=textBox;
		}
		public void widgetSelected(SelectionEvent e) {
			if(!group.getName().equals(text.getText().trim())){

				name = text.getText().trim();
				nameChanged=true;
			}

			shell.dispose();
		}


	}



	private class ShiftButtons extends SelectionAdapter{
		private boolean up ;

		public ShiftButtons(boolean up){
			this.up = up;
		}

		public void widgetSelected(SelectionEvent event) {					
		    if (up) 
			AdmixWorkflow.shiftAncUp(group,graph);
		    else
			AdmixWorkflow.shiftAncDown(group,graph);
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


}



