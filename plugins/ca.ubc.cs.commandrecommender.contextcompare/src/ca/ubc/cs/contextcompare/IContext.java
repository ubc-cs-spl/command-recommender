package ca.ubc.cs.contextcompare;

import java.util.Map;
import java.util.Set;

public interface IContext {

	// get all words in the context or context range
	public abstract Set<String> getWords();

	// get words mapped with their frequency
	public abstract Map<String, Integer> getWordsWithFreq();

	// get all words that appear n or more times
	public abstract Set<String> getFrequentWords(int n);

}
