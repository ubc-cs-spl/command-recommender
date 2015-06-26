package ca.ubc.ca.ocrreader.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs.contextcompare.ContextPoint;
import ca.ubc.cs.contextcompare.ContextRange;

/*
 * All test files should be paired in order to test OCR accuracy, i.e., there should be a txt file 
 * and a png containing exactly the same text.
 */
public class ContextFileReaderTest {

	private String filePath;
	private File testDir;

	@Before
	public void setUp() {
		// TODO file path is hard coded for now
		filePath = "/Users/Laura/CS and Programming/UAA/UAA Eclipse Workspace/contextCompareTestFiles";
		testDir = new File(filePath);
	}

	@Test
	public void testOCRAccuracy() throws IOException {
		ContextRange txtContext = new ContextRange();
		ContextRange imgContext = new ContextRange();

		File[] files = testDir.listFiles();

		// read and process context info for all .txt or .png files in test dir
		for (File file : files) {
			String fileName = file.getName();
			if (fileName.endsWith(".txt")) {
				ContextPoint thisTxtContext = new ContextPoint(file);
				txtContext.addContext(thisTxtContext);
			}
			if (fileName.endsWith(".png")) {
				ContextPoint thisImgContext = new ContextPoint(file);
				imgContext.addContext(thisImgContext);
			}
		}

		// get sets of words from both processed contexts
		Set<String> txtWords = txtContext.getWords();
		Set<String> imgWords = imgContext.getWords();

		// count how many words in the .txt context were correctly read by OCR
		double correct = 0;
		for (String word : txtWords) {
			if (imgWords.contains(word)) {
				// increment correct count
				correct++;
			}
		}

		// calculate percentage accuracy
		double accuracy = (correct / (double) txtWords.size()) * 100;
		System.out.println("The OCR was " + accuracy + "% accurate");

		// could be adjusted if greater or lesser accuracy is acceptable
		assertTrue(accuracy >= 75.0);
	}
}
