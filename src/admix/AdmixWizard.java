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

import java.util.ArrayList;
import java.util.Arrays;

import main.UI;
import shared.WaitDialog;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;

import main.FontDialog;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;


public class AdmixWizard extends Wizard {
	public AdmixProj proj;
	public boolean finished=false, horizontal, headingUnderline;
	public boolean[] options = new boolean[6];
	public FontData headingFont,popFont;
	public String heading;
	public int  phenoColumn, graphHeight, separationDistance, subjectWidth;
	public int[] margins;
        public int  sortoption;

        private char type;

	public AdmixWizard(char type) {//type 'o'=options 'n'=new 'i'=first screen only
		this.type=type;
	}

	public AdmixWizard(char type, AdmixProj proj) {//type 'o'=options 'n'=new 'i'=first screen only
		this.type=type;
		this.proj=proj;
		this.phenoColumn=proj.getPhenoColumn();	
		headingUnderline = proj.getHeadingUnderline();
	}

	public void addPages() {		
		if(type=='n'){
			addPage(new AdmixInputPage());
			addPage(new AdmixOptionsPage());
		}else if(type=='o'){				
			addPage(new AdmixOptionsPage(proj));
		}else{
			addPage(new AdmixInputPage(proj));
		}


	}

	public boolean performFinish() {
		if(type=='n'||type=='i'){
			AdmixInputPage inputPage = getAdmixInputPage();
			this.proj = inputPage.proj;		  

			if(inputPage.phenod){
				phenoColumn = inputPage.comboPheno.getSelectionIndex();  
			}else{
				phenoColumn=-3;
			}


		}
		if(type=='o'||type=='n'){		  

			AdmixOptionsPage optionsPage = (AdmixOptionsPage) getPage(AdmixOptionsPage.PAGE_NAME);
			options[0]=optionsPage.tableBoxes.getItem(0).getChecked();
			options[1]=optionsPage.tableBoxes.getItem(1).getChecked();

			headingFont=optionsPage.headingFontData;
			headingUnderline=optionsPage.headingUnderline;	
			popFont=optionsPage.popFontData;
			margins=optionsPage.margins;
				
			heading=optionsPage.textHeading.getText();
			if(heading.equals("Set Heading")){
				heading="";
			}

			graphHeight = optionsPage.graphHeight.getSelection();
			separationDistance = optionsPage.seperationDistance.getSelection();
			subjectWidth = optionsPage.subjectWidth.getSelection();
			horizontal = (optionsPage.comboHorizontal.getSelectionIndex()==0);
		}
		finished=true;

		return true;
	}



	private AdmixInputPage getAdmixInputPage() {
		return (AdmixInputPage) getPage(AdmixInputPage.PAGE_NAME);
	}

	public boolean performCancel() {
		finished=false;
		return true;
	}

	public IWizardPage getNextPage(IWizardPage page) {
		if (page instanceof AdmixInputPage) {
			AdmixOptionsPage optionsPage = (AdmixOptionsPage) getPage(AdmixOptionsPage.PAGE_NAME);        
			return optionsPage;

		}

		IWizardPage nextPage = super.getNextPage(page);
		if (nextPage instanceof AdmixOptionsPage) {

		}
		return nextPage;
	}
}

class AdmixOptionsPage extends WizardPage {
	public static final String PAGE_NAME = "Summary";

	private Label textLabel;
	Table tableBoxes;  
	FontData popFontData=new FontData("Arial", 10,SWT.BOLD  ),
		 headingFontData=new FontData("Arial", 20, SWT.BOLD  );; 
	Combo comboHorizontal;
	Text textHeading;
	AdmixProj proj;
	Spinner graphHeight,subjectWidth,seperationDistance;
	Boolean headingUnderline=false;
	int[] margins=new int[]{0,0,0,0};

	public AdmixOptionsPage() {
	    	    super(PAGE_NAME, "Options", null);
	}
	public AdmixOptionsPage(AdmixProj proj) {
		super(PAGE_NAME, "Options", null);
		this.proj=proj;
	}

