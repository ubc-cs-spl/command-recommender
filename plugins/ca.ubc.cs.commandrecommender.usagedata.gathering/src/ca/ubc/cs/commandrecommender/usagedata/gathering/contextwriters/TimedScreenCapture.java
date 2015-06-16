package ca.ubc.cs.commandrecommender.usagedata.gathering.contextwriters;

import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

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

public class TimedScreenCapture implements ActionListener {

	private static final int DELAY = 10000; // ten seconds in milliseconds
	private static final String IMG_NAME = "screenshot";
	private static final String IMG_FORMAT = ".png";

	private Timer timer;
	private int counter;
	private UsageDataCaptureActivator udca;

	public TimedScreenCapture(UsageDataCaptureActivator udca)
			throws AWTException {
		this.timer = new Timer(DELAY, this);
		timer.start();
		counter = 0;
		this.udca = udca;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
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
					String fileName = IMG_NAME + counter + IMG_FORMAT;

					// Print to console for testing purposes
					System.out.println(fileName);

					counter++;
					String imgFilePath = udca.getSettings()
							.getScreenCapFilePath(fileName);
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
