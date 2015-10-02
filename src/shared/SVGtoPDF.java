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

package shared;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.eclipse.swt.graphics.Point;
import org.w3c.dom.svg.SVGDocument;
 












import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class SVGtoPDF {
 
    /** The SVG document factory. */
    protected  SAXSVGDocumentFactory factory;
    /** The SVG bridge context. */
    protected BridgeContext ctx;
    /** The GVT builder */
    protected  GVTBuilder builder;
 
    /** Creates an SvgToPdf object. */
    public SVGtoPDF() {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        factory = new SAXSVGDocumentFactory(parser);
        UserAgent userAgent = new UserAgentAdapter();
        DocumentLoader loader = new DocumentLoader(userAgent);
        ctx = new BridgeContext(userAgent, loader);
        ctx.setDynamicState(BridgeContext.DYNAMIC);
 
        builder = new GVTBuilder();
    }
 
    /**
     * Draws an SVG file to a PdfTemplate.
     * @param map      the template to which the SVG has to be drawn.
     * @param resource the SVG content.
     * @throws IOException
     */
    public void drawSvg(String svgText, PdfTemplate template, Point size) throws IOException {
        Graphics2D g2d = new PdfGraphics2D(template, size.x, size.y);
        SVGDocument svgDoc = factory.createSVGDocument(new File("").toURL()
                .toString(), new StringReader(svgText));        
        GraphicsNode mapGraphics = builder.build(ctx, svgDoc);
        mapGraphics.paint(g2d);
        g2d.dispose();
    }
 
    /**
     * Creates a PDF document.
     * @param path 
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException 
     */
    public void createPdf(String svgDocument, Point size, String path) throws IOException, DocumentException {
    	svgDocument=convertSVG(svgDocument);
    	Document document = new Document(new Rectangle(size.x, size.y));
    	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));    	
    	//writer.setRgbTransparencyBlending(false);
    	writer.setDefaultColorspace(PdfName.DEFAULTCMYK, PdfName.DEFAULTCMYK);
    	document.open();
        PdfContentByte cb = writer.getDirectContent();        
        PdfTemplate template = cb.createTemplate(size.x, size.y);
        drawSvg(svgDocument, template, size);
        cb.addTemplate(template, 0, 0);       
        // step 5
        document.close();
    }

	/**
	 * This method converts the svg document by replacing all instances of
	 * rgba(x,y,z,a) in the xml commands with rgb(x,y,z) as the itext conversion doesnt
	 * seem to support the former.
	 * @param svgDocument the old svgDocument string
	 * @return the new svgDocument string
	 */
    private static String convertSVG(String doc) {
		StringBuilder sb=new StringBuilder();
		
		String[] substrings = doc.split("rgba");		
		sb.append(substrings[0]);
		for(int i=1; i<substrings.length; i++){
			int commaPos=getThirdCommaPos(substrings[i]);
			String before=substrings[i].substring(0,commaPos);
			String after=substrings[i].substring(substrings[i].indexOf(')'),substrings[i].length()-1);
			sb.append("rgb");
			sb.append(before.concat(after))	;		
		}
		return sb.toString();		
		
	}

	private static int getThirdCommaPos(String string) {
		int pos=-1;
		for(int i=0;i<3;i++){
			pos =string.indexOf(',',pos+1);
		}
		return pos;
	}
 
   
   
}
