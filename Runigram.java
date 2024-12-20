import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] image;

		// Tests the horizontal flipping of an image:
		image = flippedHorizontally(tinypic);
		System.out.println();
		print(image);
		
		//// Write here whatever code you need in order to test your work.
		//// You can continue using the image array.
		Color[][] image1=read(args[0]);

		
		Color[][] image2= scaled(image1, 3, 5);

		print(image2);
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		// Reads the RGB values from the file into the image array. 
		// For each pixel (i,j), reads 3 values from the file,
		// creates from the 3 colors a new Color object, and 
		// makes pixel (i,j) refer to that object.
		//// Replace the following statement with your code.
		int[] arr= new int[numCols*numRows*3];
		for(int y=0;y<arr.length;y++){
			arr[y]= in.readInt();
		}
		int count=0;

		for(int i=0; i<numRows;  i++){
			for(int j=0;j<numCols;j++){
				image[i][j]= new Color(arr[count],arr[(count+1)] , arr[count+2]);
				count= count + 3;
		}
		
	}
	return image ;
}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		//// Replace this comment with your code
		//// Notice that all you have to so is print every element (i,j) of the array using the print(Color) function.
		for(int i=0;i<image.length;i++){
			for(int j=0;j<image[0].length;j++){
				print(image[i][j]);
			}
			System.out.println();
		}
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		Color[][] newimage= new Color[image.length][image[0].length];
		for(int i=0; i<image.length;i++){
			for(int j=0;j<image[0].length;j++){
				newimage[i][j]=image[i][(image[i].length-1)-j];
			}
		}
		return newimage;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		Color[][] newimage= new Color[image.length][image[0].length];
		for(int i=0;i<image.length;i++){
			for(int j=0;j<image[0].length;j++){
				newimage[i][j]= image[image.length-1-i][j];
			}
		}
		return null;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	private static Color luminance(Color pixel) {
		int grey= (int)(0.299*(pixel.getRed())+0.114*(pixel.getBlue())+0.587*(pixel.getGreen()));
		Color greycolor= new Color(grey, grey, grey);
		return greycolor;
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		Color[][] greyscaled= new Color[image.length][image[0].length];
		for(int i=0;i<image.length;i++){
			for(int j=0;j<image[0].length;j++){
				greyscaled[i][j]=luminance(image[i][j]);
			}
		}
		return greyscaled;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {
		Color[][] scaled= new Color[width][height];
		float newy= (float)(image.length/scaled.length);
		float newx= (float)(image[0].length)/scaled[0].length;
		for (int i=0;i<scaled.length;i++){
			for( int j=0;j<scaled[0].length;j++){

				int origX = Math.min(image[0].length - 1, (int) (j * newx));
				int origY = Math.min(image.length - 1, (int) (i * newy));
				scaled[i][j]= image[origY][origX];
			}
		}

		return scaled;
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		int newred= (int)((c1.getRed()*alpha)+(c2.getRed()*(1-alpha)));
		int newgreen= (int)((c1.getGreen()*alpha)+(c2.getGreen()*(1-alpha)));
		int newblue= (int)((c1.getBlue()*alpha)+(c2.getBlue()*(1-alpha)));

		Color blend= new Color(newred, newgreen, newblue);
		return blend;
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		Color[][] blendimage= new Color[image1.length][image1[0].length];
		for(int i=0;i<blendimage.length;i++){
			for(int j=0;j<blendimage[0].length;j++){
				blendimage[i][j]=blend(image1[i][j],image2[i][j], alpha);
			}
		}
		return blendimage;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		Color[][] target1= new Color[source.length][source[0].length];
		if(source.length != target.length || source[0].length != target[0].length){
		target1= scaled(target, source.length, source[0].length);
	} else{
		target1= target;
	}
	Runigram.setCanvas(source);
	Runigram.display(source);
	StdDraw.pause(500);
	Color[][] morphimage= new Color[source.length][source[0].length];
	for(int i=0; i<n;i++){
		float alpha= (float)(n-i)/n;
		morphimage= blend(source, target1, alpha);
		Runigram.display(morphimage);
		StdDraw.pause(500);
	}
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(width, height);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

