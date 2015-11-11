/*************************************************************************
 * Genesis � program for creating structure and PCA plots of genotype data
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

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Iterator;
import org.eclipse.swt.graphics.RGB;
import shared.GLabel;


/**
 * This class contains all the information needed to describe and store an admixture graph including
 *  <ul>
 * <li>an array of {@link AdmixSubject} describing the individuals on the graph
 * <li>an array of {@link AdmixPopulationGroup} describing the external population groups
 * <li>an array of {@link Ancestor} describing the internal population groups
 * <li>the current selected ancestors
 * <li>all user specified options (which column of the phenofile specifies groups, showborder etc.)
 * </ul>
 * 
 * 
 */

class PopCompare implements Comparator<Integer> {


	int cols[];
	AdmixSubject admixData[];

	public PopCompare(int cols[],AdmixSubject admixData[]) {
		this.admixData = admixData;
		this.cols=cols;
	}

	public int compare(Integer a, Integer b) {
		int i,j;
		float m=0;

		for(i=0; (i<cols.length) && (m ==0) ; i++) {
			j=cols[i];
			m=(
					(admixData[a.intValue()].getRatio()[j]-
							admixData[b.intValue()].getRatio()[j]
							)
					);
		}
		if (m<0) return -1;
		else if (m>0) return 1;
		else return 0;

	}

	boolean equals(Integer a, Integer b) {
		return a.equals(b);
	}

}


class ColCompare implements Comparator<Integer> {

	AdmixSubject admixData[];
	int          colour;
	int          choices[];
	int          k;

	public ColCompare(int col,AdmixSubject admixData[]) {
		k=admixData[0].getRatio().length;
		int curr;
		this.admixData = admixData;
		colour=col;
		choices=new int[k];
		choices[0]=colour;
		curr=1;
		for(int i=0; i<k; i++) 
			if (i != col) {
				choices[curr]=i;
				curr++;
			}
	}

	public int compare(Integer a, Integer b) {
		int m=0;
		int i,colour;
		for(i=0; i<k; i++) {
			colour=choices[i];
			// Had a varaint of below but it makes assumptions which fail on some data sets
			// and get violation of sort contract
			//m= Math.round(0x01000000*(
			//		admixData[a.intValue()].getRatio()[colour]-
			//		admixData[b.intValue()].getRatio()[colour]));
			if ( admixData[a.intValue()].getRatio()[colour] > admixData[b.intValue()].getRatio()[colour]) {
				return 1;
			}
			if ( admixData[a.intValue()].getRatio()[colour] < admixData[b.intValue()].getRatio()[colour]) {
				return -1;
			}		
		}
		return 0;
	}

	boolean equals(Integer a, Integer b) {
		int m=0;
		m=compare(a,b);
		return m==0;
	}
}




abstract class SortColour implements Serializable {

	protected AdmixGraph graph;

	protected abstract void sortOnColours(ArrayList<Integer> unsort, int count);

	public void sortGraph(AdmixProj proj, int phenoColumn) {
		ArrayList<Integer> unsort;
		AdmixSubject a;
		int count;
		unsort = new ArrayList<Integer>(graph.admixData.length);
		// If there are no groups
		if (proj.getGroups() == null) {
			count = 0;
			unsort.clear();
			for (int i=0; i< graph.admixData.length; i++) {
				a = graph.admixData[i]; 
				if(a.getVisible()) {
					unsort.add(i);
					count++;
				}
			}
			sortOnColours(unsort,count);
			return;
		};
		// If thee are grups
		for(AdmixPopulationGroup p:proj.getGroups()[phenoColumn]){
			count = 0;
			unsort.clear();
			for (int i=0; i< graph.admixData.length; i++) {
				a = graph.admixData[i]; 
				if(a.getGroups()[phenoColumn].getID()==p.getID())
					if(a.getVisible()&&a.getGroups()[phenoColumn].getVisible()) {
						unsort.add(i);
						count++;
					}
			}
			sortOnColours(unsort,count);
		}
	}


}



class ByColour extends SortColour {

	int colour;


	public ByColour(AdmixGraph g) {        
		graph = g;
		colour = 0;
	}

	public ByColour(AdmixGraph g, int choice) {        
		graph = g;
		colour = choice;
	}


	public void setKeyColour(int col) {
		colour=col;
	}

