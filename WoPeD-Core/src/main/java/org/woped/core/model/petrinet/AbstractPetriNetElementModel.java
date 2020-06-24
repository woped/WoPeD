package org.woped.core.model.petrinet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.woped.core.Constants;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ElementContext;
import org.woped.core.model.IntPair;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.utilities.LoggerManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         Note: (from JGraph) <br>
 *         When combining the attributes from a GraphCell with the attributes
 *         from the CellView, the graph cell's attributes have precedence over
 *         the view's attributes. The special value attribute is in sync with
 *         the cell's user object.
 * 
 * 09.10.2003
 */
public abstract class AbstractPetriNetElementModel extends DefaultGraphCell implements Serializable
{
	public static final int PLACE_TYPE = 1;
	public static final int TRANS_SIMPLE_TYPE = 2;
	public static final int TRANS_OPERATOR_TYPE = 3;
	public static final int SUBP_TYPE = 4;
	public static final int TRIGGER_TYPE = 5;
	public static final int NAME_TYPE = 6;
	public static final int GROUP_TYPE = 7;
	public static final int RESOURCE_TYPE = 8;
	public static final int SIMULATION_TYPE = 9;
	//! Default color to be used for understandability (Color for not highlighted elements)
	protected static final Color defaultUnderstandabilityColor = Color.white;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Object BpelData = null;
	//! Specifies whether the element represented by this model
	//! is highlighted
	//! Highlighted elements are drawn differently by their respective view
	//! This is used for structural analysis, to show that an element is selected
	//! in the analysis dialog
	boolean highlighted = false;
	//! RGHighlighted elements are drawn differently by their view
	//! This is used for the highlighting for the current marking of the Reachabilitygraph
	boolean RGhighlighted = false;
	//! For better understandability a new color from the config colorset will highlight this element.
	private boolean understandabilityColoringActive = false;
    private Color understandabilityColor = defaultUnderstandabilityColor;
    private CreationMap    creationMap        = null;
    private String         id                 = null;
    private ElementContext elementContext     = null;
    private NameModel      nameModel          = null;
    private boolean 	   readOnly           = false;
    //! In order to be able to navigate between the different levels
    //! of element container (e.g. simple transition container of an operator)
    //! we need to store a reference to each owning container
    //! (a model element can be owned by more than one ModelElementContainer)
    //! This reference will be maintained by the ModelElementContainer methods
    //! addElement() and removeOnlyElement() which will call addOwningContainer() and
    //! removeOwningContainer() to do so.
    private Set<ModelElementContainer> owningContainers = new HashSet<ModelElementContainer>();
    //! Stores a reference to the owning container with the lowest hierarchical level
    //! (root container == 0)
    private ModelElementContainer rootOwningContainer = null;
	private Vector<?> m_unknownToolspecific = null;

	public AbstractPetriNetElementModel(CreationMap creationMap, Object userObject) {
		super(null);
		this.elementContext = new ElementContext();
		nameModel = new NameModel(creationMap);
		AttributeMap map = getAttributes();
		GraphConstants.setOpaque(map, false);
		if (creationMap.getReadOnly()) {
			GraphConstants.setBorderColor(map, new Color(125, 125, 125));
		} else {
			GraphConstants.setBorderColor(map, Color.BLACK);
		}
		GraphConstants.setEditable(map, true);
		if (creationMap.getSize() == null) {
			GraphConstants.setSize(map, new Dimension(getDefaultWidth(), getDefaultHeight()));
		} else {
			GraphConstants.setSize(map, new Dimension(creationMap.getSize().getX1(), creationMap.getSize().getX2()));
		}
		GraphConstants.setMoveable(map, true);
		setAttributes(map);

		if (creationMap.getId() != null) {
			setId(creationMap.getId());
			if (creationMap.getPosition() != null) {
				setPosition(new Point(creationMap.getPosition().x, creationMap.getPosition().y));
			}

			if (creationMap.getNamePosition() != null) {
				nameModel.setPosition(new Point(creationMap.getNamePosition().x, creationMap.getNamePosition().y));
			} else if (getPosition() != null) {
				nameModel.setPosition(getPosition().x + ((this.getWidth() - getNameModel().getWidth()) / 2), getPosition().y + getHeight() + 5);
			}
			// ToolSpec
			if (creationMap.getUnknownToolSpec() != null) setUnknownToolSpecs(creationMap.getUnknownToolSpec());
			if (creationMap.getBpeldata() != null) this.BpelData = creationMap.getBpeldata();
		} else {
			LoggerManager.error(Constants.CORE_LOGGER, "It's not allowed to create a Element without id. Please use ModelElementFactory instead.");
		}

	}

