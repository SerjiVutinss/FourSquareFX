package ie.gmit.sw.threads;

/* 
 * A simple class which is used to place charArrays on the 
 * Blocking queues with actual number of characters
 * 
 * Since each block is of the same size, 
 *  
 *  BiGram.bigramCharArray() static method
 */
public class CharBlock {

	private char[] chars; // the charArray
	private int length; // the length of the charArray

	public CharBlock(char[] chars, int length) {
		this.chars = chars;
		this.length = length;
	}

	public char[] getChars() {
		return chars;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
