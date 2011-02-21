package org.biojava.dasobert.das;


public enum DasSpec {

	SPEC1_53E("DAS/1.53E"), SPEC1_6E("DAS/1.6E");

	
	
	
	
	private String name;
	
	DasSpec(String name){
		this.name=name;
	}
	public static DasSpec convertToRegistrySpecification(String spec) {
		DasSpec convertedSpec = SPEC1_53E;
		if (spec.contains("6")) {
			convertedSpec = DasSpec.SPEC1_6E;
		}

		return convertedSpec;
	}
	
	
	
	public String toString(){
		return name;
	}

}
