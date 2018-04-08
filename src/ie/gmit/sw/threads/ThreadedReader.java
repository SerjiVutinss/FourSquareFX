package ie.gmit.sw.threads;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

public class ThreadedReader implements Runnable {

	// chunks of characters will be placed on this queue
	private BlockingQueue<CharBlock> input_queue;
	// the file to be read from
	private String input_filename;

	// determines how many characters are placed in each block on the queue
	private int chunkSize = 65536; // this seems to work well but could be tweaked

	// constructor - takes in the queue from the ThreadController, and the filename
	// to be read
	public ThreadedReader(BlockingQueue<CharBlock> queue, String input_filename) {
		this.input_queue = queue;
		this.input_filename = input_filename;
	}

	@Override
	public void run() {
		BufferedReader in = null;
		try {
			// try to open the file for reading in UTF8 mode
			in = new BufferedReader(
					new InputStreamReader(new FileInputStream(input_filename), ThreadController.charSet));
		} catch (Exception e) {
			// files are checked in GUI so should never hit an exception here
			e.printStackTrace();
		}

		// used to determine number of characters actually read - needed for the last
		// chunk to be encrypted and decrypted correctly
		int c;

		char charBuf[]; // read chunks of characters into this character array

		try {
			charBuf = new char[chunkSize]; // initialise a new character array
			// run while there is still data being read from the file
			while ((c = in.read(charBuf, 0, chunkSize)) > 0) {
				// place a new CharBlock on the queue using the character array and actual
				// characters read from file
				input_queue.put(new CharBlock(charBuf, c));
				// and reinitialise the char array
				charBuf = new char[chunkSize];
			}
			// end of file was reached, place POISON PILL block on the queue
			input_queue.put(ThreadController.PILL_BLOCK);
		} catch (InterruptedException | IOException e) {
			// files are checked in GUI so should never hit an exception here
			// TODO: how to handle thread exception correctly
			e.printStackTrace();
		}
	}

}
