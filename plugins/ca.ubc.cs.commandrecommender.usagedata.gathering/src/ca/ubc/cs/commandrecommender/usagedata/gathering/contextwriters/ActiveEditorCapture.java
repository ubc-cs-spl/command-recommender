package ca.ubc.cs.commandrecommender.usagedata.gathering.contextwriters;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import ca.ubc.cs.commandrecommender.usagedata.gathering.UsageDataCaptureActivator;

public class ActiveEditorCapture {

	private static final String FORMAT = ".txt";
	protected static final String FILENAME = "editorContents";
	protected int counter;

	public ActiveEditorCapture() {
		counter = 0;
	}

	public void captureEditorContext() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();
				IEditorPart activeEditor = page.getActiveEditor();
				if (activeEditor == null) {
					System.out.println("active editor is null");
					return;
				} // needs to handle this
				final IEditorInput input = activeEditor.getEditorInput();
				if (!(input instanceof FileEditorInput)) {
					System.out.println("editor is of wrong type");
					return;
				} // need to handle this
				writeFile(((FileEditorInput) input).getFile());
			}
		});
	}

	private void writeFile(IFile file) {
		InputStream iStream = null;
		FileOutputStream oStream = null;
		try {
			// create file name
			String fileName = FILENAME + counter + FORMAT;
			String filePath = UsageDataCaptureActivator.getDefault()
					.getStateLocation().toString()
					+ fileName;
			System.out.println(filePath);
			counter++;

			// create i/o streams and read the bytes into the new file
			iStream = file.getContents();
			oStream = new FileOutputStream(filePath);
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = iStream.read(bytes)) != -1) {
				oStream.write(bytes, 0, read);
			}
		} catch (CoreException | IOException e) {
			e.printStackTrace();
		} finally {
			if (iStream != null) {
				try {
					iStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (oStream != null) {
				try {
					oStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
