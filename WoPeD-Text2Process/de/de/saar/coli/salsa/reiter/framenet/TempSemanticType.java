package de.saar.coli.salsa.reiter.framenet;

/**
 * This class represents a temporary semantic type which is not yet loaded
 * from the FrameNet database. This class should not be used externally.
 * @author Nils Reiter
 * @since 0.4
 */
public class TempSemanticType extends SemanticType {

	
	private final static long serialVersionUID = 1l;

	/**
	 * Loads a SemanticType from name and/or id. This constructor can be used 
	 * to load a semantic type preliminary, when it is mentioned in some data
	 * file but has not been loaded yet. Both name and id are overwritten by 
	 * {@link SemanticType#supplyNode(Element)}. 
	 * 
	 * @param frameNet The FrameNet object
	 * @param name The name of the semantic type
	 * @param id The ID of the semantic type
	 */
	protected TempSemanticType(FrameNet frameNet, String name, String id) {
		super();
		this.frameNet = frameNet;
		this.id = id;
		this.name = name;
	}

}
