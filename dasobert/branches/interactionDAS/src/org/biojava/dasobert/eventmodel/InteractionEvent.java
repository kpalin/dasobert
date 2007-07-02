package org.biojava.dasobert.eventmodel;

import de.mpg.mpiinf.ag3.dasmi.model.Interaction;
import org.biojava.dasobert.das.InteractionParameters;


/**
 * Interaction event, containing the resutls of an interaction thread
 * @author Hagen Blankenburg
 *
 */
public class InteractionEvent extends AbstractDasEvent {
    Interaction[] interactions = null;
    InteractionParameters params = null;
    
    /**
     * Creates a new InteractionEvent object with the parameters used and the interactions found
     * @param params
     * @param interactions
     */
    public InteractionEvent(InteractionParameters params, Interaction[] interactions){
        super();
        this.interactions = interactions;
        this.params = params;
    }
    
    /**
     * 
     * @return The InteractionParameters used
     */
    public InteractionParameters getParams(){
        return this.params;
    }
    
    /**
     * 
     * @return The interactions returned from a server
     */
    public Interaction[] getInteractions(){
        return interactions;
    }

}
