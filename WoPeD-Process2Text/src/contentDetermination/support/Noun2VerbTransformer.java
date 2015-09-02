package contentDetermination.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;

/**
 * 
 * @author Sergey Smirnov
 * 
 */
public class Noun2VerbTransformer {


	private static List<String> suffixes;

	private static Map<String, String> cache;

	static {
		suffixes = new ArrayList<String>();
		suffixes.add("able");
		suffixes.add("al");
		suffixes.add("alism");
		suffixes.add("ance");
		suffixes.add("ant");
		suffixes.add("ate");
		suffixes.add("ation");
		suffixes.add("ator");
		suffixes.add("ement");
		suffixes.add("ence");
		suffixes.add("ent");
		suffixes.add("er");
		suffixes.add("fulness");
		suffixes.add("ous");
		suffixes.add("ousness");
		suffixes.add("ible");
		suffixes.add("icate");
		suffixes.add("ing");
		suffixes.add("ion");
		suffixes.add("ive");
		suffixes.add("iveness");
		suffixes.add("ism");
		suffixes.add("iti");
		suffixes.add("ization");
		suffixes.add("ize");
		suffixes.add("izer");
		suffixes.add("ment");
		suffixes.add("ness");

		cache = new HashMap<String, String>();
	}

	public static String toVerb(String noun, Dictionary dictionary) throws Exception {
		if (cache.containsKey(noun))
			return cache.get(noun);

		Set<String> candidateVerbs = new HashSet<String>();
		IndexWord iw = dictionary.getIndexWord(POS.NOUN, noun);

		if (iw != null)
			for (Synset synset : iw.getSenses()) {
				Pointer[] pointers = synset
						.getPointers(PointerType.NOMINALIZATION);
				for (Pointer pointer : pointers) {
					Synset derived = pointer.getTargetSynset();
					for (Word word : derived.getWords())
						if (word.getPOS() == POS.VERB)
							candidateVerbs.add(word.getLemma());
				}
			}
		int minDist = Integer.MAX_VALUE;
		String verb = noun;
		String stemmedNoun = stemNoun(noun);
		for (String candidate : candidateVerbs) {
			int distance = Distance.getLD(stemmedNoun,candidate);
			if (stemmedNoun.substring(0,1).equals(candidate.substring(0,1)) == false) {
				distance = distance + 10;
			}
			if (candidate.startsWith(stemmedNoun)) {
				distance = 0;
			}
//			System.out.println(stemmedNoun  + " - " + candidate + " - " + distance);
			if (distance < minDist) {
				minDist = distance;
				verb = candidate;
			}
		}
		cache.put(noun, verb);
		if (minDist < 8 && stemmedNoun.substring(0,1).equals(verb.substring(0,1)) == true) {
			return verb;
		} else {
			return "";
		}
	}

	public static String stemNoun(String noun) {
		for (String suffix : suffixes)
			if (noun.endsWith(suffix))
				return noun.substring(0, noun.length() - suffix.length());
		return noun;
	}

}
