package ca.ubc.cs.contextcompare;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ScreenCaptureReader extends ContextFileReader {
	public static final Tesseract TESS = new Tesseract();
	private BufferedImage img;

	public ScreenCaptureReader(File file) {
		super(file);
	}

	@Override
	public String processContextFile() throws IOException {
		BufferedImage img = ImageIO.read(super.file);
		img = convertToGreyscale(img);
		this.img = img;
		return doOCR();
	}

	/*
	 * Converts given image to grey scale to improve OCR accuracy
	 */
	public BufferedImage convertToGreyscale(BufferedImage colourImg) {
		int width = colourImg.getWidth();
		int height = colourImg.getHeight();
		BufferedImage greyImg = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = greyImg.getGraphics();
		g.drawImage(colourImg, 0, 0, null);
		g.dispose();
		return greyImg;
	}

	/*
	 * Returns a string of all text contained in the image
	 */
	public String doOCR() {
		String readOut = "";
		try {
			readOut = TESS.doOCR(img);
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return readOut;
	}
}
