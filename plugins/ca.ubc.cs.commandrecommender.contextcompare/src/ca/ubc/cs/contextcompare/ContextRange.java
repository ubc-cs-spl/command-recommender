package ca.ubc.cs.contextcompare;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContextRange implements Context {

	// TODO should there be some sort of timestamp recorded here?
	private Set<ContextPoint> contexts;

	public ContextRange() {
		contexts = new HashSet<ContextPoint>();
	}

	/*
	 * Add new context to this range
	 */
	public void addContext(ContextPoint c) {
		contexts.add(c);
	}

	/*
	 * Return all words contained in this context range. Does not include
	 * frequency.
	 */
	public Set<String> getWords() {
		Set<String> allWords = new HashSet<String>();
		for (ContextPoint c : contexts) {
			allWords.addAll(c.getWords());
		}
		return allWords;
	}

	/*
	 * Return all words contained in this context range. Includes frequency.
	 */
	public Map<String, Integer> getWordsWithFreq() {
		Map<String, Integer> wordsWithFreq = new HashMap<String, Integer>();
		for (ContextPoint c : contexts) {
			Map<String, Integer> words = c.getWordsWithFreq();

			for (Map.Entry<String, Integer> wordFreq : words.entrySet()) {
				String word = wordFreq.getKey();
				Integer freq = wordFreq.getValue();

				// if word is already recorded, update its frequency
				if (wordsWithFreq.containsKey(word)) {
					freq = freq + wordsWithFreq.get(word);
				}
				// add word and frequency to the map to be returned
				wordsWithFreq.put(word, freq);
			}
		}
		return wordsWithFreq;
	}

	/*
	 * Return all words in this range that appear n or more times.
	 */
	public Set<String> getFrequentWords(int n) {
		Set<String> freqWords = new HashSet<String>();
		for (ContextPoint c : contexts) {
			freqWords.addAll(c.getFrequentWords(n));
		}
		return freqWords;
	}

	/*
	 * returns all contexts that fall within the given range (inclusive).
	 */
	public Set<ContextPoint> getContextsInRange(long timeFrom, long timeTo) {
		Set<ContextPoint> contextsInRange = new HashSet<ContextPoint>();
		for (ContextPoint c : contexts) {
			if (c.isInRange(timeFrom, timeTo)) {
				contextsInRange.add(c);
			}
		}
		return contextsInRange;
	}

	// *** Getters & Setters ***
	public Set<ContextPoint> getAllContexts() {
		return contexts;
	}

}
