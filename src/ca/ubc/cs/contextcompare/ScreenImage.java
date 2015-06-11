package ca.ubc.cs.contextcompare;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ScreenImage {

	public static final Tesseract TESS = new Tesseract();
	private BufferedImage img;

	public ScreenImage(File imgFile) {
		this.img = null;
		BufferedImage img;
		try {
			img = ImageIO.read(imgFile);
			img = convertToGreyscale(img);
			this.img = img;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Converts given image to grey scale to improve OCR accuracy
	 */
	private BufferedImage convertToGreyscale(BufferedImage colourImg) {
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
