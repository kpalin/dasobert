package org.biojava.dasobert.das.validation;


import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * * class that validates using relaxNG and jing classes. Not used by the registry as this can't ignore old dtds specified in old xml returned 
 * from old das sources
 * @author jw12
 *
 */
public class RelaxNGValidatorJing {

	private static String PATH="http://deskpro20727.dynamic.sanger.ac.uk:8080/dasregistryOID/";
	public static String SOURCES="validation/sources.rng";
	public static String SEQUENCE="validation/sequence.rng";
	public static String FEATURE="validation/feature.rng";
	public static String TYPES="validation/types.rng";
	public static String STRUCTURE="validation/structure.rng";
	public static String ALIGNMENT="validation/alignment.rng"; 
	public static String ENTRY_POINTS="validation/entry_points.rng";
		
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 URL schemaLocation=null;
		try {
			schemaLocation = new URL(PATH +SOURCES);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     String input ="http://www.ensembl.org/das/sources";
	     RelaxNGValidatorJing validator=new RelaxNGValidatorJing();
	     validator.validateUsingRelaxNG(schemaLocation, input);
	     
	     validator.validateUsingRelaxNG(RelaxNGValidatorJing.SOURCES, input);
	     
	}
	
	public boolean validateUsingRelaxNG(URL schemaLocation, String input){
		boolean isValid=true;

		SchemaFactory sf = setSchemaFactory();
        try {
			Schema schema = sf.newSchema(schemaLocation);
			
			try {
				System.out.println("Validating "+input);
				schema.newValidator().validate(new StreamSource(input));
			} catch (IOException e) {
				e.printStackTrace();
			}
            
		} catch (SAXException e) {
			e.printStackTrace();
		}
	
		return isValid;
		
		
	}
	
	public boolean validateUsingRelaxNG(String cmdType, String input){
		boolean isValid=true;

		
		SchemaFactory sf = setSchemaFactory();
        try {
			Schema schema=null;
			try {
				schema = sf.newSchema(new URL(RelaxNGValidatorJing.PATH+cmdType));
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				System.out.println("Validating "+input);
				schema.newValidator().validate(new StreamSource(input));
			} catch (IOException e) {
				e.printStackTrace();
			}
            
		} catch (SAXException e) {
			e.printStackTrace();
		}
	
		return isValid;
		
		
	}
	
	

	private SchemaFactory setSchemaFactory() {
		System.setProperty("javax.xml.validation.SchemaFactory:" + XMLConstants.RELAXNG_NS_URI, "org.iso_relax.verifier.jaxp.validation.RELAXNGSchemaFactoryImpl");
       
              

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
		return sf;
	}

}
