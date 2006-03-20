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
 * Created on Mar 20, 2006
 *
 */
package org.biojava.dasobert.dasregistry;

import org.biojava.bio.program.das.dasalignment.DASException;
import org.biojava.dasobert.das2.Das2Source;

public class DasSourceValidator {

    public DasSourceValidator() {
        super();

    }

    public void validateDasSource(DasSource ds) throws DASException {
        
        if ( ds instanceof Das2Source ){
            Das2Source d2s = (Das2Source)ds;
            Das2Validator validator = new Das2Validator();
            validator.validate(d2s);
            
            
        } else {
            // a DAS 1 source ...
            String url =ds.getUrl();
            String[] caps = ds.getCapabilities();
            String testCode = ds.getTestCode();
            Das1Validator validator = new Das1Validator();
            String[] okcaps = validator.validate(url,testCode,caps);
            String validationMessage = validator.getValidationMessage();
            if ( okcaps.length != caps.length){
                throw new DASException("could not validate DasSource " + validationMessage);
            }
            
        }
    }
    
}
