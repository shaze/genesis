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

import main.FontDialog;
import main.UI;
import shared.GLabel;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**This class is creates a small dialog giving the user the options related to a label on the 
 * graph.
 * 
 * @author R W Buchmann
 * 
 */
public class LabelDialog {
	/**
	 * The label for which the dialog is being created
	 */
	private GLabel lbl;
	/**
	 * Whether the label was changed by this dialog
	 */
	private boolean changed;	
	/**
	 * Whether the label was deleted by this dialog
	 */
	private boolean deleted;
	/**
	 * Whether the label was moved by this dialog
	 */
	private boolean moved;
	/**
	 * Whether the label's font was changed by this dialog
	 */
	private boolean fontChanged;
	/**
	 * Whether the label currently/to be underlined
	 */
	private boolean underlined;
	/**
	 * This variable stores the new label text (if the label
	 * text is changed)
	 */
	private String labelText;
	/**
	 * The label's font
	 */
	private FontData font;
	/**
	 * The dialog window
	 */
	private Shell shell;
	/**
	 * @return Whether the font was changed by this dialog.
	 */
	public boolean getFontChanged() {
		return fontChanged;
	}

	/**The constructor of the class
	 * @param lbl a {@link GLabel} object
	 * @param pos the coordinates of the mouse click
	 */
	public LabelDialog(GLabel lbl) {
		this.lbl=lbl;			
		this.underlined=lbl.getUnderlined();
	}

	/**
	 * @return whether or not the label's text is to be changed
	 */
	public boolean isChanged(){
		return changed;
	}
	/**
	 * 
	 * @return the new text 
	 */
	public String getText(){
		return labelText;
	}


	/**
	 * Begins the dialog
	 *  
	 * @param display current display
	 */
	public void run(Display display) {		    
		shell = new Shell(display,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);		

		shell.setText("Label options");
		createContents();

		shell.pack();
		shell.open();
		centreOnScreen();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}  
	}

	/**
	 * Centres the window on the screen
	 */
	private void centreOnScreen() {
		Display display = shell.getDisplay();
		int x=(display.getBounds().width-shell.getBounds().width)/2;
		int y=(display.getBounds().height-shell.getBounds().height)/2;;

		shell.setLocation(new Point(x,y));
	}


	/**
	 * Creates the contents of the window (buttons etc.)
	 */
	private void createContents() {
		shell.setLayout(new GridLayout(2, false));	

		Label Label1 = new Label(shell, SWT.CENTER);
		Label1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		Label1.setText("                   Enter new label text                ");

		Text textName=new Text(shell,SWT.LEFT|SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		textName.setText(lbl.getText());
		textName.setSelection(textName.getText().length());//sets the caret to the end

		Button fontButton = new Button(shell, SWT.NONE);
		fontButton.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		fontButton.setText("Change Label Font");
		fontButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				FontData fd = pickFont(lbl.getFont());
				if(fd!=null){
					font=fd;
					fontChanged=true;
				}		    		
			}
		});

		Button reposButton = new Button(shell, SWT.NONE);
		reposButton.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		reposButton.setText("Reposition");
		reposButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				moved=(true);
				shell.dispose();
				UI.setDefaultCursor();
			}

		});

		Button deleteButton = new Button(shell, SWT.NONE);
		deleteButton.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		deleteButton.setText("Delete");
		deleteButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				deleted=(true);
				shell.dispose();
				UI.setDefaultCursor();
			}

		});

		//create done button
		Button doneButton = new Button(shell, SWT.NONE);
		doneButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
		doneButton.setText("Done");
		doneButton.addSelectionListener(new DoneButton(textName,shell));
		shell.setDefaultButton(doneButton);

		Button canButton = new Button(shell, SWT.NONE);
		canButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
		canButton.setText("Cancel");
		canButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				changed=false;
				fontChanged=false;
				shell.dispose();
				UI.setDefaultCursor();
			}
		});

		shell.setDefaultButton(doneButton);

	}

	/**
	 * Creates a dialog for the user to select a font. 
	 * Also sets the underlined field.
	 * @param oldFont the old font
	 * @return the new font
	 */
	FontData pickFont(FontData oldFont){
		FontDialog fd = new FontDialog(oldFont, underlined);
		fd.run(UI.display);
		if(fd.getFont()!=null){
			underlined=fd.getUnderlined();
			return fd.getFont();
		}

		return null;			

	}


	/**
	 * Returns a boolean specifying if the label is to be deleted.
	 * 
	 * @return whether or not the label is to be deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}


	/**
	 * Acts as a button listener for the done button
	 */
	private class DoneButton extends SelectionAdapter{
		private Text textName;
		private Shell shell;

		public DoneButton(Text textName,Shell shell) {				
			this.textName=textName;				
			this.shell = shell;				
		}

		public void widgetSelected(SelectionEvent e) {
			if(!textName.getText().trim().equals(lbl.getText())
					&&!textName.getText().trim().equals("")){
				labelText=textName.getText().trim();
				changed = true;
			}					

			shell.dispose();
			UI.setDefaultCursor();
		}
	}

	/**
	 * @return whether the label was moved using the reposition button
	 */
	public boolean isMoved() {
		return moved;
	}

	/**
	 * @return the new label's font
	 */
	public FontData getFont() {
		return font;
	}

	/**
	 * @return whether or not the new label font is to be underlined
	 */
	public boolean getUnderlined() {
		return underlined;
	}


}



