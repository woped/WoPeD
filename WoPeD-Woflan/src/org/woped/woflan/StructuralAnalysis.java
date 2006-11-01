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
	
	public int GetNumOperators()
	{
		Calculate_BasicNetInfo();
		return m_operators.size();
	}
	public Iterator GetOperatorsIterator()
	{
		Calculate_BasicNetInfo();
		return m_operators.iterator();
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

	public int GetNumMisusedOperators()
	{
		Calculate_BasicNetInfo();
		return m_misusedOperators.size();
	}
	public Iterator GetMisusedOperatorsIterator()
	{
		Calculate_BasicNetInfo();
		return m_misusedOperators.iterator();		
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
	
	public int GetNumFreeChoiceViolations()
	{
		Calculate_FreeChoice();
		return m_freeChoiceViolations.size();		
	}
	//! Return a list of free-choice violations
	//! Each free-choice violation is represented 
	//! by a Set of nodes defining the violation
	//! @return Iterator through a list of sets 
	//!         of nodes violating the free-choice property
	public Iterator GetFreeChoiceViolations()
	{
		Calculate_FreeChoice();
		return m_freeChoiceViolations.iterator();		
	}
	
	public int GetNumWellStructurednessViolations()
	{
		Calculate_WellStructuredness();
		return m_wellStructurednessViolations.size();
	}
	
	//! Return a list of wellstructuredness violations
	//! Each violation is represented by a Set of nodes
	//! defining the violation
	//! @return Iterator through a list of sets
	//!         of nodes violating the well-structured property
	public Iterator GetWellStructurednessViolations()
	{
		Calculate_WellStructuredness();
		return m_wellStructurednessViolations.iterator();
	}
	
	//! Remember a reference to the current editor
	//! as we need it to access the net
	private IEditor m_currentEditor;
	
	//! Will become true once
	//! the basic net info has been calculated
	boolean m_bBasicNetInfoAvailable = false;
	
	//! Stores a set of all the places of
	//! the processed net
	HashSet m_places = new HashSet();
	//! Stores a set of all the transitions of
	//! the processed net
	HashSet m_transitions = new HashSet();
	
	//! Stores a set of all operators.
	//! Operators are AND-split, AND-join
	//! XOR-Split, XOR-Join, AND-Split-Join
	//! XOR-Split-Join
	HashSet m_operators = new HashSet();
	
	//! Stores the number of arcs contained in this net
	int m_nNumArcs=0;
	
	//! Stores a set of source places
	HashSet m_sourcePlaces = new HashSet();
	//! Stores a set of sink places
	HashSet m_sinkPlaces = new HashSet();
	
	//! Stores a set of source transitions
	HashSet m_sourceTransitions = new HashSet();
	//! Stores a set of sink transitions
	HashSet m_sinkTransitions = new HashSet();
	
	//! Misused operators are operators that
	//! do not have a specific minimum or maximum of
	//! input/output arcs
	//! as is required by their operator type
	HashSet m_misusedOperators = new HashSet();
	
	boolean m_bConnectionInfoAvailable = false;
	
	//! Stores a list of all nodes (transitions 
	//! and places that are not connected)
	HashSet m_notConnectedNodes = new HashSet();
	//! Stores a list of all nodes (transitions 
	//! and places that are not strongly connected)
	HashSet m_notStronglyConnectedNodes = new HashSet();
	
	boolean m_bFreeChoiceInfoAvailable = false;
	//! Stores a list of free-choice violations
	//! consisting of node sets
	HashSet m_freeChoiceViolations = new HashSet();
	
	boolean m_bWellStructurednessInfoAvailable = false;
	//! Stores a list of well-structuredness violations
	//! consisting of node sets
	HashSet m_wellStructurednessViolations = new HashSet();
	
	//! Trigger the calculation of basic net information
	private void Calculate_BasicNetInfo()
	{
		// We cache all calculated information
		// Check if we already know what we need to know
		if (m_bBasicNetInfoAvailable)
			return;
		m_bBasicNetInfoAvailable = true;
		
		// Get the element container containing all our elements
		ModelElementContainer elements
		= m_currentEditor.getModelProcessor().getElementContainer();
		// Iterate through all elements and 
		// take notes
		m_nNumArcs = 0;
		Iterator i=elements.getRootElements().iterator();
		UpdateStatistics(i);
		// Just ask the arc map for its size...
		m_nNumArcs+= elements.getArcMap().size();		
	}
	
	private void UpdateStatistics(Iterator i)
	{
		NetAlgorithms.ArcConfiguration arcConfig = new NetAlgorithms.ArcConfiguration();
		while (i.hasNext())
		{
			try
			{
				AbstractElementModel currentNode =				
					(AbstractElementModel)i.next();
				NetAlgorithms.GetArcConfiguration(currentNode, arcConfig);
				switch (currentNode.getType())
				{
				case AbstractPetriNetModelElement.PLACE_TYPE:
					m_places.add(currentNode);
					if (arcConfig.m_numIncoming == 0)
						m_sourcePlaces.add(currentNode);
					if (arcConfig.m_numOutgoing==0)
						m_sinkPlaces.add(currentNode);
					break;
				case AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE:
				{
					OperatorTransitionModel operator = (OperatorTransitionModel)currentNode;
					// Remember the operator
					// A list of operators is provided for
					// statistical reasons
					m_operators.add(operator);
					// Verify that the operator has the correct arc configuration
					// If this is not the case it will be added to the
					// misused operators list
					VerifyOperatorArcConfiguration(operator,arcConfig);
					ModelElementContainer simpleTransContainer =
						operator.getSimpleTransContainer();
					// Recursively call ourselves to add inner nodes
					Iterator innerIterator =simpleTransContainer.getRootElements().iterator();
					UpdateStatistics(innerIterator);
					// To have the total number of arcs we must subtract 
					// the number of incoming and outgoing arcs from the
					// number of inner arcs of the operator
					m_nNumArcs += simpleTransContainer.getArcMap().size()
						- (arcConfig.m_numIncoming + arcConfig.m_numOutgoing);
				}
				break;
				case AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE:					
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
	}
	
	//! Check whether the specified operator has the correct arc configuration
	//! and add it to the misused operators list if it doesn't
	//! @param operator the operator to be verified
	//! @param arcConfig specifies the previously determined arc configuration
	void VerifyOperatorArcConfiguration(OperatorTransitionModel operator,
			NetAlgorithms.ArcConfiguration arcConfig)
	{
		boolean isCorrectConfiguration = true;
		switch (operator.getOperatorType())
		{
		// All pure split operators must have exactly one input
		// and at least two outputs
		case OperatorTransitionModel.AND_SPLIT_TYPE:
		case OperatorTransitionModel.XOR_SPLIT_TYPE:
			isCorrectConfiguration =
				((arcConfig.m_numIncoming==1)&&
						(arcConfig.m_numOutgoing>1));
			break;
		// All pure join operators must have exactly one output
		// and at least two inputs 
		case OperatorTransitionModel.AND_JOIN_TYPE:
		case OperatorTransitionModel.XOR_JOIN_TYPE:
			isCorrectConfiguration =
				((arcConfig.m_numIncoming>1)&&
						(arcConfig.m_numOutgoing==1));
			break;
		// All split-join types must have at least two inputs
		// as well as at least two outputs
		case OperatorTransitionModel.XOR_SPLITJOIN_TYPE:
		case OperatorTransitionModel.AND_SPLITJOIN_TYPE:
			isCorrectConfiguration =
				((arcConfig.m_numIncoming>1)&&
						(arcConfig.m_numOutgoing>1));
			break;			
		}
		if (!isCorrectConfiguration)
			m_misusedOperators.add(operator);
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

		// Add temporary transition t*, connecting sink to source
		AbstractElementModel ttemp =
			AddTStar();
        netElements.add(ttemp);
        		
		// First check for connectedness:
		// Return connection map presuming that all arcs may be
		// used in both directions
		NetAlgorithms.RouteInfo[][] connectionGraph = NetAlgorithms.GetAllConnections(netElements, true);
		if (connectionGraph!=null)
			NetAlgorithms.GetUnconnectedNodes(ttemp, connectionGraph, m_notConnectedNodes);	
		
		// Now get the graph for strong connectedness
		// This will also give us all shortest distances
		// according to Moore's algorithm (no arc weights) 
		NetAlgorithms.RouteInfo[][] strongConnectionGraph = NetAlgorithms.GetAllConnections(netElements, false);
		if (strongConnectionGraph!=null)
			NetAlgorithms.GetUnconnectedNodes(ttemp, strongConnectionGraph, m_notStronglyConnectedNodes);

		RemoveTStar(ttemp);
	}
	
	AbstractElementModel AddTStar()
	{
		// Create transition 't*'
        CreationMap tempMap = ((AbstractElementModel)m_transitions.iterator().next()).getCreationMap();
        tempMap.setType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE);
        String tempID = "t*";
        tempMap.setName(tempID);
        tempMap.setId(tempID);
        tempMap.setEditOnCreation(false);
        AbstractElementModel ttemp = m_currentEditor.getModelProcessor().createElement(tempMap);
        
        // Now connect the new transition 't*' to
        // the source and the target
        // For this to be possible, we will need
        // a unique source and a unique sink
        if ((m_sourcePlaces.size()==1)&&(m_sinkPlaces.size()==1))        	
        {                
        	AbstractElementModel source = (AbstractElementModel)m_sourcePlaces.iterator().next(); 
        	String sourceID =source.getId();
        	AbstractElementModel target = (AbstractElementModel)m_sinkPlaces.iterator().next(); 
        	String targetID = target.getId();        		
        	Object newEdge = m_currentEditor.getModelProcessor().createArc(tempID,sourceID);   
        	ttemp.getPort().addEdge(newEdge);
        	source.getPort().addEdge(newEdge);        	
        	newEdge = m_currentEditor.getModelProcessor().createArc(targetID,tempID);
        	ttemp.getPort().addEdge(newEdge);
        	target.getPort().addEdge(newEdge);
        }         
        
        return ttemp;
	}
	
	void RemoveTStar(AbstractElementModel tstar)
	{
		// Remove the element from the graph
		m_currentEditor.getModelProcessor().removeElement(tstar.getId());		
	}
	
	void Calculate_FreeChoice()
	{
		if (m_bFreeChoiceInfoAvailable)
			return;
		m_bFreeChoiceInfoAvailable = true;
		
		// First, calculate basic net information
		Calculate_BasicNetInfo();

		// The first thing we look for are forward-branched places (conflicts)
		// and their follow-up transitions
		Set placeResults = GetNonFreeChoiceGroups(m_places.iterator(),false);
		// Now look for backward-branched transitions (synchronization)
		// and their preceeding places
		Set transitionResults = GetNonFreeChoiceGroups(m_transitions.iterator(),true);
		
		m_freeChoiceViolations.addAll(placeResults);
		m_freeChoiceViolations.addAll(transitionResults);
	}
	Set GetNonFreeChoiceGroups(Iterator i, boolean swapArcDirection)
	{
		Set result = new HashSet();
		// Look for forward-branched places (conflicts)
		// and their follow-up transitions
		while (i.hasNext()){
			// Determine the arc configuration of the current place
			AbstractElementModel currentPlace = (AbstractElementModel)i.next();
			
			// Have a closer look at the follow-up transitions			
			// Collect all affected nodes a priori just in case
			HashSet violationGroup = new HashSet();
			boolean violation = false;
			Set compareSet = null;
			Set successors = NetAlgorithms.GetDirectlyConnectedNodes(currentPlace,
					swapArcDirection?NetAlgorithms.connectionTypeINBOUND
							:NetAlgorithms.connectionTypeOUTBOUND);
			for (Iterator s=successors.iterator();s.hasNext();)
			{
				AbstractElementModel successor = (AbstractElementModel)s.next();
				Set predecessors = NetAlgorithms.GetDirectlyConnectedNodes(successor,
						swapArcDirection?NetAlgorithms.connectionTypeOUTBOUND
								:NetAlgorithms.connectionTypeINBOUND);
				if (compareSet==null)
					compareSet = predecessors;
				else
				{
					// All predecessors of all successors of our
					// original place must be the same
					violation = violation || (!compareSet.equals(predecessors));
				}
				// Add the element and all its predecessors
				violationGroup.addAll(predecessors);							
				violationGroup.add(successor);
			}
			if (violation)
			{
				// We have a violation, store the group in the list
				result.add(violationGroup);	
			}
		}
		return result;
	}
	
	void Calculate_WellStructuredness()
	{
		if (m_bWellStructurednessInfoAvailable)
			return;
		m_bWellStructurednessInfoAvailable = true;

		// First, calculate basic net information
		Calculate_BasicNetInfo();
		
		LinkedList netElements = new LinkedList();
		// A WoPeD graph contains more than just places
		// and transitions. We are only interested in those
		// however
		netElements.addAll(m_places);
		netElements.addAll(m_transitions);

		// Add the temporary transition 't*'
		AbstractElementModel tStar = AddTStar();
		netElements.add(tStar);
		
		Iterator sourceIterator = netElements.iterator();
		while (sourceIterator.hasNext())
		{
			// Reset all markings of all places and transitions
			for (Iterator blank=netElements.iterator();blank.hasNext();)
			{
				AbstractElementModel currentBlank = (AbstractElementModel)blank.next();
				currentBlank.setMarking(-1);
			}
			// Iterate through all objects and
			// analyse them for well-structuredness violations
			AbstractElementModel currentSource =
				(AbstractElementModel) sourceIterator.next();
			// Now look into all successors
			Set successors = NetAlgorithms.GetDirectlyConnectedNodes(currentSource, 
					NetAlgorithms.connectionTypeOUTBOUND);
			Iterator successorIterator = successors.iterator();
			// Each outgoing arc gets its own number
			// This way we can tell which node was reached by which arc
			int arcCounter = 0;
			while (successorIterator.hasNext())
			{
				AbstractElementModel currentSuccessor =
					(AbstractElementModel)successorIterator.next();
				// We mark all reachable elements using breadth-first search 
				// Clearly, we need a stack for that
				LinkedList elementStack = new LinkedList();
				// Mark the source as visited in any case
				currentSource.setMarking(arcCounter);
				elementStack.addLast(currentSuccessor);
				while (elementStack.size()>0)
				{
					AbstractElementModel currentElement = 
						(AbstractElementModel) elementStack.removeLast();
					int currentMarking = currentElement.getMarking();
					if (currentMarking==-1)
					{
						// Mark the current node as it hasn't been visited before
						currentElement.setMarking(arcCounter);						
						// Recursively add all nodes reachable directly from the
						// current node
						Set recurseSet =
							NetAlgorithms.GetDirectlyConnectedNodes(currentElement,
									NetAlgorithms.connectionTypeOUTBOUND);
						for (Iterator recIt=recurseSet.iterator();recIt.hasNext();)
							elementStack.addLast(recIt.next());
					}
					else
						if ((currentMarking!=arcCounter)&&
								(currentElement.getType()!=currentSource.getType()))
					{
							Set notWellStructuredSet = new HashSet();
							// Found well-structuredness violation
							// Add to current set
							notWellStructuredSet.add(currentSource);
							notWellStructuredSet.add(currentElement);
							m_wellStructurednessViolations.add(notWellStructuredSet);
					}
				}
				++arcCounter;
			}
		}

		// Remove temporary transition from the net
		RemoveTStar(tStar);
	}
}
