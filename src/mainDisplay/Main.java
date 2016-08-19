package mainDisplay;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;

public class Main extends Application {

    public static final String CSV_PATH_SETTINGS = "C:\\Temp\\settings.csv";

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
        Parent root = FXMLLoader.load(getClass().getResource("mainDisplay.fxml"));
        primaryStage.setTitle("Feedbacker");
        primaryStage.setScene(new Scene(root, 300, 275));
        //primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);

        primaryStage.show();

        endDate = LocalDate.now();
        System.out.println(endDate);

        readSettingsCSV();

    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Settings
     * 1) date
     * 2) hour
     * 3) minute
     * 4) announcement
     */

    static void readSettingsCSV() {
        BufferedReader fileReader = null;
        String[] tokens = new String[10];
        try {
            fileReader = new BufferedReader(new FileReader(CSV_PATH_SETTINGS));
            try {
                tokens = fileReader.readLine().split(";");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (tokens[0] != null) {
            endDate = LocalDate.parse(tokens[0]);
            endHour = Integer.parseInt(tokens[1]);
            endMinute = Integer.parseInt(tokens[2]);
            announcementMode = Boolean.parseBoolean(tokens[3]);
        }
    }

    static void writeSettingsCSV() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(CSV_PATH_SETTINGS);
            fileWriter.append(endDate.toString());
            fileWriter.append(";");
            fileWriter.append(Integer.toString(endHour));
            fileWriter.append(";");
            fileWriter.append(Integer.toString(endMinute));
            fileWriter.append(";");
            fileWriter.append(Boolean.toString(announcementMode));

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } {
        }
    }
}
