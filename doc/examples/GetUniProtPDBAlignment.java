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
 * Created on Mar 6, 2007
 * 
 */

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.biojava.bio.Annotation;
import org.biojava.bio.program.das.dasalignment.Alignment;
import org.biojava.dasobert.das.AlignmentParameters;
import org.biojava.dasobert.das.AlignmentThread;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.eventmodel.AlignmentEvent;
import org.biojava.dasobert.eventmodel.AlignmentListener;


/** Example for how to get the Uniprot to PDB mapping from the mapping server
 * 
 */
public class GetUniProtPDBAlignment implements AlignmentListener {

	
	public static void main(String[] args){

		GetUniProtPDBAlignment example = new GetUniProtPDBAlignment();
		example.showExample();
	}

	public void showExample(){
		
		// use the Sax XML parser
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
		"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		System.setProperty("javax.xml.parsers.SAXParserFactory",
		"org.apache.xerces.jaxp.SAXParserFactoryImpl");

//		if you are behind a proxy, please uncomment the following lines
		System.setProperty("proxySet", "true");
		System.setProperty("proxyHost", "wwwcache.sanger.ac.uk");
		System.setProperty("proxyPort", "3128");
		
		
		Das1Source dasSource = new Das1Source();

		dasSource.setUrl("http://das.sanger.ac.uk/das/msdpdbsp/");
		
		
		AlignmentParameters params = new AlignmentParameters();

		params.setDasSource(dasSource);
		params.setQuery("P50225");
		
		AlignmentThread thread =    new AlignmentThread(params);
		thread.addAlignmentListener(this);

		thread.start();
		
//		 do an (almost) endless loop which is terminated in the AlignmentListener...
		
		try {
			int i = 0;
			while (true) {

				System.out.println(i + " seconds have passed");
				i++;
				Thread.sleep(1000);
				if (i > 100) {
					System.err.println("something went wrong. Perhaps a proxy problem?");
					System.exit(1);
				}

			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public void clearAlignment() {
	   System.out.println("clearAlignment");
		
	}

	public void newAlignment(AlignmentEvent e) {
		
		System.out.println("got alignment " +e.getAlignment());
		
		
		Alignment ali = e.getAlignment();
		
		Annotation[] objects = ali.getObjects();
		
		for (int i=0; i< objects.length;i++){
			Annotation object = objects[i];
			
			Map m = object.asMap();
			
			Set s = m.keySet();
			Iterator iter = s.iterator();
			while (iter.hasNext()){
				String key = (String)iter.next();
				System.out.println("key: " + key + " value:" + m.get(key));
			}
			
		}
		System.exit(0);
	}

	public void noAlignmentFound(AlignmentEvent e) {
		System.out.println("no alignment was found");
	
		
	}
}
