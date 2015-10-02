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

package pca;

import main.UI;
import shared.WaitDialog;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import pca.input.InputPCAData;
import pca.input.InputPCAPheno;


public class PCAWizard extends Wizard {
	public PCAGraph graph;
	public boolean finished=false, headingUnderline;
	public boolean[] options = new boolean[6];
	public FontData keyFont,headingFont,scaleFont,axisFont;
	public String heading;
	public int keyPos, phenoColumn;
	public int[] pcas;


	private char type; 

	public PCAWizard(char type) {//type 'o'=options 'n'=new 'i'=first screen only
		this.type=type;
	}

	public PCAWizard(char type, PCAGraph graph) {//type 'o'=options 'n'=new 'i'=first screen only
		this.type=type;
		this.graph=graph;
	}

	public void addPages() {
		if(type=='n'){
			addPage(new InputPage());
			addPage(new OptionsPage());
		}else if(type=='o'){				
			addPage(new OptionsPage(graph));
		}else{
			addPage(new InputPage(graph));
		}
	}

	public boolean performFinish() {
		if(type=='n'||type=='i'){
			InputPage inputPage = getInputPage();
			this.graph = inputPage.graph;
			pcas=getPCAs(inputPage.PCAS);
			graph.x=pcas[0]-1;
			graph.y=pcas[1]-1;
			if(pcas.length==3){
				graph.z=pcas[2]-1;
			}else{
				graph.z=-1;
			}

			if(inputPage.phenod){
				phenoColumn = inputPage.comboPheno.getSelectionIndex();  
			}else{
				phenoColumn=-3;
			}
			if(inputPage.rel){			  
				phenoColumn=2;
			}


		}
		if(type=='o'||type=='n'){		  

			OptionsPage optionsPage = (OptionsPage) getPage(OptionsPage.PAGE_NAME);
			options[0]=optionsPage.tableBoxes.getItem(0).getChecked();
			options[1]=optionsPage.tableBoxes.getItem(1).getChecked();
			options[2]=optionsPage.tableBoxes.getItem(2).getChecked();
			options[3]=optionsPage.tableBoxes.getItem(3).getChecked();
			options[4]=optionsPage.tableBoxes.getItem(4).getChecked();
			scaleFont=optionsPage.scaleFontData;
			axisFont=optionsPage.axisFontData;
			headingFont=optionsPage.headingFontData;	
			headingUnderline=optionsPage.headingUnderline;	
			keyFont=optionsPage.keyFontData;
			if(optionsPage.keyPos.getSelectionIndex()==0){
				keyPos=0;
			}else if(optionsPage.keyPos.getSelectionIndex()==1){
				keyPos=1;
			}else{
				keyPos=-1;
			}

			heading=optionsPage.textHeading.getText();
			if(heading.equals("Set Heading")){
				heading="";
			}
		}
		finished=true;

		return true;
	}

	private int[] getPCAs(Combo[] pcas){
		int[] result;
		if(pcas[2].getText().equals("None")){
			result = new int[2];
			for(int i=0;i<2;i++){
				result[i]=Integer.parseInt(pcas[i].getText().split("\\s+")[1]);
			}
		}else{
			result = new int[3];
			for(int i=0;i<3;i++){
				result[i]=Integer.parseInt(pcas[i].getText().split("\\s+")[1]);
			}
		}
		return result;
	}

	private InputPage getInputPage() {
		return (InputPage) getPage(InputPage.PAGE_NAME);
	}

	public boolean performCancel() {
		finished=false;
		return true;
	}

	public IWizardPage getNextPage(IWizardPage page) {
		if (page instanceof InputPage) {
			OptionsPage optionsPage = (OptionsPage) getPage(OptionsPage.PAGE_NAME);        
			return optionsPage;

		}

		IWizardPage nextPage = super.getNextPage(page);
		if (nextPage instanceof OptionsPage) {

		}
		return nextPage;
	}
}

class OptionsPage extends WizardPage {

	public static final String PAGE_NAME = "Summary";

