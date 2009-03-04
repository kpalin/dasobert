package org.biojava.dasobert.das.validation;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.iso_relax.dispatcher.Dispatcher;
import org.iso_relax.dispatcher.SchemaProvider;
import org.iso_relax.dispatcher.impl.DispatcherImpl;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.sun.msv.driver.textui.Debug;
import com.sun.msv.driver.textui.DebugController;
import com.sun.msv.driver.textui.ReportErrorHandler;
import com.sun.msv.grammar.Grammar;
import com.sun.msv.grammar.relax.RELAXModule;
import com.sun.msv.grammar.util.ExpressionPrinter;
import com.sun.msv.reader.util.GrammarLoader;
import com.sun.msv.relaxns.grammar.RELAXGrammar;
import com.sun.msv.util.Util;
import com.sun.msv.verifier.DocumentDeclaration;
import com.sun.msv.verifier.Verifier;
import com.sun.msv.verifier.regexp.REDocumentDeclaration;

/**
 * 
 * @author JWarren
 */
public class RegistryRelaxNG {

	static SAXParserFactory factory;
	private String regMessage="";
	private RegReportErrorHandler reh;

	public static void main(String[] args) throws Exception {

	}

	public int validate(String grammarName, String instName)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, MalformedURLException, IOException,
			FactoryConfigurationError, ParserConfigurationException, Exception,
			SAXException {
		System.out.println("starting relaxng validate method here");
		regMessage += " RegistryRelaxNG validation:\n";

		boolean verbose = false;
		boolean warning = true;
		boolean standalone = true;// true means we ignore dtd documents
									// specified at the top of the xml and
									// validate only with relaxng
		boolean strict = false;
		boolean usePanicMode = true;
		EntityResolver entityResolver = null;

		if (grammarName == null) {
			regMessage += localize(MSG_USAGE) + "\n";
			return -1;
		}

		if (factory == null)
			factory = SAXParserFactory.newInstance();

		if (verbose) {
			regMessage += localize(MSG_PARSER, Util.which(factory.getClass()))
					+ "\n";
		}

		factory.setNamespaceAware(true);
		factory.setValidating(false);
		if (!standalone && verbose)
			regMessage += localize(MSG_DTDVALIDATION) + "\n";

		if (standalone)
			try {
				factory.setFeature("http://xml.org/sax/features/validation",
						false);
				factory
						.setFeature(
								"http://apache.org/xml/features/nonvalidating/load-external-dtd",
								false);
			} catch (Exception e) {
				// e.printStackTrace();
				regMessage += localize(MSG_FAILED_TO_IGNORE_EXTERNAL_DTD)
						+ "\n";
			}
		else
			try {
				factory.setFeature(
						"http://apache.org/xml/features/validation/dynamic",
						true);
				// turn off XML Schema validation if Xerces is used
				factory.setFeature(
						"http://apache.org/xml/features/validation/schema",
						false);
			} catch (Exception e) {
				;
			}
		System.out.println("set intitial arguments by here!!");

		// parse schema
		// --------------------
		final long stime = System.currentTimeMillis();

		if (verbose)
			regMessage += localize(MSG_START_PARSING_GRAMMAR) + "\n";

		Grammar grammar = null;
		try {
			GrammarLoader loader = new GrammarLoader();
			System.out.println("grammer loader loaded here");
			// set various parameters
			loader.setController(new DebugController(warning, false,
					entityResolver));
			loader.setSAXParserFactory(factory);
			loader.setStrictCheck(strict);

			grammar = loader.parse(grammarName);

		} catch (SAXParseException spe) {
			if (Debug.debug)
				//spe.getException().printStackTrace();
			//; // this error is already reported.
				spe.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
			if (se.getException() != null)
				throw se.getException();
			throw se;
		}
		if (grammar == null) {
			regMessage += localize(ERR_LOAD_GRAMMAR);
			return -1;
		}

		long parsingTime = System.currentTimeMillis();
		if (verbose)
			regMessage += localize(MSG_PARSING_TIME, new Long(parsingTime
					- stime))
					+ "\n";

		System.out.println("set time here");

		// validate documents
		// --------------------
		DocumentVerifier verifier;

		// validate normally by using Verifier.
		// used by dasreg so can avoid looking at dtds that are historic
		verifier = new SimpleVerifier(new REDocumentDeclaration(grammar));

		boolean allValid = true;
		System.out
				.println("starting to actually validate documents here before for loop");

		regMessage += localize(MSG_VALIDATING, instName) + "\n";

		boolean result = false;
		System.out.println("just before setting up xml reader");
		try {
			XMLReader reader = factory.newSAXParser().getXMLReader();
			if (entityResolver != null)
				reader.setEntityResolver(entityResolver);
			reader.setErrorHandler(new RegReportErrorHandler());
			System.out.println("set handler");

			result = verifier.verify(reader, Util.getInputSource(instName),
					usePanicMode);
			System.out.println("result is here, result=" + result);
		} catch (com.sun.msv.verifier.ValidationUnrecoverableException vv) {
			vv.printStackTrace();
			regMessage += localize(MSG_BAILOUT) + "\n";
		} catch (SAXParseException se) {
			if (se.getException() != null)
				regMessage += "sax---"
						+ se.getException().getLocalizedMessage();
			; // error is already reported by ErrorHandler
		} catch (SAXException e) {
			if (e.getException() != null)
				regMessage += "Sax exception in RegistryRelaxNGValidator"
						+ e.getException().getLocalizedMessage();
		}
		regMessage += reh.getMessage();
		System.out.println("localising some messages here");
		if (result)
			regMessage += localize(MSG_VALID) + "\n";
		else {
			regMessage += localize(MSG_INVALID) + "\n";
			allValid = false;
		}

		if (verbose)
			regMessage += localize(MSG_VALIDATION_TIME, new Long(System
					.currentTimeMillis()
					- parsingTime))
					+ "\n";
		int test = allValid ? 0 : -1;
		System.out.println("int test" + test);
		return test;
	}

	
	/** acts as a function closure to validate a document. */
	private interface DocumentVerifier {
		boolean verify(XMLReader p, InputSource instance, boolean usePanicMode)
				throws Exception;
	}

