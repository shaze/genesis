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

import java.util.ArrayList;

import main.UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;







public class AdmixGraphOptions {
    protected AdmixGraph graph;
    protected Display display;
    protected Shell shell;
    protected Composite comp;
    protected UI ui;
    protected RGB[] colours;


    public AdmixGraphOptions() {};


    public AdmixGraphOptions(AdmixGraph graph, UI ui){
	this.graph=graph;
	this.ui=ui;
    }

    public void run(Display display) {
	this.display=display;
	shell = new Shell(display,SWT.APPLICATION_MODAL|SWT.RESIZE);
	shell.setText("Graph Options");	

	createContents(shell,display);

	shell.pack();
	shell.open();

	centreOnScreen(shell,display);

	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) {
		display.sleep();
	    }
	}


    }

    private void createContents(Shell shell, final Display display) {
	shell.setLayout(new GridLayout(2, false));

	createMainInterface(shell);

	createDoneButton(shell);

    }

    private void createMainInterface(Shell shell) {
	comp = newComposite(shell,1);
	comp.setLayout(new GridLayout(2, false));
	Composite colComp = newComposite(comp,1);
	colComp.setLayout(new GridLayout(3, false));
	Composite indComp = newComposite(comp,1);
	indComp.setLayout(new GridLayout(2, false));
	int noAncestors = graph.getAncestors().length;
	createColComponents(colComp,noAncestors);	
	setColours(noAncestors);
	createIndComponents(indComp,noAncestors);

    }

    private void setColours(int noAncestors) {
	colours=new RGB[noAncestors];
	for(int i=0;i<noAncestors;i++){
	    Ancestor anc = AdmixWorkflow.findAncestorWithOrder(i, graph);
	    colours[i]=anc.getColour();			
	}
	graph.setColours(colours);		
 

	int noGraphs=graph.getProj().getGraphs().size();
	if(noGraphs==1){
	    setColoursToProjectColours(colours,graph.getProj());
	}

    }

    private void setColoursToProjectColours(RGB[] colours, AdmixProj proj) {
	ArrayList<RGB> list=new ArrayList<RGB>();
	for(RGB colour:colours){
	    list.add(colour);
	}
	proj.setColours(list);

    }


    protected void createColComponents(Composite comp,int noAncestors) {
        Button[] buttons, sbuttons;
	Label [] colourLabels;

	colourLabels = new Label[noAncestors];
	buttons = new Button[noAncestors];
	sbuttons = new Button[noAncestors];
	Label sortLabel = new Label(comp,SWT.NONE);
	sortLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,3,1));
	sortLabel.setText("Change and order colours");
		
	for(int i=0;i<noAncestors;i++){
	    Ancestor anc = AdmixWorkflow.findAncestorWithOrder(i, graph);
	    colourLabels[i]=new Label(comp,SWT.BORDER);
	    colourLabels[i].setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
	    colourLabels[i].setBackground(new Color(display,anc.getColour()));
	    colourLabels[i].setText("               ");

	    buttons[i]=new Button(comp,SWT.NONE);
	    buttons[i].setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
	    buttons[i].setText("Change "+anc.getDisplayName());
	    buttons[i].addSelectionListener(new ButtonListener(i,buttons[i],buttons,colourLabels));

	    sbuttons[i]=new Button(comp,SWT.NONE);
	    sbuttons[i].setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
	    sbuttons[i].setText("Sort indivs by colour");
	    sbuttons[i].addSelectionListener(new SpecColourButtonListener(i,sbuttons[i]));


	}

    }



    protected void createIndComponents(Composite comp, int noAncestors) {
        Button sortButton []  =  new Button [2];
	Label sortLabel;
	String sortOption [] = {"Fam  order","Dominant colour"};

	//int sortoption;
	// if (proj!=null)
	// 	sortoption=proj.getSortOption();
	// else
	// 	
	//sortoption=0;
	sortLabel = new Label(comp,SWT.NONE);
	sortLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,2,1));
	sortLabel.setText("Sort within populations algorithmically: ");

	for (int i=0; i< sortOption.length; i++) {
	    sortButton[i] = new Button(comp,SWT.NONE);
	    sortButton[i].setText(sortOption[i]);
	    sortButton[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,2,1));
	    sortButton[i].addSelectionListener(new AlgButtonListener(i,sortButton[i]));
	}

        sortLabel = new Label(comp,SWT.SEPARATOR | SWT.SHADOW_OUT | SWT.HORIZONTAL);
	sortLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,2,1));
        sortLabel = new Label(comp,SWT.SEPARATOR | SWT.SHADOW_OUT | SWT.HORIZONTAL);
	// May be resurrected later
	//sortLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,2,1));
	//sortLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,2,1));
	//sortLabel.setText("Sort within populations by colour: ");


