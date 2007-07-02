package de.mpg.mpiinf.ag3.dasmi;

/**
 * Constants used by the the web interface and the dasobert parsing method
 * @author Hagen
 *
 */
public class Constants {
	
	public static final int INVALID_INT = -1;
	public static final String INVALID_STRING = "";
	
	// the names of all the sql tables
	public static final String TABLE_INTERACTOR = "INTERACTOR";
	public static final String TABLE_INTERACTION = "INTERACTION";
	public static final String TABLE_DETAIL = "DETAIL";
	public static final String TABLE_RANGE = "RANGE";
	public static final String TABLE_INTERACTION_DETAIL = "INTERACTION_DETAIL";
	public static final String TABLE_INTERACTOR_DETAIL = "INTERACTOR_DETAIL";
	public static final String TABLE_DETAIL_RANGE = "DETAIL_RANGE";
	public static final String TABLE_INTERACTION_INTERACTOR = "INTERACTION_INTERACTOR";
	public static final String TABLE_INTERACTION_INTERACTOR_DETAIL = "INTERACTION_INTERACTOR_DETAIL";
	
	// the names of the primary keys of the main tables (not the linking ones)
	public static final String TABLE_ID_INTERACTOR = "INTERACTOR_ID";
	public static final String TABLE_ID_INTERACTION = "INTERACTION_ID";
	public static final String TABLE_ID_INTERACTORREF = "INTERACTORREF_ID";
	public static final String TABLE_ID_DETAIL = "DETAIL_ID";
	public static final String TABLE_ID_RANGE = "RANGE_ID";
	public static final String TABLE_ID_INTERACTION_INTERACTOR = "INTERACTION_INTERACTOR_ID";
		
	public static final String TABLE_FIELD_NAME = "NAME";
	public static final String TABLE_FIELD_DBSOURCE = "DBSOURCE";
	public static final String TABLE_FIELD_DBSOURCECVID = "DBSOURCECVID";
	public static final String TABLE_FIELD_DBVERSION = "DBVERSION";
	public static final String TABLE_FIELD_DBACCESSIONID = "DBACCESSIONID";
	public static final String TABLE_FIELD_DBCOORDSYS = "DBCOORDSYS";
	public static final String TABLE_FIELD_SEQUENCE = "SEQ";
	public static final String TABLE_FIELD_SEQUENCESTART = "SEQSTART";
	public static final String TABLE_FIELD_SEQUENCEEND = "SEQEND";
	public static final String TABLE_FIELD_PROPERTY = "PROPERTY";
	public static final String TABLE_FIELD_PROPERTYCVID = "PROPERTYCVID";
	public static final String TABLE_FIELD_VALUE = "VAL";
	public static final String TABLE_FIELD_VALUECVID = "VALCVID";
	public static final String TABLE_FIELD_START = "STARTPOS";
	public static final String TABLE_FIELD_END = "ENDPOS";
	public static final String TABLE_FIELD_STARTSTATUS = "STARTSTATUS";
	public static final String TABLE_FIELD_ENDSTATUS = "ENDSTATUS";
	public static final String TABLE_FIELD_STARTSTATUSCVID = "STARTSTATUSCVID";
	public static final String TABLE_FIELD_ENDSTATUSCVID = "ENDSTATUSCVID";
	
	// some db configs ... should better be placed in a config file 
	public static final String DB_MODE_EMBEDDED = "embedded";
	public static final String DB_MODE_NETWORK = "network";
	public static final String DB_PROTOCOL_EMBEDDED = "jdbc:derby:";
	public static final String DB_PROTOCOL_NETWORK = "jdbc:derby://localhost:1527/";
	public static final String DB_DRIVER_EMBEDDED = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String DB_DRIVER_NETWORK = "org.apache.derby.jdbc.ClientDriver";

	public static final String LOG4J_CONFIG_FILE = "log4j.xml";
	
	/**
	 * Return the name of the table linking the passed table to its detail child
	 * @param table
	 * @return
	 */
    public static String getDetailLinkageTable(String table){
    	String linkage = null;
    	if (table == TABLE_INTERACTOR){
    		linkage = TABLE_INTERACTOR_DETAIL;
    	}else if (table == TABLE_INTERACTION){
    		linkage = TABLE_INTERACTION_DETAIL;
    	}else if(table == TABLE_INTERACTION_INTERACTOR){
    		linkage = TABLE_INTERACTION_INTERACTOR_DETAIL;
    	}
    	return linkage;
    	
    }
    
    /** 
     * Return the primary key of the passed table.
     * @param table
     * @return
     */
	public static String getPrimaryId(String table){
		String primKey = null;
		if (table == TABLE_INTERACTOR){
			primKey = TABLE_ID_INTERACTOR;
    	}else if (table == TABLE_INTERACTION){
    		primKey = TABLE_ID_INTERACTION;
    	}else if (table == TABLE_DETAIL){
    		primKey = TABLE_ID_DETAIL;
    	}else if (table == TABLE_RANGE){
    		primKey = TABLE_ID_RANGE;
    	}else if (table == TABLE_INTERACTION_INTERACTOR){
    		primKey = TABLE_ID_INTERACTION_INTERACTOR;
    	}
		return primKey;
	}
	
	
}