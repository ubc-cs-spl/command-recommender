package ca.ubc.ca.ocrreader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ContextManager {

	private List<Context> contexts;
	private ImageInput imgInput;

	public ContextManager() {
		contexts = new LinkedList<Context>();
		imgInput = new ImageInput();
	}

	public void addContext(File imgFile) {
		String text = imgInput.readImage(imgFile);
		Context context = new Context(text);
		contexts.add(context);
	}

	public Set<String> compareSharedWords(Context contextA, Context contextB) {
		return contextA.getSharedWords(contextB);
	}

	public Set<String> compareUniqueWords(Context contextA, Context contextB) {
		return contextA.getUniqueWords(contextB);
	}

}
