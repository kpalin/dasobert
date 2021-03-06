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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.biojava.dasobert.das.CapabilityStatus;
import org.biojava.dasobert.das.DasTimeFormat;
import org.biojava.dasobert.das2.Das2Capability;
import org.biojava.dasobert.das2.Das2CapabilityImpl;
import org.biojava.dasobert.das2.Das2Source;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.DasCoordinateSystem;
import org.biojava.dasobert.dasregistry.DasSource;
import org.biojava.dasobert.dasregistry.LightBean;
import org.biojava.dasobert.dasregistry.ValidationResultLights;
import org.biojava.utils.xml.PrettyXMLWriter;
import org.biojava.utils.xml.XMLWriter;

/**
 * class for writing a response suitable for indexing using the EBEye/Sanger
 * indexing system.
 * 
 * @author jw12
 * 
 */
public class DasSourceIndexWriterImpl implements DasSourceWriter {

	public static final String COORDSYSURI = "http://www.dasregistry.org/dasregistry/coordsys/";

	public DasSourceIndexWriterImpl() {
		super();

	}

	public void writeCoordinateSystem(XMLWriter xw, DasCoordinateSystem co)
			throws IOException {
		// xw.openTag("field");
		String uri = co.getUniqueId();
		if (!uri.startsWith(COORDSYSURI))
			uri = COORDSYSURI + uri;

		// xw.attribute("name","uri");
		// xw.print(uri);
		writeAdditionalField(xw, "coordinates_uri", uri);

		int taxid = co.getNCBITaxId();
		if (taxid != 0) {
			// xw.openTag("field");
			// xw.attribute("name","taxid");
			// xw.print(taxid +"" );
			// xw.closeTag("field");
			writeAdditionalField(xw, "coordinates_taxid", String.valueOf(taxid));
		}

		// xw.attribute("source",co.getCategory());
		writeAdditionalField(xw, "coordinates_source", co.getCategory());
		// xw.attribute("authority",co.getName());
		writeAdditionalField(xw, "coordinates_authority", co.getName());
		// xw.attribute("test_range",co.getTestCode());
		writeAdditionalField(xw, "coordinates_test_range", co.getTestCode());
		// TODO: get version from name;
		String version = co.getVersion();
		if ((version != null) && (!version.equals("")))
			writeAdditionalField(xw, "coordinates_version", version);
		// xw.print(co.toString());
		writeAdditionalField(xw, "coordinates", co.toString());
		// xw.closeTag("COORDINATES");
	}

	public void writeDasSource(XMLWriter xw, DasSource source, boolean times) {
		try {
			xw.openTag("entry");

			// System.out.println("DasSourceWriterImpl:  writing new source");

			xw.attribute("id", source.getId());
			xw.attribute("acc", source.getUrl());// maybe this should be a field
			// url?
			xw.openTag("name");
			xw.print(source.getNickname());
			xw.closeTag("name");
			xw.openTag("description");
			String desc = source.getDescription();
			desc = desc.replaceAll("\n", " ");
			desc = desc.replaceAll("\r", " ");
			xw.print(desc);
			xw.closeTag("description");
			xw.openTag("additional_fields");
			Date d = source.getRegisterDate();
			if (d == null)
				d = new Date();

			// xw.closeTag("field");
			writeAdditionalField(xw, "created", DasTimeFormat.toDASString(d));
			// System.out.println("before coords");
			DasCoordinateSystem[] coords = source.getCoordinateSystem();

			for (int i = 0; i < coords.length; i++) {
				DasCoordinateSystem co = coords[i];
				writeCoordinateSystem(xw, co);

			}
			if (source instanceof Das1Source) {
				// System.out.println("das1source");
				List<String> capabilities = source.getCapabilities();
				for (int i = 0; i < capabilities.size(); i++) {
					String c = capabilities.get(i);
					writeAdditionalField(xw, "capability_type",
							Das2CapabilityImpl.DAS1_CAPABILITY_PREFIX + c);
					writeAdditionalField(xw, "capability_query_uri", source
							.getUrl()
							+ c);
					// xw.closeTag("CAPABILITY");
				}
				// show validated capabilities and probably valid capabilities
				// ie ones
				// that by autovalidation are shown to be valid but
				// have not been stated as so by the user
				
					ValidationResultLights results = new ValidationResultLights(
							source);
					List<LightBean> beans = results.getLightsLinksAndResults();
					for (LightBean bean : beans) {
						CapabilityStatus status = bean.getStatus();
						if(!status.equals(CapabilityStatus.OPTIONAL)){
						writeAdditionalField(xw, bean.getCapability()
								.toString(), status.toString());
						}

					}
				

			}

			String[] labels = source.getLabels();
			if (labels != null) {

				for (int i = 0; i < labels.length; i++) {

					writeAdditionalField(xw, "source_label", labels[i]);
				}

			}
			// change these for additional fields under our other additional
			// fields

			writeAdditionalField(xw, "leaseTime", DasTimeFormat
					.toDASString(source.getLeaseDate()));

			Map<String, String> props = source.getProperties();

			Set<String> keys = props.keySet();
			for (String key : keys) {

				String prop = props.get(key);

				writeAdditionalField(xw, key, prop);

			}

			// xw.closeTag("VERSION");

			// xw.closeTag("SOURCE");
			xw.closeTag("additional_fields");
			xw.closeTag("entry");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeDasSource(OutputStream stream, DasSource source, boolean times)
			throws IOException {
		// System.out.println(source.getNickname());

		PrintWriter pw = new PrintWriter(stream);
		PrettyXMLWriter xw = new PrettyXMLWriter(pw);
		writeDasSource(xw, source, false);

	}

	public void writeAdditionalField(XMLWriter xw, String fieldName,
			String fieldContent) throws IOException {

		xw.openTag("field");
		xw.attribute("name", fieldName);
		xw.print(fieldContent);
		xw.closeTag("field");

	}

	public void writeDasSourceTypes(XMLWriter xw, String sourceIdentifier,
			List<Map<String, String>> typesInfo) throws IOException {
		xw.openTag("entry");
		xw.attribute("id", sourceIdentifier);
		
		HashSet previousTypeIds=new HashSet<String>();
		HashSet previousCategory=new HashSet<String>();
		HashSet previousCvId=new HashSet<String>();
		xw.openTag("additional_fields");
		for (Map<String, String> type : typesInfo) {
			String id = type.get("id");
			String cvId = type.get("cvId");
			String category = type.get("category");
			// String number=type.get("number");
			if (id != null && id != "") {
				if(!previousTypeIds.contains(id)){
				this.writeAdditionalField(xw, "type_id", id);
				previousTypeIds.add(id);
				}
			}
			if (cvId != null && cvId != "") {
				if(previousCvId.contains(cvId)){
				this.writeAdditionalField(xw, "cvId", cvId);
				previousCvId.add(cvId);
				}
			}
			if (category != null && category != "") {
				if(!previousCategory.contains(category)){
				this.writeAdditionalField(xw, "category", category);
				previousCategory.add(category);
				}
				
				
			}
			}

		xw.closeTag("additional_fields");
		xw.closeTag("entry");

	}

	public void writeDasSource(OutputStream stream, DasSource source)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	

}
