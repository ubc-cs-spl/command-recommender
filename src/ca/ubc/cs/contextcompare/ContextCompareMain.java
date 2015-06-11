package ca.ubc.cs.contextcompare;

import java.io.File;
import java.util.Set;

public class ContextCompareMain {

	public static void main(String[] args) {
		// TODO the folder where images are stored should be passed as the
		// param; currently hard coded for testing purposes

		ContextRange cr = new ContextRange();

		// get the directory where images are stored
		File imgDir = new File("/Users/Laura/Desktop/testImageFiles/");

		System.out.println(imgDir.getName());

		// iterate over all files in dir and pass to ContextManager
		File[] files = imgDir.listFiles();
		for (File file : files) {
			String filename = file.getName();
			int i = filename.lastIndexOf(".");
			if (filename.substring(i).equals(".png")) {
				cr.addContext(new ContextPoint(file));
				System.out.println("file name: " + filename);
				System.out.println("last modified: " + file.lastModified());
			}
		}

		Set<Context> contexts = cr.getContexts();
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
