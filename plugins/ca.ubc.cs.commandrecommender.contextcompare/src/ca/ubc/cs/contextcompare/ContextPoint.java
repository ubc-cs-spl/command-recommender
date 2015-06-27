package ca.ubc.cs.contextcompare;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContextPoint implements IContext {

	protected long timestamp;
	private Map<String, Integer> words;
	private ContextFileReader contextReader;

	/*
	 * Convenience constructor for testing
	 */
	public ContextPoint(long timestamp) {
		this.timestamp = timestamp;
		words = new HashMap<String, Integer>();
		contextReader = null;
	}

	/*
	 * Create context from a file
	 */
	public ContextPoint(File file) {
		timestamp = file.lastModified();
		words = new HashMap<String, Integer>();

		String fileName = file.getName();
		String text = "";
		try {
			if (fileName.endsWith(".png")) {
				contextReader = new ScreenCaptureReader(file);
			} else if (fileName.endsWith(".txt")) {
				contextReader = new ActiveEditorReader(file);
			} else {
				// TODO what if the file is neither of these formats?
			}
			text = contextReader.processContextFile();
			parseText(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
