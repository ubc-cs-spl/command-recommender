package ca.ubc.cs.contextcompare;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContextPoint implements Context, Comparable<ContextPoint> {

	// TODO do you need a name field? For what purpose?
	private String name;
	protected long timestamp;
	private Map<String, Integer> words;
	private ScreenImage imgOCR;

	/*
	 * Convenience constructor for testing
	 */
	public ContextPoint() {
		name = "testWords";
		timestamp = 0;
		words = new HashMap<String, Integer>();
		imgOCR = null;
	}

	public ContextPoint(File imgFile) {
		name = imgFile.getName();
		timestamp = imgFile.lastModified();
		words = new HashMap<String, Integer>();

		// get text from the image file
		imgOCR = new ScreenImage(imgFile);
		String text = imgOCR.doOCR();
		parseText(text);
	}

	/*
	 * Given a string, store all words and their frequency in the words field.
	 * Omits punctuation, white space, and the empty string; includes digits.
	 */
	private void parseText(String text) {
		String word = "";
		for (int i = 0; i < text.length(); i++) {
			Character ch = text.charAt(i);
			if (Character.isLetterOrDigit(ch)) {
				word += ch;
			}
			if (!Character.isLetterOrDigit(ch) || i >= (text.length() - 1)) {
				// skip empty string, otherwise add the completed word to words
				if (!word.equals("")) {
					addWord(word);
					word = "";
				}
			}
		}
	}

	/*
	 * Helper method for parseImgText, adds word and updates frequency
	 */
	private void addWord(String word) {
		int freq;
		if (words.containsKey(word)) {
			freq = words.get(word) + 1;
		} else {
			freq = 1;
		}
		words.put(word, freq);
	}

	/*
	 * Get all words in the context that appear n or more times
	 */
	public Set<String> getFrequentWords(int n) {
		Set<String> freqWords = new HashSet<String>();
		for (Map.Entry<String, Integer> wordPair : words.entrySet()) {
			String word = wordPair.getKey();
			Integer freq = wordPair.getValue();
			if (freq >= n) {
				freqWords.add(word);
			}
		}
		return freqWords;
	}

	// ***Getters & Setters***

	public Set<String> getWords() {
		return words.keySet();
	}

	public Map<String, Integer> getWordsWithFreq() {
		return words;
	}

	public long getTimestamp() {
		return timestamp;
	}

	/*
	 * Convenience method for testing
	 */
	public void setWords(String text) {
		parseText(text);
	}

	/*
	 * compareTo method to implement Comparable. ContextPoints are sorted by
	 * timestamp.
	 */
	@Override
	public int compareTo(ContextPoint other) {
		if (timestamp > other.timestamp) {
			return 1;
		} else if (timestamp == other.timestamp) {
			return 0;
		} else {
			return -1;
		}
	}

	public boolean isInRange(long timeFrom, long timeTo) {
		return (timeFrom <= timestamp && timestamp <= timeTo);
	}
}
