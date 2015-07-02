package ca.ubc.cs.contextcompare.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs.contextcompare.ContextPoint;

public class ContextPointTest {

	private ContextPoint cp;

	@Before
	public void setUp() {
		cp = new ContextPoint(0);
	}

	@Test
	public void testParseWordsAllLetters() {
		cp.setWords("My heart leaps up when I behold a rainbow in the sky");
		Set<String> theseWords = cp.getWords();
		assertTrue(theseWords.size() == 12);
		assertTrue(theseWords.contains("rainbow"));
	}

	@Test
	public void testParseWordsLettersAndNumbers() {
		cp.setWords("2 roads diverged in a y3ll0w wood");
		Set<String> theseWords = cp.getWords();
		assertTrue(theseWords.size() == 7);
		assertTrue(theseWords.contains("y3ll0w"));
	}

	@Test
	public void testParseWordsWithPunctuation() {
		cp.setWords("Love's {Labour}: Lost.()");
		Set<String> theseWords = cp.getWords();
		assertTrue(theseWords.size() == 4);
		assertTrue(theseWords.contains("s"));
		assertTrue(theseWords.contains("Lost"));
		assertFalse(theseWords.contains("}"));
	}

	@Test
	public void testParseWordsEmptyString() {
		cp.setWords("");
		Set<String> theseWords = cp.getWords();
		assertTrue(theseWords.size() == 0);
	}

	@Test
	public void testParseWordsRepeated() {
		cp.setWords("one two two three three three");
		Map<String, Integer> theseWords = cp.getWordsWithFreq();
		assertTrue(theseWords.size() == 3);
		assertTrue(theseWords.get("three") == 3);
		assertTrue(theseWords.get("two") == 2);
	}

	@Test
	public void testGetFreqWords() {
		cp.setWords("one two two three three three");
		Set<String> theseWords = cp.getFrequentWords(2);
		assertTrue(theseWords.size() == 2);
		assertTrue(theseWords.contains("three"));
		assertTrue(theseWords.contains("two"));
		assertFalse(theseWords.contains("one"));
	}

	@Test
	public void testGetFreqWordsEmptySet() {
		cp.setWords("one two two three three three");
		Set<String> theseWords = cp.getFrequentWords(4);
		assertTrue(theseWords.size() == 0);
	}

	@Test
	public void testIsInRange() {
		cp.setTimestamp(5000);
		assertTrue(cp.isInRange(3000, 6000));
		assertTrue(cp.isInRange(3000, 5000));
		assertTrue(cp.isInRange(5000, 6000));
		assertFalse(cp.isInRange(3000, 4000));
	}

}
