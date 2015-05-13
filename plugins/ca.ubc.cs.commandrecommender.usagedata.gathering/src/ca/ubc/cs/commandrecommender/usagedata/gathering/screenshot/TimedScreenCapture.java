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

public class TimedScreenCapture implements ActionListener{
	
	private static final int DELAY = 10000; // ten seconds in milliseconds
	private static final String IMG_NAME = "screenshot";
	
	private Robot robot;
	private Timer timer;
	private int imgCounter;
	
	public TimedScreenCapture() throws AWTException {
		// TODO consider if this is the best exception handling
		this.robot = new Robot();
		this.timer = new Timer(DELAY, this);
		timer.start();
		imgCounter = 0;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// get the dimensions of the main screen
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Rectangle screenRect = new Rectangle(toolkit.getScreenSize());
		
		// have robot take a screencap of the main screen
		BufferedImage screenshot = robot.createScreenCapture(screenRect);
		
		// write image to filejava 
		try {
			String filename = IMG_NAME + imgCounter + ".png";
			imgCounter++;
			boolean writeImg = ImageIO.write(screenshot, "png", new File(filename));
			System.out.println(filename);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}