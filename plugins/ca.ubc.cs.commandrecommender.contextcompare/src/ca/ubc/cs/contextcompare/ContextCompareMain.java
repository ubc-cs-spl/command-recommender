package ca.ubc.cs.contextcompare;

import java.io.File;
import java.util.Set;

public class ContextCompareMain {

	/*
	 * @param args is the full path name where the context files to be processed
	 * are stored
	 */
	public static void main(String[] args) {
		// create File object of the directory where images are stored
		System.out.println(args[0]);
		File contextDir = new File(args[0]);

		// where all contexts will be stored
		ContextRange allContexts = new ContextRange();

		// iterate over all files in dir; process those that are screenshots or
		// text dump from editor.
		File[] files = contextDir.listFiles();
		for (File file : files) {
			String filename = file.getName();
			if (filename.endsWith(".png") || filename.endsWith(".txt")) {
				allContexts.addContext(new ContextPoint(file));
				// TODO print to console for testing purposes
			}
		}

		// TODO this is here just for testing
		Set<ContextPoint> contexts = allContexts.getAllContexts();
		int count = 0;
		for (IContext c : contexts) {
			System.out.println("Context " + count);
			count++;
			Set<String> words = c.getWords();
			for (String word : words) {
				System.out.println(word);
			}
		}

	}
}