	public void createControl(Composite parent) {
		Composite topLevel = new Composite(parent, SWT.NONE);    
		topLevel.setLayout(new GridLayout(2,false));

		GridData doubleGD = new GridData(SWT.FILL, SWT.FILL, false, false,2,1);

		//heading options
		textHeading = new Text(topLevel,SWT.LEFT|SWT.BORDER);
		textHeading.setLayoutData(doubleGD);
		textHeading.setText("Set Heading");

		Button buttonFontHeading = new Button(topLevel, SWT.NONE);
		buttonFontHeading.setText("Select Heading Font");
		buttonFontHeading.setLayoutData(doubleGD);
		buttonFontHeading.addSelectionListener(new HeadingFontSelectionListener());

		//axis options
		tableBoxes=new Table(topLevel, SWT.CHECK|SWT.V_SCROLL);
		tableBoxes.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,2,2));	
		fillTable(tableBoxes);
		
		Button buttonFontPop = new Button(topLevel, SWT.NONE);
		buttonFontPop.setText("Select Population Group Label Font");
		buttonFontPop.setLayoutData(doubleGD);	
		buttonFontPop.addSelectionListener(new FontSelectionListener());

		//
		Label labelHeight = new Label(topLevel,SWT.NONE);
		labelHeight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,1,1));
		labelHeight.setText("Set Graph Height");

		graphHeight = new Spinner(topLevel,SWT.BORDER);
		graphHeight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1,1));	
		graphHeight.setSelection(100);
		graphHeight.setMinimum(30);
		graphHeight.setMaximum(9999);
		//
		Label labelWidth = new Label(topLevel,SWT.NONE);
		labelWidth.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,1,1));
		labelWidth.setText("Set Thickness of Each Subject");

		subjectWidth = new Spinner(topLevel,SWT.BORDER);
		subjectWidth.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1,1));	
		subjectWidth.setSelection(1);
		subjectWidth.setMinimum(1);
		subjectWidth.setMaximum(30);
		//
		Label labelSeperation = new Label(topLevel,SWT.NONE);
		labelSeperation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,1,1));
		labelSeperation.setText("Set Distance between Graphs");

		seperationDistance = new Spinner(topLevel,SWT.BORDER);
		seperationDistance.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1,1));	
		seperationDistance.setSelection(25);
		seperationDistance.setMinimum(15);
		seperationDistance.setMaximum(100);
		
		Button buttonMargins = new Button(topLevel, SWT.NONE);
		buttonMargins.setText("Set Margins");
		buttonMargins.setLayoutData(doubleGD);
		buttonMargins.addSelectionListener(new MarginsSelectionListener());
		
		comboHorizontal = new Combo(topLevel,SWT.BORDER| SWT.READ_ONLY);
		comboHorizontal.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,2,1));
		comboHorizontal.setItems(new String[]{"Horizontal","Vertical"});		
		comboHorizontal.select(0);
		

		
		if(proj!=null){
			tableBoxes.getItem(0).setChecked(proj.getShowBorder());
			tableBoxes.getItem(1).setChecked(proj.getShowPopLabels());		
			if(proj.getHeading()==""){
				textHeading.setText("Set Heading");
			}else{
				textHeading.setText(proj.getHeading());
			}

			headingFontData=proj.getHeadingFont();
			popFontData=proj.getGroupFont();
			graphHeight.setSelection(proj.getGraphHeight());
			seperationDistance.setSelection(proj.getSeparationDistance());
			subjectWidth.setSelection(proj.getSubjectWidth());
			if(!proj.isHorizontal()){
				comboHorizontal.select(1);
			}
			margins=proj.getMargins();

		}
		setControl(topLevel);
		setPageComplete(true);
	}
	
	private class FontSelectionListener implements SelectionListener{
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			FontDialog fd = new FontDialog(popFontData, false);				
			fd.run(UI.display);
			if(fd.getFont()!=null){				
				popFontData = fd.getFont(); 
				headingUnderline = fd.getUnderlined();
			}
			
			
		}	  
	}
	
	private class MarginsSelectionListener implements SelectionListener{
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {						
			int[] m;
			if(proj==null){
				m=new int[]{0,0,0,0};
			}else{
				m=proj.getMargins();
			}
			MarginDialog md = new MarginDialog(m);				
			md.run(UI.display);
			margins=md.getMargins();								
			
		}	  
	}
	
	



	private class HeadingFontSelectionListener implements SelectionListener{
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			FontDialog fd = new FontDialog(headingFontData, headingUnderline);				
			fd.run(UI.display);
			if(fd.getFont()!=null){
				headingUnderline=fd.getUnderlined();
				headingFontData = fd.getFont(); 
			}
		}
		
		
	}

	
	public void updateText(String newText) {
		textLabel.setText(newText);
	}


	private void fillTable(Table tableBoxes){
		String[] items=new String[] {"Show Borders","Show Population Group Labels"};

		for (String str:items) {
			TableItem item = new TableItem(tableBoxes, SWT.NONE);
			item.setText(str);	
			item.setChecked(true);
		}
	}

}

