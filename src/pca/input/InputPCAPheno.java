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


/**This class contains the methods used to import the PCA phenotype file.
 * It performs the validation on the input and links the phenotype data to current PCA data.
 * 
 * @author R W Buchmann
 */
public class InputPCAPheno {
	/**The only public method of the class. Imports the phenotype data into the PCASuject array.
	 * 
	 * @param path the path of the phenotype file
	 * @param pcaData the {@link PCASubject} array
	 * @return a code stating if there was an error. >1 for a valid file, -1 for an invalid
	 * file, 0 for a valid looking file but with no matching entries
	 * 
	 */
	public int importPheno(String path,PCASubject[] pcaData){
		ReadFile rf = new ReadFile();
		String[] input = rf.readFileWithoutComments(path);
		if (input == null){			
			return -1;
		}
		convertEvec2Phenofile(input);
		int check=fillPheno(pcaData, input);		
		return check;		
	}
	
	/**
	 * This method checks and converts data read from a .evec file.
	 *   Converts the string "name : [pcadata]" to "name [pcadata] 
	 *   (essentially just removing a colon).
	 * @param input array of strings of data read from the pheno file.
     *              The result w 
	 * 	
	 */
	private void convertEvec2Phenofile(String[] input) {
		if(input[0].contains(":")){
			for(int i=0; i<input.length; i++){
				String s = input[i];	
				String[] strings = s.split(":");
				input[i] = joinStrings(strings);
			}	
		}	
	}

	/**
	 * Joins a list of strings together into 1 strging
	 * @param strings the list of strings
	 * @return the 1 string
	 */
	private String joinStrings(String[] strings) {
		StringBuilder sb = new StringBuilder();
		for(String s : strings){
			sb.append(s+" ");
		}
		return sb.toString().trim();
	}

	/**
	 * This method links the data from the input strings to the pca data 
	 * @param pcaData the pca data
	 * @param input the input strings
	 * @return a code stating if there was an error. >1 for a valid file,
	 *                    0 for a file with no entries in the pheno file matching the data
	 */
	private int fillPheno(PCASubject[] pcaData,String[] input){
		String[][] phenos = new String[pcaData.length][]; 
		int count=0;
		for(String s:input){
			String[] data = s.split("\\s+|:");
			int pos = search(pcaData,data[0]+" "+data[1]);
			if(pos>-1){
				phenos[pos]=(data);
				count++;
			}else{				
				
			}
		}
		if(count>0){
			clearPheno(pcaData);
			for(int i =0;i<phenos.length;i++){
				pcaData[i].setPhenotypeData(phenos[i]);
			}
		}
		return count;
		
	}

	
	/**
	 * Searches for which index, if any, of the pca data contains the
	 * subject with the given name 
	 * @param pcaData the pca data
	 * @param name the given name
	 * @return the index of the pca data, which contains the subject with
	 *     the given name. -1 if no subject has the name.
	 */
	private int search(PCASubject[] pcaData,String name){
		for(int i=0;i<pcaData.length;i++){			
			if(name.equals(pcaData[i].getName())){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Clears the pca data's phenotype information
	 * @param pcaData the pca data
	 */
	private void clearPheno(PCASubject[] pcaData){
		for (PCASubject p: pcaData){
			p.setPhenotypeData(null);
		}
	}
	
	
}
