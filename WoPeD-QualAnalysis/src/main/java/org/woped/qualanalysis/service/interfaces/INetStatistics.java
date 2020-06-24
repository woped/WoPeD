package org.woped.qualanalysis.service.interfaces;

import java.util.Set;

import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

/**
 * interface for NetStatistics-parts of qualanalysis servies.
 * all classes which implement the NetStatistics-methods of a service must implement this interface
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 *
 */
public interface INetStatistics {
	
	/**
	 * 
	 * @return a set with all places
	 */
	public Set<AbstractPetriNetElementModel> getPlaces();
	
	/**
	 * 
	 * @return a set with all transitions
	 */
	public Set<AbstractPetriNetElementModel> getTransitions();

	/**
	 * 
	 * @return a set with all operator-transitions
	 */
	public Set<AbstractPetriNetElementModel> getOperators();
	
	/**
	 * 
	 * @return a set with all AND-join operators and operators that function as an AND-join (e.g. and-split-join)
	 */
	public Set<AbstractPetriNetElementModel> getAndJoins();
	
	/**
	 * 
	 * @return a set with all AND-split operators and operators that function as an AND-split (e.g. and-split-join)
	 */
	public Set<AbstractPetriNetElementModel> getAndSplits();
	
	/**
	 * 
	 * @return a set with all XOR-join operators and operators that function as an XOR-join (e.g. xor-split-join)
	 */
	public Set<AbstractPetriNetElementModel> getXorJoins();
	
	/**
	 * 
	 * @return a set with all XOR-split operators and operators that function as an XOR-split (e.g. xor-split-join)
	 */
	public Set<AbstractPetriNetElementModel> getXorSplits();

	/**
	 * 
	 * @return a set with all subprocesses
	 */
	public Set<AbstractPetriNetElementModel> getSubprocesses();

	/**
	 * 
	 * @return the number of arcs
	 */
	public int getNumArcs();
}
