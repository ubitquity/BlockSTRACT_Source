package pl.itcraft.appstract.core.file;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageFileUploader {
	
	static public void resizeImage(String path, int maxHeight) throws Exception {
		File file = new File(path);
		BufferedImage originalImage = ImageIO.read(file);
		
		if(null != originalImage){
			int imageType = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

			double width = originalImage.getWidth();
			double height = originalImage.getHeight();
			
			int newWidth;
			int newHeight;
			
			if(height > maxHeight){
				double scale = height/new Double(maxHeight);
				
				newHeight = maxHeight;
				newWidth = (int) (width/scale);
				
			}else{
				newHeight = (int) height;
				newWidth = (int) width;
			}
	
			BufferedImage newImage = new BufferedImage(newWidth, newHeight, imageType);
			Graphics2D g = newImage.createGraphics();
			g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
			g.dispose();
	
			ImageIO.write(newImage, path.substring(path.lastIndexOf('.') + 1), new File(path));
		} else {
			throw new Exception();
		}

	}

}
