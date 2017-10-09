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
package madkit.simulation.probe;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Probe;
import madkit.simulation.SimulationException;

/**
 * This probe inspects fields of type T on agents of type A and its subclasses.
 * 
 * @param <A>
 *            the most common class type expected in this group (e.g. AbstractAgent)
 * @param <T>
 *            the type of the property, i.e. Integer (this works if the field is an int, i.e. a primitive type)
 * @author Fabien Michel
 * @since MaDKit 5.0.0.13
 * @version 5.1
 */
public class PropertyProbe<A extends AbstractAgent, T> extends Probe<A> {

    private final Map<Class<? extends A>, Field> fields = new HashMap<>();
    private final String fieldName;

    private Field cachedField;
    private Class<? extends A> cachedClass;

    /**
     * Builds a new PropertyProbe considering a CGR location and the name of the class's field.
     * 
     * @param community
     * @param group
     * @param role
     * @param fieldName
     *            the name of a field which is encapsulated in type <A>
     */
    public PropertyProbe(String community, String group, String role, String fieldName) {
	super(community, group, role);
	this.fieldName = fieldName;
    }

    /**
     * update <code>cachedField</code> if required
     * 
     * @param agentClass
     */
    private void updateCache(A agent) {
	@SuppressWarnings("unchecked")
	final Class<? extends A> agentClass = (Class<? extends A>) agent.getClass();
	if (agentClass != cachedClass) {
	    cachedClass = agentClass;
	    cachedField = fields.get(cachedClass);
	    if (cachedField == null) {
		try {
		    cachedField = findFieldOn(cachedClass, fieldName);
		    fields.put(cachedClass, cachedField);
		}
		catch(NoSuchFieldException e) {
		    throw new SimulationException(toString() + " on " + agent, e);
		}
	    }
	}
    }

    /**
     * Returns the current value of the agent's field
     * 
     * @param agent
     *            the agent to probe
     * @return the actual value of the agent's field
     */
    @SuppressWarnings("unchecked")
    public T getPropertyValue(final A agent) {
	updateCache(agent);
	try {
	    return (T) cachedField.get(agent);
	}
	catch(IllegalAccessException e) {
	    throw new SimulationException(toString() + " on " + agent, e);
	}
    }

    /**
     * Should be used to work with primitive types or fields which are initially <code>null</code>
     * 
     * @param agent
     * @param value
     */
    public void setPropertyValue(final A agent, final T value) {
	updateCache(agent);
	try {
	    cachedField.set(agent, value);
	}
	catch(IllegalArgumentException | IllegalAccessException e) {
	    throw new SimulationException(toString() + " on " + agent, e);
	}
    }

    /**
     * Returns the maximum for the property over all the agents. The property must be numerical or {@link Comparable} for
     * this to work.
     * 
     * @return the maximum value for this property
     */
    @SuppressWarnings("unchecked")
    public T getMaxValue() {
	T max = null;
	for (final A a : getCurrentAgentsList()) {
	    try {
		if (max == null) {
		    max = getPropertyValue(a);
		}
		final T val = getPropertyValue(a);
		if (((Comparable<T>) val).compareTo(max) > 0) {
		    max = val;
		}
	    }
	    catch(ClassCastException e) {
		throw new SimulationException(toString() + " on " + a, e);
	    }
	}
	return max;
    }

    /**
     * Returns the minimum for the property over all the agents. The property must be numerical or {@link Comparable} for
     * this to work.
     * 
     * @return the minimum value for this property
     */
    @SuppressWarnings("unchecked")
    public T getMinValue() {
	T min = null;
	for (final A a : getCurrentAgentsList()) {
	    try {
		if (min == null) {
		    min = getPropertyValue(a);
		}
		final T val = getPropertyValue(a);
		if (((Comparable<T>) val).compareTo(min) < 0) {
		    min = val;
		}
	    }
	    catch(ClassCastException e) {
		throw new SimulationException(toString() + " on " + a, e);
	    }
	}
	return min;
    }

    /**
     * Returns the average value for the property over all the agents. The property must be numerical for this to work.
     * 
     * @return the average value for this property
     */
    public double getAverageValue() {
	double total = 0;
	for (final A a : getCurrentAgentsList()) {
	    try {
		total += ((Number) getPropertyValue(a)).doubleValue();
	    }
	    catch(ClassCastException e) {
		throw new SimulationException(toString() + " on " + a, e);
	    }
	}
	return total / size();
    }

}