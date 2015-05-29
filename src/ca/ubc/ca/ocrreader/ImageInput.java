package ca.ubc.ca.ocrreader;

import java.awt.Graphics;
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
		// TODO this seems an inelegant way to check the file type...
		if (filename.substring(i).equals(".png")) {
			try {
				BufferedImage img = ImageIO.read(imgFile);
				img = convertToGreyscale(img);
				String imgText = ocr.readImage(img);
				return imgText;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

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
}