	protected void sortOnColours(ArrayList<Integer> unsort, int count) {
		ArrayList<Integer> sort;
		ColCompare popcmp;

		popcmp = new ColCompare(colour,graph.admixData);
		sort=new ArrayList<Integer>(unsort);
		Collections.sort(sort,popcmp);

		for (int i=0; i<unsort.size(); i++) 
			graph.perm[unsort.get(i).intValue()]=sort.get(i).intValue();


	}
}



class FamOrder  extends SortColour {


	public FamOrder(AdmixGraph g) {        
		graph = g;
	}


	protected void sortOnColours(ArrayList<Integer> unsort, int count) {}

	public void sortGraph(AdmixProj proj, int phenoColumn) {

		for (int i=0; i< graph.admixData.length; i++) {
			graph.perm[i]=i;
		}

	}
}



class SortMultiPrettily extends SortColour {


	public SortMultiPrettily(AdmixGraph g) {        
		graph = g;
	}

	protected void sortOnColours(ArrayList<Integer> unsort, int count) {
		float col[];
		int scol[];
		ArrayList<Integer> sort;
		PopCompare popcmp;
		int k;

		k = graph.admixData[0].getRatio().length;
		col= new float [k];
		scol=new int [k]; // which colours are the most important
		graph.getRatiosForPop(unsort,count,col,scol);
		popcmp = new PopCompare(scol,graph.admixData);
		sort=new ArrayList<Integer>(unsort);
		Collections.sort(sort,popcmp);

		for (int i=0; i<unsort.size(); i++) 
			graph.perm[unsort.get(i).intValue()]=sort.get(i).intValue();


	}

}




public class AdmixGraph implements Serializable, Iterable<AdmixSubject> {	

	private static final long serialVersionUID = -1721172661147218242L;

	AdmixSubject[] admixData;
	int perm []; // permutation which should be used
	private Ancestor[] ancestors;
	private int   sortoption=0;
	private int[] shownAncestors;
	private boolean groupLabels=true,drawBox=true;
	private int width; 
	private RGB[] colours;
	private AdmixProj proj;
	private SortColour sorters [] = 
		{new FamOrder(this), new SortMultiPrettily(this),new ByColour(this)};

	@Override
	public Iterator<AdmixSubject> iterator() {
		return new AdmixSubjectIterator(admixData, perm);
	}

	public AdmixGraph(AdmixProj proj){
		this.proj = proj;
	}

	public void sortGraph(AdmixProj proj, int phenoColumn, int option) {
		sorters[option].sortGraph(proj, phenoColumn);
	}

	public void sortGraphOnColour(AdmixProj proj, int phenoColumn, int choice) {
		ByColour colourer = new ByColour(this,choice);

		colourer.sortGraph(proj,phenoColumn);
	}

	public void setAdmixData(AdmixSubject[] data,RGB[] colours){
		admixData = data;	
		perm = new int[data.length];
		for(int i=0; i< data.length; i++) perm[i]=i;
		ancestors=new Ancestor[data[0].getNoAncestors()];
		for(int i=0;i<data[0].getNoAncestors();i++){
			ancestors[i]=new Ancestor(i,"Ancestor "+i, i, colours[i]);
		}
	}

	public AdmixSubject[] getAdmixData(){
		return admixData;
	}


	public AdmixSubject getSubjPerm(int i) {
		return admixData[perm[i]];
	}

	public ArrayList<GLabel> getLabels() {
		return proj.getLabels();
	}

	public int getPerm(int i) {
		return perm[i];
	}

	public Ancestor[] getAncestors() {
		return ancestors;
	}

	public void setAncestors(Ancestor[] ancestors) {
		this.ancestors = ancestors;
	}


	public int[] getShownAncestors() {
		return shownAncestors;
	}



	public  int getMaxVisibleOrder() {
		int order = 0;
		for(AdmixPopulationGroup grp:proj.getCurrentGroups()){
			if(grp.getVisible()){
				if (grp.getOrder()>order)
					order=grp.getOrder();
			}
		}
		return order;
	}

	public void setShownAncestors(int[] shownAncestors) {
		this.shownAncestors = shownAncestors;
	}

	public boolean isGroupLabels() {
		return groupLabels;
	}

	public void setGroupLabels(boolean groupLabels) {
		this.groupLabels = groupLabels;
	}

	public boolean isDrawBox() {
		return drawBox;
	}

