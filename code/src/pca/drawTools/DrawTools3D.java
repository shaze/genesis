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

package pca.drawTools;

import java.text.DecimalFormat;
import java.util.ArrayList;

import genesisDrawable.GenesisDrawable;
import main.UI;


import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;


import pca.PCAGraph;
import pca.Subject;
import pca.drawInfo.DrawInfo3D;
import pca.graphElements.AxisLine3D;

public class DrawTools3D {
	
	private static DrawInfo3D drawInfo3D;
	private static PCAGraph graph;
	private static GenesisDrawable drawable;
	
	public static void draw3DPCA(){
		setFields();
		
		Rectangle graphArea=drawInfo3D.getDrawArea();

		//draw grid
		if(graph.isShowGrid()){				
			drawGridLines();
		}

		//draw axis markers
		if(graph.isShowMarkers()){

			drawable.setFont(new Font(UI.display,graph.getScaleFont()));
			drawable.setColor(DrawTools.grey);
			DecimalFormat df = new DecimalFormat("#.###");
			//x
			for(AxisLine3D a:drawInfo3D.getxLines()){

				String val=df.format(a.getValue());
				int w=drawable.textExtent(val).x;
				if(a.getMarker().x+3+w>graphArea.x+graphArea.width){
					continue;
				}
				if(a.getMarker().x<graphArea.x){
					continue;
				}

				int h=drawable.textExtent(val).y;
				if(a.getMarker().y-15<graphArea.y){
					continue;
				}

				if(a.getMarker().y-15+h>graphArea.y+graphArea.height){							
					continue;
				}	

				if(val.equals("-0")){
					val="0";
				}
				if(val.charAt(0)=='-'){
					val=("- "+val.substring(1));
				}
				if((!val.equals("0")||(!graph.getShowAxes()))){	
					drawable.drawLine(a.getMarker().x,a.getMarker().y-4,
							a.getMarker().x,a.getMarker().y);
					drawable.drawText(val,a.getMarker().x+6,a.getMarker().y-15);
				}
			}
			//y
			for(AxisLine3D a:drawInfo3D.getyLines()){

				String val=df.format(a.getValue());

				int w=drawable.textExtent(val).x;
				if(a.getMarker().x+w>graphArea.x+graphArea.width){
					continue;
				}
				if(a.getMarker().x<graphArea.x){
					continue;
				}

				int h=drawable.textExtent(val).y;
				if(a.getMarker().y<graphArea.y){
					continue;
				}

				if(a.getMarker().y+h>graphArea.y+graphArea.height){							
					continue;
				}

				if(val.equals("-0")){
					val="0";
				}
				if(val.charAt(0)=='-'){
					val=("- "+val.substring(1));
				}
				if((!val.equals("0"))){	
					drawable.drawLine(a.getMarker().x,a.getMarker().y-4,
							a.getMarker().x,a.getMarker().y);
					drawable.drawText(val,a.getMarker().x,a.getMarker().y);
				}
			}
			//z
			for(AxisLine3D a:drawInfo3D.getzLines()){
				String val=df.format(a.getValue());

				int w=drawable.textExtent(val).x;
				if(a.getMarker().x+5+w>graphArea.x+graphArea.width){
					continue;
				}
				if(a.getMarker().x<graphArea.x){
					continue;
				}

				int h=drawable.textExtent(val).y;
				if(a.getMarker().y-8<graphArea.y){
					continue;
				}

				if(a.getMarker().y-8+h>graphArea.y+graphArea.height){							
					continue;
				}

				if(val.equals("-0")){
					val="0";
				}
				if(val.charAt(0)=='-'){
					val=("- "+val.substring(1));
				}
				if((!val.equals("0"))){	
					drawable.drawLine(a.getMarker().x,a.getMarker().y,
							a.getMarker().x+4,a.getMarker().y);
					drawable.drawText(val,a.getMarker().x+5,a.getMarker().y-8);
				}
			}
		}		

		//draw the axes
		if(graph.getShowAxes()){
			drawable.setLineWidth(2);

			//negative
			drawable.setColor(DrawTools.lightgrey);
			drawable.drawLine(drawInfo3D.getOrigin().x, drawInfo3D.getOrigin().y,
					drawInfo3D.getxIntercepts()[1].x,  drawInfo3D.getxIntercepts()[1].y);
			drawable.drawLine(drawInfo3D.getOrigin().x, drawInfo3D.getOrigin().y,
					drawInfo3D.getyIntercepts()[1].x,  drawInfo3D.getyIntercepts()[1].y);
			drawable.drawLine(drawInfo3D.getOrigin().x, drawInfo3D.getOrigin().y,
					drawInfo3D.getzIntercepts()[1].x,  drawInfo3D.getzIntercepts()[1].y);
			//positive	
			drawable.setColor(DrawTools.darkgrey);
			drawable.drawLine(drawInfo3D.getOrigin().x, drawInfo3D.getOrigin().y,
					drawInfo3D.getxIntercepts()[0].x,  drawInfo3D.getxIntercepts()[0].y);
			drawable.drawLine(drawInfo3D.getOrigin().x, drawInfo3D.getOrigin().y,
					drawInfo3D.getyIntercepts()[0].x,  drawInfo3D.getyIntercepts()[0].y);					
			drawable.drawLine(drawInfo3D.getOrigin().x, drawInfo3D.getOrigin().y,
					drawInfo3D.getzIntercepts()[0].x,  drawInfo3D.getzIntercepts()[0].y);

			drawable.setLineWidth(1);
		} 
		//draw the box
		if(graph.getShowBorder()){
			drawBox(graphArea);				
		}
		cleanUpAroundBox(graphArea);

		//draw the axislabels
		if (graph.getShowAxisLabels()){
			drawable.setFont(new Font(UI.display, graph.getAxisFont()));
			drawable.setColor(DrawTools.black);
			//x
			Rectangle[] axisLabels = new Rectangle[3];
			Point intercept = drawInfo3D.getxIntercepts()[0];
			if(graphArea.x+graphArea.width==intercept.x){
				int width = drawable.textExtent(graph.getxLabel()).y;
				int height = drawable.textExtent(graph.getxLabel()).x;
				drawable.drawVerticalLabel(graph.getxLabel(),intercept.x+4,intercept.y-height/2);					
				axisLabels[0]=new Rectangle(intercept.x+4,intercept.y-height/2,width,height);
			}else{
				int width = drawable.textExtent(graph.getxLabel()).x;
				int height = drawable.textExtent(graph.getxLabel()).y;
				drawable.drawText(graph.getxLabel(),intercept.x-width/2,intercept.y+4);
				axisLabels[0]=new Rectangle(intercept.x-width/2,intercept.y+4,width,height);
			}
			//y	
			intercept = drawInfo3D.getyIntercepts()[0];
			if(graphArea.x==intercept.x){
				int width = drawable.textExtent(graph.getyLabel()).y;
				int height = drawable.textExtent(graph.getyLabel()).x;
				drawable.drawVerticalLabel(graph.getyLabel(),intercept.x-3-width,intercept.y-height/2);
				axisLabels[1]=new Rectangle(intercept.x-3-width,intercept.y-height/2,width,height);
			}else{
				int width = drawable.textExtent(graph.getyLabel()).x;
				int height = drawable.textExtent(graph.getyLabel()).y;
				drawable.drawText(graph.getyLabel(),intercept.x-width/2,intercept.y+4);
				axisLabels[1]=new Rectangle(intercept.x-width/2,intercept.y+4,width,height);
			}
			//z
			intercept = drawInfo3D.getzIntercepts()[0];
			int width = drawable.textExtent(graph.getzLabel()).x;
			int height = drawable.textExtent(graph.getzLabel()).y;
			drawable.drawText(graph.getzLabel(),intercept.x-width/2,intercept.y-height-2);				
			axisLabels[2]=new Rectangle(intercept.x-width/2,intercept.y-height-2,width,height);						

			drawInfo3D.setAxisLabels(axisLabels);			
		}


		//draw the heading
		if(!graph.getHeading().equals("")){
			DrawTools.drawHeading(drawable,graphArea);
		}

		//draw the points
		Subject selected=null;
		ArrayList<Subject> onTop = new ArrayList<Subject>();
		for(Subject s: drawInfo3D.getPoints()){
			if(s.getSubj().getSelected()){
				selected=s;
				continue;
			}	        	
			if(s.getSubj().getVisible()&&s.getSubj().getGroup().getVisible()){
				if(s.getSubj().isOnTop()){	        		
					onTop.add(s);
					continue;
				}
				DrawIcon.drawIcon(drawable,s.getPoint(),s.getIcon(),s.getSubj().getGroup().getBorder(),
						s.getColour(),s.getIconSize(),false);	
			}		        	
		}
		//draw the onTop points on top
		for(Subject s:onTop){
			DrawIcon.drawIcon(drawable,s.getPoint(),s.getIcon(),s.getSubj().getGroup().getBorder(),
					s.getColour(),s.getIconSize(),false);

		}
		//draw the selected point on top
		if (selected!=null){
			DrawIcon.drawIcon(drawable,selected.getPoint(),selected.getIcon(),true,selected.getColour(),
					selected.getIconSize(),true);
		}
		//draw ze key
		if(graph.getGroups()!=null&&graph.getKeyPosition()!=-1){
			DrawTools.drawKey(graph.getKeyPosition(),graphArea);
		}
		//draw the annotations
		DrawTools.drawAnnotations(graphArea);	
		DrawTools.drawObjects(graphArea);
		DrawTools.white.dispose();
		DrawTools.grey.dispose();
		DrawTools.black.dispose();
		DrawTools.darkgrey.dispose();
		DrawTools.lightgrey.dispose();

	}
	
