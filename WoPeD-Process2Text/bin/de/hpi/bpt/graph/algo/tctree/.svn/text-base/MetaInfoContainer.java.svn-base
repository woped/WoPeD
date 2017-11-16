package de.hpi.bpt.graph.algo.tctree;

import java.util.HashMap;

/**
 * This container stores additional meta information. 
 * The elements of {@link MetaInfo} are used as keys.
 * 
 * @author Christian Wiggert
 *
 */
public class MetaInfoContainer {
	
	private HashMap<MetaInfo, Object> map;
	
	public MetaInfoContainer() {
		map = new HashMap<MetaInfo, Object>();
	}
	
	public Object getMetaInfo(MetaInfo name) {
		if (map.containsKey(name))
			return map.get(name);
		return null;
	}
	
	public void setMetaInfo(MetaInfo name, Object content) {
		map.put(name, content);
	}
}
