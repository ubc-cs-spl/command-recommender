package ca.ubc.cs.commandrecommender.usagedata.gathering.screenshot;

import java.awt.AWTException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import ca.ubc.cs.commandrecommender.usagedata.gathering.UsageDataCaptureActivator;

public class TimedScreenCapture implements ActionListener{
	
	private static final int DELAY = 10000; // ten seconds in milliseconds
	private static final String IMG_NAME = "screenshot";
	private static final String IMG_FORMAT = ".png";
	
	private Timer timer;
	private int imgCounter;
	private UsageDataCaptureActivator udca;
	
	public TimedScreenCapture(UsageDataCaptureActivator udca) throws AWTException {
		this.timer = new Timer(DELAY, this);
		timer.start();
		imgCounter = 0;
		this.udca = udca;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				Display display = PlatformUI.getWorkbench().getDisplay();
				Composite shell = display.getActiveShell();
				if (shell == null) {
					return;
				} else {
					takeScreenshot(display, shell);
				}
			}

			public void takeScreenshot(Display display, Composite shell) {
				if (shell.getParent() == null) {
					GC gc = new GC(display);
					final Image screenshot = new Image(display, shell.getBounds());
					gc.copyArea(screenshot, shell.getBounds().x, shell.getBounds().y);
					gc.dispose();
					
					String fileName = IMG_NAME + imgCounter + IMG_FORMAT;
					imgCounter++;
					String imgFilePath = udca.getSettings().getScreenCapFilePath(fileName);
					// TODO printing file path for testing only
					System.out.println(imgFilePath);
					ImageLoader imgLoader = new ImageLoader();
					imgLoader.data = new ImageData[] {screenshot.getImageData()};
					imgLoader.save(imgFilePath, SWT.IMAGE_PNG);

					screenshot.dispose();
				} else {
					takeScreenshot(display, shell.getParent());
				}
			}
		});	
	}
}
