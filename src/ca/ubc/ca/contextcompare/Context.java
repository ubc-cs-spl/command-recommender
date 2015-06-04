package ca.ubc.ca.contextcompare;

import java.util.Map;
import java.util.Set;

public abstract class Context {

	public Context() {}

	public abstract Set<String> getWords();

	public abstract Map<String, Integer> getWordsWithFreq();

	public abstract Set<String> getFrequentWords(int n);

}
