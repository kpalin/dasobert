/** 
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * 
 */

package org.biojava.dasobert.das;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.eventmodel.InteractionEvent;
import org.biojava.dasobert.eventmodel.InteractionListener;
import org.biojava.dasobert.util.HttpConnectionTools;

import de.mpg.mpiinf.ag3.dasmi.model.Interaction;

import org.xml.sax.*;
import javax.xml.parsers.*;


/**
 * The interaction thread queries an interaction DAS source and parses its results. 
 * Depending on the outcome of the thread listener methods will be triggered
 * 
 * @author Hagen Blankenburg
 *
 */
public class InteractionThread extends Thread{
    private List <InteractionListener> interactionListeners;
    private InteractionParameters parameters;
    
    
    /**
     * Creates a new thread with the specified parameters
     * @param parameters the parameters to set
     */
    public InteractionThread(InteractionParameters parameters) {
        super();
        this.parameters = parameters;
        interactionListeners = new ArrayList<InteractionListener>();
    }

    
    /**
     * Add a listener to the thread
     * @param listener
     */
    public void addInteractionListener(InteractionListener listener){
    	interactionListeners.add(listener);
    }
    
    
    /**
     * Starts the interaction thread
     */
    public void run() {
    	String query = parameters.getQuery();
        Interaction[] interactions = getInteractions(query);
        
        if (interactions.length == 0) {
        	triggerNoInteractionsFound(interactions);
            return;
        } else{
        	triggerInteractionsFound(interactions);
        }
    }
    
   
        
    /**
     * queries the interactions for a specific interactor id 
     * @param id the accession id of the query interactor
     * @return an array containg all found interactions
     * TODO allow multiple query interactors
     */
    private Interaction[] getInteractions(String id) {
    	Interaction[] interactions = new Interaction[0] ;
        Das1Source dasSource = parameters.getDasSource();
        String  interactionCommand = null  ;
        String url = dasSource.getUrl() ;
        char lastChar = url.charAt(url.length()-1);      
        if (!(lastChar == '/')){
        	url += "/" ;
        }
        url += "interaction?";
        interactionCommand  =  "interactor=" + id ;
        //protect the command
        try {
        	interactionCommand = url +  URLEncoder.encode(interactionCommand,"UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        
        try{
        	interactions = retrieveInteractions(interactionCommand);
        	return interactions ;
            } catch (Exception e) {
            	// TODO determine why there is content in the prolog of the xml document
                //e.printStackTrace();
            }
        return new Interaction[0] ;
    }
    
    
    /**
     * Executes the specific DAS query  
     * @param url the DAS query to be executed
     * @return the interactions returned from the source
     * @throws IOException
     */
    private Interaction[] retrieveInteractions(String url) throws IOException{
    //	System.setProperty("org.xml.sax.driver", "org.apache.xerces.SAXParser");
        URL dasUrl = null;
        try {
            dasUrl = new URL(url);
        } catch (Exception e) {
            throw new IOException("Could not create URL " + e.getMessage());
        }
        //System.out.println("Interaction command issued: "+url);
        InputStream inStream = HttpConnectionTools.getInputStream(dasUrl);
        
        Interaction[] interactions = null;
        InputSource insource = null;
        try{
        	SAXParserFactory spfactory = SAXParserFactory.newInstance();
        	spfactory.setValidating(true);
        	SAXParser saxParser = null ;

        	try{
        	    saxParser = spfactory.newSAXParser();
        	} catch (ParserConfigurationException e) {
        	    e.printStackTrace();
        	}
        	
        	XMLReader xmlreader = saxParser.getXMLReader();

        	try {
        	    xmlreader.setFeature("http://xml.org/sax/features/validation", true);
        	} catch (SAXException e) {
        	    System.err.println("Cannot activate validation."); 
        	}
               	
        	try {
        	    xmlreader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",true);
        	} catch (SAXNotRecognizedException e){
        	    e.printStackTrace();
        	}
        	
        	//
        	// TODO check if a DAS error is returned instead of an xml file ...
        	//
        	
        	DASInteractionXMLParser interactionHandler = new DASInteractionXMLParser() ;
        	xmlreader.setContentHandler(interactionHandler);
        	xmlreader.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
        	insource = new InputSource() ;
        	insource.setByteStream(inStream);
        	xmlreader.parse(insource);
        	
        	interactions = interactionHandler.getInteractions();
        } catch (org.xml.sax.SAXParseException saxe){
        	throw new IOException("Server returned an invalid XML " + saxe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("error during creation of URL " + e.getMessage());
        }
        return interactions;
        
    }
    
  
    /**
     * Triggers the event that a DAS source found some interactions
     * @param query
     * @param interactions
     */
    private void triggerInteractionsFound(Interaction[] interactions){
    	InteractionEvent event = new InteractionEvent(parameters, interactions); 
        Iterator iter = interactionListeners.iterator();
        while (iter.hasNext()){
            InteractionListener li = (InteractionListener ) iter.next();
            //System.out.println("Going to report" + interactions.length + " interactions");
            li.newInteractions(event) ;
        }
    }
   
    
    /**
     * Triggers the event that a das source did not return any interactions
     * @param query
     * @param interactions
     */
    private void triggerNoInteractionsFound(Interaction[] interactions){
        Iterator iter = interactionListeners.iterator();
        while (iter.hasNext()){
            InteractionListener li = (InteractionListener ) iter.next();
            li.noObjectFound(parameters.getQuery()) ;
        }
    }
}
