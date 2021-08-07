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
 */

package pca.dialogs;

import genesisDrawable.SWTCanvas;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import pca.drawTools.DrawIcon;
import pca.icon.Icon;

/**
 * This class creates a dialog giving the user all options related to an individual 
 * subject on a PCA Graph.
 * 
 * @author R W Buchmann
 */
public class IconDialog {
	
	/**
	 * Whether or not the dialog was cancelled.
	 */
	private boolean cancelled;
	/**
	 * The Color object for the colour of the icon
	 */
	private Color col;
	/**
	 * Whether the subject is to have all data (colours etc) cleared
	 */
	private boolean clear=false;
	/**
	 * Whether the subject's colour was changed by this dialog
	 */
	private boolean colChanged=false;
	/**
	 * Whether the subject's icon was changed by this dialog
	 */
	private boolean iconChanged=false;
	/**
	 * Whether the subject is/is to be hidden
	 */
	private boolean	deleted=false;
	/**
	 * Whether the subject is/is to be set to be on top
	 */
	private boolean onTop=false;	
	/**
	 * The default icon for the subject.
	 * This is set to be the subject's icon before the dialog is invoked.
	 * Will initially be the icon of the subject's population group.
	 */
	private Icon defaultIcon;
	/**
	 * The new icon chose by this dialog
	 */
	private Icon icon;	
	/**
	 * Whether or not the icon is to be drawn with a border
	 */
	private boolean border=true;
	/**
	 * The dialog window
	 */
	private Shell shell;
	/**
	 * The current display
	 */
	private Display display;

	/**
	 * Whether the border check box will be shown (it isn't shown on the subject
	 * dialog's version of the icon dialog.
	 */
	private boolean showBorderBox;
	/**
	 * The constuctor of the class
	 * 
	 * @param icon the old icon
	 * @param groupIcon the default icon for the group
	 */
	public IconDialog(Icon icon, Icon groupIcon) {
		this(icon, groupIcon, false);
	}
	/**
	 * The constuctor of the class
	 * 
	 * @param icon the old icon
	 * @param groupIcon the default icon for the group
	 * @param showBorderBox whether the dialog should have a "show border"
	 * 						checkbox (not necessary for the subject dialog
	 * 						version of this dialog)
	 */
	public IconDialog(Icon icon, Icon groupIcon, boolean showBorderBox) {
		this.defaultIcon=groupIcon;
		this.icon=new Icon();
		this.icon.setIcon((icon.getIcon()==-1) ? groupIcon.getIcon() : icon.getIcon());
		this.icon.setColour((icon.getColour()==null) ? groupIcon.getColour() : icon.getColour());
		this.icon.setSize((icon.getSize()==-1) ? groupIcon.getSize() : icon.getSize());
		this.border=icon.getBorder();
		this.showBorderBox=showBorderBox;
	}
	/**
	 * @return whether or not the subject is to be placed on top
	 */
	public boolean isOnTop() {
		return onTop;
	}

	/**
	 * @return whether or not the subject is to be deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}
	/**
	 * @return whether or not the subject is to have its icon information cleared
	 */
	public boolean getClear(){
		return clear;
	}
	/**
	 * @return the new icon
	 */    
	public Icon getIcon(){		
		return icon;
	}
	/**
	 * @return whether or not the colour is to be changed
	 */
	public boolean colourWasChanged(){
		return colChanged;
	} 
	/**
	 * @return whether or not the icon is to be changed
	 */
	public boolean iconWasChanged(){
		return iconChanged;
	}

	Combo Shape, Size;

