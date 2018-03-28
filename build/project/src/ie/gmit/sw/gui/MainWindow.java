package ie.gmit.sw.gui;

import java.io.IOException;

import ie.gmit.sw.Runner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {
	public static void showGui() {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws IOException {

		// setup a stage
		primaryStage.setTitle("Four Square Cipher");

		// load the MainWindow FXML file from ./resources
		// URL url = new File("src/ie/gmit/sw/gui/MainWindow.fxml").toURI().toURL();
		// Parent root = FXMLLoader.load(url);

		FXMLLoader loader = new FXMLLoader(Runner.class.getResource("gui/MainWindow.fxml"));

		// Parent root = FXMLLoader.load(url);
		Parent root = loader.load();

		// set and show the scene in the stage
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}
