package ca.ubc.cs.contextcompare;

import java.util.Map;
import java.util.Set;

public interface Context {

	public abstract Set<String> getWords();

	public abstract Map<String, Integer> getWordsWithFreq();

	public abstract Set<String> getFrequentWords(int n);

}
