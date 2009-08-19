package tests.org.biojava.dasobert.dasregistry;
import junit.framework.TestCase;
import org.biojava.dasobert.das.Capabilities;


public class CapablitiesTests extends TestCase{
	
	public void setUp(){
		
	}

	public void testCapabilities(){
		String[] strings={"sequence","sources"};
		Capabilities[]caps=Capabilities.capabilitiesFromStrings(strings);
		assert(caps.length==strings.length);
		String[]falsStrings={"sources","rubbish"};
		Capabilities []caps2=Capabilities.capabilitiesFromStrings(falsStrings);
		assertTrue(caps2.length<falsStrings.length);
	}
}
