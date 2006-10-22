package org.woped.woflan;

import org.woped.core.controller.*;
import org.woped.core.model.*;
import org.woped.core.model.petrinet.*;
import org.woped.core.utilities.LoggerManager;
import org.jgraph.graph.*;


import java.util.*;

public class StructuralAnalysis {

	//! Construct static analysis object from
	//! a petri-net editor
	//! Note that this object will not perform all
	//! its calculations at construction time
	//! Rather, each getter method knows which calculations
	//! need to be done and will trigger them
	public StructuralAnalysis(IEditor currentEditor) {
		m_currentEditor = currentEditor;
	}
	
	public int GetNumPlaces()
	{
		Calculate_BasicNetInfo();
		return m_places.size();
	}
	//! Returns an iterator over all places of the net
	public Iterator GetPlacesIterator()
	{
		Calculate_BasicNetInfo();
		return m_places.iterator();
	}
	
	public int GetNumTransitions()
	{
		Calculate_BasicNetInfo();
		return m_transitions.size();
	}
	public Iterator GetTransitionsIterator()
	{
		Calculate_BasicNetInfo();
		return m_transitions.iterator();
	}
	public int GetNumArcs()
	{
		return m_nNumArcs;		
	}

	public int GetNumSourcePlaces()
	{
		Calculate_BasicNetInfo();
		return m_sourcePlaces.size();
	}
	public Iterator GetSourcePlacesIterator()
	{
		Calculate_BasicNetInfo();
		return m_sourcePlaces.iterator();		
	}
	public int GetNumSourceTransitions()
	{
		Calculate_BasicNetInfo();
		return m_sourceTransitions.size();
	}
	public Iterator GetSourceTransitionsIterator()
	{
		Calculate_BasicNetInfo();
		return m_sourceTransitions.iterator();		
	}

	public int GetNumSinkPlaces()
	{
		Calculate_BasicNetInfo();
		return m_sinkPlaces.size();
	}
	public Iterator GetSinkPlacesIterator()
	{
		Calculate_BasicNetInfo();
		return m_sinkPlaces.iterator();		
	}
	public int GetNumSinkTransitions()
	{
		Calculate_BasicNetInfo();
		return m_sinkTransitions.size();
	}
	public Iterator GetSinkTransitionsIterator()
	{
		Calculate_BasicNetInfo();
		return m_sinkTransitions.iterator();		
	}

	public int GetNumNotConnectedNodes()
	{
		Calculate_Connections();
		return m_notConnectedNodes.size();
	}
	//! Return all nodes of the current net that
	//! are not connected	
	public Iterator GetNotConnectedNodes()
	{
		Calculate_Connections();
		return m_notConnectedNodes.iterator();		
	}
	public int GetNumNotStronglyConnectedNodes()
	{
		Calculate_Connections();
		return m_notStronglyConnectedNodes.size();		
	}
	//! Return all nodes of the current net that 
	//! are not strongly connected
	public Iterator GetNotStronglyConnectedNodes()
	{
		Calculate_Connections();
		return m_notStronglyConnectedNodes.iterator();
		
	}
	
	//! Remember a reference to the current editor
	//! as we need it to access the net
	private IEditor m_currentEditor;
	
	//! Will become true once
	//! the basic net info has been calculated
	boolean m_bBasicNetInfoAvailable = false;
	
	//! Stores a linked list of all the places of
	//! the processed net
	LinkedList m_places = new LinkedList();
	//! Stores a linked list of all the transitions of
	//! the processed net
	LinkedList m_transitions = new LinkedList();
	//! Stores the number of arcs contained in this net
	int m_nNumArcs=0;
	
	//! Stores a linked list of source places
	LinkedList m_sourcePlaces = new LinkedList();
	//! Stores a linked list of sink places
	LinkedList m_sinkPlaces = new LinkedList();
	
	//! Stores a linked list of source transitions
	LinkedList m_sourceTransitions = new LinkedList();
	//! Stores a linked list of sink transitions
	LinkedList m_sinkTransitions = new LinkedList();
	
