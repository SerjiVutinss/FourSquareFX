package ie.gmit.sw.gui;

import java.io.File;
import java.io.IOException;

import ie.gmit.sw.FileHandler;
import ie.gmit.sw.cipher.Cipher;
import ie.gmit.sw.cipher.KeyGenerator;
import ie.gmit.sw.threads.ThreadController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindowController {

	// FXML components

	Stage stage;

	// start FXML nodes
	@FXML
	private AnchorPane ap;

	@FXML
	private Label lblPasscodeOne, lblPasscodeTwo;
	@FXML
	private TextField tfPasscodeOne, tfPasscodeTwo, tfInputFile, tfOutputFile;
	@FXML
	private TextArea taOutput;

	@FXML
	private Button btnGenerateCipher, btnChooseInput, btnChooseOutput, btnEncrypt, btnDecrypt, btnTestKeys;
	// end FXML nodes

	// the Cipher object which will be generated using the passcodes
	Cipher cipher = null;

	// objects to hold the input and output files or file paths
	File input_file, output_file;
	String default_input_filename, default_output_filename;

	// these will be used to time certain operations
	double startTime, endTime, duration;
	boolean enableEncryption = false;

	// no constructor used, using initialize() instead so that all nodes are
	// instantiated

	// add got focus and lost focus listeners to the file text fields
	@FXML
	private void initialize() {
		updateUI();

		// input file text field on/lost focus
		tfInputFile.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				updateUI();
			}
		});

		// output file text field on/lost focus
		tfOutputFile.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				updateUI();
			}
		});

		// output file text field on/lost focus
		tfPasscodeOne.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				updateUI();
			}
		});

		// output file text field on/lost focus
		tfPasscodeTwo.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				updateUI();
			}
		});

		tfPasscodeOne.setText("hello");
		tfPasscodeTwo.setText("world");

		tfInputFile.setText("/media/ramdisk/resources/WarAndPeace-LeoTolstoy.txt");
		tfOutputFile.setText("/media/ramdisk/resources/encrypted.txt");
	}

	// check that keys are valid - generate and set a cipher if so
	@FXML
	protected void testKeys(ActionEvent ev) {

		long keyOne, keyTwo;
		boolean enableEncrypt = false;

		// get keys from keygen
		// keyOne = KeyGenerator.passcodeToKey(tfPasscodeOne.getText());
		// keyTwo = KeyGenerator.passcodeToKey(tfPasscodeTwo.getText());

		keyOne = tfPasscodeOne.getText().hashCode();
		keyTwo = tfPasscodeTwo.getText().hashCode();

		// long keyOne = 23;
		// long keyTwo = 24;
		try {
			cipher = new Cipher(keyOne, keyTwo, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		enableEncryption = cipher.testKeys();
		if (enableEncryption) {
			taOutput.appendText("\nKeys Accepted");
			taOutput.appendText("\nCipher Created");
			btnGenerateCipher.setDisable(!enableEncryption);

		} else {
			taOutput.appendText("\nKeys Rejected, please try another pair");
		}

		updateUI();
	}

	// check that keys are valid - generate and set a cipher if so
	@FXML
	protected void generateCipher(ActionEvent ev) {

		long keyOne, keyTwo;

		// get keys from keygen
		// keyOne = KeyGenerator.passcodeToKey(tfPasscodeOne.getText());
		// keyTwo = KeyGenerator.passcodeToKey(tfPasscodeTwo.getText());
		keyOne = tfPasscodeOne.getText().hashCode();
		keyTwo = tfPasscodeTwo.getText().hashCode();

		// check that keys are valid and generate/set cipher
		if (KeyGenerator.keyIsValid(keyOne) && KeyGenerator.keyIsValid(keyTwo)) {
			taOutput.appendText("\nPassscodes accepted");
			taOutput.appendText("\nPassscode 1: " + keyOne);
			taOutput.appendText("\nPassscode 2: " + keyTwo);
			try {
				startTime = System.nanoTime();
				taOutput.appendText("\nBuilding Cipher...");
				cipher = new Cipher(keyOne, keyTwo, true);
				endTime = System.nanoTime();
				duration = (endTime - startTime) / 1000000000;
				taOutput.appendText("\nCipher Built in " + duration + " seconds");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			taOutput.appendText("\nCipher Created");
		} else {
			taOutput.appendText("\nInvalid Passcodes, try again");
		}

		updateUI();
	}

	// could probably tidy these methods up to reduce code repetition

	// select an input file using the file chooser
	@FXML
	protected void chooseInput(ActionEvent event) {
		// taOutput.appendText("\nChoose file....");
		// select a file, set the File chooser title to the supplied parameter
		input_file = fileChooser("Select input file");

		if (input_file != null) {

			tfInputFile.setText(input_file.getAbsolutePath());

			// taOutput.appendText("\n" + input_file.getAbsolutePath());
			// taOutput.appendText("\n" + input_file.getName());
			updateUI();
		} else {
			taOutput.appendText("\nFile select cancelled!");
		}
	}

	// select an output file using the file chooser
	@FXML
	protected void chooseOutput(ActionEvent event) {
		// taOutput.appendText("\nChoose file....");
		output_file = fileChooser("Select output file");

		if (output_file != null) {
			tfOutputFile.setText(output_file.getAbsolutePath());
			//
			// taOutput.appendText("\n" + output_file.getAbsolutePath());
			// taOutput.appendText("\n" + output_file.getName());
			updateUI();
		} else {
			taOutput.appendText("\nFile select cancelled!");
		}
	}

	// could probably tidy these methods up to reduce code repetition and better
	// exception handling
	// encrypt the input_file/file path and output to output_file/file path
	@FXML
	protected void encryptInput() {

		ThreadController tc = new ThreadController(this.cipher);
		taOutput.appendText("\n" + tc.encrypt(tfInputFile.getText(), tfOutputFile.getText()));
	}

	// could probably tidy these methods up to reduce code repetition and better
	// exception handling
	@FXML
	protected void decryptInput() {
		ThreadController tc = new ThreadController(cipher);
		taOutput.appendText("\n" + tc.decrypt(tfInputFile.getText(), tfOutputFile.getText()));
	}

	// check that either the files have been set, or the files exist at the given
	// paths
	private boolean checkFiles() {
		if (input_file == null) {
			try {
				input_file = new File(tfInputFile.getText());
				if (!input_file.exists()) {
					throw new Exception();
				}
			} catch (Exception e) {
				taOutput.appendText("\nInput file not found");
				return false;
			}
		} else if (output_file == null) {
			try {
				output_file = new File(tfOutputFile.getText());
				if (!output_file.exists()) {

					throw new Exception();
				}
			} catch (Exception e) {
				taOutput.appendText("\nOutput file not found");
				return false;
			}
		}
		return true;
	}

	// write to the output file
	@FXML
	protected void writeToFile(ActionEvent evt) {

		StringBuilder sb = new StringBuilder();

		try {
			FileHandler.outputToFile(sb, output_file);
		} catch (IOException e) {
			handleError("\nError: Output file cannot be written to");
		}
	}

	// open a file chooser dialog - called from FXML button methods
	private File fileChooser(String title) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		return fileChooser.showOpenDialog(null);
	}

	// refresh the UI, in particular enable or disable the Encrypt/Decrypt buttons
	private void updateUI() {

		// disable encryption if the cipher has not been set
		// OR if the input_file AND input_file path have not been set
		// OR if the output_file AND output_file path have not been set
		boolean disableEncryption = ((cipher == null) || ((output_file == null) && (!enableEncryption))
				|| ((input_file == null) && (!enableEncryption)));
		btnEncrypt.setDisable(disableEncryption);
		btnDecrypt.setDisable(disableEncryption);

//		boolean enableCipherCreate = (tfPasscodeOne.getText().length() >= 8 && tfPasscodeTwo.getText().length() >= 8);

//		btnGenerateCipher.setDisable(!enableCipherCreate);
	}

	// called after encrypting or decrypting - resets the files and file paths
	private void resetFiles() {

		input_file = null;
		output_file = null;
		tfInputFile.setText("");
		tfOutputFile.setText("");
		updateUI();
	}

	// simple error handler to append text to the text area
	private void handleError(String error) {
		taOutput.appendText("\n!!!Error: " + error + "!!!");
	}
}
