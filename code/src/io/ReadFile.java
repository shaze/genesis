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
import java.util.*;

/**
 * Used to read in text files for input and to load graphs from.
 */

public class ReadFile {    
	/**
	 * this method reads in a file and converts it into an array of strings
	 * each newline or return carriage causes a new string to be written
	 * terminated by EOF
	 * 
	 * @param path the path of the file
	 * @return the array of string
	 */
	public String[] readFile(String path){ //for pca use readFileWithoutComments
		File f = new File(path);	   
		if(!f.exists()){			   
			return null;			   
		}else{
			String[] a;
			this.setPath(path);
			try {
				a = this.OpenFile();
				return a;
			} catch (IOException e){			
				return null;
			}	
		}

	}
	/**
	 * this method reads in a file and converts it into an array of strings
	 * each newline or return carriage causes a new string to be written
	 * terminated by EOF. It ignores lines that start with an "@" which are 
	 * comments in the eigenstrat data file. 
	 * 
	 * @param path the path of the file
	 * @return the array of string
	 */
	public String[] readFileWithoutComments(String path){
		if(path==null){
			return null;
		}
		File f = new File(path);	   
		if(!f.exists()){			   //
			return null;			   //
		}else{
			String[] a;
			this.setPath(path);
			try {
				a = this.OpenFileWithoutComments();
				return a;
			} catch (IOException e){
				return null;
			}	
		}

	}

	/**
	 * this method reads in a file and converts it into an array of strings
	 * each newline or return carriage causes a new string to be written
	 * terminated by EOF. It also reads and stores blank lines in the string
	 * (as ""). 
	 * 
	 * @param path the path of the file
	 * @return the array of string
	 */
	public String[] readFileWithEmpty(String path){
		if(path==null){
			return null;
		}
		File f = new File(path);	   
		if(!f.exists()){			   //
			return null;			   //
		}else{
			String[] a;
			this.setPath(path);
			try {
				a = this.OpenFileWithEmpty();
				return a;
			} catch (IOException e){
				return null;
			}	
		}

	}

	private String path;
	private void setPath(String file_path){
		path = file_path;        
	}

	/**
	 * This method does the actual opening and analysis of the lines for the
	 * ReadFileWithEmpty() method 
	 * @return the string array
	 * @throws IOException
	 */
	private String[] OpenFileWithEmpty() throws IOException{
		LinkedList<String> LL = new LinkedList<String>();
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);    
		String next;
		while((next=br.readLine())!=null){  //check for eof
			LL.add(next);	        	
		}
		br.close();
		return LLtoArray(LL);       

	}

	/**
	 * This method does the actual opening and analysis of the lines for the
	 * ReadFile() method 
	 * @return the string array
	 * @throws IOException
	 */
	private String[] OpenFile() throws IOException{
		LinkedList<String> LL = new LinkedList<String>();
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);    
		String next;
		while((next=br.readLine())!=null){  //check for eof
			if(!next.equals("")){			//ignore empty line
				LL.add(next);	
			}	
		}
		br.close();
		return LLtoArray(LL);       

	}

	/**
	 * This method does the actual opening and analysis of the lines for the
	 * ReadFileWithoutComments() method 
	 * @return the string array
	 * @throws IOException
	 */
	private String[] OpenFileWithoutComments() throws IOException{       
		LinkedList<String> LL = new LinkedList<String>();
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);    
		String next;
		boolean first = false;

		while((next=br.readLine())!=null){  //check for eof			
			if(!first){
				first=true;
			}

			try{
				if(!next.equals("")&&(!(next.trim().charAt(0)=='#'))){//ignore empty line and comments	        		
					LL.add(next);	
				}	
			}catch(StringIndexOutOfBoundsException e){

			}
		}
		br.close();
		return LLtoArray(LL);       

	}


	private String[] LLtoArray(LinkedList<String> LL){
		String[] result = new String[LL.size()];
		for(int i=0;i<LL.size();i++){
			result[i]=LL.get(i);
		}
		return result;
	}
}
