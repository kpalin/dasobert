package de.mpg.mpiinf.ag3.dasmi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An interactor taking part in a certain interaction is called a participant. This distinction is 
 * necessary to give the interactor interaction specific properties, like experimental role
 * 
 * @author Hagen Blankenburg
 *
 */
public class Participant {
	
	private String id = null;
	private Interactor interactor = null;
	private List details = new ArrayList();
	
	/**
	 * Empty construcotr
	 *
	 */
	public Participant(){}
	
	/**
	 * Basic initialization
	 * @param ref
	 */
	public Participant(Interactor interactor) {
		this.interactor = interactor;
	}
	


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param ref the ref to set
	 */
	public void setInteractor(Interactor interactor) {
		this.interactor = interactor;
	}
	
	/**
	 * 
	 * @return
	 */
	public Interactor getInteractor(){
		return this.interactor;
	}

	/**
	 * @return the details
	 */
	public List getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List details) {
		this.details = details;
	}
	
	/**
	 * Adds a detail to the list
	 * @param detail the detail to add
	 */
	public void addDetail(Detail detail){
		this.details.add(detail);
	}
	
	
	/**
	 * Basic to string
	 * @return the string
	 */
	public String toString(){
		String help = null;
		if (interactor != null){
			help = "Interactor:\n\t\t" +  this.interactor.toString();
			if (details != null){
				help += "\tDetails:\n";
				for (int i = 0; i < details.size(); i++){
					help += "\t\t" + details.get(i).toString() +"\n";
				}
			}
		}
		return help;
	}
	
	
	

}
