package org.biojava.dasobert.dasregistry;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.das.CapabilityStatus;

import com.mysql.jdbc.ResultSet;

/**
 * should contain a Capabilities and a boolean for whether strictly valid or not
 * and an error message if applicable
 * 
 * @author jw12
 * 
 */
public class ValidationResultLights {
	public DasValidationResult result;
	Capabilities[] statedCaps;
	Map<Capabilities, String> statedCapsMap;
	ArrayList<LightBean> beans;
	String errors="";//hack here to store any validation errors apart from those related to any specific capability

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	private boolean overAllValid = false;

	public boolean isOverAllValid() {
		return overAllValid;
	}

	public void setOverAllValid(boolean overAllValid) {
		this.overAllValid = overAllValid;
	}

	public ValidationResultLights(DasValidationResult result,
			Capabilities[] statedCapabilities) {
		this.result = result;
		overAllValid = result.isOverallValid();
		this.statedCaps = statedCapabilities;
		this.statedCapsMap = Capabilities.capabilitiesToMap(statedCaps);

	}

	/**
	 * to show lights in listServices pages where no results object is created
	 * 
	 * @param ds
	 * @param testcode
	 */
	public ValidationResultLights(DasSource ds) {
		result = new DasValidationResult(ds.getUrl(),
				ds.getCoordinateSystem());
		this.statedCaps = Capabilities.capabilitiesFromStrings(ds
				.getCapabilities());
		this.statedCapsMap = Capabilities.capabilitiesToMap(statedCaps);
		List<String> validCaps = ds.getValidCapabilities();
		Map valid = Capabilities.capabilitiesToMap(Capabilities
				.capabilitiesFromStrings(ds.getValidCapabilities()));
		for (Capabilities allCap : EnumSet.allOf(Capabilities.class)) {
			if (valid.containsKey(allCap)) {
				result.isValid(allCap, true);
			} else {
				result.isValid(allCap, false);
			}

		}
		result.setSpecification(ds.getSpecification());
		//result.print();

	}
	
	public ValidationResultLights(){
		
	}

	public List<LightBean> getLightsLinksAndResults() {
		ArrayList<LightBean> beans = new ArrayList<LightBean>();
		//result.print();
		//System.out.println("starting getLightsLinksAndResults()");
		// loop over all capabilities and determine if invalid, probably valid
		// or valid or optional.
		for (Capabilities allCap : EnumSet.allOf(Capabilities.class)) {
			LightBean bean = new LightBean(allCap);
			bean.setErrorMessage(result.getError(allCap));
			LightBean withStatusAndMessage = getCapabilityStatus(allCap, bean);
			LightBean beanWithCmd = getCmdString(allCap, withStatusAndMessage);
			beans.add(beanWithCmd);

		}
		//System.out.println("ending getLightsLinksAndResults()");
		this.beans=beans;
		return beans;

	}

	private LightBean getCmdString(Capabilities capability, LightBean bean) {
		String cmd = result.getUrl();
		if (capability.equals(Capabilities.SOURCES)) {
			if (cmd.endsWith("/")) {
				// System.out.println("ends with /");
				cmd = cmd.substring(0, cmd.length() - 1);
				// System.out.println("after -1=" + cmd);

			}
			// now remove the datasource name at the end of the url
			String choppedURL = cmd.substring(0, cmd.lastIndexOf("/") + 1);
			// System.out.println("chopped " + choppedURL);
			cmd = choppedURL + capability;
		}

		else {
			String testCode="";
			DasCoordinateSystem[] coords = result.getCoords();
			if (coords.length>=1) {
				for (DasCoordinateSystem coord : coords) {
					testCode = coord.getTestCode();
					break;//currently only displaying 1 testcode from one coordinate system
				}
			}
			cmd += capability.getCommandTestString(testCode);
		}
		bean.setCmd(cmd);
		return bean;
	}

