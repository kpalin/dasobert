package tests.org.biojava.dasobert.dasregistry;

import static org.junit.Assert.*;

import java.util.List;

import org.biojava.dasobert.dasregistry.DasCoordinateSystem;
import org.biojava.dasobert.dasregistry.GetCoordinateSystemsFromRegistry;
import org.junit.Before;
import org.junit.Test;



public class TestGetCoordinateSystemsFromRegistry {
	List<DasCoordinateSystem> regCoords;
	
	
	
	@Before
	public void getCoordsFromRegistry(){
		GetCoordinateSystemsFromRegistry coordGetter = new GetCoordinateSystemsFromRegistry();
		try {

			regCoords = coordGetter.getCoordinates();
                        System.out.println("coordinates from GetCoords size="+regCoords.size());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Test
	public void testCoordsReturned(){
		for(DasCoordinateSystem coord:regCoords){
			System.out.println(coord.getUniqueId());
			assertFalse(coord.getUniqueId().equals(""));
		}
	}
	
	@Test
	public void testOrganismAndTaxidLogic(){
		
		DasCoordinateSystem humanGRCh37=new DasCoordinateSystem();
		
		for(DasCoordinateSystem coord:regCoords){
			if(coord.getUniqueId().equals("http://www.dasregistry.org/dasregistry/coordsys/CS_DS311")){
				System.out.println(coord.getUniqueId());
				System.out.println("organism name="+coord.getOrganismName());
				assertTrue(coord.getOrganismName().equals("Homo sapiens"));
				
			}
			
		}
	}
	
}
