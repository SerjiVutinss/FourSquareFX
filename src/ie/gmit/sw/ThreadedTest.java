package ie.gmit.sw;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ie.gmit.sw.cipher.Cipher;
import ie.gmit.sw.threads.CharBlock;
import ie.gmit.sw.threads.ThreadedDecrypter;
import ie.gmit.sw.threads.ThreadedEncrypter;
import ie.gmit.sw.threads.ThreadedReader;
import ie.gmit.sw.threads.ThreadedWriter;

public class ThreadedTest {

	private static final String POISON_PILL_MESSAGE = "EOF";
	public static final CharBlock PILL_BLOCK = new CharBlock(ThreadedTest.POISON_PILL_MESSAGE.toCharArray(),
			ThreadedTest.POISON_PILL_MESSAGE.length());

	private static BlockingQueue<CharBlock> source_read_queue = null;
	private static BlockingQueue<CharBlock> encrypted_read_queue = null;
	private static BlockingQueue<CharBlock> encrypt_queue = null;
	private static BlockingQueue<CharBlock> decrypt_queue = null;

	private static int queueCapacity = 10;

	private static Charset UTF8 = Charset.forName("UTF-8");

	static long keyOne = 1976565486;
	static long keyTwo = 318326476;

	static String passKeyOne, passKeyTwo;
	static Cipher cipher;

//	 static final String dir = System.getProperty("user.dir");
	static final String dir = "R:/";

	static String resDir = dir + File.separatorChar + "resources" + File.separatorChar;

	static String input_filename = resDir + "WarAndPeace-LeoTolstoy.txt";
//	 static String input_filename = resDir + "sample_text.txt";

	static String file_to_encrypt = resDir + input_filename;

	static String encrypted_file = resDir + "encrypted.txt";
	static String decrypted_file = resDir + "decrypted.txt";

	// static int chunkSize = 1024;

	public static boolean compareToPoison(char[] cBuf) {

		return String.valueOf(cBuf).equals(String.valueOf(ThreadedTest.POISON_PILL_MESSAGE));
	}

	public static void testOne() {

		source_read_queue = new ArrayBlockingQueue<CharBlock>(queueCapacity);
		encrypt_queue = new ArrayBlockingQueue<CharBlock>(queueCapacity);

		encrypted_read_queue = new ArrayBlockingQueue<CharBlock>(queueCapacity);
		decrypt_queue = new ArrayBlockingQueue<CharBlock>(queueCapacity);

		long startTime, endTime;
		double duration, totalTime = 0;

		passKeyOne = "hhhhhhhh";
		passKeyTwo = "kkkkkkgk";

		long keyOne = Math.abs(passKeyOne.hashCode());
		long keyTwo = Math.abs(passKeyTwo.hashCode());

		System.out.println("Key 1: " + keyOne);
		System.out.println("Key 2: " + keyTwo);

		try {
			cipher = new Cipher(passKeyOne.hashCode(), passKeyTwo.hashCode(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		testKeys(cipher);

		// read the input into the read_queue
		ThreadedReader source_reader = new ThreadedReader(source_read_queue, input_filename, "clear");
		ThreadedEncrypter encrypter = new ThreadedEncrypter(source_read_queue, encrypt_queue, cipher);
		ThreadedWriter encrypted_writer = new ThreadedWriter(encrypt_queue, encrypted_file, "encrypted");

		ThreadedReader encrypted_reader = new ThreadedReader(encrypted_read_queue, encrypted_file, "encrypted");
		ThreadedDecrypter decrypter = new ThreadedDecrypter(encrypted_read_queue, decrypt_queue, cipher);
		ThreadedWriter decrypted_writer = new ThreadedWriter(decrypt_queue, decrypted_file, "decrypted");

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

		if (encrypt_service.isTerminated()) {
			duration = (System.nanoTime() - (double) startTime) / 1000000000;
			System.out.println("Total encryption time was: " + duration + "s");
			totalTime += duration;
			duration = 0;
			startTime = System.nanoTime();
		}

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

		if (decrypt_service.isTerminated()) {
			duration = (System.nanoTime() - (double) startTime) / 1000000000;
			System.out.println("Total decryption time was: " + duration + "s");
			totalTime += duration;
			duration = 0;
			startTime = System.nanoTime();
		}
		System.out.println("Total time was: " + totalTime + "s");

	}

	private static void testKeys(Cipher cipher2) {
		StringBuilder sb = new StringBuilder();
		
		double startChar = 10;
		double squareSize = 16;
		double trimChars = 15;
		int totalChars = (int)(squareSize*squareSize);
		
//		for (int i = (int)startChar; i < ((int)squareSize * (int)trimChars); i++) {
//			sb.append((char) i);
//		}
		
//		for (int i = 0; i < (int)(squareSize*squareSize); i++) {
//			sb.append((char) i);
//		}
		sb.append("!\\#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçå¹êëìíîïðñòóôõö÷øùúûüýþÿ");

		char charsToEncrypt[] = sb.toString().toCharArray();
		char encryptedString[] = cipher.encrypt(charsToEncrypt, charsToEncrypt.length);
		char decryptedString[] = cipher.decrypt(encryptedString, encryptedString.length);

		System.out.println(charsToEncrypt);
		System.out.println(decryptedString);

		if (Arrays.equals(charsToEncrypt, decryptedString)) {
			System.out.println("Passed");
		} else {
			System.out.println("Failed");
		}

		double accuracy = ((double)sb.length() / (squareSize * squareSize)*100);
		
		DecimalFormat df = new DecimalFormat("####0.00");
		System.out.println("Accuracy: " + df.format(accuracy) + "% of " + totalChars + " characters");
		// System.out.println(sb.toString());

	}

}
