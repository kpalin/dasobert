/*                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 * Created on 30.08.2005
 * @author Andreas Prlic
 *
 */


package org.biojava.dasobert.dasregistry ;

//import org.biojava.services.das.*;
//xml stuff
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.biojava.bio.Annotation;
import org.biojava.bio.program.das.dasalignment.Alignment;
import org.biojava.bio.program.das.dasalignment.DASAlignmentClient;
import org.biojava.bio.program.das.dasalignment.DASException;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.DASStructureClient;
import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.das.DAS_DNA_Handler;
import org.biojava.dasobert.das.DAS_DSN_Handler;
import org.biojava.dasobert.das.DAS_Entry_Points_Handler;
import org.biojava.dasobert.das.DAS_Feature_Handler;
import org.biojava.dasobert.das.DAS_Sequence_Handler;
import org.biojava.dasobert.das.DAS_StylesheetRetrieve;
import org.biojava.dasobert.das.DAS_Types_Handler;
import org.biojava.dasobert.das.InteractionDasSource;
import org.biojava.dasobert.das.InteractionParameters;
import org.biojava.dasobert.das.InteractionThread;
import org.biojava.dasobert.das.validation.RelaxNGValidatorJing;
import org.biojava.dasobert.das.validation.RelaxNGValidatorMSV;
import org.biojava.dasobert.das2.Das2Source;
import org.biojava.dasobert.das2.DasSourceConverter;
import org.biojava.dasobert.das2.io.DASRegistryCoordinatesReaderXML;
import org.biojava.dasobert.das2.io.DasSourceReaderImpl;

import org.biojava.ontology.OntoTools;
import org.biojava.ontology.Ontology;
import org.biojava.ontology.OntologyException;
import org.biojava.ontology.OntologyFactory;
import org.biojava.ontology.Term;
import org.biojava.ontology.io.OboParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;

import de.mpg.mpiinf.ag3.dasmi.model.Interaction;



public class Das1Validator {

	//private final static String DATASOURCE_NAME = "jdbc/mysql";
	public String validationMessage;
	boolean supportsMD5Checksum;
	public boolean VALIDATION = false; // DTD validation ..

	public  static final boolean NO_ONTOLOGY_VALIDATION = false;
	public  static final boolean ONTOLOGY_VALIDATION = true;
	private static final boolean RELAX_NG=true;//shows relaxng validation
	private static final boolean RELAX_NG_APPROVAL_NEEDED=false;
	private static final int MAX_SEQUENCE_LENGTH = 1000;
	private static final int MAX_NR_FEATURES     = 10;
	private static final int MAX_NR_FEATURES_ONTOLOGY     = 1000;
	public  static final boolean VERBOSE = false;
	
	List<String> all_capabilities;
	
	private Ontology ontologyBS;
	private Ontology ontologySO;
	private Ontology ontologyECO;

	private Ontology[] ontologies ;
	
	
	private DasCoordinateSystem[] registryCoorinateSystems=null;
	public static final String REGISTRY_LOCATION =  "http://www.dasregistry.org/das1/sources";
	
	public Das1Validator() {
		
		supportsMD5Checksum = false;
		validationMessage = "" ;
		
		all_capabilities = new ArrayList<String>();
		
		for ( int i = 0 ; i< Capabilities.DAS_CAPABILITIES.length; i++ ) {
			all_capabilities.add(Capabilities.DAS_CAPABILITIES[i]);
		}
	}
	
	public boolean supportsMD5Checksum(){
		return supportsMD5Checksum;
	}

	/** return which errors have been produced during validation...
	 * @return String the validation message  */
	public String getValidationMessage(){
		return validationMessage;
	}


	/** validate the DAS source that is located at the provided url
	 * 
	 * @param url the URL of the DAS source
	 * @param coords the coordinate systems that should be supported by it
	 * @param capabilities the capabilities that should be tested.
	 * @return an array of capabilities that were tested successfully.
	 */ 
	public String[] validate(String url, DasCoordinateSystem[] coords, String[] capabilities){
		return validate(url,coords,capabilities,VERBOSE, NO_ONTOLOGY_VALIDATION);
	}

