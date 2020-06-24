package org.woped.core.controller;

import java.awt.geom.Point2D;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphCell;
import org.woped.core.gui.ITokenGameController;
import org.woped.core.model.CreationMap;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.SubProcessModel;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * 
 *         An Editor has to implement the IEditor interface.
 */

public interface IEditor extends IViewController {
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
	public GraphCell create(CreationMap map, boolean doNotEdit);

	/**
	 * Creates the Elements in the Graph and stores them in the used
	 * ModelProcessor
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
	public PetriNetModelProcessor getModelProcessor();

	/**
	 * Sets the ModelProcessor to use.
	 * 
	 * @param modelProcessor
	 */
	public void setModelProcessor(PetriNetModelProcessor modelProcessor);

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
	 * Sets if Understandability Coloring is active
	 */
	public void setUnderstandabilityColoringEnabled(boolean active);

	public boolean isUnderstandabilityColoringEnabled();

	/*
	 * Sets if the TokenGame for this editor is playing
	 */
	public void toggleTokenGame();

	/**
	 * Returns the type of the element, which will be created in drawing mode.
	 * 
	 * @see AbstractPetriNetElementModel for element types
	 * @return int
	 */
	public int getCreateElementType();

	/**
	 * Sets the type of the element, which will be created in drawing mode.
	 * 
	 * @see AbstractPetriNetElementModel for element types
	 * @param createElementType
	 * 
	 */
	public void setCreateElementType(int createElementType);

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

	public void closeEditor();
	
	public void hideAnalysisBar();
	
	public void hideP2TBar();
	
	public void hideMetricsBar();

	/**
	 * Disable all controls for the editor
	 * 
	 * @param readonly
	 * @author <a href="mailto:b.joerger@gmx.de">Benjamin Joerger</a>
	 * @since 02.01.2009
	 */
	public void setReadOnly(boolean readonly);

	public void rotateLayout();

	public void rotateTransLeft(Object cell);

	public void rotateTransRight(Object cell);

	public boolean isRotateSelected();

	public void setRotateSelected(boolean active);

	public boolean isShowingTStar();

	public void setTStarEnabled(boolean tStarEnabled);
	
	public boolean isClipboardEmpty();

	public boolean isTokenGameEnabled();

	public void checkMainSplitPaneDivider();

	public void setDrawMode(int type, boolean b);
	
	public ITokenGameController getTokenGameController();
	
	public void repaint();

	public abstract void terminateTokenGameSession();

	public abstract void disableTokenGame();

	public abstract void enableTokenGame();

	public void setPathname(String absolutePath);

	public String getPathname();
	
	public AbstractApplicationMediator getMediator();
}
