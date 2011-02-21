/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 * Created on 19.03.2004
 * @author Andreas Prlic
 *
 */
package org.biojava.dasobert.das;

import org.biojava.dasobert.dasregistry.DasCoordinateSystem;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.List;
import java.util.Map;

/**
 * a class to parse the response of a DAS - Feature request
 * @author Andreas Prlic
 *
 */
public class DAS_Coordinates_Handler  extends DefaultHandler{

	/**
	 * 
	 */
	List<DasCoordinateSystem> coordinates ;
	DasCoordinateSystem coordinate ;
	String coordinatefield ;
	StringBuffer characterdata ;
//attributes for Coordinates element
	String uri ;
	String taxid;
	String source;
	String authority;
	String test_range;
	String version;
	
	
	public DAS_Coordinates_Handler() {
		super();

		coordinates= new ArrayList<DasCoordinateSystem>() ;		
		characterdata = new StringBuffer();
		taxid="";
		version   = "";
		uri = "";
		authority="";
		test_range="";
		version="";
	}

	
	public List<DasCoordinateSystem> get_features() {
		return coordinates ;
	}


	void start_coordinate(String uri, String name, String qName, Attributes atts) {

		
		coordinate = new DasCoordinateSystem() ;
		String coorduri 	= atts.getValue("uri");
		coordinate.setUniqueId(coorduri);
		characterdata = new StringBuffer();
	}

	void add_coordinatedata(String uri, String name, String qName) {
		coordinate.setUniqueId(uri);
		coordinate.setAuthority(authority);
		if(taxid!=null)coordinate.setNCBITaxId(Integer.parseInt(taxid));
		coordinate.setVersion(version);
		coordinate.setCategory(source);
		coordinates.add(coordinate);
		coordinatefield = "";
		characterdata = new StringBuffer();
	}
	
	public void startElement (String xmluri, String name, String qName, Attributes atts){
		//System.out.println("new element "+qName);

		if (qName.equals("COORDINATES")) 
			start_coordinate(uri,  name,  qName,  atts);
		
			characterdata = new StringBuffer();
			coordinatefield = qName ;
		
			
			uri=atts.getValue("uri");
			taxid=atts.getValue("taxid");;
			source=atts.getValue("source");
			authority=atts.getValue("authority");
			test_range=atts.getValue("test_range");
			version=atts.getValue("version");	
			
		

	}



	public void endElement(String uri, String name, String qName) {

		if ( qName.equals("COORDINATES")
		) {
		add_coordinatedata(uri, name, qName);
		}
		
		
	}

	public void characters (char ch[], int start, int length){
		for (int i = start; i < start + length; i++) {

			characterdata.append(ch[i]);
		}

	}

}