	/** validate the DAS source that is located at the provided url
	 * method called by AutoValidator by registry
	 * @param url the URL of the DAS source
	 * @param coords the coordinate systems that should be supported by it
	 * @param capabilities the capabilities that should be tested.
	 * @param verbose flag if the output should be verbose or not
	 * @param ontologyValidation flag if the ontology should be checked as well
	 * @return an array of capabilities that were tested successfully.
	 */ 
	public String[] validate(String url, DasCoordinateSystem[] coords, 
			String[] capabilities, boolean verbose, boolean ontologyValidation){
		System.out.println("calling validate in DAS1Validator with url="+url);
		verbose=true;
		validationMessage="";
		
		if ( url == null )
			return new String[0];
		
		if ( coords == null )
			return new String[0];
		
		if ( capabilities == null )
			return new String[0];
		
		
		// a list containing all valid DAS requests ...
		
		List<String> lst =new ArrayList<String>();

		char lastChar = url.charAt(url.length()-1);
		if ( lastChar  != '/')
			url += "/";

		boolean valid=validateURL(url);
		//System.out.println("is url valid : "+valid);
		
		if ( verbose)
			System.out.println("validation message="+validationMessage);

		// test if all specified capabilities really work
		for ( int c = 0 ; c < capabilities.length ; c++) {
			String capability = capabilities[c];
			if ( all_capabilities.contains(capability)) {
				//System.out.println("testing " + capability);

				if ( capability.equals(Capabilities.SOURCES)) {
					boolean sourcesok = true;
					
						if ( ! validateSourcesCmd(url) ){
							sourcesok = false;
							
						}
							
						if ( verbose)
							System.out.println(validationMessage);
					
					if ( sourcesok) 
						lst.add(capability);
				}
				else if 
				( capability.equals(Capabilities.SEQUENCE)) {
					boolean sequenceok = true;
					for ( int i=0;i< coords.length;i++){                        
						DasCoordinateSystem ds =coords[i];
						String testcode = ds.getTestCode();

						// do a DAS sequence retreive
						if ( ! validateSequence(url,testcode) ){
							sequenceok = false;
							
						}
							
						if ( verbose)
							System.out.println(validationMessage);
					}
					if ( sequenceok) 
						lst.add(capability);
				}
				else if ( capability.equals(Capabilities.STRUCTURE)) {
					boolean structureok = true;
					for ( int i=0;i< coords.length;i++){                        
						DasCoordinateSystem ds =coords[i];
						
						// don't test for structure if this can't work...
						System.out.println("catagory="+ds.getCategory());
						//if (! ds.getCategory().equals("Protein Structure"))
							//continue;
						
						String testcode = ds.getTestCode();
						

						if (! validateStructure(url,testcode)) 
							structureok = false;
						if ( verbose)
							System.out.println(validationMessage);

					}    
					
					String cmd = url+"structure?model=1&query=";
					
					if (structureok)
						lst.add(capability);
				}
				else if ( capability.equals(Capabilities.FEATURES)){
					boolean featureok = true;
					for ( int i=0;i< coords.length;i++){                        
						DasCoordinateSystem ds =coords[i];
						String testcode = ds.getTestCode();


						if (! validateFeatures(url,testcode, ontologyValidation))
							featureok = false;
						if ( verbose)
							System.out.println(validationMessage);
					} 
					if ( featureok) 
						lst.add(capability);
				} else if ( capability.equals(Capabilities.INTERACTION)){
					boolean interactionok = true;
					for ( int i=0;i< coords.length;i++){                        
						DasCoordinateSystem ds =coords[i];
						String testcode = ds.getTestCode();
						
						if ( verbose ){
							System.out.println(" validating interaction ");
							System.out.println(url + " " + testcode );
						}
						if (! validateInteraction(url, testcode))
							interactionok = false;
						if ( verbose)
							System.out.println(validationMessage);
					} 
					if ( interactionok) 
						lst.add(capability);
				}
				else if ( capability.equals(Capabilities.ALIGNMENT)){
					boolean alignmentok = true;
					for ( int i=0;i< coords.length;i++){                        
						DasCoordinateSystem ds =coords[i];
						String testcode = ds.getTestCode();


						if (! validateAlignment(url,testcode))
							alignmentok = false;
						if ( verbose)
							System.out.println(validationMessage);
					}    
					if (alignmentok)
						lst.add(capability);
				} else if ( capability.equals(Capabilities.TYPES)){
					if ( validateTypes(url, ontologyValidation))
						lst.add(capability);
						
					if ( verbose)
						System.out.println(validationMessage);
					//else
						//    error =true ;

				} else if ( capability.equals(Capabilities.ENTRY_POINTS)) {
					if ( validateEntry_Points(url))
						lst.add(capability);
					if ( verbose)
						System.out.println(validationMessage);
					//else 
						//    error = true;
				} else if ( capability.equals(Capabilities.STYLESHEET)) {
					if ( validateStylesheet(url))
						lst.add(capability);
					if ( verbose)
						System.out.println(validationMessage);
					//} else 
						//    error = true;
				} else if ( capability.equals(Capabilities.DNA)){
					boolean dnaok = true;
					for ( int i=0;i< coords.length;i++){                        
						DasCoordinateSystem ds =coords[i];
						String testcode = ds.getTestCode();                        

						if ( ! validateDNA(url,testcode))
							dnaok = false;

					}
					if (dnaok) 
						lst.add(capability);                        
				}
				else {
					validationMessage += "<br/>---<br/> test of capability " + capability + " not implemented,yet.";
					lst.add(capability);
				}
			}
		}

		//if ( error) {
		//    System.out.println("DasValidator: "+ validationMessage);
		//}
		//this.validationMessage = validationMessage;
		return (String[])lst.toArray(new String[lst.size()]);

	}

