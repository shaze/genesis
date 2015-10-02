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

package main;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import admix.*;
import pca.*;

import shared.ExportListener;
import shared.LoadListener;
import shared.SaveListener;


/**This class contains the code for creating all the UI components as well as 
 * all the listeners for the widgets. It also contains the lists of 
 * {@link AdmixProj} and {@link PCAProj} which contain all the data for any
 * graphs
 */
public class UI {	
	/**
	 * The main Display
	 */
	public static Display display = new Display();		
	/**
	 * The shell of the main screen
	 */
	public static Shell mainWindow;

	public static TabFolder tabs;
	private static Composite panelToolbar, panelRotate;
	/**
	 * The array of all {@link PCAProj}
	 */
	public static ArrayList<PCAProj> pcaProjects = new ArrayList<PCAProj>();	
	/**
	 * The array of all {@link AdmixProj}
	 */
	public static ArrayList<AdmixProj> admixProjects = new ArrayList<AdmixProj>();
	 static ArrayList<ToolItem> toBeEnabled=new ArrayList<ToolItem>(),
			toBeEnabledIfAdmix=new ArrayList<ToolItem>(),
			toBeEnabledIfPCA=new ArrayList<ToolItem>();
	public static ArrayList<ToolItem> toBeEnabledIf3D=new ArrayList<ToolItem>();
			//toBeEnabledIfHidden=new ArrayList<ToolItem>();
	public static UI ui;

	public UI(){
		super();		
		ui=this;	
	}

	/**
	 * This method creates the main shell
	 */
	public void open(){
		
		createMainWindow();
		checkResolution();
		createControls();		
		
		mainWindow.open();
		setMainWindowSize();
		
		placeElements();

		mainWindow.addControlListener(new ResizeListener());
		
		while (!mainWindow.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private void setMainWindowSize() {
		int dispWidth=display.getBounds().width;
		int dispHeight=display.getBounds().height;
		if(dispWidth<=1600){
			mainWindow.setMaximized(true);			
		}else{
			mainWindow.setBounds((dispWidth-1600)/2, (dispHeight-900)/2, 1600, 900);
		}
	}

	/**
	 * This method checks if the current resolution is low and will warn
	 * the user if it seems too low.
	 */
	private void checkResolution() {
		if(display.getBounds().width<1025){
			createResolutionWarningBox();
		}
	}
	
	/**
	 * This method will warn the user if the resolution is low.
	 */
	private void createResolutionWarningBox(){
		MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_WARNING);
		messageBox.setMessage("Genesis has detected that your resolution is set low. \n"
				+ "For best results please set your resolution higher.");
		messageBox.setText("Resolution");
		messageBox.open();	
	}

	/**
	 * This method creates the shell
	 */
	private void createMainWindow() {
		mainWindow= new Shell(display);
		mainWindow.setText("Genesis");
		mainWindow.setMinimumSize(new Point(640,480));	
		mainWindow.addShellListener(new MainWindowListener());
	}

	/**
	 * This method creates the components on the shell
	 */
	private void createControls() {
		createMenu();
		createToolbar();
		createRotatePanel();
		createTabs();
	}
	
	/**
	 * This method creates the menu on the shell
	 */
	private void createMenu() {
		Menu menuBar = new Menu(mainWindow, SWT.BAR);
		
		createFileMenu(menuBar);
		createEditMenu(menuBar);
		createHelpMenu(menuBar);
		
		mainWindow.setMenuBar(menuBar);
	}

	private void createHelpMenu(Menu menuBar) {
		MenuItem cascadeHelpMenu = new MenuItem(menuBar, SWT.CASCADE);
		
		cascadeHelpMenu.setText("&Help");
		Menu helpMenu = new Menu(mainWindow, SWT.DROP_DOWN);
		cascadeHelpMenu.setMenu(helpMenu);
		
		MenuItem help = new MenuItem(helpMenu, SWT.PUSH);
		help.setText("&Help");
		help.addSelectionListener(new HelpListener());
		
		MenuItem about = new MenuItem(helpMenu, SWT.PUSH);
		about.setText("&About");
		about.addSelectionListener(new AboutListener());
		
	}

	private void createEditMenu(Menu menuBar) {
		MenuItem cascadeEditMenu = new MenuItem(menuBar, SWT.CASCADE);
		
		cascadeEditMenu.setText("&Graph");
		Menu fileMenu = new Menu(mainWindow, SWT.DROP_DOWN);
		cascadeEditMenu.setMenu(fileMenu);
		
		MenuItem dataOptions = new MenuItem(fileMenu, SWT.PUSH);
		dataOptions.setText("&Data Options");
		dataOptions.addSelectionListener(new DataOptionsListener());
		
		MenuItem appearanceOptions = new MenuItem(fileMenu, SWT.PUSH);
		appearanceOptions.setText("&Appearance Options");
		appearanceOptions.addSelectionListener(new AppearanceOptionsListener());
		
	}

	private void createFileMenu(Menu menuBar) {
		MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);

		cascadeFileMenu.setText("&File");
		Menu fileMenu = new Menu(mainWindow, SWT.DROP_DOWN);
		cascadeFileMenu.setMenu(fileMenu);
     
		MenuItem newAdmix = new MenuItem(fileMenu, SWT.PUSH);
		newAdmix.setText("New &Admixture Graph");
		newAdmix.addSelectionListener(new NewAdmixListener());

		MenuItem newPca = new MenuItem(fileMenu, SWT.PUSH);
		newPca.setText("New &PCA plot");
		newPca.addSelectionListener(new NewPCAListener());

		MenuItem saveMI = new MenuItem(fileMenu, SWT.PUSH);
		saveMI.setText("&Save Project");
		saveMI.addSelectionListener(new SaveListener());

		MenuItem loadMI = new MenuItem(fileMenu, SWT.PUSH);
		loadMI.setText("&Load Project");
		loadMI.addSelectionListener(new LoadListener());
		
		MenuItem exportMI = new MenuItem(fileMenu, SWT.PUSH);
		exportMI.setText("&Export Image");
		exportMI.addSelectionListener(new ExportListener());
	
		new MenuItem(fileMenu, SWT.SEPARATOR);
		MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setText("&Exit");
		exitItem.addSelectionListener(new ClickCloseListener());		
	}

