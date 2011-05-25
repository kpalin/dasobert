package org.biojava.dasobert.das;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.biojava.dasobert.dasregistry.Das1Validator;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;

public enum Capabilities {

	SOURCES("sources"){ public String getCommandTestString(String testCode) { return "sources"; } }
	,STYLESHEET("stylesheet"){ public String getCommandTestString(String testCode) { return getName(); } }
	,FEATURES("features"){ public String getCommandTestString(String testCode) { return getName()+"?segment=" + testCode; } }
	,TYPES(	"types"){ public String getCommandTestString(String testCode) { return getName(); } }
	,SEQUENCE("sequence"){ public String getCommandTestString(String testCode) { return getName()+"?segment=" + testCode; } }
	,ENTRY_POINTS("entry_points"){ public String getCommandTestString(String testCode) { return getName(); } }
	,ALIGNMENT("alignment"){ public String getCommandTestString(String testCode) { return getName()+"?query=" + testCode; } }
	,STRUCTURE("structure"){ public String getCommandTestString(String testCode) { return getName()+"?query=" + testCode; } }
	,INTERACTION("interaction"){ public String getCommandTestString(String testCode) { return getName() + "?interactor=" + testCode; } } 
	,UNKNOWN_SEGMENT("unknown-segment","unknown_segment"){ public String getCommandTestString(String testCode) { return "features"+"?segment=" + Das1Validator.invalidTestCode+":1,1000"; } }
	,UNKNOWN_FEATURE("unknown-feature", "unknown_feature"){ public String getCommandTestString(String testCode) { return "features"+"?feature_id=" + Das1Validator.invalidTestCode; } }
	,ERROR_SEGMENT("error-segment","error_segment"){ public String getCommandTestString(String testCode) { return "features"+"?segment=" + Das1Validator.invalidTestCode+":1,1000"; } }
	,MAXBINS("maxbins"){ public String getCommandTestString(String testCode) { return "features" + "?segment=" + testCode+";maxbins=1"; }}
	,CORS("cors"){ public String getCommandTestString(String testCode) { return "any valid request"; }}
	,FEATURE_BY_ID("feature-by-id","feature_id"){ public String getCommandTestString(String testCode) { return "features"+"?feature_id="; }}
	,FORMAT("format"){ public String getCommandTestString(String testCode) { return "format"; }}
	,ADJACENT_FEATURE("adjacent-feature", "adjacent"){ public String getCommandTestString(String testCode) { return "adjacent"; }}//;
	,BIGFILE_BAM("bigfile-bam", "bigfile_bam"){ public String getCommandTestString(String testCode) { return ""; }}
	,BIGFILE_BIGBED("bigfile-bigbed", "bigfile_bigbed"){ public String getCommandTestString(String testCode) { return ""; }}
	,BIGFILE_BIGWIG("bigfile-bigwig", "bigfile_bigwig"){ public String getCommandTestString(String testCode) { return ""; }}; //NEXT_FEATURE("next_feature");//FEATURE_BY_ID("feature_by_id"), GROUP_BY_ID("group_by_id")

	
	private String name;//name is the lowercase name of the command usually but not necessarily the same as the cgi command string 
	private String command;//the actual command that needs to be added to the das source url
	
	
	//error_segments: Annotation servers should report unknown-segment and unknown-feature, and reference servers should indicate error-segment instead of unknown-segment.
	private static final Map<String, Capabilities> nameToValueMap =
        new HashMap<String, Capabilities>();
  static {
  for (Capabilities value : EnumSet.allOf(Capabilities.class)) {
      nameToValueMap.put(value.getName(), value);
  }
}
  
  private static final ArrayList<Capabilities> bigFileFormats= new ArrayList<Capabilities>();

  static{
	  bigFileFormats.add(BIGFILE_BAM);
	  bigFileFormats.add(BIGFILE_BIGBED);
	  bigFileFormats.add(BIGFILE_BIGWIG);
	  
  }
  
  public ArrayList<Capabilities> getBigFileFormats(){
	  return bigFileFormats;
  }
	
	

	Capabilities(String name) {
		this.name = name;
	}
	
	Capabilities(String name, String command){
		this.name=name;
		this.command=command;
	}

