package org.biojava.dasobert.dasregistry;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.biojava.bio.program.das.dasalignment.DASException;
import org.biojava.dasobert.das.DAS_Coordinates_Handler;
import org.biojava.dasobert.util.GetXMLReader;
import org.biojava.dasobert.util.HttpConnectionTools;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import tests.org.biojava.dasobert.dasregistry.ServerLocation;

public class GetCoordinateSystemsFromRegistry {
 private String registryDefaultLocation="http://www.dasregistry.org/das/coordinatesystem";
 
 public List<DasCoordinateSystem> getCoordinates(){
	 return this.getCoordinates(registryDefaultLocation);
 }
	public List<DasCoordinateSystem> getCoordinates(String regCoordinatesURL){
		ServerLocation.setProxy();
		URL u=null;
		try {
			u = new URL(regCoordinatesURL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			InputStream dasInStream = null;
			try {
				dasInStream = HttpConnectionTools.getInputStream(u, false);
			} catch (DASException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			GetXMLReader xmlReader=new GetXMLReader();
			XMLReader xmlreader = null;
			try {
				xmlreader = xmlReader.getXMLReader();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			DAS_Coordinates_Handler cont_handle = new DAS_Coordinates_Handler();
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

			return cont_handle.get_features();
		
	}
	
}
