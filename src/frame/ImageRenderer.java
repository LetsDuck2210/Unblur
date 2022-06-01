package frame;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class ImageRenderer extends JLabel {
	private static final long serialVersionUID = 4398726593425L;
	
	private List<Image> images;
	public ImageRenderer() {
		images = new ArrayList<>();
	}
	public void add(Image img) {
		if(!images.contains(img))
			images.add(img);
	}
	public void remove(Image img) {
		images.remove(img);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// no foreach to avoid ConcurrentModificationException
		for(int i = 0; i < images.size(); i++) {
			g.drawImage(images.get(i), 0, 0, getWidth(), getHeight(), null);
		}
	}
}
