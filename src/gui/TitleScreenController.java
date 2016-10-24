package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 *
 * Author: Yuliang Zhou 6/09/2016
 */
public class TitleScreenController implements ControlledScreen{

    private MasterController _myParentController;

    @FXML
    private Button _startButton;
    @FXML
    private Button _displayStatsButton;
    @FXML
    private Button _settingsButton;
    @FXML
    private Button _quitButton;

    @Override
    public void setScreenParent(MasterController screenParent) {
        _myParentController = screenParent;
    }

    @Override
    public void setup() {}

    @Override
    public void displayScreen() {

    }

    /**
     * Requests main screen controller to switch to the quiz scene.
     * Uses a fade in and out transition.
     */
    public void normalQuizPressed(){
        _myParentController.buttonClickSound();
        _myParentController.setScreen(Main.Screen.LEVELSELECT);
    }

    /**
     * Requests main screen controller to switch to statistics scene which
     * contains a Table view of the stats of words at each level.
     * Uses a fade in and out transition
     */
    public void displayStatsButtonPressed(){
        _myParentController.buttonClickSound();
        _myParentController.setScreen(Main.Screen.STATS);
    }

    /**
     * Requests main screen controller to switch to the options scene.
     * Uses a fade in and out transition.
     */
    public void settingsButtonPressed(){
        _myParentController.buttonClickSound();
        _myParentController.setScreen(Main.Screen.SETTINGS);
    }

    /**
     * Request main screen controller to switch to the help screen
     */
    public void helpButtonPressed(){
        _myParentController.buttonClickSound();
        _myParentController.setScreen(Main.Screen.HELP);
    }

    /**
     * Confirms close operation and saves serialized data.
     */
    public void quitButtonPressed(){
        _myParentController.buttonClickSound();
        _myParentController.confirmCloseProgram();
    }


}
