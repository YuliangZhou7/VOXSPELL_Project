package gui;

import data.DatabaseIO;
import data.DatabaseManager;
import data.SpellingDatabase;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.animation.KeyFrame;
import javafx.animation.FadeTransition;

import javafx.beans.property.DoubleProperty;


import javafx.scene.media.AudioClip;
import javafx.stage.Screen;
import javafx.util.Duration;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * The MasterController extends StackPane and contains a HashMap of all the screens.
 * Once the screen has been loaded it can change screen with a fade in/out transition.
 * Also contains the DatabaseIO object for opening and saving the SpellingDatabase object
 * which contains all the spelling words and user stats. All screens has a reference to the
 * MasterController to switch screens, request info from database, etc.
 *
 * Created by Samule Li and Yuliang Zhou on 5/09/16.
 */
public class MasterController extends StackPane {
    //screens
    private HashMap< Main.Screen, Node> _screens;
    private HashMap< Main.Screen, ControlledScreen> _controllers;

    //file io
    private File _defaultFile;
    private DatabaseIO _dataIO;

    //databases
    private DatabaseManager _spellingDatabase;
    private ArrayList<String> _spellingListKeys;
    private String _currentSpellingList;

    //voice
    private String _voice;
    private String _voiceSpeed;

    //sounds effects
    private AudioClip _buttonPressSound;
    private AudioClip _correctSound;
    private AudioClip _cheeringSound;
    private AudioClip _incorrectSound;

    public MasterController(){
        super();
        _screens = new HashMap<>();
        _controllers = new HashMap<>();
        _dataIO = new DatabaseIO();
        _defaultFile = new File(".spellingData.ser");
        _spellingDatabase = _dataIO.openData(_defaultFile);
        _spellingListKeys = getSpellingListKeys();
        _currentSpellingList = "Default";
        _voice = "Default";
        _voiceSpeed = "1.00";
        _buttonPressSound = new AudioClip(MasterController.class.getResource("/resources/audio/Tiny_Button_Push-SoundBible.com-513260752.wav").toString());
        _cheeringSound = new AudioClip(MasterController.class.getResource("/resources/audio/yay.mp3").toString());
        _correctSound = new AudioClip(MasterController.class.getResource("/resources/audio/success.wav").toString());
        _incorrectSound = new AudioClip(MasterController.class.getResource("/resources/audio/incorrect.wav").toString());
    }


    /**
     * Called from Settings screen. Uses the DatabaseIO object to read the file and creates a new SpellingDatabase
     * object from it. Then adds the new SpellingDatabase object to the DatabaseManager if it's not already contained.
     * @param file
     */
    public void addSpellingFile(File file) {
        SpellingDatabase newList = new SpellingDatabase();
        boolean isSuccessful = _dataIO.readNewWordList(newList,file);
        if(isSuccessful){
            _spellingDatabase.addNewSpellingList(file.getName(),newList);
        }else{
            DialogBox.errorDialogBox("Error","Sorry incorrect format given. Please refer to help and try again.");
        }
    }

    /**
     * Returns true if level number is the last level. False otherwise.
     * @param level
     * @return
     */
    public boolean isLastLevel(int level) {
        return getCurrentSpellilngModel().isLastLevel(level);
    }

    /**
     *  Checks if user really wants to delete data before deleting.
     */
    public void requestClearStats() {
        boolean clearTrue = DialogBox.displayConfirmDialogBox("Clear User Statistics", "Are you sure you want to clear all user data?");
        if (clearTrue) {
            _spellingDatabase.clearAllStats();
        }
    }

    /**
     * Request DatabaseManager object to remove the spelling list given by the name of the list as the key.
     * @param listToRemove
     */
    public void requestDeleteSpellingList(String listToRemove){
        _spellingDatabase.removeSpellingList(listToRemove);
    }

    //===========================================SOUNDS EFFECTS=======================================================//

    public void buttonClick(){
        _buttonPressSound.play();
    }

    public void playCorrectSound(){
        _correctSound.play();
    }

    public void playIncorrectSounds(){
        _incorrectSound.play();
    }

    public void playCheeringSound(){
        _cheeringSound.play();
    }

