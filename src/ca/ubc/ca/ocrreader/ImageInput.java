package ca.ubc.ca.ocrreader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageInput {

	private OCRReader ocr;

	public ImageInput() {
		ocr = new OCRReader();
	}

	public String readImage(File imgFile) {
		try {
			BufferedImage img = ImageIO.read(imgFile);
			String imgText = ocr.readImage(img);
			return imgText;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
