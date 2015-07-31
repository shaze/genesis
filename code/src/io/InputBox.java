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

package io;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This class creates a dialog asking the user to input text
 *
 */
public class InputBox extends Dialog {
	public InputBox(Shell parent) {
		super(parent);
	}

	private String name;

	/**
	 * Runs the dialog
	 */
	public void run() {

		Shell parent = getParent();
		final Shell shell = createShell(parent);  
		shell.pack();
		shell.open();

		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}


	}

	private Shell createShell(Shell parent) {
		final Shell shell = new Shell(parent, SWT.BORDER | SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);  
		shell.setLocation(500,500);
		shell.setLayout(new GridLayout(2, true));

		Label label = new Label(shell, SWT.NULL);
		label.setText("Please enter the label text:");

		final Text textLabel = new Text(shell,SWT.BORDER);
		textLabel.setText("");
		final Button buttonDone = new Button(shell, SWT.PUSH);
		buttonDone.setText("Ok");
		buttonDone.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		Button buttonCan = new Button(shell, SWT.PUSH);
		buttonCan.setText("Cancel");

		textLabel.addModifyListener( new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setName(textLabel.getText());
				try {	    			
					buttonDone.setEnabled(true);
				} catch (Exception error) {
					buttonDone.setEnabled(false);
				}

			}
		});

		buttonDone.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();

			}
		});

		buttonCan.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setName(null);
				shell.dispose();

			}
		});

		return shell;
	}

	/**
	 * @return the text entered
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
