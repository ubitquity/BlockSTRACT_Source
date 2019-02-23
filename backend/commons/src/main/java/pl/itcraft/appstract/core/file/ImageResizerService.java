package pl.itcraft.appstract.core.file;

import java.awt.image.BufferedImage;

public interface ImageResizerService {
	byte[] resizeImage(String mimeType, BufferedImage image, int maxLongerSide);
	byte[] resizeImage(String mimeType, byte[] image, int maxLongerSide);
}