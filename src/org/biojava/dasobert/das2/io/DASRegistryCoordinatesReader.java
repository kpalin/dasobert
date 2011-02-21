package org.biojava.dasobert.das2.io;

import java.io.InputStream;
import java.util.*;

import org.biojava.dasobert.dasregistry.DasCoordinateSystem;

public interface DASRegistryCoordinatesReader {

    /** read a DAS2 coordinates response and return a list of coordinate systems.
	 *
	 */
	public List<DasCoordinateSystem> readRegistryCoordinates(InputStream stream);

}
