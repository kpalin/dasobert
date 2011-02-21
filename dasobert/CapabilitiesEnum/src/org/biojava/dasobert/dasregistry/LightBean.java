package org.biojava.dasobert.dasregistry;

import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.das.CapabilityStatus;

/**
 * store the img header and body for each capability returned to
 * listServices.jsp
 * 
 * @author jw12
 * 
 */
public class LightBean {

	private final String red = "img/red16.gif";
	private final String amber = "img/amber16.gif";
	private final String green = "img/green16.gif";
	private final String clear = "img/clear16.gif";
	private String messageHeader = "messageHeader not set";
	private String messageBody = "messageBody not set";
	private String cmd = "cmd not set";
	private String errorMessage="no errors";
	private Capabilities capability;
	private CapabilityStatus status;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	

	public LightBean() {

	}

	public Capabilities getCapability() {
		return capability;
	}

	public void setCapability(Capabilities capability) {
		this.capability = capability;
	}

	public LightBean(Capabilities capability, String messageHeader,
			String messageBody, String cmd) {
		this.capability = capability;
		this.messageHeader = messageHeader;
		this.messageBody = messageBody;
		this.cmd = cmd;

	}

	public LightBean(Capabilities cap) {
		this.capability = cap;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getImg() {
		String img = "img not set";
		if (status.equals(CapabilityStatus.INVALID)) {
			img = red;
		} else if (status.equals(CapabilityStatus.PROBABLY_VALID)) {
			img = amber;
		} else if (status.equals(CapabilityStatus.VALID)) {
			img = green;
		} else if (status.equals(CapabilityStatus.OPTIONAL)) {
			img = clear;
		}
		return img;
	}
	
	

	public String getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(String messageHeader) {
		this.messageHeader = messageHeader;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public void setStatus(CapabilityStatus status) {
		this.status = status;

	}
	public CapabilityStatus getStatus() {
		return this.status;

	}

	public boolean getVisible() {
		if (this.status.equals(CapabilityStatus.OPTIONAL)) {
			return false;
		} else {
			return true;
		}
	}

}