	/**
	 * validate the sources cmd of a server
	 * @param url
	 * @param testcode
	 * @return
	 */
	private boolean validateSourcesCmd(String url) {
		//sources is the odd capability as belongs to the server not the source
		//therefor need to chop DataSourceName off the end of the url
		System.out.println("sources url at start of validation method "+url);
		if(url.endsWith("/")){
			System.out.println("ends with /");
			url=url.substring(0,url.length()-1);
			System.out.println("after -1="+url);
			
		}
		//now remove the datasource name at the end of the url
		String choppedURL=url.substring(0,url.lastIndexOf("/")+1);
		System.out.println("chopped "+choppedURL);
		String cmd = choppedURL+"sources";
		
		System.out.println("running sources with  cmd="+cmd);
		if(!relaxNgApproved(RelaxNGValidatorMSV.SOURCES, cmd))return false;
		
		//source for programmatically validating sources response
		
		//get a list of all sources from the registry either from xml for external programs or from database
		//for the registry
		//then test the sources from the external server
		int numberOfInvalidSources=0;
		DasSourceReaderImpl reader = new DasSourceReaderImpl();
		try {
			URL u = new URL(cmd);
			DasSource[] sources = reader.readDasSource(u);
			System.out.println("number of sources being checked="+sources.length);
			
			for (int i=0; i< sources.length;i++){
				Das1Source ds = (Das1Source)sources[i];
				//System.out.println(ds.toString());
				boolean isValid=this.checkDAS1Source(ds);
				
				if(!isValid){
					numberOfInvalidSources++;
				System.out.println(ds);
				}

				
			}

		} catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("number of invalid sources returned from this sources cmd was "+numberOfInvalidSources);
		
		return true;
	
	}

	/**
	 * 
	 * @param cmdType one of RelaxNGValidatorMSV static strings such as RelaxNGValidatorMSV.SOURCES
	 * @param cmd url string sometimes with testcode added
	 * @return boolean true if valid according to relaxng if approval needed from relaxng.
	 */
	private boolean relaxNgApproved(String cmdType, String cmd) {
		if(RELAX_NG){
			RelaxNGValidatorMSV rng=new RelaxNGValidatorMSV();
			if(!rng.validateUsingRelaxNG(cmdType, cmd)){
				
				validationMessage+=rng.getMessage();
				System.out.println("getting message in das1 validator"+validationMessage);
				if(RELAX_NG_APPROVAL_NEEDED)return false;
				
			}
			
		}
			return true;
	}

	/**
	 * method to check the validity of one source in the response from the sources cmd
	 * @param ds
	 * @return
	 */
	private boolean checkDAS1Source(Das1Source ds) {
		//tests to do include what?
		//relaxng has tested the structure
		//main check is to check that the coorinate system is in the registry
		boolean isValid=false;
		DasCoordinateSystem[] coords=ds.getCoordinateSystem();
		if(this.registryCoorinateSystems==null){
			//instantiate a new list if not set
			this.registryCoorinateSystems=this.getRegistryCoordinateSystems();
		}
		for(int j=0; j<coords.length;j++){
			DasCoordinateSystem cs=coords[j];
			//System.out.println("coordinate system="+cs);
			//need to check if split cs then should equal "authority, type";
			String userCSAuthority=cs.getAuthority();
			String userCSCategory=cs.getCategory();
			
			//System.out.println("user authority="+userCSAuthority+" category="+userCSCategory);
			//System.out.println("Number of Reg coordinate systems returned="+this.registryCoorinateSystems.length);
			for(int k=0;k<registryCoorinateSystems.length;k++){
				DasCoordinateSystem tempCs=registryCoorinateSystems[k];
				if(tempCs.equals(cs)){
					//System.out.println("coordinate sytem found in registry");
					isValid=true;
					
				}
				
				//System.out.println("authority in reg="+tempCs.getAuthority());
				
			}
		}
		if(!isValid){
			System.out.println(ds.getUrl()+ "  is not valid!!!!!!!");
			
		}
		return isValid;
		
		
	}
	/**
	 * use this method to get known Coordinate systems from the registry
	 * using das call to registry by default but the registry itself can change
	 * this method to use the database directly if needed.
	 * @return
	 */
	private DasCoordinateSystem[] getRegistryCoordinateSystems(){
	        
	        DASRegistryCoordinatesReaderXML reader = new DASRegistryCoordinatesReaderXML();
	        //need to implement a reader for http://www.dasregistry.org/das1/coordinatesystem cmd	        
	        DasCoordinateSystem coords[] = reader.readRegistryDas1CoorinateSystems();
	        
	        return coords;
	        //DasCoordinateSystem[] registryCoorinateSystems
	        
	        
	    }
	

