
/*
 *                  BioJava development code
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
 * Created on Jan 16, 2006
 *
 */
package org.biojava.dasobert.das2.io;

import java.util.ArrayList;
import java.util.List;

import org.biojava.bio.program.das.dasalignment.DASException;
import org.biojava.dasobert.das2.Das2Capability;
import org.biojava.dasobert.das2.Das2CapabilityImpl;
import org.biojava.dasobert.das2.Das2Source;
import org.biojava.dasobert.das2.Das2SourceImpl;
import org.biojava.dasobert.das2.DasSourceConverter;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.DasCoordinateSystem;
import org.biojava.dasobert.dasregistry.DasSource;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/** a parser for the DAS2 sources response
 * 
 * @author Jonathan Warren
 * @since 
 * @version %I% %G%
 */
public class Das1RegistryCoordinatesHandler  extends DefaultHandler{

	
	List coordinates;
	
	//Das2Source currentSource;
	
	public  Das1RegistryCoordinatesHandler(){
		super();

		
		coordinates   = new ArrayList();
		
	}

	private void startSource (String uri, String name, String qName, Attributes atts){

//		String id = atts.getValue("uri");
//		String title = atts.getValue("title");
//		String doc_ref = atts.getValue("doc_href");
//		String description = atts.getValue("description");
//
//
//		currentSource.setId(id);
//		currentSource.setNickname(title);
//		currentSource.setHelperurl(doc_ref);
//		currentSource.setDescription(description);

	}

	private DasCoordinateSystem getCoordinateSystem(String uri, String name, String qname, Attributes atts){
		// e.g. uri="http://das.sanger.ac.uk/dasregistry/coordsys/CS_LOCAL6" 
		// source="Protein Sequence" authority="UniProt" test_range="P06213" />
		DasCoordinateSystem dcs = new DasCoordinateSystem();
		String id = atts.getValue("uri");
		dcs.setUniqueId(id);

		String source = atts.getValue("source");
		dcs.setCategory(source);

		String authority = atts.getValue("authority");
		dcs.setName(authority);

		String test_range = atts.getValue("test_range");
		dcs.setTestCode(test_range);

		try {
			String taxidstr = atts.getValue("taxid");
			int taxid = Integer.parseInt(taxidstr);
			dcs.setNCBITaxId(taxid);
		} catch (Exception e){}

		String version = atts.getValue("version");
		if ( version != null)
			dcs.setVersion(version);

		return dcs;
	}

	public void startElement (String uri, String name, String qName, Attributes atts){
		//System.out.println("new element "+qName);

	
		if( qName.equals("COORDINATES")){
			DasCoordinateSystem dcs = getCoordinateSystem(uri,name,qName,atts);
			coordinates.add(dcs);

		}       
	}

	

	public void startDocument(){
		
		coordinates = new ArrayList();
		
	}

	public void endElement(String uri, String name, String qName) {
		if ( qName.equals("DASCOORDINATESYSTEM")) {
			
			//currentSource.setCoordinateSystem((DasCoordinateSystem[])coordinates.toArray(new DasCoordinateSystem[coordinates.size()]));

		}
	}

	public DasCoordinateSystem[] getRegistryCoordinates(){    
		//System.out.println("Das2SourceHandler: source size: " + sources.size());
		return (DasCoordinateSystem[])coordinates.toArray(new DasCoordinateSystem[coordinates.size()]);
	}



}
