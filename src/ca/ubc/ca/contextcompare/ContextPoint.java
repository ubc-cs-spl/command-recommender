package ca.ubc.ca.contextcompare;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContextPoint extends Context implements Comparable<ContextPoint> {

	protected String name;
	protected long timestamp;
	private Map<String, Integer> words;

	private ScreenImage imgOCR;

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
	 * Omits punctuation, white space, and the empty string, but includes
	 * digits.
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
	 * Helper method for parseImgText
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

	// ***Getters***

	public Set<String> getWords() {
		return words.keySet();
	}

	public Map<String, Integer> getWordsWithFreq() {
		return words;
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
}
