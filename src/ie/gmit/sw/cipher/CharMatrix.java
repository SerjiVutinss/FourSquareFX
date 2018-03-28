package ie.gmit.sw.cipher;

import java.util.Random;

public abstract class CharMatrix {

	/* START public static variables - Cipher class needs access to these */
	// the first character code needed (newline character)
	public static int startCharCode = 0;
	// the dimension of the matrix
	public static int squareSize = 16;
	/* END public static variables */

	// build the clear text array of integers which represent the characters
	public static int[][] buildCharCodeMatrix() {

		// create the return array (matrix) with an initial size
		int[][] matrix = new int[squareSize][squareSize];

		// RUNS IN O(n^2) (WHERE n=squareSize) TIME - only runs once
		int val = 0;
		for (int i = 0; i < squareSize; i++) {

			for (int j = 0; j < squareSize; j++) {
				matrix[i][j] = val + startCharCode;
				val++;
			}
		}
		return matrix;
	}

	// repeatable shuffle based on the supplied long number

	/* The following code is adapted from Collections.shuffle */
	// Shuffle a 2D square array

	public static int[][] shuffle(int[][] matrix, long key) {
		Random rand = new Random(key);

		int size = matrix.length - 1;

		for (int i = matrix.length - 1; i > 0; i--) {
			for (int j = matrix[i].length - 1; j > 0; j--) {
				int m = rand.nextInt(i + 1);
				int n = rand.nextInt(j + 1);

				int temp = matrix[i][j];
				matrix[i][j] = matrix[m][n];
				matrix[m][n] = temp;
			}
		}
		return matrix;
	}

	// Swap two entries in a 2D array
	private static void swap(int[][] matrix, int columns, int i, int j) {
		int tmp = matrix[i / columns][i % columns];
		matrix[i / columns][i % columns] = matrix[j / columns][j % columns];
		matrix[j / columns][j % columns] = tmp;
	}

	// returns a Tuple with two int elements - find a char code location within
	// the cleartext matrix
	public static int[] getIndices(int charCode) {

		// return array - limit to 2 elements
		int indices[];

		int charIndex = charCode - startCharCode;

		int row, col;
		// this method may need to be improved if cipher squares are increased in size
		row = charIndex / squareSize;
		col = charIndex % squareSize;

		indices = new int[2];
		indices[0] = row;
		indices[1] = col;

		return indices;
	}

	// method used to index the cipher array - each Tuple containing the row and
	// column positions of the char in the cipher array will be placed at the
	// correct index (charCode - startCode) in the array
	// This method only runs once for each cipher matrix after they have been
	// created using the supplied keys
	public static int[][] indexCipherArray(int[][] arr) {

		// create and initialise an ArrayList of size squareSize * squareSize
		int[][] indexedList = new int[squareSize * squareSize][2];
		// this will be used for each char
		int charPos[];

		// loop through the cipher array and place each Tuple containing the char
		// position in the cipher array at the correct index (charCode - startCode) in
		// the returned (indexedList) array -
		// RUNNING TIME O(n) or O(squareSize)
		for (int row = 0; row < squareSize; row++) {
			for (int col = 0; col < squareSize; col++) {
				// assign the variable using the current row and col values
				charPos = new int[2];
				charPos[0] = row;
				charPos[1] = col;
				// and place the Tuple into the return array at the correct index
				indexedList[arr[row][col]] = charPos;
				// indexedList.set(arr[row][col] - startCharCode, charPos);
			}
		}
		return indexedList;
	}

	// DEBUGGING METHODS

	// used for testing to print out or print cipher squares to file
	public static StringBuilder getArrayStringBuilder(int[][] arr) {
		// test square output
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < squareSize; i++) {
			sb.append("\nRow " + i + ": ");
			for (int j = 0; j < squareSize; j++) {
				char el = (char) Integer.parseInt(String.valueOf(arr[i][j]));
				sb.append(" " + el + " ,");
			}
		}
		return sb;
	}
}