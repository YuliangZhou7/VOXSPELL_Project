package gui;

import data.FestivalFileWriter;
import data.SpellingDatabase;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This is the controller for that settingsScreen.fxml. In this Screen it is possible to change the type of voice
 * used by festival. It also has the option of clearing stats.
 * TODO: add background volume slider
 * Author: Yuliang Zhou 7/09/2016
 */
public class SettingsScreenController implements ControlledScreen{

    private MasterController _myParentScreensController;
    private ObservableList<String> _voiceTypeList;
    private ObservableList<String> _voiceSpeedList;

    private Festival _festival;
    private static BooleanProperty _enableInput;


    @FXML
    private Button _testButton;
    @FXML
    private ChoiceBox<String> _voiceSelect;
    @FXML
    private ChoiceBox<String> _voiceSpeed;
    @FXML
    private ChoiceBox<String> _spellingLists;

    /**
     * Sets the parent controller to the MasterController. Then gets a reference to the spelling database object
     * @param screenParent
     */
    @Override
    public void setScreenParent(MasterController screenParent) {
        _myParentScreensController = screenParent;
    }

    @Override
    public void displayScreen() {
        _voiceSelect.setValue(_myParentScreensController.get_voice());
        _voiceSpeed.setValue(_myParentScreensController.get_voiceSpeed());
    }

    @Override
    public void setup() {
        _festival = new Festival();
        //setup voice type
        _voiceTypeList = FXCollections.observableArrayList("Default","New Zealand");
        _voiceSelect.setItems(_voiceTypeList);
        _voiceSelect.setValue(_myParentScreensController.get_voice());
        //setup voice speed
        _voiceSpeedList = FXCollections.observableArrayList("0.5","1.00","1.50","2.00");//TODO: 0.5 = slower, 2.0 = faster
        _voiceSpeed.setItems(_voiceSpeedList);
        _voiceSpeed.setValue(_myParentScreensController.get_voiceSpeed());
        _enableInput = new SimpleBooleanProperty(this,"_enableInput",true);
        //setup spelling list combobox
        updateSpellingListComboBox();
        //prevent double clicking test button
        _enableInput.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(is_enableInput()){
                    _testButton.setDisable(false);
                }else{
                    _testButton.setDisable(true);
                }
            }
        });
    }

    /**
     * Opens a new file chooser window. Once a .txt file is selected, it is passed to the DatabaseIO to read
     * the file and save it as a new SpellingDatabase object inside the DatabaseManager.ser object.
     * ComboBox is then updated accordingly.
     * @param actionEvent
     */
    public void fileChooserOpened(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("."));
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files(.txt)", "*.txt"));
        File selectedFile = fc.showOpenDialog(Main.getStage());
        if (selectedFile != null) {
            _myParentScreensController.addSpellingFile(selectedFile);
            updateSpellingListComboBox();
            _spellingLists.setValue(selectedFile.getName());
        }
    }

    /**
     * This method is called when user adds or removes a spelling list.
     */
    private void updateSpellingListComboBox() {
        List<String> list = _myParentScreensController.getSpellingListKeys();
        ObservableList obList = FXCollections.observableList(list);
        _spellingLists.setItems(obList);
        _spellingLists.setValue(_myParentScreensController.get_currentSpellingList());
    }

    /**
     * Sends a request to clear all the statistics of all spelling lists
     */
    public void clearStatsButtonPressed(){
        _myParentScreensController.requestClearStats();
    }

    /**
     * Returns to title screen
     * @throws IOException
     * @throws InterruptedException
     */
    public void backButtonPressed() throws IOException, InterruptedException {
        if((_voiceSelect.getValue()).equals("Default")){
            FestivalFileWriter.getInstance().changeVoice("(voice_kal_diphone)");
            FestivalFileWriter.getInstance().changeSpeed(_voiceSpeed.getValue());
        }else if((_voiceSelect.getValue()).equals("New Zealand")){
            FestivalFileWriter.getInstance().changeVoice("(voice_akl_nz_jdt_diphone)");
            FestivalFileWriter.getInstance().changeSpeed(_voiceSpeed.getValue());
        }
        _myParentScreensController.set_voice(_voiceSelect.getValue());
        _myParentScreensController.set_voiceSpeed(_voiceSpeed.getValue());
        _myParentScreensController.setScreen(Main.Screen.TITLE);
    }

    /**
     * Reads. "Hello. I am the the <voice_type> voice."
     * @throws IOException
     * @throws InterruptedException
     */
    public void testFestival() throws IOException, InterruptedException {
        if((_voiceSelect.getValue()).equals("Default")){
            FestivalFileWriter.getInstance().changeVoice("(voice_kal_diphone)");
            FestivalFileWriter.getInstance().changeSpeechText("Hello. I am the default voice.");
        }else if((_voiceSelect.getValue()).equals("New Zealand")){
            FestivalFileWriter.getInstance().changeVoice("(voice_akl_nz_jdt_diphone)");
            FestivalFileWriter.getInstance().changeSpeechText("Hello. I am the New Zealand voice.");
        }
        FestivalFileWriter.getInstance().changeSpeed(_voiceSpeed.getValue().toString());
        _myParentScreensController.set_voice(_voiceSelect.getValue());
        _myParentScreensController.set_voiceSpeed(_voiceSpeed.getValue());
        //read out the test phrase
        _festival.restart();
    }

    /**
     * Sends a request to delete the currently selected list.
     * Default list cannot be removed
     */
    public void deleteList(){
        _myParentScreensController.requestDeleteSpellingList(_spellingLists.getValue());
        updateSpellingListComboBox();
    }



    public static boolean is_enableInput() {
        return _enableInput.get();
    }

    public static BooleanProperty _enableInputProperty() {
        return _enableInput;
    }

    public static void set_enableInput(boolean _enableInput) {
        SettingsScreenController._enableInput.set(_enableInput);
    }


}
