/*
 * Copyright or © or Copr. Fabien Michel, Olivier Gutknecht, Jacques Ferber (1997)

fmichel@lirmm.fr
olg@no-distance.net
ferber@lirmm.fr

This software is a computer program whose purpose is to 
provide a lightweight Java library for designing and simulating Multi-Agent Systems (MAS).

This software is governed by the CeCILL-C license under French law and
abiding by the rules of distribution of free software.  You can  use, 
modify and/ or redistribute the software under the terms of the CeCILL-C
license as circulated by CEA, CNRS and INRIA at the following URL
"http://www.cecill.info". 

As a counterpart to the access to the source code and  rights to copy,
modify and redistribute granted by the license, users are provided only
with a limited warranty  and the software's author,  the holder of the
economic rights,  and the successive licensors  have only  limited
liability. 

In this respect, the user's attention is drawn to the risks associated
with loading,  using,  modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean  that it is complicated to manipulate,  and  that  also
therefore means  that it is reserved for developers  and  experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards their
requirements in conditions enabling the security of their systems and/or 
data to be ensured and,  more generally, to use and operate it in the 
same conditions as regards security. 

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-C license and that you accept its terms.
 */
package madkit.concurrent;

import java.util.logging.Level;

import madkit.kernel.JunitMadkit;
import madkit.kernel.Madkit.LevelOption;
import madkit.testing.util.agent.NormalAgent;

import org.junit.Test;

/**
 * @author Fabien Michel
 * @since MaDKit 5.0.0.9
 * @version 0.9
 * 
 */

public class ConcurrentOrgTest extends JunitMadkit {

    private int testSize = 8;

    @Test
    public void noConflictTest() {
	addMadkitArgs(LevelOption.agentLogLevel.toString(), Level.OFF.toString());
	addMadkitArgs(LevelOption.kernelLogLevel.toString(), Level.OFF.toString());
	launchTest(new NormalAgent() {

	    @Override
	    protected void activate() {
		for (int i = 0; i < testSize; i++) {
		    launchAgent(new ConcurrentTestAgent(), 0);
		}
		System.err.println("everything launched");
		pause(5000);
	    }
	});
    }

    @Test
    public void randomTest() {
	addMadkitArgs(LevelOption.agentLogLevel.toString(), Level.OFF.toString());
	// addMadkitArgs(LevelOption.kernelLogLevel.toString(),Level.ALL.toString());
	launchTest(new NormalAgent() {

	    @Override
	    protected void activate() {
		for (int i = 0; i < testSize; i++) {
		    launchAgent(new ConcurrentTestAgentBis(), 0);
		}
		System.err.println("everything launched");
		pause(5000);
	    }
	});
    }

}