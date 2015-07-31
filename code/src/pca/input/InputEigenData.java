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
 * This class contains the methods needed to convert eigenstrat input into
 * the PCASubject array
 * 
 * @author R W Buchmann
 */
public class InputEigenData {

	/**
	 * Converts  PCA relate input into a PCASubject array
	 * @param input the lines of the input file
	 * @return the PCASubject array
	 * @throws NumberFormatException indicates a buggy file
	 */
	public static PCASubject[] eigenInputToSubjectArray(String[] input) throws NumberFormatException{
		int noComponents = input[0].trim().split("\\s+|\\t+").length-2;				
		if(noComponents<1){
			return InputPCAData.errorPCA(-100); //error (no components) code: -100
		}else{
			return createDataFromEigenInput(input, noComponents);
		}
		
	}
	/**
	 * Creates the PCASubject array from the input array
	 * @param input the input array
	 * @param noComponents the number of columns or PCAs
	 * @return the PCASubject array
	 */
	private static PCASubject[] createDataFromEigenInput(String[] input, int noComponents) throws NumberFormatException{
		PCASubject[] result = new PCASubject[input.length];
		for(int i=0;i<input.length;i++){
			String[] split = input[i].trim().split("\\s+");
			String name = extractName(split);				
			float[] ratio = conv(split);

			if(ratio.length!=noComponents){
				return InputPCAData.errorPCA(-200);
				//error (lines have different numbers of components) code: -200
			}
			result[i]=new PCASubject(ratio,name);
		}

		return result;
		
	}
	
	/**
	 * Extract the name from a line of the eigen input.
	 * eigen line format : "firstname:optionalsecondname col1 col2 ... "
	 * @param input the (split) input line 
	 * @return  the name (first and second name seperated by a space if there are 2 names)
	 */
	private static String extractName(String[] input) {
		String[] nameArr = input[0].split(":");
		String name;
		if(nameArr.length==1){//uniquely identified individual
			name=nameArr[0]+" "+nameArr[0];
		}else{
			name=nameArr[0]+" "+nameArr[1];
		}
		return name;
	}
	/**
	 * Converts 1 line of eigenstrat data to an array of floats that represent
	 * the values for each PCA axis
	 * @param line the (split) input line
	 * @return the array of floats
	 * @throws NumberFormatException
	 */
	private static float[] conv(String[] split)throws NumberFormatException{	
		float[] result = new float[split.length-2];
		for(int i=1;i<split.length-1;i++){
			result[i-1]=Float.parseFloat(split[i].trim());
		}
		return result;

	}

}
