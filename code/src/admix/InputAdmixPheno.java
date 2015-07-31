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
 * This class handles the validation and importing of the phenotype file for the admixture graphs
 */
public class InputAdmixPheno {
	
	/**
	 * @param path the location of the phenotype file
	 * @param admixData the array of AdmixSubjects to be populated with phenotype information
	 * @return -1 if there was an error or the file was the incorrect format<p>
	 * 1 if the file was validated
	 */
	public int importPheno(String path,AdmixSubject[] admixData){
		
		ReadFile rf = new ReadFile();
		String[] input = rf.readFile(path);
		if (input == null){
			return -1;
		}
		int check=fillPheno(admixData, input);
		return check;
		
	}
		
	
	
	public int fillPheno(AdmixSubject[] admixData, String[] input){
		String[][] phenos = new String[admixData.length][]; 
		int count=0;
		for(String s:input){
			String[] data = s.split("\\s+|:");
			int pos = search(admixData,data[0]+" "+data[1]);
			if(pos>-1){
				phenos[pos]=(data);
				count++;
			}
		}
		
		if(count>0){
			//commit pheno data
			clearPheno(admixData);
			for(int i =0;i<phenos.length;i++){
				if(phenos[i]!=null){
					admixData[i].setPhenotypeData(phenos[i]);
				}else{
					admixData[i].setPhenotypeData(admixData[i].getName().split(" "));
				}
				
			}
		}
		return count;
		
	}

	
	private int search(AdmixSubject[] admixData,String name){
		for(int i=0;i<admixData.length;i++){
			if(name.equals(admixData[i].getName())){
				return i;
			}
		}
		return -1;
	}
	
	private void clearPheno(AdmixSubject[] admixData){
		for (AdmixSubject a: admixData){
			a.setPhenotypeData(null);
		}
	}



}
