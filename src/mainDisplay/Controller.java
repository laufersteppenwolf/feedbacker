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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public static final String CSV_PATH_DATA = "C:\\Temp\\data.csv";
    public static final String CSV_PATH_LOG = "C:\\Temp\\log.csv";
    public static final String BACKGROUND_PATH = "C:\\test.png";

    public ImageView background;
    public Label counterJa;
    public Label counterNein;
    public Button buttonSettings;

    public Thread hideLabels;

    public long resetTimeJa;
    public long resetTimeNein;

    String[] previousLog = new String[10000];


    public void work(MouseEvent mouseEvent) throws InterruptedException {
        if (!Main.isDone) {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                Main.counterJa++;
                counterJa.setVisible(true);
                counterJa.setText(Integer.toString(Main.counterJa));

                resetTimeJa = (Calendar.getInstance().getTimeInMillis() + 2000);

                writeLogCSV("Ja", "");
            }
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                Main.counterNein++;
                counterNein.setVisible(true);
                counterNein.setText(Integer.toString(Main.counterNein));

                resetTimeNein = (Calendar.getInstance().getTimeInMillis() + 2000);

                writeLogCSV("", "Nein");
            }
            writeCSV();
        }
    }

    public void setBackground() {
        File file = new File(BACKGROUND_PATH);
        Image image = new Image(file.toURI().toString());
        background.setImage(image);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setBackground();

        counterJa.setFont(Font.font("Verdana", FontWeight.BOLD, 42));
        counterNein.setFont(Font.font("Verdana", FontWeight.BOLD, 42));

        hideLabels = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!Main.isDone) {
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        if (currentTime >= resetTimeJa) {
                            counterJa.setVisible(false);
                        }
                        if (currentTime >= resetTimeNein) {
                            counterNein.setVisible(false);
                        }
                    } else {
                        counterJa.setVisible(true);
                        counterNein.setVisible(true);
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (Main.endDate != null){
                        //System.out.println("date " + Main.endDate + " hour " + Main.endHour + " minute " + Main.endMinute);
                        if (LocalDate.now().isAfter(Main.endDate)) {
                            Main.isDone = true;
                            //System.out.println("1");
                        } else if (LocalDate.now().isBefore(Main.endDate)) {
                            Main.isDone = false;
                            //System.out.println("2");
                        } else if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > Main.endHour) {
                            Main.isDone = true;
                            //System.out.println("3");
                        } else if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < Main.endHour) {
                            Main.isDone = false;
                            //System.out.println("4");
                        } else if (Calendar.getInstance().get(Calendar.MINUTE) > Main.endMinute) {
                            Main.isDone = true;
                            //System.out.println("5");
                        } else if (Calendar.getInstance().get(Calendar.MINUTE) < Main.endMinute) {
                            Main.isDone = false;
                            //System.out.println("6");
                        } else {
                            Main.isDone = true; //default fallback
                            //System.out.println("7");
                        }
                    }
                }
            }
        });
        hideLabels.setDaemon(true);
        hideLabels.start();

        buttonSettings.setOnAction(new EventHandler<ActionEvent>() {
                                       public void handle(ActionEvent event) {
                                           try {
                                               FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settings.fxml"));
                                               Parent root1 = (Parent) fxmlLoader.load();
                                               Stage stage = new Stage();
                                               stage.initModality(Modality.APPLICATION_MODAL);
                                               stage.setTitle("Settings");
                                               stage.setScene(new Scene(root1));
                                               stage.show();

                                           } catch (IOException e) {
                                               e.printStackTrace();
                                           }
                                       }
                                   } );

        readCSV();
        counterJa.setText(Integer.toString(Main.counterJa));
        counterNein.setText(Integer.toString(Main.counterNein));

        File f = new File(CSV_PATH_LOG);
        if (!f.exists()) {
            writeLogHeaderCSV();
        }

    }

    void readCSV() {
        File f = new File(CSV_PATH_DATA);
        if (f.exists()) {
            BufferedReader fileReader = null;
            String[] tokens = new String[2];
            try {
                fileReader = new BufferedReader(new FileReader(CSV_PATH_DATA));
                try {
                    tokens = fileReader.readLine().split(";");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (tokens[0] != null) {
                Main.counterJa = Integer.parseInt(tokens[0]);
                Main.counterNein = Integer.parseInt(tokens[1]);
            }
        }
    }

    void writeCSV() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(CSV_PATH_DATA);
            fileWriter.append(Integer.toString(Main.counterJa));
            fileWriter.append(";");
            fileWriter.append(Integer.toString(Main.counterNein));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    static void writeLogCSV(String value1, String value2) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(CSV_PATH_LOG, true);
            fileWriter.append(getCurrentTimeStamp());
            fileWriter.append(";");
            fileWriter.append(value1);
            fileWriter.append(";");
            fileWriter.append(value2);
            fileWriter.append("\n");

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void writeLogHeaderCSV() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(CSV_PATH_LOG);
            fileWriter.append("Time");
            fileWriter.append(";");
            fileWriter.append("Ja");
            fileWriter.append(";");
            fileWriter.append("Nein");
            fileWriter.append("\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}