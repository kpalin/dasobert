package org.biojava.dasobert.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.biojava.bio.program.das.dasalignment.DASException;
import org.biojava.dasobert.das.DAS_Coordinates_Handler;
import org.biojava.dasobert.dasregistry.DasCoordinateSystem;
import org.biojava.dasobert.util.GetXMLReader;
import org.biojava.dasobert.util.HttpConnectionTools;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import tests.org.biojava.dasobert.dasregistry.ServerLocation;

import junit.framework.TestCase;

public class TestCoordinates_Handler extends TestCase{

	
	public void testCoordinatesHandler(){
		ServerLocation.setProxy();
		URL u=null;
		try {
			u = new URL(ServerLocation.REGISTRY+"/das/coordinatesystem");
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

			List <DasCoordinateSystem>coordinates = cont_handle.get_features();
			assertTrue(coordinates.size()>0);
			DasCoordinateSystem firstCoord=coordinates.get(0);
			System.out.println(firstCoord.toString());
			assertTrue(firstCoord.getAuthority()!=null && firstCoord.getCategory()!=null);
		
	}
	
	
	
	
	
}
