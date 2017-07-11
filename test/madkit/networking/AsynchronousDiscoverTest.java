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
package madkit.networking;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import madkit.action.KernelAction;
import madkit.agr.CloudCommunity;
import madkit.agr.LocalCommunity;
import madkit.agr.LocalCommunity.Groups;
import madkit.kernel.AbstractAgent;
import madkit.kernel.JunitMadkit;
import madkit.kernel.Madkit.BooleanOption;

/**
 * @author Fabien Michel
 * @since MaDKit 5.0.0.10
 * @version 0.9
 * 
 */

public class AsynchronousDiscoverTest extends JunitMadkit {

	@Test
	public void multipleAsynchroneConnectionTest() {
		cleanHelperMDKs();
		addMadkitArgs(BooleanOption.network.toString());
//		 addMadkitArgs(LevelOption.networkLogLevel.toString(),"FINER");
		launchTest(new AbstractAgent() {
			@Override
			protected void activate() {
				launchThreadedMKNetworkInstance();
				launchThreadedMKNetworkInstance();
				launchThreadedMKNetworkInstance();
				KernelAction.LAUNCH_NETWORK.getActionFor(this).actionPerformed(null);
				launchThreadedMKNetworkInstance();
				launchThreadedMKNetworkInstance();
				pause(100);
				checkConnectedIntancesNb(this, 6);
				if (logger != null)
					logger.info("" + getAgentsWithRole(LocalCommunity.NAME, Groups.NETWORK, LocalCommunity.Roles.NET_AGENT));
				KernelAction.STOP_NETWORK.getActionFor(this).actionPerformed(null);
				checkConnectedIntancesNb(this, 0);
				pause(500);

				// not connected
				assertFalse(isCommunity(CloudCommunity.NAME));

				// second round
				KernelAction.LAUNCH_NETWORK.getActionFor(this).actionPerformed(null);
				checkConnectedIntancesNb(this, 6);
				cleanHelperMDKs();
				checkConnectedIntancesNb(this, 1);
			}
		});
	}

}
