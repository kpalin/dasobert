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
	        System.out.println("error in errorhandler jw");
	        printSAXParseException( e, MSG_ERROR );
	    }
	    
	    public void fatalError( SAXParseException e ) throws SAXException {
	        hadError = true;
	        System.out.println("fatal error in error handler jw");
	        printSAXParseException( e, MSG_FATAL );
	        throw new ValidationUnrecoverableException(e);
	    }
	    
	    public void warning( SAXParseException e ) {
	    	System.out.println("warning in error handler jw");
	        printSAXParseException( e, MSG_WARNING );
	    }
	    
	    protected  void printSAXParseException( SAXParseException spe, String prop ) {
	    	message+="sax exception in error handler\n";
	        message+="Line Number:"+spe.getLineNumber()+" column number:"+spe.getColumnNumber()+" system id:"+ spe.getSystemId()+" "+spe.getLocalizedMessage()+"\n";
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
	        System.out.println("count check exception in error handler jw");
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

}