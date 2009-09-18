package tests.org.biojava.dasobert.dasregistry;

import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.Das1Validator;

import junit.framework.TestCase;

public class Das1ValidatorTest extends TestCase {
	String registryLocation=ServerLocation.REGISTRY;
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
		validator.setRelaxNgPath(registryLocation+"/validation/");
		//assertTrue(validator.validateSourcesCmd("http://www.ensembl.org/das/sources"));
		//System.out.println(validator.getValidationMessage());
		//assertFalse(validator.validateUnknownSegment("http://www.ebi.ac.uk/das-srv/uniprot/das/aristotle/"));
		validator.setRelaxNgApprovalNeeded(true);
//		assertTrue(validator.validateMaxbins("http://www.ebi.ac.uk/das-srv/genomicdas/das/hydragenevar_eQTL_1mb/", "21:43001932,44001931"));
//		assertFalse(validator.validateMaxbins("http://das.ensembl.org/das/ens_zfish7_array/", "20:43603923,43653923"));
//		
		Das1Source [] sources=validator.getDas1SourcesFromSourcesXml(registryLocation+"/das/sources");
		System.out.println("sources length:"+sources.length);

		boolean allValid=validator.validateSources(sources);
//		assertTrue(allValid);
//		if(allValid)System.out.println("-------------All sources are valid");
	}
	


}
