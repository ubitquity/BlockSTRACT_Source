package pl.itcraft.appstract.core.file;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

@Service
public class ImageResizerServiceImpl implements ImageResizerService {
	
	@Override
	public byte[] resizeImage(String mimeType, BufferedImage image, int maxLongerSide) {
		String formatName = getFormatName(mimeType); // resolve before resizing
		try {
			int srcWidth = image.getWidth();
			int srcHeight = image.getHeight();
			
			int newWidth = 0;
			int newHeight = 0;
			if (srcWidth <= maxLongerSide && srcHeight <= maxLongerSide) {
				newWidth = srcWidth;
				newHeight = srcHeight;
			} else if (srcWidth > srcHeight) {
				newWidth = maxLongerSide;
				newHeight = calculateNewShorterSide(srcHeight, maxLongerSide, srcWidth);
			} else {
				newHeight = maxLongerSide;
				newWidth = calculateNewShorterSide(srcWidth, maxLongerSide, srcHeight);
			}
			
			BufferedImage scalledImage = resize(image, newWidth, newHeight);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(scalledImage, formatName, baos);
			return baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@Override
	public byte[] resizeImage(String mimeType, byte[] image, int maxLongerSide) {
		try {
			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(image));
			return resizeImage(mimeType, bi, maxLongerSide);
		}
		catch(IOException exc) {
			throw new RuntimeException(exc.getMessage(), exc);
		}
	}
	
	private String getFormatName(String mimeType) {
		if ("image/png".equals(mimeType)) {
			return "png";
		} else if ("image/jpg".equals(mimeType)) {
			return "jpg";
		} else {
			throw new RuntimeException("Only jpg|png is supported");
		}
	}
	
	private int calculateNewShorterSide(int val, int maxLongerSide, int longerSideWidth) {
		float factor = (float) maxLongerSide / (float) longerSideWidth;
		return Math.round(factor * val);
	}
	
	private BufferedImage resize(final Image srcImage, int width, int height) {
		final BufferedImage dstImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D graphics2D = dstImage.createGraphics();
		graphics2D.setComposite(AlphaComposite.Src);
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.drawImage(srcImage, 0, 0, width, height, null);
		graphics2D.dispose();
		return dstImage;
	}
}
