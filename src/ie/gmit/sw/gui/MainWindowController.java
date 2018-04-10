package ie.gmit.sw.gui;

import java.io.File;
import java.io.IOException;

import ie.gmit.sw.cipher.Cipher;
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
	private TextField tfPasscodeOne, tfPasscodeTwo, tfEncryptInputFile, tfEncryptOutputFile, tfDecryptInputFile,
			tfDecryptOutputFile;
	@FXML
	private TextArea taOutput;

	@FXML
	private Button btnChooseInput, btnChooseOutput, btnEncrypt, btnDecrypt, btnTestKeys, btnClearOutput;

	@FXML
	private Label lblCipherSet;
	// end FXML nodes

	// the Cipher object which will be generated using the passcodes
	Cipher cipher = null;

	// objects to hold the input and output files or file paths
	File input_file_encrypt, output_file_encrypt, input_file_decrypt, output_file_decrypt;
	String default_input_filename, default_output_filename;

	// these will be used to time certain operations
	double startTime, endTime, duration;
	boolean enableEncryption = false;

	// no constructor used, using initialize() instead so that all nodes are
	// instantiated

	// add got focus and lost focus listeners to the file text fields
	@FXML
	private void initialize() {
		updateUI(-1);

		// input file text field on/lost focus
		tfEncryptInputFile.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				updateUI(0);
			}
		});

		// output file text field on/lost focus
		tfEncryptOutputFile.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				updateUI(0);
			}
		});

		// input file text field on/lost focus
		tfDecryptInputFile.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				updateUI(1);
			}
		});

		// output file text field on/lost focus
		tfDecryptOutputFile.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				updateUI(1);
			}
		});

		// output file text field on/lost focus
		tfPasscodeOne.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				updateUI(-1);
			}
		});

		// output file text field on/lost focus
		tfPasscodeTwo.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				updateUI(-1);
			}
		});

		tfPasscodeOne.setText("hello");
		tfPasscodeTwo.setText("world");

		String dir = System.getProperty("user.dir");
		String resDir = dir + File.separatorChar + "resources" + File.separatorChar;

		tfEncryptInputFile.setText(resDir + "WarAndPeace-LeoTolstoy.txt");
		tfEncryptOutputFile.setText(resDir + "encrypted.txt");
		tfDecryptInputFile.setText(resDir + "encrypted.txt");
		tfDecryptOutputFile.setText(resDir + "decrypted.txt");
		btnEncrypt.setDisable(true);
		btnDecrypt.setDisable(true);
	}

	private void validateFiles(TextField input, TextField output, TextArea outputArea, Button btn, String type) {
		btn.setDisable(true);
		// if fields are not empty
		if (input.getText().length() > 0 && output.getText().length() > 0) {
			File inputF = new File(input.getText());
			File outputF = new File(output.getText());
			if (!inputF.isFile()) {
				outputArea.appendText("\nInput file for " + type + "ion does not exist, cannot " + type);
				btn.setDisable(true);
			} else if (!outputF.isFile()) {
				outputArea.appendText("\nOutput file for " + type + "ion does not exist, trying to create");
				try {
					if (outputF.createNewFile()) {
						outputArea.appendText("\n...Output file for " + type + "ion created");
						btn.setDisable(false);
					}
				} catch (IOException e) {
					outputArea.appendText("\nError: Output file for " + type + "ion could not be created");
					btn.setDisable(true);
				}

			} else {
				btn.setDisable(false);
			}
		} else {
			outputArea.appendText("\nOne or more filenames are empty for " + type + "ion");
			btn.setDisable(true);
		}
	}

	// check that keys are valid - generate and set a cipher if so
	@FXML
	protected void testKeys(ActionEvent ev) {

		long keyOne, keyTwo;
		//boolean enableEncrypt = false;

		keyOne = tfPasscodeOne.getText().hashCode();
		keyTwo = tfPasscodeTwo.getText().hashCode();

		// used for debugging only
//		System.out.println(keyOne);
//		System.out.println(keyTwo);

		cipher = new Cipher(keyOne, keyTwo, true);
		enableEncryption = cipher.testKeys();
		if (enableEncryption) {
			taOutput.appendText("\nKeys Accepted");
			taOutput.appendText("\nCipher Created");
			lblCipherSet.setText("TRUE");

		} else {
			taOutput.appendText("\nKeys Rejected, please try another pair");
			lblCipherSet.setText("FALSE");
			cipher = null;
		}

		updateUI(2);
	}

	// could probably tidy these 4 file chooser methods up to reduce code repetition
	// select an input file using the file chooser
	@FXML
	protected void chooseEncryptInput(ActionEvent event) {
		input_file_encrypt = fileChooser("Select input file for encryption");

		if (input_file_encrypt != null) {

			tfEncryptInputFile.setText(input_file_encrypt.getAbsolutePath());
			updateUI(0);
		} else {
			taOutput.appendText("\nFile select cancelled!");
		}
	}

	// select an output file using the file chooser
	@FXML
	protected void chooseEncryptOutput(ActionEvent event) {
		output_file_encrypt = fileChooser("Select output file for encryption");

		if (output_file_encrypt != null) {
			tfEncryptOutputFile.setText(output_file_encrypt.getAbsolutePath());
			updateUI(0);
		} else {
			taOutput.appendText("\nFile select cancelled!");
		}
	}

	@FXML
	protected void chooseDecryptInput(ActionEvent event) {
		input_file_decrypt = fileChooser("Select input file for decryption");
		if (input_file_decrypt != null) {
			tfDecryptInputFile.setText(input_file_decrypt.getAbsolutePath());
			updateUI(1);
		} else {
			taOutput.appendText("\nFile select cancelled!");
		}
	}

	// select an output file using the file chooser
	@FXML
	protected void chooseDecryptOutput(ActionEvent event) {
		output_file_decrypt = fileChooser("Select output file for decryption");
		if (output_file_decrypt != null) {
			tfDecryptOutputFile.setText(output_file_decrypt.getAbsolutePath());
			updateUI(1);
		} else {
			taOutput.appendText("\nFile select cancelled!");
		}
	}

	// encrypt the input_file/file path and output to output_file/file path
	@FXML
	protected void encryptInput() {

		ThreadController tc = new ThreadController(this.cipher);
		taOutput.appendText("\n" + tc.encrypt(tfEncryptInputFile.getText(), tfEncryptOutputFile.getText()));
	}

	@FXML
	protected void decryptInput() {
		ThreadController tc = new ThreadController(cipher);
		taOutput.appendText("\n" + tc.decrypt(tfDecryptInputFile.getText(), tfDecryptOutputFile.getText()));
	}

	// open a file chooser dialog - called from FXML button methods
	private File fileChooser(String title) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		return fileChooser.showOpenDialog(null);
	}

	// refresh the UI, in particular enable or disable the Encrypt/Decrypt buttons,
	// 0 for encryption, 1 for decryption, 2 for both
	private void updateUI(int type) {
		if (type != -1) {
			if (cipher != null) {
				if (type == 0) {
					validateFiles(tfEncryptInputFile, tfEncryptOutputFile, taOutput, btnEncrypt, "encrypt");
				} else if (type == 1) {
					validateFiles(tfDecryptInputFile, tfDecryptOutputFile, taOutput, btnDecrypt, "decrypt");
				} else {
					validateFiles(tfEncryptInputFile, tfEncryptOutputFile, taOutput, btnEncrypt, "encrypt");
					validateFiles(tfDecryptInputFile, tfDecryptOutputFile, taOutput, btnDecrypt, "decrypt");
				}
			}
		}
	}
	
	@FXML
	protected void clearOutput() {
		taOutput.clear();
	}

	// simple error handler to append text to the text area
	public void handleError(String error) {
		taOutput.appendText("\n!!!Error: " + error + "!!!");
	}
}
