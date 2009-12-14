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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
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
import org.biojava.dasobert.das.validation.DasRegistryOntologyLookUp;
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



public class Das1_6Validator extends Das1Validator{

	
	
	public Das1_6Validator() {
		
		supportsMD5Checksum = false;
		validationMessage = "" ;
		
		
	}
	

	

/**
 * DNA command is not allowed in 1.6
 * @param url
 * @param testcode
 * @return
 */
	private boolean validateDNA(String url, String testcode){
		
				if(appendValidationErrors)validationMessage += " DNA is not a valid 1.6 coommand please use sequence cmd instead";

				return false;
			
	}

	
	

	
	/** DSN is not allowed in 1.6
	 * 
	 * @param url the full url of a source, including the name of the das source
	 * @return flag if the DSN  response is o.k.
	 */
	public boolean validateDSN(String url){
		if(appendValidationErrors)validationMessage += " DSN is not a valid 1.6 coommand please use sources cmd instead as this gives more helpful information for DAS clients";

		return false;
		
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

		SimpleTerm t = testTypeIDAgainstOntology(typeID);


		
//		if (! t.getDescription().equals(type)){
//			boolean synonymUsed = false;
//			Object[] synonyms = t.getSynonyms();
//			for (Object syno :  synonyms){
//				//System.out.println(syno);
//				if ( syno.equals(type)){
//					synonymUsed = true;
//					break;
//				}
//			}
//			if ( ! synonymUsed) {			
//				throw new DASException("feature type ("+ type + 
//						") does not match Ontology description (" + 
//						t.getDescription()+" for termID: " +
//				typeID+")");
//			}
//		}
//
//		// test evidence code
//
		// parse the ECO id from the typeCategory;
		Matcher m = ecoPattern.matcher(typeCategory);
		String eco = null;
		if ( m.find() ) {
			eco = m.group(0);
		}

		if ( eco == null){
			throw new DASException("could not identify ECO id in " + typeCategory);
		}
		if (! lookup.exists(eco, "ECO")){
			throw new DASException("unknown evidence code >" + eco + "<");
		}



		return true;//returning true at the moment as failing ontology test currently does not stop source being valid

	}

	
	
	
	/**
	 * written by jw to add ontology to the types validation
	 * @param typesList
	 * @return
	 */
  private boolean validateTypesAgainstOntology(String[] typesList){
		//System.out.println("validating type ontology jw");
		//validationMessage += "got " + typesList.length + " types\n";
		boolean ontologyOK = true;
		
		//start at 1 as 0 is ID
		for( int i=1; i<typesList.length; i++){
			
			//validationMessage += "*** validating type " + i +": " + typesList[i] +"\n";
			try {
				//validate code here to replace validat tracks in feature equivalent method
				SimpleTerm term=testTypeIDAgainstOntology(typesList[i]);
				if ( term!=null ) {
					//validationMessage +="  track ok!\n";
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
	
	

	

	
	
	public static void main(String []args){
		
//		Properties props= new Properties(System.getProperties());
//		props.put("http.proxySet", "true");
//		props.put("http.proxyHost", "wwwcache.sanger.ac.uk");
//		props.put("http.proxyPort", "3128");
//		Properties newprops = new Properties(props);
//		System.setProperties(newprops);
//		System.out.println("set Sanger specific properties");
		
		Das1_6Validator validator=new Das1_6Validator();
		String andy="http://www.ebi.ac.uk/~aj/test/das/sources";
		String ensembl="http://www.ensembl.org/das/sources";
		String dasregistry="http://www.dasregistry.org/das1/sources";
		String myLocalTest="http://localhost:8080/das/sources";
		//validator.validateSourcesCmd("http://www.ensembl.org/das/sources");
		if(validator.validateSourcesCmdShallow(myLocalTest)){
			System.out.println("sourcesCmd Was valid "+validator.validationMessage);
		}else{
			System.out.println("sourcesCmd was invalid"+validator.validationMessage);
		}
	}

	public void setRelaxNgPath(String relaxNgPath) {
		this.relaxNgPath=relaxNgPath;
		
	}
	

}
