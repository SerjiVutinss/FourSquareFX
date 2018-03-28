package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import ie.gmit.sw.cipher.Cipher;

public class LineTest {

	private static Charset UTF8 = Charset.forName("UTF-8");

	static long keyOne = 1976565486;
	static long keyTwo = 318326476;
	static Cipher cipher;

	static final String dir = System.getProperty("user.dir");

	static String resDir = dir + File.separatorChar + "resources" + File.separatorChar;

	static String input_filename = resDir + "WarAndPeace-LeoTolstoy.txt";
	// static String input_filename = resDir + "WPx10.txt";
	// static String input_filename = resDir + "sample_text.txt";

	// static String input_filename = resDir + "DeBelloGallico.txt";

	// static String input_filename = resDir + "PoblachtNaHEireann.txt";

	static String file_to_encrypt = resDir + input_filename;

	static String encrypted_file = resDir + "encrypted.txt";
	static String decrypted_file = resDir + "decrypted.txt";

	static int chunkSize = 768;

	public static void testFly() {

		try {
			cipher = new Cipher(1234, 5678, true);

			String testString = "hellos";
			System.out.println("Test String: " + testString);
			String encryptedString = String.valueOf(cipher.encrypt(testString.toCharArray(), testString.length()));
			System.out.println("Encrypted String: " + encryptedString);
			String decryptedString = String.valueOf(cipher.decrypt(encryptedString.toCharArray(), testString.length()));
			System.out.println("Decrypted String: " + decryptedString);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testEncryptByLine() {
		try {

			long startTime, endTime;
			double duration, totalTime = 0;
			cipher = new Cipher(1234, 5678, true);

			startTime = System.nanoTime();
			encryptByLine(input_filename, encrypted_file);
			endTime = System.nanoTime() - startTime;
			duration = (double) endTime / 1000000000;
			totalTime += duration;
			System.out.println("Read, encrypted and written in: " + duration);
			startTime = System.nanoTime();
			decryptByLine(encrypted_file, decrypted_file);
			endTime = System.nanoTime() - startTime;
			duration = (double) endTime / 1000000000;
			totalTime += duration;
			System.out.println("Read, decrypted and written in: " + duration);
			System.out.println("Total Time: " + totalTime);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void encryptByLine(String input_filename, String output_filename) throws IOException {

		FileInputStream is = new FileInputStream(input_filename);
		BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF8"));
		Writer writer = new OutputStreamWriter(new FileOutputStream(output_filename, false), UTF8);

		int c = 0;
		char charBuf[] = new char[chunkSize];

		while ((c = in.read(charBuf, 0, chunkSize)) != -1) {

			writer.write(cipher.encrypt(charBuf, c));
			charBuf = new char[chunkSize];
		}
		writer.close();

		in.close();

		System.out.println("Done");

	}

	public static void decryptByLine(String input_filename, String output_filename) throws IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(input_filename), "UTF8"));
		Writer writer = new OutputStreamWriter(new FileOutputStream(output_filename, false), UTF8);

		char charBuf[] = new char[chunkSize];
		int c = 0;
		while ((c = in.read(charBuf, 0, chunkSize)) != -1) {

			writer.write(cipher.decrypt(charBuf, c));
		}
		writer.close();

		in.close();

		System.out.println("Done");
	}
}
