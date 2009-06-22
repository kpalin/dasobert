package org.biojava.dasobert.das.validation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import javax.xml.rpc.ServiceException;


import org.biojava.dasobert.dasregistry.SimpleTerm;



import uk.ac.ebi.www.ontology_lookup.OntologyQuery.Query;
import uk.ac.ebi.www.ontology_lookup.OntologyQuery.QueryService;
import uk.ac.ebi.www.ontology_lookup.OntologyQuery.QueryServiceLocator;


/**
* stores ontologies from ebi webservices in a flat file and retrieves
the data from it when asked 
* @author jw12
*
*/
public class DasRegistryOntologyLookUp {

	private Query query = null;
	private String fieldSep="\t";
	String ontologies[]={"BS","ECO", "GO"};
	
	Map ontologyBS=null;//maps for storing ontology information if decide to make a cache of the webservice
	Map ontologyECO=null;
	Map ontologySO=null;
	
	private static String newline = System.getProperty("line.separator");

	public static void main(String args[]) {
		DasRegistryOntologyLookUp lookup = new DasRegistryOntologyLookUp();
		//"SO:0001077"
		boolean isValid=lookup.exists("blah", "BS");
		if(isValid){
			System.out.println("true");
		}else{
			System.out.println("false");
		}
		

	}

	public DasRegistryOntologyLookUp() {
		System.out.println("instantiating");
		init();
		//this.exists("poo", "BS");
		//
		


	}

	private void init() {
		//needed to get out through sanger proxy
//		Properties props= new Properties(System.getProperties());
//		props.put("http.proxySet", "true");
//		props.put("http.proxyHost", "wwwcache.sanger.ac.uk");
//		props.put("http.proxyPort", "3128");
//		Properties newprops = new Properties(props);
//		System.setProperties(newprops);
//		System.out.println("set properties");
		QueryService locator = new QueryServiceLocator();
		this.query = null;
		try {
			query = locator.getOntologyQuery();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//get dir as a file from the servlet that will run at specific time intervals
		//File dir=new File("directory");
//		writeOntology(locator, "ECO");
//		writeOntology(locator, "SO");
//		writeOntology(locator, "BS");
		
	}

	private void writeOntology(QueryService locator, String ontology){//,File dir) {
		
		//can use servletcontext.getRealPath("relative to webapproot");
		String path=ontology+".txt";
		File file=new File(path);
		BufferedWriter out=null;
		System.out.println("ontology file is here:"+file.getAbsolutePath());
		try {
			FileWriter fStream=new FileWriter(path);
			out=new BufferedWriter(fStream);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		

		HashMap map = null;
		try {
			map = query.getAllTermsFromOntology(ontology);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collection collection = map.keySet();
		Iterator it = collection.iterator();
		System.out.println("initializing web service for ontologies");
		while (it.hasNext()) {
			boolean isObsolete=false;
			String ontKey = (String) it.next();
			
			try {
				isObsolete=query.isObsolete(ontKey,ontology);
				try {
					out.write(ontKey+fieldSep+map.get(ontKey)+fieldSep+isObsolete
+newline);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	
	public boolean exists(String termId){
		boolean inOntology=false;
		for(int i=0; i<ontologies.length; i++){
		if(this.exists(termId, ontologies[i]))return true;	
		}
		return inOntology;
	}
	
	public boolean isObselete(String termId){
		for(int i=0; i<ontologies.length; i++){
		try {
			if(query.isObsolete(termId,ontologies[i]))return true;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return false;
	}
	
	
	/**
	 * could change this to call web service and if thats down get info
from file
	 * @param id GO or SO id
	 * @param ontology e.g. GO or BS
	 * @return boolean (valid term or not)
	 */
	public boolean exists(String id, String ontology){
		boolean exists= false;
		if(this.query==null){
			this.init();
		}
		try {
			System.out.println("trying id:"+id+" ontology:"+ontology);
			String term=query.getTermById(id, ontology);
			if(term.equals(id)){
				exists= false;
			}else{
			System.out.println("term found="+term);
			exists= true;
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exists; 
	}
	
	public boolean existsInFile(String id, String ontology){
		boolean exists=false;
		if(ontology.equalsIgnoreCase("bs")){
			
		}
		
		
		return exists;
	}
	
	public SimpleTerm getTerm(String id){
		SimpleTerm term=null;
		for(int i=0; i<ontologies.length; i++){
			term=this.getTerm(id,ontologies[i]);
			if(term!=null)return term;
		}
		return term;
	}
	
	public SimpleTerm getTerm(String id, String ontology){
		
		SimpleTerm termObject=null;
		if(this.query==null){
			this.init();
		}
		try {
			System.out.println("trying id:"+id+" ontology:"+ontology);
			String name=query.getTermById(id, ontology);
			if(name.equals(id)){
				return null;
			}else{
				termObject=new SimpleTerm(id);
			termObject.setName(name);
			
			termObject.setObsolete(query.isObsolete(id,ontology));
			termObject.setXrefs(query.getTermXrefs(id, ontology));
			//termObject.setSynonyms(query.get)
			
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return termObject;
	}
	
	
	
//	public Term getTerm(String id){
//		if(this.query==null){
//			this.init();
//		}
//		String ontology="BS";
//		try {
//			System.out.println("trying id:"+id+" ontology:"+ontology);
//			String termString=query.getTermById(id, ontology);
//			if(termString.equals(id)){
//				return null;
//			}else{
//			System.out.println("term found="+termString);
//			
//			}
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return new Term();
//		
//	}
//	
}
