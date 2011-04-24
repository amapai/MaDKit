/*
 * Copyright 1997-2011 Fabien Michel, Olivier Gutknecht, Jacques Ferber
 * 
 * This file is part of MadKit.
 * 
 * MadKit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MadKit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with MadKit. If not, see <http://www.gnu.org/licenses/>.
 */
package madkit.gui;

import static madkit.gui.MadkitActions.AGENT_LAUNCH_AGENT;
import static madkit.gui.MadkitActions.CONNECT_WEB_REPO;
import static madkit.gui.MadkitActions.MADKIT_CLONE;
import static madkit.gui.MadkitActions.MADKIT_DEICONIFY_ALL;
import static madkit.gui.MadkitActions.MADKIT_EXIT_ACTION;
import static madkit.gui.MadkitActions.MADKIT_ICONIFY_ALL;
import static madkit.gui.MadkitActions.MADKIT_KILL_AGENTS;
import static madkit.gui.MadkitActions.MADKIT_LAUNCH_DEMO;
import static madkit.gui.MadkitActions.MADKIT_LAUNCH_NETWORK;
import static madkit.gui.MadkitActions.MADKIT_LOAD_JAR_FILE;
import static madkit.gui.MadkitActions.MADKIT_RESTART;
import static madkit.gui.MadkitActions.MADKIT_STOP_NETWORK;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import madkit.kernel.AbstractAgent;
import madkit.kernel.KernelAddress;
import madkit.kernel.Madkit;

/**
 * Class containing static which could used to build agent GUI
 * 
 * @author Fabien Michel
 * @since MadKit 5.0.0.7
 * @version 0.9.1
 * 
 */
final public class MKToolkit {
	
	// TODO every Icon static. So avoiding useless instances //TODO create this
	// only if required
	static ConcurrentMap<AbstractAgent, List<AgentUIComponent>> agentUIListeners = new ConcurrentHashMap<AbstractAgent, List<AgentUIComponent>>();
	static ConcurrentMap<KernelAddress, JLabel> networkLabel = new ConcurrentHashMap<KernelAddress, JLabel>();
	private static HashMap<KernelAddress,Map<MadkitActions,Action>> globalActions;
	static Map<KernelAddress, GUIManagerAgent> guiManagers = new ConcurrentHashMap<KernelAddress, GUIManagerAgent>();

	static void buildGlobalActions(GUIManagerAgent guiManager) {
		MadkitActions.registerGlobalActions(guiManager);
		guiManagers.put(guiManager.getKernelAddress(), guiManager);
	}

	static public void updateAgentUI(AbstractAgent a) {
		List<AgentUIComponent> l = agentUIListeners.get(a);
		if (l != null) {
			for (AgentUIComponent ui : l) {
				ui.updateAgentUI();
			}
		}
	}

	static public void updateAgentsUI() {
		for (List<AgentUIComponent> list : agentUIListeners.values()) {
			for (AgentUIComponent ui : list) {
				ui.updateAgentUI();
			}
		}
	}

	static public JMenu createLogLevelMenu(final AbstractAgent agent) {
		LogLevelMenu menu = new LogLevelMenu(agent);
		menu.setMnemonic(KeyEvent.VK_L);
		addUIListenerFor(agent, menu);
		return menu;
	}

	static void addUIListenerFor(final AbstractAgent agent, AgentUIComponent component) {
		agentUIListeners.putIfAbsent(agent, new ArrayList<AgentUIComponent>());
		agentUIListeners.get(agent).add(component);
	}

	static public JMenu createLaunchingMenu(final AbstractAgent agent) {
		return new AgentMenu(agent);
	}
	
	static public JMenu createAgentsMenu(final AbstractAgent agent) {
		return guiManagers.get(agent.getKernelAddress()).createAgentsMenu();
	}

	static public JMenu createDemosMenu(final AbstractAgent agent) {
		return guiManagers.get(agent.getKernelAddress()).createDemosMenu();
	}
}