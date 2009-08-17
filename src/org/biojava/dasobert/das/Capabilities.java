package org.biojava.dasobert.das;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Capabilities {

	SEQUENCE("sequence"), STRUCTURE("structure"), ALIGNMENT("alignment"), TYPES(
			"types"), FEATURES("features"), ENTRY_POINTS("entry_points"), STYLESHEET("stylesheet"), INTERACTION("interaction"), SOURCES(
			"sources");

	private static final Map<String, Capabilities> nameToValueMap =
        new HashMap<String, Capabilities>();
	private static final ArrayList <Capabilities> capabilitiesInCoreOrder=new ArrayList<Capabilities>();
	private static final ArrayList <String> capabilitiesStringsInCoreOrder=new ArrayList<String>();

    static {
        for (Capabilities value : EnumSet.allOf(Capabilities.class)) {
            nameToValueMap.put(value.toString(), value);
        }
    }
    static{
    	capabilitiesInCoreOrder.add(SOURCES);
    	capabilitiesInCoreOrder.add(STYLESHEET);
    	capabilitiesInCoreOrder.add(FEATURES);
    	capabilitiesInCoreOrder.add(TYPES);
    	capabilitiesInCoreOrder.add(SEQUENCE);
				capabilitiesInCoreOrder.add(ENTRY_POINTS);
				capabilitiesInCoreOrder.add(ALIGNMENT);
				capabilitiesInCoreOrder.add(STRUCTURE);
				capabilitiesInCoreOrder.add(INTERACTION);
    }
	static{
		for(Capabilities cap:EnumSet.allOf(Capabilities.class)){
			capabilitiesStringsInCoreOrder.add(cap.toString());
		}
	}
	private String name;
	

	Capabilities(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}


	
	/**
	 * return a subset of the capabilities as not all capabilities are DAS
	 * commands
	 * 
	 * @return
	 */
	public static String[] getCommandStrings() {

		return new String[] { SEQUENCE.toString(), STRUCTURE.toString(),
				ALIGNMENT.toString(), TYPES.toString(), FEATURES.toString(),
				ENTRY_POINTS.toString(), STYLESHEET.toString(),
				INTERACTION.toString(), SOURCES.toString() };

	}

	public static String[] getCapabilityStrings() {
		ArrayList caps = new ArrayList();
		for (Capabilities cap : Capabilities.values()) {
			caps.add(cap.toString());
		}
		return (String[]) caps.toArray();

	}

	public static boolean exists(String capability) {
		nameToValueMap.containsKey(capability);
		return false;
	}

	public String toString() {
		return name;
	}
	
	public static ArrayList<String> getCapabilityStringsInCoreOrder(){
		return capabilitiesStringsInCoreOrder;
	}
	public static ArrayList<Capabilities> getCapabilitiesInCoreOrder(){
		return capabilitiesInCoreOrder;
	}

	public static void main(String[] args) {
		for (Capabilities cap : Capabilities.values()) {
			System.out.println(cap.toString());
		}
	}
}
