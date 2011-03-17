package tests.org.biojava.dasobert.dasregistry;


import junit.framework.TestCase;

import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.Das1Validator;
import org.biojava.dasobert.dasregistry.Das1_6Validator;
import org.biojava.dasobert.dasregistry.DasValidationResult;
import org.junit.Before;
import org.junit.Test;

public class AdjacentFeatureValidationTest extends TestCase{
	final String relaxng15 = ServerLocation.REGISTRY + "validation/";
	final String relaxng16 = ServerLocation.REGISTRY + "validation1.6/";
	final String testCode = "P08487";
	final String testURL = "http://das.sanger.ac.uk/das/pfam/";
	Das1_6Validator validator16;
	Das1Validator validator15;
	Das1Validator validator;
	
	
	@Before
	public void setUp() throws Exception {
		ServerLocation.setProxy();
		validator16 = new Das1_6Validator();
		validator16.setRelaxNgPath(relaxng16);
		//validator15 = new Das1Validator();
		//validator15.setRelaxNgPath(relaxng15);
		//		
	}
	
	@Test
	public void testAdjacentFeature(){
		
		Das1Source[] sangerSources = validator16.getDas1SourcesFromSourcesXml("http://das.sanger.ac.uk/das/cosmic_mutations");//ServerLocation.SANGER_SOURCES);
		Das1Source testSource=sangerSources[0];
		DasValidationResult result=validator16.validate(testSource.getUrl(), testSource.getCoordinateSystem(), testSource.getCapabilities());
		assertTrue(result.isValid(Capabilities.FEATURES));
		assertFalse(result.isValid(Capabilities.ADJACENT_FEATURE));
		
	}

}