	/**
	 * This method creates the toolbar on the shell
	 */
	private void createToolbar() {       
		panelToolbar = new Composite(mainWindow, SWT.FLAT);
		panelToolbar.setBounds(0,0,display.getClientArea().width,45);  

		Image image = SWTImageLoader.loadImage("newAdmix.png");		
		Image imAd = new Image(display,image.getImageData());
		image = SWTImageLoader.loadImage("newPCA.png");		
		Image imPCA = new Image(display,image.getImageData());
		image = SWTImageLoader.loadImage("options.png");		
		Image imOptions = new Image(display,image.getImageData());
		image = SWTImageLoader.loadImage("refresh.png");		
		Image imRefresh = new Image(display,image.getImageData());
		image = SWTImageLoader.loadImage("pcas.png");		
		Image imPCAS = new Image(display,image.getImageData());
		image = SWTImageLoader.loadImage("rotate.png");		
		Image imRotate = new Image(display,image.getImageData());
		image = SWTImageLoader.loadImage("load.png");		
		Image imLoad = new Image(display,image.getImageData());
		image = SWTImageLoader.loadImage("save.png");		
		Image imSave = new Image(display,image.getImageData());
		image = SWTImageLoader.loadImage("search.png");		
		Image imSearch = new Image(display,image.getImageData());
		image = SWTImageLoader.loadImage("hidden.png");		
		Image imHidden = new Image(display,image.getImageData());
		image = SWTImageLoader.loadImage("close.png");		
		Image imClose = new Image(display,image.getImageData());
		image = SWTImageLoader.loadImage("export.png");		
		Image imExport = new Image(display,image.getImageData());
		image = SWTImageLoader.loadImage("line.png");		
		Image imLine= new Image(display,image.getImageData());
		image = SWTImageLoader.loadImage("arrow.png");		
		Image imArrow = new Image(display,image.getImageData());

		ToolBar toolBar = new ToolBar(panelToolbar, SWT.WRAP|SWT.FLAT);        
		ToolItem itemAd = new ToolItem(toolBar, SWT.PUSH);
		itemAd.setImage(imAd);
		itemAd.setToolTipText("New Admixture");
		itemAd.addSelectionListener(new NewAdmixListener());

		ToolItem itemPCA = new ToolItem(toolBar, SWT.PUSH);
		itemPCA.setImage(imPCA);
		itemPCA.setToolTipText("New PCA");
		itemPCA.addSelectionListener(new NewPCAListener());

		new ToolItem(toolBar, SWT.SEPARATOR);   

		ToolItem itemSave= new ToolItem(toolBar, SWT.PUSH);
		itemSave.setImage(imSave);
		itemSave.setToolTipText("Save Project");
		itemSave.addSelectionListener(new SaveListener());
		itemSave.setEnabled(false);
		toBeEnabled.add(itemSave);

		ToolItem itemLoad = new ToolItem(toolBar, SWT.PUSH);
		itemLoad.setImage(imLoad);
		itemLoad.setToolTipText("Load Project");
		itemLoad.addSelectionListener(new LoadListener());                

		new ToolItem(toolBar, SWT.SEPARATOR);   

		ToolItem itemPCAS = new ToolItem(toolBar, SWT.PUSH);
		itemPCAS.setImage(imPCAS);
		itemPCAS.addSelectionListener(new DataOptionsListener());
		itemPCAS.setToolTipText("Data Options");
		itemPCAS.setEnabled(false);
		toBeEnabled.add(itemPCAS);

		ToolItem itemOptions = new ToolItem(toolBar, SWT.PUSH);
		itemOptions.setImage(imOptions);
		itemOptions.addSelectionListener(new AppearanceOptionsListener());
		itemOptions.setToolTipText("Appearance Options");
		itemOptions.setEnabled(false);
		toBeEnabled.add(itemOptions);

		ToolItem itemRefresh = new ToolItem(toolBar, SWT.PUSH);
		itemRefresh.setImage(imRefresh);
		itemRefresh.addSelectionListener(new RefreshListener());
		itemRefresh.setToolTipText("Refresh");
		itemRefresh.setEnabled(false);
		toBeEnabled.add(itemRefresh);

		ToolItem itemRotate = new ToolItem(toolBar, SWT.PUSH);
		itemRotate.setImage(imRotate);
		itemRotate.addSelectionListener(new RotateListener());
		itemRotate.setToolTipText("Show/Hide 3D PCA Rotate Panel");
		itemRotate.setEnabled(false);
		toBeEnabledIf3D.add(itemRotate);

		ToolItem itemSearch = new ToolItem(toolBar, SWT.PUSH);
		itemSearch.setImage(imSearch);
		itemSearch.addSelectionListener(new SearchListener());
		itemSearch.setToolTipText("Search for individual");
		itemSearch.setEnabled(false);
		toBeEnabled.add(itemSearch);

		ToolItem itemHidden = new ToolItem(toolBar, SWT.PUSH);
		itemHidden.setImage(imHidden);
		itemHidden.addSelectionListener(new HiddenListener());
		itemHidden.setToolTipText("Select Hidden Individuals or Groups");
		itemHidden.setEnabled(false);
		toBeEnabled.add(itemHidden);
		
		new ToolItem(toolBar, SWT.SEPARATOR); 
		
		ToolItem itemLine = new ToolItem(toolBar, SWT.PUSH);
		itemLine.setImage(imLine);
		itemLine.addSelectionListener(new LineListener());
		itemLine.setToolTipText("Draw Line");
		itemLine.setEnabled(false);
		toBeEnabled.add(itemLine);
		
		ToolItem itemArrow = new ToolItem(toolBar, SWT.PUSH);
		itemArrow.setImage(imArrow);
		itemArrow.addSelectionListener(new ArrowListener());
		itemArrow.setToolTipText("Draw Arrow");
		itemArrow.setEnabled(false);
		toBeEnabled.add(itemArrow);

		new ToolItem(toolBar, SWT.SEPARATOR); 

		ToolItem itemExport = new ToolItem(toolBar, SWT.PUSH);
		itemExport.setImage(imExport);
		itemExport.setToolTipText("Export");
		itemExport.addSelectionListener(new ExportListener());
		itemExport.setEnabled(false);
		toBeEnabled.add(itemExport);

		ToolItem itemClose = new ToolItem(toolBar, SWT.PUSH);
		itemClose.setImage(imClose);
		itemClose.setToolTipText("Close Project");
		itemClose.addSelectionListener(new CloseListener());
		itemClose.setEnabled(false);
		toBeEnabled.add(itemClose);

		/*ToolItem itemTest = new ToolItem(toolBar, SWT.PUSH);        
		itemTest.setToolTipText("Test Button");
		itemTest.addSelectionListener(new TestListener());*/

		toolBar.pack();
	}
		
