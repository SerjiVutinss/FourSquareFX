package ie.gmit.sw;

import java.io.File;
import java.io.IOException;

import ie.gmit.sw.cipher.Cipher;
import ie.gmit.sw.threads.ThreadController;

//import ie.gmit.sw.gui.MainWindow;

public class Runner {

	static double startTime, endTime, duration, totalTime;
	private static Cipher cipher;
	
	
	static String dir = "/media/ramdisk";

	static String resDir = dir + File.separatorChar + "resources" + File.separatorChar;
	static String input_filename = resDir + "WarAndPeace-LeoTolstoy.txt";
	static String encrypted_file = resDir + "encrypted.txt";
	static String decrypted_file = resDir + "decrypted.txt";

	public static void main(String[] args) throws Exception {

		// display the JavaFX window
//		MainWindow.showGui();
		
		long keyOne = 72372131;
		long keyTwo = 2798721;
		try {
			cipher = new Cipher(keyOne, keyTwo, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(cipher.testKeys()) {
			System.out.println("Keys Accepted");
			ThreadController tc = new ThreadController(cipher);
			tc.encrypt(input_filename, encrypted_file);
			tc.decrypt(encrypted_file, decrypted_file);
		} else {
			System.out.println("Keys Rejected");
		}


	}

}