	/** make sure the URL matches the DAS spec 
     returns true if URL looks o.k...
     @param url to validate
     @return boolean true if URL looks ok
	 */
	public  boolean validateURL(String url) {
		System.out.println("*********validating url**************");
		String[] spl = url.split("/");

		//for (int i = 0 ; i < spl.length  ; i++ ) {
		//    System.out.println("spl["+i+"]:"+spl[i]);
		//}

		if (spl == null ) {
			validationMessage +="---<br/> URL is not well formed" ;
			return false;
		}

		if ( spl.length <= 4) {
			validationMessage +="---<br/> URL is not well formed <br/>" +
			"should be http[s]://site.specific.prefix/das/dassourcename/";
			return false; 
		} 

		//System.out.println("split 0 : " + spl[0]); 
		if ( ! (spl[0].equals("http:"))) {
			if ( ! ( spl[0].equals("https:"))){
				validationMessage +="---<br/> URL is not well formed (does not start with http:// or https://)" ;
				return false;
			}

		}

		String dastxt = spl[spl.length-2] ;
		//System.out.println("should be >das< " + dastxt);
		if ( ! dastxt.equals("das")) {
			String suggestion = spl[0] + "//" ;
			String wrong      = spl[0] + "//" ;
			for (int i = 2 ; i < spl.length -2 ; i++ ) {
				suggestion += spl[i] + "/" ;
				wrong += spl[i] + "/" ;
			}
			suggestion +="<b>das</b>/"+spl[spl.length-1];
			wrong +="<b>" + spl[spl.length-2] + "</b>/"+spl[spl.length-1];
			validationMessage +="--<br/> the URL does not match the DAS spec. it should be <br/>"+
			" http[s]://site.specific.prefix/das/dassourcename/ <br/>" +
			" found >" + dastxt +" < instead of >das< <br/>" +
			" suggested url: " + suggestion + "<br/>"+
			" instead of: " + wrong ;
			return false;
		}
		return true;

	}

	private boolean validateDNA(String url, String testcode){
		try { 	    
			String cmd = url+"dna?segment="+testcode;
			URL strurl = new URL(cmd);
			InputStream dasInStream = open(strurl); 

			XMLReader xmlreader = getXMLReader();

			DAS_DNA_Handler cont_handle = new DAS_DNA_Handler() ;

			xmlreader.setContentHandler(cont_handle);
			xmlreader.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
			InputSource insource = new InputSource() ;

			insource.setByteStream(dasInStream);
			xmlreader.parse(insource);
			String sequence = cont_handle.get_sequence();
			if ( sequence.length() > 0 ) {
				return true;
			} else {
				validationMessage  +="<br/>---<br/> contacting " + cmd + "<br/>";
				validationMessage += " no sequence was returned";

				return false;
			}


		} catch ( Exception e) {
			//e.printStackTrace();
			validationMessage += "<br/>---<br/> contacting " + url + "dna?segment="+testcode + "<br/>";

			Throwable cause = e.getCause();
			if ( cause != null) 
				validationMessage += cause.toString();
			else
				validationMessage += e.toString();
		}
		return false;

	}

	private boolean validateStylesheet(String url) {
		try {
			DAS_StylesheetRetrieve dsr = new DAS_StylesheetRetrieve();
			URL styleurl = new URL(url+"stylesheet");

			Map[] stylesheet = dsr.retrieve(styleurl);
			if (( stylesheet != null ) && ( stylesheet.length > 0)) 
				return true;
			else {
				validationMessage  +="<br/>---<br/> contacting " + url + "stylesheet<br/>";
				validationMessage += " no stylesheet was returned";
				return false;
			}
		} catch (Exception e) {
			validationMessage += "<br/>---<br/> contacting " + url+"stylesheet <br/>";

			Throwable cause = e.getCause();
			if ( cause != null) 
				validationMessage += cause.toString();
			else
				validationMessage += e.toString();
		}
		return false;
	}

	private boolean validateAlignment(String url, String testcode){
		String cmd = url+"alignment?query=" ;
		//System.out.println(cmd + " " + testcode);
		if(!relaxNgApproved(RelaxNGValidatorMSV.ALIGNMENT, cmd))return false;
		
		
		try {
			
			
			DASAlignmentClient dasc= new DASAlignmentClient(cmd);
			//System.out.println("getting alignments for testcode " + testcode);
			Alignment[] alignments = dasc.getAlignments(testcode);
			if ( alignments.length > 0 ) {
				return true;
			}
			else {
				validationMessage  +="<br/>---<br/> contacting " + cmd +testcode + "<br/>";
				validationMessage += " no Alignments were returned";
				return false;
			}

		} catch (Exception e) {
			validationMessage += "<br/>---<br/> contacting " + cmd +testcode + "<br/>";

			Throwable cause = e.getCause();
			if ( cause != null) 
				validationMessage += cause.toString();
			else
				validationMessage += e.toString();
		}
		return false;
	}

