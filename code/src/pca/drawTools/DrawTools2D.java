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

package pca.drawTools;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;

import pca.Subject;
import pca.graphElements.AxisLine;

public class DrawTools2D {	
	/**
	 * Draw a 2D PCA graph onto the canvas
	 */

	static void draw2DPCA() {
		Rectangle graphArea=DrawTools.drawInfo.getDrawArea();
		//draw the heading
		if(!DrawTools.graph.getHeading().equals("")){
			DrawTools.drawHeading(DrawTools.drawable,graphArea);
		}
		//draw grid
		if(DrawTools.graph.isShowGrid()){
			DrawTools.drawable.setColor(DrawTools.lightgrey);
			for(AxisLine a:DrawTools.drawInfo.getxLines()){		
				if((a.getMarker()<graphArea.x)||(a.getMarker()>graphArea.x+graphArea.width)){
					continue;			        		
				}
				DrawTools.drawable.drawLine(a.getMarker(),graphArea.y,a.getMarker(),(graphArea.y+graphArea.height));

			}
			for(AxisLine a:DrawTools.drawInfo.getyLines()){
				if((a.getMarker()<graphArea.y)||(a.getMarker()>graphArea.y+graphArea.height)){
					continue;
				}
				DrawTools.drawable.drawLine(graphArea.x,a.getMarker(),graphArea.x+graphArea.width,a.getMarker());	
			}
		}	        
		//draw the axes
		if(DrawTools.graph.getShowAxes()){
			DrawTools.drawable.setLineWidth(2);
			DrawTools.drawable.setColor(DrawTools.grey);
			DrawTools.drawable.drawLine(DrawTools.drawInfo.getOrigin().x, graphArea.y, DrawTools.drawInfo.getOrigin().x, graphArea.y+graphArea.height);
			DrawTools.drawable.drawLine(graphArea.x, DrawTools.drawInfo.getOrigin().y, graphArea.x+graphArea.width, DrawTools.drawInfo.getOrigin().y);
			DrawTools.drawable.setLineWidth(1);
		}
		//draw the axis labels
		if(DrawTools.graph.getShowAxisLabels()){	
			DrawTools.drawable.setColor(DrawTools.black);
			DrawTools.drawable.setFont(new Font(DrawTools.display, DrawTools.graph.getAxisFont()));
			int width = DrawTools.drawable.textExtent(DrawTools.graph.getxLabel()).x;
			int height = DrawTools.drawable.textExtent(DrawTools.graph.getyLabel()).x;
			DrawTools.drawable.drawText(DrawTools.graph.getxLabel(), graphArea.x+(graphArea.width-width)/2, graphArea.y+(graphArea.height)+3);
			DrawTools.drawable.drawVerticalLabel(DrawTools.graph.getyLabel(), graphArea.x-DrawTools.drawable.textExtent(DrawTools.graph.getyLabel()).y-3, graphArea.y+(graphArea.height-height)/2);

		}
		//draw border around graph
		if(DrawTools.graph.getShowBorder()){
			DrawTools.drawable.setColor(DrawTools.grey);
			DrawTools.drawable.setLineWidth(2);

			DrawTools.drawable.setColor(DrawTools.grey);
			DrawTools.drawable.drawLine(graphArea.x, graphArea.y, graphArea.x+graphArea.width, graphArea.y);
			DrawTools.drawable.drawLine(graphArea.x, graphArea.y+graphArea.height, graphArea.x+graphArea.width, graphArea.y+graphArea.height);
			DrawTools.drawable.drawLine(graphArea.x, graphArea.y, graphArea.x, graphArea.y+graphArea.height);
			DrawTools.drawable.drawLine(graphArea.x+graphArea.width, graphArea.y, graphArea.x+graphArea.width, graphArea.y+graphArea.height);
			DrawTools.drawable.setLineWidth(1);

		}
		//draw numerical markers
		if(DrawTools.graph.isShowMarkers()){
			drawNumericalMarkers2D(graphArea);

		}
		//draw each point	    
		Subject selected=null;
		ArrayList<Subject> onTop = new ArrayList<Subject>();
		for(Subject s: DrawTools.drawInfo.getPoints()){
		 	if(s.getSubj().getSelected()){
 				selected=s;
 				continue;
 			}	     

 			if(s.getSubj().getVisible()&&
 					s.getSubj().getGroup().getVisible()){
 				if(s.getSubj().isOnTop()){
 					onTop.add(s);
 					continue;
 				}
 				DrawIcon.drawIcon(DrawTools.drawable,s.getPoint(),s.getIcon(),s.getSubj().getGroup().getBorder()
 						,s.getColour(),s.getIconSize(),false);	

 			}	

 		}
		//draw the onTop points on top
		for(Subject s:onTop){
		    DrawIcon.drawIcon(DrawTools.drawable,s.getPoint(),s.getIcon(),s.getSubj().getGroup().getBorder(),
						s.getColour(),s.getIconSize(),false);
		}
		//draw the selected point on top
		if (selected!=null){
			DrawIcon.drawIcon(DrawTools.drawable,selected.getPoint(),selected.getIcon(),true,selected.getColour(),
						selected.getIconSize(),true);
		}
		//draw the key if we have input a phenofile
		if(DrawTools.graph.getGroups()!=null&&DrawTools.graph.getKeyPosition()!=-1){
			DrawTools.drawKey(DrawTools.graph.getKeyPosition(),graphArea);
		}

		DrawTools.drawAnnotations(graphArea);	       
		DrawTools.drawObjects(graphArea);	    

		DrawTools.white.dispose();
		DrawTools.grey.dispose();
		DrawTools.black.dispose();
		DrawTools.darkgrey.dispose();
		DrawTools.lightgrey.dispose();

	}
	
