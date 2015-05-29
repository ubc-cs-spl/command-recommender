package ca.ubc.ca.ocrreader;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ContextManager implements Iterable<Context> {

	private List<Context> contexts;
	private ImageInput imgInput;

	public ContextManager() {
		contexts = new LinkedList<Context>();
		imgInput = new ImageInput();
	}

	// Read the text from the given file, if the file is an image.
	public void addContext(File file) {
		String text = imgInput.readImage(file);
		if (text != null) {
			String filename = file.getName();
			long timestamp = file.lastModified();
			Context context = new Context(filename, timestamp, text);
			contexts.add(context);
		}
	}

	public Set<String> compareSharedWords(Context contextA, Context contextB) {
		return contextA.getSharedWords(contextB);
	}

	public Set<String> compareUniqueWords(Context contextA, Context contextB) {
		return contextA.getUniqueWords(contextB);
	}

	@Override
	public Iterator<Context> iterator() {
		return contexts.iterator();
	}

}
