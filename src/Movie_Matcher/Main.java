package Movie_Matcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Scanner;

public class Main extends Application {
    public static String pass;
    @Override
    public void start(Stage primaryStage) throws Exception{
        PickerRunner.main(null);
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setTitle("Movie Matcher");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Whats your MySQL Root Password?: ");
        pass = scan.next();
        launch(args);
    }
}
