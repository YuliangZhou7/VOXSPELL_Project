package gui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * This is the controller for the postQuizScreen.fxml. This screen gives the user the option to play the next level,
 * review the mistakes made on the current level, return to the main menu or play the reward video. Playing the reward
 * video or play next level is only available if you get 9 or more words correct.
 * Author: Yuliang Zhou 6/09/2016
 */
public class PostQuizController implements ControlledScreen{

    private MasterController _myParentController;

    @FXML
    private Label _userResultsOne;
    @FXML
    private Label _userResultsTwo;
    @FXML
    private Button _playVideoButton;
    @FXML
    private Button _nextLevelButton;
    @FXML
    private Button _reviewButton;


    private int _levelInt;
    private int _correct;
    private int _total;
    private double _accuracy;


    @Override
    public void setScreenParent(MasterController screenParent) {
        _myParentController = screenParent;
    }

    @Override
    public void setup() {
    }

    @Override
    public void displayScreen() {
        if (_total == 0) {
            _reviewButton.setDisable(true);
            _userResultsOne.setText("Congratulations.");
            _userResultsTwo.setText("Keep up the good work :)");
        }else {
            _reviewButton.setDisable(false);
            _userResultsOne.setText("Congratulations you scored: " + _correct + " of " + _total);
            _userResultsTwo.setText("Accuracy for  " + _levelInt + ": " + _accuracy + "%");
        }
        if(_correct>8){
            _playVideoButton.setDisable(false);
        }else{
            _playVideoButton.setDisable(true);
        }
        if( _correct<9 || _myParentController.isLastLevel(_levelInt) ){
            _nextLevelButton.setDisable(true);
        }else{
            _nextLevelButton.setDisable(false);
        }
    }


    public void returnToTitleButtonPressed(ActionEvent event){
        _myParentController.buttonClickSound();
        _myParentController.setScreen(Main.Screen.TITLE);
    }

    /**
     * Switches screens to play the video
     * @param event
     * @throws IOException
     */
    public void playVideoButtonPressed(ActionEvent event)throws IOException{
        _myParentController.buttonClickSound();
        //sets up the video
        ((VideoPlayerController)_myParentController.getScreenController(Main.Screen.VIDEO)).setCurrentVideo(_levelInt);
        //switches screen to the videoplayer screen and calls displayScreen() which auto plays the video
        _myParentController.setScreen(Main.Screen.VIDEO);
    }

    /**
     * This button is only enable if user scored 9 or more and the level is less than level 11.
     */
    public void nextLevelButtonPressed(ActionEvent event){
        _myParentController.buttonClickSound();
        //change into quiz screen
        _myParentController.setScreen(Main.Screen.QUIZ);

        //next level number
        int nextLevel = _levelInt + 1;

        //get the QuizScreen Controller
        QuizScreenController nextScreen = (QuizScreenController)_myParentController.getScreenController(Main.Screen.QUIZ);
        nextScreen.setupTest(nextLevel,false);
    }

    /**
     * When the reivew Level button is pressed this method is called. When called the screen will be set into the
     * quiz screen on review mode.
     * @param event
     */
    public void reviewLevelButtonPressed(ActionEvent event){
        _myParentController.buttonClickSound();

        //get the QuizScreen Controller
        QuizScreenController nextScreen = (QuizScreenController)_myParentController.getScreenController(Main.Screen.QUIZ);
        boolean enabledQuiz = nextScreen.setupTest(_levelInt,true);

        if(enabledQuiz){
            //change into the review quiz screen
            _myParentController.setScreen(Main.Screen.QUIZ);
        }else{
            DialogBox.errorDialogBox("VOXSPELL","No words to review :)");
        }
    }

    /**
     *  sets the fields of the test results of the current level.
     */
    public void set_testResults(int level, double accuracy, int correct, int total){
        _levelInt = level;
        _correct = correct;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        _accuracy = Double.parseDouble(df.format(accuracy));

        _total = total;
    }

}
