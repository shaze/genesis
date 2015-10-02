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
package main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
/**
 * Display the splash screen logo and then close
 *
 */
public class Splash {

	boolean timerDone;
	Shell mainWindow;
	/**
	 * The constructor of the method that creates the shell and closes it
	 * 150 ms after opening.
	 * 
	 * @param display The current display
	 */
	public Splash(final Display display) {
		super();
		mainWindow=createShell(display);			
		Image image = getSplashImage(display);
		mainWindow.setBackgroundImage(image);				
		mainWindow.open();

		startTimer(display);	

		while (!mainWindow.isDisposed()) {
			if (!display.readAndDispatch())
			{	
				display.sleep();

			}
		}
	}

	/**
	 * This method starts the timer that will close the splash after 150 ms
	 * @param display The current Display
	 */
	private void startTimer(Display display){
		display.timerExec(150, new Runnable() {
			public void run() {
				mainWindow.dispose();
			}

		});
	}

	/**
	 * This method loads and scales the image that is used for the splash
	 * @param display The current display
	 * @return The image
	 */
	private Image getSplashImage(Display display){
		Rectangle bds = mainWindow.getBounds();
		Image image = SWTImageLoader.loadImage("back.png");		
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

		Rectangle bds = new Rectangle(mainWindow.getDisplay().getBounds().width/2-250, 
				mainWindow.getDisplay().getBounds().height/2-150, 500, 300);
		mainWindow.setBounds(bds);	
		return mainWindow;		
	}

}
