package org.biojava.dasobert.das;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public enum Capabilities {

//	SEQUENCE("sequence"), STRUCTURE("structure"), ALIGNMENT("alignment"), TYPES(
//			"types"), FEATURES("features"), ENTRY_POINTS("entry_points"), STYLESHEET("stylesheet"), INTERACTION("interaction"), SOURCES(
//			"sources"),
//			ERROR_SEGMENT("error_segment"),UNKNOWN_SEGMENT("unknown_segment"),UNKNOWN_FEATURE("unknown_feature"), FEATURE_BY_ID("feature_by_id");

	
	
	SOURCES("sources"),STYLESHEET("stylesheet"),FEATURES("features"),TYPES(	"types"), SEQUENCE("sequence"),  ENTRY_POINTS("entry_points"),ALIGNMENT("alignment"),  STRUCTURE("structure"),   INTERACTION("interaction"), 
		UNKNOWN_SEGMENT("unknown_segment"),UNKNOWN_FEATURE("unknown_feature"), ERROR_SEGMENT("error_segment"),  MAXBINS("maxbins"), NEXT_FEATURE("next_feature");//FEATURE_BY_ID("feature_by_id"), GROUP_BY_ID("group_by_id")
//error_segments: Annotation servers should report unknown-segment and unknown-feature, and reference servers should indicate error-segment instead of unknown-segment.
	private static final Map<String, Capabilities> nameToValueMap =
        new HashMap<String, Capabilities>();
  static {
  for (Capabilities value : EnumSet.allOf(Capabilities.class)) {
      nameToValueMap.put(value.toString(), value);
  }
}

	private String name;//name is the lowercase name of the command usually but not necessarily the same as the cgi command string 
	private String command;//the actual command that needs to be added to the das source url
	

	Capabilities(String name) {
		this.name = name;
	}
	
	Capabilities(String name, String command){
		this.name=name;
		this.command=command;
	}

	public String getName() {
		return this.name;
	}

	public String getCommand(){
		return this.command;
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
		return (String[]) caps.toArray(new String[caps.size()]);

	}

	/**
	 * test if a capability exists that is represented by this string
	 * @param capability
	 * @return
	 */
	public static boolean exists(String capability) {
		if(nameToValueMap.containsKey(capability))return true;
		return false;
	}

	public String toString() {
		return name;
	}
	
	
	/**
	 * create a list of the enums represented by the strings
	 * @param strings
	 * @return array of Capabilities
	 * @throws NoSuchCapabilityException - if no Capability found represented by a string[]
	 */
	public static Capabilities [] capabilitiesFromStrings  (String[] strings) {
		ArrayList <Capabilities>caps=new ArrayList<Capabilities>();
		for(int i=0;i<strings.length;i++){
			if(nameToValueMap.containsKey(strings[i])){
				caps.add(nameToValueMap.get(strings[i]));
			}else{
				System.err.println("Warning a capability not found for  String "+strings[i]);
				 //throw new NoSuchCapabilityException(strings[i]);
				
			}
		
		}
		return caps.toArray(new Capabilities[caps.size()]);
	}
	
	public static String[] capabilitiesAsStrings(Collection <Capabilities>capabilities){
		ArrayList <String>list=new ArrayList<String>();
		for(Capabilities cap:capabilities){
			list.add(cap.toString());
		}
		return list.toArray(new String[list.size()]);
	}
	/**
	 * is this set of capabilities Strings contained fully in the superset of Strings
	 * @param stated
	 * @param valid
	 * @return
	 */
	public static List<String> containsSubSet(String [] stated, String[] valid){
		ArrayList <String> notValidButStated=new ArrayList<String>();
		for(String sub: stated){
			boolean found=false;
			for(String superS:valid){
				if(sub.equals(superS)){
					found=true;
				}
			}
			if(!found)notValidButStated.add(sub);
		}
		return notValidButStated;
	}

	public static void main(String[] args) {
		for (Capabilities cap : Capabilities.values()) {
			System.out.println(cap.toString());
		}
		
		if(Capabilities.SEQUENCE.equals(Capabilities.SEQUENCE.toString()))System.out.println("is true");
	}
	

}
