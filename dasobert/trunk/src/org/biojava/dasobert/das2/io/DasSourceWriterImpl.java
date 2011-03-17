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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.das.CapabilityStatus;
import org.biojava.dasobert.das.DasTimeFormat;
import org.biojava.dasobert.das2.Das2Capability;
import org.biojava.dasobert.das2.Das2CapabilityImpl;
import org.biojava.dasobert.das2.Das2Source;
import org.biojava.dasobert.dasregistry.Das1Source;

import org.biojava.dasobert.dasregistry.DasCoordinateSystem;
import org.biojava.dasobert.dasregistry.DasSource;
import org.biojava.dasobert.dasregistry.LightBean;
import org.biojava.dasobert.dasregistry.RegistryTimer;
import org.biojava.dasobert.dasregistry.ValidationResultLights;
import org.biojava.utils.xml.PrettyXMLWriter;
import org.biojava.utils.xml.XMLWriter;

public class DasSourceWriterImpl implements DasSourceWriter {

	public static final String COORDSYSURI = "http://www.dasregistry.org/dasregistry/coordsys/";
	private RegistryTimer timer;
	public DasSourceWriterImpl() {
		super();
		timer=new RegistryTimer();

	}

	public void writeCoordinateSystem(XMLWriter xw, DasCoordinateSystem co)
			throws IOException {
		xw.openTag("COORDINATES");
		String uri = co.getUniqueId();
		if (!uri.startsWith(COORDSYSURI))
			uri = COORDSYSURI + uri;

		xw.attribute("uri", uri);

		int taxid = co.getNCBITaxId();
		if (taxid != 0) {
			xw.attribute("taxid", taxid + "");
		}

		xw.attribute("source", co.getCategory());
		xw.attribute("authority", co.getName());
		xw.attribute("test_range", co.getTestCode());
		// TODO: get version from name;
		String version = co.getVersion();
		if ((version != null) && (!version.equals("")))
			xw.attribute("version", version);
		xw.print(co.toString());
		xw.closeTag("COORDINATES");
	}

	public void writeDasSource(XMLWriter xw, DasSource source, boolean writeTimeData)
			throws IOException {
		xw.openTag("SOURCE");
		// System.out.println("DasSourceWriterImpl:  writing new source");

		xw.attribute("uri", source.getId());
		xw.attribute("title", source.getNickname());
		String helperurl = source.getHelperurl();
		if (helperurl == null)
			helperurl = "";
		if (!helperurl.equals(""))
			xw.attribute("doc_href", source.getHelperurl());
		String desc = source.getDescription();
		desc = desc.replaceAll("\n", " ");
		desc = desc.replaceAll("\r", " ");
		xw.attribute("description", desc);

		xw.openTag("MAINTAINER");
		xw.attribute("email", source.getAdminemail());
		xw.closeTag("MAINTAINER");
		// System.out.println("before version");
		xw.openTag("VERSION");
		xw.attribute("uri", source.getId());

		Date d = source.getRegisterDate();
		if (d == null)
			d = new Date();
		xw.attribute("created", DasTimeFormat.toDASString(d));

		// System.out.println("before coords");
		DasCoordinateSystem[] coords = source.getCoordinateSystem();
		if(coords!=null && coords.length>0){
		for (int i = 0; i < coords.length; i++) {
			DasCoordinateSystem co = coords[i];
			if(co!=null)writeCoordinateSystem(xw, co);

		}
		}
		// System.out.println("before das specific part");
		if (source instanceof Das2Source) {
			// System.out.println("das2source");
			Das2Source s = (Das2Source) source;
			Das2Capability[] caps = s.getDas2Capabilities();

			for (int i = 0; i < caps.length; i++) {
				Das2Capability cap = caps[i];
				// System.out.println("DasSourceWriterImpl: capability: " +
				// cap);
				xw.openTag("CAPABILITY");
				xw.attribute("type", cap.getCapability());
				xw.attribute("query_uri", cap.getQueryUri());
				xw.closeTag("CAPABILITY");
			}

		} else if (source instanceof Das1Source) {
			// System.out.println("das1source");
			List<String> capabilities = source.getCapabilities();
			String queryUriString="";
			String sourceUri="";
			for (int i = 0; i < capabilities.size(); i++) {
				String c = capabilities.get(i);
				xw.openTag("CAPABILITY");				
				xw.attribute("type", Das2CapabilityImpl.DAS1_CAPABILITY_PREFIX
						+ c);
				if(c.equals("sources")){
					sourceUri=source.getUrl();
					if(sourceUri.endsWith("/")){
				queryUriString=source.getUrl().substring(0,source.getUrl().lastIndexOf('/'));
					}else{
						queryUriString=source.getUrl();
					}
				}else{
				queryUriString=source.getUrl() + c;
				}
				xw.attribute("query_uri", queryUriString);
				xw.closeTag("CAPABILITY");
			}
			

		}
	

		String[] labels = source.getLabels();
		if (labels != null) {

			for (int i = 0; i < labels.length; i++) {

				xw.openTag("PROP");
				xw.attribute("name", Das2SourceHandler.LABELPROPERTY);
				xw.attribute("value", labels[i]);
				xw.closeTag("PROP");
			}

		}

		xw.openTag("PROP");
		xw.attribute("name", "leaseTime");

		xw.attribute("value", DasTimeFormat.toDASString(source.getLeaseDate()));
		xw.closeTag("PROP");

		Map<String, String> props = source.getProperties();

		Set<String> keys = props.keySet();
		for (String key : keys) {

			String prop = props.get(key);
			xw.openTag("PROP");
			xw.attribute("name", key);
			xw.attribute("value", prop);
			xw.closeTag("PROP");

		}
		
		String spec=source.getSpecification();
		if(spec!=null && spec!=""){
		xw.openTag("PROP");
		xw.attribute("name", "spec");
		xw.attribute("value", spec);
		xw.closeTag("PROP");
		}
		writeCapabilityStatus(xw, source, writeTimeData);
		
		int daysBeforeDeletion=timer.daysBeforeArchiving(source);
		//System.out.println(daysBeforeDeletion);
		if(daysBeforeDeletion!=60){
		xw.openTag("PROP");
		xw.attribute("name", "daysBeforeDeletion");
		xw.attribute("value", Integer.toString(daysBeforeDeletion));
		xw.closeTag("PROP");
		}
		xw.closeTag("VERSION");

		xw.closeTag("SOURCE");
	}

	protected void writeCapabilityStatus(XMLWriter xw, DasSource source, boolean times)
			throws IOException {
		//do nothing for a normal response
		//only implemented by the registry to give status of capabilities see RegistryDasSourceWriterImpl for method
	}

	public void writeDasSource(OutputStream stream, DasSource source)
			throws IOException {
		// System.out.println(source.getNickname());

		PrintWriter pw = new PrintWriter(stream);
		PrettyXMLWriter xw = new PrettyXMLWriter(pw);
		writeDasSource(xw, source, false);

	}

	public void writeDasSourceTypes(XMLWriter xw, String sourceIdentifier,
			List<Map<String, String>> typesInfo) throws IOException {
		System.err.println("called writeDasSourceTypes on DasSourceWriterImpl where method has not been written yet!");
		
	}

}
