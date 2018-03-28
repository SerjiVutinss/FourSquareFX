package ie.gmit.sw.cipher;

public class BiGram {

	// takes in a character array and splits it into an array of pairs of integers,
	// each integer representing a character code
	public static int[][] bigramCharArray(char charArray[], int length) {
		// the array of bigrams to be returned
		int[][] returnArray;
		// used to check if the supplied charArray arg has an
		// even or uneven number of chars
		boolean evenNumChars;

		// set boolean value according to supplied length arg
		evenNumChars = (length % 2 == 0) ? true : false;

		// if there are an even number of chars
		if (evenNumChars) {
			// initialise the array - half the number of bigrams as chars are needed
			returnArray = new int[length / 2][2];
		} else {
			// otherwise, add an extra bigram to accommodate the pad
			returnArray = new int[(length + 1) / 2][2];
		}

		int count = 0; // loop counter

		// loop through the supplied charArray, since we are
		// taking bigrams, increment by 2
		for (int i = 0; i < length; i += 2) {
			// do not use charCode 0
			if (((int) charArray[i] != 0) && ((int) charArray[i + 1] != 0)) {
				// add both of the charCodes to the return array in the corect positions
				returnArray[count][0] = charArray[i];
				returnArray[count][1] = charArray[i + 1];
			}
			count++; // increment loop counter
		}

		// if an uneven charArray was supplied initially
		if (!evenNumChars) {
			System.out.println("Adding PAD");
			// pad the return array by setting last char to charCode for backtick
			returnArray[returnArray.length - 1][1] = (int) '`';
		}

		return returnArray; // return the array of charCode pairs

	}
}
