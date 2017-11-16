package de.hpi.bpt.process.engine;

import java.util.Set;

import de.hpi.bpt.hypergraph.abs.Vertex;

/***
 * 
 * @author Artem Polyvyanyy
 *
 */
public interface IProcess {
	public boolean isTerminated();
	public Set<Vertex> getEnabledElements();
	public boolean fire(Vertex t);
	public void serialize();
}
