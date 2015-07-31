package main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * Display the splash screen logo and then close
 *
 */
public class HelpMenu {

	boolean timerDone;
	Shell mainWindow;
	
	public HelpMenu(final Display display) {
		super();
		mainWindow=createShell(display);			
		Image image = getSplashImage(display);
		mainWindow.setBackgroundImage(image);	
		mainWindow.setBackgroundMode(SWT.INHERIT_FORCE);
		createHelpBrowser();
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

	private void createHelpBrowser() {
		Browser browser;
		mainWindow.setLayout(new GridLayout(1,false));	
		try {
			browser = new Browser(mainWindow, SWT.NONE);
		} catch (SWTError e) {
			UI.errorMessage("Help file cannot be initialised.");
			return;
		}
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,1));	
		//String workingDir = System.getProperty("user.dir");
		browser.setUrl("http://www.bioinf.wits.ac.za/software/genesis/html/");
		
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

		Rectangle bds = new Rectangle(mainWindow.getDisplay().getBounds().width/2-290, 
				mainWindow.getDisplay().getBounds().height/2-163, 580, 326);
		mainWindow.setBounds(bds);	
		return mainWindow;		
	}

}
