
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
 * Created on Nov 13, 2007
 * 
 */

import java.net.URL;

import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.das2.io.DasSourceReaderImpl;
import org.biojava.dasobert.dasregistry.Das1Validator;
import org.biojava.dasobert.dasregistry.DasSource;


public class ValidateSourcesCmd {
	public static final String SOURCES_LOCATION =  "http://dasmi.bioinf.mpi-inf.mpg.de/sources" ;


	public static void main(String[] args){
		try {

			System.setProperty("proxySet","true");
			System.setProperty("proxyHost","wwwcache.sanger.ac.uk");
			System.setProperty("proxyPort","3128");


			DasSourceReaderImpl reader = new DasSourceReaderImpl();


			URL url = new URL(SOURCES_LOCATION);

			DasSource[] sources = reader.readDasSource(url);


			Das1Validator validator = new Das1Validator();


			for (DasSource source:sources){
				System.out.println("*** validating " + source.getNickname() + " " + source.getUrl());
				String[] testCaps = source.getCapabilities();
				
				if (! source.hasCapability(Capabilities.INTERACTION.toString()))
					continue;
				
				String[] okCaps = validator.validate(source.getUrl(), source.getCoordinateSystem(),testCaps);

				if ( okCaps.length != testCaps.length){
					System.err.println("!!! DAS source " + source.getNickname() +" does not validate");
					System.err.println(validator.getValidationMessage());
					
				} else {
					System.out.println(" Excellent, server is fine! ***");
				}


			}

		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
