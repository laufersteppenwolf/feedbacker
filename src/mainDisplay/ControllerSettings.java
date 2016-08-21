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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Stefan Zimmer on 19.08.2016.
 */
public class ControllerSettings implements Initializable {

    public CheckBox announcement;
    public DatePicker pickerEndDate;
    public Button resetCounters;
    public Label labelEndDate;
    public ChoiceBox choiceBoxTimeHour;
    public ChoiceBox choiceBoxTimeMinute;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //write settings values to form
        pickerEndDate.setValue(Main.endDate);
        announcement.setSelected(Main.announcementMode);

        choiceBoxTimeHour.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
        choiceBoxTimeMinute.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
                24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59));
        choiceBoxTimeHour.setValue(Main.endHour);
        choiceBoxTimeMinute.setValue(Main.endMinute);

        //set onClickListeners for checkboxes
        choiceBoxTimeHour.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Main.endHour = newValue.intValue();
                Main.writeSettingsCSV();
            }
        });

        choiceBoxTimeMinute.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Main.endMinute = newValue.intValue();
                Main.writeSettingsCSV();
            }
        });
    }

    public void onDatePickerClicked(ActionEvent actionEvent) {
        Main.endDate = pickerEndDate.getValue();
        Main.writeSettingsCSV();
    }

    public void onAnnouncementClicked(ActionEvent actionEvent) {
        Main.announcementMode = announcement.isSelected();
        Main.writeSettingsCSV();
    }

    public void onResetCountersClicked(ActionEvent actionEvent) {
        Main.counterJa = 0;
        Main.counterNein = 0;
        Controller.writeLogHeaderCSV();
        Controller.writeCSV();
    }
}
