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

package main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;


public class About {	

	boolean timerDone;
	Shell mainWindow;

	public About(final Display display) {
		super();
		mainWindow=createShell(display);			
		Image image = getSplashImage(display);
		mainWindow.setBackgroundImage(image);	
		mainWindow.setBackgroundMode(SWT.INHERIT_FORCE);
		createAboutText();
		createCloseButton();
		mainWindow.open();

		while (!mainWindow.isDisposed()) {
			if (!display.readAndDispatch()){	
				display.sleep();
			}
		}

	}



	private void createCloseButton() {
		Button donebutton = new Button(mainWindow, SWT.NONE);
		donebutton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1,1)); //*
		donebutton.setText("OK");
		donebutton.addSelectionListener(new DoneButton());

	}


	private class DoneButton extends SelectionAdapter{			

		public void widgetSelected(SelectionEvent e) {
			mainWindow.dispose();
		}

	}	


	private void createAboutText() {
		mainWindow.setLayout(new GridLayout(1,false));	
		createEmptyLabel() ;
		
		newAboutLabel("Genesis ".concat(Main.version).concat(" -- program for creating structure and PCA plots of genotype data"),51);
		newAboutLabel(" Copyright (C) 2014. Robert W Buchmann, University of the Witwatersrand,",92);
		newAboutLabel(" Johannesburg. This program is free software: you can redistribute it and/or modify",166);
		newAboutLabel(" it under the terms of the GNU Affero General Public License as published by",208);
		newAboutLabel(" the Free Software Foundation.",255);
		
		

	}

	/**
	 * This label is created to push the about labels to the bottom of the shell
	 */
	private void createEmptyLabel() {
		Label label0 = new Label(mainWindow,SWT.SINGLE);
		label0.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,1,1));
		label0.setFont(new Font(UI.display,"Arial Narrow", 11, SWT.NONE ));
		label0.setForeground(new Color(UI.display,208,34,35));
		label0.setText(" ");


	}
	
	private void newAboutLabel(String string, int red) {
		Label label = new Label(mainWindow,SWT.SINGLE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1,1));
		label.setFont(new Font(UI.display,"Arial", 12, SWT.NONE ));
		label.setForeground(new Color(UI.display,red,red*34/208,red*34/208));
		label.setText(string);
		
	}



	/**
	 * This method loads and scales the image that is used for the splash
	 * @param display The current display
	 * @return The image
	 */
	private Image getSplashImage(Display display){
		Rectangle bds = mainWindow.getBounds();
		Image image = SWTImageLoader.loadImage("about.png");		
		return new Image(display,image.getImageData().scaledTo((int)(bds.width),(int)(bds.height)));
	}

	/**
	 * This creates the shell for the main window and centres it on the screen 
	 * @param display The current display
	 * @return The main window
	 */
	private Shell createShell(Display display) {
		mainWindow = new Shell(display,SWT.NO_TRIM);		
		mainWindow.setText("Welcome to Genesis");

		Rectangle bds = new Rectangle(mainWindow.getDisplay().getBounds().width/2-290, 
				mainWindow.getDisplay().getBounds().height/2-163, 580, 326);
		mainWindow.setBounds(bds);	
		return mainWindow;		
	}

}
