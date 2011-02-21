package tests.org.biojava.dasobert.dasregistry;

import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.Das1Validator;
import org.biojava.dasobert.dasregistry.Das1_6Validator;

import junit.framework.TestCase;

public class Das1_6ValidatorTest extends TestCase {
	final String relaxng15=ServerLocation.REGISTRY+"validation/";
	final String relaxng16=ServerLocation.REGISTRY+"validation1.6/";
	final String testCode="P08487";
	final String testURL="http://das.sanger.ac.uk/das/pfam/";
	Das1_6Validator validator16;
	Das1Validator validator;
	public void setUp(){
		System.out.println("running setup");
		ServerLocation.setProxy();
		validator16=new Das1_6Validator();
		validator16.setRelaxNgPath(relaxng16);
		Das1Validator validator=new Das1Validator();
		validator.setRelaxNgPath(relaxng15);
//		
		
	}
	
//	public void testSourcesResponse(){
//		Das1Validator validator=new Das1Validator();
//		validator.setRelaxNgPath(relaxng15);
//		assertTrue(validator.validateSourcesCmd("http://www.ensembl.org/das/sources"));
//		System.out.println(validator.getValidationMessage());
//		Das1_6Validator validator16=new Das1_6Validator();
//		validator16.setRelaxNgPath(relaxng16);
//		assertTrue(validator16.validateSourcesCmd("http://www.ensembl.org/das/sources")==validator.validateSourcesCmd("http://www.ensembl.org/das/sources"));
//		assertTrue(validator16.getValidationMessage()==validator.getValidationMessage());
//		
//		assertFalse(validator16.validateFeatures(testURL, testCode, false)==validator.validateFeatures(testURL, testCode, false));
//		assertFalse(validator16.validateSequence(testURL, testCode)==validator.validateSequence(testURL, testCode));
//		assertFalse(validator16.validateTypes(testURL, false)==validator.validateTypes(testURL, false));
//		
//	}
	
	public void testHeadersTesting(){
		validator16.validateHeaders(testURL);
		
	}
	
	public void testAndy16SourcesResponse(){
//		Das1Validator validator=new Das1Validator();
//		validator.setRelaxNgPath(relaxng15);
		//assertTrue(validator.validateSourcesCmd("http://www.ensembl.org/das/sources"));
		//System.out.println(validator.getValidationMessage());
		
		//String andyUrl="http://wwwdev.ebi.ac.uk/das-srv/genomicdas/das/sources";
		
		//assertTrue(validator16.validateSourcesCmd(andyUrl));
		//System.out.println("about to run getSources");
		//Das1Source [] sources=validator16.getDas1SourcesFromSourcesXml(andyUrl);
		//System.out.println("sources length:"+sources.length);

		//boolean allValid=validator16.validateSources(sources);
		//assertTrue(allValid);
		//if(allValid)System.out.println("-------------All sources are valid");
		
		//assertTrue(validator16.validateSourcesCmdShallow("http://www.ensembl.org/das/sources")==validator.validateSourcesCmdShallow("http://www.ensembl.org/das/sources"));
//		assertTrue(validator16.getValidationMessage()==validator.getValidationMessage());
//		
//		assertFalse(validator16.validateFeatures(testURL, testCode, false)==validator.validateFeatures(testURL, testCode, false));
//	assertFalse(validator16.validateSequence(testURL, testCode)==validator.validateSequence(testURL, testCode));
//		assertFalse(validator16.validateTypes(testURL, false)==validator.validateTypes(testURL, false));
		
	}
	
	

}
