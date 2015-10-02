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


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
/**This class creates a dialog giving the user all options related to an individual 
 * subject on the Admixture Graph.
 * 
 * <p>Created by {@link UIAdmixture}
 * 
 *
 */
public class SubjectDialog {
	private boolean deleted, cancelled;
	private AdmixSubject subject;


	public SubjectDialog(ASubject subject) {
		this.subject=subject.getSubject();
	}

	public boolean isDeleted() {
		return deleted;
	}	
	
	public boolean isCancelled() {
		return cancelled;
	}	

	/**Begins the dialog
	 * @param display the current display
	 */
	public void run(Display display) {		    

		Shell shell = new Shell(display,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);
		shell.setText(subject.getName());	
		shell.setLocation(new Point(display.getBounds().width/2-100,
				display.getBounds().height/2-100));
		createContents(shell,display);

		shell.pack();
		shell.open();

		shell.forceActive();
		shell.forceFocus();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}


	}

	private void createContents(final Shell shell, final Display display) {
		shell.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(SWT.FILL,SWT.FILL,true,true);
		//heading
		Label heading = new Label(shell, SWT.CENTER);
		heading.setLayoutData(gd);
		heading.setText(subject.getName());	
		//position data
		Label coords = new Label(shell, SWT.CENTER);
		coords.setLayoutData(gd);

		Label labelBlank3 = new Label(shell, SWT.CENTER);
		labelBlank3.setLayoutData(gd);
		labelBlank3.setText("--------------------");

		if (subject.isReceivedPheno()){
			createPhenoPanel(shell);				
		}

		Button deleteBox = new Button(shell, SWT.CHECK);
		deleteBox.setText("Hide this Individual from the graph");		
		deleteBox.setSelection(!subject.getVisible());
	

		Label labelBlank9 = new Label(shell, SWT.CENTER);
		labelBlank9.setLayoutData(gd);
		labelBlank9.setText("                                                  ");	

		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false);
		Composite compButtons= new Composite(shell, SWT.BORDER);		    
		compButtons.setLayoutData(gd2);
		compButtons.setLayout(new GridLayout(2, false));

		Button donebutton = new Button(compButtons, SWT.NONE);
		donebutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); //*
		donebutton.setText("Done");



		Button canbutton = new Button(compButtons, SWT.NONE);
		canbutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); //*
		canbutton.setText("Cancel");		    

		canbutton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				cancelled=true;
				shell.dispose();
			}
		});


		//if OK button is pressed, closes window
		donebutton.addSelectionListener(new DoneButton(shell,deleteBox));


	}


	private void createPhenoPanel(Shell shell) {
		GridData gd = new GridData(SWT.FILL,SWT.FILL,true,true);
		Label labelPheno = new Label(shell, SWT.CENTER);
		labelPheno.setLayoutData(gd);
		labelPheno.setText("Phenotype Data:");

		Composite comp = new Composite(shell, SWT.BORDER);
		GridData compositeImportData = new GridData(SWT.FILL, SWT.FILL, true, false,subject.getPhenotypeData().length+1,1);
		comp.setLayoutData(compositeImportData);
		comp.setLayout(new GridLayout(1, false));

		for(String str:subject.getPhenotypeData()){
			Label n = new Label(comp, SWT.LEFT);
			n.setLayoutData(gd);
			n.setText("      "+str);
		}

		Label labelBlank6 = new Label(shell, SWT.CENTER);
		labelBlank6.setLayoutData(gd);
		labelBlank6.setText("             ");
		
	}


	private class DoneButton extends SelectionAdapter{			
		Shell shell;		
		Button deleteBox;

		public DoneButton(Shell shell, Button deleteBox ) {	
			this.deleteBox=deleteBox;
			this.shell = shell;

		}

		public void widgetSelected(SelectionEvent e) {	
			if(deleteBox.getSelection()){
				deleted=true;
			}
			cancelled=false;
			shell.dispose();
		}						
	}	


}



