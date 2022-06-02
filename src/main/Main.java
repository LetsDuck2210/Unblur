package main;

import frame.Frame;
import imageop.operations.RemoveGrayOperation;
import imageop.operations.SaveOperation;

public class Main {
	public static void main(String[] args) {
		long threshold = 0xf4240; // -> 1 mil
		int grayscale = 0xffadadad; // light gray
		
		// parse arguments to override default values
		if(args.length > 0) {
			for(int i = 0; i < args.length; i++) {
				var arg = args[i];
				
				// terrible design please don't read
				if(arg.startsWith("-grayscale") && arg.contains("=")) {
					var val = arg.substring(arg.indexOf('=') + 1);
					try {
						grayscale = Integer.parseInt(val);
						System.out.println("using " + grayscale + " as grayscale");
					} catch(NumberFormatException e0) {
						try {
							grayscale = (int) Long.parseLong(val, 16); // try to parse hex
							System.out.println("using " + grayscale + " as grayscale");
						} catch(NumberFormatException e1) {
							System.out.println("NaN: " + e1.getMessage());
							System.out.println("Using default value for grayscale...");
						}
					}
				}
				if(arg.startsWith("-threshold") && arg.contains("=")) {
					var val = arg.substring(arg.indexOf('=') + 1);
					try {
						threshold = Integer.parseInt(val);
						System.out.println("using " + threshold + " as threshold");
					} catch(NumberFormatException e0) {
						try {
							threshold = Long.parseLong(val, 16);
							System.out.println("using " + threshold + " as threshold");
						} catch(NumberFormatException e1) {
							System.out.println("NaN: " + e1.getMessage());
							System.out.println("Using default value for threshold...");
						}
					}
				}
			}
		}
		
		var frame = Frame.get();
		
		frame.addImageOperation(
			new RemoveGrayOperation(grayscale, threshold),
			new SaveOperation()
		);
		
		frame.showGUI();
	}

}
