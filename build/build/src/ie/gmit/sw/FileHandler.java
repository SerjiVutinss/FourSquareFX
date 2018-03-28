package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
//import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;


// all exceptions will be caught in UI
public class FileHandler {
	
	private static Charset UTF8 = Charset.forName("UTF-8");

	public static StringBuilder readFileUTF8(String filename, boolean isClearText) throws IOException {

		Reader reader = new InputStreamReader(new FileInputStream(filename), UTF8);

		StringBuilder sb = new StringBuilder();

		// BufferedReader br = new BufferedReader(new FileReader(filename));
		try {
			int i = reader.read();

			// reading in as stringbuilder

			// change this to read pairs of chars
			while (i != -1) {
				sb.append((char) i);
				// DO NOT add NewLine characters to the encrypted text as this will cause
				// decryption discrepancies
				if (isClearText) {
					// sb.append(System.lineSeparator());
				}
				i = reader.read();
			}

			// remove the last new line from the end of the sb
			// sb.replace(sb.length() - 1, sb.length(), "");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			reader.close();
		}

		return sb;
	}

	public static void outputToFileUTF8(StringBuilder sb, String filename) throws IOException {
		// File file = new File(filename);

		Writer writer = new OutputStreamWriter(new FileOutputStream(filename), UTF8);

		writer.write(sb.toString());
		if (writer != null)
			writer.close();
	}

	public static void outputToFile(StringBuilder sb, String filename) throws IOException {
		File file = new File(filename);
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(file));

		// remove any new lines from the end of the file

		writer.write(sb.toString());
		if (writer != null)
			writer.close();
	}

	public static void outputToFile(StringBuilder sb, File file) throws IOException {

		FileHandler.outputToFile(sb, file.getAbsolutePath());

	}

	public static StringBuilder readFile(String filename, boolean isClearText) throws IOException {

		StringBuilder sb = new StringBuilder();

		BufferedReader br = new BufferedReader(new FileReader(filename));
		try {
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				// DO NOT add NewLine characters to the encrypted text as this will cause
				// decryption discrepancies
				if (isClearText) {
					sb.append(System.lineSeparator());
				}
				line = br.readLine();
			}

			// remove the last new line from the end of the sb
			// sb.replace(sb.length() - 1, sb.length(), "");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			br.close();
		}

		return sb;
	}

	public static StringBuilder readFile(File file, boolean isClearText) throws IOException {
		return FileHandler.readFile(file.getAbsolutePath(), isClearText);
	}
	
	// public static void outputToFile(StringBuilder sb, String filename) throws
	// IOException {
	// PrintWriter out = new PrintWriter(new FileWriter(filename));
	// out.print(sb.toString().getBytes());
	// out.close();
	// }

	// public static void outputToFile(StringBuilder sb, String filename) throws
	// IOException {
	// // final StringBuilder aSB = ...;
	// final int aLength = sb.length();
	// final int aChunk = 1024;
	// final char[] aChars = new char[aChunk];
	//
	// File file = new File(filename);
	// BufferedWriter writer = null;
	// try {
	// writer = new BufferedWriter(new FileWriter(file));
	// // writer.write(sb.toString());
	//
	// for (int aPosStart = 0; aPosStart < aLength; aPosStart += aChunk) {
	// final int aPosEnd = Math.min(aPosStart + aChunk, aLength);
	// sb.getChars(aPosStart, aPosEnd, aChars, 0); // Create no new buffer
	// final CharArrayReader aCARead = new CharArrayReader(aChars); // Create no new
	// buffer
	//
	// // This may be slow but it will not create any more buffer (for bytes)
	// int aByte;
	// while ((aByte = aCARead.read()) != -1)
	// writer.write(aByte);
	// }
	// } finally {
	// if (writer != null)
	// writer.close();
	// }
	// }

}