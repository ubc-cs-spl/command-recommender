package ca.ubc.ca.ocrreader.tests;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs.contextcompare.ContextPoint;
import ca.ubc.cs.contextcompare.ContextRange;

public class ContextRangeTest {

	private ContextRange cr, cr2;
	private ContextPoint cp, cp2;

	@Before
	public void setUp() {
		cr = new ContextRange();
		cr2 = new ContextRange();
		cp = new ContextPoint();
		cp2 = new ContextPoint();
	}

	@Test
	public void testGetWords() {
		cp.setWords("O wild west wind, thou breath of autumn's being");
		cr.addContext(cp);

		cp2.setWords("Thou from whose unseen presence the leaves dead are driven");
		cr2.addContext(cp2);
		cr.addContext(cr2);

		Set<String> words = cr.getWords();
		assertTrue(words.size() == 19);
		assertTrue(words.contains("unseen"));
	}

	@Test
	public void testGetWordsWithFrequency() {
		cp.setWords("one two two three three three");
		cr.addContext(cp);

		cp2.setWords("three three three four four four four");
		cr2.addContext(cp2);
		cr.addContext(cr2);

		Map<String, Integer> words = cr.getWordsWithFreq();
		assertTrue(words.size() == 4);
		assertTrue(words.containsKey("one"));
		assertTrue(words.containsKey("three"));
		assertTrue(words.containsKey("four"));
		assertTrue(words.get("three") == 6);
	}

	@Test
	public void testGetWordsInRange() {

	}
}
