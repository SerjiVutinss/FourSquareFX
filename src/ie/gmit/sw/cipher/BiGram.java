package ie.gmit.sw.cipher;

public class BiGram {

	public static int[][] bigramCharArray(char charArray[], int length) {

		int[][] returnArray;
		boolean evenNumChars;

		// if there is an even amount of chars

		evenNumChars = (length % 2 == 0) ? true : false;
		
		if(evenNumChars) {
			returnArray = new int[length / 2][2];
//			System.out.println("Even Array length: " + returnArray.length);
		} else {
			returnArray = new int[(length + 1) / 2][2];
//			System.out.println("Uneven Array length: " + returnArray.length);
		}
		
		
		int count = 0;
		for (int i = 0; i < length; i += 2) {
			if (((int) charArray[i] != 0) && ((int) charArray[i + 1] != 0)) {
				returnArray[count][0] = charArray[i];
				// if (evenNumChars) {
				returnArray[count][1] = charArray[i + 1];
				// }
			}
			count++;
		}
		
//		System.out.println((char)returnArray[returnArray.length-1][1]);

		if (!evenNumChars) {
			System.out.println("Adding PAD");
			returnArray[returnArray.length - 1][1] = (int) '`';
		}

//		System.out.println("RetArraySize: " + returnArray.length);

		return returnArray;

	}
}
