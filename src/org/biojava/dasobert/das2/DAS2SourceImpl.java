/*
 *                  BioJava development code
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
 * Created on Feb 9, 2006
 *
 */
package org.biojava.dasobert.das2;

import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.DasSource;

public class DAS2SourceImpl 
extends Das1Source
implements Das2Source 

{

    Das2Capability[] capabilities;
    
    public DAS2SourceImpl() {
        super();
        capabilities = new Das2Capability[0];
    }
    
    
    /**  compare if two DasSources are identical
     * 
     */
    public boolean equals(DasSource other){
        if (! (other instanceof DAS2SourceImpl))
            return false;
       
        // to compare if two Das2Sources are identical we do the following:
        //  we check the capabilities
        
        DAS2SourceImpl d2o = (DAS2SourceImpl)other;
        
        if ( nickname.equals(d2o.getNickname()))
            return true;
        
        Das2Capability[] othercaps = d2o.getDas2Capabilities();
        
        if ( ! (capabilities.length == othercaps.length))
            return false;
        
        for ( int x=0;x<capabilities.length;x++){
            Das2Capability tmpcap = capabilities[x];
            boolean foundCap = false;
            for (int y=0; y< othercaps.length;y++){
                Das2Capability tmpcapo = othercaps[y];
                if ( tmpcap.equals(tmpcapo))
                    foundCap = true;
            }
            if ( ! foundCap)
                return false;
        }
        
        
        //TODO?
        // should we add a check for coordinate systems?
        // but we already check for the endpoints, that should be enough...
        
        return true;
        
    }
    
    public String[] getCapabilities() {
        //todo mark as not needed / not appropriate ...
        return super.getCapabilities();
    }



    public void setCapabilities(String[] u) {
        // TODO Auto-generated method stub
        super.setCapabilities(u);
    }



    public Das2Capability[] getDas2Capabilities() {
        // TODO Auto-generated method stub
        return capabilities;
    }

    public void setDas2Capabilities(Das2Capability[] capabilities) {
        // TODO Auto-generated method stub
        this.capabilities = capabilities;
        
    }
    
    
    

}
