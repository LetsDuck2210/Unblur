package imageop.operations;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import frame.Frame;
import imageop.ImageOperation;

public class SaveOperation extends ImageOperation {

	public SaveOperation() {
		super("save");
	}

	@Override
	public void apply(BufferedImage img) {
		try {
			var imageFile = Frame.get().getImageFile();
			var path = imageFile.getAbsolutePath();
			var filetype = path.substring(path.lastIndexOf('.') + 1);
			ImageIO.write(img, filetype, imageFile);
		} catch (IOException e) {
			Frame.get().debug(Color.RED, "Could not save file: " + e.getMessage());
		}
	}
	
}
