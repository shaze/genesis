/*************************************************************************
 * Genesis ---  program for creating structure and PCA plots of genotype data
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

package shared;

import io.MyFont;
import main.UI;
import java.io.Serializable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import drawObjects.DrawObject;
import admix.AdmixGraph;
	
/**This class stores the data needed to draw one label onto the graph.
 * This includes: 
 * <ul>
 * <li>the text of the label
 * <li>the position and size of the label
 * </ul>
 * 
 * Created by the {@link AdmixGraph} object
 * 
 *
 */
public class GLabel implements DrawObject,Serializable{
	private static final long serialVersionUID = -570567079680085096L;
	private String text;
	private int[] ratio=new int[2];
	private int[] position=new int[4];
	private MyFont font = new MyFont("Arial", 10, SWT.BOLD );
	private boolean underlined=false;
	public boolean selected;
	

	public FontData getFont() {
		return font.toFontData();
	}
	
	public void setFont(FontData font) {
		this.font.setName(font.getName());
		this.font.setSize(font.getHeight());
		this.font.setStyle(font.getStyle());
	}
	
	public Point getRatio() {
		return new Point(ratio[0],ratio[1]);
	}
	public void setRatio(Point ratio) {
		this.ratio[0]=ratio.x;
		this.ratio[1]=ratio.y;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	

        public void snap() {
	   ratio[0]=(ratio[0]/4)*4;
	   ratio[1]=(ratio[1]/4)*4;
        }



	public Rectangle getPosition() {
		return new Rectangle(position[0],position[1],position[2],position[3]);
	}

	public void setPosition(Rectangle position) {
		this.position[0] = position.x;
		this.position[1] = position.y;
		this.position[2] = position.width;
		this.position[3] = position.height;
				
	}

	public boolean getUnderlined() {
		return underlined;
	}

	public void setUnderlined(boolean underlined) {
		this.underlined = underlined;
	}


       public boolean clicked(Point point) {
           return getPosition().contains(point); 
       }


	public static boolean checkValidRatio(Point ratio) {
		if(ratio.x>1000||ratio.x<0||ratio.y>1000||ratio.y<0){
			return false;
		}
		return true;
	}



	@Override
	public boolean getSelected() {
		return this.selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected=selected;
	}


      public void shift(int deltax, int deltay, int imageWidth, int imageHeight) {
	  Point ratio = getRatio();
	  ratio.x=getRatio().x+(1000*deltax)/imageWidth;
	  ratio.y=getRatio().y+(1000*deltay)/imageHeight;
	  if(checkValidRatio(ratio)){
		setRatio(ratio);	
	  }
      }

      public int getType() {		
		return 2;
      }


}
