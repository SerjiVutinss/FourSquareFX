package ie.gmit.sw.threads;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import ie.gmit.sw.ThreadedTest;

public class ThreadedReader implements Runnable {

	private BlockingQueue<CharBlock> input_queue;
	private String input_filename;
//	private String type;

	public ThreadedReader(BlockingQueue<CharBlock> queue, String input_filename, String type) {
		this.input_queue = queue;
		this.input_filename = input_filename;
//		this.type = type;

	}

	@Override
	public void run() {
//		FileInputStream is = null;
		BufferedReader in = null;
		try {
//			is = new FileInputStream(input_filename);
			in = new BufferedReader(new InputStreamReader(new FileInputStream(input_filename), "UTF8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		int c;

		int chunkSize = 65536;
		char charBuf[];

		try {
			charBuf = new char[chunkSize];
			// run while there is still data being read from the file
			while ((c = in.read(charBuf, 0, chunkSize)) > 0) {
				input_queue.put(new CharBlock(charBuf, c));
				// and reinitialise the char array
				charBuf = new char[chunkSize];
			}

			input_queue.put(ThreadedTest.PILL_BLOCK);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

}
