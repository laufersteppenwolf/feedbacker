package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;

public class Main extends Application {

    public static int counterJa = 0;
    public static int counterNein = 0;
    public static boolean isDone;

    // Settings
    public static boolean announcementMode;
    public static LocalDate endDate; //today
    public static int endHour = 12;
    public static int endMinute = 30;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Feedbacker");
        primaryStage.setScene(new Scene(root, 300, 275));
        //primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);

        primaryStage.show();

        endDate = LocalDate.now();
        System.out.println(endDate);

    }


    public static void main(String[] args) {
        launch(args);


    }
}