	private Label textLabel;
	Table tableBoxes;
	Combo keyPos;
	Boolean headingUnderline=false;
	FontData keyFontData=new FontData("Arial", 12,SWT.BOLD  )
		,headingFontData=new FontData("Arial", 20, SWT.BOLD  )
		,scaleFontData=new FontData("Arial",10,SWT.BOLD)
		,axisFontData=new FontData("Arial",12,SWT.BOLD); 
	Text textHeading;
	PCAGraph graph;

	public OptionsPage() {
		super(PAGE_NAME, "Options", null);
	}
	public OptionsPage(PCAGraph graph) {
		super(PAGE_NAME, "Options", null);
		this.graph=graph;
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
		tableBoxes.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,2));	
		fillTable(tableBoxes);
		//key
		keyPos=new Combo(topLevel, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);
		keyPos.setItems(new String[] {"Key on Right","Key on Bottom","No Key"});
		keyPos.setLayoutData(doubleGD);
		keyPos.select(0);

		Button buttonFontKey = new Button(topLevel, SWT.NONE);
		buttonFontKey.setText("Select Key Font");
		buttonFontKey.setLayoutData(doubleGD);	
		buttonFontKey.addSelectionListener(new KeyFontSelectionListener());
		//scale font
		Button buttonFontScale = new Button(topLevel, SWT.NONE);
		buttonFontScale.setText("Select Scale Font");
		buttonFontScale.setLayoutData(doubleGD);	
		buttonFontScale.addSelectionListener(new ScaleFontSelectionListener());
		//axis label font
		Button buttonFontAxis = new Button(topLevel, SWT.NONE);
		buttonFontAxis.setText("Select Axis Label Font");
		buttonFontAxis.setLayoutData(doubleGD);	
		buttonFontAxis.addSelectionListener(new AxisFontSelectionListener());
		
		if(graph!=null){
			setControlsToGraph();		
		}	
		setControl(topLevel);
		setPageComplete(true);
	}

	private void setControlsToGraph() {
		tableBoxes.getItem(0).setChecked(graph.getShowAxes());
		tableBoxes.getItem(1).setChecked(graph.getShowAxisLabels());
		tableBoxes.getItem(2).setChecked(graph.getShowBorder());
		tableBoxes.getItem(3).setChecked(graph.isShowGrid());
		tableBoxes.getItem(4).setChecked(graph.isShowMarkers());
		if(graph.getHeading()==""){
			textHeading.setText("Set Heading");
		}else{
			textHeading.setText(graph.getHeading());
		}
		keyFontData=graph.getKeyFont();
		scaleFontData=graph.getScaleFont();
		axisFontData=graph.getAxisFont();
		headingFontData=graph.getHeadingFont();
		if(graph.getKeyPosition()==0){
			keyPos.select(0);
		}else if(graph.getKeyPosition()==1){
			keyPos.select(1);
		}else{
			keyPos.select(2);
		}
		
	}
	
	private class ScaleFontSelectionListener implements SelectionListener{
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			FontDialog fd = new FontDialog(scaleFontData, false);				
			fd.run(UI.display);
			if(fd.getFont()!=null){			
				scaleFontData = fd.getFont(); 	
			}

		}	  
	}
	
	private class AxisFontSelectionListener implements SelectionListener{
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			FontDialog fd = new FontDialog(axisFontData, false);				
			fd.run(UI.display);
			if(fd.getFont()!=null){			
				axisFontData = fd.getFont(); 	
			}

		}	  
	}


	private class KeyFontSelectionListener implements SelectionListener{
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			FontDialog fd = new FontDialog(keyFontData, false);				
			fd.run(UI.display);
			if(fd.getFont()!=null){			
				keyFontData = fd.getFont(); 
				headingUnderline = fd.getUnderlined();
			}

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
		String[] items=new String[] {"Show Axes","Show Axis Labels","Show Border",
				"Show Grid","Show Scale"};

		for (String str:items) {
			TableItem item = new TableItem(tableBoxes, SWT.NONE);
			item.setText(str);	
			item.setChecked(true);
		}
	}

}

class InputPage extends WizardPage {
	public PCAGraph graph;
	private Text textData,textPheno;
	private Button btnData,btnPheno;
	Composite compositePCAs;
	public boolean phenod=false;
	public boolean rel=false;



	public static final String PAGE_NAME = "Input";


	public InputPage() {
		super(PAGE_NAME, "Input Files", null);
	}


