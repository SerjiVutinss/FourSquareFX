package ie.gmit.sw.threads;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;

import ie.gmit.sw.ThreadedTest;

public class ThreadedWriter implements Runnable {

	private BlockingQueue<CharBlock> output_queue;
	private String output_filename;
//	private String type;

	public ThreadedWriter(BlockingQueue<CharBlock> queue, String output_filename, String type) {
		this.output_queue = queue;
		this.output_filename = output_filename;
//		this.type = type;
	}

	@Override
	public void run() {

		try {
			Writer writer;
			writer = new OutputStreamWriter(new FileOutputStream(output_filename, false), "UTF8");

			char cBuf[];
			boolean keepAlive = true;
			while (keepAlive) {
				cBuf = output_queue.take().getChars();
				if (cBuf.length == 3) {
					if (ThreadedTest.compareToPoison(cBuf)) {
						keepAlive = false;
					}
				} else {
					writer.write(cBuf);
					cBuf = null;
				}
			}
			writer.close();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}

	}

}
