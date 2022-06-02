package imageop;

import java.awt.image.BufferedImage;

public abstract class ImageOperation {
	protected String display;
	
	public ImageOperation(String display) {
		this.display = display;
	}
	public String getDisplay() {
		return display;
	}
	
	
	public abstract void apply(BufferedImage img);
	
}
