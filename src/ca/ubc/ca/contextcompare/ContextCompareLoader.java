package ca.ubc.ca.contextcompare;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

public class ContextCompareLoader {

	public static void main(String[] args) {
		ContextManager manager = new ContextManager();
		// TODO the folder where images are stored should be passed as the
		// param; currently hard coded for testing purposes
		// TODO add last modified time to files, ContextManager and Context
		// constructor

		// get the directory where images are stored
		File imgDir = new File("/Users/Laura/Desktop/testImageFiles/");

		System.out.println(imgDir.getName());

		// iterate over all files in dir and pass to ContextManager
		File[] files = imgDir.listFiles();
		for (File file : files) {
			System.out.println(file.getName());
			manager.addContext(file);
		}

		Iterator<Context> iterator = manager.iterator();
		while (iterator.hasNext()) {
			Context c1 = iterator.next();
			Context c2 = iterator.next();
			Set<String> sharedWords = c1.getSharedWords(c2);
			Set<String> uniqueWords = c2.getUniqueWords(c1);
			for (String word : uniqueWords) {
				System.out.println(word);
			}
		}

// for (Context context : manager) {
		// Map<String, Integer> words = context.getWords();
		// for (Map.Entry<String, Integer> pair : words.entrySet()) {
		// System.out.println(pair.getKey() + " : " + pair.getValue());
		// }
		// }
	}
}