// 	Label colourLabels [] =  new Label [noAncestors];
// 	for(int i=0;i<noAncestors;i++){
// 	    Ancestor anc = graph.getAncestors()[i];
// 	    colourLabels[i]=new Label(comp,SWT.BORDER);
// 	    colourLabels[i].setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
// 	    colourLabels[i].setBackground(new Color(display,anc.getColour()));
// 	    colourLabels[i].setText("               ");
// 	}

    }



    private Composite newComposite(Composite parent, int n) {
	Composite comp = new Composite(parent,SWT.BORDER);
	GridData gd = new GridData(SWT.FILL,SWT.FILL,true,true);
	gd.horizontalSpan=n;
	comp.setLayoutData(gd);
	return comp;
    }	

    private void createDoneButton(final Shell shell) {
	GridData gd = new GridData(SWT.FILL,SWT.FILL,true,true);
	gd.horizontalSpan=2;
	Button donebutton = new Button(shell, SWT.NONE);
	donebutton.setLayoutData(gd); //*
	donebutton.setText("Done");	    

	donebutton.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {		    
		    shell.dispose();
		}
	    });

    }

    protected void centreOnScreen(Shell shell,Display display) {
	int x=(display.getBounds().width-shell.getBounds().width)/2;
	int y=(display.getBounds().height-shell.getBounds().height)/2;;

	shell.setLocation(new Point(x,y));

    }

    private void reorderGroups(Button buttons[], Label colourLabels []) {
	int noAncestors = graph.getAncestors().length;
	for(int i=0;i<noAncestors;i++){
	    Ancestor anc = AdmixWorkflow.findAncestorWithOrder(i, graph);
	    colourLabels[i].setBackground(new Color(display,anc.getColour()));
	    buttons[i].setText("Change "+anc.getDisplayName()+"'s colour");
	}

    }

    private RGB[] findCustomColours() {
	ArrayList<RGB> temp = new ArrayList<RGB>();
	for(AdmixGraph g : graph.getProj().getGraphs()){
	    RGB[] cols=g.getColours();
	    addCustoms(cols,temp);
	}
	return temp.toArray(new RGB[temp.size()]);
    }

    private void addCustoms(RGB[] cols, ArrayList<RGB> temp) {
	for(RGB col:cols){
	    if(!temp.contains(col)){
		temp.add(col);
	    }
	}

    }

    protected class ButtonListener implements SelectionListener{

	private int order;
	private Button buttons[];
	private Label colourLabels[];

	public ButtonListener(int order, Button button, Button allbuttons[], Label colourLabels []){
	    this.order = order;	
	    buttons = allbuttons;
	    this.colourLabels = colourLabels;
	}

	@Override
	    public void widgetDefaultSelected(SelectionEvent arg0) {		

	}

	@Override
	    public void widgetSelected(SelectionEvent arg0) {

	    Ancestor anc = AdmixWorkflow.findAncestorWithOrder(order, graph);
	    RGB[] customCols=findCustomColours();
	    AdmixWorkflow.groupDialog(anc,graph,customCols);
	    reorderGroups(buttons,colourLabels);
	    setColours(graph.getAncestors().length);
	    ui.drawGraph();

	    shell.pack(); 
	    bringToFront(shell);
	    centreOnScreen(shell, display);

	}



	private void bringToFront(final Shell shell) {
	    shell.getDisplay().asyncExec(new Runnable() {
		    public void run() {		
			try{
			    shell.forceActive();	
			}catch(SWTException e){
			    shell.dispose();
			}
					
		    }
		});

	}

    }


    protected class SpecColourButtonListener implements SelectionListener{

	private int order;

	public SpecColourButtonListener(int order, Button button){
	    this.order = order;	
	}

	@Override
	    public void widgetDefaultSelected(SelectionEvent arg0) {		

	}

	@Override
	    public void widgetSelected(SelectionEvent arg0) {
	    Ancestor anc = AdmixWorkflow.findAncestorWithOrder(order, graph);
	    AdmixProj proj = graph.getProj(); 
			
	    graph.sortGraphOnColour(proj, proj.getPhenoColumn(),anc.getID());
	    ui.drawGraph();

	    shell.pack(); 
	    bringToFront(shell);
	    centreOnScreen(shell, display);

	}

	private void bringToFront(final Shell shell) {
	    shell.getDisplay().asyncExec(new Runnable() {
		    public void run() {		
			try{
			    shell.forceActive();	
			}catch(SWTException e){
			    shell.dispose();
			}
					
		    }
		});

	}
    }

    protected class AlgButtonListener implements SelectionListener{

	private int order;

	public AlgButtonListener(int order, Button button){
	    this.order = order;	
	}

	@Override
	    public void widgetDefaultSelected(SelectionEvent arg0) {		

	}

	@Override
	    public void widgetSelected(SelectionEvent arg0) {
	    AdmixProj proj = graph.getProj(); 
			
	    graph.sortGraph(proj, proj.getPhenoColumn(),order);
	    ui.drawGraph();

	    shell.pack(); 
	    bringToFront(shell);
	    centreOnScreen(shell, display);

	}

	private void bringToFront(final Shell shell) {
	    shell.getDisplay().asyncExec(new Runnable() {
		    public void run() {		
			try{
			    shell.forceActive();	
			}catch(SWTException e){
			    shell.dispose();
			}
					
		    }
		});

	}
    }



}
