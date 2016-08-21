/**
 * Copyright (C) 2016, Stefan Zimmer
 *
 * This software file (the "File") is distributed by Marvell International
 * Ltd. under the terms of the GNU General Public License Version 2, June 1991
 * (the "License").  You may use, redistribute and/or modify this File in
 * accordance with the terms and conditions of the License, a copy of which
 * is available by writing to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA or on the
 * worldwide web at http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 *
 * THE FILE IS DISTRIBUTED AS-IS, WITHOUT WARRANTY OF ANY KIND, AND THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE
 * ARE EXPRESSLY DISCLAIMED.  The License provides additional details about
 * this warranty disclaimer.
 */
package mainDisplay;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;

public class Main extends Application {

    public static final String CSV_PATH_SETTINGS = "S:\\settings.csv";

     static int counterJa = 0;
     static int counterNein = 0;
     static boolean isDone;

    // Settings
     static boolean announcementMode;
     static LocalDate endDate; //today
     static int endHour = 12;
     static int endMinute = 30;

     static Stage mainStage;


    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("mainDisplay.fxml"));
        primaryStage.setTitle("Feedbacker");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setFullScreen(true);
        //primaryStage.setMaximized(true);

        primaryStage.show();

        //init variable
        endDate = LocalDate.now();
        System.out.println(endDate);

        //read settings from file
        try {
            readSettingsCSV();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        File f = new File(CSV_PATH_SETTINGS);
        if (f.exists()) {
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
