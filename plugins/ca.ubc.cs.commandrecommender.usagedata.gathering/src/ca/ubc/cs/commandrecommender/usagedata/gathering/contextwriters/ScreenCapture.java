package ca.ubc.cs.commandrecommender.usagedata.gathering.contextwriters;

import java.io.IOException;
import java.util.Date;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import ca.ubc.cs.commandrecommender.usagedata.gathering.UsageDataCaptureActivator;

public class ScreenCapture {

	private static final String FILENAME = "screenshot";
	private static final String IMG_FORMAT = ".png";

	public ScreenCapture() {
	}

	public void captureScreenContext() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// get the currently active Eclipse window (could be null if
				// eclipse is not active; could be a child of the main
				// workbench window)
				Composite shell = PlatformUI.getWorkbench().getDisplay()
						.getActiveShell();
				takeScreencap(shell);
			}

			private void takeScreencap(Composite shell) {
				// if Eclipse is not active or window has been disposed, do
				// nothing
				if (shell == null || shell.isDisposed()) {
					System.out.println("eclipse is not the active shell");
					return;
				} else {
					// if shell's parent is null, that means it is the main
					// workbench window; use this to get the bounds for the
					// screenshot. If shell's parent is not null, recurse
					// until
					// the main workbench window is found
					if (shell.getParent() == null) {
						// create the file name for the new screencap
						Date date = new Date();
						String fileName = FILENAME + date.toString()
								+ IMG_FORMAT;
						// TODO printing file name to console for testing only,
						// to be deleted
						System.out.println(fileName);

						// get screen coordinates and dimensions of main
						// workbench window
						Rectangle eclipseWin = shell.getBounds();
						int winX = eclipseWin.x;
						int winY = eclipseWin.y;
						int winWidth = eclipseWin.width;
						int winHeight = eclipseWin.height;
						String winBounds = "-R " + winX + "," + winY + ","
								+ winWidth + "," + winHeight;

						// pass screencapture command and args to command line
						// -x flag turns off shutter sound
						ProcessBuilder pb = new ProcessBuilder("screencapture",
								"-x", winBounds, fileName);
						pb.directory(UsageDataCaptureActivator.getDefault()
								.getStateLocation().toFile());
						try {
							pb.start();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						// pass shell's parent back until main workbench window
						// is found
						takeScreencap(shell.getParent());
					}
				}
			}
		});
	}
}