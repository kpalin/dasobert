import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

import org.biojava.bio.program.das.dasalignment.DASException;
import org.biojava.dasobert.das.InteractionParameters;
import org.biojava.dasobert.das.InteractionThread;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.DasSource;
import org.biojava.dasobert.dasregistry.DasCoordinateSystem;
import org.biojava.dasobert.das.InteractionDasSource;
import org.biojava.dasobert.das2.Das2Source;
import org.biojava.dasobert.das2.DasSourceConverter;
import org.biojava.dasobert.das2.io.DasSourceReaderImpl;
import org.biojava.dasobert.eventmodel.InteractionEvent;
import org.biojava.dasobert.eventmodel.InteractionListener;

import de.mpg.mpiinf.ag3.dasmi.model.Interaction;

/**
 * Small test class showing the exemplary use of the DASMI extension
 * @author Hagen Blankenburg
 *
 */
public class GetInteraction {
	private  Map <String,InteractionDasSource>sources = new HashMap<String,InteractionDasSource>(); // the DAS sources, coming from the registry
	
	private  int activeThreads = 0;
	
	public static void main(String[] args) {
		
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
		
//		 if you are behind a proxy, please uncomment the following lines
	    System.setProperty("proxySet","true");
	    System.setProperty("proxyHost","wwwcache.sanger.ac.uk");
	    System.setProperty("proxyPort","3128");
		
		
		GetInteraction me = new GetInteraction();
		try {
			me.getInteractionDasSources("http://dasmi.bioinf.mpi-inf.mpg.de/sources");
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		// first load the XML file containing all the DAS sources from a DAS regisrtry
		
		// in this example we want to query bz an Entrez Gene_ID, so prepare the query coordinate system
		DasCoordinateSystem
		queryCoordSys = new DasCoordinateSystem();
		queryCoordSys.setUniqueId("CS_DS16");
		queryCoordSys.setCategory("Gene_ID");
		queryCoordSys.setName("Entrez");
		// and start the query
		me.querySources("1212", queryCoordSys);
	}
	
	public GetInteraction(){

		
	}
	
	/**
	 * Retrieves the interactions from all the active sources. To this end starts an InteractionThread
	 * for each source. The results of these threads will then be reported to the myListener class
	 * @param query The query
	 * @param queryCoordSys 
	 */
	public void querySources(String query, DasCoordinateSystem queryCoordSys){
		try {
			// iterate over all interaction DAS sources 
			Iterator it = sources.entrySet().iterator();
			while (it.hasNext()){
		        Map.Entry pair = (Map.Entry)it.next();
		        InteractionDasSource source = (InteractionDasSource) pair.getValue();
		        // ... but only use the active ones ...
				if (source.getIsActive()){
					// ... that are compatible to the query coordinate system
					if (source.getIsCompatible(queryCoordSys)){
						// prepare the parameters, equipt the thread, add a listener and start the whole thing
						InteractionParameters params = new InteractionParameters();
						params.setQuery(query);
						params.setDasSource(source);
						InteractionThread thread = new InteractionThread(params);
						thread.setName(source.getNickname() + "_" + query + "_thread");
						InteractionListener listener = new MyListener();
						thread.addInteractionListener(listener);
						//System.out.println("--> Starting thread for acc " + query + " on datasource " + source.getNickname());
						thread.start();
						activeThreads++;
					}
				}
			}
			// loops until there is only the main thread left
			int i = 0;
			while (activeThreads > 0) {
				System.out.println(i + "/10th seconds have passed");
				i++;
				Thread.sleep(1000); //sleep a second
				if (i > (10 * 10)) {
					System.out.println("Still active, thus ignored: " + Thread.activeCount());
					return;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Requests all the das1 sources from a DAS registry and stores those capable of the interaction extension.
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws DASException
	 */
	private  void getInteractionDasSources(String registryUrl)throws MalformedURLException, DASException {
		Set <DasSource> das1sources = new HashSet<DasSource>();
		//Map interactionDasSources = new HashMap();
		DasSourceReaderImpl reader = new DasSourceReaderImpl();
		URL url = new URL(registryUrl);
		// firstly retrieve all the sources
		DasSource[] srcs = reader.readDasSource(url);
		//System.out.println("Received " + srcs.length + " sources from "+ url +", now filtering out the interaction sources");
		// now check whether they are das1 or das2 and interaction capable
		for (int i = 0; i < srcs.length; i++) {
			DasSource ds = srcs[i];
			// if it is a das2 source, check whether we can treat it as a das1 source

			String[] caps = ds.getCapabilities();
			boolean isInteractionSource = false;
			for (int c = 0; c < caps.length; c++) {
				if (caps[c].equals("interaction")) {
					isInteractionSource = true;
				}
			}
			if (isInteractionSource) {
				das1sources.add(ds);
			}

		}
		
		// convert the found sources to interaction sources and sort the QM sources out
		Iterator it = das1sources.iterator();
		while (it.hasNext()){
			InteractionDasSource ids = InteractionDasSource.fromDasSource((Das1Source) it.next());
			if(!sources.containsKey(ids.getId())){
				sources.put(ids.getId(), ids);
			}
		}
	}
	
		
	
	
	
	/**
	 * A listener for the interaction thread, handles potential outcomes like
	 * new interactions or no interactions found
	 * 
	 * @author Hagen
	 * 
	 */
	private class MyListener implements InteractionListener {
		
		/**
		 * Called if a thread found interactions within its datasource
		 */
		public synchronized void newInteractions(InteractionEvent event) {
			activeThreads--;
			Interaction[] inters = event.getInteractions();
			System.out.println("--> " + Thread.currentThread().getName() + " found " + inters.length + " interactions");
			// do something usefull with the returned interactions ...
			for (int i = 0; i < inters.length; i++){
				System.out.println(inters[i]);
			}
		}

		/**
		 * If the thread has not found interactions for the requested interactor
		 * in the datasource
		 */
		public synchronized void noObjectFound(String id) {
			activeThreads--;
			System.out.println("--> " + Thread.currentThread().getName() + " did not found any interactions");
		}

		/**
		 * Not implemented yet
		 */
		public synchronized void comeBackLater() {
			activeThreads--;
		}

		/**
		 * Not implemented yet
		 */
		public synchronized void newObjectRequested(String accessionCode) {
			activeThreads--;
		}

	}
}