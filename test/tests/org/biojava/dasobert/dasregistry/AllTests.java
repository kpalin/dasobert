package tests.org.biojava.dasobert.dasregistry;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for tests.org.biojava.dasobert.dasregistry");
		//$JUnit-BEGIN$
		suite.addTestSuite(Das1ValidatorTest.class);
		suite.addTestSuite(Das1_6ValidatorTest.class);
		suite.addTestSuite(CapablitiesTests.class);
		suite.addTestSuite(KeywordsTest.class);
		suite.addTestSuite(HeadersTest.class);
		//$JUnit-END$
		return suite;
	}

}
