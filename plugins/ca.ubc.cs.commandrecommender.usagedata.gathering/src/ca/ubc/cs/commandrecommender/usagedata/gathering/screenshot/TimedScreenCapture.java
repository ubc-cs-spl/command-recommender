package ca.ubc.cs.commandrecommender.usagedata.gathering.screenshot;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import ca.ubc.cs.commandrecommender.usagedata.gathering.UsageDataCaptureActivator;

public class TimedScreenCapture implements ActionListener{
	
	private static final int DELAY = 10000; // ten seconds in milliseconds
	private static final String IMG_NAME = "screenshot";
	private static final String IMG_FORMAT = ".png";
	
	//private Robot robot;
	private Timer timer;
	private int imgCounter;
	private UsageDataCaptureActivator udca;
	
	public TimedScreenCapture(UsageDataCaptureActivator udca) throws AWTException {
		//this.robot = new Robot();
		this.timer = new Timer(DELAY, this);
		timer.start();
		imgCounter = 0;
		this.udca = udca;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				Display display = PlatformUI.getWorkbench().getDisplay().getDefault();
				Shell shell = display.getActiveShell();

				GC gc = new GC(display);
				final Image screenshot = new Image(display, shell.getBounds());
				gc.copyArea(screenshot, screenshot.getBounds().x, screenshot.getBounds().y);
				gc.dispose();

				ImageLoader imgLoader = new ImageLoader();
				imgLoader.data = new ImageData[] {screenshot.getImageData()};
				String fileName = IMG_NAME + imgCounter + IMG_FORMAT;
				imgCounter++;
				
				// TODO printing file path for testing only
				String imgFilePath = udca.getSettings().getScreenCapFilePath(fileName);
				System.out.println(imgFilePath);
				imgLoader.save(imgFilePath, SWT.IMAGE_PNG);

				screenshot.dispose();
			}
		});
	}

}
