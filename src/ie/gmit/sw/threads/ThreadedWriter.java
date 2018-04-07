package ie.gmit.sw.threads;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;

public class ThreadedWriter implements Runnable {

	// take blocks from this queue
	private BlockingQueue<CharBlock> output_queue;
	// write to this file
	private String output_filename;

	public ThreadedWriter(BlockingQueue<CharBlock> queue, String output_filename) {
		this.output_queue = queue;
		this.output_filename = output_filename;
	}

	@Override
	public void run() {

		try {
			Writer writer;
			// attempt to open the file for writing
			writer = new OutputStreamWriter(new FileOutputStream(output_filename, false), ThreadController.charSet);

			char cBuf[]; // read into this array
			boolean keepAlive = true; // used to decide when to stop reading from queue
			while (keepAlive) {
				// take from the queue
				cBuf = output_queue.take().getChars();
				// check length of character array
				// assuming this is more efficient than comparing every chunk to the POISON
				if (cBuf.length == 3) {
					// compare to POISON, if true, break from while loop
					if (ThreadController.compareToPoison(cBuf)) {
						keepAlive = false;
					}
				} else {
					// still looping, write chunk to file
					writer.write(cBuf);
					// and reset the character array
					cBuf = null;
				}
			}
			// finished writing, close the file
			writer.close();
		} catch (InterruptedException | IOException e) {
			// files are checked in GUI so should never hit an exception here
			// TODO: how to handle thread exception correctly
			e.printStackTrace();
		}

	}

}