class AdmixInputPage extends WizardPage {
	public AdmixProj proj;
	private Text textData,textPheno,textFam;
	private Button btnData,btnPheno,btnFam;
	public boolean phenod=false;




	public static final String PAGE_NAME = "Input";


	public AdmixInputPage() {
		super(PAGE_NAME, "Input Files", null);
	}


	public AdmixInputPage(AdmixProj proj) {	 
		super(PAGE_NAME, "Admixture Options", null);
		this.proj=proj;
	}

	private class FamListener implements SelectionListener{


		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {

		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			FileDialog dialog = new FileDialog(UI.display.getActiveShell(),
					SWT.APPLICATION_MODAL);


			String path = dialog.open();
			if(path==null){ //happens when dialog is cancelled
				return; //do nothing
			}
			WaitDialog.start();

			InputFam input = new InputFam();
			for(AdmixGraph graph:proj.getGraphs()){
				int result = input.importFam(path, graph.getAdmixData());
				if(result!=1){
					MessageBox messageBox = new MessageBox(UI.display.getActiveShell(), SWT.ICON_ERROR);
					messageBox.setMessage("The selected fam file is invalid.");
					messageBox.setText("File Import Error "+result);
					messageBox.open();
					WaitDialog.end();
					return;
				}
			}
			WaitDialog.end();
			proj.setFam(true);
			textFam.setText(path);
		//	btnData.setEnabled(false);
			btnFam.setEnabled(false);
			btnPheno.setEnabled(true);
			
			setPageComplete(true);

		}

	}

	private class DataListener implements SelectionListener{
		boolean first;
		String path;

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {			
			AdmixSubject[] data = getPathAndReadInput();
			if(data==null){
				return;
				/*this means the dialog was cancelled*/
				}

			if(data[0].getPercent(0)<0){  
				fileImportErrorMessageBox(data[0].getPercent(0));					
			}else{							
				if(proj==null){
					createNewProj();
				}

				AdmixGraph graph = new AdmixGraph(proj);
				setDefaultGraphValues(graph, data);
				
				if(proj.getGraphs().size()>0){//ie if this isnt the first graph in the project
					if(!checkSameSubjects(graph,proj.getGraphs().get(0))){
						notCompatibleMessageBox();						
						WaitDialog.end();
						return;
					}
					if(proj.hasFam()){
						getFamFromAnotherGraph(graph,proj.getGraphs().get(0));
						if(proj.hasPheno()){
							getDataFromAnotherGraph(graph,proj.getGraphs().get(0));
						}
					}
					
				}
				setTextBoxTextToPath();
				proj.addGraph(graph);									
				if(first){		
					//btnData.setEnabled(false);
					btnFam.setEnabled(true);			
					textFam.setText("");													
					setPageComplete(false);			
					btnFam.setFocus();						
				}else if(proj.hasPheno()){	

				}

			}	
			WaitDialog.end();
		}

		private void setTextBoxTextToPath() {
			if(textData.getText()==""){
				textData.setText(path);	
			}else{
				textData.setText(path+"; "+path);
			}
			
		}

		private void notCompatibleMessageBox() {
			MessageBox messageBox = new MessageBox(UI.display.getActiveShell(), SWT.ICON_ERROR);
			messageBox.setMessage("The selected file is not compatible with the current data.");
			messageBox.setText("Error");
			messageBox.open();
			
		}

