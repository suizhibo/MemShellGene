package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import utils.SystemInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        if (SystemInfo.isMacOS && System.getProperty("apple.laf.useScreenMenuBar") == null)
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("org.apache.commons.collections.enableUnsafeSerialization", "true");
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("MemShellGene V1.2");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1190, 600));
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
