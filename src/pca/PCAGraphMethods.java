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


import java.util.LinkedList;


import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import pca.drawInfo.AxisMarker;
import pca.drawInfo.DrawInfo;
import pca.drawInfo.DrawInfo3D;
import pca.graphElements.AxisLine;
import pca.graphElements.AxisLine3D;

/** This class contains all the methods for constructing a PCA Graph and 
 * populating the {@link DrawInfo} or {@link DrawInfo3D}.
 *  The relevant objects
 * are only passed in once during construction.
  *@see PCAGraph
  */

public class PCAGraphMethods {
	private PCAGraph graph;
	private DrawInfo drawInfo;
	private DrawInfo3D drawInfo3D;
	private Shell mainWindow;

	/**The constructor of the class.
	 * @param graph the PCAGraph object
	 * @param drawInfo the PCA DrawInfo object
	 * @param drawInfo3D the PCA DrawInfo3D
	 * @param shell the shell of the parent
	 */
	public PCAGraphMethods(PCAGraph graph, DrawInfo drawInfo, DrawInfo3D drawInfo3D,Shell shell) {

		this.graph = graph;
		this.drawInfo = drawInfo;
		this.drawInfo3D = drawInfo3D;
		this.mainWindow=shell;
	}

	/**The only public non-constructor method of the class. 
	 * This method takes the information from the 
	 * PCAGraph object and uses it to fill the DrawInfo or DrawInfo3D object.
	 * 
	 * @param size the size of the component to be drawn upon
	 * 
	 */
	
	public void setDrawInfo(Rectangle size){
		int[] pcas = new int[]{graph.x,graph.y,graph.z};	
		int[] margins = getMargins();			
		if(graph.z==-1){	//2D
			checkAxisNames(pcas, 2);
			Rectangle graphArea=calculateGraphArea(margins, size);
			drawInfo.setDrawArea(graphArea);			
			
			float[][] points = get2DPoints(pcas);
			float[][] range = getRange(points);			
			
			Point origin = getOrigin(graphArea,range);				
			drawInfo.setOrigin(origin);
			//these 2 values represent how many units are required to move 1 pixel
	        float xPix=(range[0][1]-range[0][0])/(graphArea.width); 			        
	        float yPix=(range[1][1]-range[1][0])/(graphArea.height);
	        //converts the points to the subject array
	        Subject[] subj=getDrawInfoSubjectArray(origin,xPix,yPix,pcas);
	        drawInfo.setPoints(subj);
	        setAxisLines(origin, range, graphArea);
	        
		}else{		
			checkAxisNames(pcas, 3);
			Rectangle graphArea=calculateGraphArea(margins, size);
			drawInfo3D.setDrawArea(graphArea);
			
			float[][] points = get3DPoints(pcas);
			
			int r=graph.getRotation();
			float xlim,ylim,zlim=graphArea.height;
				
			xlim=(float)(((float)graphArea.width)/Math.cos(Math.toRadians(30-r)));
			ylim=(float)(((float)graphArea.height)/Math.sin(Math.toRadians(30+r)));
			
			float[][] range = getRange(points);
			float xsplit=range[0][1]/(range[0][1]-range[0][0]);		
			float dx = (float)(xsplit-0.5)*xlim;
			float mxx= dx*(float)Math.cos(Math.toRadians(30-r));
			float myx= dx*(float)Math.sin(Math.toRadians(30-r));
			
			float ysplit=range[1][1]/(range[1][1]-range[1][0]);		
			float dy = (float)(ysplit-0.5)*ylim;
			float mxy= dy*(float)Math.cos(Math.toRadians(30+r));
			float myy= dy*(float)Math.sin(Math.toRadians(30+r));
			
			float zsplit=range[2][1]/(range[2][1]-range[2][0]);		
			float dz = (float)(zsplit-0.5)*zlim;
			float mxz= 0;
			float myz= dz;
			
			Point origin = new Point(graphArea.x+graphArea.width/2+Math.round(mxx+mxy+mxz),
					graphArea.y+graphArea.height/2+Math.round(myx+myy+myz));
			drawInfo3D.setOrigin(origin);
			drawInfo3D.setXMarker(getXMarker(xlim,range[0]), range[0][1]);
			drawInfo3D.setYMarker(getYMarker(ylim,range[1]), range[1][1]);
			drawInfo3D.setZMarker(getZMarker(zlim,range[2]), range[2][1]);			
			
			
			
			Subject[] subj=new Subject[graph.getPCAData().length];
							
			for(int i=0;i<graph.getPCAData().length;i++){				
				subj[i]=new Subject();
				subj[i].setSubj(graph.getPCAData()[i]);
				subj[i].setPoint(get3DCoords(graph.getPCAData()[i]));	

				getDrawData(subj[i]);
			}				
				
			drawInfo3D.setPoints(subj);

			fitGraphToArea();

			findAxisIntercepts();

			find3DAxisMarkers();
			findGridMarkers();

										
		}
			
	}
	
