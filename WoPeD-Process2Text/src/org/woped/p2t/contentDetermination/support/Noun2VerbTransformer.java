package org.woped.p2t.contentDetermination.support;

import net.didion.jwnl.data.*;
import net.didion.jwnl.dictionary.Dictionary;

import java.util.*;

/**
 * @author Sergey Smirnov
 */
public class Noun2VerbTransformer {
    private static final List<String> suffixes;
    private static final Map<String, String> cache;

    static {
        suffixes = new ArrayList<>();
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

        cache = new HashMap<>();
    }

    public static String toVerb(String noun, Dictionary dictionary) throws Exception {
        if (cache.containsKey(noun))
            return cache.get(noun);

        Set<String> candidateVerbs = new HashSet<>();
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
            int distance = Distance.getLD(stemmedNoun, candidate);
            if (!stemmedNoun.substring(0, 1).equals(candidate.substring(0, 1))) {
                distance = distance + 10;
            }
            if (candidate.startsWith(stemmedNoun)) {
                distance = 0;
            }
            if (distance < minDist) {
                minDist = distance;
                verb = candidate;
            }
        }
        cache.put(noun, verb);
        if (minDist < 8 && stemmedNoun.substring(0, 1).equals(verb.substring(0, 1))) {
            return verb;
        } else {
            return "";
        }
    }

    private static String stemNoun(String noun) {
        for (String suffix : suffixes)
            if (noun.endsWith(suffix))
                return noun.substring(0, noun.length() - suffix.length());
        return noun;
    }
}