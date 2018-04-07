package ie.gmit.sw.cipher;

import java.io.IOException;
import java.util.Arrays;

public final class Cipher {

	private int[][] clearSquare, topRightSquare, bottomLeftSquare;
	private int[][] topRightIndex, bottomLeftIndex;
	private long key_one, key_two;

	// private int squareSize = CharMatrix.squareSize;

	public Cipher(long key_one, long key_two, boolean writeToFile) {

		// the keys which are used to shuffle the matrices
		this.key_one = key_one;
		this.key_two = key_two;

		// build the cleartext and cipher matrices
		
		// BIG O notation within the method, below
		buildCipher();

		// if (writeToFile) {
		// writeCiphersToFile();
		// }
	}

	// build the cipher squares
	private void buildCipher() {
		// BIG O notation: n = CharMatrix.squareSize

		// set up the matrices - first the clear text one which is built from simple
		// character codes
		clearSquare = CharMatrix.buildCharCodeMatrix(); // O(n^2)

		// now build the cipher matrices, each one using each of the provided keys

		// each of these have a running time of O(n^2) for creation and O(n^2) for
		// shuffle, it should be noted that each of these are only run ONCE when the
		// ciphers are actually created - they do not affect actual encryption or
		// decryption running times
		topRightSquare = CharMatrix.shuffle(CharMatrix.buildCharCodeMatrix(), key_one);
		bottomLeftSquare = CharMatrix.shuffle(CharMatrix.buildCharCodeMatrix(), key_two);

		// the indexed cipher arrays, this technique allows O(1) access to a known
		// character code during decryption

		// these have a running time of O(n^2) each
		topRightIndex = CharMatrix.indexCipherArray(topRightSquare);
		bottomLeftIndex = CharMatrix.indexCipherArray(bottomLeftSquare);
	}

	// takes a character array and the number of characters to be encrypted (length)
	public char[] encrypt(char charArray[], int length) {

		// create the return array
		char returnArray[] = new char[length];

		int count = 0;
		// split the character array into an array of integer pairs,
		// each integer represents a character code

		// loop - running time of O(n/2) where n = length(number of characters in array)
		for (int bigram[] : BiGram.bigramCharArray(charArray, length)) {

			// encrypt each pair of integers, and add them to the return array

			// main encryption algorithm - switch pairs integers in matrices based on four
			// square cipher

			// all matrix elements can be accessed in constant time - O(1)
			returnArray[count] = ((char) (int) this.topRightSquare[bigram[0] / CharMatrix.squareSize][bigram[1]
					% CharMatrix.squareSize]);
			returnArray[count + 1] = ((char) (int) this.bottomLeftSquare[bigram[1] / CharMatrix.squareSize][bigram[0]
					% CharMatrix.squareSize]);

			count += 2; // increment by 2 since we are dealing with pairs
		}
		return returnArray;
	}

	// takes a character array and the number of characters to be decrypted (length)
	public char[] decrypt(char[] charArray, int length) {

		char returnArray[] = new char[length];

		int count = 0; // used to determine where in the return array each bigram should be placed
		
		// split the character array into an array of integer pairs,
		// each integer represents a character code

		// loop - running time of O(n/2) where n = length(number of characters in array)
		for (int bigram[] : BiGram.bigramCharArray(charArray, length)) {

			// decrypt each pair of integers, and add them to the return array

			// main decryption algorithm - switch pairs integers in matrices based on four
			// square cipher

			// because each of the cipher squares have been indexed, all matrix elements can
			// again be accessed in constant time - O(1)
			returnArray[count] = ((char) (int) this.clearSquare[topRightIndex[bigram[0]][0]][bottomLeftIndex[bigram[1]][1]]);
			returnArray[count
					+ 1] = ((char) (int) this.clearSquare[bottomLeftIndex[bigram[1]][0]][topRightIndex[bigram[0]][1]]);

			count += 2; // increment by 2 since we are dealing with pairs
		}
		return returnArray;
	}

	// Tests the integrity of the cipher created using the given keys.
	// Although this test is not 100% accurate, it should be good enough to indicate
	// if encryption and decryption will be accurate
	public boolean testKeys() {
		// create a string builder object containing the most common characters
		StringBuilder sb = new StringBuilder(
				" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ");

		// convert the stringbuilder to a character array
		char charsToEncrypt[] = sb.toString().toCharArray();

		// encrypt the characters
		char encryptedChars[] = this.encrypt(charsToEncrypt, charsToEncrypt.length);
		// and then decrypt them
		char decryptedChars[] = this.decrypt(encryptedChars, encryptedChars.length);

		// check that the decrypted chars are equivalent to the original char array
		// and return the result
		return Arrays.equals(charsToEncrypt, decryptedChars);

	}

	// for testing only
	public void writeCiphersToFile() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("Clear Text Square: " + System.lineSeparator());
		sb.append(CharMatrix.getArrayStringBuilder(clearSquare));
		sb.append(System.lineSeparator() + System.lineSeparator() + "Top Right Square: " + System.lineSeparator());
		sb.append(CharMatrix.getArrayStringBuilder(topRightSquare));
		sb.append(System.lineSeparator() + System.lineSeparator() + "Bottom Left Square: " + System.lineSeparator());
		sb.append(CharMatrix.getArrayStringBuilder(bottomLeftSquare));
		// FileHandler.outputToFile(sb, "./cipher_out.txt");
	}
}
