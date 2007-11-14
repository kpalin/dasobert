package org.biojava.dasobert.eventmodel;
import org.biojava.dasobert.das.InteractionParameters;

/**
 * Interface describing the potential outcomes of an interaction thread
 * @author Hagen Blankenburg, Max Planck Institute for Informatics
 *
 */
public interface InteractionListener 
extends ObjectListener{
    
	/**
	 * Called if new interactions where found
	 * @param event Interactions and source parameters
	 */
	public void newInteractions(InteractionEvent event);
    
	/**
	 * Called if no interactions were found
	 * @param params Source paramters
	 */
    public void noObjectFound(InteractionParameters params);
   
    /**
     * Called if the results have to be prepared first and the client shoudl return later
     */
    public void comeBackLater();
    
    /**
     * Dunno
     */
    void newObjectRequested(String accessionCode);
}
