package org.biojava.dasobert.eventmodel;

/**
 * Interface describing the potentail outcomes of an interaction thread
 * @author Hagen Blankenburg
 *
 */
public interface InteractionListener 
extends ObjectListener{
    
	/**
	 * Called if new interactions where found
	 * @param event
	 */
	public void newInteractions(InteractionEvent event);
    
    public void noObjectFound(String id);
   
    public void comeBackLater();
    
    void newObjectRequested(String accessionCode);
}
