package tests.org.biojava.dasobert.dasregistry;
import junit.framework.TestCase;
import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.das.NoSuchCapabilityException;


public class CapablitiesTests extends TestCase{
	
	public void setUp(){
		
	}

	public void testCapabilities(){
		String[] strings={"sequence","sources"};
		Capabilities[] caps=null;
		//try {
			caps = Capabilities.capabilitiesFromStrings(strings);
		//} catch (NoSuchCapabilityException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		assert(caps.length==strings.length);
		String[]falsStrings={"sources","rubbish"};
		Capabilities[] caps2=null;
		//try {
			caps2 = Capabilities.capabilitiesFromStrings(falsStrings);
		//} catch (NoSuchCapabilityException e) {
			// could change this to save a null pointer exception by creating an array if we want to
			//e.printStackTrace();
		//}
		assertTrue(caps2.length==1);
	}
}
