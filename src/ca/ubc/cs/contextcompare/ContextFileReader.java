package ca.ubc.cs.contextcompare;

import java.io.File;
import java.io.IOException;

public abstract class ContextFileReader {

	protected File file;

	public ContextFileReader(File file) {
		if (file.canRead()) {
			this.file = file;
		} else {
			// TODO what if file can't be read?
		}
	}

	public abstract String processContextFile() throws IOException;

}
