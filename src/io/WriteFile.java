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


package io;
import java.io.*;
/**
 * Used to read in files for input and to save graphs
 * @see ReadFile
 *
 */
public class WriteFile {
	/**WriteFile takes in a file path and String array and creates a text file at the file path
	 * 	containing the lines in the String array.
	 * @param text the array of strings to save to the file
	 * @param fileName the path of the file to be saved
	 */
	public void writeFile(String[] text, String fileName){ 
        File file = new File(fileName);					   
        if (!file.exists()){							   
        	try{ 										   
        		file.createNewFile();                      
        	}catch(IOException e){}
        }        	
        try{
    		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
    		for(int i=0;i<text.length;i++){
    			bw.write(text[i]);
    			if(i!=text.length-1){
    				bw.write("\n");
    			}
    		}
            bw.close();
    	}catch(IOException e){}
        

    }
}
