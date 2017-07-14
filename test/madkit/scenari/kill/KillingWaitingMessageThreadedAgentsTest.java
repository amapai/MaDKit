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
package madkit.scenari.kill;

import static madkit.kernel.AbstractAgent.ReturnCode.SUCCESS;
import static madkit.kernel.AbstractAgent.ReturnCode.TIMEOUT;
import static org.junit.Assert.assertEquals;

import java.util.logging.Level;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Agent;
import madkit.kernel.JunitMadkit;
import madkit.testing.util.agent.DoItDuringLifeCycleAgent;

import org.junit.Test;

/**
 * @author Fabien Michel
 * @since MaDKit 5.0.0.10
 * @version 0.9
 * 
 */

public class KillingWaitingMessageThreadedAgentsTest extends JunitMadkit {

	@Test
	public void brutalKills() {// TODO brutal kill with to < 0
		launchTest(new AbstractAgent() {
			public void activate() {
				getLogger().setLevel(Level.ALL);
				Agent a;

				a = new WaitingMessageAgent(true, false, true);
				assertEquals(TIMEOUT, launchAgent(a, 1));
				assertEquals(TIMEOUT, killAgent(a, 1));
				assertAgentIsTerminated(a);

				a = new WaitingMessageAgent(true, true, true);
				assertEquals(TIMEOUT, launchAgent(a, 1));
				assertEquals(TIMEOUT, killAgent(a, 1));
				assertAgentIsTerminated(a);
			}
		}, true);
	}

	@Test
	public void brutalKillOnWaitInActivate() {// TODO brutal kill with to < 0
		launchTest(new AbstractAgent() {
			public void activate() {
				getLogger().setLevel(Level.ALL);
				WaitingMessageAgent a = new WaitingMessageAgent(true, false, false);
				assertEquals(TIMEOUT, launchAgent(a, 1));
				assertEquals(SUCCESS, killAgent(a, 1));
				assertAgentIsTerminated(a);
			}
		}, true);
	}

	@Test
	public void brutalKillOnWaitInLive() {// TODO brutal kill with to < 0
		launchTest(new AbstractAgent() {
			public void activate() {
				getLogger().setLevel(Level.ALL);
				WaitingMessageAgent a = new WaitingMessageAgent(false, true, false);
				assertEquals(SUCCESS, launchAgent(a));
				pause(100);
				assertEquals(SUCCESS, killAgent(a, 1));
				assertAgentIsTerminated(a);
			}
		}, true);
	}

	@Test
	public void brutalKillOnWaitInEnd() {// TODO brutal kill with to < 0
		launchTest(new AbstractAgent() {
			public void activate() {
				getLogger().setLevel(Level.ALL);
				WaitingMessageAgent a = new WaitingMessageAgent(false, false, true);
				assertEquals(SUCCESS, launchAgent(a));
				pause(100);
				assertEquals(TIMEOUT, killAgent(a, 1));
				assertAgentIsTerminated(a);
			}
		}, true);
	}

	@Test
	public void normalKills() {// TODO more cases
		launchTest(new AbstractAgent() {
			public void activate() {
				
				super.activate();
				Agent a;
				a = new WaitingMessageAgent(true, false, false);
				assertEquals(TIMEOUT, launchAgent(a, 1));
				assertEquals(SUCCESS, killAgent(a));
				assertAgentIsTerminated(a);

				a = new WaitingMessageAgent(false, true, false);
				assertEquals(SUCCESS, launchAgent(a));
				pause(100);
				assertEquals(SUCCESS, killAgent(a));
				assertAgentIsTerminated(a);

				a = new WaitingMessageAgent(false, false, true);
				assertEquals(SUCCESS, launchAgent(a));
				pause(100);
				assertEquals(TIMEOUT, killAgent(a, 1));
				assertAgentIsTerminated(a);

				a = new WaitingMessageAgent(true, false, true);
				assertEquals(TIMEOUT, launchAgent(a, 1));
				assertEquals(TIMEOUT, killAgent(a, 1));
				assertAgentIsTerminated(a);

				a = new WaitingMessageAgent(true, true, true);
				assertEquals(TIMEOUT, launchAgent(a, 1));
				assertEquals(TIMEOUT, killAgent(a, 1));
				assertAgentIsTerminated(a);
			}
		}, true);
	}
}

class WaitingMessageAgent extends DoItDuringLifeCycleAgent {

	/**
	 * @param inActivate
	 * @param inLive
	 * @param inEnd
	 */
	public WaitingMessageAgent(boolean inActivate, boolean inLive, boolean inEnd) {
		super(inActivate, inLive, inEnd);
	}

	@Override
	public void doIt() {
		waitNextMessage();
		waitNextMessage();
	}

}