	private boolean validateEntry_Points(String url){
		try {
			URL u = new URL(url+"entry_points");
			
			
			if(!relaxNgApproved(RelaxNGValidatorMSV.ENTRY_POINTS, url+"entry_points"))return false;

			InputStream dasInStream = open(u); 

			XMLReader xmlreader = getXMLReader();

			DAS_Entry_Points_Handler cont_handle = new DAS_Entry_Points_Handler() ;

			xmlreader.setContentHandler(cont_handle);
			xmlreader.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
			InputSource insource = new InputSource() ;
			insource.setByteStream(dasInStream);
			xmlreader.parse(insource);
			String version = cont_handle.getVersion();
			if ( version != null ) {
				return true;
			} else {
				validationMessage  +="<br/>---<br/> contacting " + url +"entry_points <br/>";
				validationMessage += " no version was returned";

				return false;
			}


		} catch ( Exception e) {
			//e.printStackTrace();
			validationMessage += "<br/>---<br/> contacting " + url+ "entry_points <br/>";

			Throwable cause = e.getCause();
			if ( cause != null) 
				validationMessage += cause.toString();
			else
				validationMessage += e.toString();
		}
		return false;
	}

	
	/** validate the DSN command for a DAS source.
	 * 
	 * @param url the full url of a source, including the name of the das source
	 * @return flag if the DSN  response is o.k.
	 */
	public boolean validateDSN(String url){
		try {
			
			String[] spl = url.split("/");
			
			String dsnurl = "";
			
			for (int i=0 ; i< spl.length -1;i++){
				dsnurl+=spl[i]+"/";
			}
			
			
			URL u = new URL(dsnurl+"dsn");
			
			//System.out.println(u.toString());
			
			// parse dsn ...
			InputStream dasInStream = open(u); 
			XMLReader xmlreader = getXMLReader();

			DAS_DSN_Handler cont_handle = new DAS_DSN_Handler() ;

			xmlreader.setContentHandler(cont_handle);
			xmlreader.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
			InputSource insource = new InputSource() ;
			insource.setByteStream(dasInStream);
			xmlreader.parse(insource);
			List sources = cont_handle.getDsnSources();
			
			//System.out.println("got " + sources.size() + " sources listed in DSN");
			if ( sources.size() > 0 )
				return true;
			
		} catch ( Exception e) {
			//e.printStackTrace();
			validationMessage += "<br/>---<br/> contacting " + url+ "types <br/>";

			Throwable cause = e.getCause();
			if ( cause != null) 
				validationMessage += cause.toString();
			else
				validationMessage += e.toString();
		}
		return false;
		
	}
	
	
	
	private boolean validateTypes(String url, boolean ontologyValidation){
		try {
			URL u = new URL(url+"types");
			
			
			if(!relaxNgApproved(RelaxNGValidatorMSV.TYPES, url+"types"))return false;
			
			InputStream dasInStream = open(u); 
			XMLReader xmlreader = getXMLReader();

			DAS_Types_Handler cont_handle = new DAS_Types_Handler() ;

			xmlreader.setContentHandler(cont_handle);
			xmlreader.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
			InputSource insource = new InputSource() ;
			if (ontologyValidation)
				cont_handle.setMaxFeatures(MAX_NR_FEATURES_ONTOLOGY);
			insource.setByteStream(dasInStream);
			xmlreader.parse(insource);
			String[] types = cont_handle.getTypes();
			if ( types.length > 0 ) {
				if ( ! ontologyValidation)
					return true;
				validationMessage  +="<br/>---<br/> contacting " + url + "<br/>";				
				return validateTypesAgainstOntology(types);
				
			} else {
				validationMessage  +="<br/>---<br/> contacting " + url +"types <br/>";
				validationMessage += " no types were returned";

				return false;
			}


		} catch ( Exception e) {
			//e.printStackTrace();
			validationMessage += "<br/>---<br/> contacting " + url+ "types <br/>";

			Throwable cause = e.getCause();
			if ( cause != null) 
				validationMessage +="exception thrown at end of types validation"+ cause.toString();
			else
				validationMessage +="Could be an empty page returned?? "+ e.toString();
		}
		return false;
	}

