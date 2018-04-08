# FourSquareFX - https://github.com/SerjiVutinss/FourSquareFX

## Introduction

* A four sqaure cipher implementation created in Java with a JavaFX GUI.
* The application utilises BlockingQueues and multiple Threads for reading/writing files and encrypting/decrypting character arrays.

* For ease of testing, the input and output filename textfields are automatically populated with values, so it is advised to copy the supplied 'resources' folder into the working (or project root) directory.

* The passcode text fields are also populated with default values, however, any combination of passcodes can be used once they pass the required test (more below).

## Operation

1. Run the application from source.
2. Enter a pair of keys in the Passcode#1 and Passcode#2 textfields.
3. Click 'Test Keys' and ensure that the keys have been accepted by checking the Output textarea.
4. If the keys have been rejected, the 'Cipher Set' label should read FALSE and another key pair should be tried.
5. If the keys have been accepted, the 'Cipher Set' label should read TRUE and encryption/decryption are now possible.
6. The following applies for both Encryption and Decryption:
	1. Ensure that the InputFile textfield is populated - the InputFile textfield MUST contain a valid filepath for encryption or decryption to work, i.e. a file must already exist at that path.
	2. The OutputFile textfields must contain a valid filepath or a path which is writable, i.e. if a file does not exist at this path, the application will attempt to create it.  
	3. Check the OutputArea for feedback.
7. It is advised to check the integrity of the encryption by comparing the plaintext source file and the plaintext decrypted file from the command line or terminal:
	1. In Linux: 'diff source.txt decrypted.txt'
	2. In Windows: 'FC source.txt decrypted.txt'