	boolean m_bConnectionInfoAvailable = false;
	
	//! Stores a list of all nodes (transitions 
	//! and places that are not connected)
	HashSet m_notConnectedNodes = new HashSet();
	//! Stores a list of all nodes (transitions 
	//! and places that are not strongly connected)
	HashSet m_notStronglyConnectedNodes = new HashSet();
	
	//! Trigger the calculation of basic net information
	private void Calculate_BasicNetInfo()
	{
		// We cache all calculated information
		// Check if we already know what we need to know
		if (!m_bBasicNetInfoAvailable)
		{
			// Get the element container containing all our elements
			ModelElementContainer elements
			= m_currentEditor.getModelProcessor().getElementContainer();
			// Iterate through all elements and 
			// take notes
			Iterator i=elements.getRootElements().iterator();
			ArcConfiguration arcConfig = new ArcConfiguration();
			while (i.hasNext())
			{
				try
				{
					AbstractElementModel currentNode =				
						(AbstractElementModel)i.next();
					GetArcConfiguration(currentNode, arcConfig);
					switch (currentNode.getType())
					{
					case AbstractPetriNetModelElement.PLACE_TYPE:
						m_places.add(currentNode);
						if (arcConfig.m_numIncoming == 0)
							m_sourcePlaces.add(currentNode);
						if (arcConfig.m_numOutgoing==0)
							m_sinkPlaces.add(currentNode);
						break;
					case AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE:					
						// Treat simple and complex (operator) transitions
						// equally
					case AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE:
						m_transitions.add(currentNode);
						if (arcConfig.m_numIncoming == 0)
							m_sourceTransitions.add(currentNode);
						if (arcConfig.m_numOutgoing==0)
							m_sinkTransitions.add(currentNode);						
						break;												
					default:
						// Ignore all the rest
					}
						
				}
				catch(Exception e)
				{
					LoggerManager.info(Constants.WOFLAN_LOGGER, "Illegal object type found!");					
				}
			}
			// Just ask the arc map for its size...
			m_nNumArcs = elements.getArcMap().size();
			m_bBasicNetInfoAvailable = true;
		}		
	}
	private class ArcConfiguration
	{
		int m_numIncoming = 0;
		int m_numOutgoing = 0;
	}
	//! Get the configuration of the arcs connected to the
	//! specified element
	//! @param element specifies the element to be analysed
	//! @param config specifies the object that will receive the
	//!        number of incoming and outgoing arcs for the
	//!        specified element
	private void GetArcConfiguration(AbstractElementModel element,
			ArcConfiguration config)
	{
		// Reset values
		config.m_numIncoming = 0;
		config.m_numOutgoing = 0;
		Port port = element.getPort();
		for (Iterator i=port.edges();i.hasNext();)
		{
			Edge current = (Edge)i.next();
			// If the current port is the target
			// the edge is incoming
			if (current.getTarget()==port)
				++config.m_numIncoming;
			// If the current port is the source
			// the edge is outgoing
			if (current.getSource()==port)
				++config.m_numOutgoing;
		}
	}
	
	private void Calculate_Connections()
	{
		if (m_bConnectionInfoAvailable==true)
			return;
		m_bConnectionInfoAvailable=true;
		
		// First, calculate basic net information
		Calculate_BasicNetInfo();
		LinkedList netElements = new LinkedList();
		// A WoPeD graph contains more than just places
		// and transitions. We are only interested in those
		// however
		netElements.addAll(m_places);
		netElements.addAll(m_transitions);
		
		if (m_transitions.size()==0)
			return;

		// Create transition 't*'
        CreationMap tempMap = ((AbstractElementModel)m_transitions.getFirst()).getCreationMap();
        tempMap.setType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE);
        String tempID = "t*";
        tempMap.setName(tempID);
        tempMap.setId(tempID);
        tempMap.setEditOnCreation(false);
        AbstractElementModel ttemp = m_currentEditor.getModelProcessor().createElement(tempMap);
        netElements.add(ttemp);
        
        
        // Now connect the new transition 't*' to
        // the source and the target
        // For this to be possible, we will need
        // a unique source and a unique sink
        if ((m_sourcePlaces.size()==1)&&(m_sinkPlaces.size()==1))        	
        {                
        	AbstractElementModel source = (AbstractElementModel)m_sourcePlaces.getFirst(); 
        	String sourceID =source.getId();
        	AbstractElementModel target = (AbstractElementModel)m_sinkPlaces.getFirst(); 
        	String targetID = target.getId();        		
        	Object newEdge = m_currentEditor.getModelProcessor().createArc(tempID,sourceID);   
        	ttemp.getPort().addEdge(newEdge);
        	source.getPort().addEdge(newEdge);        	
        	newEdge = m_currentEditor.getModelProcessor().createArc(targetID,tempID);
        	ttemp.getPort().addEdge(newEdge);
        	target.getPort().addEdge(newEdge);
        }        
        		
