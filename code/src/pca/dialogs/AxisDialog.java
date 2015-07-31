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

package pca.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/** This class creates a dialog used to change the options of the axis labels.
 * 
 *  @author R W Buchmann
 */
public class AxisDialog {
	/**
	 * The previous name for the axis label.
	 */
	private String oldName;
	/**
	 * The new name for the axis label. This value is returned
	 * by the getName() method.
	 */
	private String newName;
	/**
	 * The axis in question. Will either be 'x', 'y' or 'z'.
	 */
	private char axis;	
	/**
	 * The location on the screen where the dialog will be drawn.
	 */
	private Point pos;	
	
	/**
	 * The constuctor of the class
	 * @param old the current/previous name on the axis
	 * @param axisIndex the index of the axis 0=x, 1=y, 2=z
	 * @param point the point on the screen to create the axis
	 */
	public AxisDialog(String currentName,int axisIndex, Point point) {
		this.oldName=currentName;
		pos=point;
		if(axisIndex==0){
			axis='x';				
		}else if(axisIndex==1){
			axis='y';
		}else{
			axis='z';	
		}
	}

	/**
	 * This method creates and runs the dialog
	 */
	public void run(Display display) {		    

		Shell shell = new Shell(display,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);
		shell.setLocation(pos);

		shell.setText("Change "+axis+"-axis label");
		createContents(shell,display);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}	

	}

	/**
	 * This method places the buttons and other components on the shell
	 */
	private void createContents(final Shell shell, final Display display) {
		shell.setLayout(new GridLayout(2, false));
		
		createTitleLabel(shell);
		Text textName = createTextBox(shell);
		createDoneButton(shell, textName);
		createCancelButton(shell);				
	}

	/**
	 * Creates the title text for the dialog
	 * @param shell the shell
	 */
	private void createTitleLabel(Shell shell) {
		Label Label1 = new Label(shell, SWT.CENTER);
		Label1.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		Label1.setText("          Enter new label for " +axis+"-axis        ");
		
	}

	/**
	 * Creates the textbox and returns it to be sent to the doneButton 
	 * listener
	 * @param shell the shell
	 * @return the textbox
	 */
	private Text createTextBox(Shell shell) {
		Text textName=new Text(shell,SWT.LEFT|SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		textName.setSelection(textName.getText().length());//sets the caret to the end
		return textName;
	}

	/**
	 * Creates the done button
	 * @param shell the shell 
	 * @param text the textBox
	 */
	private void createDoneButton(Shell shell, Text text) {
		Button donebutton = new Button(shell, SWT.NONE);
		donebutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,1,1)); //*
		donebutton.setText("Done");
		donebutton.addSelectionListener(new DoneButton(text,shell));
	}

	/**
	 * Creates the cancel button
	 * @param shell the shell
	 */
	private void createCancelButton(final Shell shell) {
		Button canbutton = new Button(shell, SWT.NONE);
		canbutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,1,1)); //*
		canbutton.setText("Cancel");
		canbutton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				newName=oldName;
				shell.dispose();
			}
		});
		
	}

	/**
	 * This creates the listener for the done button on dialog.
	 * The button stores the value of the string in the textbox
	 * in the newName variable 
	 */
	private class DoneButton extends SelectionAdapter{
		private Text textName;
		private Shell shell;

		public DoneButton(Text textName,Shell shell) {				
			this.textName=textName;				
			this.shell = shell;				
		}

		public void widgetSelected(SelectionEvent e) {
			newName = textName.getText().trim();
			if(newName.equals("")){
				newName=oldName;
			}					

			shell.dispose();
		}		
	}
	
	/**Gets the new name of the axis
	 * @return the new name of the axis
	 */
	public String getName(){
		return newName;
	}

}



