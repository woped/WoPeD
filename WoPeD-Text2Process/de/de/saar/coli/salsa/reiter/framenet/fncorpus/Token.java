package de.saar.coli.salsa.reiter.framenet.fncorpus;

import de.saar.coli.salsa.reiter.framenet.AbstractToken;
import de.uniheidelberg.cl.reiter.util.Range;

/**
 * This class represents tokens in the original FrameNet corpus.
 * 
 * @author reiter
 * @since 0.4
 * 
 */
public class Token extends AbstractToken {

	Range range;

	Sentence sentence;

	protected Token(Sentence sentence, Range range) {
		this.sentence = sentence;
		this.range = range;
	}

	@Override
	public Range getRange() {
		return range;
	}

	@Override
	public String toString() {
		return sentence.getText().substring(range.getElement1(),
				range.getElement2());
	}

	@Override
	public Sentence getSentence() {
		return sentence;
	}

}
