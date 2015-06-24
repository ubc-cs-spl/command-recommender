package ca.ubc.cs.commandrecommender.usagedata.gathering.contextwriters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class ContextWriter implements ActionListener, IContextWriter {

	protected static final int DELAY = 10000; // ten seconds in milliseconds
	protected Timer timer;

	private ActiveEditorCapture aec = new ActiveEditorCapture();
	private static final ScreenCapture tsc = new ScreenCapture();
	private boolean captureToggle;

	private static ContextWriter instance;

	public static ContextWriter getInstance() {
		if (instance == null) {
			instance = new ContextWriter();
		}
		return instance;
	}

	private ContextWriter() {
		timer = new Timer(DELAY, this);
		timer.start();
		captureToggle = false;
	}

	public void switchCaptureMode(boolean captureModeVal) {
		captureToggle = captureModeVal;
	}

	public void actionPerformed(ActionEvent e) {
		if (captureToggle) {
			tsc.captureScreenContext();
		} else {
			aec.captureEditorContext();
		}
	}

}