		private void setDefaultGraphValues(AdmixGraph graph, AdmixSubject[] data) {
			int maxAnc=data[0].getNoAncestors();

			RGB colours[] = getProjectColors(proj,maxAnc);

			graph.setColours(colours);
			graph.setAdmixData(data,colours);						
			if(proj.getGraphs().size()>0){
				first=false;
			}else{
				first=true;
			}
			
		}

		private void createNewProj() {
			proj = new AdmixProj();
			proj.setName(getDefaultProjName());
			phenod=false;
			
		}		

		private void fileImportErrorMessageBox(float errorCode) {
			MessageBox messageBox = new MessageBox(UI.display.getActiveShell(), SWT.ICON_ERROR);
			messageBox.setMessage("The selected file is invalid. error "+errorCode);
			messageBox.setText("File Import Error");

			messageBox.open();	
			
		}

		private AdmixSubject[] getPathAndReadInput() {
			FileDialog dialog = new FileDialog(UI.display.getActiveShell(),
					SWT.APPLICATION_MODAL);

			path = dialog.open();
			if(path==null){ //happens when dialog is cancelled
				return null; //do nothing
			}
			WaitDialog.start();

			AdmixInput input = new AdmixInput();
			AdmixSubject[] data = input.readAdmixInput(path);
			
			return data;
					
		}

		private String getDefaultProjName() {
			return "Admix "+ getProjNum();
		}

		private int getProjNum() {
			int max = 0;
			for(AdmixProj g: UI.admixProjects){
				int projNum=extractProjectNum(g.getName());
				if(max<projNum){
					max=projNum;
				}
			}
			return max+1;
			
		}

		private int extractProjectNum(String name) {
			try{
				if(name.substring(0, 6).equals("Admix ")){
					return Integer.parseInt(name.substring(6));
				}
			}catch(NumberFormatException e){}
						
			return 0;
		}

		private void getDataFromAnotherGraph(AdmixGraph graph,AdmixGraph admixGraph) {
			getPhenoFromAnotherGraph(graph,proj.getGraphs().get(0));
			getVisibleFromAnotherGraph(graph,proj.getGraphs().get(0));
		}

	
		private RGB[] getProjectColors(AdmixProj proj, int maxAnc) {
			RGB[] result;		
			if(proj.getColours()==null){
				result = generateDefaultColours(maxAnc);			
			}else{
				result = useExistingColours(proj,maxAnc);
			}

			proj.setColours(new ArrayList<RGB>(Arrays.asList(result)));

			return result;
		}

		private RGB[] useExistingColours(AdmixProj proj, int maxAnc) {
			RGB[] result;
			if(maxAnc>proj.getColours().size()){
				result=getColoursFromProjAndGenerateMore(maxAnc,proj);
			}else {
				result=proj.getColours().toArray(new RGB[maxAnc]);
			}

			return result;
		}

		private RGB[] getColoursFromProjAndGenerateMore(int maxAnc, AdmixProj proj) {
			RGB[] result = new RGB[maxAnc];
			for(int i=0;i<proj.getColours().size();i++){
				result[i]=proj.getColours().get(i);
			}
			for(int i=proj.getColours().size();i<maxAnc;i++){
				result[i]=defaultColour(i);
			}

			return result;
		}

		@SuppressWarnings("unused")
		private RGB[] getXColoursFromProj(int X, AdmixProj proj) {
			RGB[] result = new RGB[X];
			for(int i=0;i<X;i++){
				result[i]=proj.getColours().get(i);
			}

			return result;
		}

		private RGB[] generateDefaultColours(int maxAnc) {
			ArrayList<RGB> colours = new ArrayList<RGB>();
			for(int i=0;i<maxAnc;i++){
				colours.add(defaultColour(i));
			}
			return colours.toArray(new RGB[colours.size()]);
		}

		private void getFamFromAnotherGraph(AdmixGraph graph, AdmixGraph admixGraph) {
			for(int i=0;i<graph.getAdmixData().length;i++){
				graph.getAdmixData()[i].setName(admixGraph.getAdmixData()[i].getName());
			}

		}

