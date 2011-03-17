package tests.org.biojava.dasobert.dasregistry;

import org.biojava.dasobert.dasregistry.Das1Validator;
import org.biojava.dasobert.dasregistry.DasCoordSysComparator;
import org.biojava.dasobert.dasregistry.DasCoordinateSystem;
import junit.framework.TestCase;

public class TestCoordinateSystemEquals extends TestCase{
	
	DasCoordinateSystem coord;
	DasCoordinateSystem coord2;

	public void setUp(){
		//DasCoordinateSystem [] coordinates=new DasCoordinateSystem[2];
		//need to put create coordinate xml  in here from CoordinateSystem objects?
        coord=new DasCoordinateSystem();
        coord.setAuthority("CalJacTes");
        coord.setNCBITaxId(9483);
        coord.setOrganismName("callithrix jacchus");
       coord.setVersion("");
        coord.setCategory("supercontig");
       System.out.println(coord.toString());
        //coordinates[0]=coord;
        
        coord2=new DasCoordinateSystem();
        coord2.setAuthority("CalJacTes");
        coord2.setNCBITaxId(9483);
        coord2.setOrganismName("callithrix jacchus");
       coord2.setVersion("2");
        coord2.setCategory("supercontig");
       System.out.println(coord.toString());
       // coordinates[1]=coord2;
		
	}

	
	public void testKeywordsResponses(){
		
		
		assertTrue(coord.equals(coord2));
		
		
	}
	
//	public void testRowsResponse(){
//		assertTrue(sourcesWithKeywords.length!=sourcesWithRows.length);
//	}
}
