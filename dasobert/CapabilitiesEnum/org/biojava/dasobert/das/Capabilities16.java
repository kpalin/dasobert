package org.biojava.dasobert.das;

public enum Capabilities16 {
	
		SEQUENCE("sequence"),
		STRUCTURE("structure"),
		ALIGNMENT("alignment"),
		TYPES("types"),
		FEATURES("features"),
		ENTRY_POINTS("entry_points"),
		STYLESHEET("stylesheet"),
		INTERACTION("interaction"),
		SOURCES("sources");
	
	private String name;
	
	Capabilities16(String name){
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static Capabilities16 [] getCapabilitesInCoreOrder(){
		Capabilities16 []capabilities={SOURCES,STYLESHEET, FEATURES,TYPES,
				SEQUENCE,ENTRY_POINTS, ALIGNMENT, STRUCTURE, INTERACTION};
		return capabilities;
		}
	
	/**
	 * return a subset of the capabilities as not all capabilities are DAS commands
	 * @return
	 */
	public Capabilities16[] getCommands(){
		
		return new Capabilities16[]{SEQUENCE,SEQUENCE,
				STRUCTURE,
				ALIGNMENT,
				TYPES,
				FEATURES,
				ENTRY_POINTS,
				STYLESHEET,
				INTERACTION,
				SOURCES};
		
	}
	
	public static void main(String[] args){
		for( Capabilities16 cap : Capabilities16.values()){
			System.out.println(cap.toString());
		}
	}
}


	

