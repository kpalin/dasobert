package org.biojava.dasobert.das;

public enum CapabilityStatus {
	
	//invalid, probably valid or valid or optional
	INVALID, PROBABLY_VALID, VALID, OPTIONAL;
	
	public String toString(){
		return super.toString().toLowerCase();
	}
	

}
