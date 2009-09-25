package org.biojava.dasobert.das;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.biojava.dasobert.dasregistry.Das1Validator;

public enum Capabilities {

//	SEQUENCE("sequence"), STRUCTURE("structure"), ALIGNMENT("alignment"), TYPES(
//			"types"), FEATURES("features"), ENTRY_POINTS("entry_points"), STYLESHEET("stylesheet"), INTERACTION("interaction"), SOURCES(
//			"sources"),
//			ERROR_SEGMENT("error_segment"),UNKNOWN_SEGMENT("unknown_segment"),UNKNOWN_FEATURE("unknown_feature"), FEATURE_BY_ID("feature_by_id");

//	else if (capability.equals(Capabilities.SEQUENCE)
//			|| capability.equals(Capabilities.FEATURES)) {
//		cmd += capability + "?segment=" + testCode;
//		
//		
//	} else if (capability.equals(Capabilities.ALIGNMENT)
//			|| capability.equals(Capabilities.STRUCTURE)) {
//		cmd += capability + "?query=" + testCode;
//	} else if (capability.equals(Capabilities.ENTRY_POINTS)) {
//		cmd += capability;
//	} else if (capability.equals(Capabilities.STYLESHEET)) {
//		cmd += capability;
//	}  else if (capability.equals(Capabilities.TYPES)) {
//		cmd += capability;
//	} else if (capability.equals(Capabilities.INTERACTION)) {
//		cmd += capability + "?interactor=" + testCode;
//}else if (capability.equals(Capabilities.ERROR_SEGMENT)) {
//	cmd+="features?"+"segment="+Das1Validator.invalidTestCode;
//} else if (capability.equals(Capabilities.UNKNOWN_SEGMENT)) {
//	cmd+="features?"+"segment="+Das1Validator.invalidTestCode;
//} else if (capability.equals(Capabilities.UNKNOWN_FEATURE)) {
//	cmd+="features?feature_id=" + Das1Validator.invalidTestCode;
//} else if(capability.equals(Capabilities.MAXBINS)){
//	cmd += "features" + "?segment=" + testCode+";maxbins=1";
//}
	
	
	
	
	SOURCES("sources"){ public String getCommandString(String testCode) { return "sources"; } }
	,STYLESHEET("stylesheet"){ public String getCommandString(String testCode) { return getName(); } }
	,FEATURES("features"){ public String getCommandString(String testCode) { return getName()+"?segment=" + testCode; } }
	,TYPES(	"types"){ public String getCommandString(String testCode) { return getName(); } }
	,SEQUENCE("sequence"){ public String getCommandString(String testCode) { return getName()+"?segment=" + testCode; } }
	,ENTRY_POINTS("entry_points"){ public String getCommandString(String testCode) { return getName(); } }
	,ALIGNMENT("alignment"){ public String getCommandString(String testCode) { return getName()+"?query=" + testCode; } }
	,STRUCTURE("structure"){ public String getCommandString(String testCode) { return getName()+"?query=" + testCode; } }
	,INTERACTION("interaction"){ public String getCommandString(String testCode) { return getName() + "?interactor=" + testCode; } } 
	,UNKNOWN_SEGMENT("unknown_segment"){ public String getCommandString(String testCode) { return "features"+"?segment=" + Das1Validator.invalidTestCode; } }
	,UNKNOWN_FEATURE("unknown_feature"){ public String getCommandString(String testCode) { return "features"+"?segment=" + Das1Validator.invalidTestCode; } }
	,ERROR_SEGMENT("error_segment"){ public String getCommandString(String testCode) { return "features"+"?segment=" + Das1Validator.invalidTestCode; } }
	,MAXBINS("maxbins"){ public String getCommandString(String testCode) { return "features" + "?segment=" + testCode+";maxbins=1"; } }; //NEXT_FEATURE("next_feature");//FEATURE_BY_ID("feature_by_id"), GROUP_BY_ID("group_by_id")
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

	public abstract String getCommandString(String testCode);
	
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
	
	/**
	 * convenience method to see if all stated capabilities are contained in valid capabilities
	 * @param stated
	 * @param valid
	 * @return
	 */
	public static boolean allStatedAreValid(String []stated, String[] valid){
	List <String> notValidButStated=Capabilities.containsSubSet(stated,valid );
	return notValidButStated.size()==0 ? true : false;
	}

}