	/**Creates the Dialog
	 * @param display The current display
	 */
	public void run(Display display) {		
		this.display=display;
		shell = new Shell(display,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);
		shell.setText("Icon Options");		

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
	 * Creates the contents (buttons etc.) for this dialog 
	 */
	private void createContents() {		
		shell.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(SWT.FILL,SWT.FILL,true,true);
		//heading
		Label heading = new Label(shell, SWT.CENTER);
		heading.setLayoutData(gd);
		heading.setText("Select Icon");	
	
		GridData longGD = new GridData(SWT.FILL, SWT.FILL, true, false,2,1);	

		//create combo box for shape changes
		Shape = new Combo(shell, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);			
		Shape.setLayoutData(longGD);
		Shape.setItems(new String[]{"Circle","Square","Triangle","Diamond",
				"Cross","Plus","Default"});
		Shape.select(icon.getIcon());
		
		Size = new Combo(shell, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);			
		Size.setLayoutData(longGD);
		Size.setItems(new String[]{"2","3","4","5","7","8","10","12","15","18","20","24","28","32","36"});
		Size.select(icon.getSize()-2);		

		// Start with old clour.
		col = new Color(shell.getDisplay(),icon.getColour());

		//creates grid for buttons
		Composite buttonlayout = new Composite(shell, SWT.BORDER);
		GridData compositeImportData = new GridData(SWT.FILL, SWT.FILL, true, false);
		buttonlayout.setLayoutData(compositeImportData);
		GridLayout gl=new GridLayout(2, false);
		gl.marginTop=35;
		gl.marginBottom=0;		
		buttonlayout.setLayout(gl);

		// Use a label full of spaces to show the colour

		final Label color = new Label(buttonlayout, SWT.PUSH);
		color.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));		    
		color.setText("         ");
		color.setBackground(new Color(display,255,255,255));		    
		color.addPaintListener(new paintListener(color));

		//create change colour buttons
		Button buttonCol = new Button(buttonlayout, SWT.NONE);
		buttonCol.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
		buttonCol.setText("Select Colour");
		
		if(showBorderBox){
			final Button borderBox = new Button(shell, SWT.CHECK);
			borderBox.setText("Draw Border around Icons");		
			borderBox.setSelection(border);
			borderBox.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e){
					icon.setBorder(borderBox.getSelection());
					border=borderBox.getSelection();
					color.redraw();
				}
			});
		}
		
		//create done button		    
		Composite compButtons= new Composite(shell, SWT.BORDER);		    
		compButtons.setLayoutData(compositeImportData);
		gl=new GridLayout(2, false);
		gl.marginTop=35;
		gl.marginBottom=0;
		compButtons.setLayout(gl);

		Button donebutton = new Button(compButtons, SWT.NONE);
		donebutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
		donebutton.setText("Done");

		Button canbutton = new Button(compButtons, SWT.NONE);
		canbutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
		canbutton.setText("Cancel");
		canbutton.addSelectionListener(new SelectionAdapter() {		
			public void widgetSelected(SelectionEvent event) {
				cancelled=true;
				col.dispose();
				shell.dispose();
			}
		});

		buttonCol.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				ColorDialog dlgbox = new ColorDialog(shell);
				dlgbox.setRGB(color.getBackground().getRGB());
				dlgbox.setText("Choose a Colour");
				RGB rgb = dlgbox.open();

				if (rgb != null) {
					col.dispose();
					colChanged=true;
					col = new Color(display,rgb);		         
					color.redraw();
				}
			}
		});

		Shape.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				if(Shape.getSelectionIndex()==6){//default				
					icon=defaultIcon;
					colChanged=false;							
				}
				color.redraw();

			}

		});		 
		
		Size.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				color.redraw();
			}

		});		
		

		donebutton.addSelectionListener(new DoneButton(Shape,Size,shell));
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

	public boolean getCancelled() {
		return cancelled;
	}
	
	/**
	 * Acts as the listener for the done button.
	 * Sets all the variables and closes the shell.
	 */
	private class DoneButton extends SelectionAdapter{
		Combo shape, size;
		Shell shell;

		public DoneButton(Combo shape, Combo size,Shell shell) {
			this.size=size;
			this.shape = shape;
			this.shell = shell;
		}

		public void widgetSelected(SelectionEvent e) {	

			deleted=false;
			if(shape.getSelectionIndex()>-1){
				icon.setIcon(shape.getSelectionIndex());
			}		
			icon.setSize(size.getSelectionIndex()+2);
			icon.setColour(col.getRGB());
			col.dispose();
			shell.dispose();
		}						
	}	


	/**
	 *	The paint listener for the icon preview
	 */
	private class paintListener implements PaintListener{		
		Label label;
		public paintListener(Label label) {
			this.label = label;
		}

		@Override
		public void paintControl(PaintEvent e) {
			int x=(label.getSize().x)/2;
			int y=(label.getSize().y)/2;
			int selectedIcon = Shape.getSelectionIndex();
			selectedIcon = selectedIcon==6 
					? defaultIcon.getIcon() : selectedIcon;
			int size=Size.getSelectionIndex()+2;

			SWTCanvas canvas = new SWTCanvas(e.gc);

			DrawIcon.drawIcon(canvas, new Point(x, y), selectedIcon, 
					border, col.getRGB(), size, false);
		}

	}

}



