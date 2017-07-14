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
package madkit.classreloading;

import static org.junit.Assert.fail;

import java.util.logging.Level;

import madkit.action.AgentAction;
import madkit.kernel.AbstractAgent;
import madkit.kernel.JunitMadkit;
import madkit.testing.util.agent.NormalAA;

import org.junit.Test;

/**
 * bin directory should be cleaned before use
 * the .class file is part of the test
 * 
 * @author Fabien Michel
 * @since MadKit 5.0.0.20
 * @version 0.9
 * 
 */
public class ReloadByGUITest extends JunitMadkit {

	@Test
	public void noExceptionTest() {
		launchTest(new AbstractAgent() {
			protected void activate() {
				NormalAA a = new NormalAA(){
					@Override
					protected void activate() {
						super.activate();
					}
				};
				launchAgent(a);
				try {
					getLogger().setLevel(Level.ALL);
					AgentAction.RELOAD.getActionFor(a).actionPerformed(null);
				} catch (Throwable e) {
					fail(e.getMessage());
				}
		}
		});
	}


	@Test
	public void noExceptionOnAATest() {
		launchTest(new AbstractAgent() {
			protected void activate() {
				try {
					AbstractAgent a = new AbstractAgent();
					launchAgent(a);
					AgentAction.RELOAD.getActionFor(a).actionPerformed(null);
				} catch (Throwable e) {
					fail(e.getMessage());
				}
			}
		});
	}

}
