package ie.gmit.sw.threads;

import java.util.concurrent.BlockingQueue;

import ie.gmit.sw.ThreadedTest;
import ie.gmit.sw.cipher.Cipher;

public class ThreadedDecrypter implements Runnable {

	private BlockingQueue<CharBlock> read_queue;
	private BlockingQueue<CharBlock> decrypt_queue;
	private Cipher cipher;

	public ThreadedDecrypter(BlockingQueue<CharBlock> read_queue, BlockingQueue<CharBlock> decrypt_queue,
			Cipher cipher) {
		this.read_queue = read_queue;
		this.decrypt_queue = decrypt_queue;
		this.cipher = cipher;
	}

	@Override
	public void run() {

		CharBlock cBlock;

//		char[] charBuf;
//		int length;
//
//		int count = 0;
		boolean keepAlive = true;
		try {
			while (keepAlive) {
				// read from the read_queue
				cBlock = read_queue.take();
				if (cBlock.getLength() == 3) {
					if (ThreadedTest.compareToPoison(cBlock.getChars())) {
						// System.out.println("FOUND POISON PILL - decrypter");
						keepAlive = false;
					}
				} else {

					CharBlock encBlock = new CharBlock(cipher.decrypt(cBlock.getChars(), cBlock.getLength()),
							cBlock.getLength());
					decrypt_queue.put(encBlock);
					// System.out.println("Put decrypted: " + count);
//					count++;
				}
			}
			// add a poison pill to the decrypt queue
			this.decrypt_queue.put(ThreadedTest.PILL_BLOCK);
			// System.out.println("PUT POISON PILL - DECRYPTER");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}