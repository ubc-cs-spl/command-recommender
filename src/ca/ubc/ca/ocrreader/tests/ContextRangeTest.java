package ca.ubc.ca.ocrreader.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs.contextcompare.ContextPoint;
import ca.ubc.cs.contextcompare.ContextRange;

public class ContextRangeTest {

	private ContextRange cr;
	private ContextPoint cp, cp2;

	private static final File TEST_IMG_DIR = new File(
			"~/CS and Programming/UAA/testImages/");

	@Before
	public void setUp() {
		cr = new ContextRange();
		cp = new ContextPoint(0);
		cp2 = new ContextPoint(1);
	}

	@Test
	public void testWordAccuracy() {
		File[] imgFiles = TEST_IMG_DIR.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".png");
			}
		});
		for (File file : imgFiles) {
			cr.addContext(new ContextPoint(file));
		}

		File[] txtFiles = TEST_IMG_DIR.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		});

		// TODO extract string(s) from text files
		Set<String> expectedText = new HashSet<String>();

		Set<String> words = cr.getWords();
		for (String word : words) {
			if (expectedText.contains(word)) {

			}
		}
	}

	@Test
	public void testGetWords() {
		cp.setWords("O wild west wind");
		cp2.setWords("thou breath of autumn's being");
		cr.addContext(cp);
		cr.addContext(cp2);
		assertTrue(cr.getContexts().size() == 2);

		Set<String> words = cr.getWords();
		for (String word : words) {
			System.out.println(word);
		}
		assertTrue(words.size() == 10);
		assertTrue(words.contains("autumn"));
	}

	@Test
	public void testGetWordsWithFrequency() {
		cp.setWords("one two two three three three");
		cp2.setWords("three three three four four four four");
		cr.addContext(cp);
		cr.addContext(cp2);

		Map<String, Integer> words = cr.getWordsWithFreq();
		assertTrue(words.size() == 4);
		assertTrue(words.containsKey("one"));
		assertTrue(words.containsKey("three"));
		assertTrue(words.containsKey("four"));
		assertTrue(words.get("three") == 6);
	}

	@Test
	public void testIsInRangeTrue() {
		cp.setTimestamp(5000);

		assertTrue(cp.isInRange(3000, 6000));
		assertTrue(cp.isInRange(3000, 5000));
		assertTrue(cp.isInRange(5000, 6000));
		assertFalse(cp.isInRange(3000, 4000));
	}

	@Test
	public void testGetContextsInRange() {
		cp.setTimestamp(4000);

		cp2.setTimestamp(5000);

		ContextPoint cp3 = new ContextPoint(2);
		cp3.setTimestamp(6000);

		cr.addContext(cp);
		cr.addContext(cp2);
		cr.addContext(cp3);

		Set<ContextPoint> contextsInRange = cr.getContextsInRange(0, 5000);
		assertTrue(contextsInRange.contains(cp));
		assertTrue(contextsInRange.contains(cp2));
		assertFalse(contextsInRange.contains(cp3));
	}
}
