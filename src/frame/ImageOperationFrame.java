package frame;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import imageop.Changeable;
import imageop.ImageOperation;

public class ImageOperationFrame {
	private static ImageOperationFrame instance;
	
	private ImageOperationFrame() { }
	public static ImageOperationFrame get() {
		if(instance == null)
			instance = new ImageOperationFrame();
		return instance;
	}
	
	private JFrame frame;
	private JPanel container;
	private JLabel debug;
	
	/**
	 * Will show a GUI to edit each {@link Changeable} Field
	 * */
	public void showGUI(ImageOperation operation) {
		frame = new JFrame(operation.getDisplay());
		frame.setSize(400, 300);
		frame.setLocation(300, 0);
		frame.setDefaultCloseOperation(1);
		frame.setResizable(false);
		frame.setVisible(true);
		
		container = new JPanel();
		container.setSize(frame.getSize());
		container.setLayout(null);
		frame.add(container);
		
		debug = new JLabel("", SwingConstants.CENTER);
		debug.setSize(frame.getSize());
		container.add(debug);
		
		// get all *Changeable* fields
		var fields = operation.getClass().getDeclaredFields();
		for(int i = 0; i < fields.length; i++) {
			if(fields[i].isAnnotationPresent(Changeable.class)) {
				fields[i].setAccessible(true);
				String fieldDisplay = fields[i].getAnnotation(Changeable.class).displayName();
				var type = fields[i].getType();
				var fieldDisplayLabel = new JLabel(fieldDisplay);
				fieldDisplayLabel.setSize(container.getWidth() - 26, 24);
				fieldDisplayLabel.setLocation(10, i * (24 * 2 + 5));
				container.add(fieldDisplayLabel);
				
				try {
					// TODO fix terrible design, also missing inputs for  float, double, short, char, byte, ...
					if(type == int.class) {
						var inputField = new JTextField(Integer.toHexString(fields[i].getInt(operation)).toUpperCase());
						inputField.setSize(container.getWidth() - 26, 24);
						inputField.setLocation(5, i * (24 * 2 + 5) + 24);
						container.add(inputField);
						
						// verify that only numbers can be pressed
						onlyDigits(inputField);
						
						final int j = i;
						inputField.addActionListener(l -> {
							int res = 0;
							try {
								res = Integer.parseInt(inputField.getText());
							} catch(NumberFormatException e0) {
								try {
									res = (int) Long.parseLong(inputField.getText(), 16);
								} catch(NumberFormatException e1) {
									debug.setText("NaN: " + e1.getMessage());
									debug.setForeground(Color.RED);
									return;
								}
							}
							
							try {
								fields[j].setInt(operation, res);
							} catch (IllegalArgumentException | IllegalAccessException e) {
								e.printStackTrace();
							}
						});
					}
					if(type == long.class) {
						var inputField = new JTextField(Long.toHexString(fields[i].getLong(operation)).toUpperCase());
						inputField.setSize(container.getWidth() - 26, 24);
						inputField.setLocation(5, i * (24 * 2 + 5) + 24);
						container.add(inputField);
						
						// verify that only numbers can be pressed
						onlyDigits(inputField);
						
						final int j = i;
						inputField.addActionListener(l -> {
							long res = 0;
							try {
								res = Long.parseLong(inputField.getText());
							} catch(NumberFormatException e0) {
								try {
									res = Long.parseLong(inputField.getText(), 16);
								} catch(NumberFormatException e1) {
									debug.setText("NaN: " + e1.getMessage());
									debug.setForeground(Color.RED);
									return;
								}
							}
							
							try {
								fields[j].setLong(operation, res);
							} catch (IllegalArgumentException | IllegalAccessException e1) {
								e1.printStackTrace();
							}
						});
					}
					if(type == String.class) {
						var inputField = new JTextField("" + fields[i].get(operation));
						inputField.setSize(container.getWidth() - 10, 24);
						inputField.setLocation(5, i * (24 * 2 + 5) + 24);
						container.add(inputField);
						
						final int j = i;
						inputField.addActionListener(l -> {
							try {
								fields[j].set(operation, inputField.getText());
							} catch (IllegalArgumentException | IllegalAccessException e) {
								e.printStackTrace();
							}
						});
					}
				} catch(IllegalAccessException | IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void onlyDigits(JTextField inputField) {
		inputField.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyTyped(KeyEvent e) {
	        	var allowedKeys = List.of(
	        		KeyEvent.VK_BACK_SPACE,
	        		KeyEvent.VK_CONTROL,
	        		KeyEvent.VK_ENTER
	        	);
	        	var allowedChars = List.of('a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F');
	            if (!Character.isDigit(e.getKeyChar()) && !allowedKeys.contains(e.getKeyCode()) && !allowedChars.contains(e.getKeyChar())) {
	                e.consume();
	            }
	        }
	    });
	}
}
