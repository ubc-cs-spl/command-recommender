package ca.ubc.ca.ocrreader;

import java.io.File;

public class ContextCompareLoader {


	public static void main(String[] args) {
		ContextManager manager = new ContextManager();
		// the folder where images are stored should be passed as the param
		// FIXME currently hard coded for testing purposes
		// TODO add last modified time to files, ContextManager and Context
		// constructor

		// get the directory where images are stored
		File imgDir = new File("~/Desktop/testImageFiles");

		// iterate over all files and pass to ContextManager
		File[] files = imgDir.listFiles();

		for (File file : files) {
			// TODO check if this is an image file, not other or dir
			manager.addContext(file);
		}
	}
}