	static void drawBox(Rectangle graphArea) {
		drawable.setLineWidth(2);
		drawable.setColor(DrawTools.grey);
		drawable.drawLine(graphArea.x, graphArea.y, graphArea.x+graphArea.width, graphArea.y);
		drawable.drawLine(graphArea.x, graphArea.y+graphArea.height, graphArea.x+graphArea.width, graphArea.y+graphArea.height);
		drawable.drawLine(graphArea.x, graphArea.y, graphArea.x, graphArea.y+graphArea.height);
		drawable.drawLine(graphArea.x+graphArea.width, graphArea.y, graphArea.x+graphArea.width, graphArea.y+graphArea.height);
		drawable.setLineWidth(1);

	}

	static void cleanUpAroundBox(Rectangle graphArea) {		
		drawable.setColor(DrawTools.white);
		drawable.fillRectangle(0, 0, DrawTools.size.x, graphArea.y);
		drawable.fillRectangle(0, 0, graphArea.x, DrawTools.size.y);
		drawable.fillRectangle(graphArea.x + graphArea.width, 0, DrawTools.size.x - (graphArea.x + graphArea.width), DrawTools.size.y);
		drawable.fillRectangle(0, graphArea.y + graphArea.height, DrawTools.size.x, DrawTools.size.y- (graphArea.y + graphArea.height));
	}
	
