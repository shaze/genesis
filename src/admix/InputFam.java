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

/**
 * This class handles the importing of the fam file into the {@link AdmixGraph}
 *
 */
public class InputFam {	/**
	 * @param path the location of the phenotype file
	 * @param admixData the array of AdmixSubjects to be populated with family information
	 * @return a negative number if there was an error or the file was the incorrect format<p>
	 * 1 if the file was validated
	 */
	public int importFam(String path,AdmixSubject[] admixData){
		ReadFile rf = new ReadFile();
		String[] input = rf.readFile(path);
		if (input == null){
			return -1;
		}
		
		if (!(input.length==admixData.length)){
			return -2;
		}	
		
		return fillFam(input, admixData);
		
	}
	
	private int fillFam(String[] input, AdmixSubject[] admixData){
		for(int i=0;i<admixData.length;i++){
			try{
				String[] line = input[i].split("\\s+");
				admixData[i].setName(line[0]+" "+line[1]);
			}catch(ArrayIndexOutOfBoundsException e){
				clearFam(admixData);
				return -3;
			}
		}
		return 1;
		
	}

	private void clearFam(AdmixSubject[] admixData) {
		for (AdmixSubject a:admixData){
			a.setName(null);
		}
		
	}
}
