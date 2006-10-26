/*
 *                  BioJava development code
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
 * Created on Jul 25, 2006
 *
 */
package org.biojava.dasobert.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/** a class that takes care about opening HttpURLConnections and sets the proper timeouts
 * 
 * @author Andreas Prlic
 * @since 9:58:25 AM
 * @version %I% %G%
 */
public class HttpConnectionTools {
    
    
    public static String VERSION = "0.2";
    
    public static final String PROJECTNAME = "dasobert - Java ";
    
    static Logger logger = Logger.getLogger("org.biojava.spice");
    
    static int    DEFAULT_CONNECTION_TIMEOUT = 15000; // timeout for http connection = 15. sec
    
    
    public HttpConnectionTools() {
        super();
        
    }
    
    /**open HttpURLConnection. Recommended way to open
     * HttpURLConnections, since this take care of setting timeouts
     * properly for java 1.4 and 1.5
     * 
     * @param url URL to oopen
     * @param timeout timeout in milli seconds
     * @return a HttpURLConnection
     * @throws IOException 
     * @throws ConnectException 
     * 
     *
     */
    public static HttpURLConnection openHttpURLConnection(URL url, int timeout)
    throws IOException, ConnectException{
      
        HttpURLConnection huc = null;
        
        huc = (HttpURLConnection) url.openConnection();
        
        String os_name    = java.lang.System.getProperty("os.name");
        String os_version = java.lang.System.getProperty("os.version");
        String os_arch    = java.lang.System.getProperty("os.arch");    
        
        String userAgent = PROJECTNAME+ " " + VERSION + "("+os_name+"; "+os_arch + " ; "+ os_version+")";
        //e.g. "Mozilla/5.0 (Windows; U; Win98; en-US; rv:1.7.2) Gecko/20040803"
        huc.addRequestProperty("User-Agent", userAgent);
        
        
        // this sets the timeouts for Java 1.4
        System.setProperty("sun.net.client.defaultConnectTimeout", ""+timeout);
        System.setProperty("sun.net.client.defaultReadTimeout", ""+timeout);
        
        // for Java 1.5 we need to do this:
        // use reflection to determine if get and set timeout methods for urlconnection are available
        // seems java 1.5 does not watch the System properties any longer...
        // and java 1.4 did not provide the new classes...
      
        try {
            // try to use reflection to set timeout property
            Class urlconnectionClass = Class.forName("java.net.HttpURLConnection");
            
            Method setconnecttimeout = urlconnectionClass.getMethod (
                    "setConnectTimeout", new Class [] {int.class}        
            ); 
            setconnecttimeout.invoke(huc,new Object[] {new Integer(timeout)});
            
            Method setreadtimeout = urlconnectionClass.getMethod (
                    "setReadTimeout", new Class[] {int.class}
            );
            setreadtimeout.invoke(huc,new Object[] {new Integer(timeout)});
            //System.out.println("successfully set java 1.5 timeout");
        } catch (Exception e) {
            //e.printStackTrace();
            // most likely it was a NoSuchMEthodException and we are running java 1.4.
        }
        return huc;
    }
    
    
    /** open HttpURLConnection. Recommended way to open
     * HttpURLConnections, since this take care of setting timeouts
     * properly for java 1.4 and 1.5
     * uses the DEFAULT_CONNECTION_TIMEOUT (= 15 seconds)
     * 
     * @param url a URL to open a http connection to
     * @return HttpURLConnect the opened connection
     * @throws IOException
     * @throws ConnectException
     * 
     * */
    public static HttpURLConnection openHttpURLConnection(URL url) 
    throws IOException, ConnectException {
        
        return openHttpURLConnection(url,DEFAULT_CONNECTION_TIMEOUT);
        
    }
    
    /** connect to DAS server and return result as an InputStream.
     * always asks for response to be in GZIP encoded
     * 
     * @param url the URL to connect to
     * @return an InputStream
     * @throws IOException 
    *
    */    
   public static InputStream getInputStream(URL url) 
   throws IOException
   {
	   return getInputStream(url,true);
   }
   
   /** open a URL and return an InputStream to it
    *  if acceptGzipEncoding == true, use GZIPEncoding to
    *  compress communication
    * 
    * @param url
    * @param acceptGzipEncoding
    * @return an InputStream to the URL
    * @throws IOException
    */
   public static InputStream getInputStream(URL url, boolean acceptGzipEncoding)
   throws IOException {
       InputStream inStream = null ;
       
       //System.out.println("opening connection to "+url);
       HttpURLConnection huc = HttpConnectionTools.openHttpURLConnection(url);  
                       
       if ( acceptGzipEncoding) {
       // should make communication faster
    	   huc.setRequestProperty("Accept-Encoding", "gzip");
       }
       
       String contentEncoding = huc.getContentEncoding();
   
       inStream = huc.getInputStream();
       
       if (contentEncoding != null) {
           if (contentEncoding.indexOf("gzip") != -1) {
               // we have gzip encoding
               inStream = new GZIPInputStream(inStream);               
           }
       }
       
       return inStream;
       
   }
    
}
