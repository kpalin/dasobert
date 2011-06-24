package org.biojava.dasobert.das.validation;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.msv.verifier.ValidationUnrecoverableException;

/**                    BioJava development code
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
 * {@link ErrorHandler} that reports all errors and warnings.
 * 
 * SAX parse errors are also handled.
 * 
 * @author JWarren
 */

public class RegReportErrorHandler implements ErrorHandler {

	private int counter = 0;
	public boolean hadError = false;
	private String message = "";

	public void error(SAXParseException e) throws SAXException {
		
		//System.out.println("e.getmessage()="+e.getMessage());
		//if error is type of namespace then ignore
//		if(e.getLocalizedMessage().contains("namespace")){
//			hadError=false;
//			System.out.println("namespace type error ignoring in errorhandler!!!!"+e.getLocalizedMessage());
//		}else{
			hadError = true;
			countCheck(e);
			printSAXParseException(e, MSG_ERROR);
		//}
		

		
	}

	public void fatalError(SAXParseException e) throws SAXException {
		hadError = true;

		printSAXParseException(e, MSG_FATAL);
		throw new ValidationUnrecoverableException(e);
	}

	public void warning(SAXParseException e) {

		printSAXParseException(e, MSG_WARNING);
	}

	protected void printSAXParseException(SAXParseException spe, String prop) {

		String err = prop
				+ " Line Number:"
				+ spe.getLineNumber()
				+ " column number:"
				+ spe.getColumnNumber()
//				+ " system id:"
//				+ spe.getSystemId()
				+ " "
				+ RegReportErrorHandler.stringToHTMLString(spe
						.getLocalizedMessage()) + "\n";
		message += err;

		
//		message += "original message returned="+RegistryRelaxNG.localize( prop, new Object[]{ new
//		 Integer(spe.getLineNumber()), new Integer(spe.getColumnNumber()),
//		 spe.getSystemId(), spe.getLocalizedMessage()} );
		 
	}

	private void countCheck(SAXParseException e)
			throws ValidationUnrecoverableException {
		if (counter++ < 20)
			return;
		System.out.println("count check exception in RegReportErrorHandler");
		message += "too many erros";
		/* System.out.println( Driver.localize(MSG_TOO_MANY_ERRORS) ); */
		throw new ValidationUnrecoverableException(e);
	}

	public static final String MSG_TOO_MANY_ERRORS = // arg:1
	"RegReportErrorHandler.TooManyErrors";
	public static final String MSG_ERROR = // arg:4
	"RegReportErrorHandler.Error";
	public static final String MSG_WARNING = // arg:4
	"RegReportErrorHandler.Warning";
	public static final String MSG_FATAL = // arg:4
	"RegReportErrorHandler.Fatal";

	public String getMessage() {
		return message;
	}

	/**
	 * Converts a string containing elements into a string which will show these
	 * elements in html
	 * 
	 * @param string
	 *            to convert
	 * @return html friendly string
	 */
	public static String stringToHTMLString(String string) {
		StringBuffer sb = new StringBuffer(string.length());
		// true if last char was blank
		boolean lastWasBlankChar = false;
		int len = string.length();
		char c;

		for (int i = 0; i < len; i++) {
			c = string.charAt(i);
			if (c == ' ') {
				// blank gets extra work,
				// this solves the problem you get if you replace all
				// blanks with &nbsp;, if you do that you loss
				// word breaking
				if (lastWasBlankChar) {
					lastWasBlankChar = false;
					sb.append("&nbsp;");
				} else {
					lastWasBlankChar = true;
					sb.append(' ');
				}
			} else {
				lastWasBlankChar = false;
				//
				// HTML Special Chars
				if (c == '"')
					sb.append("&quot;");
				else if (c == '&')
					sb.append("&amp;");
				else if (c == '<')
					sb.append("&lt;");
				else if (c == '>')
					sb.append("&gt;");
				else if (c == '\n')
					// Handle Newline
					sb.append("&lt;br/&gt;");
				else {
					int ci = 0xffff & c;
					if (ci < 160)
						// nothing special only 7 Bit
						sb.append(c);
					else {
						// Not 7 Bit use the unicode system
						sb.append("&#");
						sb.append(new Integer(ci).toString());
						sb.append(';');
					}
				}
			}
		}
		return sb.toString();
	}

}