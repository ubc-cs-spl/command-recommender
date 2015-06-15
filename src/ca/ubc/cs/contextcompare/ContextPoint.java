package ca.ubc.cs.contextcompare;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContextPoint implements Context {

	protected long timestamp;
	private Map<String, Integer> words;
	private ScreenImageReader imgOCR;

	/*
	 * Convenience constructor for testing
	 */
	public ContextPoint(long timestamp) {
		this.timestamp = timestamp;
		words = new HashMap<String, Integer>();
		imgOCR = null;
	}

	/*
	 * Create context from a screencapture image
	 */
	public ContextPoint(File imgFile) {
		timestamp = imgFile.lastModified();
		words = new HashMap<String, Integer>();

		// get text from the image file
		imgOCR = new ScreenImageReader(imgFile);
		String text = imgOCR.doOCR();
		parseText(text);
	}

	/*
	 * Create context from string output from editor contents
	 */

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

	public boolean isInRange(long timeFrom, long timeTo) {
		return (timeFrom <= timestamp && timestamp <= timeTo);
	}

	/*
	 * Convenience method for testing
	 */
	public void setWords(String text) {
		parseText(text);
	}

	// ***Getters & Setters***

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Set<String> getWords() {
		return words.keySet();
	}

	public Map<String, Integer> getWordsWithFreq() {
		return words;
	}

	public long getTimestamp() {
		return timestamp;
	}

	// *** hashCode and equals ***
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContextPoint other = (ContextPoint) obj;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}

}
