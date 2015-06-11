package ca.ubc.cs.contextcompare;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ContextRange implements Context {

	// TODO should there be some sort of timestamp recorded here?
	private SortedSet<Context> contexts;

	public ContextRange() {
		contexts = new TreeSet<Context>();
	}

	/*
	 * Add new context to this range
	 */
	public void addContext(Context c) {
		contexts.add(c);
	}

	/*
	 * Return all words contained in this context range. Does not include
	 * frequency.
	 */
	public Set<String> getWords() {
		Set<String> allWords = new HashSet<String>();
		for (Context c : contexts) {
			allWords.addAll(c.getWords());
		}
		return allWords;
	}

	/*
	 * Return all words contained in this context range. Includes frequency.
	 */
	public Map<String, Integer> getWordsWithFreq() {
		Map<String, Integer> wordsWithFreq = new HashMap<String, Integer>();
		for (Context c : contexts) {
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
		for (Context c : contexts) {
			freqWords.addAll(c.getFrequentWords(n));
		}
		return freqWords;
	}

	/*
	 * returns all contexts that fall within the given range (inclusive). This
	 * method could potentially double up particular contexts if some are stored
	 * as ContextPoints and others are ContextRanges that cover the same points.
	 */
	public Set<Context> getContextsInRange(long timeFrom, long timeTo) {
		Set<Context> contextsInRange = new TreeSet<Context>();
		for (Context c : contextsInRange) {
			if (c.isInRange(timeFrom, timeTo)) {
				contextsInRange.add(c);
			}
		}
		return contextsInRange;
	}

	@Override
	public boolean isInRange(long timeFrom, long timeTo) {
		boolean inRange = true;
		for (Context c : contexts) {
			if (!c.isInRange(timeFrom, timeTo)) {
				inRange = false;
			}
		}
		return inRange;
	}

	public Set<Context> getContexts() {
		return contexts;
	}

}