	private boolean validateInteraction(String url, String testcode){
		//System.out.println("called validate interaction method url " +url);
		//url="http://localhost:8080/dasregistryOID/interactionTestOld.xml";
		
		if(!relaxNgApproved(RelaxNGValidatorMSV.INTERACTION, url+"interaction?interactor="+testcode))return false;
		
		InteractionDasSource source = new InteractionDasSource();
		source.setUrl(url);
		InteractionParameters params = new InteractionParameters();
		
		
		
		params.setDasSource(source);
		params.setQueries(new String[]{testcode});
		
		InteractionThread thread = new InteractionThread(params);
		//System.out.println("set up interaction thread");
		
		// TODO: how can I do  multiple threads with JUnit??
		Interaction[] interA = thread.getInteractions(new String[]{testcode,});
		//System.out.println("interA.length="+interA.length);
		if ( interA.length > 0)
			return true;
		return false;
	}

	private boolean validateFeatures(String url, 
			String testcode, boolean ontologyValidation){
		try {
			URL u = new URL(url+"features?segment="+testcode);
			
			
			if(!relaxNgApproved(RelaxNGValidatorMSV.FEATURE, url+"features?segment="+testcode))return false;
			//System.out.println("validation message after features and rng call= "+validationMessage);
			InputStream dasInStream = open(u); 
			XMLReader xmlreader = getXMLReader();

			DAS_Feature_Handler cont_handle = new DAS_Feature_Handler() ;

			// make sure we do not load the features of a whole chromosome, in case a user specified those...
			cont_handle.setMaxFeatures(MAX_NR_FEATURES);
			
			if (ontologyValidation)
				cont_handle.setMaxFeatures(MAX_NR_FEATURES_ONTOLOGY);
			cont_handle.setDASCommand(url.toString());
			xmlreader.setContentHandler(cont_handle);
			xmlreader.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
			InputSource insource = new InputSource() ;
			insource.setByteStream(dasInStream);
			xmlreader.parse(insource);
			List<Map<String,String>> features = cont_handle.get_features();
			
			if ( cont_handle.isMD5Checksum())
				supportsMD5Checksum = true;
			
			if ( features.size() > 0 ) {
				if ( ! ontologyValidation)
					return true;
				validationMessage  +="<br/>---<br/> contacting " + url+"features?segment="+testcode + "<br/>";				
				return validateFeatureOntology(features);
				
			} else {
				validationMessage  +="<br/>---<br/> contacting " + url+"features?segment="+testcode + "<br/>";
				validationMessage += " no features were returned";

				return false;
			}

		} catch ( Exception e) {
			//e.printStackTrace();
			validationMessage += "<br/>---<br/> contacting " + url+"features?segment="+testcode + "<br/>";

			Throwable cause = e.getCause();
			if ( cause != null) 
				validationMessage += cause.toString();
			else
				validationMessage += e.toString();
		}
		return false;
	}
	
	
	private void initOntologies(){
		try {
			ontologyBS = readOntology("BioSapiens","the BioSapiens Ontology", "biosapiens.obo");
			ontologySO = readOntology("SequenceOntology", "the Sequence Ontology" , "so.obo");			
			ontologies =  new Ontology[]{ontologyBS, ontologySO};
			ontologyECO = readOntology("ECO", "the Evidence Code Ontology" , "evidence_code.obo");
		} catch (OntologyException ex) {
			ex.printStackTrace();
		}
	}
	

	private Ontology readOntology(String ontoName, String ontoDesc, String fileName) throws OntologyException{


		OboParser parser = new OboParser();
		InputStream inStream = this.getClass().getResourceAsStream("/ontologies/"+fileName);
		//System.out.println("reading ontology: /ontologies/" + fileName);
		if (inStream == null){
			System.err.println("did not find " + fileName );
		}
		
		BufferedReader oboFile = new BufferedReader ( new InputStreamReader ( inStream ) );
		
		try {
			Ontology ontology = parser.parseOBO(oboFile, ontoName, ontoDesc );

			//System.out.println("finished parsing: " + ontology);
			//Set keys = ontology.getTerms();
			//Iterator<Term> iter = keys.iterator();
			//while (iter.hasNext()){
			//	Term term = iter.next();				
			//	System.out.println(term + " " + term.getDescription());

			//}
			return ontology;

		} catch (Exception e){
			e.printStackTrace();
		}

		// return a dummy ontology
		OntologyFactory factory = OntoTools.getDefaultFactory();
		Ontology ontology = factory.createOntology(ontoName, ontoDesc);

		return ontology;

	}
	

	private Term getTerm(String typeID) {
		if ( typeID == null){
			System.err.println("typeID is NULL, no terms in an ontology");
			return null;
		}
		Term t = null;

		if ( ontologies == null) {
			initOntologies();
		}
		
		try {
			for (Ontology ontology: ontologies){
				if ( ontology.containsTerm(typeID)) {
					t = ontology.getTerm(typeID);
					if (t != null)
						return t;
				}
			}
		} catch (NoSuchElementException ex){
			ex.printStackTrace();
			//System.err.println(ex.getMessage());
		}
		return t;
	}

	
	
