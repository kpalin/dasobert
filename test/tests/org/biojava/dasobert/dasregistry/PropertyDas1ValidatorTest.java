package tests.org.biojava.dasobert.dasregistry;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.das.DasSpec;
import org.biojava.dasobert.das2.io.DasSourceReaderImpl;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.Das1Validator;
import org.biojava.dasobert.dasregistry.Das1_6Validator;
import org.biojava.dasobert.dasregistry.DasCoordinateSystem;
import org.biojava.dasobert.dasregistry.DasSource;
import org.biojava.dasobert.dasregistry.DasValidationResult;

import junit.framework.TestCase;

public class PropertyDas1ValidatorTest extends TestCase {
	final String relaxng15 = ServerLocation.REGISTRY + "validation/";
	final String relaxng16 = ServerLocation.REGISTRY + "validation1.6/";
	final String testCode = "P08487";
	final String testURL = "http://das.sanger.ac.uk/das/pfam/";
	Das1_6Validator validator16;
	Das1Validator validator15;
	Das1Validator validator;
	
	public void setUp() {

		ServerLocation.setProxy();
		//validator16 = new Das1_6Validator();
		//validator16.setRelaxNgPath(relaxng16);
		validator15 = new Das1Validator();
		validator15.setRelaxNgPath(relaxng15);
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

		validator=validator15;
		Das1Source[] sangerSources = validator
				.getDas1SourcesFromSourcesXml("http://www.dasregistry.org/das/sources");
		ArrayList<Das1Source> invalidSources = new ArrayList<Das1Source>();
		Map<String,String> errors=new HashMap<String, String>();
		assertTrue(sangerSources.length > 100);
		
		int i = 0;
		for (Das1Source source : sangerSources) {
			//System.out.println(source.getUrl()+" "+source.getNickname());
			boolean isValid = true;
			DasCoordinateSystem[] coords = source.getCoordinateSystem();
			validator.VERBOSE = false;
			
				System.out.println(source.getUrl()+" "+source.getNickname());
				boolean result = validator.validateSourcesCmdShallow(source.getUrl(), true);
				if(result){
					//check whether it uses PROP or PROPERTY
					DasSourceReaderImpl reader = new DasSourceReaderImpl();
					String cmd=validator.removeDataSourceNameFromUrl(source.getUrl());
					System.out.println("cmd="+cmd);
						URL u = null;
						try {
							u = new URL(cmd+"sources");
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						DasSource[] sources = reader.readDasSource(u);
						//System.out.println("number of sources being checked="
							//	+ sources.length);

						
						for (int j = 0; j < sources.length; j++) {
							DasSource source2 = sources[j];
							source2.getProperties();
						}
						
					
					
				}
			}
			i++;
			//System.out.println("number of sources checked=" + i);
			
	}

		
		
	
	
	


}
