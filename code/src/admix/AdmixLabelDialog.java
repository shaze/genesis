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

import main.FontDialog;
import main.UI;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import shared.GLabel;

/**This class is creates a small dialog giving the user the options related to a label on the 
 * graph.
 * 
 */
public class AdmixLabelDialog {
	private GLabel lbl;
	private boolean changed,deleted;
	private String name;
	private boolean moved,fontChanged, underlined;
	private FontData font;


	/**The constructor of the class
	 * @param lbl a AdmixLabel object
	 * @param pos the coordinates of the mouse click
	 */
	public AdmixLabelDialog(GLabel lbl) {
		this.lbl=lbl;

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
		return name;
	}


	/**begins the dialog
	 *  
	 * @param display current display
	 */
	public void run(Display display) {		    		    
		Shell shell = new Shell(display,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);

		shell.setText("Label options");
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


		GridData g = new GridData(SWT.FILL,SWT.FILL,true,true);
		g.horizontalSpan=2;

		GridData g1 = new GridData(SWT.FILL,SWT.FILL,true,true);
		g1.horizontalSpan=1;

		Label Label1 = new Label(shell, SWT.CENTER);
		Label1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		Label1.setText("                   Enter new label text                ");

		Text textName=new Text(shell,SWT.LEFT|SWT.BORDER);
		textName.setLayoutData(g);
		textName.setText(lbl.getText());
		textName.setSelection(textName.getText().length());//sets the caret to the end

		
		Button fontButton = new Button(shell, SWT.NONE);
		fontButton.setLayoutData(g);
		fontButton.setText("Label Font");
		fontButton.addSelectionListener(new SelectionAdapter(){			

			public void widgetSelected(SelectionEvent e){
				FontData fd = pickFont(lbl.getFont());
				if(fd!=null){
					setFont(fd);
					setFontChanged(true);				
				}		    		
			}

		});


		Button reposButton = new Button(shell, SWT.NONE);
		reposButton.setLayoutData(g1);
		reposButton.setText("Reposition");
		reposButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				moved=(true);
				shell.dispose();	
				UI.setDefaultCursor();
			}

		});

		Button deleteButton = new Button(shell, SWT.NONE);
		deleteButton.setLayoutData(g1);
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
	
 

	FontData pickFont(FontData oldFont){
		FontDialog fd = new FontDialog(oldFont);
		fd.run(UI.display);
		if(fd.getFont()!=null){
			underlined=fd.getUnderlined();
			return fd.getFont();
		}
		
		return null;			

	}


	/**Returns a boolean specifying if the label is to be deleted.
	 * 
	 * @return whether or not the label is to be deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}


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
				name=textName.getText().trim();
				changed = true;
			}					
			UI.setDefaultCursor();
			shell.dispose();
		}
	}


	public boolean isMoved() {
		return moved;
	}

	public boolean isFontChanged() {
		return fontChanged;
	}

	public void setFontChanged(boolean fontChanged) {
		this.fontChanged = fontChanged;
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


}



