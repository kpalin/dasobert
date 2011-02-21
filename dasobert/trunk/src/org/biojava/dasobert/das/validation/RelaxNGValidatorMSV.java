package org.biojava.dasobert.das.validation;

import java.util.Map;

import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.das.validation.RegistryRelaxNG;
import org.apache.axis.components.logger.LogFactory;
import org.apache.log4j.Logger;
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

	



	//private static String path = "http://localhost:8080/dasregistryOID/validation/";//default also set here
	Logger logger=Logger.getLogger(RelaxNGValidatorMSV.class);

	//private static String PATH="http://localhost:8080/dasregistryOID/validation1.6E/";
	private String path = "http://www.dasregistry.org/validation/";
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	private String message="";
	private Map configuration;
	private RegistryRelaxNG rng;
	
	public RelaxNGValidatorMSV(){
		rng = new RegistryRelaxNG();
	}
	/**
	 * 
	 * @param path override the default base path to relaxNg Documents
	 */
	public RelaxNGValidatorMSV(String path){
		this.path=path;
		rng = new RegistryRelaxNG();
	}

	public boolean validateUsingRelaxNG(Capabilities capability, String input) {
		boolean isValid = true;
		
		
		//System.out.println("running relaxng with path="+path);
		if (-1 == rng.validateCatchingExceptions(path + capability+".rng", input)) {
			isValid = false;
			this.message += rng.getRegMessage();
			//logger.debug("valmsg in msv=" + rng.getRegMessage());

		}
		return isValid;

	}

	public String getMessage() {

		return message;
	}
	

}
