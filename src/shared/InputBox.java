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

package shared;

import main.UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import main.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This class creates a dialog asking the user to input text
 */
public class InputBox extends Dialog {
	public InputBox(Shell parent) {

		super(parent);

	}

	private String name;
	private FontData font = new FontData("Arial",10,SWT.NONE);
	private boolean underlined;

	/**
	 * Runs the dialog
	 */
	public void run() {

		Shell parent = getParent();
		final Shell shell =  new Shell(parent, SWT.BORDER | SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);  

		shell.setLayout(new GridLayout(2, true));

		Label label = new Label(shell, SWT.NULL);
		label.setText("Please enter the label text:");

		final Text textLabel = new Text(shell,SWT.BORDER);
		textLabel.setText("");
		textLabel.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		createFontButton(shell);
		
		final Button buttonDone=createDoneButton(shell);
		createCancelButton(shell);
	

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

		shell.pack();
		shell.open();

		Display display = parent.getDisplay();

		centreOnScreen(shell,display);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}


	}

	private Button createFontButton(final Shell shell) {
		Button button = new Button(shell, SWT.PUSH);
		button.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		button.setText("Change Label Font");
		
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FontDialog fd = new FontDialog(font, false);				
				fd.run(UI.display);
				FontData fontData = fd.getFont();			
				if (fontData == null){			
					return;
				}else{
					setUnderlined(fd.getUnderlined());
					font = fontData;
				}				
			}
		});
		
		return button;
	}
	
	private Button createCancelButton(final Shell shell) {
		Button buttonCan = new Button(shell, SWT.PUSH);
		buttonCan.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		buttonCan.setText("Cancel");
		buttonCan.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setName(null);
				shell.dispose();

			}
		});
		return buttonCan;

	}

	private Button createDoneButton(final Shell shell) {
		final Button buttonDone = new Button(shell, SWT.PUSH);
		buttonDone.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		buttonDone.setText("Ok");		
		buttonDone.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();

			}
		});
		return buttonDone;

	}

	private void centreOnScreen(Shell shell,Display display) {
		int x=(display.getBounds().width-shell.getBounds().width)/2;
		int y=(display.getBounds().height-shell.getBounds().height)/2;;

		shell.setLocation(new Point(x,y));

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
	
	public FontData getFont() {
		return font;
	}

	public void setFont(FontData font) {
		this.font = font;
	}

	public boolean getUnderlined() {
		return underlined;
	}

	public void setUnderlined(boolean underlined) {
		this.underlined = underlined;
	}


}
