package org.woped.core.model.uml;

import java.awt.Dimension;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.woped.core.model.CreationMap;

public class OperatorModel extends AbstractUMLElementModel {
	public static final int WIDTH = 40;

	public static final int HEIGHT = 40;

	private int operatorType = -1;

	public static final int AND_TYPE = 110;

	public static final int XOR_TYPE = 111;

	public OperatorModel(CreationMap map) {
		super(map);
		if (map.getOperatorType() != -1) {
			this.operatorType = map.getOperatorType();
		}
		AttributeMap amap = getAttributes();
		if (map.getSize() == null) {
			GraphConstants.setSize(amap, new Dimension(getDefaultWidth(), getDefaultHeight()));
		} else {
			GraphConstants.setSize(amap, new Dimension(map.getSize().getX1(), map.getSize().getX2()));
		}
		changeAttributes(amap);
	}

	public int getType() {
		return AbstractUMLElementModel.OPERATOR_TYPE;
	}

	public CreationMap getCreationMap() {
		CreationMap creationMap = super.getCreationMap();
		creationMap.setType(AbstractUMLElementModel.OPERATOR_TYPE);
		creationMap.setOperatorType(getOperatorType());
		return creationMap;
	}

	public int getDefaultHeight() {
		return HEIGHT;
	}

	public int getDefaultWidth() {
		return WIDTH;
	}

	public String getToolTipText() {
		return "Operator " + getId();
	}

	public int getOperatorType() {
		return operatorType;
	}

}
