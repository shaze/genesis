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

package admix;

import io.ReadFile;
import java.lang.NumberFormatException;


/** The class used to import and validate the admixture input file
 *
 */
public class AdmixInput {

	/**The public method which reads in the file and validates it
	 * 
	 * @param path the location of the file to be input
	 * 
	 * @return the array of AdmixSubjects to populate the {@link AdmixGraph}<p>
	 * Will return a single column with a negative number in the first elementof 
	 * the first {@link AdmixSubject}'s AdmixData field if there was some sort of error or the file was not validated
	 */
	public AdmixSubject[] readAdmixInput(String path){ 
		ReadFile rf = new ReadFile();				 
		String[] input = rf.readFile(path);			  
		if(input==null){							  									
			return errorAdmix(-5);	//file not found						
		}
		try{
			boolean clumpp=false;
			int noAncestors=0,pos;
			if((pos=input[0].indexOf(':'))==-1){
				noAncestors = input[0].split("\\s+").length;
			}else{
				String temp=input[0].substring(pos+1).trim();
				noAncestors = temp.split("\\s+").length;
				clumpp=true;

			}
 
			if(noAncestors<2){
				return errorAdmix(-1); //error (invalid file) code: -1
			}
			AdmixSubject[] result = new AdmixSubject[input.length];
			for(int i=0;i<input.length;i++){
				float[] ratio = splitAndConv(input[i].trim(),clumpp);
				if(ratio.length!=noAncestors){
					return errorAdmix(-2);
					//error (lines have different numbers of ancestors) code: -2
				}
				result[i]=new AdmixSubject(ratio);
			}
			return result;
		}catch(NumberFormatException e){
			return errorAdmix(-3);
			//error (invalid file) code: -3
		}catch(ArrayIndexOutOfBoundsException e){
			return errorAdmix(-1);
			//error (probably input a blank file) code: -1
		}
	}
	
	private AdmixSubject[] errorAdmix(int errorCode){ //creates the admix column with the error code
		AdmixSubject[] a = new AdmixSubject[1];
		float[] b = new float[1];
		b[0]=errorCode;
		a[0]=new AdmixSubject(b);
		return a;
	}
	
	
	/**This method converts the line into a float array
	 * @param in The line to be converted
	 * @param clumpp whether or not the input file is a clumpp file. Note that a clumpp file
	 * 				 has a colon, before which everything can be ignored
	 * @return the float array
	 * @throws NumberFormatException
	 */
	private float[] splitAndConv(String in, boolean clumpp)throws NumberFormatException{ 								
		if(clumpp){
			in=in.substring(in.indexOf(':')+1).trim();
		}
		String[] split=in.split("\\s+");		
		float[] result = new float[split.length];
		for(int i=0;i<split.length;i++){
			result[i]=Float.parseFloat(split[i].trim());
		}
		return result;
		
	}
	
}
