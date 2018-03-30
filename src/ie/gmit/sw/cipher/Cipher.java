package ie.gmit.sw.cipher;

import java.io.IOException;
import java.util.Arrays;

import ie.gmit.sw.FileHandler;

public final class Cipher {

	private int[][] clearSquare, topRightSquare, bottomLeftSquare;
	private int[][] topRightIndex, bottomLeftIndex;
	private long key_one, key_two;

	private int squareSize = CharMatrix.squareSize;

	public Cipher(long key_one, long key_two, boolean writeToFile) throws IOException {

		this.key_one = key_one;
		this.key_two = key_two;

		buildCipher();

		if (writeToFile) {
			System.out.println("Writing Ciphers to file...");
			writeCiphersToFile();
		}
	}

	// build the cipher squares
	private void buildCipher() {
		// set up the arrays - first the clear text one which is built from simple char
		// codes
		clearSquare = CharMatrix.buildCharCodeMatrix();

		// now build the cipher arrays, each one using each of the provided keys
		System.out.println("Building Ciphers");
		topRightSquare = CharMatrix.shuffle(CharMatrix.buildCharCodeMatrix(), key_one);
		bottomLeftSquare = CharMatrix.shuffle(CharMatrix.buildCharCodeMatrix(), key_two);

		System.out.println("Indexing Ciphers");
		topRightIndex = CharMatrix.indexCipherArray(topRightSquare);
		bottomLeftIndex = CharMatrix.indexCipherArray(bottomLeftSquare);
		System.out.println("Done");
	}

	public char[] encrypt(char charArray[], int length) {

		// if (length % 2 != 0) {
		// System.out.println("Encrypt: Received uneven Array!!!!!!");
		// }

		char returnArray[] = new char[length];

		int count = 0;
		for (int bigram[] : BiGram.bigramCharArray(charArray, length)) {

			returnArray[count] = ((char) (int) this.topRightSquare[bigram[0] / squareSize][bigram[1] % squareSize]);
			returnArray[count
					+ 1] = ((char) (int) this.bottomLeftSquare[bigram[1] / squareSize][bigram[0] % squareSize]);

			count += 2;
		}
		return returnArray;
	}

	// decrypts a StringBuilder object using the already built ciphers
	public char[] decrypt(char[] charArray, int length) {

		if (length % 2 != 0) {
			System.out.println("Decrypt: Received uneven Array!!!!!!");
		}

		char returnArray[] = new char[length];

		int count = 0;
		for (int bigram[] : BiGram.bigramCharArray(charArray, length)) {

			try {
				returnArray[count] = ((char) (int) this.clearSquare[topRightIndex[bigram[0]][0]][bottomLeftIndex[bigram[1]][1]]);
				returnArray[count
						+ 1] = ((char) (int) this.clearSquare[bottomLeftIndex[bigram[1]][0]][topRightIndex[bigram[0]][1]]);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(
						((char) (int) this.clearSquare[bottomLeftIndex[bigram[1]][0]][topRightIndex[bigram[0]][1]]));
			}
			count += 2;
		}
		return returnArray;
	}

	public boolean testKeys() {
		StringBuilder sb = new StringBuilder();
		sb.append(
				" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ");

		char charsToEncrypt[] = sb.toString().toCharArray();
		char encryptedString[] = this.encrypt(charsToEncrypt, charsToEncrypt.length);
		char decryptedString[] = this.decrypt(encryptedString, encryptedString.length);

		if (Arrays.equals(charsToEncrypt, decryptedString)) {
			return true;
		} else {
			return false;
		}

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
		FileHandler.outputToFile(sb, "./cipher_out.txt");
	}
}
