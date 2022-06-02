package imageop.operations;

import java.awt.image.BufferedImage;

import imageop.Changeable;
import imageop.ImageOperation;

public class RemoveGrayOperation extends ImageOperation {
	@Changeable(displayName = "grayscale")
	private int grayscale;
	@Changeable(displayName = "threshold")
	private long threshold;
	
	
	public RemoveGrayOperation(int grayscale, int threshold) {
		super("unblur");
		this.grayscale = grayscale;
		this.threshold = threshold;
	}

	@Override
	public void apply(BufferedImage img) {
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				int rgb = img.getRGB(x, y);
				
				if(Math.abs(grayscale - rgb) <= threshold) { // if rgb value has max difference of threshold to grayscale, set pixel white
					img.setRGB(x, y, 0xffffff);
				}
			}
		}
	}
}
