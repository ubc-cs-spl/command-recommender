package ca.ubc.ca.contextcompare;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ContextRange extends Context {

	private SortedSet<Context> children;

	public ContextRange() {
		children = new TreeSet<Context>();
	}

	/*
	 * Add new context to this range
	 */
	public void addContext(Context c) {
		children.add(c);
	}

	/*
	 * Return all words contained in this context range. Does not include
	 * frequency.
	 */
	public Set<String> getWords() {
		Set<String> allWords = new HashSet<String>();
		for (Context c : children) {
			allWords.addAll(c.getWords());
		}
		return allWords;
	}

	/*
	 * Return all words contained in this context range. Includes frequency.
	 */
	public Map<String, Integer> getWordsWithFreq() {
		Map<String, Integer> wordsWithFreq = new HashMap<String, Integer>();
		for (Context c : children) {
			Map<String, Integer> words = c.getWordsWithFreq();

			for (Map.Entry<String, Integer> wordFreq : words.entrySet()) {
				String word = wordFreq.getKey();
				Integer freq = wordFreq.getValue();

				// if word is already recorded, update its frequency
				if (wordsWithFreq.containsKey(word)) {
					freq = freq + wordsWithFreq.get(word);
				}

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
		for (Context c : children) {
			freqWords.addAll(c.getFrequentWords(n));
		}
		return freqWords;
	}

	public Set<ContextPoint> contextsInRange(long timeFrom, long timeTo) {
		// TODO complete this method
		return null;
	}

}
