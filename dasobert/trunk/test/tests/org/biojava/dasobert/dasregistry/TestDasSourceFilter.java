package tests.org.biojava.dasobert.dasregistry;

import static org.junit.Assert.assertTrue;

import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.Das1_6Validator;
import org.biojava.dasobert.dasregistry.DasSourceFilter;
import org.junit.Test;


public class TestDasSourceFilter {

	@Test
	public void TestDasSourceFilter(){
		Das1_6Validator validator=new Das1_6Validator();
		Das1Source[] sources = validator
		.getDas1SourcesFromSourcesXml(ServerLocation.REGISTRY+"das/sources");
		
		DasSourceFilter filter=new DasSourceFilter();
		boolean capFound=false;
		for(Das1Source source:sources){
		if(filter.hasValidCapability(source, "features"))capFound=true;
		}
		assertTrue(capFound);
	}
	
	
	
}
