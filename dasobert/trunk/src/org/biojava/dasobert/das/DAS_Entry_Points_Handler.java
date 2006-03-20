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
package org.biojava.dasobert.das ;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;


/** a class to parse the reponse of a DAS - types request 
 */
public class DAS_Entry_Points_Handler extends DefaultHandler {
 
    String version ;
    
    public DAS_Entry_Points_Handler() {
	super();
	
	version = null;
    }

    public void startElement (String uri, String name, String qName, Attributes atts){
	if ( qName.equals("DASEP")) {
	    
	}  else if ( qName.equals("ENTRY_POINTS")) {
	 
	    String v = atts.getValue("version");
	    version = v;	    
	} 	
    }
    
    /** returns true if the server returns an entry points */
    public String getVersion() {
	return version;
    }
   
}