	/** validates a document by using divide &amp; validate framework. */
	private static class RELAXNSVerifier implements DocumentVerifier {
		private final SchemaProvider sp;

		RELAXNSVerifier(SchemaProvider sp) {
			this.sp = sp;
		}

		public boolean verify(XMLReader p, InputSource instance,
				boolean panicMode) throws Exception {
			System.out.println("verifying using RELAXNSVerifier ");
			Dispatcher dispatcher = new DispatcherImpl(sp);
			dispatcher.attachXMLReader(p);
			ReportErrorHandler errorHandler = new ReportErrorHandler();
			dispatcher.setErrorHandler(errorHandler);

			// TODO: support the panicMode argument
			p.parse(instance);
			return !errorHandler.hadError;
		}
	}

	private class SimpleVerifier implements DocumentVerifier {
		private final DocumentDeclaration docDecl;

		SimpleVerifier(DocumentDeclaration docDecl) {
			this.docDecl = docDecl;
		}

		public boolean verify(XMLReader p, InputSource instance,
				boolean panicMode) throws Exception {
			System.out.println("verifying using SimpleVerifier ");
			reh = new RegReportErrorHandler();
			Verifier v = new Verifier(docDecl, reh);
			v.setPanicMode(panicMode);

			p.setDTDHandler(v);
			p.setContentHandler(v);
			p.setErrorHandler(reh);

			p.parse(instance);
			return v.isValid();
		}
	}

	public static String localize(String propertyName, Object[] args) {
		String format = java.util.ResourceBundle.getBundle(
				"com.sun.msv.driver.textui.Messages").getString(propertyName);
		return java.text.MessageFormat.format(format, args);
	}

	public static String localize(String prop) {
		return localize(prop, null);
	}

	public static String localize(String prop, Object arg1) {
		return localize(prop, new Object[] { arg1 });
	}

	public static String localize(String prop, Object arg1, Object arg2) {
		return localize(prop, new Object[] { arg1, arg2 });
	}

	public static final String MSG_DTDVALIDATION = "Driver.DTDValidation";
	public static final String MSG_PARSER = "Driver.Parser";
	public static final String MSG_USAGE = "Driver.Usage";
	public static final String MSG_UNRECOGNIZED_OPTION = "Driver.UnrecognizedOption";
	public static final String MSG_START_PARSING_GRAMMAR = "Driver.StartParsingGrammar";
	public static final String MSG_PARSING_TIME = "Driver.ParsingTime";
	public static final String MSG_VALIDATING = "Driver.Validating";
	public static final String MSG_VALIDATION_TIME = "Driver.ValidationTime";
	public static final String MSG_VALID = "Driver.Valid";
	public static final String MSG_INVALID = "Driver.Invalid";
	public static final String ERR_LOAD_GRAMMAR = "Driver.ErrLoadGrammar";
	public static final String MSG_BAILOUT = "Driver.BailOut";
	public static final String MSG_FAILED_TO_IGNORE_EXTERNAL_DTD = "Driver.FailedToIgnoreExternalDTD";
	public static final String MSG_WARNING_FOUND = "Driver.WarningFound";

	public String getRegMessage() {
		return regMessage;

	}

	public int validateCatchingExceptions(String rng, String input) {
		int valid = 0;
		try {
			valid = this.validate(rng, input);
		} catch (MalformedURLException e) {

			regMessage += "Problem with URL "+e.getLocalizedMessage();
			return -1;
		} catch (InstantiationException e) {

			regMessage +="Problem with Instantiation "+ e.getLocalizedMessage();
			return -1;
		} catch (IllegalAccessException e) {

			regMessage +="Problem with Illegal Access? "+ e.getLocalizedMessage();
			return -1;
		} catch (ClassNotFoundException e) {

			regMessage += "Class not found problem "+e.getLocalizedMessage();
			return -1;
		} catch (IOException e) {

			System.out.println("error in IO"+e.getLocalizedMessage());
			System.out.println("Setting io error in message");
			regMessage += "error in IO data source may not be found at "+e.getLocalizedMessage();
			return -1;
		} catch (FactoryConfigurationError e) {

			regMessage += "Problem with Factory configuration "+e.getLocalizedMessage();
			return -1;
		} catch (ParserConfigurationException e) {

			regMessage +="Problem with Parser Configuration "+ e.getLocalizedMessage();
			return -1;
		} catch (SAXException e) {

			regMessage +="SAXException problem "+ e.getLocalizedMessage();
			return -1;
		} catch (Exception e) {

			regMessage += "Problem with a Non Specific Exception "+e.getLocalizedMessage();
			return -1;
		}
		return valid;
	}

}
