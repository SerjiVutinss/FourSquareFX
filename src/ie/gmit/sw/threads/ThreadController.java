package ie.gmit.sw.threads;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ie.gmit.sw.Runner;
import ie.gmit.sw.cipher.Cipher;

public class ThreadController {

	static long startTime;
	static double duration, totalTime = 0;

	static long keyOne = 98273821;
	long keyTwo = 926372565;

	static String passKeyOne, passKeyTwo;
	Cipher cipher = null;

//	// static final String dir = System.getProperty("user.dir");
//	static String dir = "/media/ramdisk";
//
//	static String resDir = dir + File.separatorChar + "resources" + File.separatorChar;
//
//	static String input_filename = resDir + "WarAndPeace-LeoTolstoy.txt";
//	// static String input_filename = resDir + "sample_text.txt";
//
//	static String file_to_encrypt = resDir + input_filename;
//
//	static String encrypted_file = resDir + "encrypted.txt";
//	static String decrypted_file = resDir + "decrypted.txt";

	private static BlockingQueue<CharBlock> source_read_queue = null;
	private static BlockingQueue<CharBlock> encrypted_read_queue = null;
	private static BlockingQueue<CharBlock> encrypt_queue = null;
	private static BlockingQueue<CharBlock> decrypt_queue = null;

	private static int queueCapacity = 10;

	public ThreadController(Cipher cipher) {
		this.cipher = cipher;
	}

	public String encrypt(String input_filename, String output_filename) {

		source_read_queue = new ArrayBlockingQueue<CharBlock>(queueCapacity);
		encrypt_queue = new ArrayBlockingQueue<CharBlock>(queueCapacity);

		// read the input into the read_queue
		ThreadedReader source_reader = new ThreadedReader(source_read_queue, input_filename, "clear");
		ThreadedEncrypter encrypter = new ThreadedEncrypter(source_read_queue, encrypt_queue, cipher);
		ThreadedWriter encrypted_writer = new ThreadedWriter(encrypt_queue, output_filename, "encrypted");

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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (true) {
			if (encrypt_service.isTerminated()) {
				duration = (System.nanoTime() - (double) startTime) / 1000000000;
				String  msg = ("Total encryption time was: " + duration + "s");
				Runner.totalTime += duration;
				duration = 0;
				startTime = System.nanoTime();
				return msg;
			}
		}
	}

	public String decrypt(String input_filename, String output_filename) {

		encrypted_read_queue = new ArrayBlockingQueue<CharBlock>(queueCapacity);
		decrypt_queue = new ArrayBlockingQueue<CharBlock>(queueCapacity);

		ThreadedReader encrypted_reader = new ThreadedReader(encrypted_read_queue, input_filename, "encrypted");
		ThreadedDecrypter decrypter = new ThreadedDecrypter(encrypted_read_queue, decrypt_queue, cipher);
		ThreadedWriter decrypted_writer = new ThreadedWriter(decrypt_queue, output_filename, "decrypted");

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
				String  msg = ("Total decryption time was: " + duration + "s");
				Runner.totalTime += duration;
				duration = 0;
				startTime = System.nanoTime();
				return msg;
			}
		}
	}

	// private static void testKeys(Cipher cipher) {
	// StringBuilder sb = new StringBuilder();
	//
	// // double startChar = 10;
	// double squareSize = 16;
	// // double trimChars = 15;
	// int totalChars = (int) (squareSize * squareSize);
	// sb.append(
	// "
	// !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ");
	//
	// char charsToEncrypt[] = sb.toString().toCharArray();
	// char encryptedString[] = cipher.encrypt(charsToEncrypt,
	// charsToEncrypt.length);
	// char decryptedString[] = cipher.decrypt(encryptedString,
	// encryptedString.length);
	//
	// System.out.println(charsToEncrypt);
	// System.out.println(decryptedString);
	//
	// if (Arrays.equals(charsToEncrypt, decryptedString)) {
	// System.out.println("Passed");
	// } else {
	// System.out.println("Failed");
	// }
	//
	// double accuracy = ((double) sb.length() / (squareSize * squareSize) * 100);
	//
	// DecimalFormat df = new DecimalFormat("####0.00");
	// System.out.println(
	// "Accuracy: " + df.format(accuracy) + "% (" + sb.length() + ") of " +
	// totalChars + " characters");
	// }
}
