package de.mpg.mpiinf.ag3.dasmi.model;

import de.mpg.mpiinf.ag3.dasmi.Constants;

/**
 * Specifies a range for a positional feature, such as a binding site
 * 
 * @author Hagen Blankenburg
 *
 */
public class Range {
	
	private int start = Constants.INVALID_INT;
	private int end = Constants.INVALID_INT;
	private String startStatus = null;
	private String endStatus = null;
	private String startStatusCvId = null;
	private String endStatusCvId = null;

	/**
	 * Basic initialization
	 *
	 */
	public Range(){
		this.startStatus = Constants.INVALID_STRING;
		this.startStatusCvId = Constants.INVALID_STRING;
		this.endStatus = Constants.INVALID_STRING;
		this.endStatusCvId = Constants.INVALID_STRING;
	}
	
	/**
	 * Basic initialization
	 * @param start start of the range
	 * @param end end of the range
	 */
	public Range(int start, int end){
		this.start = start;
		this.end = end;
		this.startStatus = Constants.INVALID_STRING;
		this.startStatusCvId = Constants.INVALID_STRING;
		this.endStatus = Constants.INVALID_STRING;
		this.endStatusCvId = Constants.INVALID_STRING;
	}
	

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}

	/**
	 * @return the endStatus
	 */
	public String getEndStatus() {
		return endStatus;
	}

	/**
	 * @param endStatus the endStatus to set
	 */
	public void setEndStatus(String endStatus) {
		this.endStatus = endStatus;
	}

	/**
	 * @return the endStatusCvId
	 */
	public String getEndStatusCvId() {
		return endStatusCvId;
	}

	/**
	 * @param endStatusCvId the endStatusCvId to set
	 */
	public void setEndStatusCvId(String endStatusCvId) {
		this.endStatusCvId = endStatusCvId;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the startStatus
	 */
	public String getStartStatus() {
		return startStatus;
	}

	/**
	 * @param startStatus the startStatus to set
	 */
	public void setStartStatus(String startStatus) {
		this.startStatus = startStatus;
	}

	/**
	 * @return the startStatusCvId
	 */
	public String getStartStatusCvId() {
		return startStatusCvId;
	}

	/**
	 * @param startStatusCvId the startStatusCvId to set
	 */
	public void setStartStatusCvId(String startStatusCvId) {
		this.startStatusCvId = startStatusCvId;
	}

	
	/**
	 * Basic output
	 * @return the string
	 */
	public String toString(){
		String help = "Start: " +  start +
					" - StartStatus: " + startStatus + 
					" - End: " + end +
					" - EndStatus: " + endStatus;
		return help;
	}
	
	

}