	private void setAxisLines(Point origin, float[][] range, Rectangle graphArea) {
		AxisLine[] xlines = getXlines(origin.x,range[0],graphArea.width);
        drawInfo.setxLines(xlines);
        AxisLine[] ylines = getYlines(origin.y,range[1],graphArea.height);
        drawInfo.setyLines(ylines);
		
	}

	private Rectangle calculateGraphArea(int[] margins, Rectangle size) {
		int xMargin=margins[0];
		int yMargin=margins[1];
		if(!graph.getHeading().equals("")){
			yMargin+=30;				
		}
		int xRightMargin = margins[2];
		int yRightMargin = margins[3];

		Rectangle graphArea=new Rectangle(xMargin,yMargin,size.width-xRightMargin-xMargin,
				size.height-yRightMargin-yMargin); 	
		
		return graphArea;
	}

	/**
	 * This method checks if the PCA axes have been changed and
	 * if so, will set the name of that axis to "PCA #"
	 *
	 * @param pcas the pcas
	 * @param dimension the dimension of the graph (2D or 3D)
	 */
	private void checkAxisNames(int[] pcas, int dimension) {
		if(pcas[0]!=drawInfo.oldx){			
			graph.setxLabel("PCA "+String.valueOf(pcas[0]+1));
			drawInfo.oldx=pcas[0];
		}
		if(pcas[1]!=drawInfo.oldy){	
			graph.setyLabel("PCA "+String.valueOf(pcas[1]+1));
			drawInfo.oldy=pcas[1];
		}
		if(dimension==3){
			if(pcas[2]!=drawInfo3D.oldz){
				graph.setzLabel("PCA "+String.valueOf(pcas[2]+1));
				drawInfo3D.oldz=pcas[2];
			}
		}
		
	}

	private float[] getAxisLineValues(float valIntern, float valInterp) {
		float[] mult;
		if((valInterp-valIntern)>25.6){
			mult=findMultiplesInRange(valIntern,valInterp,6.4f);
			
		}else if((valInterp-valIntern)>6.4){
			mult=findMultiplesInRange(valIntern,valInterp,1.6f);
			
			
		}else if((valInterp-valIntern)>1.6){
			mult=findMultiplesInRange(valIntern,valInterp,0.4f);
			
		}else if((valInterp-valIntern)>0.4){
			mult=findMultiplesInRange(valIntern,valInterp,0.1f);
			
		}else if((valInterp-valIntern)>0.1){
			mult=findMultiplesInRange(valIntern,valInterp,0.025f);			
		}else{
			mult=findMultiplesInRange(valIntern,valInterp,0.01f);
		}
		
		return mult;
	}

