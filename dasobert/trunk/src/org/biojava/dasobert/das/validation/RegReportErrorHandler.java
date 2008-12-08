package org.biojava.dasobert.das.validation;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.msv.verifier.ValidationUnrecoverableException;

/**
 * {@link ErrorHandler} that reports all errors and warnings.
 * 
 * SAX parse errors are also handled.
 * 
 * @author JWarren
 */

public class RegReportErrorHandler implements ErrorHandler{

	
	
	    
	    private int counter = 0;
	    public boolean hadError = false;
	    private String message="";
	    
	    public void error( SAXParseException e ) throws SAXException {
	        hadError = true;
	        countCheck(e);
	        System.out.println("error in RegReportErrorHandler");
	        printSAXParseException( e, MSG_ERROR );
	    }
	    
	    public void fatalError( SAXParseException e ) throws SAXException {
	        hadError = true;
	        System.out.println("fatal error in RegReportErrorHandler");
	        printSAXParseException( e, MSG_FATAL );
	        throw new ValidationUnrecoverableException(e);
	    }
	    
	    public void warning( SAXParseException e ) {
	    	System.out.println("warning in RegReportErrorHandler");
	        printSAXParseException( e, MSG_WARNING );
	    }
	    
	    protected  void printSAXParseException( SAXParseException spe, String prop ) {
	    	message+="sax exception in RegReportErrorHandler\n";
	        String err="Line Number:"+spe.getLineNumber()+" column number:"+spe.getColumnNumber()+" system id:"+ spe.getSystemId()+" "+RegReportErrorHandler.stringToHTMLString(spe.getLocalizedMessage())+"\n";
	        message+=err;
	        
	        /*System.out.println(
	            Driver.localize( prop, new Object[]{
	                new Integer(spe.getLineNumber()), 
	                new Integer(spe.getColumnNumber()),
	                spe.getSystemId(),
	                spe.getLocalizedMessage()} ) );*/
	    }
	    
	    
	    private void countCheck( SAXParseException e )
	        throws ValidationUnrecoverableException    {
	        if( counter++ < 20 )    return;
	        System.out.println("count check exception in RegReportErrorHandler");
	        message+="too many erros";
	       /* System.out.println( Driver.localize(MSG_TOO_MANY_ERRORS) );*/
	        throw new ValidationUnrecoverableException(e);
	    }
	    
	    public static final String MSG_TOO_MANY_ERRORS = //arg:1
	        "ReportErrorHandler.TooManyErrors";
	    public static final String MSG_ERROR = // arg:4
	        "ReportErrorHandler.Error";
	    public static final String MSG_WARNING = // arg:4
	        "ReportErrorHandler.Warning";
	    public static final String MSG_FATAL = // arg:4
	        "ReportErrorHandler.Fatal";
	

	public String getMessage(){
	return message;
	}
	
	/**
	 * Converts a string containing elements into a string which will show these elements in html
	 * @param string to convert
	 * @return html freindly string
	 */
	public static String stringToHTMLString(String string) {
	    StringBuffer sb = new StringBuffer(string.length());
	    // true if last char was blank
	    boolean lastWasBlankChar = false;
	    int len = string.length();
	    char c;

	    for (int i = 0; i < len; i++)
	        {
	        c = string.charAt(i);
	        if (c == ' ') {
	            // blank gets extra work,
	            // this solves the problem you get if you replace all
	            // blanks with &nbsp;, if you do that you loss 
	            // word breaking
	            if (lastWasBlankChar) {
	                lastWasBlankChar = false;
	                sb.append("&nbsp;");
	                }
	            else {
	                lastWasBlankChar = true;
	                sb.append(' ');
	                }
	            }
	        else {
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
	                if (ci < 160 )
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