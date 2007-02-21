package org.woped.editor.controller.vc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;

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
	
	public int getNumPlaces()
	{
		calculateBasicNetInfo();
		return m_places.size();
	}
	//! Returns an iterator over all places of the net
	public Iterator getPlacesIterator()
	{
		calculateBasicNetInfo();
		return m_places.iterator();
	}
	
	public int getNumTransitions()
	{
		calculateBasicNetInfo();
		return m_transitions.size();
	}
	public Iterator getTransitionsIterator()
	{
		calculateBasicNetInfo();
		return m_transitions.iterator();
	}
	
	public int getNumOperators()
	{
		calculateBasicNetInfo();
		return m_operators.size();
	}
	public Iterator getOperatorsIterator()
	{
		calculateBasicNetInfo();
		return m_operators.iterator();
	}
	
	public int getNumArcs()
	{
		return m_nNumArcs;		
	}

	public int getNumSourcePlaces()
	{
		calculateBasicNetInfo();
		return m_sourcePlaces.size();
	}
	public Iterator getSourcePlacesIterator()
	{
		calculateBasicNetInfo();
		return m_sourcePlaces.iterator();		
	}
	public int getNumSourceTransitions()
	{
		calculateBasicNetInfo();
		return m_sourceTransitions.size();
	}
	public Iterator getSourceTransitionsIterator()
	{
		calculateBasicNetInfo();
		return m_sourceTransitions.iterator();		
	}

	public int getNumSinkPlaces()
	{
		calculateBasicNetInfo();
		return m_sinkPlaces.size();
	}
	public Iterator getSinkPlacesIterator()
	{
		calculateBasicNetInfo();
		return m_sinkPlaces.iterator();		
	}
	public int getNumSinkTransitions()
	{
		calculateBasicNetInfo();
		return m_sinkTransitions.size();
	}
	public Iterator getSinkTransitionsIterator()
	{
		calculateBasicNetInfo();
		return m_sinkTransitions.iterator();		
	}

	public int getNumMisusedOperators()
	{
		calculateBasicNetInfo();
		return m_misusedOperators.size();
	}
	public Iterator getMisusedOperatorsIterator()
	{
		calculateBasicNetInfo();
		return m_misusedOperators.iterator();		
	}
	
	public int getNumNotConnectedNodes()
	{
		calculateConnections();
		return m_notConnectedNodes.size();
	}
	//! Return all nodes of the current net that
	//! are not connected	
	public Iterator getNotConnectedNodes()
	{
		calculateConnections();
		return m_notConnectedNodes.iterator();		
	}
	public int getNumNotStronglyConnectedNodes()
	{
		calculateConnections();
		return m_notStronglyConnectedNodes.size();		
	}
	//! Return all nodes of the current net that 
	//! are not strongly connected
	public Iterator getNotStronglyConnectedNodes()
	{
		calculateConnections();
		return m_notStronglyConnectedNodes.iterator();		
	}
	
	public int getNumFreeChoiceViolations()
	{
		calculateFreeChoice();
		return m_freeChoiceViolations.size();		
	}
	//! Return a list of free-choice violations
	//! Each free-choice violation is represented 
	//! by a Set of nodes defining the violation
	//! @return Iterator through a list of sets 
	//!         of nodes violating the free-choice property
	public Iterator getFreeChoiceViolations()
	{
		calculateFreeChoice();
		return m_freeChoiceViolations.iterator();		
	}
	
	//! FIXME: Well-structuredness violation detection
	//! does not always work.
	//! This algorithm will be replaced with 
	//! a max-flow min-cut algorithm soon.
	//! Meanwhile, woflan results will be used
	//! instead of the results of this method	
	public int getNumWellStructurednessViolations()
	{
		calculateWellStructuredness();
		return m_wellStructurednessViolations.size();
	}
	
	//! Return a list of wellstructuredness violations
	//! Each violation is represented by a Set of nodes
	//! defining the violation
	//! FIXME: Well-structuredness violation detection
	//! does not always work.
	//! This algorithm will be replaced with 
	//! a max-flow min-cut algorithm soon.
	//! Meanwhile, woflan results will be used
	//! instead of the results of this method	
	//! @return Iterator through a list of sets
	//!         of nodes violating the well-structured property
	public Iterator getWellStructurednessViolations()
	{
		calculateWellStructuredness();
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
	HashSet<AbstractElementModel> m_places = new HashSet<AbstractElementModel>();
	//! Stores a set of all the transitions of
	//! the processed net
	HashSet<AbstractElementModel> m_transitions = new HashSet<AbstractElementModel>();
	
	//! Stores a set of all operators.
	//! Operators are AND-split, AND-join
	//! XOR-Split, XOR-Join, AND-Split-Join
	//! XOR-Split-Join
	HashSet<AbstractElementModel> m_operators = new HashSet<AbstractElementModel>();
	
	//! Stores the number of arcs contained in this net
	int m_nNumArcs=0;
	
	//! Stores a set of source places
	HashSet<AbstractElementModel> m_sourcePlaces = new HashSet<AbstractElementModel>();
	//! Stores a set of sink places
	HashSet<AbstractElementModel> m_sinkPlaces = new HashSet<AbstractElementModel>();
	
	//! Stores a set of source transitions
	HashSet<AbstractElementModel> m_sourceTransitions = new HashSet<AbstractElementModel>();
	//! Stores a set of sink transitions
	HashSet<AbstractElementModel> m_sinkTransitions = new HashSet<AbstractElementModel>();
	
	//! Misused operators are operators that
	//! do not have a specific minimum or maximum of
	//! input/output arcs
	//! as is required by their operator type
	HashSet<AbstractElementModel> m_misusedOperators = new HashSet<AbstractElementModel>();
	
	boolean m_bConnectionInfoAvailable = false;
	
	//! Stores a list of all nodes (transitions 
	//! and places that are not connected)
	HashSet<AbstractElementModel> m_notConnectedNodes = new HashSet<AbstractElementModel>();
	//! Stores a list of all nodes (transitions 
	//! and places that are not strongly connected)
	HashSet<AbstractElementModel> m_notStronglyConnectedNodes = new HashSet<AbstractElementModel>();
	
	boolean m_bFreeChoiceInfoAvailable = false;
	//! Stores a list of free-choice violations
	//! consisting of node sets
	HashSet<Set<AbstractElementModel>> m_freeChoiceViolations = new HashSet<Set<AbstractElementModel>>();
	
	boolean m_bWellStructurednessInfoAvailable = false;
	//! Stores a list of well-structuredness violations
	//! consisting of node sets
	HashSet<Set<AbstractElementModel>> m_wellStructurednessViolations = new HashSet<Set<AbstractElementModel>>();
	
	//! Trigger the calculation of basic net information
	private void calculateBasicNetInfo()
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
		updateStatistics(i);
		// Just ask the arc map for its size...
		m_nNumArcs+= elements.getArcMap().size();		
	}
	
	private void updateStatistics(Iterator i)
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
					verifyOperatorArcConfiguration(operator,arcConfig);
					ModelElementContainer simpleTransContainer =
						operator.getSimpleTransContainer();
					// Recursively call ourselves to add inner nodes
					Iterator innerIterator =simpleTransContainer.getRootElements().iterator();
					updateStatistics(innerIterator);
					// To have the total number of arcs we must subtract 
					// the number of incoming and outgoing arcs from the
					// number of inner arcs of the operator
					m_nNumArcs += simpleTransContainer.getArcMap().size()
						- (arcConfig.m_numIncoming + arcConfig.m_numOutgoing);
				}
				break;
				case AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE:
				// Default behaviour for sub processes is to treat them as a single transition
				case AbstractPetriNetModelElement.SUBP_TYPE:
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
			}
		}
	}
	
	//! Check whether the specified operator has the correct arc configuration
	//! and add it to the misused operators list if it doesn't
	//! @param operator the operator to be verified
	//! @param arcConfig specifies the previously determined arc configuration
	void verifyOperatorArcConfiguration(OperatorTransitionModel operator,
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
		case OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE:
		case OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE:
			isCorrectConfiguration =
				((arcConfig.m_numIncoming>1)&&
						(arcConfig.m_numOutgoing>1));
			break;			
		}
		if (!isCorrectConfiguration)
			m_misusedOperators.add(operator);
	}
	
	
	private void calculateConnections()
	{
		if (m_bConnectionInfoAvailable==true)
			return;
		m_bConnectionInfoAvailable=true;
		
		// First, calculate basic net information
		calculateBasicNetInfo();
		LinkedList<AbstractElementModel> netElements = new LinkedList<AbstractElementModel>();
		// A WoPeD graph contains more than just places
		// and transitions. We are only interested in those
		// however
		netElements.addAll(m_places);
		netElements.addAll(m_transitions);
		
		if (m_transitions.size()==0)
			return;

		// Add temporary transition t*, connecting sink to source
		AbstractElementModel ttemp =
			addTStar();
		if (ttemp!=null)
			netElements.add(ttemp);
        		
		// First check for connectedness:
		// Return connection map presuming that all arcs may be
		// used in both directions
		NetAlgorithms.RouteInfo[][] connectionGraph = NetAlgorithms.getAllConnections(netElements, true);
		if (connectionGraph!=null)
			NetAlgorithms.getUnconnectedNodes(ttemp, connectionGraph, m_notConnectedNodes);	
		
		// Now get the graph for strong connectedness
		// This will also give us all shortest distances
		// according to Moore's algorithm (no arc weights) 
		NetAlgorithms.RouteInfo[][] strongConnectionGraph = NetAlgorithms.getAllConnections(netElements, false);
		if (strongConnectionGraph!=null)
			NetAlgorithms.getUnconnectedNodes(ttemp, strongConnectionGraph, m_notStronglyConnectedNodes);

		if (ttemp!=null)
			removeTStar(ttemp);
	}
	
	AbstractElementModel addTStar()
	{
		AbstractElementModel ttemp = null;
		// Create transition 't*'
		Iterator i = m_transitions.iterator();
		AbstractElementModel transitionTemplate = ((i.hasNext())?(AbstractElementModel)i.next():null);
		if (transitionTemplate!=null)
		{
			CreationMap tempMap = transitionTemplate.getCreationMap();
			tempMap.setType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE);
			String tempID = "t*";
			tempMap.setName(tempID);
			tempMap.setId(tempID);
			tempMap.setEditOnCreation(false);
			ttemp = m_currentEditor.getModelProcessor().createElement(tempMap);

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
				if (newEdge != null)
				{
					ttemp.getPort().addEdge(newEdge);
					target.getPort().addEdge(newEdge);
				}
			}         
		}
        return ttemp;
	}
	
	void removeTStar(AbstractElementModel tstar)
	{
		// Remove the element from the graph
		m_currentEditor.getModelProcessor().removeElement(tstar.getId());		
	}
	
	void calculateFreeChoice()
	{
		if (m_bFreeChoiceInfoAvailable)
			return;
		m_bFreeChoiceInfoAvailable = true;
		
		// First, calculate basic net information
		calculateBasicNetInfo();

		// The first thing we look for are forward-branched places (conflicts)
		// and their follow-up transitions
		Set<Set<AbstractElementModel>> placeResults = getNonFreeChoiceGroups(m_places.iterator(),false);
		// Now look for backward-branched transitions (synchronization)
		// and their preceeding places
		// Commented out: Probably not really needed as this condition is already covered with
		// the detection of forward-branched places 2007-01-15, AE
		// Set<Set<AbstractElementModel>> transitionResults = getNonFreeChoiceGroups(m_transitions.iterator(),true);
		
		m_freeChoiceViolations.addAll(placeResults);
		//m_freeChoiceViolations.addAll(transitionResults);
	}
	Set<Set<AbstractElementModel>> getNonFreeChoiceGroups(Iterator i, boolean swapArcDirection)
	{
		Set<Set<AbstractElementModel>> result = new HashSet<Set<AbstractElementModel>>();
		// Look for forward-branched places (conflicts)
		// and their follow-up transitions
		while (i.hasNext()){
			// Determine the arc configuration of the current place
			AbstractElementModel currentPlace = (AbstractElementModel)i.next();
			
			// Have a closer look at the follow-up transitions			
			// Collect all affected nodes a priori just in case
			HashSet<AbstractElementModel> violationGroup = new HashSet<AbstractElementModel>();
			boolean violation = false;
			Set<AbstractElementModel> compareSet = null;
			Set<AbstractElementModel> successors = NetAlgorithms.getDirectlyConnectedNodes(currentPlace,
					swapArcDirection?NetAlgorithms.connectionTypeINBOUND
							:NetAlgorithms.connectionTypeOUTBOUND);
			for (Iterator s=successors.iterator();s.hasNext();)
			{
				AbstractElementModel successor = (AbstractElementModel)s.next();
				Set<AbstractElementModel> predecessors = NetAlgorithms.getDirectlyConnectedNodes(successor,
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
	
	void calculateWellStructuredness()
	{
		if (m_bWellStructurednessInfoAvailable)
			return;
		m_bWellStructurednessInfoAvailable = true;

		// First, calculate basic net information
		calculateBasicNetInfo();
		
		LinkedList<AbstractElementModel> netElements = new LinkedList<AbstractElementModel>();
		// A WoPeD graph contains more than just places
		// and transitions. We are only interested in those
		// however
		netElements.addAll(m_places);
		netElements.addAll(m_transitions);

		// Add the temporary transition 't*'
		AbstractElementModel tStar = addTStar();
		if (tStar!=null)
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
			Set successors = NetAlgorithms.getDirectlyConnectedNodes(currentSource, 
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
				LinkedList<AbstractElementModel> elementStack = new LinkedList<AbstractElementModel>();
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
						Set<AbstractElementModel> recurseSet =
							NetAlgorithms.getDirectlyConnectedNodes(currentElement,
									NetAlgorithms.connectionTypeOUTBOUND);
						
						for (AbstractElementModel aem : recurseSet) {
						    elementStack.addLast(aem);
						}						
					}
					else
					{
						// Treat sub-processes like transitions
						int currentElementType = currentElement.getType();
						if (currentElementType == PetriNetModelElement.SUBP_TYPE)
							currentElementType = PetriNetModelElement.TRANS_SIMPLE_TYPE;
						int currentSourceType = currentSource.getType();
						if (currentSourceType == PetriNetModelElement.SUBP_TYPE)
							currentSourceType = PetriNetModelElement.TRANS_SIMPLE_TYPE;
						
						if ((currentMarking!=arcCounter)&&
								(currentElementType!=currentSourceType))
						{
							Set<AbstractElementModel> notWellStructuredSet = new HashSet<AbstractElementModel>();
							// Found well-structuredness violation
							// Add to current set
							notWellStructuredSet.add(currentSource);
							notWellStructuredSet.add(currentElement);
							m_wellStructurednessViolations.add(notWellStructuredSet);
						}
					}
				}
				++arcCounter;
			}
		}

		// Remove temporary transition from the net
		if (tStar!=null)
			removeTStar(tStar);
	}
}
