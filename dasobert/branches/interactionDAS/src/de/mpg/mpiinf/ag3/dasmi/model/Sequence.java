package de.mpg.mpiinf.ag3.dasmi.model;

import de.mpg.mpiinf.ag3.dasmi.Constants;

/**
 * Class representing a sequence. The type of the sequence is determined by the interactor type.
 * 
 * @author Hagen Blankenburg
 *
 */
public class Sequence {
	
	private int start;
	private int end;
	private String sequence; 
	
	/**
	 * Basic initialization
	 *
	 */
	public Sequence(){
		this.sequence = Constants.INVALID_STRING;
		this.start = Constants.INVALID_INT;
		this.end = Constants.INVALID_INT;
	}
	

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}
	
	/**
	 * @param end the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}
	
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}
	
	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}
	
	/**
	 * 
	 * @return the sequence
	 */
	public String getSequence(){
		return this.sequence;
	}
	
	/**
	 * 
	 * @param sequence the sequence to set
	 */
	public void setSequence(String sequence){
		this.sequence = sequence;
	}
	
	
	/**
	 * Returns a specific part of the sequence 
	 * @param from the start of the sequence chunk
	 * @param till the end of the sequence chunk
	 * @return
	 */
	public String getSequence(int from, int till){
		if (from > 0 && from < end){
			return sequence.substring(from); 
		}else{
			return null;
		} 
	}

}
