package org.biojava.dasobert.util;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;

public class GetXMLReader {
	public boolean VALIDATION = false; // DTD validation ..
	public  XMLReader getXMLReader() throws SAXException {
		SAXParserFactory spfactory = SAXParserFactory.newInstance();

		spfactory.setValidating(false);
		SAXParser saxParser = null;

		try {
			saxParser = spfactory.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		XMLReader xmlreader = saxParser.getXMLReader();
		boolean validation = VALIDATION;
		// XMLReader xmlreader = XMLReaderFactory.createXMLReader();
		try {
			xmlreader.setFeature("http://xml.org/sax/features/validation",
					validation);
		} catch (SAXException e) {
			e.printStackTrace();
		}

		try {
			xmlreader
					.setFeature(
							"http://apache.org/xml/features/nonvalidating/load-external-dtd",
							validation);
		} catch (SAXNotRecognizedException e) {
			e.printStackTrace();
		}
		return xmlreader;

	}
}
