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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

public class MarginDialog {
	private int[] oldMargins;
	private Shell shell;	
	private int[] margins;
	

	public MarginDialog(int[] oldMargins) {
		this.oldMargins=oldMargins;
	}


	public void run(Display display) {
		shell = new Shell(display,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);
		shell.setText("Select Font");	
		shell.setLocation(new Point(display.getBounds().width/2-100,
				display.getBounds().height/2-100));
		createContents(display);
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


	private void createContents(Display display) {
		shell.setLayout(new GridLayout(2, false));	
		
		Label labelLeft = new Label(shell,SWT.NONE);
		labelLeft.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,1,1));
		labelLeft.setText("Left Margin");

		Spinner leftMargin = new Spinner(shell,SWT.BORDER);
		leftMargin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1,1));			
		leftMargin.setMinimum(0);
		leftMargin.setMaximum(600);
		leftMargin.setSelection(oldMargins[0]);
		
		Label labelTop = new Label(shell,SWT.NONE);
		labelTop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,1,1));
		labelTop.setText("Top Margin");

		Spinner topMargin = new Spinner(shell,SWT.BORDER);
		topMargin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1,1));			
		topMargin.setMinimum(0);
		topMargin.setMaximum(600);
		topMargin.setSelection(oldMargins[1]);
		
		Label labelRight = new Label(shell,SWT.NONE);
		labelRight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,1,1));
		labelRight.setText("Right Margin");

		Spinner rightMargin = new Spinner(shell,SWT.BORDER);
		rightMargin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1,1));	
		rightMargin.setMinimum(0);
		rightMargin.setMaximum(600);
		rightMargin.setSelection(oldMargins[2]);
		
		Label labelBottom = new Label(shell,SWT.NONE);
		labelBottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,1,1));
		labelBottom.setText("Bottom Margin");

		Spinner bottomMargin = new Spinner(shell,SWT.BORDER);
		bottomMargin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1,1));	
		bottomMargin.setMinimum(0);
		bottomMargin.setMaximum(600);
		bottomMargin.setSelection(oldMargins[3]);
		
		Button donebutton = new Button(shell, SWT.NONE);
		donebutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true,3,1)); //*
		donebutton.setText("Done");
		donebutton.addSelectionListener(new DoneButton(shell , 
				new Spinner[]{leftMargin,topMargin,rightMargin,bottomMargin}));

		Button canbutton = new Button(shell, SWT.NONE);
		canbutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true,3,1)); //*
		canbutton.setText("Cancel");
		canbutton.addSelectionListener(new CanButton(shell));
		
	}
	
	public int[] getMargins() {
		return margins;
	}


	private class DoneButton extends SelectionAdapter{			
		Shell shell;
		Spinner[] spinners;
		
		public DoneButton(Shell shell, Spinner[] spinners) {
			super();
			this.shell = shell;
			this.spinners=spinners;
		
		}

		public void widgetSelected(SelectionEvent e) {
			
			margins = new int[]{spinners[0].getSelection(),
					spinners[1].getSelection(),spinners[2].getSelection(),spinners[3].getSelection()};
			shell.dispose();
						
		}

			
				
	}	
	
	private class CanButton extends SelectionAdapter{			
		Shell shell;

		
		public CanButton(Shell shell) {
			super();
			this.shell = shell;
		
		}

		public void widgetSelected(SelectionEvent e) {			
				margins=oldMargins;				
				shell.dispose();									
		}
		
	}

}
