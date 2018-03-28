package ie.gmit.sw.threads;

/* 
 * this class is used to place charArrays on the 
 * Blocking queues with their known lengths 
 */
public class CharBlock {

	private char[] chars;
	private int length;

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
