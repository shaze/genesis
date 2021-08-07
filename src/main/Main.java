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

import org.eclipse.swt.widgets.Display;

/**Contains the public static void main(String[] args) method
 * Runs the splash, then runs the UI.
 */


public class Main {
	public static String version = "0.3.0 2021-08-08";
	
	public static void main(String[] args) {
		final Display display = new Display();
		
        new Splash(display);
        display.dispose();
                
        UI ui = new UI();
        ui.open();
       
	}
	
}