	private float getDistance(Point x,Point y){
		
		return (float)(Math.sqrt(((long)y.y-x.y)*(y.y-x.y)+(y.x-x.x)*(y.x-x.x)));
	}	
	
	
	/**Fetches the 2D points from the {@link AdmixGraph} for the given pcas 
	 * 
	 * @param pcas the selected pcas
	 * @return 2 dimensional array of values
	 */
	private float[][] get2DPoints(int[] pcas){
		float[][] result;
			result = new float[graph.getPCAData().length][2];
			for(int i=0;i<result.length-1;i++){
				for(int j=0;j<2;j++){
					
					result[i][j]=graph.getPCAData()[i].getData()[pcas[j]];
				}
			}
			return result;
		}
	/**Fetches the 3D points from the {@link AdmixGraph} for the given pcas 
	 * 
	 * @param pcas the selected pcas
	 * @return 2 dimensional array of values
	 */	
	private float[][] get3DPoints(int[] pcas){
		float[][] result;
			result = new float[graph.getPCAData().length][3];		
			for(int i=0;i<result.length-1;i++){
				for(int j=0;j<3;j++){
					result[i][j]=graph.getPCAData()[i].getData()[pcas[j]];
				}
				
			}
			return result;
		}
		
		
		/**Finds the range on each axis
		 * 
		 * @param the set of points
		 * @return 2x2 or 3x2 array in the form (xmin,xmax,ymin,ymax[,zmin,zmax])
		 */
		private float[][] getRange(float[][] points){
			float[][] result=new float[points[0].length][2];
			
			if(points[0].length==2){//2d
				float minX=Float.MAX_VALUE,minY=Float.MAX_VALUE;
				float maxX=-Float.MAX_VALUE,maxY=-Float.MAX_VALUE;
				for(int i=0;i<points.length;i++){
					if((!graph.getPCAData()[i].getVisible())||
							(!graph.getPCAData()[i].getGroup().getVisible())){
						continue;
					}				
					float[] f=points[i];
					if(f[0]>maxX){
						maxX=f[0];
					}
					if(f[0]<minX){
						minX=f[0];
					}
					if(f[1]>maxY){
						maxY=f[1];
					} 
					if(f[1]<minY){
						minY=f[1];
					}
				}			
				if(minX<0){
					result[0][0]=minX*1.05f;	
				}else{
					result[0][0]=minX*0.95f;
				}
				if(maxX<0){
					result[0][1]=maxX*0.95f;	
				}else{
					result[0][1]=maxX*1.05f;
				}
				if(minY<0){
					result[1][0]=minY*1.05f;	
				}else{
					result[1][0]=minY*0.95f;
				}
				if(maxY<0){
					result[1][1]=maxY*0.95f;	
				}else{
					result[1][1]=maxY*1.05f;
				}			
				
			}else{
				float minX=(float) -0.1, minY=(float) -0.1;
				float maxX=(float) 0.1, maxY=(float) 0.1;
				float minZ=(float) -0.1, maxZ=(float) 0.1;
				for(int i=0;i<points.length;i++){										
					if((!graph.getPCAData()[i].getVisible())||
							(!graph.getPCAData()[i].getGroup().getVisible())){				
						continue;
					}
					float[] f=points[i];
					if(f[0]>maxX){
						maxX=f[0];
					}
					if(f[0]<minX){
						minX=f[0];
					}
					if(f[1]>maxY){
						maxY=f[1];
					} 
					if(f[1]<minY){
						minY=f[1];
					}
					if(f[2]<minZ){
						minZ=f[2];
					}
					if(f[2]>maxZ){
						maxZ=f[2];
					}
				}
				if(minX<0){
					result[0][0]=minX*1.05f;	
				}else{
					result[0][0]=minX*0.95f;
				}
				if(maxX<0){
					result[0][1]=maxX*0.95f;	
				}else{
					result[0][1]=maxX*1.05f;
				}
				if(minY<0){
					result[1][0]=minY*1.05f;	
				}else{
					result[1][0]=minY*0.95f;
				}
				if(maxY<0){
					result[1][1]=maxY*0.95f;	
				}else{
					result[1][1]=maxY*1.05f;
				}
				if(minZ<0){
					result[2][0]=minZ*1.05f;	
				}else{
					result[2][0]=minZ*0.95f;
				}
				if(maxZ<0){
					result[2][1]=maxZ*0.95f;	
				}else{
					result[2][1]=maxZ*1.05f;
				}
			}

			return result;
		}
		
		/**
		 * Sets the {@link AxisLine3D}s in the {@link DrawInfo} 
		 */
		private void find3DAxisMarkers() {		
			AxisMarker marker;
			float distanceInter,distanceMarker,valInterp,valIntern;
			Point origin=drawInfo3D.getOrigin();
			Point inter, m;
			AxisLine3D[] axisLines;
			float[] axisLineVals;
			//x
		
			marker=drawInfo3D.getxMarker();///		
			m=marker.getPoint();
			inter=drawInfo3D.getxIntercepts()[0];///
			distanceInter=getDistance(inter,origin);
			distanceMarker=getDistance(marker.getPoint(),origin);
			valInterp=(distanceInter/distanceMarker)*(marker.getValue());
			inter=drawInfo3D.getxIntercepts()[1];///
			distanceInter=getDistance(inter,origin);
			valIntern=-(distanceInter/distanceMarker)*(marker.getValue());
			if(distanceMarker!=0){
				axisLineVals=getAxisLineValues(valIntern,valInterp);
			}else{
				axisLineVals=new float[0];	
			}
			
			
			axisLines=new AxisLine3D[axisLineVals.length];
			for(int i=0;i<axisLineVals.length;i++){
				axisLines[i]=new AxisLine3D();
				float lx=(origin.x)+(m.x-origin.x)*((axisLineVals[i])/(marker.getValue()));
				float ly=(origin.y)+(m.y-origin.y)*((axisLineVals[i])/(marker.getValue()));
				
				axisLines[i].setValue(axisLineVals[i]);
				axisLines[i].setMarker(new Point(Math.round(lx),Math.round(ly)));			
			}
			
			drawInfo3D.setxLines(axisLines);///
	
			//y
			marker=drawInfo3D.getyMarker();
			m=marker.getPoint();
			inter=drawInfo3D.getyIntercepts()[0];
			distanceInter=getDistance(inter,origin);
			distanceMarker=getDistance(marker.getPoint(),origin);
			valInterp=(distanceInter/distanceMarker)*(marker.getValue());
			inter=drawInfo3D.getyIntercepts()[1];
			distanceInter=getDistance(inter,origin);
			valIntern=-(distanceInter/distanceMarker)*(marker.getValue());
			
			if(distanceMarker!=0){
				axisLineVals=getAxisLineValues(valIntern,valInterp);
			}else{
				axisLineVals=new float[0];	
			}
			
			
			
			axisLines=new AxisLine3D[axisLineVals.length];
			for(int i=0;i<axisLineVals.length;i++){
				axisLines[i]=new AxisLine3D();
				float lx=(origin.x)+(m.x-origin.x)*((axisLineVals[i])/(marker.getValue()));
				float ly=(origin.y)+(m.y-origin.y)*((axisLineVals[i])/(marker.getValue()));
				
				axisLines[i].setValue(axisLineVals[i]);
				axisLines[i].setMarker(new Point(Math.round(lx),Math.round(ly)));			
			}
			
			drawInfo3D.setyLines(axisLines);

			marker=drawInfo3D.getzMarker();
			m=marker.getPoint();
			inter=drawInfo3D.getzIntercepts()[0];
			distanceInter=getDistance(inter,origin);
			distanceMarker=getDistance(marker.getPoint(),origin);
			valInterp=(distanceInter/distanceMarker)*(marker.getValue());
			inter=drawInfo3D.getzIntercepts()[1];
			distanceInter=getDistance(inter,origin);
			valIntern=-(distanceInter/distanceMarker)*(marker.getValue());		
			if(distanceMarker!=0){
				axisLineVals=getAxisLineValues(valIntern,valInterp);
			}else{
				axisLineVals=new float[0];	
			}
			
			axisLines=new AxisLine3D[axisLineVals.length];
			for(int i=0;i<axisLineVals.length;i++){
		
				axisLines[i]=new AxisLine3D();			
				float ly=(origin.y)+(m.y-origin.y)*((axisLineVals[i])/(marker.getValue()));
				
				axisLines[i].setValue(axisLineVals[i]);
				axisLines[i].setMarker(new Point(Math.round(origin.x),Math.round(ly)));			
			}
			
			drawInfo3D.setzLines(axisLines);			
			
		}
		
