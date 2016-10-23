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

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This is the controller for that settingsScreen.fxml. In this Screen it is possible to change the type of voice
 * used by festival. It also has the option of clearing stats.
 * Author: Yuliang Zhou 7/09/2016
 */
public class SettingsScreenController implements ControlledScreen{

    private MasterController _myParentController;
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
        _myParentController = screenParent;
    }

    @Override
    public void displayScreen() {
        _voiceSelect.setValue(_myParentController.get_voice());
        if(_voiceSpeed.getValue().equals("Fast")) {
            _voiceSpeed.setValue("0.75");
        }else if(_voiceSpeed.getValue().equals("Slow")) {
            _voiceSpeed.setValue("1.5");
        }else {//normal speed
            _voiceSpeed.setValue("1.00");
        }
    }

    @Override
    public void setup() {
        _festival = new Festival();
        //setup voice type
        _voiceTypeList = FXCollections.observableArrayList("Default","New Zealand");
        _voiceSelect.setItems(_voiceTypeList);
        _voiceSelect.setValue(_myParentController.get_voice());
        //setup voice speed
        _voiceSpeedList = FXCollections.observableArrayList("Slow","Normal","Fast");
        _voiceSpeed.setItems(_voiceSpeedList);
        _voiceSpeed.setValue(_myParentController.get_voiceSpeed());
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
        _myParentController.buttonClickSound();
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("."));
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files(.txt)", "*.txt"));
        File selectedFile = fc.showOpenDialog(Main.getStage());
        if (selectedFile != null) {
            _myParentController.addSpellingFile(selectedFile);
            updateSpellingListComboBox();
            _spellingLists.setValue(selectedFile.getName());
        }
    }

    /**
     * Sends a request to delete the currently selected list.
     * Default list cannot be removed
     */
    public void deleteList(){
        _myParentController.buttonClickSound();
        _myParentController.requestDeleteSpellingList(_spellingLists.getValue());
        updateSpellingListComboBox();
    }

    /**
     * This method is called when user adds or removes a spelling list.
     */
    private void updateSpellingListComboBox() {
        List<String> list = _myParentController.getSpellingListKeys();
        ObservableList obList = FXCollections.observableList(list);
        _spellingLists.setItems(obList);
        _spellingLists.setValue(_myParentController.get_currentSpellingList());
    }

    /**
     * Sends a request to clear all the statistics of all spelling lists
     */
    public void clearStatsButtonPressed(){
        _myParentController.buttonClickSound();
        _myParentController.requestClearStats();
    }

    /**
     * Returns to title screen
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
        _myParentController.setScreen(Main.Screen.TITLE);
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
        _myParentController.set_voice(_voiceSelect.getValue());
        String newVoiceSpeed;
        if(_voiceSpeed.getValue().equals("Fast")) {
            newVoiceSpeed = "0.75";
            _myParentController.set_voiceSpeed("0.75");
        }else if(_voiceSpeed.getValue().equals("Slow")) {
            newVoiceSpeed = "1.5";
            _myParentController.set_voiceSpeed("1.5");
        }else {//normal speed
            newVoiceSpeed = "1.00";
            _myParentController.set_voiceSpeed("1.00");
        }
        FestivalFileWriter.getInstance().changeSpeed(newVoiceSpeed);
        //read out the test phrase
        _festival.restart();
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
