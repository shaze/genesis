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




import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import drawObjects.DrawObject;

/**This class is creates a small dialog giving the user the options related to a label on the 
 * graph.
 * 
 */
public class DrawObjectDialog {
	private DrawObject dO;
	private boolean deleted;

	

	public DrawObjectDialog(DrawObject dO) {
		this.dO=dO;
	}

	/**begins the dialog
	 *  
	 * @param display current display
	 */
	public void run(Display display) {		    

		Shell shell = new Shell(display,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);		

		shell.setText(DrawObject.objectNames[dO.getType()] + " Options");
		createContents(shell,display);


		shell.pack();
		shell.open();
		centreOnScreen(shell);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}  
	}

	private void centreOnScreen(Shell shell) {
		Display display = shell.getDisplay();
		int x=(display.getBounds().width-shell.getBounds().width)/2;
		int y=(display.getBounds().height-shell.getBounds().height)/2;;

		shell.setLocation(new Point(x,y));

	}


	private void createContents(final Shell shell, final Display display) {
		shell.setLayout(new GridLayout(2, false));	
/*
		Label Label1 = new Label(shell, SWT.CENTER);
		Label1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		Label1.setText("                   Enter new label text                ");		*/

		Button deleteButton = new Button(shell, SWT.NONE);
		deleteButton.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		deleteButton.setText("Delete");
		deleteButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				deleted=(true);
				shell.dispose();
			}

		});

		//create done button
		Button doneButton = new Button(shell, SWT.NONE);
		doneButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,2,1)); 
		doneButton.setText("Cancel");
		doneButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				deleted=(false);
				shell.dispose();
			}

		});
		shell.setDefaultButton(doneButton);
		
	

	}
	
	
	/**Returns a boolean specifying if the label is to be deleted.
	 * 
	 * @return whether or not the label is to be deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	
	

}



