package tests.org.biojava.dasobert.dasregistry;

import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.Das1Validator;

import junit.framework.TestCase;

public class KeywordsCoordsTest extends TestCase {
	
	String registryLocation=ServerLocation.REGISTRY;
	Das1Validator validator;
	Das1Source [] coords;
	Das1Source [] coordsWithKeywords;
	Das1Source [] coordsWithRows;
	
	public void setUp(){
		System.setProperty("proxySet", "true");
		System.setProperty("proxyHost", "wwwcache.sanger.ac.uk");
		System.setProperty("proxyPort", "3128");
		
	
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
//		validator=new Das1Validator();
//coords=validator.getDas1SourcesFromSourcesXml(registryLocation+"/das/coordinatesystem");
//		coordsWithKeywords=validator.getDas1SourcesFromSourcesXml(registryLocation+"/das/coordinatesystem?keywords=homo");
//		coordsWithRows=validator.getDas1SourcesFromSourcesXml(registryLocation+"/das/coordinatesystem?keywords=homo&rows=1-2");
		//currently no coordinate system reader in dasobert????
	}
	
public void testKeywordsResponses(){
		
		
		assertTrue(coords.length!=coordsWithKeywords.length);
		
		
	}
	
	public void testRowsResponse(){
		assertTrue(coordsWithKeywords.length!=coordsWithRows.length);
	}
}