	public AbstractPetriNetElementModel(CreationMap creationMap) {
		this(creationMap, creationMap.getName());
	}

    //! Return the owning container with the lowest hierarchical level
    //! @return returns the lowest model element container owning this element
    public ModelElementContainer getRootOwningContainer()
    {
    	return rootOwningContainer;
    }

    //! Determine the hierarchy level of this element
    //! using the root owning container
    //! @return hierarchy level starting at zero for elements owned by the root container
    //! or -1 if the element is not currently owned
	public int getHierarchyLevel() {
    	int result = -1;
    	if (rootOwningContainer!=null)
    	{
    		result = 0;
    		AbstractPetriNetElementModel owningElement = rootOwningContainer.getOwningElement();
    		if (owningElement!=null)
    			result = owningElement.getHierarchyLevel() + 1;
    	}
		return result;
	}

	public void addOwningContainer(ModelElementContainer owningContainer)
    {
    	owningContainers.add(owningContainer);

    	// We may have a new element to own us
    	AbstractPetriNetElementModel newOwningElement = owningContainer.getOwningElement();
    	int nNewHiearchyLevel = ((newOwningElement!=null)?(newOwningElement.getHierarchyLevel() + 1):0);
    	if ((rootOwningContainer==null)||(nNewHiearchyLevel<getHierarchyLevel()))
    		// We climbed in the hierarchy, reflect this by making the new container
    		// our 'root owning container'
    		rootOwningContainer = owningContainer;
    }

	public void removeOwningContainer(ModelElementContainer owningContainer)
    {
    	owningContainers.remove(owningContainer);
    	if (owningContainer==rootOwningContainer)
    	{
    		// Huh, we lost our 'root owning container'
    		// and need to find the closest one in the remaining set
    		Iterator<ModelElementContainer> i = owningContainers.iterator();
    		// Do we still *have* any owners ?
    		if (i.hasNext())
    		{
    			// Start with the first one
				rootOwningContainer = i.next();
				AbstractPetriNetElementModel owningElement = rootOwningContainer.getOwningElement();
    			int newLowest = ((owningElement!=null)?owningElement.getHierarchyLevel()+1:0);
    			while (i.hasNext())
    			{
    				ModelElementContainer candidate = i.next();
    				owningElement = candidate.getOwningElement();
    				int candidateHierarchy = ((owningElement!=null)?owningElement.getHierarchyLevel()+1:0);
    				if (candidateHierarchy<newLowest)
    				{
    					rootOwningContainer = candidate;
    					newLowest = candidateHierarchy;
    				}
    			}
    		}
    		else
    			rootOwningContainer = null;
    	}

    }
    
    public Set<ModelElementContainer> getOwningContainers()
    {
    	return owningContainers;
    }
    
    public Iterator<ModelElementContainer> getOwningContainersIterator()
    {
    	return (owningContainers!=null)?owningContainers.iterator():null;
    }