	/**validates a track for consistency with the BioSapiens annotation
	 * @param feature 
	 * 
	 * @return true if the track validates
	 * @throws DASException 
	 */
	public boolean  validateTrack(Map<String,String> feature) throws DASException{

		Pattern ecoPattern = Pattern.compile("(ECO:[0-9]+)");
		
		// validate type:
		String type         = feature.get("TYPE");
		String typeID       = feature.get("TYPE_ID");
		String typeCategory = feature.get("TYPE_CATEGORY");
		//System.out.println("type  " + type);
		//System.out.println("method " + feature.get("METHOD"));
		//System.out.println("typeID " + typeID);
		//System.out.println("typeCategory " + typeCategory);
	
		if ( typeID == null) {
			throw new DASException("track does not have the TYPE - id field set");
		}
		if ( typeCategory == null) {
			throw new DASException("track does not have the TYPE - category field set");
		}

		Term t = testTypeIDAgainstOntology(typeID);
		

		if (! t.getDescription().equals(type)){
			boolean synonymUsed = false;
			Object[] synonyms = t.getSynonyms();
			for (Object syno :  synonyms){
				//System.out.println(syno);
				if ( syno.equals(type)){
					synonymUsed = true;
					break;
				}
			}
			if ( ! synonymUsed) {			
				throw new DASException("feature type ("+ type + 
						") does not match Ontology description (" + 
						t.getDescription()+" for termID: " +
				typeID+")");
			}
		}

		// test evidence code

		// parse the ECO id from the typeCategory;
		Matcher m = ecoPattern.matcher(typeCategory);
		String eco = null;
		if ( m.find() ) {
			eco = m.group(0);
		}

		if ( eco == null){
			throw new DASException("could not identify ECO id in " + typeCategory);
		}
		if (! ontologyECO.containsTerm(eco)){
			throw new DASException("unknown evidence code >" + eco + "<");
		}



		return true;

	}

	private Term testTypeIDAgainstOntology(String typeID) throws DASException {
		Term t = getTerm(typeID);
		
		if ( t == null){
			throw new DASException ("term " + typeID +" not found in any Ontology");
		}
						
		Annotation anno = t.getAnnotation();
		try {
			Boolean obsolete = (Boolean) anno.getProperty("is_obsolete");
			if ((obsolete != null )&& (obsolete.equals(true))) {
				throw new DASException("Feature uses an obsolete term: "+ t.getName() + " " + t.getDescription());
			}
		} catch (NoSuchElementException e){
			// the property is_obsolete is not set, 
			// which means the term is still current
			// and we proceed as normal
		}
		return t;
	}
	
	private boolean validateFeatureOntology(List<Map<String, String>> featuresList){
		
		validationMessage += "got " + featuresList.size() + " features\n";
		boolean ontologyOK = true;
		int i = 0;
		for( Map<String,String>feature : featuresList){
			i++;
			validationMessage += "*** validating track " + i +": " + feature.get("TYPE") +"\n";
			try {
				
				if (( feature.get("START").equals(feature.get("END"))) &&
						(feature.get("START").equals("0"))){
					validationMessage +="  Non-positional features are currently not supported, yet.\n";
					continue;
				}
				if ( validateTrack(feature)) {
					validationMessage +="  track ok!\n";
				}
			} catch (DASException ex){
				//System.out.println(ex.getMessage());
				//ex.printStackTrace();
				validationMessage += "   " + ex.getMessage() +"\n";
				validationMessage += "   This DAS source does NOT comply with the BioSapiens ontology!\n";
				ontologyOK = false;

			}
		}
		return ontologyOK;
	}
	
	/**
	 * written by jw to add ontology to the types validation
	 * @param typesList
	 * @return
	 */
  private boolean validateTypesAgainstOntology(String[] typesList){
		//System.out.println("validating type ontology jw");
		validationMessage += "got " + typesList.length + " types\n";
		boolean ontologyOK = true;
		
		//start at 1 as 0 is ID
		for( int i=1; i<typesList.length; i++){
			
			validationMessage += "*** validating type " + i +": " + typesList[i] +"\n";
			try {
				//validate code here to replace validat tracks in feature equivalent method
				Term term=testTypeIDAgainstOntology(typesList[i]);
				if ( term!=null ) {
					validationMessage +="  track ok!\n";
				}
			} catch (DASException ex){
				//System.out.println(ex.getMessage());
				//ex.printStackTrace();
				validationMessage += "   " + ex.getMessage() +"\n";
				validationMessage += "   This DAS source does NOT comply with the BioSapiens ontology!\n";
				ontologyOK = false;

			}
		}
		return ontologyOK;
	}
	
	

	

