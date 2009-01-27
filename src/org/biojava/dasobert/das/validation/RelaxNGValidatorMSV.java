package org.biojava.dasobert.das.validation;

import java.util.Map;

import org.biojava.dasobert.das.validation.RegistryRelaxNG;
import org.biojava.services.das.registry.RegistryConfiguration;

/**
 * 
 * @author jw12
 *
 */
public class RelaxNGValidatorMSV {

	


	private static String PATH = null;//default also set here
	
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
		RegistryConfiguration rconfig = new RegistryConfiguration();
		// rconfig is set by the outside via Spring
		configuration = rconfig.getConfiguration();
		PATH=(String)configuration.get("relaxNgBasePath");
		System.out.println("setting relaxng base path in msv validator="+PATH);
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
