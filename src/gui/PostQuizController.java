package gui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.swing.*;
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


    private String _level;
    private int _correct;
    private int _total;
    private double _accuracy;

    public void returnToTitleButtonPressed(ActionEvent event){
        _myParentController.setScreen(Main.Screen.TITLE);
    }

    /**
     * Opened the browser to play video
     * @param event
     * @throws IOException
     */
    public void playVideoButtonPressed(ActionEvent event)throws IOException{
        _myParentController.setScreen(Main.Screen.VIDEO);
    }

    /**
     * This button is only enable if user scored 9 or more and the level is less than level 11.
     */
    public void nextLevelButtonPressed(ActionEvent event){
        String[] level = _level.split(" ");
        int nextLevelNumber = Integer.parseInt(level[1]);
        nextLevelNumber++;
        String nextLevel = "Level " + nextLevelNumber;

        //change into quiz screen
        _myParentController.setScreen(Main.Screen.QUIZ);

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
        //change into the review quiz screen
        _myParentController.setScreen(Main.Screen.QUIZ);

        //get the QuizScreen Controller
        QuizScreenController nextScreen = (QuizScreenController)_myParentController.getScreenController(Main.Screen.QUIZ);
        nextScreen.setupTest(_level,true);
    }

    @Override
    public void setScreenParent(MasterController screenParent) {
        _myParentController = screenParent;
    }

    @Override
    public void setup() {
    }
    // sets the fields of the test results of the current level.
    public void set_testResults(String level, double accuracy, int correct, int total){
        _level = level;
        _correct = correct;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        _accuracy = Double.parseDouble(df.format(accuracy));

        _total = total;
    }

    /**
     * This method is called by the QuizScreenController after the results have been set and just before the screen
     * switches
     */
    public void showResults() {
        if (_total == 0) {
            _reviewButton.setDisable(true);
            _userResultsOne.setText("Congratulations. No mistakes to review.");
            _userResultsTwo.setText("Keep up the good work :)");
        }else {
            _reviewButton.setDisable(false);
            _userResultsOne.setText("Congratulations you scored: " + _correct + " of " + _total);
            _userResultsTwo.setText("Accuracy for  " + _level + ": " + _accuracy + "%");
        }
        if(_correct>8){
            _playVideoButton.setDisable(false);
        }else{
            _playVideoButton.setDisable(true);
        }
        if(_level.equals("Level 11") || _correct<9 ){
            _nextLevelButton.setDisable(true);
        }else{
            _nextLevelButton.setDisable(false);
        }

    }
}
