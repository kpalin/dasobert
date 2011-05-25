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
 * Created on Dec 8, 2006
 * 
 */

package org.biojava.dasobert.dasregistry;

import java.util.ArrayList;
import java.util.List;

import org.biojava.dasobert.das.DasSpec;

/**
 * a class to filter a set of DAS sources, or to check if single DAS sources
 * fulfill certain requirements
 * 
 * 
 * @author Andreas Prlic
 * 
 */
public class DasSourceFilter {

	public boolean hasAuthority(DasSource source, String authority) {
		if (authority == null)
			return true;
		if (authority.equals(""))
			return true;

		DasCoordinateSystem[] coords = source.getCoordinateSystem();
		for (int j = 0; j < coords.length; j++) {
			DasCoordinateSystem cs = coords[j];
			if (authority.equalsIgnoreCase(cs.getName())) {
				return true;
			}
		}

		return false;
	}

	public boolean hasCapability(DasSource source, String capability) {

		if (capability == null)
			return true;
		if (capability.equals(""))
			return true;

		return source.hasCapability(capability);
	}
	
	public boolean hasValidCapability(DasSource source, String capability) {

		if (capability == null)
			return true;
		if (capability.equals(""))
			return true;

		return source.hasValidCapability(capability);
	}

	public boolean hasLabel(DasSource source, String label) {
		if (label == null)
			return true;
		if (label.equals(""))
			return true;

		String[] labels = source.getLabels();
		for (int j = 0; j < labels.length; j++) {
			String l = labels[j];
			if (l.equalsIgnoreCase(label))
				return true;
		}
		return false;
	}

	public boolean hasType(DasSource source, String type) {
		if (type == null)
			return true;
		if (type.equals(""))
			return true;

		DasCoordinateSystem[] coords = source.getCoordinateSystem();
		for (int j = 0; j < coords.length; j++) {
			DasCoordinateSystem cs = coords[j];
			if (type.equalsIgnoreCase(cs.getCategory())) {
				return true;
			}
		}

		return false;
	}

	public boolean hasOrganism(DasSource source, String organism) {
		// test for correct organism
		if (organism == null)
			return true;
		if (organism.equals(""))
			return true;

		DasCoordinateSystem[] coords = source.getCoordinateSystem();
		for (int j = 0; j < coords.length; j++) {
			DasCoordinateSystem cs = coords[j];
			//System.out.println(cs.getOrganismName()+" "+organism);
			if ((organism.equalsIgnoreCase(cs.getOrganismName()))
					|| (organism.equalsIgnoreCase(cs.getNCBITaxId() + ""))) {
				return true;
			}
		}
		return false;
	}

	public boolean isSpec(DasSource source, DasSpec spec) {
		if (spec == null)
			return true;
		
		if (DasSpec.convertToRegistrySpecification(source.getSpecification()).equals(spec)) {
			return true;
		}
//System.out.println("returning false:"+source.getUrl());
		return false;
	}

	/**
	 * filter a set of DasSources by particular requirements all arguments can
	 * be set to null which means they are ignored
	 * 
	 * @param sources
	 * @param label
	 * @param organism
	 * @param authority
	 * @param capability
	 * @param type
	 * @param spec TODO
	 * @param version TODO
	 * @return an array of DasSources that match the requested filtering rules
	 */
	public List<DasSource> filterBy(List<DasSource> sources, String label,
			String organism, String authority, String capability, String type, DasSpec spec, String version, String id) {
//cant return sources here as it then gets re-ordered so we need a copy
//		if ((label == null) && (organism == null) && (authority == null)
//				&& (capability == null) && (type == null) && (spec==null))
//			return sources;

		List lst = new ArrayList();
		for (int i = 0; i < sources.size(); i++) {
			DasSource source = sources.get(i);
//if(source.getNickname().equals("Bamtestagain")){
			// test for correct label
			if (hasLabel(source, label) && hasOrganism(source, organism)
					&& hasAuthority(source, authority)
					&& hasValidCapability(source, capability)
					&& hasType(source, type)
					&& isSpec(source, spec) && hasVersion(source,version)
					&& isCoordinateId(source, id)) {
				lst.add(source);
			}
			//}
		}

		return lst;

	}

	private boolean hasVersion(DasSource source, String version) {
		if (version == null)
			return true;
		if (version.equals(""))
			return true;

		DasCoordinateSystem[] coords = source.getCoordinateSystem();
		for (int j = 0; j < coords.length; j++) {
			DasCoordinateSystem cs = coords[j];
			if ((version.equalsIgnoreCase(cs.getVersion()))) {
				return true;
			}
		}
		return false;
	}

	
	private boolean isUCSC(DasSource source){
		if(source.getId().startsWith("UCSC_"))return true;
		return false;
	}
	
	private boolean isCoordinateId(DasSource source, String id){
		
		if (id == null)
			return true;
		if (id.equals(""))
			return true;
		
		//accept uris or ids
		if(id.contains("/")){
			id=id.substring(id.lastIndexOf("/")+1, id.length());
			}
				
		DasCoordinateSystem[] coords = source.getCoordinateSystem();
		for (int j = 0; j < coords.length; j++) {
			DasCoordinateSystem cs = coords[j];
			//System.out.println("coord id="+cs.getUniqueId());
			if (id.equalsIgnoreCase(cs.getUniqueId())) {
				//System.out.println("match found");
				return true;
			}
		}
		return false;
		
	}
}
