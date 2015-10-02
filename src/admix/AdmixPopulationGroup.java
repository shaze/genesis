package admix;

import java.io.Serializable;

/**This class contains the information about one PopulationGroup object,
 * including:
 * <ul>
 * <li> the ID of the group
 * <li> the order of the group in the key
 * <li> name and display name of the group
 * <li> the number of members in the group
 * </ul>
 * 
 * This class is a child of {@link AdmixGraph}
 */
public class AdmixPopulationGroup implements Serializable{	 

	private static final long serialVersionUID = -6395240723605643479L;
	private String displayName,name;
	private int ID,order,noMembers,noVisibleMembers=-1;
	private int row=0;
	private boolean visible=true;
	
	public AdmixPopulationGroup(int ID, String name, int order){
		this.setID(ID);
		this.order = order;
		this.displayName = name;
		this.name=name;
	}
	
	public int getOrder(){
		return order;
	}
	
	public void setOrder(int order){
		this.order=order;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName){
		this.displayName=displayName;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getNoMembers() {
		return noMembers;
	}

	public void setNoMembers(int noMembers) {
		this.noMembers = noMembers;
	}

	public int getNoVisibleMembers() {
		return noVisibleMembers;
	}

	public void setNoVisibleMembers(int noVisibleMembers) {
		this.noVisibleMembers = noVisibleMembers;
	}

	public boolean getVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}



	
}
