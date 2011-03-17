package tests.org.biojava.dasobert.dasregistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.das.DasSpec;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.Das1Validator;
import org.biojava.dasobert.dasregistry.Das1_6Validator;
import org.biojava.dasobert.dasregistry.DasCoordinateSystem;
import org.biojava.dasobert.dasregistry.DasValidationResult;

import junit.framework.TestCase;

public class Das1ValidatorTest extends TestCase {
	final String relaxng15 = ServerLocation.REGISTRY + "validation/";
	final String relaxng16 = ServerLocation.REGISTRY + "validation1.6/";
	final String testCode = "P08487";
	final String testURL = "http://das.sanger.ac.uk/das/pfam/";
	Das1_6Validator validator16;
	Das1Validator validator15;
	Das1Validator validator;
	
	public void setUp() {

		ServerLocation.setProxy();
		validator16 = new Das1_6Validator();
		validator16.setRelaxNgPath(relaxng16);
		//validator15 = new Das1Validator();
		//validator15.setRelaxNgPath(relaxng15);
		//		

	
	}
	
	public void testSourcesResponse(){
		Das1Source test16Source=null;
		Das1Source testFeatureById=null;
		Das1Validator validator=new Das1Validator();
		validator.setRelaxNgPath(ServerLocation.REGISTRY+"validation/");
		//assertTrue(validator.validateSourcesCmd("http://www.ensembl.org/das/sources"));
		//System.out.println(validator.getValidationMessage());
		//assertFalse(validator.validateUnknownSegment("http://www.ebi.ac.uk/das-srv/uniprot/das/aristotle/"));
		validator.setRelaxNgApprovalNeeded(true);
//		assertTrue(validator.validateMaxbins("http://www.ebi.ac.uk/das-srv/genomicdas/das/hydragenevar_eQTL_1mb/", "21:43001932,44001931"));
//		assertFalse(validator.validateMaxbins("http://das.ensembl.org/das/ens_zfish7_array/", "20:43603923,43653923"));
//		
		//Das1Source [] sources=validator.getDas1SourcesFromSourcesXml(ServerLocation.SANGER_SOURCES+"das/sources");
		//System.out.println("sources length:"+sources.length);

		validator=validator16;
		Das1Source[] sangerSources = validator
				.getDas1SourcesFromSourcesXml(ServerLocation.SANGER_SOURCES);
		ArrayList<Das1Source> invalidSources = new ArrayList<Das1Source>();
		Map<String,String> errors=new HashMap<String, String>();
		assertTrue(sangerSources.length > 100);
		
		int i = 0;
		for (Das1Source source : sangerSources) {
			boolean isValid = true;
			DasCoordinateSystem[] coords = source.getCoordinateSystem();
			validator.VERBOSE = false;
			List<Capabilities> caps = Capabilities
					.capabilitiesListFromStrings(source.getCapabilities());
			
//				if (source.getSpecification().equals(
//						DasSpec.SPEC1_6E.toString())) {
//					validator=validator16;
//					
//					
//				} else {
//					validator=validator15;
//					
//				}
				
				//isValid = validator.validateFeatures(source.getUrl(),
				//		coords[0].getTestCode(), false);
				System.out.println(source.getId());
				if(source.getId().equals("test16genes")){
					test16Source=source;
					
					//assertTrue(results.isOverallValid());
					
					
				}
				if(source.getId().equals("GAD")){
				testFeatureById = source;
					
					//assertTrue(results.isOverallValid());
					
					
				}
				
				if (!isValid) {
					// add to invalid list with the error message
					invalidSources.add(source);
					errors.put(source.getId(), validator.getValidationMessage());
				}
				//System.out.println("invalid sources size="
						//+ invalidSources.size());
			
			validator.validationMessage = ""; // reset the validation message as
												// we only want it for this
												// specific source
			i++;
			//System.out.println("number of sources checked=" + i);
			
		}

		for (Das1Source invalidSource : invalidSources) {
			System.out
					.println("--------------------invalid source found for url "
							+ invalidSource.getId()
							+ " email="
							+ invalidSource.getAdminemail()+"/n"+errors.get(invalidSource.getId()));
		}
		
		DasValidationResult results = validator.validate(test16Source.getUrl(), test16Source.getCoordinateSystem(), test16Source.getCapabilities(), false, false);
		System.out.println("test id found="+test16Source.getId());
		//assertTrue(test16Source.getSpecification().equals(DasSpec.SPEC1_6E.toString()));
		assertTrue(results.isValid(Capabilities.STYLESHEET));
		assertTrue(results.isValid(Capabilities.FEATURES));
		assertTrue(results.isValid(Capabilities.TYPES));
		assertTrue(results.isValid(Capabilities.SEQUENCE));
		assertTrue(results.isValid(Capabilities.ENTRY_POINTS));
		assertFalse(results.isValid(Capabilities.INTERACTION));
		assertFalse(results.isValid(Capabilities.STRUCTURE));
		assertFalse(results.isValid(Capabilities.INTERACTION));
		assertFalse(results.isValid(Capabilities.MAXBINS));
		assertTrue(results.isValid(Capabilities.SOURCES));
		assertTrue(results.isValid(Capabilities.CORS));
		assertFalse(results.isValid(Capabilities.FEATURE_BY_ID));
		//assertTrue(invalidSources.size()<=47);
		
		DasValidationResult resultsForFeatureById = validator.validate(testFeatureById.getUrl(), testFeatureById.getCoordinateSystem(), testFeatureById.getCapabilities(), false, false);
		assertTrue(resultsForFeatureById.isValid(Capabilities.FEATURE_BY_ID));
	}
	
	


}
