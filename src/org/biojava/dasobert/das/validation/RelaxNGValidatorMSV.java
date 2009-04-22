package org.biojava.dasobert.das.validation;

import java.util.Map;

import org.biojava.dasobert.das.validation.RegistryRelaxNG;
//import org.biojava.services.das.registry.RegistryConfiguration;

/**                    BioJava development code
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
 * @author jw12
 *
 */
public class RelaxNGValidatorMSV {

	



	//private static String PATH = "http://localhost:8080/dasregistryOID/validation/";//default also set here
	private static String PATH="http://localhost:8080/dasregistryOID/validation1.6E/";
	//private static String PATH = "http://www.dasregistry.org/validation/";
	public static final String INTERACTION = "interaction.rng";
	public static final String SOURCES = "sources.rng";
	public static final String SEQUENCE = "sequence.rng";
	public static final String FEATURE = "features.rng";
	public static final String TYPES = "types.rng";
	public static final String STRUCTURE = "structure.rng";
	public static final String ALIGNMENT = "alignment.rng";
	public static final String ENTRY_POINTS = "entry_points.rng";
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	private String message="";
	private Map configuration;
	private RegistryRelaxNG rng;
	
	public RelaxNGValidatorMSV(){

		//RegistryConfiguration rconfig = new RegistryConfiguration();

		// rconfig is set by the outside via Spring

		//configuration = rconfig.getConfiguration();
		//PATH=(String)configuration.get("relaxNgBasePath");
		
		System.out.println("setting relaxng base path in msv validator="+PATH);

		//configuration = rconfig.getConfiguration();
		
		//PATH=(String)configuration.get("relaxNgBasePath");
		//System.out.println("setting relaxng base path in msv validator="+PATH);

		rng = new RegistryRelaxNG();
	}

	public boolean validateUsingRelaxNG(String cmdType, String input) {
		boolean isValid = true;
		
		
		
		if (-1 == rng.validateCatchingExceptions(PATH + cmdType, input)) {
			isValid = false;
			this.message += rng.getRegMessage();
			System.out.println("valmsg in msv=" + rng.getRegMessage());

		}
		return isValid;

	}

	public String getMessage() {

		return message;
	}
	


}
