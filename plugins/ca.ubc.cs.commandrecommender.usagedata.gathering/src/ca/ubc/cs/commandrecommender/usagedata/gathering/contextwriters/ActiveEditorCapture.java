package ca.ubc.cs.commandrecommender.usagedata.gathering.contextwriters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Timer;

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

public class ActiveEditorCapture implements ActionListener {

	private static final int DELAY = 10000; // ten seconds in milliseconds
	private static final String FILENAME = "editorContents";
	private static final String FORMAT = ".txt";

	private Timer timer;
	private int counter;
	private UsageDataCaptureActivator udca;

	public ActiveEditorCapture(UsageDataCaptureActivator udca) {
		timer = new Timer(DELAY, this);
		timer.start();
		counter = 0;
		this.udca = udca;
	}

	public void actionPerformed(ActionEvent e) {
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
			iStream = file.getContents();
			String filename = FILENAME + counter + FORMAT;
			String filePath = udca.getSettings().getScreenCapFilePath(filename);
			System.out.println(filePath);
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
