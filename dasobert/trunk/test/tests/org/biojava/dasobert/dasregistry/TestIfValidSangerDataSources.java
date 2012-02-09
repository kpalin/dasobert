package tests.org.biojava.dasobert.dasregistry;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.Das1Validator;
import org.biojava.dasobert.dasregistry.Das1_6Validator;
import org.biojava.dasobert.dasregistry.DasCoordinateSystem;
import org.biojava.dasobert.dasregistry.DasValidationResult;
import org.junit.Before;
import org.junit.Test;


public class TestIfValidSangerDataSources {
	final String relaxng15 = ServerLocation.REGISTRY + "validation/";
	final String relaxng16 = ServerLocation.REGISTRY + "validation1.6/";
	final String testCode = "P08487";
	final String testURL = "http://das.sanger.ac.uk/das/pfam/";
	Das1_6Validator validator16;
	Das1Validator validator15;
	Das1Validator validator;
	
	@Before
	public void setUp() {

		ServerLocation.setProxy();
		validator16 = new Das1_6Validator();
		validator16.setRelaxNgPath(relaxng16);
		validator15 = new Das1Validator();
		validator15.setRelaxNgPath(relaxng15);
		//		

	
	}
	
	@Test
	public void testSangerSourcesResponse(){
		validator=validator16;
		validator.setRelaxNgPath(relaxng16);
		//assertTrue(validator.validateSourcesCmd("http://www.ensembl.org/das/sources"));
		//System.out.println(validator.getValidationMessage());
		//assertFalse(validator.validateUnknownSegment("http://www.ebi.ac.uk/das-srv/uniprot/das/aristotle/"));
		validator.setRelaxNgApprovalNeeded(true);
		validator.VERBOSE=false;
//		assertTrue(validator.validateMaxbins("http://www.ebi.ac.uk/das-srv/genomicdas/das/hydragenevar_eQTL_1mb/", "21:43001932,44001931"));
//		assertFalse(validator.validateMaxbins("http://das.ensembl.org/das/ens_zfish7_array/", "20:43603923,43653923"));
//		
		//Das1Source [] sources=validator.getDas1SourcesFromSourcesXml(ServerLocation.SANGER_SOURCES+"das/sources");
		//System.out.println("sources length:"+sources.length);

		String newSangerUrl="http://web-das-psdev.internal.sanger.ac.uk:8009/das/sources";
//		Das1Source[] sangerSources = validator
//				.getDas1SourcesFromSourcesXml(ServerLocation.SANGER_SOURCES);
		Das1Source[] sangerSources = validator
		.getDas1SourcesFromSourcesXml(newSangerUrl);
		ArrayList<Das1Source> invalidSources = new ArrayList<Das1Source>();
		Map<String,String> errors=new HashMap<String, String>();
		assertTrue(sangerSources.length > 100);
		
		int i = 1;
		for (Das1Source source : sangerSources) {
			System.out.println("============================   "+i+" ================");
			
			DasCoordinateSystem[] coords = source.getCoordinateSystem();
			validator.VERBOSE = false;
			List<Capabilities> caps = Capabilities
					.capabilitiesListFromStrings(source.getCapabilities());
			
			
			
				System.out.println(source.getId());
				 //List<String> statedCaps = source.getCapabilities();
				System.out.println(source.getCapabilities());
				System.out.println("source.geturl="+source.getUrl());
				//stick to whatever server url not what the queryUri says
				List<String> statedCaps=new ArrayList<String>();
				String modifiedUrl=source.getUrl().replace("http://das.sanger.ac.uk/das/","http://web-das-psdev.internal.sanger.ac.uk:8009/das/");
				
				DasValidationResult result = validator.validate(modifiedUrl, source.getCoordinateSystem(), statedCaps);
				List<String> validCaps = result.getValidCaps();
				System.out.println(result.getValidCaps());
				
				
				List<String> notValidButStated = Capabilities.containsSubSet(
						statedCaps, validCaps);
				boolean isValid = false;
				if (notValidButStated.size() == 0) {
					isValid = true;
					result.setOverallValid(true);
				}
				
				if (!isValid) {
					// add to invalid list with the error message
					invalidSources.add(source);
					errors.put(source.getId(), validator.getValidationMessage());
					
				}
				System.out.println("number of invalid sources so far="+invalidSources.size());
			
			validator.validationMessage = ""; // reset the validation message as
												// we only want it for this
												// specific source
			
			
			i++;
		}

		for (Das1Source invalidSource : invalidSources) {
			System.out
					.println("--------------------invalid source found for url "
							+ invalidSource.getId()
							+ " email="
							+ invalidSource.getAdminemail()+"\n"+errors.get(invalidSource.getId()));
		}
		System.out.println("number of invalid Sanger Sources="+invalidSources.size());
		
		
	}
	
	




}
