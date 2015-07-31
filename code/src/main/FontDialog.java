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

package main;


import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
/**
 * 
 * 
 *
 */
public class FontDialog {
	private FontData oldFont, font;
	private boolean underlined;
	Shell shell;
	
	private void bringToFront() {
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {		            
				shell.forceActive();
			}
		});

	}

	public FontDialog(FontData oldFont) {
		this.oldFont=oldFont;
	}
	
	public FontDialog(FontData oldFont, boolean underlined) {
		this.oldFont=oldFont;
		this.setUnderlined(underlined);
	}
	
	public FontData getFont(){
		return font;
	}

	/**
	 * Begins the dialog
	 * @param display the current display
	 */
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

	private Combo selectFont,fontSize;
	private Button boldBox, italicBox, underlineBox;
	
	private void createContents(final Display display) {
		shell.setLayout(new GridLayout(6, true));		
		//font
		selectFont = new Combo(shell, SWT.BORDER|SWT.READ_ONLY);
		selectFont.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,true,4,1));
		selectFont.setItems(getAllFonts());
		int pos;
		if((pos=(getPosOf(oldFont.getName(), selectFont)))>-1){
			selectFont.select(pos);
		}else if((pos=(getPosOf("Arial", selectFont)))>-1){
			selectFont.select(pos);
		}else{
			selectFont.select(0);
		}

		//font size
		fontSize = new Combo(shell, SWT.BORDER);
		fontSize.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,true,2,1));
		fontSize.setItems(getFontSizes());
		fontSize.setText(String.valueOf(oldFont.getHeight()));
		//bold
		boldBox = new Button(shell, SWT.CHECK);
		boldBox.setLayoutData(new GridData(SWT.NONE,SWT.NONE,false,true,2,1));
		boldBox.setText("Bold");
		boldBox.setSelection(checkBold(oldFont.getStyle()));
		//italic
		italicBox = new Button(shell, SWT.CHECK);
		italicBox.setLayoutData(new GridData(SWT.NONE,SWT.NONE,false,true,2,1));
		italicBox.setText("Italic");
		italicBox.setSelection(checkItalic(oldFont.getStyle()));
		//underline
		underlineBox = new Button(shell, SWT.CHECK);
		underlineBox.setLayoutData(new GridData(SWT.NONE,SWT.NONE,false,true,2,1));
		underlineBox.setText("Underline");
		underlineBox.setSelection(underlined);

		Button donebutton = new Button(shell, SWT.NONE);
		donebutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true,3,1)); //*
		donebutton.setText("Done");
		donebutton.addSelectionListener(new DoneButton(shell));

		Button canbutton = new Button(shell, SWT.NONE);
		canbutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true,3,1)); //*
		canbutton.setText("Cancel");
		canbutton.addSelectionListener(new CanButton(shell));
	}

	private boolean checkBold(int style) {
		if(style==SWT.BOLD||style==(SWT.BOLD|SWT.ITALIC)||style==(SWT.BOLD|SWT.ITALIC|SWT.NORMAL)){
			return true;
		}else{
			return false;
		}
		
	}
	
	private boolean checkItalic(int style) {
		if(style==SWT.ITALIC||style==(SWT.BOLD|SWT.ITALIC)||style==(SWT.BOLD|SWT.ITALIC|SWT.NORMAL)){
			return true;
		}else{
			return false;
		}
		
	}

	private int getPosOf(String name, Combo combo) {
		for(int i=0; i<combo.getItemCount(); i++){
			if(combo.getItem(i).equals(name)){
				return i;
			}
		}
		return -1;
	}

	private String[] getFontSizes() {
		ArrayList<String> al = new ArrayList<String>();	
		int[] fontSizes=new int[]{4,6,8,10,12,14,15,17,18,20,24,26,28,32,36,42,58,56,65,72,90};
		for (int i:fontSizes) {
			al.add(String.valueOf(i));
		}
		return al.toArray(new String[al.size()]);
	}

	private String[] getAllFonts(){
		ArrayList<String> al = new ArrayList<String>();
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();	    
		for (java.awt.Font f : e.getAllFonts()) {
			al.add(f.getFontName());
		}
		return al.toArray(new String[al.size()]);
	}

	

	public boolean getUnderlined() {
		return underlined;
	}

	public void setUnderlined(boolean underlined) {
		this.underlined = underlined;
	}



	private class DoneButton extends SelectionAdapter{			
		Shell shell;

		
		public DoneButton(Shell shell) {
			super();
			this.shell = shell;
		
		}

		public void widgetSelected(SelectionEvent e) {
			try{
				font = oldFont;			
				font.setName(selectFont.getItem(selectFont.getSelectionIndex()));
				font.setHeight(Integer.parseInt(fontSize.getText()));
				font.setStyle(getStyleInt()); 
				setUnderlined(underlineBox.getSelection());
				shell.dispose();
			}catch(NumberFormatException err){
				invalidFontSizeMessageBox();
				bringToFront();
			}
						
		}

		private int getStyleInt() {
			if(boldBox.getSelection()&&italicBox.getSelection()){
				return SWT.BOLD|SWT.ITALIC;
			}else if(boldBox.getSelection()){
				return SWT.BOLD;
			}else	 if(italicBox.getSelection()){
				return SWT.ITALIC;
			}else{
				return 0;
			}
		}		
		
		private void invalidFontSizeMessageBox() {
			MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_ERROR);
			messageBox.setMessage("Please enter or select a valid font size.");
			messageBox.setText("Invalid Font Size");
			messageBox.open();	
		}
	}	
	
	private class CanButton extends SelectionAdapter{			
		Shell shell;

		
		public CanButton(Shell shell) {
			super();
			this.shell = shell;
		
		}

		public void widgetSelected(SelectionEvent e) {			
				font = oldFont;						
				shell.dispose();									
		}
		
	}


}



