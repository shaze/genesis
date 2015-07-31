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

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**This class creates the dialog boxes needed to get user input about 
 * exporting the graph to an image or pdf.
 * <p>
 * Used by both {@link UIPCA} and {@link UIAdmixture}

 */
public class ExportDialog {

	private int selection=-1;
	private int width,height;
	static int oldWidth;
	static int oldHeight;

	public ExportDialog(){

	}

	public void run(Display display) {		    

		Shell shell = new Shell(display,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);

		Rectangle bds = shell.getDisplay().getBounds();						 		     			

		shell.setText("Export PCA");
		createContents(shell,display);

		Point p = shell.getSize();

		shell.setBounds((bds.width - p.x) / 2, (bds.height - p.y)/2, p.x, p.y);


		shell.pack();
		shell.open();
		
		centreOnScreen(shell,UI.display);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}  
	}



	private void createContents(final Shell shell, final Display display) {
		shell.setLayout(new GridLayout(1, false));
		Label Label0= new Label(shell, SWT.CENTER);
		Label0.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		Label0.setText("                ");	  		

		Button pdfButton = new Button(shell, SWT.NONE);
		pdfButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		pdfButton.setText("Export to PDF");
		pdfButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				setSelection(0);		    		
				shell.dispose();
			}

		});		 
		
	
		
		Button imButton = new Button(shell, SWT.NONE);
		imButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
		imButton.setText("Export to PNG Image");
		imButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				setSelection(1);
				shell.dispose();
			}

		});
		Button svgButton = new Button(shell, SWT.NONE);
		svgButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
		svgButton.setText("Export to SVG Image");
		svgButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				setSelection(2);
				shell.dispose();
			}

		});
		Label Label7= new Label(shell, SWT.CENTER);
		Label7.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		Label7.setText("                ");

		Button canButton = new Button(shell, SWT.NONE);
		canButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
		canButton.setText("Cancel");
		canButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.dispose();
			}
		});

	}


	/**
	 * @return whether to export to pdf(0), png(1) or bmp(2)
	 */
	public int getSelection() {
		return selection;
	}


	public void setSelection(int selection) {
		this.selection = selection;
	}


	/**
	 * @return the height of the image to be saved
	 */
	public int getHeight() {
		return height;
	}



	public void setHeight(int height) {
		this.height = height;
	}


	/**
	 * @return the width of the image to be saved
	 */
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	
	
	private void centreOnScreen(Shell shell,Display display) {
		int x=(display.getBounds().width-shell.getBounds().width)/2;
		int y=(display.getBounds().height-shell.getBounds().height)/2;;

		shell.setLocation(new Point(x,y));

	}

}