	/**
	 * This method creates the panel that holds the Rotate slider
	 */
	private void createRotatePanel() {	
		panelRotate = new Composite(panelToolbar, SWT.BORDER);
		GridData compositeImportData = new GridData(SWT.FILL, SWT.FILL, true, false);
		panelRotate.setLayoutData(compositeImportData);        
		panelRotate.setVisible(false); 

		Label labelRotate = new Label(panelRotate,SWT.CENTER|SWT.SINGLE);
		labelRotate.setFont(new Font(display,"Arial", 11, SWT.NONE ));
		GC gc = new GC(display);
		int height = gc.textExtent("Rotate : ").y;
		labelRotate.setBounds(0,(45-height)/2-5,80,45);
		labelRotate.setText("Rotate : ");
		gc.dispose();

		Slider sliderRotate=new Slider(panelRotate,SWT.CENTER|SWT.BORDER);
		sliderRotate.setBounds(80,0,300,45);
		sliderRotate.setLayoutData( new FillLayout());
		sliderRotate.setMaximum(58);
		sliderRotate.setMinimum(2);
		sliderRotate.setIncrement(1);
		sliderRotate.setThumb(1);		
		sliderRotate.setSelection(32);
		sliderRotate.setVisible(true);
		sliderRotate.addSelectionListener(new RotateSliderListener(sliderRotate));
	}

	/**
	 * This method creates the TabFolder on the shell
	 */
	private void createTabs() {
		tabs = new TabFolder(mainWindow, SWT.BORDER);          		
		tabs.addSelectionListener(new TabsListener());
	}
	
	/**
	 * This method sets the TabFolders size to fill the window
	 * and places the rotate panel in position
	 */
	private void placeElements(){
		tabs.setBounds(0,panelToolbar.getBounds().height+1,
				mainWindow.getClientArea().width, 
				mainWindow.getClientArea().height-panelToolbar.getBounds().height);
		placeRotatePanel();
		
	}

	private void placeRotatePanel() {
		ToolBar toolbar = (ToolBar) panelToolbar.getChildren()[0];
		ToolItem lastButton = toolbar.getItems()[toolbar.getItems().length-1];
		int panelX = lastButton.getBounds().x+lastButton.getBounds().width+10;
		panelRotate.setBounds(panelX,0,380,45); //(panel is 380x45 pixels)
		
	}

	/**
	 * This method will create a new tab with the given Title that can then be placed on the
	 * TabFolder
	 * @param title
	 * @return the TabItem
	 */
	public TabItem newTab(String title){	
		TabItem tabItem = new TabItem(tabs, SWT.CLOSE);
		tabItem.setText(title);

		final ScrolledComposite composite = new ScrolledComposite(tabs, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));       
		tabItem.setControl(composite);
		tabs.setSelection(tabItem);

