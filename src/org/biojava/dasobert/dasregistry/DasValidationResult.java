package org.biojava.dasobert.dasregistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.biojava.dasobert.das.Capabilities;



/**
 * class to hold the results of validation in the registry and so we can display the info we got as apposed to core DasSource data that is 
 * stored in the DasSource objects. Types data maybe contained here as we are going to call this through ajax and not the DasSource objects?
 * 
 * @author jw12
 *
 */
public class DasValidationResult  {
	/**
	 * for each capability we want to save the result of invalid, possiblyValid, Valid and any messages associated with the error or positive outcome
	 */
	private Map <Capabilities, Boolean>isValid=new HashMap<Capabilities, Boolean>();
	private Map <Capabilities,String>errors=new HashMap<Capabilities, String>();
	private Map <Capabilities,Long>times=new HashMap<Capabilities, Long>();
	public Map<Capabilities, Long> getTimes() {
		return times;
	}
	private List<Map<String,String>>types = new ArrayList<Map<String,String>>();//list of types as maps of id->type, cvId-> cvId, category->category for each source if capable and associated cvId and category if available
	
		private boolean overallValid=false;
	private String url;//the root url of the source e.g. http://das.sanger.ac.uk/das/
	private DasCoordinateSystem []coords;

	private String specification="";//default is not set

	public String getSpecification() {
		return specification;
	}
	public DasValidationResult(String url, DasCoordinateSystem[] coords) {
		this.url=url;
		this.coords=coords;
	}
	public Map<Capabilities, Boolean> getIsValid() {
		return isValid;
	}

	public void setIsValid(Map<Capabilities, Boolean> isValid) {
		this.isValid = isValid;
	}

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	public List<Map<String, String>> getTypes() {
		return types;
	}

	public void setTypes(List<Map<String, String>> types) {
		this.types = types;
	}


	public boolean isOverallValid() {
		return overallValid;
	}

	public void setOverallValid(boolean overallValid) {
		this.overallValid = overallValid;
	}

	public void isValid(Capabilities capability, boolean b) {
		isValid.put(capability, b);
		
	}
	public boolean isValid(Capabilities capabilities){
		return isValid.get(capabilities);
	}

	public void error(Capabilities capability, String validationMessage) {
		errors.put(capability, validationMessage);
		
	}
	public String getError(Capabilities cap){
		return errors.get(cap);
	}
	
	public void time(Capabilities capability, Long duration) {
		times.put(capability, duration);
		
	}
	
	public Long getTime(Capabilities cap){
		return times.get(cap);
	}
	
	public String[] getValidCaps(){
		Set <Map.Entry<Capabilities, Boolean>>keys=isValid.entrySet();
		Iterator<Map.Entry<Capabilities, Boolean>>valuesIterator=keys.iterator();
		ArrayList <String>list=new ArrayList<String>();
		while(valuesIterator.hasNext()){
			Map.Entry<Capabilities,Boolean>entry=valuesIterator.next();
			if(entry.getValue()){
				list.add(entry.getKey().toString());
			}
		}
		return (String[]) list.toArray(new String[list.size()]);
	}
	
	
	
	
	public void print(){
		System.out.println("printing");
		this.printHashes();
		
	}
	
	private void printHashes(){
		Set <Map.Entry<Capabilities, Boolean>>keys=isValid.entrySet();
		Iterator<Map.Entry<Capabilities, Boolean>>valuesIterator=keys.iterator();
		
		while(valuesIterator.hasNext()){
			Map.Entry<Capabilities,Boolean>entry=valuesIterator.next();
			
				System.out.println(entry.getKey().toString()+"|"+entry.getValue());
			
		}
		
		Set <Map.Entry<Capabilities,String>>errorKeys=errors.entrySet();
		Iterator<Map.Entry<Capabilities, String>>errorValuesIterator=errorKeys.iterator();
		
		while(errorValuesIterator.hasNext()){
			Map.Entry<Capabilities,String>errorEntry=errorValuesIterator.next();
			
				System.out.println(errorEntry.getKey().toString()+"|"+errorEntry.getValue());
			
		}
		
	}

	public DasCoordinateSystem getCoord() {
		
		return null;
	}

	public DasCoordinateSystem[] getCoords() {
	
		return coords;
	}
	public void setSpecification(String specification) {
		this.specification=specification;
		
	}
	public String getValidationResultsString() {
		String resultString="";
		Set <Map.Entry<Capabilities, Boolean>>keys=isValid.entrySet();
		Iterator<Map.Entry<Capabilities, Boolean>>valuesIterator=keys.iterator();
		
		while(valuesIterator.hasNext()){
			Map.Entry<Capabilities,Boolean>entry=valuesIterator.next();
			
				resultString+=entry.getKey().toString()+"|"+entry.getValue()+"\n";
			
		}
		
		Set <Map.Entry<Capabilities,String>>errorKeys=errors.entrySet();
		Iterator<Map.Entry<Capabilities, String>>errorValuesIterator=errorKeys.iterator();
		
		while(errorValuesIterator.hasNext()){
			Map.Entry<Capabilities,String>errorEntry=errorValuesIterator.next();
			
				resultString+=errorEntry.getKey().toString()+"|"+errorEntry.getValue()+"\n";
			
		}
		return resultString;
	}
	

}
