package ca.ubc.ca.ocrreader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Context {

	private Map<String, Integer> words;

	// private long timestamp;

	public Context(String text) {
		words = new HashMap<String, Integer>();
		parseImgText(text);
	}

	/*
	 * Given a string, store all words and their frequency in the words field.
	 * Omits punctuation, spaces, and the empty string, but includes digits.
	 */
	private void parseImgText(String text) {
		String word = "";
		
		for (int i = 0; i < text.length(); i++) {
			Character ch = text.charAt(i);
			if (Character.isLetterOrDigit(ch)) {
				word += ch;
			}
			if (!Character.isLetterOrDigit(ch) || i >= text.length() - 1) {
				// skip empty string, otherwise add the word to words map
				if (!word.equals("")) {
					addWord(word);
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
	 * Given another context to compare to, return a set of all words common to
	 * both contexts
	 */
	public Set<String> getSharedWords(Context other) {
		Set<String> sharedWords = new HashSet<String>();
		Set<String> otherWords = other.getWords().keySet();
		for (String word : otherWords) {
			if (words.containsKey(word)) {
				sharedWords.add(word);
			}
		}
		return sharedWords;
	}

	/*
	 * Return a set of all words that appear only in this context (i.e., are not
	 * present in the other, given context)
	 */
	public Set<String> getUniqueWords(Context other) {
		Set<String> uniqueWords = words.keySet();
		Set<String> otherWords = other.getWords().keySet();
		for (String word : otherWords) {
			if (uniqueWords.contains(word)) {
				uniqueWords.remove(word);
			}
		}
		return uniqueWords;
	}

	private Map<String, Integer> getWords() {
		return words;
	}
}
