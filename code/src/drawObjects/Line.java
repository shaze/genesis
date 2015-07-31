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

package drawObjects;

import java.io.Serializable;

import org.eclipse.swt.graphics.Point;

public class Line implements DrawObject, Serializable{
	private static final long serialVersionUID = 1490825282373465425L;
	public Point start;
	public Point end;
	
	public boolean selected;
	
	public Line(Point start, Point end){
		this.start=start;
		this.end=end;
	}

	public int getType() {		
		return 0;
	}

	public boolean clicked(Point point) {		
		if(LineToPointDistance.distToSegment(point, start, end)<4){
			return true;
		}else{
			return false;
		}
	}



    public void snap() {
	start.x = start.x/4*4;
	start.y = start.y/4*4;
	end.x   = end.x/4*4;
	end.y   = end.y/4*4;
    }


       static boolean checkValidPos(int m) {
	   return (m>=0) && (m<=1001);
       }



        public void shift(int deltax, int deltay, int imageWidth, int imageHeight) {
     	    int xRatio = (1000*(deltax))/imageWidth;
	    int yRatio = (1000*(deltay))/imageHeight;
	    int x1,x2,y1,y2;
	    
	    x1  = start.x+xRatio;
	    y1  = start.y+yRatio;
	    y2  = end.y+yRatio;
	    x2  = end.x+xRatio;
	    if ( checkValidPos(x1) && checkValidPos(x2) && checkValidPos(y1) && checkValidPos(y2) ) {
		start.x = x1;
		start.y = y1;
		end.x   = x2;
		end.y   = y2;
	    }
	    
	}

	@Override
	public boolean getSelected() {
		return this.selected;
	}


	@Override
	public void setSelected(boolean selected) {
		this.selected=selected;
	}
}
