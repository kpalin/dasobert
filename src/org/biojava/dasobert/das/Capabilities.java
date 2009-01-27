/*
 *                  BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 * 
 * Created on Oct 13, 2006
 * 
 */

package org.biojava.dasobert.das;

/** provides some static variables for the DAS1 capabilities
 * 
 * @author Andreas Prlic
 *
 */
public class Capabilities {

	public static final String ENTRY_POINTS = "entry_points";
	public static final String FEATURES     = "features";
	public static final String STYLESHEET   = "stylesheet";
	public static final String SEQUENCE     = "sequence";
	public static final String ALIGNMENT    = "alignment";
	public static final String STRUCTURE    = "structure";
	public static final String TYPES        = "types";
	public static final String DNA	        = "dna";
	public static final String INTERACTION  = "interaction";
	public static final String SOURCES		= "sources";
	
	// this is also the order of the fields in the DasSourceManager capabilities sql...
	// DO NOT CHANGE!
	public final static String[] DAS_CAPABILITIES = { 
		SEQUENCE,
		STRUCTURE,
		ALIGNMENT,
		TYPES,
		FEATURES,
		ENTRY_POINTS,
		DNA,
		STYLESHEET,
		INTERACTION,
		SOURCES
		
	} ;
	
}
