//package ie.gmit.sw;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.charset.Charset;
//
//import ie.gmit.sw.cipher.Cipher;
//
//public class Test {
//
//	private static Charset UTF8 = Charset.forName("UTF-8");
//
//	static long keyOne = 654845;
//	static long keyTwo = 3297151;
//	static Cipher cipher;
//
//	static final String dir = System.getProperty("user.dir");
//
//	static String resDir = dir + File.separatorChar + "resources" + File.separatorChar;
//
//	static String input_filename = "WarAndPeace-LeoTolstoy.txt";
//	// static String input_filename = "sample_text.txt";
//
//	// String input_filename = "PoblachtNaHEireann.txt";
//
//	static String file_to_encrypt = resDir + input_filename;
//
//	static String encrypted_file = resDir + "encrypted.txt";
//	static String decrypted_file = resDir + "decrypted.txt";
//
//	static int chunkSize = 1024;
//
//	public static void testEncryptByLine() {
//		try {
//			encryptByLine(input_filename, encrypted_file);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	// public static void testEncryptChunk() {
//	//
//	// long startTime, endTime;
//	// double duration, totalTime = 0;
//	//
//	// try {
//	// cipher = new Cipher(9874, 5678, true);
//	//
//	// // stream encryption
//	// startTime = System.nanoTime();
//	// encryptByLine(file_to_encrypt, encrypted_file);
//	// endTime = System.nanoTime() - startTime;
//	// duration = (double) endTime / 1000000000;
//	// totalTime += duration;
//	// System.out.println("Encrypted chunks in: " + duration);
//	//
//	// // stream decryption
//	// startTime = System.nanoTime();
//	// chunkDecrypt(encrypted_file, decrypted_file);
//	// endTime = System.nanoTime() - startTime;
//	// duration = (double) endTime / 1000000000;
//	// totalTime += duration;
//	// System.out.println("Decrypted chunks in: " + duration);
//	//
//	// // chunkEncrypt(file_to_encrypt, encrypted_file);
//	//
//	// } catch (IOException e) {
//	// // TODO Auto-generated catch block
//	// e.printStackTrace();
//	// }
//	// }
//
//	public static void encryptByLine(String input_filename, String output_filename) throws IOException {
//		FileInputStream is = new FileInputStream(input_filename);
//
//		// reinitialise the blank file
//		FileWriter fwOb = new FileWriter(output_filename, false);
//		PrintWriter pwOb = new PrintWriter(fwOb, false);
//		pwOb.flush();
//		pwOb.close();
//		fwOb.close();
//
//		// open the stream in append mode
//		FileOutputStream os = new FileOutputStream(output_filename, true);
//
////		StringBuilder sb = new StringBuilder();
//
//		// int chunkSize = 32694;
//		byte[] bytes = new byte[chunkSize];
//		int bytesRead = 0;
////		int count = 0;
//		while ((bytesRead = is.read(bytes, 0, chunkSize)) != -1) {
//			// do something with bytes array here
//
////			sb = new StringBuilder();
//			// byte[] encryptedChunk = new byte[chunkSize];
//			String str = new String(bytes, UTF8);
////			sb.append(str);
//
//			// if byte array is even length - so not last chunk
//			// if (bytes.length % 2 == 0) {
//			// for (int i = 0; i < bytes.length; i += 2) {
//			// byte bigram[] = new byte[2];
//			// System.out.println(bytes.toString());
//
//			// sb.append((char)bytes[i]);
//
//			// bigram[0] = bytes[i];
//			// bigram[1] = bytes[i + 1];
//			//// System.out.println((char)bigram[0] + ":" + (char)bigram[1]);
//			// byte encryptedBytes[] = cipher.encrypt(bigram);
//			//
//			// encryptedChunk[i] = encryptedBytes[0];
//			// encryptedChunk[i + 1] = encryptedBytes[1];
//
//			// }
//			// }
//			os.write(str.toString().getBytes());
////			sb = null;
//		}
//		System.out.println("Done");
//	}
//
//	// public static void chunkEncrypt(String input_filename, String
//	// output_filename) throws IOException {
//	// FileInputStream is = new FileInputStream(input_filename);
//	//
//	// // reinitialise the blank file
//	// FileWriter fwOb = new FileWriter(output_filename, false);
//	// PrintWriter pwOb = new PrintWriter(fwOb, false);
//	// pwOb.flush();
//	// pwOb.close();
//	// fwOb.close();
//	//
//	// // open the stream in append mode
//	// FileOutputStream os = new FileOutputStream(output_filename, true);
//	//
//	// // int chunkSize = 32694;
//	// byte[] bytes = new byte[chunkSize];
//	// int bytesRead = 0;
//	// int count = 0;
//	// while ((bytesRead = is.read(bytes, 0, chunkSize)) != -1) {
//	// // do something with bytes array here
//	//
//	// byte[] encryptedChunk = new byte[chunkSize];
//	//
//	// // if byte array is even length - so not last chunk
//	// // if (bytes.length % 2 == 0) {
//	// for (int i = 0; i < bytes.length; i += 2) {
//	// byte bigram[] = new byte[2];
//	// bigram[0] = bytes[i];
//	// bigram[1] = bytes[i + 1];
//	// // System.out.println((char)bigram[0] + ":" + (char)bigram[1]);
//	// byte encryptedBytes[] = cipher.encrypt(bigram);
//	//
//	// encryptedChunk[i] = encryptedBytes[0];
//	// encryptedChunk[i + 1] = encryptedBytes[1];
//	//
//	// // }
//	// }
//	//
//	// // write the encrypted chunk
//	// os.write(encryptedChunk);
//	//
//	// // encrypt the byte array
//	// count++;
//	// }
//	// System.out.println(count);
//	// }
//	//
//	// public static void chunkDecrypt(String input_filename, String
//	// output_filename) throws IOException {
//	// FileInputStream is = new FileInputStream(input_filename);
//	//
//	// // reinitialise the blank file
//	// FileWriter fwOb = new FileWriter(output_filename, false);
//	// PrintWriter pwOb = new PrintWriter(fwOb, false);
//	// pwOb.flush();
//	// pwOb.close();
//	// fwOb.close();
//	//
//	// // open the stream in append mode
//	// FileOutputStream os = new FileOutputStream(output_filename, true);
//	//
//	// int chunkSize = 32694;
//	// byte[] bytes = new byte[chunkSize];
//	// int bytesRead = 0;
//	// int count = 0;
//	// while ((bytesRead = is.read(bytes, 0, chunkSize)) != -1) {
//	// // do something with bytes array here
//	//
//	// byte[] decryptedChunk = new byte[chunkSize];
//	//
//	// // if byte array is even length - so not last chunk
//	// if (bytes.length % 2 == 0) {
//	// for (int i = 0; i < bytes.length; i += 2) {
//	// byte bigram[] = new byte[2];
//	// bigram[0] = bytes[i];
//	// bigram[1] = bytes[i + 1];
//	//
//	// System.out.println((int) (char) bigram[0] + ":" + (int) (char) bigram[1]);
//	// byte decryptedBytes[] = cipher.decrypt(bigram);
//	//
//	// decryptedChunk[i] = decryptedBytes[0];
//	// decryptedChunk[i + 1] = decryptedBytes[1];
//	//
//	// }
//	// }
//	//
//	// // write the encrypted chunk
//	// os.write(decryptedChunk);
//	//
//	// // encrypt the byte array
//	// count++;
//	// }
//	// System.out.println(count);
//	// }
//	//
//	// public static void encryptDecryptEntireFile() {
//	//
//	// long startTime, endTime;
//	// double duration, totalTime = 0;
//	//
//	// String s = "helloworld";
//	// try {
//	// cipher = new FourSquareCipher(1234, 5678, true);
//	//
//	// startTime = System.nanoTime();
//	// cipher.encryptFile(file_to_encrypt, encrypted_file);
//	// endTime = System.nanoTime() - startTime;
//	// duration = (double) endTime / 1000000000;
//	// totalTime += duration;
//	// System.out.println("Read and encrypted in: " + duration);
//	// startTime = System.nanoTime();
//	// cipher.decryptFile(encrypted_file, decrypted_file);
//	// endTime = System.nanoTime() - startTime;
//	// duration = (double) endTime / 1000000000;
//	// totalTime += duration;
//	// System.out.println("Read and decrypted in: " + duration);
//	// System.out.println("Total Time: " + totalTime);
//	//
//	// } catch (IOException e) {
//	// // TODO Auto-generated catch block
//	// e.printStackTrace();
//	// }
//	// }
//	//
//	// public static void testStreamReader() {
//	// try {
//	// StringBuilder sb = FileHandler.readFileUTF8(file_to_encrypt, true);
//	// System.out.println(sb.length());
//	//
//	// FileHandler.outputToFileUTF8(sb, decrypted_file);
//	// } catch (IOException e) {
//	// // TODO Auto-generated catch block
//	// e.printStackTrace();
//	// }
//	// }
//}