		private void getDrawData(Subject subject){	
			PCASubject p = subject.getSubj();
			RGB col;
			PCAPopulationGroup pop=new PCAPopulationGroup("",new RGB( 0, 0, 0),-1,-1);
			int icon,iconSize;
			if(graph.getGroups()!=null){			
	    		pop=p.getGroup();
	    	}        			
	    	//set the colour of the subj
	    	if(p.getColour()!=null){
	    		col=(p.getColour());
	    	}else if(graph.getGroups()!=null){
	    		col=(pop.getColour());
	    	}else{
	    		col=new RGB( 20, 100, 172);
	    	}
	    	//set the icon
	    	if(p.getIconSymbol()!=-1){
	    		icon=p.getIconSymbol();
	    	}else if(graph.getGroups()!=null){
	    		icon=pop.getIconSymbol();
	    	}else{
	    		icon=0;
	    	}
	    	//set the iconsize
	    	if(p.getIconSize()>1){
	    		iconSize=p.getIconSize();
	    	}else if(graph.getGroups()!=null){
	    		iconSize=pop.getIconSize();
	    	}else{
	    		iconSize=3;
	    	}
	    	
	    	subject.setColour(col);
	    	subject.setIcon(icon);
	    	subject.setIconSize(iconSize);
		}
		
		/**Using the {@link AxisLine}s and the origin, calculates where on the 2D panel
		 * or image the given {@link PCASubject} will lie
		 * @param p a PCASubject
		 * @return the location of the point on the 2D image
		 */
		private Point get3DCoords(PCASubject p){
			float x=p.getData()[graph.x];
			float y=p.getData()[graph.y];
			float z=p.getData()[graph.z];
			
					
			Point origin = drawInfo3D.getOrigin();
			Point result = new Point(origin.x,origin.y);		
			
			AxisMarker xMarker=drawInfo3D.getxMarker();
			AxisMarker yMarker=drawInfo3D.getyMarker();
			AxisMarker zMarker=drawInfo3D.getzMarker();
			
			result.x+=(x/xMarker.getValue())*(xMarker.getPoint().x-origin.x);
			result.x+=(y/yMarker.getValue())*(yMarker.getPoint().x-origin.x);
			result.x+=(z/zMarker.getValue())*(zMarker.getPoint().x-origin.x);
			
			result.y+=(x/xMarker.getValue())*(xMarker.getPoint().y-origin.y);
			result.y+=(y/yMarker.getValue())*(yMarker.getPoint().y-origin.y);
			result.y+=(z/zMarker.getValue())*(zMarker.getPoint().y-origin.y);

			return result;
		}
		
