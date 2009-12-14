package org.biojava.dasobert.das;


public enum DasSpec {

	SPEC1_5, SPEC1_6;

	public static DasSpec convertToRegistrySpecification(String spec) {
		DasSpec convertedSpec = SPEC1_5;
		if (spec.contains("6")) {
			convertedSpec = DasSpec.SPEC1_6;
		}

		return convertedSpec;
	}

}
