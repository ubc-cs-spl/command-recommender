package ca.ubc.cs.commandrecommender.usagedata.gathering.contextwriters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import ca.ubc.cs.commandrecommender.usagedata.gathering.UsageDataCaptureActivator;

public class TimedScreenCapture {

	private static final String FILENAME = "screenshot";
	private static final String IMG_FORMAT = ".png";
	private int counter;

	public TimedScreenCapture() {
		counter = 0;
	}

	public void captureScreenContext() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				Display display = PlatformUI.getWorkbench().getDisplay();
				Composite shell = display.getActiveShell();
				// if the Eclipse window is not active, skip the screenshot
				// TODO this is not working correctly, not taking screenshot if
				// multiple child windows open
				if (shell == null || shell.isDisposed()) {
					return;
				} else {
					takeScreenshot(display, shell);
				}
			}

			public void takeScreenshot(Display display, Composite shell) {
				// if this is the main Eclipse workbench (i.e., not a child of
				// the Eclipse window,
				// so parent == the display which is null), then take the
				// screenshot using the shell coordinates
				if (shell.getParent() == null) {
					// take the screenshot
					GC gc = new GC(display);
					Rectangle winRect = shell.getBounds();
					Image screenshot = new Image(display, winRect);
					gc.copyArea(screenshot, winRect.x, winRect.y);
					gc.dispose();

					// save the screenshot
					String fileName = FILENAME + counter + IMG_FORMAT;

					// Print to console for testing purposes
					System.out.println(fileName
							+ " saved by TimedScreenCapture");

					counter++;
					String imgFilePath = UsageDataCaptureActivator.getDefault()
							.getStateLocation().toString()
							+ fileName;
					ImageLoader imgLoader = new ImageLoader();
					imgLoader.data = new ImageData[] { screenshot
							.getImageData() };
					imgLoader.save(imgFilePath, SWT.IMAGE_PNG);

					// Print to console for testing purposes
					System.out.println(imgFilePath);

					screenshot.dispose();
				} else {
					// if this is a child of the main Eclipse shell, pass its
					// parent shell back to takeScreenshot
					// recursion should be minimal as there will only ever be a
					// small number of Eclipse windows open
					takeScreenshot(display, shell.getParent());
				}
			}
		});
	}
}
