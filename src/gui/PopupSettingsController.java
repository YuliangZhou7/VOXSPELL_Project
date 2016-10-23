package gui;

import data.FestivalFileWriter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This is the controller for that settingsScreen.fxml. In this Screen it is possible to change the type of voice
 * used by festival. It also has the option of clearing stats.
 * Author: Yuliang Zhou 7/09/2016
 */
public class PopupSettingsController {

    private Stage _thisWindow;
    private MasterController _myParentController;

    @FXML
    private ObservableList<String> _voiceTypeList;
    @FXML
    private ObservableList<String> _voiceSpeedList;
    @FXML
    private ChoiceBox<String> _voiceSelect;
    @FXML
    private ChoiceBox<String> _voiceSpeed;

    public void setUp(Stage w, MasterController m){
        _thisWindow = w;
        _myParentController = m;

        //setup voice types
        _voiceTypeList = FXCollections.observableArrayList("Default","New Zealand");
        _voiceSelect.setItems(_voiceTypeList);
        //setup voice speeds
        _voiceSpeedList = FXCollections.observableArrayList("Slow","Normal","Fast");
        _voiceSpeed.setItems(_voiceSpeedList);

        //set ChoiceBox values to current values
        _voiceSelect.setValue(_myParentController.get_voice());
        _voiceSpeed.setValue(_myParentController.get_voiceSpeed());
    }

    /**
     * Saves festival.scm file and closes this window.
     * @throws IOException
     * @throws InterruptedException
     */
    public void backButtonPressed() throws IOException, InterruptedException {
        _myParentController.buttonClickSound();
        String newVoiceSpeed;
        if(_voiceSpeed.getValue().equals("Fast")) {
            newVoiceSpeed = "0.75";
        }else if(_voiceSpeed.getValue().equals("Slow")) {
            newVoiceSpeed = "1.5";
        }else {//normal speed
            newVoiceSpeed = "1.00";
        }
        FestivalFileWriter.getInstance().changeSpeed(newVoiceSpeed);
        if((_voiceSelect.getValue()).equals("Default")){
            FestivalFileWriter.getInstance().changeVoice("(voice_kal_diphone)");
        }else if((_voiceSelect.getValue()).equals("New Zealand")){
            FestivalFileWriter.getInstance().changeVoice("(voice_akl_nz_jdt_diphone)");
        }
        _myParentController.set_voice(_voiceSelect.getValue());

        _thisWindow.close();
    }


}
