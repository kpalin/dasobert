package tests.org.biojava.dasobert.dasregistry;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.das.DasSpec;
import org.biojava.dasobert.das2.io.DasSourceReaderImpl;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.Das1Validator;
import org.biojava.dasobert.dasregistry.Das1_6Validator;
import org.biojava.dasobert.dasregistry.DasCoordinateSystem;
import org.biojava.dasobert.dasregistry.DasSource;

public class TestSangerSources extends TestCase {

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
		validator15 = new Das1Validator();
		validator15.setRelaxNgPath(relaxng15);
		//		

	}

	public void testSangerSources() {
		// System.out.println("running testSangerSources");

		// DasSourceReaderImpl reader = new DasSourceReaderImpl();
		//
		//	
		// URL url = null ;
		// try {
		// String loc=ServerLocation.SANGER_SOURCES+"sources";
		// System.out.println(loc);
		// url = new URL(loc);
		// } catch (MalformedURLException e) {
		//		
		// }
		// assertNotNull(url);
		//	
		// DasSource[] sources = reader.readDasSource(url);
		//
		// assertTrue(sources.length > 100);

	}

	public void testValidateSangerSources() {
		validator=validator15;
		Das1Source[] sangerSources = validator
				.getDas1SourcesFromSourcesXml(ServerLocation.SANGER_SOURCES
						+ "sources");
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
			if (caps.contains(Capabilities.FEATURES)) {
				if (source.getSpecification().equals(
						DasSpec.SPEC1_6E.toString())) {
					validator=validator16;
					
					
				} else {
					validator=validator15;
					
				}
				isValid = validator.validateFeatures(source.getUrl(),
						coords[0].getTestCode(), false);
				
				if(source.getId().equals("Test 1.6 sources")){
					System.out.println(source.getSpecification());
					assertTrue(source.getSpecification().equals(DasSpec.SPEC1_6E.toString()));
				}
				if (!isValid) {
					// add to invalid list with the error message
					invalidSources.add(source);
					errors.put(source.getId(), validator.getValidationMessage());
				}
				System.out.println("invalid sources size="
						+ invalidSources.size());
			}
			validator.validationMessage = ""; // reset the validation message as
												// we only want it for this
												// specific source
			i++;
			System.out.println("number of sources checked=" + i);
			
		}

		for (Das1Source invalidSource : invalidSources) {
			System.out
					.println("--------------------invalid source found for url "
							+ invalidSource.getId()
							+ " email="
							+ invalidSource.getAdminemail()+"/n"+errors.get(invalidSource.getId()));
		}
		assertTrue(invalidSources.size()<=47);
	}

}
