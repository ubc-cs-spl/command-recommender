package ca.ubc.ca.contextcompare;

import java.io.File;

public class ContextCompareMain {

	private ContextRange allContext;

	public ContextCompareMain() {
		allContext = new ContextRange();
	}

	public void addContext(Context c) {
		allContext.addContext(c);
	}

	public static void main(String[] args) {
		// TODO the folder where images are stored should be passed as the
		// param; currently hard coded for testing purposes

		ContextCompareMain ccm = new ContextCompareMain();

		// get the directory where images are stored
		File imgDir = new File("/Users/Laura/Desktop/testImageFiles/");

		System.out.println(imgDir.getName());

		// iterate over all files in dir and pass to ContextManager
		File[] files = imgDir.listFiles();
		for (File file : files) {
			String filename = file.getName();
			int i = filename.lastIndexOf(".");
			if (filename.substring(i).equals(".png")) {
				ccm.addContext(new ContextPoint(file));
			}

		}
	}
}
