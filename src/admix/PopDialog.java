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
 * {@link AdmixPopulationGroup}s in the admixture graph. Created by {@link UIAdmixture}
 * 
 */
public class PopDialog {
	private boolean deleted,up=false,down=false,cancelled=false;
	private AdmixPopulationGroup group;
	private String name;
	private int noGroups;
	private AdmixProj proj;
	private Button buttonDec, buttonInc;
	private Shell shell;

    public PopDialog(AdmixPopulationGroup group, AdmixProj proj) {
	AdmixGraph g;
	g = proj.getGraphs().get(0);
	this.group=group;
	this.setName(group.getDisplayName());
	this.noGroups=g.getMaxVisibleOrder();			
	this.proj = proj;
    }

	/**
	 * Returns whether or not the group is to be hidden/deleted from the graph
	 * @return whether or not the group is to be hidden/deleted from the graph
	 */
	public boolean getDeleted() {
		return deleted;
	}	

	/**Begins the dialog.
	 * 
	 * @param display the current display
	 */
	public void run(Display display) {		    

		shell = new Shell(display,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);
		shell.setText(group.getDisplayName());	
		createContents(shell,display);

		shell.pack();
		shell.open();
		centreOnScreen(shell, display);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}


	}

	private void centreOnScreen(Shell shell,Display display) {
		int x=(display.getBounds().width-shell.getBounds().width)/2;
		int y=(display.getBounds().height-shell.getBounds().height)/2;;

		shell.setLocation(new Point(x,y));

	}

	private void createContents(final Shell shell, final Display display) {
		shell.setLayout(new GridLayout());
		GridData gd = new GridData(SWT.FILL,SWT.FILL,true,true);
		Label heading = new Label(shell, SWT.CENTER);
		heading.setLayoutData(gd);
		heading.setText(group.getName());	
		Label blank = new Label(shell, SWT.CENTER);
		blank.setLayoutData(gd);
		blank.setText("                 ----------------                      ");


		Label changeName = new Label(shell, SWT.CENTER);
		changeName.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		changeName.setText("            Enter a Display Name:            ");
		Text textName=new Text(shell, SWT.CENTER|SWT.BORDER);
		GridData td = new GridData(SWT.FILL,SWT.FILL,true,true);
		textName.setLayoutData(td);
		textName.setText(group.getDisplayName());
		textName.setSelection(textName.getText().length());//sets the caret to the end

		Button deleteBox = new Button(shell, SWT.CHECK);
		deleteBox.setText("Hide this group from the graph");		
		deleteBox.setSelection(!group.getVisible());

		Composite compOrder= new Composite(shell, SWT.BORDER);
		GridData gdc=new GridData(GridData.CENTER, GridData.FILL, false, false);
		gdc.heightHint=60;
		compOrder.setLayoutData(gdc);
		GridLayout fl=new GridLayout(2,false);
		fl.marginHeight=30;
		compOrder.setLayout(fl);

		buttonInc = new Button(compOrder, SWT.NONE);
		gdc=new GridData(GridData.FILL, GridData.FILL, true, true);
		buttonInc.setLayoutData(gdc); //*

		buttonDec = new Button(compOrder, SWT.NONE);
		gdc=new GridData(GridData.FILL, GridData.FILL, false, false);
		buttonDec.setLayoutData(gdc); //*

		buttonInc.addSelectionListener(new ShiftButtons(true));
		buttonDec.addSelectionListener(new ShiftButtons(false));

		checkShiftButtonsEnabled();

		if(proj.isHorizontal()){
			buttonInc.setText("Shift Group Left");
			buttonDec.setText("Shift Group Right");
		}else{
			buttonInc.setText("Shift Group Up");
			buttonDec.setText("Shift Group Down");
		}

		Composite compButtons= new Composite(shell, SWT.BORDER);
		gdc=new GridData(GridData.FILL, GridData.FILL, false,false);
		gdc.heightHint=60;
		compButtons.setLayoutData(gdc);
		fl=new GridLayout(2,false);
		fl.marginHeight=30;
		compButtons.setLayout(fl);

		Button donebutton = new Button(compButtons, SWT.NONE);
		donebutton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false)); //*
		donebutton.setText("Done");

		donebutton.addSelectionListener(new DoneButton(shell,textName,deleteBox));


		Button canbutton = new Button(compButtons, SWT.NONE);
		canbutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false)); //*
		canbutton.setText("Cancel");		    

		canbutton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				setCancelled(true);
				shell.dispose();
			}
		});

		shell.setDefaultButton(donebutton);



	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public boolean isDown() {
		return down;
	}

	public boolean isUp() {
		return up;
	}



	public boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	private void checkShiftButtonsEnabled() {		
		buttonDec.setEnabled(!(group.getOrder()==noGroups));				
		buttonInc.setEnabled(!(group.getOrder()==0));				
	}

	private class ShiftButtons extends SelectionAdapter{
		private boolean up ;

		public ShiftButtons(boolean up){
			this.up = up;
		}

	    public void widgetSelected(SelectionEvent event) {   
		AdmixPopulationGroup gp;
		int delta = up ? -1 : +1;
		gp = proj.findPopGroup(group.getName());
		proj.shiftPop(gp,delta);
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

	private class DoneButton extends SelectionAdapter{	
		Shell shell;
		Text textBox;
		private Button deleteBox; 


		public DoneButton(Shell shell,Text textBox, Button deleteBox ) {	
			this.deleteBox=deleteBox;
			this.shell = shell;
			this.textBox=textBox;

		}

		public void widgetSelected(SelectionEvent e) {	
			deleted=deleteBox.getSelection();			
			setName(textBox.getText());
			shell.dispose();
		}						
	}	

}