		private void getPhenoFromAnotherGraph(AdmixGraph graph, AdmixGraph fromGraph) {

			for(int i=0;i<graph.getAdmixData().length;i++){
				AdmixSubject newSubject=graph.getAdmixData()[i];
				AdmixSubject oldSubject=fromGraph.getAdmixData()[i];
				newSubject.setPhenotypeData(oldSubject.getPhenotypeData());
				newSubject.setGroup(oldSubject.getGroups());
			}
			graph.getProj().setPhenoColumn(fromGraph.getProj().getPhenoColumn());
			//AdmixWorkflow.createPopGroups(proj);			
			
			/*for(AdmixPopulationGroup pop : graph.getGroups()){				
				AdmixPopulationGroup oldGroup = AdmixWorkflow.findPopGroup(pop.getName(), fromGraph);
				pop.setOrder(oldGroup.getOrder());
				pop.setDisplayName(oldGroup.getDisplayName());				
			}*/
		}

		
		private void getVisibleFromAnotherGraph(AdmixGraph graph, AdmixGraph admixGraph) {
			for(int i=0;i<graph.getAdmixData().length;i++){
				graph.getAdmixData()[i].setVisible(admixGraph.getAdmixData()[i].getVisible());
			}
			
		}

		private boolean checkSameSubjects(AdmixGraph graph, AdmixGraph admixGraph) {
			if(graph.getAdmixData().length!=admixGraph.getAdmixData().length){
				return false;
			}			
			return true;	
		}

	}

	private RGB defaultColour(int index){
		RGB[] colours = new RGB[]{new RGB(255,0,0),//red
				new RGB(0,193,0),//green
				new RGB(0,0,255),//blue
				new RGB(255,255,0),//yellow
				new RGB(255,0,255),//pink
				new RGB(0,255,255),//turquoise
				new RGB(255,108,0),//orange
				new RGB(229,229,229),//light grey
				new RGB(0,0,150),//etc
				new RGB(105,105,105),//etc
				new RGB(205,102,29),
				new RGB(205,173,0),
				new RGB(85,107,47),
				new RGB(150,150,150),
				new RGB(50,50,50),
				new RGB(240,128,128),
				new RGB(255,193,193),
				new RGB(139,125,123),
				new RGB(0,0,0),
				new RGB(135,0,0),
				new RGB(178,34,34),
				new RGB(255,160,122),
				new RGB(240,10,10),
				new RGB(20,20,240),
				new RGB(20,240,20),
				new RGB(238,149,114),
				new RGB(230,30,30),
				new RGB(25,25,140),
				new RGB(160,30,30),
				new RGB(205,129,98)};
index = index % colours.length;
return colours[index];
	}

	private class PhenoListener implements SelectionListener{
		Control[] controls;
		PhenoListener(Control[] controls){
			this.controls=controls;
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {	

			FileDialog dialog = new FileDialog(UI.display.getActiveShell(),
					SWT.APPLICATION_MODAL);

			String path = dialog.open();
			if(path==null){ //happens when dialog is cancelled
				return; //do nothing
			}
			WaitDialog.start();


			for(AdmixGraph graph:proj.getGraphs()){
				InputAdmixPheno input = new InputAdmixPheno();
				int check = input.importPheno(path, graph.getAdmixData());				
				if(check<0){  
					UI.errorMessage("The selected phenotype file is invalid.");					
					WaitDialog.end();
					return;				
				}else if (check==0){ 
					UI.errorMessage("The selected phenotype file does not contain any phenotype "
							+ "data concerning any individuals in the current data.");					
					WaitDialog.end();
					return;				
				}				
			}
			proj.createPopGroups();

			textPheno.setText(path);
			String[] columns = new String[proj.getGraphs().get(0).getAdmixData()[1].getPhenotypeData().length];
			for(int i=0;i<proj.getGraphs().get(0).getAdmixData()[1].getPhenotypeData().length;i++){
				columns[i]="Column "+String.valueOf(i+1);
			}

			((Combo) controls[0]).setItems(columns);	

			if(proj.getPhenoColumn()==-1){
				((Combo) controls[0]).select(0);
			}else{
				((Combo) controls[0]).select(proj.getPhenoColumn());
			}

			for(Control c:controls){
				c.setVisible(true);
			}

			phenod=true;
			WaitDialog.end();
			btnPheno.setEnabled(false);
			//btnMore.setEnabled(true);
			//btnMore.setVisible(true);
			//textMore.setText("");
			//textMore.setVisible(true);
			setPageComplete(true);
		}

	}
	Combo comboPheno;

