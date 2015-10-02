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

package io;

import java.io.Serializable;

import org.eclipse.swt.graphics.FontData;

/**
 * This class is simply a Serializable (saveable) version 
 * of the SWT Font class. It contains a font name, size and style 
 *
 */
public class MyFont implements Serializable{
	private static final long serialVersionUID = 2451306202051575326L;
	private String name;
	private int size,style;
	
	public MyFont(String name, int size, int style) {
		super();
		this.setName(name);
		this.setSize(size);
		this.setStyle(style);
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public FontData toFontData(){
		return new FontData(name,size,style);
	}
}
