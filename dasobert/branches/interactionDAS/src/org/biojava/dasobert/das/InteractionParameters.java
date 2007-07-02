package org.biojava.dasobert.das;

import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.DasCoordinateSystem;

/** 
 * 
 * @author Hagen Blankenburg
 *
 */
public class InteractionParameters {
    private String query = null;
    private DasCoordinateSystem queryCoordinateSystem = null;
    private Das1Source dasSource = null;
    private boolean qmSource = false;
        
    /**
     * Empty constructor
     *
     */
    public InteractionParameters() {
        super();
    }


    /**
     * 
     * @return The source associated with an interaction thread 
     */
    public Das1Source getDasSource() {
        return dasSource;
    }

    
    /**
     * 
     * @param dasSource The source to set
     */
    public void setDasSource(Das1Source dasSource) {
        this.dasSource = dasSource;
    }

    /**
     * 
     * @return The query
     */
    public String getQuery() {
        return query;
    }

    /**
     * 
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }


    /**
     * 
     * @return The coordinate system
     */
    public DasCoordinateSystem getQueryCoordinateSystem() {
        return queryCoordinateSystem;
    }


    /**
     * 
     * @param queryCoordinateSystem The coordinate system to set
     */
    public void setQueryCoordinateSystem(DasCoordinateSystem queryCoordinateSystem) {
        this.queryCoordinateSystem = queryCoordinateSystem;
    }
    
    
    /**
     * Spcifies if the source specified in the parameters is a qm source only providigng
     * additional confidence score and not interactions 
     * TODO overthink this ...
     * @param isQm the qm flag to set
     */
    public void setQmSource(boolean isQm){
    	this.qmSource = isQm;
    }
    
    /**
     * 
     * @return the qm flag
     */
    public boolean getQmSource(){
    	return this.qmSource;
    }
}
