
/**
 * @author Your Name Here
 * Your Summary Here
 */
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import javax.imageio.ImageIO;

public class Assignment10 {

	// WARNING! DO NOT MODIFY ANYTHING ABOVE THIS LINE. (Except for JavaDoc.)

	public static final String PATH_TO_PPM_INPUT_FILE = "";
	public static final String PATH_TO_JPG_OUTPUT_FILE = "";

	public static int[][] compressRuns(int[] arr) {
		// TODO: Implement me and add JavaDoc.
		return new int[0][0];
	}

	public static int[] decompressRuns(int[][] arr) {
		// TODO: Implement me and add JavaDoc.
		return new int[0];
	}

	public static int[] localMaxima(int[] arr) {
		// TODO: Implement me and add JavaDoc.
		return new int[0];
	}

	public static int[][] blurImageData(int[][] data) {
		// TODO: Implement me and add JavaDoc.
		return new int[0][0];
	}

	// WARNING! DO NOT MODIFY ANYTHING BELOW THIS LINE.

	public static void main(String[] args) throws IOException {
		runTask1CompressionTest();
		runTask1DecompressionTest();
		runTask2Test();
		runTask3Test();

	}

	/**
	 * Utility method to print out the contents of an array.
	 * 
	 * @param arr The array to be printed
	 */
	public static void printArray(int[] arr) {
		for (int val : arr) {
			System.out.print(val + "\t");
		}
		System.out.println();
	}

	/**
	 * Utility method to print out the contents of an array.
	 * 
	 * @param arr The array to be printed
	 */
	public static void printArray(int[][] arr) {
		for (int[] row : arr) {
			printArray(row);
		}
	}

	/**
	 * Runs unit test for compressing array runs.
	 */
	public static void runTask1CompressionTest() {
		int[] example = { 4, 4, 4, 1, 1, 2 };
		int[][] result = compressRuns(example);

		System.out.println("Input");
		printArray(example);

		System.out.println("Result");
		printArray(result);

		System.out.println("Expected");
		int[][] expected = { { 4, 3 }, { 1, 2 }, { 2, 1 } };
		printArray(expected);
	}

	/**
	 * Runs unit test for decompressing array runs.
	 */
	public static void runTask1DecompressionTest() {
		int[][] example = { { 4, 3 }, { 1, 2 }, { 2, 1 } };
		int[] result = decompressRuns(example);

		System.out.println("Input");
		printArray(example);

		System.out.println("Result");
		printArray(result);

		System.out.println("Expected");
		int[] expected = { 4, 4, 4, 1, 1, 2 };
		printArray(expected);
	}

	/**
	 * Runs a unit test for the finding of local maxima in an array.
	 */
	public static void runTask2Test() {
		int[] example = { 1, 2, 5, 2, 3, 3, 9 };
		int[] result = localMaxima(example);

		System.out.println("Input");
		printArray(example);

		System.out.println("Result");
		printArray(result);

		System.out.println("Expected");
		int[] expected = { 7, 9 };
		printArray(expected);
	}

	/**
	 * Creates a blurred JPG image based on an input PPM file.
	 * 
	 * @throws IOException
	 */
	private static void runTask3Test() throws IOException {
		int[][] inputData = readP2PPM(PATH_TO_PPM_INPUT_FILE);
		int[][] blurredData = blurImageData(inputData);
		writeGrayscaleJPG(blurredData, PATH_TO_JPG_OUTPUT_FILE);
	}

	/**
	 * Convert a 2D int array of grayscale [0,255] values to a JPEG file.
	 *
	 * @param pixels 2D array, pixels[row][col], 0–255
	 * @param outputPath Path to the output .jpg file
	 * @throws IOException If the file cannot be written
	 */
	public static void writeGrayscaleJPG(int[][] pixels, String outputPath) throws IOException {
		if (pixels == null || pixels.length == 0 || pixels[0].length == 0) {
			throw new IllegalArgumentException("pixels array must be non-empty");
		}

		int height = pixels.length;
		int width = pixels[0].length;

		// Create a grayscale image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

		// Set each pixel
		for (int y = 0; y < height; y++) {
			if (pixels[y].length != width) {
				throw new IllegalArgumentException("All rows must have the same length");
			}
			for (int x = 0; x < width; x++) {
				int gray = pixels[y][x];

				// Clamp to [0,255] just in case
				if (gray < 0)
					gray = 0;
				if (gray > 255)
					gray = 255;

				int rgb = (gray << 16) | (gray << 8) | gray; // R=G=B=gray
				image.setRGB(x, y, rgb);
			}
		}

		// Write to JPEG
		File outFile = new File(outputPath);
		ImageIO.write(image, "jpg", outFile);
	}

	/**
	 * Reads a P2 (ASCII) PPM file and returns its pixel data as int[height][width].
	 * Each value is in [0, maxVal] as specified in the file header.
	 *
	 * @param path Path to the .ppm file
	 * @return 2D int array of grayscale values
	 * @throws IOException if there is an I/O or format error
	 */
	public static int[][] readP2PPM(String path) throws IOException {
		try (Reader r = new BufferedReader(new FileReader(path))) {
			StreamTokenizer st = new StreamTokenizer(r);

			// Configure tokenizer:
			// - Treat '#' as starting a comment to end of line
			// - Default whitespace rules (space, newline, etc.)
			st.commentChar('#');

			// 1. Read magic number ("P2")
			String magic = nextWord(st);
			if (!"P2".equals(magic)) {
				throw new IOException("Not a P2 PPM file: magic=" + magic);
			}

			// 2. Read width, height, and maxVal
			int width = nextInt(st);
			int height = nextInt(st);
			int maxVal = nextInt(st); // you can store/use this if needed

			if (width <= 0 || height <= 0 || maxVal <= 0) {
				throw new IOException(
						"Invalid PPM header: width=" + width + ", height=" + height + ", maxVal=" + maxVal);
			}

			int[][] pixels = new int[height][width];

			// 3. Read pixel data row by row
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int gray = nextInt(st);

					// Optionally clamp to [0, maxVal]
					if (gray < 0)
						gray = 0;
					if (gray > maxVal)
						gray = maxVal;

					pixels[y][x] = gray;
				}
			}

			return pixels;
		}
	}

	// Helper: read next token as a "word" (for the magic number "P2")
	private static String nextWord(StreamTokenizer st) throws IOException {
		int tokenType = st.nextToken();
		if (tokenType == StreamTokenizer.TT_WORD) {
			return st.sval;
		}
		throw new IOException("Expected a word token, got type=" + tokenType);
	}

	// Helper: read next token as an int
	private static int nextInt(StreamTokenizer st) throws IOException {
		int tokenType = st.nextToken();
		if (tokenType == StreamTokenizer.TT_NUMBER) {
			return (int) st.nval;
		} else if (tokenType == StreamTokenizer.TT_WORD) {
			// In case numbers are read as words (rare, but safer):
			try {
				return Integer.parseInt(st.sval);
			} catch (NumberFormatException e) {
				throw new IOException("Expected integer, got: " + st.sval, e);
			}
		}
		throw new IOException("Expected integer, got token type=" + tokenType);
	}

}
