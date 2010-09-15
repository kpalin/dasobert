package org.biojava.dasobert.test.sourcesdocument;

import java.net.URL;
import java.util.List;

import org.biojava.dasobert.das2.io.DASRegistryCoordinatesReaderXML;
import org.biojava.dasobert.das2.io.DasSourceReaderImpl;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.DasCoordinateSystem;
import org.biojava.dasobert.dasregistry.DasSource;

import junit.framework.TestCase;

public class SourcesDocumentTest extends TestCase {
	List<DasCoordinateSystem> coords;

	public void setUp() {
		DASRegistryCoordinatesReaderXML reader = new DASRegistryCoordinatesReaderXML();
		// need to implement a reader for
		// http://www.dasregistry.org/das1/coordinatesystem cmd

		coords = reader.readRegistryDas1CoorinateSystems();
	}

	public void testCoords() {
 String nameOfSourceToTest="havana_pig";
		DasCoordinateSystem[] coordsForUniprot = null;
		DasSourceReaderImpl reader = new DasSourceReaderImpl();
		try {
			URL u = new URL(
					"http://localhost:8080/dasregistry/sanger/das/sources");// http://das.sanger.ac.uk/das/sources
			DasSource[] sources = reader.readDasSource(u);
			System.out.println("number of sources being checked="
					+ sources.length);

			for (int i = 0; i < sources.length; i++) {
				Das1Source ds = (Das1Source) sources[i];
				if (ds.getId().equalsIgnoreCase(nameOfSourceToTest)) {
					coordsForUniprot = ds.getCoordinateSystem();

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		assertTrue(testCoord(coordsForUniprot[0]));

		DasCoordinateSystem uniprot = new DasCoordinateSystem();
		// uniprot.setAuthority("UniProt");
		uniprot.setCategory("Protein Sequence");
		uniprot.setName("UniProt");
		uniprot.setVersion("");
		uniprot
				.setUniqueId("http://www.dasregistry.org/dasregistry/coordsys/CS_DS6");
		boolean found = testCoord(uniprot);

		assertTrue(found);

	}

	private boolean testCoord(DasCoordinateSystem coordToBeTested) {
		boolean found = false;
		for (DasCoordinateSystem co : coords) {
			if (co.getName().equalsIgnoreCase(coordToBeTested.getName())) {
				System.out.println(co.getDebugString());
				System.out.println("coordToBeTested="
						+ coordToBeTested.getDebugString());

				if (co.equals(coordToBeTested)) {
					System.out.println("found: " + co);
					found = true;
				}
			}

		}
		return found;
	}

}