	public abstract String getCommandTestString(String testCode);
	
	public String getName() {
		return this.name;
	}

	public String getCommand(){
		if(command!=null){
			return this.command;
		}else{
			return this.getName();
		}
	
	}
	
	/**
	 * return commmands or query params for capabilities
	 * 
	 * @return
	 */
	public static String[] getCommandStrings() {

		ArrayList caps = new ArrayList();
		for (Capabilities value : EnumSet.allOf(Capabilities.class)) {
			caps.add(value.getCommand());
		}
		return (String[]) caps.toArray(new String[caps.size()]);

	}

	/**
	 * get the names of the capabilities
	 * @return
	 */
	public static String[] getCapabilityStrings() {
		ArrayList caps = new ArrayList();
		for (Capabilities value : EnumSet.allOf(Capabilities.class)) {
			caps.add(value.getName());
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
		//in most instances we want the command and not the name if we want the name call getName()
		return this.getCommand();
	}
	
	
	/**
	 * create a list of the enums represented by the strings
	 * @param list
	 * @return array of Capabilities
	 * @throws NoSuchCapabilityException - if no Capability found represented by a string[]
	 */
	public static Capabilities [] capabilitiesFromStrings  (List<String> list) {
		ArrayList <Capabilities>caps=new ArrayList<Capabilities>();
		for(int i=0;i<list.size();i++){
			if(nameToValueMap.containsKey(list.get(i))){
				caps.add(nameToValueMap.get(list.get(i)));
			}else{
				System.err.println("Warning a capability not found for  String "+list.get(i));
				 //throw new NoSuchCapabilityException(strings[i]);
				
			}
		
		}
		return caps.toArray(new Capabilities[caps.size()]);
	}
	
	public static List<Capabilities> capabilitiesListFromStrings  (List<String> list) {
		
		ArrayList <Capabilities>caps=new ArrayList<Capabilities>();
		if(list==null){
			System.out.println("Warning capabilities from Strings is passed no capabilities");
			return caps;
		}
		for(int i=0;i<list.size();i++){
			if(nameToValueMap.containsKey(list.get(i))){
				caps.add(nameToValueMap.get(list.get(i)));
			}else{
				System.err.println("Warning a capability not found for  String "+list.get(i));
				 try {
					throw new NoSuchCapabilityException(list.get(i));
				} catch (NoSuchCapabilityException e) {
					System.out.println("no such found capability "+e.getUnfoundCapability());
					e.printStackTrace();
				}
				
			}
		
		}
		return caps;
	}
	
	public static List<String> capabilitiesListFromStrings  (String[] list) {
		ArrayList <String>caps=new ArrayList<String>();
		for(int i=0;i<list.length;i++){
			caps.add(list[i]);
		
		}
		return caps;
	}
	
	public static List<String> capabilitiesAsStrings(Collection <Capabilities>capabilities){
		ArrayList <String>list=new ArrayList<String>();
		for(Capabilities cap:capabilities){
			list.add(cap.getName());
		}
		return list;
	}
	/**
	 * is this set of capabilities Strings contained fully in the superset of Strings
	 * @param list
	 * @param caps
	 * @return
	 */
	public static List<String> containsSubSet(List<String> list, List<String> caps){
		ArrayList <String> notValidButStated=new ArrayList<String>();
		for(String sub: list){
			boolean found=false;
			for(String superS:caps){
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
	public static boolean allStatedAreValid(List<String>stated, List<String> valid){
	List <String> notValidButStated=Capabilities.containsSubSet(stated,valid );
	return notValidButStated.size()==0 ? true : false;
	}

	
	public static Map capabilitiesToMap(Capabilities [] caps){
		Map<Capabilities,String> map =new HashMap<Capabilities,String>();
	  
	  for (Capabilities cap:caps) {
	      map.put(cap, "");
	  }
	  return map;
	  }


public static Capabilities getValue(String nameOfCapability){
	return nameToValueMap.get(nameOfCapability);
}



public static boolean isBigFileFormat(String name) {
	
	for(Capabilities cap: bigFileFormats){
		if(cap.getName().equals(name)){
			return true;
		}
	}
	return false;
}
}
