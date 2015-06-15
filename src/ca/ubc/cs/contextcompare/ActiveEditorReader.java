package ca.ubc.cs.contextcompare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class ActiveEditorReader {

	/*
	 * from Giovanni
	 */
	private String getFileContent(IFile file) {
		BufferedReader br = null;
		String line;
		StringBuilder sb = new StringBuilder();
		try {
			InputStream stream = file.getContents();
			br = new BufferedReader(new InputStreamReader(stream));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (CoreException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