	public void createControl(Composite parent) {	

		Composite topLevel = new Composite(parent, SWT.NONE);

		

		topLevel.setLayout(new GridLayout(4,false));
		if(proj==null){//NEW GRAPH
			//data button

			btnData = new Button(topLevel, SWT.CENTER);
			textData = new Text(topLevel, SWT.BORDER);
			btnFam = new Button(topLevel, SWT.CENTER);
			textFam = new Text(topLevel, SWT.BORDER);
			btnPheno = new Button(topLevel, SWT.CENTER);
			textPheno = new Text(topLevel, SWT.BORDER);
			Label blank = new Label(topLevel, SWT.CENTER);
			Label phenoLabel = new Label(topLevel,SWT.WRAP|SWT.CENTER);
			comboPheno = new Combo(topLevel, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);
			//data button			
			btnData.setText("Import Data File");  
			btnData.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
			btnData.addSelectionListener(new DataListener());	    
			textData.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,3,1));
			textData.setEditable(false);  
			//fam button
			btnFam.setText("Import Fam File");  
			btnFam.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
			btnFam.addSelectionListener(new FamListener());
			btnFam.setEnabled(false);	
			textFam.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,3,1));
			textFam.setEditable(false);
			//pheno button	    
			btnPheno.setText("Import Phenotype File");
			btnPheno.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
			btnPheno.setEnabled(false);	    
			btnPheno.addSelectionListener(new PhenoListener(new Control[] {comboPheno,phenoLabel}));	    
			textPheno.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
			textPheno.setEditable(false);    
			
			//blank label for layout purposes
			blank.setText("");
			blank.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,4,1));
			phenoLabel.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,3,1));
			phenoLabel.setText("Which column represents the phenotype data?");
			phenoLabel.setVisible(false);

			comboPheno.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,0));
			comboPheno.setVisible(false);

			setPageComplete(false);
		}else{//ANOTHER ADMIX AND PHENO
			//pheno button
			Label phenoLabel;
			btnData = new Button(topLevel, SWT.CENTER);
			textData = new Text(topLevel, SWT.BORDER);
			btnPheno = new Button(topLevel, SWT.CENTER);
			textPheno = new Text(topLevel, SWT.BORDER);

			phenoLabel = new Label(topLevel,SWT.WRAP|SWT.CENTER);
			phenoLabel.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,3,1));
			phenoLabel.setText("Which column which represents the phenotype data?");
			comboPheno = new Combo(topLevel, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);
			btnData.setText("Import Additional Data Files");
			btnData.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
			btnData.setEnabled(true);
			btnData.addSelectionListener(new DataListener());
			textData.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
			textData.setEditable(false);
			btnPheno.setText("Import Phenotype File");
			btnPheno.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
			btnPheno.setVisible(false);
			textPheno.setVisible(false);
			textPheno.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
			textPheno.setEditable(false);  

			//"select pheno column" combo
			comboPheno.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,0));
			try{
				if(proj.getGraphs().get(0).getAdmixData()[1].getPhenotypeData().length-2>=1){
					phenod=true;
					String[] columns = new String[proj.getGraphs().get(0).getAdmixData()[1].getPhenotypeData().length];
					for(int i=0;i<proj.getGraphs().get(0).getAdmixData()[1].getPhenotypeData().length;i++){
						columns[i]="Column "+String.valueOf(i+1);
					}
					comboPheno.setItems(columns);				
					if(proj.getPhenoColumn()==-1){
						comboPheno.select(0);
					}else{
						comboPheno.select(proj.getPhenoColumn());
					}
				}else{
					comboPheno.setVisible(false);
					phenoLabel.setVisible(false);
				}	
			}catch(NullPointerException e){
				comboPheno.setVisible(false);
				phenoLabel.setVisible(false);
			}


			setPageComplete(true);
		}


		setControl(topLevel);


	}



}
