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
 * Created on Oct, 19 2009
 * Created on 19.10.2009
 * @author Jonathan Warren
 * 
 * 
 */


package org.biojava.das.ucsc;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.biojava.dasobert.das.Capabilities;
import org.biojava.dasobert.das.DAS_DSN_Handler;
import org.biojava.dasobert.das.DAS_Types_Handler;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;

/**
 * Can we walk the UCSC DAS sources and convert them to Das1Sources which can then be used by the registry to write a sources document and load into the database
 * @author jw12
 *
 */
public class UCSCtoDAS1Sources {
 private static final String dsnUrl="http://genome.ucsc.edu/cgi-bin/das/dsn";
	
public static void main(String args[]){
	System.setProperty("proxySet", "true");
	System.setProperty("proxyHost", "wwwcache.sanger.ac.uk");
	System.setProperty("proxyPort", "3128");
	

	System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
	System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
	//read the dsn document
	UCSCtoDAS1Sources ucsc=new UCSCtoDAS1Sources();
	List dsns=ucsc.getDSNs();
	List<Das1Source> das1Sources=ucsc.getDas1Sources(dsns);
	//follow each datasourcename and see what the types are and create a datasource object for each with the url having a :type on the end and use the types catagory and method to create a description
	
	//List <String>types=ucsc.getTypes("http://genome.cse.ucsc.edu/cgi-bin/das/hg19");
	//can use the entry_points command to create a testcode for features command (pick first one)
}


	
private List<Das1Source> getDas1Sources(List<HashMap<String,String>> dsns){
	//alltypes size=4198 so there should be that many dassources from ucsc
	ArrayList<Das1Source> sources=new ArrayList<Das1Source>();
	ArrayList allTypes=new ArrayList();
	
	for( HashMap dsn:dsns){
		String url=(String)dsn.get("MAPMASTER");
		List <String>types=this.getTypes(url);
		for(String type:types){
			System.out.println(type);
			//create a source for each type here
			
			allTypes.add(type);
		}
		break;//for testing just stop after first dsn
		//Das1Source source=new Das1Source();
		//source.setNickname(name)
		
		//dsn
		
		
	}
	System.out.println("alltypes size="+allTypes.size());
	return sources;
}

private Das1Source createDas1SourceFromUCSCType(HashMap dsn, String type){
	Das1Source source=new Das1Source();
	//source.setNickname(dsn.get("ID")id)
	return source; 
	
	
}
	
	private List getDSNs(){
		
		String[] spl = dsnUrl.split("/");

		String dsnurl = "";

		for (int i = 0; i < spl.length - 1; i++) {
			dsnurl += spl[i] + "/";
		}

		URL u=null;
		try {
			u = new URL(dsnurl + "dsn");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 System.out.println(u.toString());

		// parse dsn ...
		InputStream dasInStream=null;
		try {
			dasInStream = open(u);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XMLReader xmlreader=null;
		try {
			xmlreader = getXMLReader();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DAS_DSN_Handler cont_handle = new DAS_DSN_Handler();

		xmlreader.setContentHandler(cont_handle);
		xmlreader.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
		InputSource insource = new InputSource();
		insource.setByteStream(dasInStream);
		try {
			xmlreader.parse(insource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List sources = cont_handle.getDsnSources();

		 System.out.println("got " + sources.size() +
		 " sources listed in DSN");
		return sources;
		
	}
	
	
	protected XMLReader getXMLReader() throws SAXException {
		SAXParserFactory spfactory = SAXParserFactory.newInstance();

		spfactory.setValidating(false);
		SAXParser saxParser = null;

		try {
			saxParser = spfactory.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		XMLReader xmlreader = saxParser.getXMLReader();
		
		// XMLReader xmlreader = XMLReaderFactory.createXMLReader();
		try {
			xmlreader.setFeature("http://xml.org/sax/features/validation",
					false);
		} catch (SAXException e) {
			e.printStackTrace();
		}

		try {
			xmlreader
					.setFeature(
							"http://apache.org/xml/features/nonvalidating/load-external-dtd",
							false);
		} catch (SAXNotRecognizedException e) {
			e.printStackTrace();
		}
		return xmlreader;

	}
	
	protected InputStream open(URL url) throws Exception {

		// TODO Auto-generated method stub

		InputStream inStream = null;

		HttpURLConnection huc = null;
		huc = (HttpURLConnection) url.openConnection();
		// String contentEncoding = huc.getContentEncoding();
		inStream = huc.getInputStream();
		return inStream;
	}

	private List<String> getTypes(String url){
		
		String urlString=url+"/types";
		String removedPort=urlString.replace(":80", "");
		URL u=null;
		try {
			System.out.println(removedPort);
			u = new URL(urlString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		InputStream dasInStream=null;
		try {
			dasInStream = open(u);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XMLReader xmlreader=null;
		try {
			xmlreader = getXMLReader();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DAS_Types_Handler cont_handle = new DAS_Types_Handler();

		xmlreader.setContentHandler(cont_handle);
		xmlreader.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
		InputSource insource = new InputSource();
		insource.setByteStream(dasInStream);
		
		try {
			xmlreader.parse(insource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cont_handle.getTypesAsList();
		
	}
}
