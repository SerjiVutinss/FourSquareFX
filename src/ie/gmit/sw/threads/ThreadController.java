package ie.gmit.sw.threads;

import java.nio.charset.Charset;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ie.gmit.sw.cipher.Cipher;

public class ThreadController {

	static long startTime;
	static double duration, totalTime = 0;
	static Charset charSet = Charset.forName("UTF-8");

	Cipher cipher = null;
	

	private static BlockingQueue<CharBlock> source_read_queue = null;
	private static BlockingQueue<CharBlock> encrypted_read_queue = null;
	private static BlockingQueue<CharBlock> encrypt_queue = null;
	private static BlockingQueue<CharBlock> decrypt_queue = null;

	private static int queueCapacity = 10;

	private static final String POISON_PILL_MESSAGE = "EOF";
	public static final CharBlock PILL_BLOCK = new CharBlock(POISON_PILL_MESSAGE.toCharArray(),
			POISON_PILL_MESSAGE.length());

	public static boolean compareToPoison(char[] cBuf) {
		return String.valueOf(cBuf).equals(String.valueOf(POISON_PILL_MESSAGE));
	}

	public ThreadController(Cipher cipher) {
		this.cipher = cipher;
	}

	public String encrypt(String input_filename, String output_filename) {

		source_read_queue = new ArrayBlockingQueue<CharBlock>(queueCapacity);
		encrypt_queue = new ArrayBlockingQueue<CharBlock>(queueCapacity);

		// read the input into the read_queue
		ThreadedReader source_reader = new ThreadedReader(source_read_queue, input_filename);
		ThreadedEncrypter encrypter = new ThreadedEncrypter(source_read_queue, encrypt_queue, cipher);
		ThreadedWriter encrypted_writer = new ThreadedWriter(encrypt_queue, output_filename);

		// encrypt
		startTime = System.nanoTime();
		ExecutorService encrypt_service = Executors.newFixedThreadPool(3);
		encrypt_service.submit(new Thread(source_reader));
		encrypt_service.submit(new Thread(encrypter));
		encrypt_service.submit(new Thread(encrypted_writer));
		if (encrypt_service.isShutdown()) {
			System.out.println("ENCRYPTED");
		}
		//
		encrypt_service.shutdown();
		try {
			encrypt_service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		while (true) {
			if (encrypt_service.isTerminated()) {
				duration = (System.nanoTime() - (double) startTime) / 1000000000;
				String msg = ("Total encryption time was: " + duration + "s");
				duration = 0;
				startTime = System.nanoTime();
				return msg;
			}
		}
	}

	public String decrypt(String input_filename, String output_filename) {

		encrypted_read_queue = new ArrayBlockingQueue<CharBlock>(queueCapacity);
		decrypt_queue = new ArrayBlockingQueue<CharBlock>(queueCapacity);

		ThreadedReader encrypted_reader = new ThreadedReader(encrypted_read_queue, input_filename);
		ThreadedDecrypter decrypter = new ThreadedDecrypter(encrypted_read_queue, decrypt_queue, cipher);
		ThreadedWriter decrypted_writer = new ThreadedWriter(decrypt_queue, output_filename);

		startTime = System.nanoTime();
		ExecutorService decrypt_service = Executors.newFixedThreadPool(3);
		// decrypt
		decrypt_service.submit(new Thread(encrypted_reader));
		decrypt_service.submit(new Thread(decrypter));
		decrypt_service.submit(new Thread(decrypted_writer));

		//
		decrypt_service.shutdown();
		try {
			decrypt_service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (true) {
			if (decrypt_service.isTerminated()) {
				duration = (System.nanoTime() - (double) startTime) / 1000000000;
				String msg = ("Total decryption time was: " + duration + "s");
				duration = 0;
				startTime = System.nanoTime();
				return msg;
			}
		}
	}
}
