package org.woped.soundness.datamodel;

import java.util.*;

public abstract class AbstractNode {
	private Set<AbstractNode> preNodes = new HashSet<AbstractNode>();
	private Set<AbstractNode> postNodes = new HashSet<AbstractNode>();
		
	public AbstractNode addPreNode(AbstractNode preNode){
		preNodes.add(preNode);
		return preNode;
	}	
	
	public AbstractNode addPostNode(AbstractNode postNode){
		postNodes.add(postNode);
		return postNode;
	}
	
}