	public InputPage(PCAGraph graph) {	 
		super(PAGE_NAME, "PCA Options", null);
		this.graph=graph;


	}

	private class DataListener implements SelectionListener{
		Control[] controls;
		public DataListener(Control[] controls) {
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
			InputPCAData input = new InputPCAData();
			PCASubject[] data = input.readPCAInput(path);
			if(data[0].getData()[0]<-99){   //this is if we have erroneous input. data[0].getData()[0] contains the error code.
				//error codes are explained in AdmixInput
				MessageBox messageBox = new MessageBox(UI.display.getActiveShell(), SWT.ICON_ERROR);
				messageBox.setMessage("The selected file is invalid."+data[0].getData()[0]);
				messageBox.setText("File Import Error");
				messageBox.open();
			}else{			//this means the data had been validated
				phenod=false;
				//fills the graph object
				graph=new PCAGraph();			
				graph.setPCAData(data);
				graph.setRelFile(!(input.eigenFile));

				if(input.eigenFile){
					btnPheno.setEnabled(true);
					textPheno.setText("");
					rel=false;
				}else{
					btnPheno.setEnabled(false);
					textPheno.setText("Phenotype data included in Relate file");
					rel=true;
				}
				textData.setText(path);
				for(Combo c: PCAS){
					try{
						c.removeModifyListener(combMod);
					}catch (IllegalArgumentException e){}
				}
				compositePCAs.setVisible(true);
				setComboValues(PCAS,new int[]{1,2});
				for(Combo c: PCAS){
					c.addModifyListener(combMod);
				}

				setPageComplete(true);
				btnPheno.setFocus();
				for(Control c:controls){
					c.setVisible(false);
				}

			}	
			WaitDialog.end();
		}



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
			InputPCAPheno input = new InputPCAPheno();
			int check = input.importPheno(path, graph.getPCAData());		

