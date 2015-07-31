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

package pca;

import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabItem;

import pca.drawInfo.DrawInfo;
import pca.drawInfo.DrawInfo3D;

public class PCAProj {
	private PCAGraph graph;
	private DrawInfo drawInfo=new DrawInfo();
	private DrawInfo3D drawInfo3D=new DrawInfo3D();
	private int id;
	private TabItem tab;
	private PaintListener paintListener;
	private Label img;

    
	
	public PaintListener getPaintListener() {
		return paintListener;
	}
	
	public void setPaintListener(PaintListener paintListener) {
		this.paintListener = paintListener;
	}
	
	public void setTab(TabItem tab) {
		this.tab = tab;
	}
	public TabItem getTab() {
		return tab;
	}
	
	public PCAGraph getGraph() {
		return graph;
	}

	public void setGraph(PCAGraph graph) {
		this.graph = graph;
	}

	public DrawInfo getDrawInfo() {
		return drawInfo;
	}

	public void setDrawInfo(DrawInfo drawInfo) {
		this.drawInfo = drawInfo;
	}

	public DrawInfo3D getDrawInfo3D() {
		return drawInfo3D;
	}

	public void setDrawInfo3D(DrawInfo3D drawInfo3D) {
		this.drawInfo3D = drawInfo3D;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PCAProj(PCAGraph graph) {
		super();
		this.graph = graph;
	}

	public Label getImg() {
		return img;
	}

	public void setImg(Label img) {
		this.img = img;
	}

	

}
