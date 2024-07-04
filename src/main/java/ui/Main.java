package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.SystemInfo;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        if (SystemInfo.isMacOS && System.getProperty("apple.laf.useScreenMenuBar") == null)
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("org.apache.commons.collections.enableUnsafeSerialization", "true");
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("MemShellGene Alpha");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1190, 600));
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
