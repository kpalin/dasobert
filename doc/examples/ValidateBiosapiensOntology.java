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
 * Created on Jan 21, 2008
 * 
 */

import java.util.Map;
import org.biojava.bio.program.das.dasalignment.DASException;
import org.biojava.dasobert.das.FeatureThread;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.Das1Validator;
import org.biojava.dasobert.eventmodel.FeatureEvent;
import org.biojava.dasobert.eventmodel.FeatureListener;


public class ValidateBiosapiensOntology {

	public static void main(String[] args){
		String accessionCode = "P00123";
		if ( args.length == 1 ) {
			accessionCode = args[0];
		}

		ValidateBiosapiensOntology f = new ValidateBiosapiensOntology();
		f.showExample(accessionCode);
	}


	

	public ValidateBiosapiensOntology(){

		
		

	}


	public void showExample(String accessionCode){
		try {

//			first we set some system properties

			// make sure we use the Xerces XML parser..
			System.setProperty("javax.xml.parsers.DocumentBuilderFactory","org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
			System.setProperty("javax.xml.parsers.SAXParserFactory","org.apache.xerces.jaxp.SAXParserFactoryImpl");

			// if you are behind a proxy, please uncomment the following lines
			System.setProperty("proxySet","true");
			System.setProperty("proxyHost","wwwcache.sanger.ac.uk");
			System.setProperty("proxyPort","3128");



			Das1Source dasSource = new Das1Source();

			dasSource.setUrl("http://www.ebi.ac.uk/das-srv/uniprot/das/uniprot/");

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

//		that is the class that listens to features
		FeatureListener listener = new MyListener();

//		now create the thread that will do the DAS requests
		FeatureThread thread = new FeatureThread(accessionCode, source);

//		and register the listener
		thread.addFeatureListener(listener);

//		launch the thread
		thread.start();

	}

	class MyListener 
	implements FeatureListener{
		public synchronized void newFeatures(FeatureEvent e){
			Das1Source ds = e.getSource();
			Map<String,String>[] features = e.getFeatures();

			System.out.println("das source " + ds.getNickname() + " returned " + features.length +" features");
			if ( features.length>0) {

				Das1Validator validator = new Das1Validator();
				
		
				System.out.println("got " + features.length + " tracks");
				int i = 0;
				for( Map<String,String>feature : features){
					i++;
					System.out.println("*** validating track " + i +": " + feature.get("id"));
					try {
						if ( validator.validateTrack(feature) ){
							System.out.println("track ok!");
						}
					} catch (DASException ex){
						System.out.println(ex.getMessage());
						System.out.println("this DAS source does NOT comply to the BioSapiens ontology");

					}
				}
			}

			System.exit(0);
		}

		public void comeBackLater(FeatureEvent e){}
	}
}