    //=========================================GETTERS & SETTERS======================================================//

    /**
     * Returns the instance of the controller object given as type ControlledScreen. Must be cast
     * to the specific type before calling any subclass specific methods.
     * @param screen
     * @return
     */
    public ControlledScreen getScreenController(Main.Screen screen){
        return _controllers.get(screen);
    }

    /**
     * Returns a reference to the spelling database object specified by the key. Returns null if there
     * is no spelling list associated with the key.
     * @return
     */
    public SpellingDatabase getCurrentSpellilngModel() {
        return _spellingDatabase.getSpellingList(_currentSpellingList);
    }

    /**
     * Returns a list of all the spelling lists currently added to the DatabaseManager object
     * @return
     */
    public ArrayList<String> getSpellingListKeys(){
        _spellingListKeys = _spellingDatabase.getSpellingKeys();
        return _spellingListKeys;
    }

    public String get_currentSpellingList() {
        return _currentSpellingList;
    }

    public void set_currentSpellingList(String _currentSpellingList) {
        this._currentSpellingList = _currentSpellingList;
    }

    public String get_voice() {
        return _voice;
    }

    public void set_voice(String _voice) {
        this._voice = _voice;
    }

    public String get_voiceSpeed() {return _voiceSpeed;}

    public void set_voiceSpeed(String _voiceSpeed) { this._voiceSpeed = _voiceSpeed;}



    //===============================================SCREEN_OPERATIONS================================================//



    /**
     * Show dialog box to confirm if user wants to close program.
     */
    public void confirmCloseProgram(){
        Boolean closeOperation = DialogBox.displayConfirmDialogBox("Please don't go","Are you sure you want to quit?");
        if(closeOperation){
            Platform.exit();
        }
    }


    /**
     * Calls the writeData() method in the DatabaseIO object to save the spelling DatabaseManager object
     * to a hidden .ser file
     */
    public void saveData(){
        _dataIO.writeData(_spellingDatabase, _defaultFile);
    }

    /**
     * Loads the fxml file and injects the screenPane into the controller. Then calls the setup method
     * on the screen controllers to do pre-display setup.
     * @param nameScreen
     * @param resource
     * @return
     * @throws Exception
     */
    public boolean loadScreen(Main.Screen nameScreen , String resource){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            Parent root = loader.load();
            ControlledScreen myScreenController = loader.getController();
            myScreenController.setScreenParent(this);
            myScreenController.setup();
            //Save controller to field
            _controllers.put(nameScreen, myScreenController);
            _screens.put(nameScreen, root);
            System.out.println ("Screen successfully loaded");
            return true;
        }
        catch (Exception e){
            System.out.println("Error loading screen...");
            System.out.println(e.getMessage());
            return false;
        }

    }

    /**
     * This method tries to display the selected screen. It first checks whether or not the screen has already been
     * loaded. If there is more than one screen, the new screen is added second, and the current screen is removed.
     * @param name
     * @return
     */
    public boolean setScreen(Main.Screen name) {
        if (_screens.get(name) != null) { //screen loaded
            _controllers.get(name).displayScreen();
            final DoubleProperty opacity = opacityProperty();
            if (!getChildren().isEmpty()) {
                //fade out/fade in transition
                Timeline transition = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(300), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                getChildren().remove(0);    //remove the displayed screens
                                getChildren().add(0, _screens.get(name)); //add the screen
                                Timeline fadeIn = new Timeline(
                                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                        new KeyFrame(new Duration(200), new KeyValue(opacity, 1.0)));
                                fadeIn.play();
                            }
                        }, new KeyValue(opacity, 0.0)));
                transition.play();
            } else {
                setOpacity(0.0);
                //add the screen to the view
                getChildren().add(_screens.get(name));
                //fade in transition
                Timeline transition = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
                transition.play();
            }
            return true;
        } else {
            System.out.println("Screen hasn't been loaded");
            return false;
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public boolean unloadScreen(Main.Screen name) {
        if (_screens.remove(name) == null) {
            System.out.println("Screen doesn't exist");
            return false;
        } else {
            return true;
        }
    }


}