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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import admix.*;

/**
 * This class creates the admix project save files by serializing the class and writing it to a path
 * It also handles the loading of these files
 */
public class AdmixSaveAndLoad {

	/**
	 * This method saves an admix proj as a project at the given file location 
	 * @param admixProj the admix proj
	 * @param fileName the file where to write the proj
	 * @throws IOException 
	 */
	public static void saveAdmix(AdmixSerializable admixProj, String fileName) throws IOException{
		FileOutputStream fos = new FileOutputStream(fileName);
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(admixProj);
	    oos.close();
		
	}
		
	/**
	 * This method loads an admix proj from a project at the given file location 
	 * @param fileName the file where to load the proj from
	 * @return the Admix proj loaded from the file
	 * @throws Exception 
	 */
	public static AdmixSerializable loadAdmix(String fileName) throws Exception{	 
	   FileInputStream fin = new FileInputStream(fileName);
	   ObjectInputStream ois = new ObjectInputStream(fin);
	   AdmixSerializable proj= (AdmixSerializable) ois.readObject();
	   ois.close();
	   return proj;
	}

}
