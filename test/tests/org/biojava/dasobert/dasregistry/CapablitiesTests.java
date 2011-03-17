package tests.org.biojava.dasobert.dasregistry;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.das.NoSuchCapabilityException;


public class CapablitiesTests extends TestCase{
	
	public void setUp(){
		
	}

	public void testCapabilities(){
		List<String> strings=new ArrayList();
		strings.add("sequence");
		strings.add("sources");
		Capabilities[] caps=null;
		//try {
			caps = Capabilities.capabilitiesFromStrings(strings);
		//} catch (NoSuchCapabilityException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		assert(caps.length==strings.size());
		List<String>falseStrings=new ArrayList();
		falseStrings.add("sources");
		falseStrings.add("rubbish");
		Capabilities[] caps2=null;
		//try {
			caps2 = Capabilities.capabilitiesFromStrings(falseStrings);
		//} catch (NoSuchCapabilityException e) {
			// could change this to save a null pointer exception by creating an array if we want to
			//e.printStackTrace();
		//}
		assertTrue(caps2.length==1);
	}
}
