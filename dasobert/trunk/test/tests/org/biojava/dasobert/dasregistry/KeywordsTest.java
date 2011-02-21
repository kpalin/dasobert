package tests.org.biojava.dasobert.dasregistry;

import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.Das1Validator;

import junit.framework.TestCase;

public class KeywordsTest extends TestCase {
	String registryLocation=ServerLocation.REGISTRY;
	Das1Validator validator;
	Das1Source [] sources;
	Das1Source [] sourcesWithKeywords;
	Das1Source [] sourcesWithRows;
	
	public void setUp(){
		System.setProperty("proxySet", "true");
		System.setProperty("proxyHost", "wwwcache.sanger.ac.uk");
		System.setProperty("proxyPort", "3128");
		
	
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
		validator=new Das1Validator();
		sources=validator.getDas1SourcesFromSourcesXml(registryLocation+"das/sources");
		sourcesWithKeywords=validator.getDas1SourcesFromSourcesXml(registryLocation+"das/sources?keywords=homo");
		sourcesWithRows=validator.getDas1SourcesFromSourcesXml(registryLocation+"das/sources?keywords=homo&rows=1-2");
		
	}

	
	public void testKeywordsResponses(){
		
		
		assertTrue(sources.length!=sourcesWithKeywords.length);
		
		
	}
	
	public void testRowsResponse(){
		assertTrue(sourcesWithKeywords.length!=sourcesWithRows.length);
	}
}
