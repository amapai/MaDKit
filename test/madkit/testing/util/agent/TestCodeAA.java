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
package madkit.testing.util.agent;

import madkit.action.KernelAction;
import madkit.agr.DefaultMaDKitRoles;
import madkit.agr.LocalCommunity;
import madkit.agr.LocalCommunity.Groups;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Message;
import madkit.message.EnumMessage;
import madkit.message.ObjectMessage;
import madkit.message.StringMessage;

/**
 * @author Fabien Michel
 * @since MaDKit 5.0.0.14
 * @version 0.9
 * 
 */
public class TestCodeAA extends AbstractAgent {


	public void reload(){
		launchAgent(getClass().getName(),0, true);
		killAgent(this);
	}
	
	@Override
	protected void activate() {
		try {
			Class<?> cl = Class.forName("java.lang.Thread");
			Object o = cl.newInstance();
			o.toString();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		Message m = new Message();
		System.err.println(m);
		sendMessage(LocalCommunity.NAME, Groups.SYSTEM, DefaultMaDKitRoles.GROUP_MANAGER_ROLE, m);
		System.err.println(m);

		m = new ObjectMessage<>("zd");
		System.err.println(m);
		sendMessage(LocalCommunity.NAME, Groups.SYSTEM, DefaultMaDKitRoles.GROUP_MANAGER_ROLE, m);
		System.err.println(m);

		m = new StringMessage("zd");
		System.err.println(m);
		sendMessage(LocalCommunity.NAME, Groups.SYSTEM, DefaultMaDKitRoles.GROUP_MANAGER_ROLE, m);
		System.err.println(m);

		m = new EnumMessage<>(KernelAction.COPY, "kj",3);
		System.err.println(m);
		sendMessage(LocalCommunity.NAME, Groups.SYSTEM, DefaultMaDKitRoles.GROUP_MANAGER_ROLE, m);
		System.err.println(m);

		try {
			System.err.println(this.getClass().getConstructor((Class<?>[])null));
		} catch (SecurityException | NoSuchMethodException e) {
			e.printStackTrace();
		}
//		proceedCommandMessage(new EnumMessage<AgentAction>(AgentAction.RELOAD));
//		proceedCommandMessage(new EnumMessage<AgentAction>(AgentAction.LAUNCH_AGENT,"madkit.testing.util.agent.SelfLaunch"));
	}
	
	public static void main(String[] args) {
		executeThisAgent(args);
//		String[] argss = {Option.launchAgents.toString(),"madkit.kernel.AbstractAgent"};
//		Madkit.main(argss);
	}

}
