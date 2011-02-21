/*
 *                    BioJava development code
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
 * Created on 19.03.2004
 * @author Andreas Prlic
 *
 */
package org.biojava.dasobert.das ;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

import java.util.ArrayList ;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** a class to parse the reponse of a DAS - types request 
 */
public class DAS_Types_Handler extends DefaultHandler {
	 List <Map<String,String>>types;
	    boolean dastypesPresent;
	    boolean gffPresent;
	    boolean segmentPresent;
		private int maxNrFeaturesOntolgy;
		private String chars;//hold data from between a set of tags
		HashMap<String,String>map;
		public boolean segmentTypePresent;
	    
	    public DAS_Types_Handler() {
		super();
		types = new ArrayList<Map<String,String>>();
		dastypesPresent = false;
		gffPresent=false;
		segmentPresent=false;
		segmentTypePresent=false;
	    }

	    public void startElement (String uri, String name, String qName, Attributes atts){
	    	 chars = "";
	    	
	    	if ( qName.equals("DASTYPES")) {
		    dastypesPresent = true;
		    
		} else if ( qName.equals("GFF")) {
		    gffPresent = true;
		    
		} else if ( qName.equals("SEGMENT")) {
		    segmentPresent = true;	
		 
		    String id = atts.getValue("id");
		    String type=atts.getValue("type");//optional in 1.5 deprecated in 1.6 so still optional
		    if(type!=null)segmentTypePresent=true;
		    // id is optional here
		    //if ( id != null ) {
			//types.add("id",id);
		    //}
		} else if ( qName.equals("TYPE")){
			map=new HashMap<String,String>();
		    String type = atts.getValue("id");
		    // id is mandatory ...	    
		    
		    String category=atts.getValue("category");
		    String method=atts.getValue("method");
		    String cvId=atts.getValue("cvId");
		    if(type!=null){
		    	map.put("id",type);
		    }
		    if(category!=null){
		    	map.put("category",category);
		    }
		    if(method!=null){
		    	map.put("method",method);
		    }
		    if(cvId!=null){
		    	map.put("cvId",cvId);
		    }
		    
		}
		
	    }
	    
	    public void endElement(String uri, String name, String qName) {
	    	if ( qName.equals("TYPE")){
	    		map.put("number",chars);
	    		types.add(map);
	    	}
	    }

	    public String[] getTypes(){
		return (String[])types.toArray(new String[types.size()]);
	    }
	    
	    public List<Map<String,String>> getTypesAsList(){
	    	return types;
	        }

		public void setMaxFeatures(int maxNrFeaturesOntology) {
			// TODO Auto-generated method stub
			this.maxNrFeaturesOntolgy=maxNrFeaturesOntology;
		}
		
		
		 public void characters (char ch[], int start, int length){
		        
		     
	            for (int i = start; i < start + length; i++) {
	               chars += ch[i];
	            }
	        
	        
	    }
	}