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

import pca.PCAGraph;

/**
 * This class creates the admix project save files by serializing the class and writing it to a path
 * It also handles the loading of these files
 */
public class PCASaveAndLoad {
	/**
	 * This method saves an @link{PCAGraph} as a project at the given file location 
	 * @param graph the pca graph
	 * @param fileName the file where to write the proj
	 * @throws IOException 
	 */
	public static void savePCA(PCAGraph graph,String fileName)throws IOException{	    
	    FileOutputStream fos = new FileOutputStream(fileName);
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(graph);
	    oos.close();
	}
	
	/**
	 * This method loads a @link{PCAGraph} from a project at the given file location 
	 * @param fileName the file where to load the proj from
	 * @return the @link{PCAGraph} loaded from the file
	 * @throws Exception 
	 */
	public static PCAGraph loadPCA(String fileName) throws Exception{	 
	   FileInputStream fin = new FileInputStream(fileName);
	   ObjectInputStream ois = new ObjectInputStream(fin);
	   PCAGraph graph= (PCAGraph) ois.readObject();
	   ois.close();
	   return graph;
	}

}
