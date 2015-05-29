package ca.ubc.ca.ocrreader;

import java.awt.image.BufferedImage;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCRReader {

	private Tesseract tesseract;

	public OCRReader() {
		tesseract = new Tesseract();
	}

	/*
	 * Returns a String of all text contained in the image
	 */
	public String readImage(BufferedImage img) {
		try {
			String readOut = tesseract.doOCR(img);
			return readOut;
		} catch (TesseractException e) {
			e.printStackTrace();
			return null;
		}
	}

}
