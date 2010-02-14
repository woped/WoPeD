package org.woped.core.controller;

import java.awt.geom.Point2D;
import java.io.File;

import javax.swing.JComponent;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphCell;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.AbstractModelProcessor;
import org.woped.core.model.CreationMap;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.SubProcessModel;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * 
 * An Editor has to implement the IEditor interface.
 */

public interface IEditor extends IViewController
{
    public static int TYPE = 1;

    /**
     * Returns the Graph for the Editor
     * 
     * @return
     */
    public AbstractGraph getGraph();

	public void openTokenGameSubProcess(SubProcessModel subProcess);
    
    
    /**
     * Creates the Element in the Graph and stores it in the used ModelProcessor
     * 
     * @param creationMap
     * @return
     */
    public GraphCell create(CreationMap map);
    /**
     * Creates the Elements in the Graph and stores them in the used ModelProcessor
     * 
     * @param creationMap
     * @return
     */
    public GraphCell[] createAll(CreationMap[] maps);

    /**
     * Returns the used ModelProcessor
     * 
     * @return
     */
    public AbstractModelProcessor getModelProcessor();

    /**
     * Sets the ModelProcessor to use.
     * 
     * @param modelProcessor
     */
    public void setModelProcessor(AbstractModelProcessor modelProcessor);

    /**
     * Remembers the last mouse postition
     * 
     * @param position
     */
    public void setLastMousePosition(Point2D position);

    /**
     * Returns the last mouse position the AbstractMarqueeHandler has noticed.
     * It is necessary register the AbstractmarqueeHandler at the used Graph
     * when instanciated.
     * 
     * @return position
     */
    public Point2D getLastMousePosition();

    /**
     * Adds an routing point to the arc.
     *  
     */
    public void addPointToSelectedArc();

    /**
     * 
     *  
     */
    public void removeSelectedPoint();

    /**
     * Returns the drawing mode. If the net is in drawing mode, clicking the
     * left mouse button will draw the Element with the set creation type.
     * 
     * @see getCreateElementType
     * @return drawing mode
     */
    public boolean isDrawingMode();

    /**
     * Returns if the reachability graph is enabled for this editor.
     * 
     * @see getCreateElementType
     * @return drawing mode
     */
    public boolean isReachabilityEnabled();
    
    /**
     * Sets the drawing mode. If the net is in drawing mode, clicking the left
     * mouse button will draw the Element with the set creation type.
     * 
     * @see getCreateElementType
     * @param flag
     */
    public void setDrawingMode(boolean flag);
    
    /**
     * Sets if the reachability graph for this editor is enabled
     * 
     * @see getCreateElementType
     * @return drawing mode
     */
    public void setReachabilityEnabled(boolean flag);
    
    /**
     * Sets if the TokenGame for this editor is playing
     */
    public void setTokenGameEnabled(boolean state);
    
    
    /**
     * Sets if Understandability Coloring is active
     */
    public void setUnderstandabilityColoringEnabled(boolean active);
    
    public boolean isUnderstandabilityColoringEnabled();
    
    /* Sets if the TokenGame for this editor is playing
     */
    public void toggleTokenGame();
    
    /**
     * Get TokenGameEnabled for this editor if TokenGame is stoped
     * @return m_tokenGameEnabled
     */
    public boolean isTokenGameEnabled();

    /**
     * Returns the type of the element, which will be created in drawing mode.
     * 
     * @see PetriNetModelElement for element types
     * @return int
     */
    public int getCreateElementType();

    /**
     * Sets the type of the element, which will be created in drawing mode.
     * 
     * @see PetriNetModelElement for element types
     * @param createElementType
     *  
     */
    public void setCreateElementType(int createElementType);

    /**
     * Returns the Containter in witch the Editor is running. The Editor doesn't
     * need a Container if it is itself the Component.
     * 
     * @return
     */
    public JComponent getContainer();

    /**
     * Sets (ONLY THE REFERENCE to) the Container in witch the Editor is
     * running. The Editor doesn't need a Container if it is itself the
     * Component.
     * 
     * @param container
     */
    public void setContainer(JComponent container);

    public void deleteCells(Object toDelete[]);

    public void deleteCell(DefaultGraphCell toDelete);

    public void updateNet();

    public void setName(String name);

    public String getName();

	public boolean isSubprocessEditor();
 
	/**
	 * Returns the saved flag for the editor.
	 * 
	 * @return boolean
	 */
	public boolean isSaved();

	/**
	 * Sets the saved flag for the editor. true when net was saved, or just
	 * loaded.
	 * 
	 * @param savedFlag
	 *            The savedFlag to set
	 */
	public void setSaved(boolean savedFlag);
	
	public AbstractElementModel getSubprocessInput();

	public void setSubprocessInput(AbstractElementModel p_subprocessInput);

	public AbstractElementModel getSubprocessOutput();

	public void setSubprocessOutput(AbstractElementModel p_subprocessOutput);
	
	public void closeEditor();
	
	/**
	 * Disable all controls for the editor
	 * @param readonly
	 * @author <a href="mailto:b.joerger@gmx.de">Benjamin Joerger</a>
	 * @since 02.01.2009
	 */
	public void setReadOnly(boolean readonly);
	
	public void showAnalysisBar(File temporaryFile, IEditor editor, AbstractApplicationMediator mediator);
	
	public void hideAnalysisBar();
	
	public boolean getAnalysisBarVisible();
	
	public void rotateLayout();
	
	public void rotateTransLeft(Object cell);
	
	public void rotateTransRight(Object cell);
	
	public boolean isRotateSelected();

	public void setRotateSelected(boolean active);
	
	/**
	 * Checks if woflan.dll should be used for qualanalysis <br />
	 * For non-windows-systems it always should return false
	 * 
	 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
	 * 
	 * @return true if woflan.dll should be used for qualanalysis
	 */
	public Boolean isUseWoflanDLL();
}
