package org.woped.core.model.petrinet;

import java.awt.Dimension;
import java.awt.Point;

//! Stores the layout of the editor window:
//! Dimension, Position, state of the tree view etc. 
//! This is part of the model because namely the sub-process model
//! remembers that exact information for the editor
//! The editor is then instantiated based on the content of this object
public class EditorLayoutInfo {

	public EditorLayoutInfo() {
		super();
	}
	private Dimension m_savedSize = null;
	private Point 	  m_savedLocation = null;
	private int   	  m_treeViewWidth = 0;
	
	/**
	 * Returns the saved Size of the Editor.
	 * 
	 * @return Dimension
	 */
	public Dimension getSavedSize()
	{
		return m_savedSize;
	}

	/**
	 * Sets the saved Size of the Editor.
	 * 
	 * @param savedSize
	 *            The savedSize to set
	 */
	public void setSavedSize(Dimension savedSize)
	{
		this.m_savedSize = savedSize;
	}
	

	/**
	 * Returns the location of the Editor. Only used in a MDI.
	 * 
	 * @return Returns the savedLocation.
	 */
	public Point getSavedLocation()
	{
		return this.m_savedLocation;
	}

	/**
	 * Stes the location of the Editor. Only used in a MDI.
	 * 
	 * @param location
	 */
	public void setSavedLocation(Point location)
	{
		this.m_savedLocation = location;
	}

	public int getTreeViewWidth() {
		return m_treeViewWidth;
	}

	public void setTreeViewWidth(int viewWidth) {
		m_treeViewWidth = viewWidth;
	}
	
}
