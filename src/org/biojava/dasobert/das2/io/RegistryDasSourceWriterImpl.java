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
 * Created on Feb 6, 2006
 *
 */
package org.biojava.dasobert.das2.io;

import java.io.IOException;
import java.util.List;

import org.biojava.dasobert.das.CapabilityStatus;
import org.biojava.dasobert.dasregistry.DasSource;
import org.biojava.dasobert.dasregistry.LightBean;
import org.biojava.dasobert.dasregistry.ValidationResultLights;
import org.biojava.utils.xml.XMLWriter;

public class RegistryDasSourceWriterImpl extends DasSourceWriterImpl  {

	protected void writeCapabilityStatus(XMLWriter xw, DasSource source)
			throws IOException {
		ValidationResultLights results = new ValidationResultLights(
				source);
		List<LightBean> beans = results.getLightsLinksAndResults();
		for (LightBean bean : beans) {
			CapabilityStatus status = bean.getStatus();
			if(!status.equals(CapabilityStatus.OPTIONAL)){//don't bother writing optional fields
				xw.openTag("PROP");
				xw.attribute("name", bean.getCapability().toString());
				xw.attribute("value", status.toString());

				xw.closeTag("PROP");
			}

		}
	}

	

}
