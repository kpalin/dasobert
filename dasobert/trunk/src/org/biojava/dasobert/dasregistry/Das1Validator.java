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
import org.xml.sax.*;
import javax.xml.parsers.*;
import java.util.ArrayList                    ;
import java.util.Map                          ;
import java.util.List                         ;
import java.io.InputStream                    ;

import java.net.URL                           ;


//for validation add dependency on SPICE... :-/
import java.net.HttpURLConnection;
import org.biojava.bio.structure.io.DASStructureClient;
import org.biojava.bio.program.das.dasalignment.DASAlignmentClient;
import org.biojava.bio.program.das.dasalignment.Alignment;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.Chain;
import org.biojava.dasobert.das.*;

import de.mpg.mpiinf.ag3.dasmi.model.Interaction;



public class Das1Validator {

	//private final static String DATASOURCE_NAME = "jdbc/mysql";
	String validationMessage;
	boolean supportsMD5Checksum;
	public boolean VALIDATION = false; // DTD validation ..

	private static final int MAX_SEQUENCE_LENGTH = 1000;
	private static final int MAX_NR_FEATURES     = 10;
	public static final boolean VERBOSE = false;
	
	List<String> all_capabilities;
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
		return validate(url,coords,capabilities,VERBOSE);
	}

	/** validate the DAS source that is located at the provided url
	 * 
	 * @param url the URL of the DAS source
	 * @param coords the coordinate systems that should be supported by it
	 * @param capabilities the capabilities that should be tested.
	 * @param verbose flag if the output should be verbose or not
	 * @return an array of capabilities that were tested successfully.
	 */ 
	public String[] validate(String url, DasCoordinateSystem[] coords, String[] capabilities, boolean verbose){
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

		validateURL(url); 

		if ( verbose)
			System.out.println(validationMessage);

		// test if all specified capabilities really work
		for ( int c = 0 ; c < capabilities.length ; c++) {
			String capability = capabilities[c];
			if ( all_capabilities.contains(capability)) {
				//System.out.println("testing " + capability);

				if ( capability.equals(Capabilities.SEQUENCE)) {
					boolean sequenceok = true;
					for ( int i=0;i< coords.length;i++){                        
						DasCoordinateSystem ds =coords[i];
						String testcode = ds.getTestCode();

						// do a DAS sequence retreive
						if ( ! validateSequence(url,testcode) )
							sequenceok = false;
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
						if (! ds.getCategory().equals("Protein Structure"))
							continue;
						
						String testcode = ds.getTestCode();
						

						if (! validateStructure(url,testcode)) 
							structureok = false;
						if ( verbose)
							System.out.println(validationMessage);

					}    
					if (structureok)
						lst.add(capability);
				}
				else if ( capability.equals(Capabilities.FEATURES)){
					boolean featureok = true;
					for ( int i=0;i< coords.length;i++){                        
						DasCoordinateSystem ds =coords[i];
						String testcode = ds.getTestCode();


						if (! validateFeatures(url,testcode))
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
					if ( validateTypes(url))
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

	/** make sure the URL matches the DAS spec 
     returns true if URL looks o.k...
     @param url to validate
     @return boolean true if URL looks ok
	 */
	public  boolean validateURL(String url) {
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
		try {
			
			
			DASAlignmentClient dasc= new DASAlignmentClient(cmd);
			System.out.println("getting alignments for testcode " + testcode);
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
			
			System.out.println(u.toString());
			
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
			
			System.out.println("got " + sources.size() + " sources listed in DSN");
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
	
	
	
	private boolean validateTypes(String url){
		try {
			URL u = new URL(url+"types");
			InputStream dasInStream = open(u); 
			XMLReader xmlreader = getXMLReader();

			DAS_Types_Handler cont_handle = new DAS_Types_Handler() ;

			xmlreader.setContentHandler(cont_handle);
			xmlreader.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
			InputSource insource = new InputSource() ;
			insource.setByteStream(dasInStream);
			xmlreader.parse(insource);
			String[] types = cont_handle.getTypes();
			if ( types.length > 0 ) {
				return true;
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
				validationMessage += cause.toString();
			else
				validationMessage += e.toString();
		}
		return false;
	}

	private boolean validateInteraction(String url, String testcode){
		InteractionDasSource source = new InteractionDasSource();
		source.setUrl(url);
		InteractionParameters params = new InteractionParameters();
		
		
		
		params.setDasSource(source);
		params.setQueries(new String[]{testcode});
		InteractionThread thread = new InteractionThread(params);
		
		
		// TODO: how can I do  multiple threads with JUnit??
		Interaction[] interA = thread.getInteractions(new String[]{testcode,});
		if ( interA.length > 0)
			return true;
		return false;
	}

	private boolean validateFeatures(String url, String testcode){
		try {
			URL u = new URL(url+"features?segment="+testcode);
			InputStream dasInStream = open(u); 
			XMLReader xmlreader = getXMLReader();

			DAS_Feature_Handler cont_handle = new DAS_Feature_Handler() ;

			// make sure we do not load the features of a whole chromosome, in case a user specified those...
			cont_handle.setMaxFeatures(MAX_NR_FEATURES);
			cont_handle.setDASCommand(url.toString());
			xmlreader.setContentHandler(cont_handle);
			xmlreader.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
			InputSource insource = new InputSource() ;
			insource.setByteStream(dasInStream);
			xmlreader.parse(insource);
			List features = cont_handle.get_features();
			
			if ( cont_handle.isMD5Checksum())
				supportsMD5Checksum = true;
			
			if ( features.size() > 0 ) {
				return true;
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

	private boolean validateStructure(String url, String testcode) {
		String cmd = url+"structure?model=1&query=";
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
