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


import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.graphics.Color;

/**
 * This Class is used to load the images packaged in the build
 */

public class SWTImageLoader {
	private static Map<String, Image> imageMap = new HashMap<String, Image>();
	
	/**Loads an image. Image must be stored in the same directory as Main.class.
	 * 
	 * @param path The filename of the image
	 * @return The SWT image
	 */
	public static Image loadImage(String path) {
		Class<Main> cls=Main.class;
		Display display=Display.getCurrent();
        String x=cls.getName() + '|' + path;
        Image image;
        
        try{
        	ImageData data = new ImageData(cls.getResourceAsStream(path));
        	image = new Image(display, data);                    
            imageMap.put(x,image);
        }catch (Exception e){        	
        	image=greySquare();
            imageMap.put(x,image);
        }
        
        return image;
	}
	
	/**
	 * This method returns a grey square to be used as the image if the 
	 * image cannot be found
	 * 
	 * @return The grey square image.
	 */
	private static Image greySquare(){
		Image image = new Image(Display.getCurrent(),15,15);
        GC gc=new GC(image);               
        gc.setBackground(new Color(Display.getCurrent(),150,150,150));
        gc.fillRectangle(0,0,15,15);
        gc.dispose();
        return image;
	}
	
	
}