	/**
	 * set the capability status of the capability and return a bean with everything set
	 * @param allCap
	 * @param bean
	 * @return
	 */
	private LightBean getCapabilityStatus(Capabilities allCap, LightBean bean) {
		CapabilityStatus status = CapabilityStatus.OPTIONAL;// lets assume
		// optional as most
		// are.
		if (result.isValid(allCap)) {
			// if is valid then is valid whether independant of anything else
			if (statedCapsMap.containsKey(allCap)) {
				bean.setStatus(CapabilityStatus.VALID);
				bean.setMessageHeader(" capability is valid.");
				bean
						.setMessageBody(" is present and valid for this server- click to see the response.");
			} else {
				bean.setStatus(CapabilityStatus.PROBABLY_VALID);
				bean
						.setMessageHeader(" capability is possibly valid, treat with caution!!");
				bean
						.setMessageBody(" possibly valid, but has not been stated by the data owner. (The owner could probably make this valid with ease).");
			}
			// but if it is not stated it is then possibly valid and should be
			// displayed as such
		} else {
			// if not valid then it's either not valid or clear as optional
			// only non-optional is sources
			if (allCap.equals(Capabilities.SOURCES)) {
				// should be red
				status = CapabilityStatus.INVALID;
				bean.setStatus(status);
				bean.setMessageHeader(" capability not valid.");
				if (statedCapsMap.containsKey(allCap)) {
					bean
							.setMessageBody(" capability is stated for this server but is not valid or provided.");
				} else {
					bean
							.setMessageBody(" capability is highly recommended for this server but is not valid or provided.");
				}
			} else {
				// test the optional capability and see if it's clear or red
				bean = testFailedOptionalCapability(allCap, bean);

				return bean;
			}

		}
		return bean;
	}

	/**
	 * What is the status of this capability for this source
	 * @param cap
	 * @return status
	 */
	public CapabilityStatus getStatus(Capabilities cap){
		CapabilityStatus status = null;
		for(LightBean bean:beans){
			if(bean.getCapability().equals(cap)){
				bean.getStatus();
				status =bean.getStatus();
			}
			
			
		}
		return status;
	}
	/**
	 * 
	 * @param allCap
	 * @return false if not truly optional as some other rule here dictates it
	 *         should be a valid capability for this server
	 */
	private LightBean testFailedOptionalCapability(Capabilities allCap,
			LightBean bean) {
		// return true if clear false if red

		if (statedCapsMap.containsKey(allCap)) {
			bean.setStatus(CapabilityStatus.INVALID);
			bean.setMessageHeader(" capability not valid.");
			bean
					.setMessageBody(" capability was stated for this server but is either not present or valid.");
			return bean;
		} else
		// unless capability is types and has feautes stated then it
		// should be red
		if ((allCap.equals(Capabilities.TYPES) || allCap
				.equals(Capabilities.FEATURES))
				&& statedCapsMap.containsKey(Capabilities.FEATURES)) {
			bean.setMessageHeader(" capability not valid.");
			bean
					.setMessageBody(" capability is highly recommended for this server as it implements features");
			bean.setStatus(CapabilityStatus.INVALID);
			return bean;
		} else
		// unless capability is entry_points and sequence capability
		// has been stated
		if ((allCap.equals(Capabilities.ENTRY_POINTS) || allCap
				.equals(Capabilities.SEQUENCE))
				&& statedCapsMap.containsKey(Capabilities.SEQUENCE)) {
			bean.setMessageHeader(" capability not valid.");
			bean
					.setMessageBody(" capability for this server is highly recommended as it implements sequence");
			bean.setStatus(CapabilityStatus.INVALID);
			return bean;
		}
		// if non of these rules apply then return optional
		bean.setMessageHeader(" capability not in use.");
		bean.setMessageBody(" not needed for this server.");
		bean.setStatus(CapabilityStatus.OPTIONAL);
		return bean;

	}
	
	public Map<Capabilities, String> getStatedCapsMap() {
		return statedCapsMap;
	}

	public void setStatedCapsMap(Map<Capabilities, String> statedCapsMap) {
		this.statedCapsMap = statedCapsMap;
	}
	public String getSpecification(){
		return result.getSpecification();
	}

}
