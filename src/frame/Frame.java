package frame;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import imageop.ImageOperation;

public class Frame {
	private static Frame instance;
	
	private Frame() { }
	public static Frame get() {
		if(instance == null)
			instance = new Frame();
		
		return instance;
	}
	
	private JFrame frame;
	private JPanel container;
	private JLabel debug;
	private ImageRenderer renderer;
	private File imageFile;
	private BufferedImage selectedImage;
	
	public void showGUI() {
		frame = new JFrame("unblur");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(3); // exit on close
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		container = new JPanel();
		container.setSize(frame.getSize());
		container.setLayout(null);
		frame.add(container);
		
		debug = new JLabel();
		debug.setSize(container.getSize());
		container.add(debug);
		
		selectFile();
		if(selectedImage == null) return;
		
		frame.setSize(selectedImage.getWidth(), selectedImage.getHeight());
		container.setSize(frame.getSize());
		debug.setSize(frame.getSize());
		
		renderer = new ImageRenderer();
		renderer.setSize(container.getSize());
		container.add(renderer);
		
		renderer.add(selectedImage);
		
		new Thread(() -> {
			container.repaint();
			renderer.repaint();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		
		showOperationGUI();
	}
	
	private List<ImageOperation> operations = new ArrayList<>();
	public void showOperationGUI() {
		var frame = new JFrame("operations");
		frame.setSize(300, 300);
		frame.setDefaultCloseOperation(3); // exit on close
		frame.setResizable(false);
		frame.setVisible(true);
		
		var container = new JPanel();
		container.setSize(frame.getSize());
		container.setLayout(null);
		frame.add(container);
		
		for(int i = 0; i < operations.size(); i++) {
			var operation = operations.get(i);
			var button = new JButton(operation.getDisplay());
			button.setSize((int) Math.round(container.getWidth() / 1.5), 30);
			button.setLocation((int) Math.round(container.getWidth() / 2 - button.getWidth() / 2) - 8, i * (button.getHeight() + 5));
			button.addActionListener(l -> { 
				operation.apply(selectedImage);
				renderer.repaint(); // update image on screen
			});
			container.add(button);
		}
	}
	public void addImageOperation(ImageOperation...operations) {
		this.operations.addAll(List.of(operations));
	}
	
	public void debug(Color color, String text) {
		debug.setForeground(color);
		debug.setText(text);
	}
	
	public void selectFile() {
		var fileChooser = new JFileChooser();
		fileChooser.setApproveButtonText("unblur");
		int option = fileChooser.showOpenDialog(null);
		if(option == JFileChooser.CANCEL_OPTION || option == JFileChooser.ERROR_OPTION) {
			frame.dispose();
			System.exit(-1);
		}
		imageFile = fileChooser.getSelectedFile();
		var accepted = List.of("png", "pdf", "jpg", "jpeg");
		var path = imageFile.getAbsolutePath();
		var filetype = path.substring(path.lastIndexOf('.') + 1);
		if(!accepted.contains(filetype)) {
			debug(Color.RED, "unknown filetype '" + filetype + "'");
			return;
		}
		debug(Color.GREEN, "loading image...");
		try {
			selectedImage = ImageIO.read(imageFile);
			debug(Color.BLACK, "");
		} catch (IOException e) {
			debug(Color.RED, "I/O Exception: " + e.getMessage());
			return;
		}
	}
	public File getImageFile() {
		return imageFile;
	}
}