	private boolean validateStructure(String url, String testcode) {
		String cmd = url+"structure?model=1&query=";
		
		System.out.println("running structure with  cmd="+cmd);
		
			
			if(!relaxNgApproved(RelaxNGValidatorMSV.STRUCTURE, cmd+testcode))return false;

		DASStructureClient dasc= new DASStructureClient(cmd);
		
		
		try {
			Structure struc = dasc.getStructureById(testcode);
			//System.out.println(struc);
			Chain c = struc.getChain(0);
			if ( c.getAtomLength() > 0 ) {
				return true;
			} else {
				validationMessage += "<br/>---<br/>contacting " + cmd + testcode+"<br/>";
				validationMessage += " no structure found";
				return false;
			}
		} catch (Exception e) {
			validationMessage += "<br/>---<br/>contacting " + cmd + testcode+"<br/>";

			Throwable cause = e.getCause();
			if ( cause != null) 
				validationMessage += cause.toString();
			else
				validationMessage += e.toString();
			//e.printStackTrace();	    
		}
		return false;
	}
	private boolean validateSequence(String url, String testcode) {
		URL dasUrl;
		
		// try to parse the test region from the testcode - if it looks like a chromosomal one 
		// make sure only 50 bp are being read!
		String reference = "";
		String start = "";
		
		String[] spl = testcode.split(":");
		if (spl.length <2) {
			reference = testcode;
		} else if ( spl.length ==2) {
			reference = spl[0];
			String coordsspl = spl[1];
			String[] splc = coordsspl.split(",");
			if ( splc.length == 2 ) {
				start = splc[0];
				
			}
		}
		
		
		int startInt = -9999 ; 
		try { 
			startInt = Integer.parseInt(start);
		} catch (NumberFormatException ex){}
		
		String cmd = url+"sequence?segment="+reference;
		if ( startInt != -9999)
			cmd += ":" + startInt + "," + (startInt+50);
		
		//System.out.println(cmd);
	
		
		try {
			dasUrl = new URL(cmd);

		} catch ( Exception e) {
			e.printStackTrace();
			return false;
		}
		
		URL schemaLocation=null;
		
		
		
		if(!relaxNgApproved(RelaxNGValidatorMSV.SEQUENCE, cmd))return false;
		
		
		try {
			//System.out.println("opening " + dasUrl);
			InputStream dasInStream =open(dasUrl); 
			SAXParserFactory spfactory =
				SAXParserFactory.newInstance();
			spfactory.setValidating(true);

			SAXParser saxParser = null ;

			try{
				saxParser =
					spfactory.newSAXParser();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			XMLReader xmlreader = saxParser.getXMLReader();

			try {
				xmlreader.setFeature("http://xml.org/sax/features/validation", VALIDATION);
			} catch (SAXException e) {
				e.printStackTrace();
			}
			try {
				xmlreader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",VALIDATION);
			} catch (SAXNotRecognizedException e){
				e.printStackTrace();
			}

			DAS_Sequence_Handler cont_handle = new DAS_Sequence_Handler() ;
			cont_handle.setMaxLength(MAX_SEQUENCE_LENGTH);
			xmlreader.setContentHandler(cont_handle);
			xmlreader.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
			InputSource insource = new InputSource() ;
			insource.setByteStream(dasInStream);
			xmlreader.parse(insource);
			String sequence = cont_handle.get_sequence();
			//System.out.println("done parsing sequence ...");
			if ( ( sequence==null) || (sequence.equals(""))) {
				validationMessage += "---<br/>contacting " + cmd +"<br/>";
				validationMessage += "no sequence found";
				return false;
			} 
			return true; 
		} catch (Exception e) {
			validationMessage += "---<br/>contacting " + cmd +"<br/>";

			Throwable cause = e.getCause();
			if ( cause != null) 
				validationMessage += cause.toString();
			else
				validationMessage += e.toString();
			//e.printStackTrace();
		}
		return false;
	}

	private XMLReader getXMLReader() throws SAXException{
		SAXParserFactory spfactory =
			SAXParserFactory.newInstance();

		spfactory.setValidating(false) ;
		SAXParser saxParser = null ;

		try{
			saxParser =
				spfactory.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} 

		XMLReader xmlreader = saxParser.getXMLReader();
		boolean validation = VALIDATION;
		//XMLReader xmlreader = XMLReaderFactory.createXMLReader();
		try {
			xmlreader.setFeature("http://xml.org/sax/features/validation", validation);
		} catch (SAXException e) {
			e.printStackTrace();
		}

		try {
			xmlreader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",validation);
		} catch (SAXNotRecognizedException e){	
			e.printStackTrace();		
		}
		return xmlreader;

	}



	private InputStream open(URL url) throws Exception{

		// TODO Auto-generated method stub

		InputStream inStream = null;

		HttpURLConnection huc = null;
		huc = (HttpURLConnection) url.openConnection();
		//String contentEncoding = huc.getContentEncoding();
		inStream = huc.getInputStream();	
		return inStream;
	}

	

}