package org.biojava.dasobert.das;

public class NoSuchCapabilityException extends Exception {
	String unfoundCapability;
	public NoSuchCapabilityException(String string){
		this.unfoundCapability=string;
		
	}

	public String getUnfoundCapability(){
		return unfoundCapability;
	}
}