		/** Finds the x {@link AxisMarker}
		 * @param xlim how much space there is in the x direction
		 * @param xrange the range of values in the x direction
		 * @return the X AxisMarker
		 */
		private int[] getXMarker(float xlim,float[] xrange){	
			int[] result = new int[2];
			
			float dx = xlim*((float)xrange[1]/(xrange[1]-xrange[0]));
			float mx=(float)(dx*Math.cos(Math.toRadians(30-graph.getRotation())));
			float my=(float)(dx*Math.sin(Math.toRadians(30-graph.getRotation())));
			
			result[0]=Math.round(drawInfo3D.getOrigin().x+mx);
			result[1]=Math.round(drawInfo3D.getOrigin().y+my);
			
			return result;
		}
		/** Finds the y {@link AxisMarker}
		 * @param how much space there is in the y direction
		 * @param yrange the range of values in the y direction
		 * @return the Y AxisMarker
		 */
		private int[] getYMarker(float ylim,float[] yrange){
			int[] result = new int[2];
			
			float dy = ylim*((float)yrange[1]/(yrange[1]-yrange[0]));
			float mx=(float)(dy*Math.cos(Math.toRadians(30+graph.getRotation())));
			float my=(float)(dy*Math.sin(Math.toRadians(30+graph.getRotation())));
			
			result[0]=Math.round(drawInfo3D.getOrigin().x-mx);
			result[1]=Math.round(drawInfo3D.getOrigin().y+my);
			
			return result;
		}
		/** Finds the z {@link AxisMarker}
		 * @param how much space there is in the z direction
		 * @param zrange the range of values in the z direction
		 * @return the Z AxisMarker
		 */
		private int[] getZMarker(float zlim,float[] zrange){
			int[] result = new int[2];
			
			float dz = zlim*((float)zrange[1]/(zrange[1]-zrange[0]));
			float mx=0f;
			float my=-dz;
			
			result[0]=Math.round(drawInfo3D.getOrigin().x+mx);
			result[1]=Math.round(drawInfo3D.getOrigin().y+my);
			return result;
		}
		
	
		private int transformPoint(int oldRangeMin,int oldRangeMax,int newRangeMax,int x){
			return Math.round(((float)x-oldRangeMin)/(oldRangeMax-oldRangeMin)*(newRangeMax));
		}				
		
		/**Used only for 3D plotting,
		 * this function takes the set of {@link Subject}s and their points
		 * and scales them so that they fit to the given size.
		 * <p>
		 * Both the set of {@link Subject}s and the given size are stored
		 * in the {@link DrawInfo3D} object
		 * 
		 */
		private void fitGraphToArea() {
			Rectangle realSize = drawInfo3D.getDrawArea();			
			Rectangle graphArea = new Rectangle(realSize.x,realSize.y,realSize.width,realSize.height);
			graphArea.height=graphArea.height-10;
			graphArea.width=graphArea.width-10;
			graphArea.x=graphArea.x+5;
			graphArea.y=graphArea.y+5;
			Point orig = new Point(drawInfo3D.getOrigin().x,drawInfo3D.getOrigin().y);
			Point xMark = new Point(drawInfo3D.getxMarker().getPoint().x,drawInfo3D.getxMarker().getPoint().y);
			Point yMark = new Point(drawInfo3D.getyMarker().getPoint().x,drawInfo3D.getyMarker().getPoint().y);
			Point zMark = new Point(drawInfo3D.getzMarker().getPoint().x,drawInfo3D.getzMarker().getPoint().y);
			//x		
			int[] range = getXRange();
			int low = range[0];
			int high = range[1];	
			
			int newx;
			for(int i=0;i<drawInfo3D.getPoints().length;i++){				
				int x=drawInfo3D.getPoints()[i].getPoint().x;
				newx=transformPoint(low,high,graphArea.width,x);
				drawInfo3D.getPoints()[i].setPoint(new Point(graphArea.x + newx,
						drawInfo3D.getPoints()[i].getPoint().y));
			}			
													
			orig.x=graphArea.x +transformPoint(low,high,graphArea.width,orig.x);			
			xMark.x=graphArea.x +transformPoint(low,high,graphArea.width,xMark.x);			
			yMark.x=graphArea.x +transformPoint(low,high,graphArea.width,yMark.x);			
			zMark.x=graphArea.x +transformPoint(low,high,graphArea.width,zMark.x);
			
			range = getXRange();
			low = range[0];
			high = range[1];
			
			//y			
			range = getYRange();
			low = range[0];
			high = range[1];
			
			
			for(int i=0;i<drawInfo3D.getPoints().length;i++){				
				int x=drawInfo3D.getPoints()[i].getPoint().y;
				newx=transformPoint(low,high,graphArea.height,x);
				drawInfo3D.getPoints()[i].setPoint(new Point(drawInfo3D.getPoints()[i].getPoint().x
						,graphArea.y +newx));
			}
			
			
			orig.y=graphArea.y +transformPoint(low,high,graphArea.height,orig.y);			
			xMark.y=graphArea.y +transformPoint(low,high,graphArea.height,xMark.y);			
			yMark.y=graphArea.y +transformPoint(low,high,graphArea.height,yMark.y);			
			zMark.y=graphArea.y +transformPoint(low,high,graphArea.height,zMark.y);
			
			drawInfo3D.setOrigin(orig);
		
			drawInfo3D.getxMarker().setPoint(xMark);
			drawInfo3D.getyMarker().setPoint(yMark);
			drawInfo3D.getzMarker().setPoint(zMark);


		}
		