	private static void drawNumericalMarkers2D(Rectangle graphArea){
		DrawTools.drawable.setColor(DrawTools.grey);
		DrawTools.drawable.setFont(new Font(DrawTools.display,DrawTools.graph.getScaleFont()));
		DecimalFormat df = new DecimalFormat("#.###");
		for(AxisLine a:DrawTools.drawInfo.getxLines()){	
			String val=df.format(a.getValue());
			if(val.equals("-0")){
				val="0";
			}
			if(val.charAt(0)=='-'){
				val=("- "+val.substring(1));
			}
			if((!val.equals("0")||(!DrawTools.graph.getShowAxes()))){			        		
				int w = DrawTools.drawable.textExtent(val).x;
				int wy = DrawTools.drawable.textExtent(val).y;
				if((a.getMarker()-w/2<=graphArea.x-1)||(a.getMarker()+w/2+1>=graphArea.x+graphArea.width)){
					continue;
				}

				//top
				if(!(a.getMarker()>graphArea.x+graphArea.width-50)){

					DrawTools.drawable.drawText(val,a.getMarker()-w/2,graphArea.y+5);
					DrawTools.drawable.drawLine(a.getMarker(), graphArea.y, a.getMarker(), graphArea.y+4);	
				}			        		
				//bottom
				if(!(a.getMarker()<graphArea.x+50)){			        		
					DrawTools.drawable.drawText(val,a.getMarker()-w/2,graphArea.y+graphArea.height-5-wy);
					DrawTools.drawable.drawLine(a.getMarker(), graphArea.height+graphArea.y-4, a.getMarker(), graphArea.height+graphArea.y);	
				}			        		
			}

		}
		for(AxisLine a:DrawTools.drawInfo.getyLines()){			
			DrawTools.drawable.setColor(DrawTools.grey);

			String val=df.format(a.getValue());
			if(val.equals("-0")){
				val="0";
			}
			if(val.charAt(0)=='-'){
				val=("- "+val.substring(1));
			}
			if((!val.equals("0")||(!DrawTools.graph.getShowAxes()))){			        		
				int w = DrawTools.drawable.textExtent(val).y;
				int wx = DrawTools.drawable.textExtent(val).x;
				if((a.getMarker()-w/2<=graphArea.y-1)||(a.getMarker()+w/2+1>=graphArea.y+graphArea.height)){
					continue;
				}
				//left
				if(!(a.getMarker()<graphArea.y+50)){
					DrawTools.drawable.drawText(val,graphArea.x+5,a.getMarker()-w/2);
					DrawTools.drawable.drawLine(graphArea.x,a.getMarker(), graphArea.x+4, a.getMarker());	
				}			        		
				//right
				if(!(a.getMarker()>graphArea.y+graphArea.height-50)){
					DrawTools.drawable.drawText(val,graphArea.x+graphArea.width-wx-5,a.getMarker()-w/2);
					DrawTools.drawable.drawLine(graphArea.x+graphArea.width-4,a.getMarker(), graphArea.x+graphArea.width, a.getMarker());	
				}

			}			        	
		}		
	}


}
