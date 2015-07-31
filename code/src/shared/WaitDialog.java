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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * This class contains the simple methods needed to create destroy the dialog
 * that says "Please wait..." 
 */
public class WaitDialog {
	private static Shell newShell;
	
	/**
	 * This method starts the wait Dialog
	 */
	public static  void start(){
		createShell();
		createLabel();
		centreOnScreen();
		newShell.open();
		
	}

	/**
	 * This method creates the new shell
	 */
	private static void createShell() {
		newShell = new Shell(UI.display,SWT.APPLICATION_MODAL);	
	}

	/**
	 * This method creates the label on the shell
	 */
	private static void createLabel() {
		Label lbl = new Label(newShell,SWT.WRAP);
		lbl.setText("Please Wait...");
		lbl.setBounds(new Rectangle(10,10,100,25));

	}
	
	/**
	 * This method centres the shell on the screen
	 */
	private static void centreOnScreen() {
		Rectangle bds = newShell.getDisplay().getBounds();
		newShell.setSize(110,35);
		Point p = newShell.getSize();
		newShell.setBounds((bds.width - p.x) / 2, (bds.height - p.y)/2, p.x, p.y);

	}
	
	/**
	 * This method ends the wait Dialog
	 */
	public static void end(){
		newShell.dispose();					
	}

}
