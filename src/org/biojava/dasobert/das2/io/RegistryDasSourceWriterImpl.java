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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.das.CapabilityStatus;
import org.biojava.dasobert.das.DasTimeFormat;
import org.biojava.dasobert.dasregistry.DasSource;
import org.biojava.dasobert.dasregistry.LightBean;
import org.biojava.dasobert.dasregistry.ValidationResultLights;
import org.biojava.utils.xml.XMLWriter;

public class RegistryDasSourceWriterImpl extends DasSourceWriterImpl  {

	protected void writeCapabilityStatus(XMLWriter xw, DasSource source, boolean times)
			throws IOException {
		//System.out.println("in registryDasSourceWriter capabilitystatus");
		ValidationResultLights results = new ValidationResultLights(
				source);
		List<LightBean> beans = results.getLightsLinksAndResults();
		for (LightBean bean : beans) {
			CapabilityStatus status = bean.getStatus();
			if(!status.equals(CapabilityStatus.OPTIONAL)){//don't bother writing optional fields
				xw.openTag("PROP");
				xw.attribute("name", bean.getCapability().getName());
				xw.attribute("value", status.toString());

				xw.closeTag("PROP");
			}

		}
		xw.openTag("PROP");
		xw.attribute("name", "leaseTime");

		xw.attribute("value", DasTimeFormat.toDASString(source.getLeaseDate()));
		xw.closeTag("PROP");
		
		int daysBeforeDeletion=timer.daysBeforeArchiving(source);
		//System.out.println(daysBeforeDeletion);
		if(daysBeforeDeletion!=60){
		xw.openTag("PROP");
		xw.attribute("name", "daysBeforeDeletion");
		xw.attribute("value", Integer.toString(daysBeforeDeletion));
		xw.closeTag("PROP");
		}
		
		if(times){
			//System.out.println("print times here--------------------");
			Map<Capabilities, Long> timeMap = source.getCapabilityTimes();
		
			Iterator it=timeMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<Capabilities,Long> entry=(Entry<Capabilities, Long>) it.next();
				Capabilities cap=(Capabilities) entry.getKey();
				xw.openTag("PROP");
				xw.attribute("name", cap.getName()+"_time");
				xw.attribute("value", String.valueOf(timeMap.get(cap)));

				xw.closeTag("PROP");
			}
		}
	}

	

}
