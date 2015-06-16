package ca.ubc.cs.contextcompare;

import java.io.File;
import java.util.Set;

public class ContextCompareMain {

	public static void main(String[] args) {
		// TODO the folder where images are stored should be passed as the
		// param; currently hard coded for testing purposes

		ContextRange cr = new ContextRange();

		// get the directory where images are stored
		File contextDir = new File("/Users/Laura/Desktop/testContextFiles/");

		System.out.println(contextDir.getName());

		// iterate over all files in dir and pass to ContextManager
		File[] files = contextDir.listFiles();
		for (File file : files) {
			String filename = file.getName();
			if (filename.endsWith(".png") || filename.endsWith(".txt")) {
				cr.addContext(new ContextPoint(file));
				// print to console for testing purposes
				System.out.println("file name: " + filename);
				System.out.println("last modified: " + file.lastModified());
			}
		}

		Set<ContextPoint> contexts = cr.getAllContexts();
		int count = 0;
		for (Context c : contexts) {
			System.out.println("Context " + count);
			count++;
			Set<String> words = c.getWords();
			for (String word : words) {
				System.out.println(word);
			}
		}

	}
}