		private int[] getYRange() {
			int low=99999;
			int high=-11111;
			for(int i=0;i<drawInfo3D.getPoints().length;i++){
				if(subjAndGroupAreVisible(drawInfo3D.getPoints()[i])){
					if (drawInfo3D.getPoints()[i].getPoint().y<low){
						low=drawInfo3D.getPoints()[i].getPoint().y;
					}
					if (drawInfo3D.getPoints()[i].getPoint().y>high){
						high=drawInfo3D.getPoints()[i].getPoint().y;
					}				
				}
			}
			return new int[]{low,high};
		}

		private int[] getXRange() {
			int low=9999999,high=-1111111;			
			for(int i=0;i<drawInfo3D.getPoints().length;i++){
				if(subjAndGroupAreVisible(drawInfo3D.getPoints()[i])){
					if (drawInfo3D.getPoints()[i].getPoint().x<low){										
						low=drawInfo3D.getPoints()[i].getPoint().x;												
					}
					if (drawInfo3D.getPoints()[i].getPoint().x>high){
						high=drawInfo3D.getPoints()[i].getPoint().x;
					}
				}							
			}	
			
			return new int[]{low, high};
		}

		private boolean subjAndGroupAreVisible(Subject subject) {   
			return subject.getSubj().getVisible()&&subject.getSubj().getGroup().getVisible();
		}

		private Subject getDrawInfoSubject(Point origin,float xPix,float yPix,PCASubject p,int[] pcas){
			RGB col;
	    	int icon,iconSize;
	    	PCAPopulationGroup pop=new PCAPopulationGroup("",new RGB( 0, 0, 0),-1,-1);
	    	if(graph.getGroups()!=null){
	    		pop=p.getGroup();
	    	}        	
	    	//set the colour of the subj
	    	if(p.getColour()!=null){
	    		col=(p.getColour());
	    	}else if(graph.getGroups()!=null){
	    		col=(pop.getColour());
	    	}else{
	    		col=new RGB( 20, 100, 172);
	    	}
	    	//set the icon
	    	if(p.getIconSymbol()!=-1){
	    		icon=p.getIconSymbol();
	    	}else if(graph.getGroups()!=null){
	    		icon=pop.getIconSymbol();
	    	}else{
	    		icon=0;
	    	}
	    	//set the iconsize
	    	if(p.getIconSize()>1){
	    		iconSize=p.getIconSize();
	    	}else if(graph.getGroups()!=null){
	    		iconSize=pop.getIconSize();
	    	}else{
	    		iconSize=3;
	    	}
	    	//find the point where the subject is situated
	    	Point point = new Point(origin.x+Math.round((p.getData()[pcas[0]])/xPix),
					origin.y-Math.round((p.getData()[pcas[1]])/yPix));
	    	Subject s=new Subject();
	    	s.setColour(col);
	    	s.setIcon(icon);
	    	s.setIconSize(iconSize);
	    	s.setPoint(point);
	    	s.setSubj(p);
	    	
	    	return s;
		}
		
		/**Used for 2D plotting, this function creates and sets all the 
		 * {@link Subject}
		 * @param origin the location of the origin
		 * @param xPix the value needed to shift a point one pixel to the right 
		 * @param yPix the value needed to shift a point one pixel up 
		 * @param pcas the selected pcas
		 * @return the array of {@link Subject}
		 */
		private Subject[] getDrawInfoSubjectArray(Point origin,float xPix,float yPix,int[] pcas){
			Subject[] result = new Subject[graph.getPCAData().length];
			for(int i=0;i<graph.getPCAData().length;i++){
				result[i]=getDrawInfoSubject(origin,xPix,yPix,graph.getPCAData()[i],pcas);
			}
			return result;
		}
		
		private AxisLine[] getXlines(int origin, float[] range,int length) {				
			float[] mult;
			if((range[1]-range[0])>25.6){
				mult=findMultiplesInRange(range[0],range[1],6.4f);				
			}else if((range[1]-range[0])>6.4){
				mult=findMultiplesInRange(range[0],range[1],1.6f);
			}else if((range[1]-range[0])>1.6){
				mult=findMultiplesInRange(range[0],range[1],0.4f);				
			}else if((range[1]-range[0])>0.4){
				mult=findMultiplesInRange(range[0],range[1],0.1f);				
			}else if((range[1]-range[0])>0.1){
				mult=findMultiplesInRange(range[0],range[1],0.025f);			
			}else if((range[1]-range[0])>0.025){
				mult=findMultiplesInRange(range[0],range[1],0.005f);
			}else if((range[1]-range[0])>0.005){
				mult=findMultiplesInRange(range[0],range[1],0.001f);
			}else{
				mult=findMultiplesInRange(range[0],range[1],0.01f);
			}
			
			AxisLine[] result = new AxisLine[mult.length];
			for(int i=0;i<mult.length;i++){				
				int d = origin+Math.round(mult[i]/(range[1]-range[0])*length);
				result[i] = new AxisLine();
				result[i].setMarker(d);
				result[i].setValue(mult[i]);				
			}
			return result;			
		}
		
		
		
