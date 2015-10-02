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

/**
 * This class contains the methods needed to convert PCA relate input into
 * the PCASubject array
 * 
 * @author R W Buchmann
 */
public class InputRelData {
	/**
	 * Converts  PCA relate input into a PCASubject array
	 * @param input the lines of the input file
	 * @return the PCASubject array
	 * @throws NumberFormatException indicates a buggy file
	 */
	static PCASubject[] relInputToSubjectArray(String[] input) throws NumberFormatException{
		int lineNo=getFirstImportantLine(input);

		int noComponents = input[lineNo].trim().split("\\s+|\\t+").length-3;
		if(noComponents<1){
			return InputPCAData.errorPCA(-150); //error (no components) code: -150
		}else{
			return createDataFromRelInput(input, lineNo, noComponents);
		}

	}

	/**
	 * The PCA relate file's first few line represent the eigenvalues
	 * which (for now) are ignored. This method calculates the first line 
	 * which is not an eigenvalue. This is indicated by the line starting
	 * with the letter 's' from "sample ... "
	 * @param input the input
	 * @return the first non-eigenvalue line
	 */
	private static int getFirstImportantLine(String[] input) {
		int lineNo=0;

		while(true){
			if(input[lineNo].charAt(0)=='s'){
				lineNo++;
				break;
			}
			lineNo++;
		}			
		return lineNo;
	}

	/**
	 * Creates the PCASubject array from the input array
	 * @param input the input array
	 * @param lineNo the line representing the first subject
	 * @param noComponents the number of columns or PCAs
	 * @return the PCASubject array
	 */
	private static PCASubject[] createDataFromRelInput(String[] input, int lineNo, int noComponents) {
		PCASubject[] result = new PCASubject[input.length-(lineNo)];

		for(int i=lineNo;i<input.length;i++){
			String[] split =  input[i].trim().split("\\s+|:");
			String name = extractName(split);			
			float[] ratio = conv(split);
			if(ratio.length!=noComponents){
				return InputPCAData.errorPCA(-250);
				//error (lines have different numbers of components) code: -250
			}					
			PCASubject subj=new PCASubject(ratio,name);
			//set the pheno value since the rel file has it included					
			subj.setPhenotypeData(new String[] {name,"",split[2]});
			result[i-lineNo]=subj;
		}
		return result;
	}

	/**
	 * Extract the name from a line of the rel input.
	 * rel line format : "firstname-optionalsecondname phenoGroup col1 col2 ... "
	 * @param input the (split) input line 
	 * @return  the name (first and second name seperated by a space if there are 2 names)
	 */
	private static String extractName(String[] input) {		
		String[] nameArr = input[1].split("-");
		String name;
		if(nameArr.length==1){//uniquely identified individual
			name=nameArr[0]+" "+nameArr[0];
		}else{
			name=nameArr[0]+" "+nameArr[1];
		}
		return name;
	}

	/**
	 * Converts 1 line of PCArelate data to an array of floats that represent
	 * the values for each PCA axis
	 * @param line the (split) input line
	 * @return the array of floats
	 * @throws NumberFormatException
	 */
	private static float[] conv(String[] line)throws NumberFormatException{	
		float[] result = new float[line.length-3];
		for(int i=3;i<line.length;i++){
			result[i-3]=Float.parseFloat(line[i].trim());
		}
		return result;
	}
}