	public void setDrawBox(boolean drawBox) {
		this.drawBox = drawBox;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public RGB[] getColours() {
		return colours;
	}

	public void setColours(RGB[] colours) {
		this.colours = colours;
	}

	public AdmixProj getProj() {
		return proj;
	}

	public void setProj(AdmixProj proj) {
		this.proj = proj;
	}



	private static int getMembersOfPop(AdmixGraph curr, 
			ArrayList<Integer> unsort,
			int phenoColumn,
			AdmixPopulationGroup p) {
		AdmixSubject ad;
		int count = 0;
		unsort.clear();
		for (int i=0; i<curr.admixData.length; i++) {
			ad = curr.admixData[i]; 
			if((p==null) || (ad.getGroups()[phenoColumn].getID()==p.getID())) {
				unsort.add(i);
				count++;
			}
		}
		return count;
	}





	private void matchRatios(AdmixGraph other, ArrayList<Integer> unsort,
			int phenoColumn, AdmixPopulationGroup p,
			int weights[]) {
		int     this_scol [], other_scol [], count, i;
		float   this_rat  [], other_rat[];
		int k;

		// Find the indivds in this pop in src graph and 
		// work out average colours
		k = other.admixData[0].getNoAncestors();
		count = getMembersOfPop(other,unsort,phenoColumn,p);
		other_scol = new int [k];
		other_rat  = new float [k];
		other.getRatiosForPop(unsort,count,other_rat,other_scol);
		// Ditto for same pop in src graph
		k = admixData[0].getNoAncestors();
		this_scol = new int [k];
		this_rat  = new float [k];
		count = getMembersOfPop(this,unsort,phenoColumn,p);
		getRatiosForPop(unsort,count,this_rat,this_scol);

		// Now match
		int this_big = this_scol[0];
		int oth_big  = other_scol[0];
		int old_order, new_order;

		RGB old_col, new_col;


		if ( (this_rat[this_big]>=0.5) &&
				(other_rat[oth_big]>0.5) &&
				(count > weights[this_big])) {
			old_col = ancestors[this_big].getColour();
			new_col = other.ancestors[oth_big].getColour();
			old_order = ancestors[this_big].getOrder();
			new_order = other.ancestors[oth_big].getOrder();
			if (new_order==k) // other graph has more colours
				new_order=k-1;
			weights[this_big]=count;
			// Replace existing use of "new_col"
			for(i=0; i<k; i++) {
				if (ancestors[i].getColour().equals(new_col)) 
					ancestors[i].setColour(old_col);
				if (ancestors[i].getOrder()==new_order) 
					ancestors[i].setOrder(old_order);

			}
			// Recolour most signficant block
			ancestors[this_big].setColour(new_col);
			ancestors[this_big].setOrder(new_order);
		};
	}


	public void baseColoursOn(AdmixGraph other,int phenoColumn) {

		int weights [];

		weights= new int [admixData[0].getNoAncestors()];
		// in general it is possible to match colours in several ways -- goal of the weights array is to
		// use the biggest populations to guide the matching process. If on population i we decide to recolour
		// an ancestral population, then on a later population we will only recolour the same population if
		// it's a bigger populations


		ArrayList<Integer> unsort;

		unsort = new ArrayList<Integer>(admixData.length);
		if (proj.getGroups() == null) 
			matchRatios(other, unsort, phenoColumn, null, weights);
		else {
			for(AdmixPopulationGroup p:proj.getGroups()[phenoColumn]){
				matchRatios(other, unsort, phenoColumn, p, weights);
			}
		}
	}


	public  void 
	getRatiosForPop(
			ArrayList<Integer> thepop,
			int count, float col[], int scol[]) {
		float ratios [];
		int k = admixData[0].getRatio().length;
		int u, mx_i;
		for (int i=0; i<count; i++) {
			float s;
			s=0;
			u = thepop.get(i).intValue();
			ratios = admixData[u].getRatio();
			for (int j=0; j<k; j++) {
				s=s+ratios[j];
				col[j] += ratios[j];
			}
		}
		for (int i=0; i<k; i++) 
			col[i] = col[i]/count;
		// now find the colours in order
		for (int j=0; j<k; j++) {
			mx_i = j;
			for(u=j+1; u<k; u++) 
				if (col[u]>col[mx_i]) mx_i=u;
			scol[j]=mx_i;
			scol[mx_i]=j;
		}
	}

}