		// First check for connectedness:
		// Return connection map presuming that all arcs may be
		// used in both directions
		RouteInfo[][] connectionGraph = GetAllConnections(netElements, true);
		if (connectionGraph!=null)
			GetUnconnectedNodes(ttemp, connectionGraph, m_notConnectedNodes);	
		
		// Now get the graph for strong connectedness
		// This will also give us all shortest distances
		// according to Moore's algorithm (no arc weights) 
		RouteInfo[][] strongConnectionGraph = GetAllConnections(netElements, false);
		if (strongConnectionGraph!=null)
			GetUnconnectedNodes(ttemp, strongConnectionGraph, m_notStronglyConnectedNodes);
		
		// Remove the element from the graph
		m_currentEditor.getModelProcessor().removeElement(tempID);		
	}
	//! Extract unconnected nodes from the given RouteInfo array and store
	//! it in the set
	//! An unconnected node is a node that has no connection to or from
	//! the specified node
	//! @param centralNode specifies the node all nodes need to be connected to
	//! @param connectionGraph specifies the RouteInfo array
	//! @param unconnectedNodes set that receives the unconnected nodes
	void GetUnconnectedNodes(
			AbstractElementModel centralNode,
			RouteInfo[][] connectionGraph, Set unconnectedNodes)
	{
		int nNumElements = connectionGraph.length;
		int nCentralNodeIndex = -1;
		for (int i=0;i<nNumElements;++i)
			if (connectionGraph[i][i].m_thisElement==centralNode)
				nCentralNodeIndex = i;
		if (nCentralNodeIndex!=-1)
		{
			// Add all elements that do not have a connection to or from the
			// specified "centralNode"
			for (int i=0;i<nNumElements;++i)
			{
				if (connectionGraph[nCentralNodeIndex][i].m_nDistanceToSource==-1)
					unconnectedNodes.add(connectionGraph[nCentralNodeIndex][i].m_thisElement);
				if (connectionGraph[i][nCentralNodeIndex].m_nDistanceToSource==-1)
					// If item i does not have a connection to nCentralNodeIndex,
					// item i (whose reference is always stored in [i][i]) must be added 
					// to the unconnected list
					unconnectedNodes.add(connectionGraph[i][i].m_thisElement);
			}
		}
	}				
	private class RouteInfo
	{
		//! Store a reference to the predecessor
		//! on the route back to the source
		//! null if no connection to the source exists
		public RouteInfo m_predecessor = null;
		//! stores the number of arcs between the
		//! source and this element
		//! or -1 if no connection exists
		public int m_nDistanceToSource = -1;
		//! Stores a reference to the actual petri-net
		//! element this entry has been created for
		public AbstractElementModel m_thisElement = null;  
	}
	//! This method calculates all shortest connections between net elements
	//! according to Moore's algorithm. Arc weights are ignored.
	//! @param netElements specifies a collection of net elements that should be 
	//!                    taken into account (we typically don't want all of them)
	//! @param ignoreArcDirection specifies whether the direction of arcs should be
	//!                           taken into account
	//! @return Returns a two-dimensional array of RouteInfo
	//!         containing one row for each element in netElements,
	//!         specifying for each element in netElements what is the distance
	//!         to that element. For each element in a row,
	//!         the shortest route to the element described by the row
	//!         can be reconstructed by following the m_predecessor back-references
	private RouteInfo[][] GetAllConnections(Collection netElements, boolean ignoreArcDirection)
	{
		int nNumNetElements = netElements.size();
		RouteInfo routeInfo[][] = new RouteInfo[nNumNetElements][nNumNetElements];
		HashMap nodeToIndex=new HashMap();
		
		// Build a map from node to index
		// We will need this when traversing the graph
		// (the graph doesn't know about our collection of input
		// net elements)
		Iterator nodeIndexIterator = netElements.iterator();
		int nNodeIndex = 0;
		while (nodeIndexIterator.hasNext())
		{
			nodeToIndex.put(nodeIndexIterator.next(),new Integer(nNodeIndex));
			++nNodeIndex;
		}
		
		try
		{
			// Iterate through outer index
			for (int i=0;i<nNumNetElements;++i)
			{							
				int j=0;
				Iterator innerIterator=netElements.iterator();
				while (innerIterator.hasNext())
				{
					routeInfo[i][j]= new RouteInfo();
					routeInfo[i][j].m_thisElement = 
						(AbstractElementModel)innerIterator.next();
					++j;
				}				
				// Apply Moore's algorithm
				// We have a distance of zero to ourselves
				routeInfo[i][i].m_nDistanceToSource = 0;
				// Define the starting point
				LinkedList currentList =
					new LinkedList();
				currentList.add(routeInfo[i][i]);
				// Keep going until no more elements need to be processed
				while (currentList.size()>0)
				{
					// Create the follow-up list
					LinkedList newList = new LinkedList();
					for (Iterator listContent=currentList.iterator();listContent.hasNext();)
					{
						RouteInfo currentRouteInfo = (RouteInfo)listContent.next();
						// Look up all connections (to and from, only from if directed
						// and iterate them)
						Port currentPort = currentRouteInfo.m_thisElement.getPort();
						Iterator edges =
							currentPort.edges();
						while (edges.hasNext())
						{
							Edge currentEdge = (Edge)edges.next();
							// Add destinations of edges
							// Interestingly we need a reference to DefaultPort
							// here, the port interface itself does not expose a getParent()
							// method
							// (Excourse: arcs connect to ports which are part of the node
							// the arc connects to. Ports and nodes have a child parent relation)
							DefaultPort targetPort = 
								(DefaultPort)currentEdge.getTarget();
							DefaultPort sourcePort = 
								(DefaultPort)currentEdge.getSource();
							AbstractElementModel target = null;
							if (targetPort!=currentPort)
								target =
									(AbstractElementModel)targetPort.getParent();
							// Depending on our configuration we care about the
							// direction of the edge or not
							if ((sourcePort!=currentPort)&&ignoreArcDirection)
								target =
									(AbstractElementModel)sourcePort.getParent();
							// Did we find anything useful ?
							if (target!=null)
							{
								// Use our node to index lookup table to
								// find the RouteInfo object corresponding to the
								// target
								RouteInfo newRouteInfo =
									routeInfo[i][((Integer)nodeToIndex.get(target)).intValue()];
								// See whether this node has already been visited
								if (newRouteInfo.m_nDistanceToSource==-1)
								{
									// Update the information on this node
									newRouteInfo.m_predecessor = currentRouteInfo;
									newRouteInfo.m_nDistanceToSource = currentRouteInfo.m_nDistanceToSource + 1;
									// Add it to the new node list
									newList.add(newRouteInfo);
								}
							}
						}
					}
					// Iterate through the new list and
					// see what's left
					currentList = newList;
				}
			}
		}
		catch(Exception e)
		{
			LoggerManager.info(Constants.WOFLAN_LOGGER, "Illegal object type found!");
			// Can't calculate if non petri-net element has been 
			// passed as input
			routeInfo = null;
		}
		return routeInfo;
	}
}
