package ie.gmit.sw.threads;

import java.util.concurrent.BlockingQueue;

import ie.gmit.sw.cipher.Cipher;

public class ThreadedEncrypter implements Runnable {

	// read clear text from this queue
	private BlockingQueue<CharBlock> read_queue;
	// place encrypted text on this queue
	private BlockingQueue<CharBlock> encrypt_queue;
	// the cipher object to be used for encryption
	private Cipher cipher;

	// queues from ThreadController, Cipher from MainWindowController
	public ThreadedEncrypter(BlockingQueue<CharBlock> read_queue, BlockingQueue<CharBlock> encrypt_queue,
			Cipher cipher) {
		this.read_queue = read_queue;
		this.encrypt_queue = encrypt_queue;
		this.cipher = cipher;
	}

	@Override
	public void run() {

		CharBlock cBlock;
		boolean keepAlive = true;

		try {

			while (keepAlive) {
				// read from the read_queue
				cBlock = read_queue.take();
				if (cBlock.getLength() == 3) {
					if (ThreadController.compareToPoison(cBlock.getChars())) {
						keepAlive = false;
					}
				} else {
					if (!(cBlock.getLength() % 2 == 0)) {
						cBlock.setLength(cBlock.getLength() + 1);
					}
					CharBlock encBlock = new CharBlock(cipher.encrypt(cBlock.getChars(), cBlock.getLength()),
							cBlock.getLength());
					// and add it to the encrypt queue
					encrypt_queue.put(encBlock);
				}
			}
			// add a poison pill to the encrypt queue
			this.encrypt_queue.put(ThreadController.PILL_BLOCK);

		} catch (InterruptedException e) {
			// TODO: how to handle thread exception correctly
			e.printStackTrace();
		}

	}

}
