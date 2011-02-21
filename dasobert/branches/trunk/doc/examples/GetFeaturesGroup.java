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
 * Created on Nov 20, 2005
 *
 */


import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.das.FeatureThread;
import org.biojava.dasobert.eventmodel.FeatureListener;
import org.biojava.dasobert.eventmodel.FeatureEvent;
import org.biojava.dasobert.feature.FeatureTrackConverter;
import org.biojava.dasobert.feature.FeatureTrack;

import java.util.Map;

/** an example that first connects to the DAS registration server,
 * then selects all DAS-sources that are in <i>UniProt,Protein
 * sequence</i> coordinate system and then does feature requests for
 * them.
 */
public class GetFeaturesGroup {


    public static void main (String[] args) {

	String segment = "7:130000000,140077686";
	if ( args.length == 1 ) {
	    segment = args[0];
	}

	GetFeaturesGroup f = new GetFeaturesGroup();
	f.showExample(segment);
	
	

    }
    
    public void showExample(String accessionCode) {
	try {

	    // first we set some system properties
	   
	    // make sure we use the Xerces XML parser..
	    System.setProperty("javax.xml.parsers.DocumentBuilderFactory","org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
	    System.setProperty("javax.xml.parsers.SAXParserFactory","org.apache.xerces.jaxp.SAXParserFactoryImpl");

	    // if you are behind a proxy, please uncomment the following lines
	    System.setProperty("proxySet","true");
	    System.setProperty("proxyHost","wwwcache.sanger.ac.uk");
	    System.setProperty("proxyPort","3128");


	    
	    Das1Source dasSource = new Das1Source();
	    
	    dasSource.setUrl("http://das.sanger.ac.uk/das/cosmic_transcripts_ncbi36");

	    // we want to get the features for this UniProt entry:
	   
	    

	    requestFeatures(accessionCode,dasSource);

	    
	    // do a loop over 10 seconds. the das sources really should respond during this time.
	    int i = 0 ;
	    while (true){
		System.out.println(i  + " seconds have passed");
		i++;	
		Thread.sleep(1000);
		if ( i > 10) {
		    System.err.println("We assume that das source do not take more than 10 seconds to provide a response.");
		    System.out.println("In case you see SAX parser exceptions above - they are the result of some DAS servers not having any features in their responses and this can be ignored");
		    System.exit(1);
		}	    
	    }	
	} catch (Exception e){
	    e.printStackTrace();
	}
    }

   
    
  


   
 
   
    /** request the features for a singe das source.
     */
    private void requestFeatures(String accessionCode, Das1Source source) {
	
	// that is the class that listens to features
	FeatureListener listener = new MyListener();

	// now create the thread that will do the DAS requests
	FeatureThread thread = new FeatureThread(accessionCode, source);
	
	// and register the listener
	thread.addFeatureListener(listener);

	// launch the thread
	thread.start();
	
    }

    class MyListener 
	implements FeatureListener{
	public synchronized void newFeatures(FeatureEvent e){
	    Das1Source ds = e.getSource();
	    Map<String,String>[] features = e.getFeatures();

	    System.out.println("das source " + ds.getNickname() + " returned " + features.length +" features");
	    if ( features.length>0) {
	    	
	    	// lets convert the features into FeatureTrack objects.
	    	
	    	// we don't care about the stylesheet in this case
	    	// and we assume this is not a Histogram DAS source, 
	    	// otherwise the conversion would work differently...
	    	FeatureTrackConverter conv = new FeatureTrackConverter();
	    	FeatureTrack[] tracks = conv.convertMap2Features(features);
	    	
	    	System.out.println("got " + tracks.length + " tracks");
	    	for( FeatureTrack track : tracks){
	    		System.out.println(track );
	    	}

	    	
	    	
	    }
	    	
	    System.exit(0);
	}

	public void comeBackLater(FeatureEvent e){}
    }
}
