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
 * Created on Jul 3, 2007
 * 
 */

package org.biojava.dasobert.test.servers;

import org.biojava.dasobert.das.InteractionParameters;
import org.biojava.dasobert.das.InteractionThread;
import org.biojava.dasobert.das.SequenceThread;
import org.biojava.dasobert.dasregistry.Das1Source;
import org.biojava.dasobert.eventmodel.SequenceEvent;
import org.biojava.dasobert.eventmodel.SequenceListener;

import de.mpg.mpiinf.ag3.dasmi.model.Interaction;

import junit.framework.TestCase;

public class TestServers extends TestCase implements SequenceListener{

	Das1Source uniprot ;
	Das1Source interactionMPI;
	
	String seq;
	public void setUp(){
		System.setProperty("proxySet", "true");
		System.setProperty("proxyHost", "wwwcache.sanger.ac.uk");
		System.setProperty("proxyPort", "3128");
		
		uniprot =  new Das1Source();
		uniprot.setUrl("http://www.ebi.ac.uk/das-srv/uniprot/das/aristotle/");
		
		interactionMPI = new Das1Source();
		interactionMPI.setUrl("http://dasmi.bioinf.mpi-inf.mpg.de/das/intact/");
		
		
		seq = null;
	}
	
	public void testUniProtServer(){
		SequenceThread thread = new SequenceThread("P50225",uniprot);
		thread.addSequenceListener(this);
		
		// this is now run in the main thread, not parallell. not sure how to do this with Junit...
		
		thread.getSequence();
		assertNotNull(seq);
		assertTrue(seq.length() > 100);
		
	}
	
	public void testInteractionServer(){
		InteractionParameters params = new InteractionParameters();
		params.setDasSource(interactionMPI);
		params.setQuery("1212");
		InteractionThread thread = new InteractionThread(params);
		
		// TODO: how can I do  multiple threads with JUnit??
		Interaction[] interA = thread.getInteractions(params.getQuery());
		assertNotNull(interA);
		assertTrue(interA.length > 0);
			
		
	}

	public void clearSelection() {
		// TODO Auto-generated method stub
		
	}

	public void newSequence(SequenceEvent e) {
		seq = e.getSequence();
		
	}

	public void selectedSeqPosition(int position) {
		// TODO Auto-generated method stub
		
	}

	public void selectedSeqRange(int start, int end) {
		// TODO Auto-generated method stub
		
	}

	public void selectionLocked(boolean flag) {
		// TODO Auto-generated method stub
		
	}

	public void newObjectRequested(String accessionCode) {
		// TODO Auto-generated method stub
		
	}

	public void noObjectFound(String accessionCode) {
		// TODO Auto-generated method stub
		
	}
}
