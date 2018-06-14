package worldModel;

import java.util.Collection;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;

/**
 * Simple Data structure, based on the Stanford sentence.
 * Adds a unique ID to enable tracing
 *
 */
public class T2PSentence extends Sentence<Word> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4145260396296512358L;
	private int f_id = createID();
	private Tree f_tree; //syntax Tree
	private GrammaticalStructure f_gramStruc; //dependencies
	private int f_offset;
	
	/**
	 * Constructs an empty sentence.
	 */
	public T2PSentence() {
		super();
	}

	/**
	 * Constructs an empty list with the specified initial capacity.
	 *
	 * @param initialCapacity The initial sentence allocation size
	 */
	public T2PSentence(int initialCapacity) {
		super(initialCapacity);
	}


	/**
	 * Constructs a sentence from the input Collection.
	 *
	 * @param w A Collection (interpreted as ordered) to make the sentence
	 *          out of.
	 */
	public T2PSentence(Collection<Word> w) {
		super(w);
	}

	
	
	public int getID(){
		return f_id;
	}
	
	/**
	 * gets the length of the sentence (in characters)
	 * @return
	 */
	public int getCharLength() {
		return getEndPosition() - getBeginPosition();
				
	}
	
	/**
	 * return the position of the first letter of the first word in the original text.
	 * @return
	 */
	public int getBeginPosition() {
		if(size() > 0) {
			return this.get(0).beginPosition()-f_offset;
		}
		return -1;	
	}
	
	/**
	 * returns the position of the last letter of the last word in the original text.
	 * @return
	 */
	public int getEndPosition() {
		if(size() > 0) {
			return this.get(this.size()-1).endPosition()-f_offset;
		}
		return -1;		
	}
	
	public String toStringFormated() {
		return PTBTokenizer.ptb2Text(toString(true));
	}
	
	/**
	 * returns the Stanford dependency tree
	 * @return
	 */
	public GrammaticalStructure getGrammaticalStructure() {
		return f_gramStruc;
	}
	
	/**
	 * returns the Stanford syntax tree
	 * @return
	 */
	public Tree getTree() {
		return f_tree;
	}
	

	/**
	 * sets the grammatical structure containing the stanford 
	 * dependencies
	 * @param _gs
	 */
	public void setGrammaticalStructure(GrammaticalStructure gs) {
		gs.toString();
		f_gramStruc = gs;
	}	
	
	
	/**
	 * Here the stanford syntax tree is set
	 * @param _parse
	 */
	public void setTree(Tree tree) {
		f_tree = tree;
	}
	
	/**
	 * Because of comment lines, the actual begin and end position of this sentence in a JtextField
	 * can differ
	 * @param sentenceOffset
	 */
	public void setCommentOffset(int sentenceOffset) {
		f_offset = sentenceOffset;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	
	
	//---------- a static id creation
	private static int f_lastID = 0;
	
	private static int createID(){
		return f_lastID++;
	}
	
	public static void resetIDs() {
		f_lastID = 0;
	}

	

	
}
