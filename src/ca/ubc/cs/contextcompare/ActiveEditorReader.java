package ca.ubc.cs.contextcompare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ActiveEditorReader extends ContextFileReader {

	private File file;

	public ActiveEditorReader(File file) {
		super(file);
	}

	@Override
	public String processContextFile() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(super.file));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null) {
			sb.append(line);
			// no reason to append newline char as it will be omitted when
			// the string is parsed by the client
			line = br.readLine();
		}
		br.close();
		return sb.toString();
	}

}
