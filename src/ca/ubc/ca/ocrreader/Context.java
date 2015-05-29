package ca.ubc.ca.ocrreader;

import java.util.HashMap;
import java.util.Map;

public class Context {

	private Map<String, Integer> words;

	// private long timestamp;

	public Context(String text) {
		words = new HashMap<String, Integer>();
		parseImgText(text);
	}

	private void parseImgText(String text) {
		String currentWord = "";
		
		for (int i = 0; i < text.length(); i++) {
			Character ch = text.charAt(i);
			if (Character.isLetterOrDigit(ch)) {
				currentWord += ch;
			}
			if (!Character.isLetterOrDigit(ch) || i >= text.length() - 1) {
				addWord(currentWord);
			}
		}
	}

	private void addWord(String word) {
		if (word.equals("")) {
			return;
		}

		int freq;
		if (words.containsKey(word)) {
			freq = words.get(word) + 1;
		} else {
			freq = 1;
		}
		words.put(word, freq);
	}

}
