package tests.org.biojava.dasobert.dasregistry;

import org.biojava.dasobert.dasregistry.Das1Validator;

import junit.framework.TestCase;

public class Das1ValidatorTest extends TestCase {
	public void setUp(){
		
		System.setProperty("proxySet", "true");
		System.setProperty("proxyHost", "wwwcache.sanger.ac.uk");
		System.setProperty("proxyPort", "3128");
		
	
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
//		
		
	}
	
	public void testSourcesResponse(){
		Das1Validator validator=new Das1Validator();
		assertTrue(validator.validateSourcesCmd("http://www.ensembl.org/das/sources"));
		System.out.println(validator.getValidationMessage());
	}

}