			if(check<=0){   //this is if we have erroneous input
				invalidPhenoMessage();
				phenod=false;

			}else{			//this means the data had been validated	
				try{
					phenod=true;

					String[] columns = new String[graph.getPCAData()[1].getPhenotypeData().length];
					for(int i=0;i<graph.getPCAData()[1].getPhenotypeData().length;i++){
						columns[i]="Column "+String.valueOf(i+1);
					}

					((Combo) controls[0]).setItems(columns);	

					if(graph.getPhenoColumn()==-1){
						((Combo) controls[0]).select(0);
					}else{
						((Combo) controls[0]).select(graph.getPhenoColumn());
					}

					for(Control c:controls){
						c.setVisible(true);
					}
				}catch(NullPointerException e){
					invalidPhenoMessage();
					phenod=false;
				}


			}	
			textPheno.setText(path);
			WaitDialog.end();
		}
		private void invalidPhenoMessage() {
			MessageBox messageBox = new MessageBox(UI.display.getActiveShell(), SWT.ICON_ERROR);			
			messageBox.setMessage("The selected phenotype file is invalid.");					
			messageBox.setText("File Import Error");
			messageBox.open();
		}
	}
	
	Combo comboPheno;
	Combo[] PCAS; 
	public void createControl(Composite parent) {	

		Composite topLevel = new Composite(parent, SWT.NONE);
		
		topLevel.setLayout(new GridLayout(4,true));
		if(graph==null){//NEW GRAPH
			//data button

			btnData = new Button(topLevel, SWT.CENTER);
			textData = new Text(topLevel, SWT.BORDER);
			btnPheno = new Button(topLevel, SWT.CENTER);
			textPheno = new Text(topLevel, SWT.BORDER);
			compositePCAs = new Composite(topLevel, SWT.BORDER);
			Label phenoLabel = new Label(topLevel,SWT.WRAP|SWT.RIGHT);
			comboPheno = new Combo(topLevel, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);

			btnData.setText("Import Data File");  
			btnData.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
			btnData.addSelectionListener(new DataListener(new Control[] {comboPheno,phenoLabel}));	    
			textData.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
			textData.setEditable(false);      
			//pheno button	    
			btnPheno.setText("Import Phenotype File");
			btnPheno.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
			btnPheno.setEnabled(false);	    
			btnPheno.addSelectionListener(new PhenoListener(new Control[] {comboPheno,phenoLabel}));	    
			textPheno.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
			textPheno.setEditable(false);    

			//This panel is used to contain our comboboxes					
			compositePCAs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,4,3));
			compositePCAs.setLayout(new GridLayout(1, false));  		
			Label label = new Label(compositePCAs,SWT.WRAP|SWT.CENTER);
			label.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
			label.setText("Please select the PCAs");
			Combo PCA1,PCA2,PCA3;
			PCA1 = new Combo(compositePCAs, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);
			PCA2 = new Combo(compositePCAs, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);
			PCA3 = new Combo(compositePCAs, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);
			PCA1.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,0));
			PCA2.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,0));
			PCA3.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,0));
			PCAS=new Combo[]{PCA1,PCA2,PCA3};
			compositePCAs.setVisible(false);

			phenoLabel.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
			phenoLabel.setText("Which column which represents the phenotype data?");
			phenoLabel.setVisible(false);

			comboPheno.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,0));
			comboPheno.setVisible(false);

			setPageComplete(false);
		}else{//EDIT PCAS AND PHENO
			//pheno button
			topLevel.setLayout(new GridLayout(4,true));
			Label phenoLabel;
			if(!graph.isRelFile()){
				btnPheno = new Button(topLevel, SWT.CENTER);
				textPheno = new Text(topLevel, SWT.BORDER);
				compositePCAs = new Composite(topLevel, SWT.BORDER);
				phenoLabel = new Label(topLevel,SWT.WRAP|SWT.CENTER);
				phenoLabel.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
				phenoLabel.setText("Which column which represents the phenotype data?");
				comboPheno = new Combo(topLevel, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);
				btnPheno.setVisible(false);
				textPheno.setVisible(false);
				rel=false;
			}else{//rel file
				compositePCAs = new Composite(topLevel, SWT.BORDER);
				phenoLabel = new Label(topLevel,SWT.WRAP|SWT.RIGHT);
				phenoLabel.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
				phenoLabel.setText("Which column which represents the phenotype data?");
				comboPheno = new Combo(topLevel, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);
				rel=true;
			}

			//This panel is used to contain our comboboxes					
			compositePCAs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,4,3));
			compositePCAs.setLayout(new GridLayout(1, false));  		
			Label label = new Label(compositePCAs,SWT.WRAP|SWT.CENTER);
			label.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
			label.setText("Please select the PCAs");
			Combo PCA1,PCA2,PCA3;
			PCA1 = new Combo(compositePCAs, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);
			PCA2 = new Combo(compositePCAs, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);
			PCA3 = new Combo(compositePCAs, SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);
			PCA1.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,0));
			PCA2.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,0));
			PCA3.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,0));
			PCAS=new Combo[]{PCA1,PCA2,PCA3};
			if(graph.z==-1){
				setComboValues(PCAS,new int[]{graph.x+1,graph.y+1});	
			}else{
				setComboValues(PCAS,new int[]{graph.x+1,graph.y+1,graph.z+1});
			}
			for(Combo c: PCAS){
				c.addModifyListener(combMod);
			}

			comboPheno.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,0));
			if((graph.getPCAData()[1].getPhenotypeData().length-2>=1)&&(!rel)){
				phenod=true;
				String[] columns = new String[graph.getPCAData()[1].getPhenotypeData().length];
				for(int i=0;i<graph.getPCAData()[1].getPhenotypeData().length;i++){
					columns[i]="Column "+String.valueOf(i+1);
				}
				comboPheno.setItems(columns);				
				if(graph.getPhenoColumn()==-1){
					comboPheno.select(0);
				}else{
					comboPheno.select(graph.getPhenoColumn());
				}
			}else{
				comboPheno.setVisible(false);
				phenoLabel.setVisible(false);
			}

			setPageComplete(true);
		}


		setControl(topLevel);


	}

	//this function ensures that the same 2 pca values aren't picked and picks default values to ensure
	//at least 2 items are picked. It will run each time a different value is selected.
	public void setComboValues(Combo[] pcas,int[] selection){	
		int noPCAs = graph.getPCAData()[0].getData().length;
		if(selection.length==2){
			setTwoComboValues(pcas,selection,noPCAs);
		}else{
			setThreeComboValues(pcas,selection,noPCAs);
		}
		
	}

	private void setThreeComboValues(Combo[] pcas, int[] selection, int noPCAs) {
		for(int x=0;x<3;x++){ // for each x where x represents a combo box.
			
			String[] indexValues = getThreeIndexValues(x,selection,noPCAs);			
			pcas[x].setItems(indexValues);
			pcas[x].select(pcas[x].indexOf("PCA "+selection[x]));

		}
	}
	
	private String[] getThreeIndexValues(int x, int[] selection,int noPCAs) {
	
		String[] indexValues=getEmptyIndexValuesArray(noPCAs, x);
		
		int count=0;
		for (int i = 0; i < noPCAs; i++){
			if(selection[(x+1)%3]!=i+1&&selection[(x+2)%3]!=i+1){ //checks that the i doesn't match the other combo boxes' selection
				indexValues[count] = "PCA "+(Integer.toString(i+1));
				count++;	
			}	
		}
		return indexValues;
	}


	private void setTwoComboValues(Combo[] pcas, int[] selection,int noPCAs) {
		setFirstTwoComboValues(pcas, selection,noPCAs);
		setLastComboValues(pcas, selection,noPCAs);
		
	}


	private void setLastComboValues(Combo[] pcas, int[] selection,int noPCAs) {
		
		String[] indexValues= getLastIndexValues(noPCAs, selection);				
		pcas[2].setItems(indexValues);
		pcas[2].select(noPCAs-2);
		
	}


	private String[] getLastIndexValues(int noPCAs, int[] selection){		
		String[] indexValues= new String[noPCAs-1];
		indexValues[noPCAs-2]="None";
		
		int count=0;
		for (int i = 0; i < noPCAs; i++){
			if(selection[0]!=i+1&&selection[1]!=i+1){
				indexValues[count] = "PCA "+(Integer.toString(i+1));
				count++;
			}
		}
		return indexValues;
	}


	private void setFirstTwoComboValues(Combo[] pcas, int[] selection,int noPCAs ) {
		for(int x=0;x<2;x++){
			String[] indexValues=getTwoIndexValues(x,selection,noPCAs);
			pcas[x].setItems(indexValues);
			if(selection[x]>selection[(x+1)%2]){
				pcas[x].select(selection[x]-2);
			}else{
				pcas[x].select(selection[x]-1);
			}
		}
		
	}


	private String[] getTwoIndexValues(int x, int[] selection,int noPCAs) {
		int count=0;
		String[] indexValues = new String[noPCAs-1];
		for(int i=0; i<noPCAs; i++){
			if(selection[(x+1)%2]!=i+1){
				indexValues[count] = "PCA "+(Integer.toString(i+1));
				count++;
			}
		}
		return indexValues;
	}


	

	private String[] getEmptyIndexValuesArray(int noPCAs,int x) {
		String[] indexValues;
		if(x==2){
			indexValues= new String[noPCAs-1];
			indexValues[noPCAs-2]="None";
		}else {
			indexValues = new String[noPCAs-2];
		}		
		return indexValues;
	}
	
	private ComboModifiedAction combMod = new ComboModifiedAction();
    //this is the listener that listens for a change in combo boxes so it can set them properly and allow for
    //the correct choices in the others.
    private class ComboModifiedAction implements ModifyListener{           
        @Override
        public void modifyText(ModifyEvent e) { //happens when something is changed in the combos           

            for(int i=0;i<3;i++){
            	PCAS[i].removeModifyListener(this);        
            }
            int[] selection;
            if(PCAS[2].getText().equals("None")){
                selection = new int[2];
                for(int i=0;i<2;i++){
                    selection[i]=Integer.parseInt(PCAS[i].getText().split("\\s+")[1]);
                }
                setComboValues(PCAS,selection);
            }else{
                selection = new int[3];
                for(int i=0;i<3;i++){
                    selection[i]=Integer.parseInt(PCAS[i].getText().split("\\s+")[1]);
                }
                setComboValues(PCAS,selection);
            }

            UI.display.timerExec(100, timer);       

        }

        Runnable timer = new Runnable() {
            public void run() {
                for(int i=0;i<3;i++){
                    PCAS[i].addModifyListener(combMod);
                }

            }
        };

    }

	

}