		private AxisLine[] getYlines(int origin, float[] range,int length) {		
			float[] mult;
			if((range[1]-range[0])>25.6){
				mult=findMultiplesInRange(range[0],range[1],6.4f);			
			}else if((range[1]-range[0])>6.4){
				mult=findMultiplesInRange(range[0],range[1],1.6f);						
			}else if((range[1]-range[0])>1.6){
				mult=findMultiplesInRange(range[0],range[1],0.4f);			
			}else if((range[1]-range[0])>0.4){
				mult=findMultiplesInRange(range[0],range[1],0.1f);			
			}else if((range[1]-range[0])>0.1){
				mult=findMultiplesInRange(range[0],range[1],0.025f);			
			}else{
				mult=findMultiplesInRange(range[0],range[1],0.01f);
			}
			
			AxisLine[] result = new AxisLine[mult.length];
			for(int i=0;i<mult.length;i++){				
				int d = origin-Math.round(mult[i]/(range[1]-range[0])*length);
				result[i] = new AxisLine();
				result[i].setMarker(d);		
				result[i].setValue(mult[i]);				
			}
			return result;			
		}
		
		//find the location of the origin based on the size of the graph.
		private Point getOrigin(Rectangle graphArea,float[][] range){
			float minX=range[0][0];
			float maxX=range[0][1];
			float minY=range[1][0];
			float maxY=range[1][1];			
			int yAxis=Math.round(graphArea.x+(-minX/(-minX+maxX))*((graphArea.width-17)));
			int xAxis=Math.round(graphArea.y+(maxY/(-minY+maxY))*((graphArea.height-13)));
			
			return new Point(yAxis,xAxis);
			
		}
		//this finds the endpoints of the gridlines
		private void findGridMarkers(){		
			Point o=drawInfo3D.getOrigin();
			float fx,fy,f;
			int dy,dx;
			Rectangle drawArea=drawInfo3D.getDrawArea();
			//x		
			Point om = drawInfo3D.getyIntercepts()[0];				
			dy=om.y-o.y;
			dx=o.x-om.x;
			for(AxisLine3D a:drawInfo3D.getxLines()){
				Point m=a.getMarker();
				Point mm= new Point(m.x-dx,m.y+dy);
				a.setSecondLineEnd(new Point(m.x,drawInfo3D.getDrawArea().y));
				fx=((float)mm.x-drawArea.x)/(mm.x-m.x);
				fy=((float)(drawArea.y+drawArea.height-mm.y))/(m.y-mm.y);	
				f=Math.max(fx, fy);
				a.setFirstLineEnd(new Point(Math.round(mm.x-f*(mm.x-m.x)),Math.round(mm.y-f*(mm.y-m.y))));
			}
			//y
			om = drawInfo3D.getxIntercepts()[0];				
			dy=om.y-o.y;
			dx=om.x-o.x;
			for(AxisLine3D a:drawInfo3D.getyLines()){
				Point m=a.getMarker();
				Point mm= new Point(m.x+dx,m.y+dy);
				a.setSecondLineEnd(new Point(m.x,drawInfo3D.getDrawArea().y));
				fx=((float)(drawArea.x+drawArea.width-mm.x))/(m.x-mm.x);
				fy=((float)(drawArea.y+drawArea.height-mm.y))/(m.y-mm.y);	
				f=Math.max(fx, fy);
				a.setFirstLineEnd(new Point(Math.round(mm.x-f*(mm.x-m.x)),Math.round(mm.y-f*(mm.y-m.y))));
			}
			//z
			om = drawInfo3D.getyIntercepts()[0];				
			dy=om.y-o.y;
			dx=o.x-om.x;	
			for(AxisLine3D a:drawInfo3D.getzLines()){
				Point m=a.getMarker();
				Point mm= new Point(m.x-dx,m.y+dy);
				a.setSecondLineEnd(new Point(m.x,drawInfo3D.getDrawArea().y));
				fx=((float)mm.x-drawArea.x)/(mm.x-m.x);
				fy=((float)(drawArea.y+drawArea.height-mm.y))/(m.y-mm.y);	
				f=Math.max(fx, fy);
				a.setFirstLineEnd(new Point(Math.round(mm.x-f*(mm.x-m.x)),Math.round(mm.y-f*(mm.y-m.y))));
			}
			om = drawInfo3D.getxIntercepts()[0];				
			dy=om.y-o.y;
			dx=om.x-o.x;
			for(AxisLine3D a:drawInfo3D.getzLines()){
				Point m=a.getMarker();
				Point mm= new Point(m.x+dx,m.y+dy);
				a.setSecondLineEnd(new Point(m.x,drawInfo3D.getDrawArea().y));
				fx=((float)(drawArea.x+drawArea.width-mm.x))/(m.x-mm.x);
				fy=((float)(drawArea.y+drawArea.height-mm.y))/(m.y-mm.y);	
				f=Math.max(fx, fy);
				a.setSecondLineEnd(new Point(Math.round(mm.x-f*(mm.x-m.x)),Math.round(mm.y-f*(mm.y-m.y))));
			}
			

		}


		