	private static void drawGridLines() {
		drawable.setColor(DrawTools.lightgrey);
		for(AxisLine3D a:drawInfo3D.getxLines()){
			if(a.getValue()>-0.001){
				drawable.drawLine(a.getMarker().x,a.getMarker().y,a.getFirstLineEnd().x,a.getFirstLineEnd().y);
				drawable.drawLine(a.getMarker().x,a.getMarker().y,a.getSecondLineEnd().x,a.getSecondLineEnd().y);	
			}							
		}
		for(AxisLine3D a:drawInfo3D.getyLines()){
			if(a.getValue()>-0.001){
				drawable.drawLine(a.getMarker().x,a.getMarker().y,a.getFirstLineEnd().x,a.getFirstLineEnd().y);
				drawable.drawLine(a.getMarker().x,a.getMarker().y,a.getSecondLineEnd().x,a.getSecondLineEnd().y);	
			}
		}
		for(AxisLine3D a:drawInfo3D.getzLines()){
			if(a.getValue()>-0.001){
				drawable.drawLine(a.getMarker().x,a.getMarker().y,a.getFirstLineEnd().x,a.getFirstLineEnd().y);
				drawable.drawLine(a.getMarker().x,a.getMarker().y,a.getSecondLineEnd().x,a.getSecondLineEnd().y);	
			}
		}

	}

	private static void setFields() {
		drawInfo3D = DrawTools.drawInfo3D;
		graph = DrawTools.graph;
		drawable = DrawTools.drawable;
				
		
	}

	
	

}
