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
 * Created on Jun 19, 2007
 * 
 */

package tests.org.biojava.dasobert.dasregistry;

import java.net.MalformedURLException;
import java.net.URL;

public class ServerLocation {

//	URL u = new URL("http://deskpro349.dynamic.sanger.ac.uk:8088/dasregistry/services/das_directory");
	//URL u = new URL("http://www.dasregistry.org/services/das_directory");
	public static final boolean withinSanger=false;
	//public static final String REGISTRY= "http://www.dasregistry.org/";
	public static final String SANGER_SOURCES="http://www.dasregistry.org/sanger/das/";
	//public static final String SANGER_SOURCES="http://localhost:8080/dasregistry/sanger/das/";
	
	//public static final String REGISTRY= "http://deskpro349.dynamic.sanger.ac.uk:8080/dasregistry";
	//public static final String REGISTRY= "http://deskpro20727.dynamic.sanger.ac.uk:8080/dasregistryOID";
	public static final String REGISTRY= "http://localhost:8080/dasregistry/";
	
	public static  URL REGISTRYURL ;
	
	static  {
		try {
			REGISTRYURL = new URL(REGISTRY);
		} catch (MalformedURLException e){
			e.printStackTrace();
		}
	}
	
	public static void setProxy(){
		
		if(withinSanger){
		System.setProperty("proxySet", "true");
		System.setProperty("proxyHost", "wwwcache.sanger.ac.uk");
		System.setProperty("proxyPort", "3128");
		
	
		

		}
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
		"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		System.setProperty("javax.xml.parsers.SAXParserFactory",
		"org.apache.xerces.jaxp.SAXParserFactoryImpl");

	}
	
}
