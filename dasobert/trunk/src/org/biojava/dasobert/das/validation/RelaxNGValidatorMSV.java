package org.biojava.dasobert.das.validation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.iso_relax.verifier.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.sun.msv.driver.textui.ReportErrorHandler;

import com.sun.msv.driver.textui.*;
/**
 * 
 * @author jw12
 *
 */
public class RelaxNGValidatorMSV {

	private static String PATH = "http://deskpro20727.dynamic.sanger.ac.uk:8080/dasregistryOID/";
	public static String SOURCES = "validation/sources.rng";
	public static String SEQUENCE = "validation/sequence.rng";
	public static String FEATURE = "validation/feature.rng";
	public static String TYPES = "validation/types.rng";
	public static String STRUCTURE = "validation/structure.rng";
	public static String ALIGNMENT = "validation/alignment.rng";
	public static String ENTRY_POINTS = "validation/entry_points.rng";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	private String message;

	public boolean validateUsingRelaxNG(String cmdType, String input) {
		boolean isValid = true;
		boolean ignoreExternalDocs = true;

		RegistryRelaxNG rng = new RegistryRelaxNG();
		if (-1 == rng.validateCatchingExceptions(PATH + cmdType, input)) {
			isValid = false;
			this.message = rng.getRegMessage();
			//System.out.println("valmsg in msv=" + rng.getRegMessage());

		}
		return isValid;

	}

	public String getMessage() {

		return message;
	}

}