		return tabItem;
	}

	/**
	 * This method will call the relevant methods in the creation of a new PCA project
	 * @return The PCA project
	 */
	private PCAProj newPCA(){
		PCAProj proj = PCAWorkflow.newPCA();

		return proj;
	}


	/**
	 * This method can be called publically and will cause the UI to repaint the graph that
	 * is on the current tab
	 */
	public void drawGraph(){
		if(tabs.getItemCount()==0){
			return;
		}
		TabItem currentTab = tabs.getSelection()[0];
		for(PCAProj proj:pcaProjects){
			if(currentTab.equals(proj.getTab())){
				PCAWorkflow.drawPCA(proj);
				return;
			}
		}
		for(AdmixProj proj : admixProjects){
			if(currentTab.equals(proj.getTab())){
				proj.draw();
				return;
			}
		}

	}	

	/**
	 * This method will check to see if a PCA project is open on the current tab, and if so,
	 * will redraw the PCA graph
	 */
	public void drawPCA(){
		if(tabs.getItemCount()==0){
			return;
		}
		TabItem currentTab = tabs.getSelection()[0];
		for(PCAProj proj:pcaProjects){
			if(currentTab.equals(proj.getTab())){
				PCAWorkflow.drawPCA(proj);
				return;
			}
		}		

	}	


	/**
	 * This method will enable all the buttons that both a PCA project and an Admixture project
	 * might need
	 */
	public void enableButtons(){
		for(ToolItem i: toBeEnabled){
			i.setEnabled(true);
		}
	}

	/**
	 * This method will enable all the buttons that a PCA project might need
	 */
	public void enablePCAControls(){
		for(ToolItem t:toBeEnabled){
			t.setEnabled(true);
		}
		for(ToolItem t:toBeEnabledIfPCA){
			t.setEnabled(true);
		}
		for(ToolItem t:toBeEnabledIfAdmix){
			t.setEnabled(false);
		}
	}
	/**
	 * This method will enable all the buttons that an Admixture project might need
	 */
	public void enableAdmixControls(){
		for(ToolItem t:toBeEnabled){
			t.setEnabled(true);
		}
		for(ToolItem t:toBeEnabledIfPCA){
			t.setEnabled(false);
		}
		for(ToolItem t:toBeEnabledIf3D){
			t.setEnabled(false);
		}
		for(ToolItem t:toBeEnabledIfAdmix){
			t.setEnabled(true);
		}
		panelRotate.setVisible(false);
	}

	/**
	 * This method will find the {@link PCAProj} that belongs on the given tab (if there is one)
	 * @param tab the tab to try match
	 * @return {@link PCAProj} that belongs on the given tab, will return null if there is none
	 */
	public PCAProj findPCAProjByTab(TabItem tab){
		for(PCAProj p:pcaProjects){
			if(p.getTab().equals(tab)){
				return p;
			}
		}
		return null;		
	}

	/**
	 * This method will find the {@link AdmixProj} that belongs on the given tab (if there is one)
	 * @param tab the tab to try match
	 * @return {@link AdmixProj} that belongs on the given tab, will return null if there is none
	 */
	public AdmixProj findAdmixProjByTab(TabItem tab){
		for(AdmixProj p:admixProjects){
			if(p.getTab().equals(tab)){
				return p;
			}
		}
		return null;		
	}

	/**
	 * This listener listens for when the tab is changed and and will enable
	 * the relevant controls on the UI for the project on the tab
	 */
	private class TabsListener extends SelectionAdapter{		

		public void widgetSelected(SelectionEvent event){
			PCAProj pcaProj = findPCAProjByTab(tabs.getSelection()[0]);
			if(pcaProj!=null){
				enablePCAControls();
				if(pcaProj.getGraph().z==-1){
					for(ToolItem t : toBeEnabledIf3D){
						t.setEnabled(false);
						panelRotate.setVisible(false);
					}
				}else{
					for(ToolItem t : toBeEnabledIf3D){
						t.setEnabled(true);
					}
				}
			}else{
				AdmixProj proj=findAdmixProjByTab(tabs.getSelection()[0]);
				if(proj!=null){
					enableAdmixControls();
				}
			}

		}
	}

	/**
	 * This listener listens for changes in the rotation slider for 3D PCAs and will 
	 * perform the rotation on the current PCA and redraw the graph 
	 */
	private class RotateSliderListener extends SelectionAdapter{
		Slider slider;
		RotateSliderListener(Slider slider){
			this.slider=slider;
		}

		public void widgetSelected(SelectionEvent event) {		
			PCAProj proj = findPCAProjByTab(tabs.getSelection()[0]);
			proj.getGraph().setRotation(slider.getSelection()-29);
			drawGraph();
		}
	}



	

	/**
	 * This method will remove the given {@link AdmixProj} from the {@link AdmixProj} array
	 * @param proj The {@link AdmixProj} to remove
	 */
	private void deleteAdmix(AdmixProj proj) {	
		MessageBox messageBox = createProjCloseConfirmationMessageBox();		
		if(messageBox.open()==SWT.YES){
			admixProjects.remove(proj);
			tabs.getSelection()[0].dispose();	
		}

	}

	/**
	 * This method will remove the given {@link PCAProj} from the {@link PCAProj} array
	 * @param proj The {@link PCAProj} to remove
	 */
	private void deletePCA(PCAProj proj) {
		MessageBox messageBox = createProjCloseConfirmationMessageBox();		
		if(messageBox.open()==SWT.YES){
			pcaProjects.remove(proj);
			tabs.getSelection()[0].dispose();	
		}

	}

	/**
	 * This method creates a confirmation box when a project is closed
	 * @return the MessageBox
	 */
	private MessageBox createProjCloseConfirmationMessageBox() {
		int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
		MessageBox messageBox = new MessageBox(mainWindow, style);
		messageBox.setMessage("Are you sure you would like to close the current project? "
				+ "Any unsaved changes will be lost.");
		messageBox.setText("Close?");
		return messageBox;
	}	

	/**
	 * This listener listens if the Close Project button is clicked.
	 *
	 */
	private class CloseListener implements SelectionListener {
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {	
			try{
				PCAProj proj = findPCAProjByTab(tabs.getSelection()[0]);
				if(proj!=null){
					deletePCA(proj);
				}else{
					AdmixProj admixProj = findAdmixProjByTab(tabs.getSelection()[0]);
					if(admixProj!=null){
						deleteAdmix(admixProj);					
					}
				}
				if(tabs.getSelectionIndex()==-1){
					deactivateControls();
				}else{
					activateControls();
				}

			}catch(ArrayIndexOutOfBoundsException e){
				//this happens when there is no tab open
			}

		}

		/**
		 * This method will activate the relevant contols depending on what project 
		 * is currently open 
		 */
		private void activateControls() {
			PCAProj proj = findPCAProjByTab(tabs.getSelection()[0]);
			if(proj!=null){
				activatePCA(proj);
			}else{
				AdmixProj admixProj = findAdmixProjByTab(tabs.getSelection()[0]);
				if(admixProj!=null){
					enableAdmixControls();					
				}
			}

		}	
		
		/**
		 * This method activates the controls that might be needed by a PCA 
		 * graph as well as checking if the rotate button needs to be enabled
		 */
		private void activatePCA(PCAProj proj) {
			enablePCAControls();
			if(proj.getGraph().z!=-1){
				for(ToolItem t : toBeEnabledIf3D){
					t.setEnabled(true);
				}
			}
		}

		/**
		 * This method deactivates all the buttons (except new/load) when there are no
		 * active projects
		 */
		private void deactivateControls() {		
			for(ToolItem t : toBeEnabled){
				t.setEnabled(false);
			}
			for(ToolItem t : toBeEnabledIf3D){
				t.setEnabled(false);
			}
			for(ToolItem t : toBeEnabledIfAdmix){
				t.setEnabled(false);
			}
			for(ToolItem t : toBeEnabledIfPCA){
				t.setEnabled(false);
			}
			panelRotate.setVisible(false);

		}


	}

	/**
	 * This method opens a dialog saying "File Exists. Would you like to overwrite?"
	 * with a Yes and No button
	 * @return The answer from the user
	 */
	public boolean mustOverWrite() {
		int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.CENTER;
		MessageBox messageBox = new MessageBox(mainWindow, style);
		messageBox.setMessage("File Exists. Would you like to overwrite?");
		messageBox.setText("Overwrite?");
		if(messageBox.open() == SWT.YES){
			return true;
		}else{
			return false;
		}	
	}

	/**
	 * This method checks if a file at the given path exists
	 * @param path the location to check
	 * @return whether or not file with the given path exists
	 */
	public boolean fileExists(String path) {
		File f = new File(path);
		if(f.exists()) {
			return true;
		}
		return false;
	}
	
	/**
	 * This listener listens for when the New PCA button is clicked and
	 * will create the PCA project by opening the wizard and enable the 
	 * correct controls for the UI. 
	 */
	private class NewPCAListener implements SelectionListener{
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			PCAProj proj = newPCA();
			if(proj==null){
				return;
			}		

			if(proj.getGraph().z==-1){
				panelRotate.setVisible(false);
				for(ToolItem t : toBeEnabledIf3D){
					t.setEnabled(false);
				}
			}else{
				for(ToolItem t : toBeEnabledIf3D){
					t.setEnabled(true);
				}
			}

			proj.getGraph().setName(PCAWorkflow.getDefaultProjName());

			enableButtons();
			TabItem newTab=newTab(proj.getGraph().getName());
			proj.setTab(newTab);
			ScrolledComposite comp = (ScrolledComposite)newTab.getControl();
			comp.addMouseListener(new PCAMouseListener(proj));

			pcaProjects.add(proj);
			drawGraph();
			enablePCAControls();

		}
	}

	/**
	 * This listener listens for when the New Admix button is clicked and
	 * will create the Admix project by opening the wizard and enabling the 
	 * correct controls for the UI. 
	 */
	private class NewAdmixListener implements SelectionListener{
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			AdmixProj proj = AdmixWorkflow.newAdmix();
			if(proj==null){
				return;
			}								

			enableButtons();
			TabItem newTab=newTab(proj.getName());
			proj.setTab(newTab);
			admixProjects.add(proj);
			drawGraph();
			enableAdmixControls();
		}	

	}


	/**
	 * This listener listens for when the Appearance Options button is clicked and
	 * will create the menu with those options.
	 */
	private class AppearanceOptionsListener implements SelectionListener{

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {		
			try{
				PCAProj proj = findPCAProjByTab(tabs.getSelection()[0]);	
				if(proj!=null){
					PCAWorkflow.OptionsWizard(proj);
				}else{
					AdmixProj temp = findAdmixProjByTab(tabs.getSelection()[0]);
					if(temp==null){
						return;
					}
					AdmixWorkflow.OptionsWizard(temp);
				}
			}catch(ArrayIndexOutOfBoundsException e){
				noProjectMessageBox();
			}
		}	
	}	
	
	
	/**
	 * This listener listens for when the About button is clicked and
	 * will create the about screen. 
	 */
	private class AboutListener implements SelectionListener{
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {		
			new About(display);
		}
	
	}
	
	/**
	 * This listener listens for when the Help button is clicked and
	 * will create the HTML help menu. 
	 */
	private class HelpListener implements SelectionListener{
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {		
			new HelpMenu(display);
		}
	
	}
	/**
	 * This listener listens for when the Data Options button is clicked and
	 * will create the menu with those options.
	 */
	private class DataOptionsListener implements SelectionListener{
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {		
			try{
				PCAProj proj = findPCAProjByTab(tabs.getSelection()[0]);
				if(proj!=null){
					pcaOptions(proj);				
					return;	
				}

				AdmixProj admixProj = findAdmixProjByTab(tabs.getSelection()[0]);
				if(admixProj!=null){
					AdmixWorkflow.dataOptions(admixProj);
					
					return;	
				}
			}catch(ArrayIndexOutOfBoundsException e){
				noProjectMessageBox();
			}
			
		}

		/**
		 * Opens the Data Options menu for the given {@link PCAProj} and
		 * sets all the changes when the menu is closed
		 * @param proj the given {@link PCAProj}
		 */
		private void pcaOptions(PCAProj proj) {
			PCAWizard wiz = new PCAWizard('i',proj.getGraph());
			WizardDialog dialog = new WizardDialog(UI.display.getActiveShell(),wiz);
			dialog.open();
			if(wiz.finished){		
				if(wiz.phenoColumn!=proj.getGraph().getPhenoColumn()){
					proj.getGraph().setPhenoColumn(wiz.phenoColumn);	
					PCAWorkflow.createPopGroups(proj.getGraph());
				}				
				drawGraph();	
				if(proj.getGraph().z==-1){
					panelRotate.setVisible(false);
					for(ToolItem t : toBeEnabledIf3D){
						t.setEnabled(false);
					}
				}else{
					for(ToolItem t : toBeEnabledIf3D){
						t.setEnabled(true);
					}
				}
			}
			
		}
	}
	
	/**
	 * This listener listens for when the Refresh button is clicked and
	 * will repaint the current graph
	 */
	private class RefreshListener implements SelectionListener{
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			drawGraph();
		}
	}

	/**
	 * This listener listens for when the Rotate button is clicked and
	 * will hide/show the Rotate Panel
	 */
	private class RotateListener implements SelectionListener{		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			panelRotate.setVisible(!panelRotate.getVisible());
		}
	}

	/**
	 * This listener listens for when the Search button is clicked
	 */
	private class SearchListener implements SelectionListener{		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			PCAProj pcaProj = findPCAProjByTab(tabs.getSelection()[0]);
			if(pcaProj!=null){
				PCAWorkflow.pcaSearch(pcaProj);
				return;
			}

			AdmixProj proj = findAdmixProjByTab(tabs.getSelection()[0]);
			if(proj!=null){
				AdmixWorkflow.admixSearch(proj);
				return;
			}


		}

	}
	
	/**
	 * This listener listens for when the Select Hidden Subjects button is clicked
	 */
	private class LineListener implements SelectionListener{	

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			PCAProj pcaProj=findPCAProjByTab(tabs.getSelection()[0]);
			if(pcaProj!=null){
				PCAWorkflow.drawLine(pcaProj);
			}else{
				AdmixProj admixProj=findAdmixProjByTab(tabs.getSelection()[0]);
				if(admixProj!=null){
					AdmixWorkflow.drawLine(admixProj);
					
				}
			}

		}
	}
	
	/**
	 * This listener listens for when the Select Hidden Subjects button is clicked
	 */
	private class ArrowListener implements SelectionListener{	

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			PCAProj pcaProj=findPCAProjByTab(tabs.getSelection()[0]);
			if(pcaProj!=null){
				PCAWorkflow.drawArrow(pcaProj);
			}else{
				AdmixProj admixProj=findAdmixProjByTab(tabs.getSelection()[0]);
				if(admixProj!=null){
					AdmixWorkflow.drawArrow(admixProj);
				}
			}

		}
	}
	
	/**
	 * This listener listens for when the Select Hidden Subjects button is clicked
	 */
	private class HiddenListener implements SelectionListener{	

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			PCAProj pcaProj=findPCAProjByTab(tabs.getSelection()[0]);
			if(pcaProj!=null){
				hiddenPCADialog(pcaProj);
			}else{
				AdmixProj admixProj=findAdmixProjByTab(tabs.getSelection()[0]);
				if(admixProj!=null){
					hiddenAdmixDialog(admixProj);
				}
			}

		}
		
		
		
		
		
		/**
		 * This method creates a dialog with a combobox allowing the user to 
		 * select a hidden individual from an AdmixGraph
		 * @param proj the current admix project
		 */
		private void hiddenAdmixDialog(AdmixProj proj) {
			ArrayList<AdmixSubject> subjs = new ArrayList<AdmixSubject>();
			AdmixGraph graph = proj.getGraphs().get(0);
			for(AdmixSubject s : graph.getAdmixData()){
				if(!s.getVisible()){
					subjs.add(s);
				}
			}

			if(subjs.size()==0&&!anyGroupsInvisible(proj)){
				createNoHiddenSubjectsDialog();				
			}else{
				Combo combo;
				Shell newShell = new Shell(mainWindow,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);

				newShell.setLayout(new GridLayout(2, true));

				Rectangle bds = newShell.getDisplay().getBounds();
				newShell.setSize(220,50);
				Point p = newShell.getSize();

				newShell.setBounds((bds.width - p.x) / 2, (bds.height - p.y)/2, p.x, p.y);
				Label lbl = new Label(newShell,SWT.WRAP);
				lbl.setText("Select Individual");
				lbl.setBounds(new Rectangle(10,10,100,25));

				combo = new Combo(newShell,SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);
				for(AdmixSubject s : subjs){
					combo.add(s.getName());
				}
				combo.addSelectionListener(new HiddenAdmixComboListener(newShell,proj,subjs,combo));
				
				addInvisibleGroups(proj, combo);

				newShell.open();
				newShell.pack();

			}

		}

		private boolean anyGroupsInvisible(AdmixProj proj) {
			for(AdmixPopulationGroup gp : proj.getGroups()[proj.getPhenoColumn()]){
				if(!gp.getVisible()){
					return true;
				}
			}
			return false;
		}

		private void addInvisibleGroups(AdmixProj proj, Combo combo) {
			for(AdmixPopulationGroup gp : proj.getGroups()[proj.getPhenoColumn()]){
				if(!gp.getVisible()){
					combo.add("Group: " + gp.getName());
				}
			}
			
		}

		/**
		 * This method creates a dialog with a combobox allowing the user to 
		 * select a hidden individual from an AdmixGraph
		 * @param proj the current PCA project
		 */
		private void  hiddenPCADialog(PCAProj proj) {
			ArrayList<PCASubject> subjs;
			subjs = new ArrayList<PCASubject>();
			PCAGraph graph = proj.getGraph();
			for(PCASubject s : graph.getPCAData()){
				if(!s.getVisible()){
					subjs.add(s);
				}
			}

			if(subjs.size()==0&&!anyGroupsInvisible(proj)){
				createNoHiddenSubjectsDialog();

			}else{
				Combo combo;
				Shell newShell = new Shell(mainWindow,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);
				newShell.setText("Select Hidden Individual");
				newShell.setLayout(new GridLayout(2, true));

				Rectangle bds = newShell.getDisplay().getBounds();
				newShell.setSize(220,50);
				Point p = newShell.getSize();

				newShell.setBounds((bds.width - p.x) / 2, (bds.height - p.y)/2, p.x, p.y);
				Label lbl = new Label(newShell,SWT.WRAP);
				lbl.setText("Select Individual");
				lbl.setBounds(new Rectangle(10,10,100,25));

				combo = new Combo(newShell,SWT.LEFT|SWT.DROP_DOWN | SWT.READ_ONLY);
				for(PCASubject s : subjs){
					combo.add(s.getName());
				}
				combo.addSelectionListener(new HiddenPCAComboListener(newShell,proj,subjs,combo));

				addInvisibleGroups(proj, combo);
				
				newShell.open();
				newShell.pack();

			}

		}
		
		private void addInvisibleGroups(PCAProj proj, Combo combo) {
			for(PCAPopulationGroup gp : proj.getGraph().getGroups()){
				if(!gp.getVisible()){
					combo.add("Group: " + gp.getName());
				}
			}
			
		}

		private boolean anyGroupsInvisible(PCAProj proj) {
			for(PCAPopulationGroup gp : proj.getGraph().getGroups()){
				if(!gp.getVisible()){
					return true;
				}
			}
			return false;
		}

		/**
		 * This method creates a dialog for when there are no hidden subjects 
		 * but the "Select hidden subjects" button is clicked
		 */
		private void createNoHiddenSubjectsDialog() {
			final Shell newShell = new Shell(mainWindow,SWT.APPLICATION_MODAL|SWT.DRAG|SWT.TITLE);
			newShell.setText("Select Hidden Individual");
			newShell.setLayout(new GridLayout(1, false));
			
			Rectangle bds = newShell.getDisplay().getBounds();
			newShell.setSize(220,100);
			Point p = newShell.getSize();

			newShell.setBounds((bds.width - p.x) / 2, (bds.height - p.y)/2, p.x, p.y);
			Label lbl = new Label(newShell,SWT.WRAP|SWT.CENTER);
			lbl.setText("No Hidden Subjects");
			lbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
						
			Button btn = new Button(newShell, SWT.BORDER);
			btn.setText("OK");
			btn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			btn.addSelectionListener(new SelectionListener() {				
				@Override
				public void widgetSelected(SelectionEvent arg0) {				
					newShell.close();
				}				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {}
			});

			newShell.open();
			newShell.pack();			
		}		
		
		

		/**		 
		 *This method listens for when an item is selected from the Select Hidden Subjects 
		 *on a PCA graph
		 */
		private class HiddenPCAComboListener implements SelectionListener{
			Shell newShell;
			PCAGraph graph;
			pca.drawInfo.DrawInfo drawInfo;
			pca.drawInfo.DrawInfo3D drawInfo3D;	
			ArrayList<PCASubject> subjs;
			Combo combo;

			public HiddenPCAComboListener(Shell newShell, PCAProj proj,ArrayList<PCASubject> subjs, Combo combo) {
				this.newShell=newShell;
				this.graph=proj.getGraph();
				drawInfo3D=proj.getDrawInfo3D();
				drawInfo=proj.getDrawInfo();
				this.subjs=subjs;
				this.combo=combo;
			}



			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try{
					Subject s = findSubjectByName(subjs.get(combo.getSelectionIndex()).getName());
					newShell.dispose();
					if(s!=null){
						PCAWorkflow.PCASubjectDialog(s,new Point(200,200),graph);
						drawGraph();	
					}
				}catch(IndexOutOfBoundsException e){
					String str = combo.getText().split(": ")[1].trim();	
					PCAPopulationGroup group = PCAWorkflow.findPopGroup(str, graph);
					newShell.dispose();
					PCAWorkflow.groupDialog(group, graph);
					drawGraph();						
				}


			}	

			private Subject findSubjectByName(String name){				
				if(graph.z==-1){
					for(Subject s:drawInfo.getPoints()){
						if(s.getSubj().getName().equals(name)){
							return s;					
						}
					}			
				}else{
					for(Subject s:drawInfo3D.getPoints()){
						if(s.getSubj().getName().equals(name)){
							return s;					
						}
					}			
				}
				return null;
			}

		}	
		
		/**		 
		 *This method listens for when an item is selected from the Select Hidden Subjects 
		 *on an Admix graph
		 */
		private class HiddenAdmixComboListener implements SelectionListener{
			Shell newShell;
			AdmixGraph graph;
			admix.drawinfo.DrawInfo drawInfo;

			ArrayList<AdmixSubject>  subjs;
			Combo combo;

			public HiddenAdmixComboListener(Shell newShell, AdmixProj proj,ArrayList<AdmixSubject> subjs, Combo combo) {
				this.newShell=newShell;
				this.graph=proj.getGraphs().get(0);
				this.drawInfo=proj.getDrawInfo().get(0);
				this.subjs=subjs;
				this.combo=combo;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try{
					ASubject s = findSubjectByName(subjs.get(combo.getSelectionIndex()).getName());
					newShell.dispose();
					if(s!=null){
						AdmixWorkflow.SubjectDialog(s,graph);
						drawGraph();	
					}
				}catch(IndexOutOfBoundsException e){
					String str = combo.getText().split(": ")[1].trim();	
					AdmixPopulationGroup group = graph.getProj().findPopGroup(str);
					newShell.dispose();
					AdmixWorkflow.popDialog(group, graph.getProj());
					drawGraph();						
				}
				
			}	

			private ASubject findSubjectByName(String name){							
				for(ASubject s:drawInfo.getSubjects()){
					if(s.getSubject().getName().equals(name)){
						return s;					
					}
				}	
				return null;
			}

		}
	}

	/**
	 *This method listens for when the close button is selected from the menu
	 */
	private class ClickCloseListener implements SelectionListener{

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {			
			mainWindow.notifyListeners(SWT.Close, new Event());
		}	

	}	

	/**
	 *This method listens for when the main shell is resized
	 */
	private class ResizeListener implements ControlListener{
		@Override
		public void controlMoved(ControlEvent arg0) {}

		@Override
		public void controlResized(ControlEvent arg0) {
			placeElements();	
			drawPCA();
		}	

	}

	private class MainWindowListener implements ShellListener{

		@Override
		public void shellActivated(ShellEvent e) {			

		}

		@Override
		public void shellClosed(ShellEvent e) {
			MessageBox messageBox = createExitConfirmationMessageBox();	        
			if(messageBox.open() == SWT.YES){
				e.doit=true;
				System.exit(1);
			}else{
				e.doit=false;
			}		
		}

		@Override
		public void shellDeactivated(ShellEvent e) {
			
		}

		@Override
		public void shellDeiconified(ShellEvent e) {
			drawGraph();
		}

		@Override
		public void shellIconified(ShellEvent e) {

		}
		
		private MessageBox createExitConfirmationMessageBox() {
			int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
			MessageBox messageBox = new MessageBox(mainWindow, style);
			messageBox.setMessage("Are you sure you would like to Quit? Any unsaved changes will be lost.");
			messageBox.setText("Quit?");
			return messageBox;
		}	
		
	}
	
	public static void noProjectMessageBox() {
		MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_ERROR);
		messageBox.setMessage("Unable to perform action");
		messageBox.setText("No Project Open");
		messageBox.open();	
	}
	
	public static void errorMessage(String errorMessage) {
		MessageBox messageBox = new MessageBox(UI.mainWindow, SWT.ICON_ERROR);
		messageBox.setMessage(errorMessage);
		messageBox.setText("Error");
		messageBox.open();	
	}
	/**
	 * Sets the cursor to be the default cursor 
	 */
	public static void setDefaultCursor(){
		mainWindow.setCursor(new Cursor(UI.display, SWT.CURSOR_ARROW));
	}
	/**
	 * Sets the cursor to be the drawing cursor (finger cursor) 
	 */
	public static void setDrawCursor(){
		mainWindow.setCursor(new Cursor(UI.display, SWT.CURSOR_HAND));
	}		
	/**
	 * Sets the cursor to be the dragging cursor (4 way arrow cross) 
	 */
	public static void setDragCursor(){
		mainWindow.setCursor(new Cursor(UI.display, SWT.CURSOR_CROSS));
	}
	/**
	 * Sets the cursor to be the cross cursor 
	 */
	public static void setCrossCursor(){
		mainWindow.setCursor(new Cursor(UI.display,SWT.CURSOR_CROSS));
	}
}




