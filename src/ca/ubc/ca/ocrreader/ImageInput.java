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
		String filename = imgFile.getName();
		int i = filename.lastIndexOf(".");
		// TODO this seems a clunky way to check the file type...
		if (filename.substring(i).equals(".png")) {
			try {
				BufferedImage img = ImageIO.read(imgFile);
				String imgText = ocr.readImage(img);
				return imgText;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
