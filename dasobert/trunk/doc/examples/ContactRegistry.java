/*                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 * Created on 24.11.2005
 * @author Andreas Prlic
 *
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;


import org.biojava.bio.program.das.dasalignment.DASException;
import org.biojava.dasobert.das2.Das2Source;
import org.biojava.dasobert.das2.DasSourceConverter;
import org.biojava.dasobert.das2.io.DasSourceReaderImpl;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.dasregistry.DasSource;


public class ContactRegistry {
    
    public ContactRegistry () {
        
        
    }
    
    
    public static void main(String[] args) {
        try {
            
//          make sure we use the Xerces XML parser..
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                    "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
            System.setProperty("javax.xml.parsers.SAXParserFactory",
                    "org.apache.xerces.jaxp.SAXParserFactoryImpl");
            
            ContactRegistry contact = new ContactRegistry();
            Das1Source[] sources = contact.getDas1Sources();
            System.out.print("got " + sources.length + "das1 sources");
        } catch ( Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
    public Das1Source[] getDas1Sources() throws MalformedURLException, DASException{
        
        DasSourceReaderImpl reader = new DasSourceReaderImpl();
        
        // THIS IS THE EXPERIMENTAL SERVER - NOT INTENDET FOR PRODUCTION USE!
        // THIS FUNCTIONALITY WILL SOON BE AVAILABLE FROM
        // http://das.sanger.ac.uk/registry/
        String u = "http://www.spice-3d.org/dasregistry/das1/sources";
        
        URL url = new URL(u);
        
        DasSource[] sources = reader.readDasSource(url);
        
        List das1sources = new ArrayList();
        for (int i=0;i< sources.length;i++){
            DasSource ds = sources[i];
            if ( ds instanceof Das2Source){
                Das2Source d2s = (Das2Source)ds;
                if (d2s.hasDas1Capabilities()){
                    Das1Source d1s = DasSourceConverter.toDas1Source(d2s);
                    das1sources.add(d1s);
                }
                    
            } else if ( ds instanceof Das1Source){
                das1sources.add((Das1Source)ds);
            }
        }
        
        return (Das1Source[])das1sources.toArray(new Das1Source[das1sources.size()]);
        
        
        
    }
    
}