    public CreationMap getCreationMap()
    {
        if (creationMap == null)
        {
            creationMap = CreationMap.createMap();
        }
        creationMap.setPosition(getPosition());
        creationMap.setId(getId());
        creationMap.setName(getNameValue());
        creationMap.setSize(new IntPair(GraphConstants.getSize(getAttributes())));
        creationMap.setNamePosition(getNameModel().getPosition());
        creationMap.setType(getType());

        creationMap.setBpeldata(this.BpelData);
        
        return creationMap;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public abstract int getType();

    public DefaultPort getPort()
    {
        if (!isLeaf()) return (DefaultPort) getChildAt(0);
        else return null;
    }

    public int getX()
    {
        return (int) GraphConstants.getBounds(getAttributes()).getX();
    }

    public int getY()
    {
        return (int) GraphConstants.getBounds(getAttributes()).getY();
    }

    public Point getPosition()
    {
        Rectangle2D rect = GraphConstants.getBounds(getAttributes());
        if (rect != null)
        {
            return new Point((int) rect.getX(), (int) rect.getY());
        }
        return null;
    }

	public void setPosition(Point2D p) {
		setPosition((int) p.getX(), (int) p.getY());
	}

    public void setPosition(int x, int y)
    {
        AttributeMap map = getAttributes();
        GraphConstants.setBounds(map, new Rectangle(x, y, getWidth(), getHeight()));
        getAttributes().applyMap(map);
    }
    
    /**
     * Set all Objects from a model and the model to the new position 
     * @param x 
     * 			the value to set the model and the surrounded Objects for the x coordinate
     * @param y
     * 			the value to set the model and the surrounded Objects for the y coordinate
     */
    public void setPositionGroup(int x, int y) {
    	setPosition(x, y);
    	nameModel = getNameModel();
    	if(this.getId().startsWith("t"))
    		nameModel.setPosition(x - 10, y + 35);
    	else
    		nameModel.setPosition(x, y + 35);
		if (this instanceof TransitionModel) {
			TransitionModel transitionModel = (TransitionModel) this;
			if (transitionModel.hasTrigger()) {
				TriggerModel triggerModel = transitionModel.getToolSpecific().getTrigger();
				triggerModel.setPosition(x + 10, y - 22);
			}
			if (transitionModel.hasResource()) {
				TransitionResourceModel transitionResourceModel = transitionModel.getToolSpecific().getTransResource();
				transitionResourceModel.setPosition(x - 5, y - 45);
			}
		}
    }

    public abstract String getToolTipText();

    public abstract int getDefaultWidth();

    public abstract int getDefaultHeight();

    public ElementContext getElementContext()
    {
        return elementContext;
    }

    public void setElementContext(ElementContext elementContext)
    {
        this.elementContext = elementContext;
    }

    public String getNameValue()
    {
        return (String) getNameModel().getUserObject();
    }

    public void setNameValue(String name)
    {

        getNameModel().setUserObject(name);
    }

    /**
     * Returns the nameModel.
     * 
     * @return NameModel
     */
    public NameModel getNameModel()
    {
        return nameModel;
    }

    /**
     * Sets the nameModel.
     * 
     * @param nameModel
     *            The nameModel to set
     */
    public void setNameModel(NameModel nameModel)
    {
        this.nameModel = nameModel;
    }
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	//! Specifies whether outgoing arcs may be created for this element
	//! @return returns true if outgoing arcs may be connected, false otherwise
	public boolean getAllowOutgoingConnections()
	{
		return true;
	}
	
	public boolean isHighlighted() {
		return highlighted;
	}
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}
	
	/**
     * Returns true when the element is in the current marking of the Reachabilitygraph
     * 
     * @return boolean
     */
	public boolean isRGHighlighted() {
		return RGhighlighted;
	}
	/**
     * Set the state for the element
     * 
     * @param highlighted
     */
	public void setRGHighlighted(boolean highlighted) {
		this.RGhighlighted = highlighted;
	}

    public void setUnderstandColoringActive(boolean active) {
		this.understandabilityColoringActive= active;
	}	
	
	public boolean isUnderstandabilityColoringActive(){
		return this.understandabilityColoringActive;
	}

	public Color getColor() {
		return this.understandabilityColor;
	}
	
