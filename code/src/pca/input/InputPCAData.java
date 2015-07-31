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

package pca.input;

import pca.PCASubject;
import io.ReadFile;
import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 *  This class contains the methods to validate and input a PCA eigenstrat/relate
 *  file into an array of {@link PCASubject}
 *  
 *  @author R W Buchmann
 */
public class InputPCAData {
	/**
	 * Stores whether or not the input file was an eigenstrat file
	 */
	public boolean eigenFile; 
	/**
	 * takes the path of the PCA input file and returns an array of PCASubjects. 
	 * 
	 * @param path the file path
	 * @return the array of {@link PCASubject}s. If an error is encountered or 
	 * 	incorrect input is entered, will return one PCASubject with a large negative 
	 * 	number as the error code in the first column of the PCA data
	 * 
	 */



	public PCASubject[] readPCAInput(String path){ 
		try{
			ReadFile rf = new ReadFile();  
			String[] input = rf.readFileWithoutComments(path);
			if(input==null){//blank file
				return null;	 
			}
			input = getPlink(path,input);
			if(isRelInput(input)){
				eigenFile=false;
				return(InputRelData.relInputToSubjectArray(input));				
			};
			
			eigenFile=true;
			return(InputEigenData.eigenInputToSubjectArray(input));		
		}catch(NumberFormatException e){
			return errorPCA(-300);
			//error (data error) code: -300
		}catch(ArrayIndexOutOfBoundsException e){
			return errorPCA(-400);
			//error (probably input a blank file) code: -400
		}
	}


		private String [] getPlink(String path,String input []) {
		    ReadFile rf = new ReadFile();				 		    String famfname, pheno, fam[];
		    Pattern epat,fpat;
		    Matcher m;

		    if (!(path.endsWith(".eigenvec")))
			return input;
		    famfname = path.replaceFirst(".eigenvec",".fam");
		    File f = new File(famfname);
		    if (!(f.exists())) return input;
		    if (input[1].indexOf(":") != -1) return input;

		    fam = rf.readFileWithoutComments(famfname);
		    epat = Pattern.compile("^\\s*(\\S+)\\s+(\\S+)(.*)");
		    fpat = Pattern.compile("^.*\\s(\\S+)");
		    for(int i=0; i<input.length; i++) {
			m = fpat.matcher(fam[i]);
			if (!(m.find())) {
			    System.out.println("Un grand probleme");
			    System.out.println(fam[i]);
                            InputPCAData.errorPCA(-102); //error (plink eigenvec format problem) code: -102
			}
			pheno = m.group(1);
			m = epat.matcher(input[i]);			
			if (!(m.find())) {
			    System.out.println("Un grand probleme");
			    System.out.println(input[i]);
                            InputPCAData.errorPCA(-103); //error (plink eigenvec format problem) code: -102
			}
			input[i] = " "+m.group(1)+":"+m.group(2)+" "+m.group(3)+"\t"+pheno;
		    }
		    return input;
		}
		    

	/**    
	 * Checks whether the input is from a rel file (PCARelate).
	 * It does this by checking if the first line is "x" which
	 * is standard with rel files
	 * @param input array of strings of the lines of the input file
	 * @return true if the input is a rel file, else false
	 */
	private boolean isRelInput(String[] input) {
		return (input[0].equals("x"));		
	}

	/**
	 * Creates a single element PCASubject array that can be returned if 
	 * there is an error
	 * @param errorCode the error code to be returned
	 * @return
	 */
	static PCASubject[] errorPCA(int errorCode){
		PCASubject[] result = new PCASubject[1];
		float[] cols = new float[1];
		cols[0]=errorCode;
		result[0]=new PCASubject(cols,"error");
		return result;
	}
}
