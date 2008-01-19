package org.woped.qualanalysis.reachabilitygraph.data;

import java.awt.Dimension;

import org.jgraph.JGraph;
import org.woped.core.controller.IEditor;

public class ReachabilityGraphModel {

	public static final int HIERARCHIC = 0;
	public static final int CIRCLE = 1;
	
	private IEditor editor = null;
	
	public ReachabilityGraphModel(IEditor editor){
		this.editor = editor;
	}
	
	public JGraph getGraph(int type, Dimension dim){
		switch(type){
			case HIERARCHIC:	return ReachabilityLayoutHierarchic.layoutGraph(editor);
			case CIRCLE:		return ReachabilityGraphCircle.layoutGraph(editor, dim);
			default: 			return null;
		}
	}
	
}
