package ie.gmit.sw.cipher;

public class KeyGenerator {

	// some way of getting a long value from a string
	public static long passcodeToKey(String passKey) {
		
		return passKey.hashCode();
		
//		long keyValue = 0;
//		for (int i = 0; i < passKey.length(); i++) {
//			keyValue += (long) passKey.charAt(i);
//		}
//		return (long) (keyValue * Math.sqrt(keyValue));
	}

	// check if a key is valid, i.e. not 
	public static boolean keyIsValid(long key) {
		return (key > 0 && key < Long.MAX_VALUE) ? true : false;
	}
}
