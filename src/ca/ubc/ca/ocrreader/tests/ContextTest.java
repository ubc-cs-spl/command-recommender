package ca.ubc.ca.ocrreader.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ca.ubc.ca.ocrreader.Context;

public class ContextTest {


	@Test
	public void testParseTextOnlyLetters() {
		Context c = new Context("here is a test phrase");
		Map<String, Integer> words = c.getWords();
		assertTrue(words.containsKey("test"));
		assertEquals(words.size(), 5);
	}

	@Test
	public void testParseTextLettersAndDigits() {
		Context c = new Context("1 is the loneliest num3er");
		Map<String, Integer> words = c.getWords();
		assertTrue(words.containsKey("num3ber"));
		assertTrue(words.containsKey("1"));
		assertEquals(words.size(), 5);
	}

	@Test
	public void testParseTextPunctuation() {
		Context c = new Context("love's labour.() lost[]{}");
		Map<String, Integer> words = c.getWords();
		assertTrue(words.containsKey("labour"));
		assertTrue(words.containsKey("s"));
		assertEquals(words.size(), 4);
	}

	@Test
	public void testParseTextRepeatedWords() {
		Context c = new Context("one two two three three three");
		Map<String, Integer> words = c.getWords();
		assertTrue(words.get("two") == 2);
		assertTrue(words.get("three") == 3);
		assertEquals(words.size(), 3);
	}

	@Test
	public void testCompareSharedWordsNoCommonWords() {
		Context c1 = new Context("one two two three three three");
		Map<String, Integer> words1 = c1.getWords();

		Context c2 = new Context("four five six seven");
		Map<String, Integer> words2 = c2.getWords();

		Set<String> sharedWords = c1.getSharedWords(c2);
		assertTrue(sharedWords.isEmpty());
	}

	@Test
	public void testCompareSharedWordsSomeCommonWords() {
		Context c1 = new Context("one two three");
		Map<String, Integer> words1 = c1.getWords();

		Context c2 = new Context("two three four five");
		Map<String, Integer> words2 = c2.getWords();

		Set<String> sharedWords = c1.getSharedWords(c2);
		assertFalse(sharedWords.isEmpty());
		assertTrue(sharedWords.contains("two"));
		assertFalse(sharedWords.contains("five"));
		assertFalse(sharedWords.contains("one"));
	}

	@Test
	public void testCompareUniqueWordsEmptySet() {
		Context c1 = new Context("one two three");
		Map<String, Integer> words1 = c1.getWords();

		Context c2 = new Context("one two three four");
		Map<String, Integer> words2 = c2.getWords();

		Set<String> uniqueWords = c1.getUniqueWords(c2);
		assertTrue(uniqueWords.isEmpty());
	}

	@Test
	public void testCompareUniqueWordsSomeWords() {
		Context c1 = new Context("one two three");
		Map<String, Integer> words1 = c1.getWords();

		Context c2 = new Context("three four five");
		Map<String, Integer> words2 = c2.getWords();

		Set<String> uniqueWords = c1.getUniqueWords(c2);
		assertFalse(uniqueWords.isEmpty());
		assertTrue(uniqueWords.contains("two"));
		assertFalse(uniqueWords.contains("five"));
	}
}