    public void setColor(Color c) {
		// alpha 0-255, the lower the brighter
		int alpha = 180;
    	this.understandabilityColor = new Color(c.getRed(),
				c.getGreen(), c.getBlue(), alpha);
	}
	
	//! Reset any previously set understandability colors to their default value
	public void ResetUnderstandabilityColor() {

		setColor(defaultUnderstandabilityColor);		
	}

	public Vector<?> getUnknownToolSpecs() {
		return m_unknownToolspecific;
	}

	public void setUnknownToolSpecs(Vector<?> unknownToolSpecs) {
		m_unknownToolspecific = unknownToolSpecs;
	}

	public void setSize(Dimension dim) {
		AttributeMap map = getAttributes();
		GraphConstants.setSize(map, dim);
		getAttributes().applyMap(map);
	}

	public void setSize(int width, int height) {
		setSize(new Dimension(width, height));
	}

	public int getHeight() {
		return (int) (GraphConstants.getSize(getAttributes()) == null ? -1
				: GraphConstants.getSize(getAttributes()).getHeight());
	}

	public int getWidth() {
		return (int) (GraphConstants.getSize(getAttributes()) == null ? -1
				: GraphConstants.getSize(getAttributes()).getWidth());
	}

	public final void setBaseActivity(Object arg) {
		this.BpelData = arg;
	}

	public final Object getBpelData() {
		return this.BpelData;
	}

	/** 
	 * Determine whether an element is part of a sub process
	 * 
	 * @return true if element is part of a sub process, false otherwise
	 */
	public boolean isSubprocessElement() {
		for (ModelElementContainer container: getOwningContainers())
			if (container.getOwningElement() instanceof SubProcessModel)
				return true;
		return false;
	}

	/**
	 * Determine whether an element is a sink node (no outgoing connections in any of the owning containers)
	 * @return true if element is a sink node, false otherwise
	 */
	public boolean isSink() {
		boolean result = true;
		// An object can have multiple owning containers
		// Iterate through all of them to get all connections
		Iterator<ModelElementContainer> ownerIterator = getOwningContainers().iterator();
		while (ownerIterator.hasNext()) {
			ModelElementContainer currentContainer = ownerIterator.next();
			Map<String, AbstractPetriNetElementModel> targetElements = currentContainer.getTargetElements(getId());
			if (targetElements.size()>0)
				result = false;
		}
		return result;
	}
	
	/**
	 * Determine whether an element is a root node (no incoming connections)
	 * @return true if element is a root node, false otherwise
	 */
	public boolean isRoot() {
		boolean result = true;
		Iterator<ModelElementContainer> ownerIterator = getOwningContainers().iterator();
		while (ownerIterator.hasNext()) {
			ModelElementContainer currentContainer = ownerIterator.next();
			Map<String, AbstractPetriNetElementModel> sourceElements = currentContainer.getSourceElements(getId());
			if (sourceElements.size()>0)
				result = false;
		}
		return result;
	}

//	/**
//	 * Determine whether an element is a source node (no incoming connections in any of the owning containers)
//	 * @return true if element is a source node, false otherwise
//	 */
//	public boolean isSource() {
//		boolean result = true;
//		// An object can have multiple owning containers
//		// Iterate through all of them to get all connections
//		Iterator<ModelElementContainer> ownerIterator = getOwningContainers().iterator();
//		while (ownerIterator.hasNext()) {
//			ModelElementContainer currentContainer = ownerIterator.next();
//			Map<String, AbstractPetriNetElementModel> sourceElements = currentContainer.getSourceElements(getId());
//			if (sourceElements.size()>0)
//				result = false;
//		}
//		return result;
//	}
	
	/**
	 * While the actual implementation is node-type specific, we have a common definition of what active means for
	 * all node types: Being active means that the node can fire when triggered to do so.
	 * This common notion is used to share some of the view and drawing logic around the notion of being active 
	 * @return true if the node is active, false otherwise
	 */
	public abstract boolean isActivated();
	
}