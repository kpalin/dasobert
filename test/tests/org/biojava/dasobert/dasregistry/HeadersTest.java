package tests.org.biojava.dasobert.dasregistry;

import org.biojava.dasobert.dasregistry.Das1Validator;
import org.biojava.dasobert.dasregistry.DasHeaders;

import junit.framework.TestCase;

public class HeadersTest extends TestCase {
	
	public void setUp(){
		ServerLocation.setProxy();
		
	}

public void testCors(){
	DasHeaders headers=new DasHeaders("http://www.ebi.ac.uk/das-srv/genomicdas/das/batman_GM/");
	assertTrue(headers.validHttpStatus());//make sure we got a valid response before testing headers
	assertTrue(headers.hasCors());
	assertTrue(headers.getDasVersion().equals("DAS/1.53E"));
	
	DasHeaders falseHeaders=new DasHeaders("http://www.ensembl.org/das/Anolis_carolinensis.AnoCar1.0.reference/features?segment=scaffold_3762:1,10342");
	assertFalse(falseHeaders.hasCors());
	
	DasHeaders errorStatus=new DasHeaders("http://www.ensembl.org/das/Homo_sapiens.NCBI36.transcriptorssss/");
	assertFalse(errorStatus.validHttpStatus());
	assertFalse(errorStatus.hasCors());
}
}
	