		private void findAxisIntercepts(){
			Point[] xIntercepts=new Point[2];
			Point[] yIntercepts=new Point[2];
			Point[] zIntercepts=new Point[2];
			
			Rectangle drawArea = drawInfo3D.getDrawArea();
			float fx,fy,f;
			Point m,o=drawInfo3D.getOrigin();
			//x axis
			m=drawInfo3D.getxMarker().getPoint();
			fx=((float)(drawArea.x+drawArea.width-o.x))/(m.x-o.x);
			fy=((float)(drawArea.y+drawArea.height-o.y))/(m.y-o.y);
			f=Math.min(fx, fy);
			xIntercepts[0]=new Point(Math.round(o.x-f*(o.x-m.x)),Math.round(o.y-f*(o.y-m.y)));
			fx=((float)o.x-drawArea.x)/(o.x-m.x);
			fy=((float)o.y-drawArea.y)/(o.y-m.y);
			f=Math.max(fx, fy);
			xIntercepts[1]=new Point(Math.round(o.x-f*(o.x-m.x)),Math.round(o.y-f*(o.y-m.y)));	
			//y axis
			m=drawInfo3D.getyMarker().getPoint();
			fx=((float)o.x-drawArea.x)/(o.x-m.x);
			fy=((float)(drawArea.y+drawArea.height-o.y))/(m.y-o.y);		
			f=Math.min(fx, fy);
			yIntercepts[0]=new Point(Math.round(o.x-f*(o.x-m.x)),Math.round(o.y-f*(o.y-m.y)));			
			fx=((float)(drawArea.x+drawArea.width-o.x))/(m.x-o.x);
			fy=((float)o.y-drawArea.y)/(o.y-m.y);
			f=Math.max(fx, fy);
			yIntercepts[1]=new Point(Math.round(o.x-f*(o.x-m.x)),Math.round(o.y-f*(o.y-m.y)));	
			//z axis
			zIntercepts[0]=new Point(o.x,drawArea.y);
			zIntercepts[1]=new Point(o.x,drawArea.y+drawArea.height);

			drawInfo3D.setxIntercepts(xIntercepts);
			drawInfo3D.setyIntercepts(yIntercepts);
			drawInfo3D.setzIntercepts(zIntercepts);
			
			
		}
		
		/**
		 * Calculates and returns the margins to leave when drawing the box around the graph.
		 * 
		 * @return The margins in the format {leftMargin, topMargin, rightMargin, bottomMargin}.
		 */
		private int[] getMargins(){
			int topMargin=0, bottomMargin=0, leftMargin=0, rightMargin=0;
			
			//margin for heading
			topMargin += getHeadingMargin();
			
			//margin for axis labels
			int textHeight;
			if(graph.getShowAxisLabels()){
				textHeight = getAxisLabelHeight();		
			}else{
				textHeight=15;
			}
			
			bottomMargin+=textHeight;
			leftMargin+=textHeight;
			if(graph.z!=-1){
				topMargin+=textHeight;
				rightMargin+=textHeight;
			}
			
			
			//margin for key
			if(graph.getKeyPosition()==0){
				int keyWidth=PCAWorkflow.getRightKeyWidth(graph);
				drawInfo.setKeyWidth(keyWidth);
				rightMargin+=15+keyWidth;
				
			}else if(graph.getKeyPosition()==1){
				bottomMargin+=PCAWorkflow.getBottomKeyHeight(graph)+15;
			}
						
			return new int[]{leftMargin, topMargin, rightMargin, bottomMargin};
			
			
		}
		
		private int getAxisLabelHeight() {
			int height=PCAWorkflow.getAxisLabelHeight(graph);
			
			return height + 5;
		}

		private int getHeadingMargin() {
			GC gc = new GC(mainWindow);
			gc.setFont(new Font(gc.getDevice(),graph.getHeadingFont()));
			int margin = gc.textExtent(graph.getHeading()).y;
			gc.dispose();
			return margin;
		}

		private float[] findMultiplesInRange(float lowerBound, float upperBound,float diff){
			int x=(int)Math.floor(lowerBound);
			LinkedList<Float> floats =new LinkedList<Float>();		
			float marker=x;
			while(true){				
				if(marker>=lowerBound){
					floats.add(marker);
				}
				marker+=diff;
				if(marker>upperBound){
					break;
				}			
			}
			float[] result = new float[floats.size()];
			for(int i=0;i<floats.size();i++){
				result[i]=floats.get(i);						
			}
			return result;
			
		}
		
		
}
