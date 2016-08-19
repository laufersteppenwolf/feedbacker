package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by John Doe on 19.08.2016.
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
        pickerEndDate.setValue(Main.endDate);

        choiceBoxTimeHour.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
        choiceBoxTimeMinute.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
                24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59));
        choiceBoxTimeHour.setValue(Main.endHour);
        choiceBoxTimeMinute.setValue(Main.endMinute);

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
    }
}
