package gui;

import data.FestivalFileWriter;
import data.SpellingDatabase;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Main controller for both normal quiz mode and review quiz mode.
 * Author: Yuliang Zhou 6/09/2016
 */
public class QuizScreenController implements ControlledScreen{

    private MasterController _myParentController;

    private Festival _festival;

    private static BooleanProperty _enableInput;

    @FXML
    private Text _title;
    @FXML
    private TextField _textfield;
    @FXML
    private ProgressBar _progressBar;
    @FXML
    private Label _progressLabel;
    @FXML
    private Label _tooltip;
    @FXML
    private Label _accuracy;
    @FXML
    private Button _submit;
    @FXML
    private Button _repeat;

    /**
     * This method sets the the MasterController as the parent controller for the quiz controller
     * then sets the quiz controller as a field in the master controller. Thus can call methods in
     * the quiz controller object.
     * @param screenParent
     */
    @Override
    public void setScreenParent(MasterController screenParent) {
        _myParentController = screenParent;
    }

    /**
     * This method is called after the parent screen controller reference is set. Initializes the Festival object
     * for reading the words, and gets a reference to the database object.
     */
    @Override
    public void setup() {
        _enableInput = new SimpleBooleanProperty(this,"_enableInput",true);
        _enableInput.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(is_enableInput()){
                    _submit.setDisable(false);
                    _repeat.setDisable(false);

                }else{
                    _submit.setDisable(true);
                    _repeat.setDisable(true);
                }
            }
        });
        _festival = new Festival();
        _database = _myParentController.getCurrentSpellilngModel();
    }

    @Override
    public void displayScreen() {
        _textfield.setText("");
    }


    /**
     * reads out the word that needs to be spelt, can be pressed multiple times without penalty.
     * @param event
     */
    public void repeatButtonPressed(ActionEvent event){
        read(_wordList[_position]);
        //sets focus onto textfield after repeated word.
        _textfield.requestFocus();
    }

    /**
     * Allows user to quit the current quiz and abandon current progress to go back to the main title screen.
     * @param event
     */
    public void abortQuizButtonPressed(ActionEvent event){
        boolean confirm = DialogBox.displayConfirmDialogBox("Quit current quiz","Are you sure you wish to " +
                "quit current quiz? Unsaved progress will be lost.");
        if(confirm) {
            _myParentController.setScreen(Main.Screen.TITLE);
        }
    }

    /**
     * enteredWord is called whenever Enter button is pressed or enter key is pressed, it takes the user input from the
     * textfield and checks if the word matches the proposed word.
     */
    public void enteredWord(ActionEvent event) {
        if(is_enableInput()){
            if(_textfield.getText().equals("")){
                _tooltip.setText("Please enter a word");
            }else if(_textfield.getText().matches(".*\\d+.*")){
                _tooltip.setText("Please do not enter numbers");
            }else{
                //_userAttempt.set(_textfield.getText());
                _userAttempt=_textfield.getText();
                _tooltip.setText("");
                checkUserAttempt();
            }
            _textfield.setText("");
            _textfield.requestFocus();
        }
    }




    //==========================================SPELLING_LOGIC=====================================================//

    private enum Status{FIRSTATTEMPT,SECONDATTEMPT};

    private SpellingDatabase _database;

    private String _userAttempt;

    //level and mode
    private boolean _isRevision;
    private int _levelInt;
    private String _currentLevel; //"Level 5" or "small animals" actual key

    //words
    private String[] _wordList;
    private int _position;
    private Status _status;

    //4pts for mastered, 2pts for faulted, 0pts failed
    private int _masteredScore = 100;
    private int _faultedScore = 50;
    private int _failedScore = 0;
    private String[] _results;
    private int _score;

    /**
     * This method is called from LevelScreenController and PostQuizController after the screen is set.
     * @param level int
     * @param isRevision boolean
     * @return boolean  returns false if there are no words from that level
     */
    public boolean setupTest(int level,boolean isRevision){
        //setup pretest state
        _database = _myParentController.getCurrentSpellilngModel();
        _levelInt = level;
        _currentLevel = _database.getActualKey(level);
        _isRevision = isRevision;
        _position = 0;
        _score = 0;
        _status = Status.FIRSTATTEMPT;
        if(_isRevision) {
            _wordList = _database.getReviewQuiz(_currentLevel);
        }else{
            _wordList = _database.getNormalQuiz(_currentLevel);
        }
        _results = new String[_wordList.length];

        //if there are no words - from revision mode
        if( _wordList.length == 0){
            return false;
        }

        //Commence test
        read("Please spell: " + _wordList[_position]);
        //set progress label and progress bar and accuracy
        _progressBar.setProgress(_position);
        _title.setText( "VOXSPELL " + _currentLevel );
        _progressLabel.setText("Please spell word "+(_position+1)+" of "+_wordList.length);
        _accuracy.setText("Accuracy: "+0.0+"%");
        _tooltip.setText("");
        return true;
    }


    /**
     * This method is called whenever the user attempt string is changed. This occurs when the user enters a word.
     * Checks if the user's attempt is same as the correct spelling of the word. Ignores case.
     * @return void
     */
    private void checkUserAttempt(){
        boolean completed = false;
        if (_status == Status.FIRSTATTEMPT) {//================================================================MASTERED
            if (_wordList[_position].toLowerCase().equals(_userAttempt.toLowerCase())) {

                //UPDATE SCORE 4pts MASTERED
                _results[_position] = "MASTERED";
                _score+=_masteredScore;

                //MOVE ONTO NEXT WORD
                _position++;
                if( _position == _wordList.length ){
                    completed = true;
                    read("Correct.");
                }else {//Move onto next word
                    read("Correct. Please spell: " + _wordList[_position]);
                    _progressLabel.setText("Please spell word "+(_position+1)+" of "+_wordList.length);
                }

            } else { // GO TO SECOND ATTEMPT
                read("Incorrect. Please try again: " + _wordList[_position]);
                _progressLabel.setText("Incorrect. Please spell word "+(_position+1)+" of "+_wordList.length);
                _status = Status.SECONDATTEMPT;
            }
        } else {//==============================================================================================FAULTED
            if (_wordList[_position].toLowerCase().equals(_userAttempt.toLowerCase())) {

                //UPDATE SCORE 2pts FAULTED
                _results[_position] = "FAULTED";
                _score+=_faultedScore;

                //MOVE ONTO NEXT WORD
                _position++;
                if( _position == _wordList.length ){
                    completed = true;
                    read("Correct.");
                }else {//Correct on second attempt. Move onto next word
                    read("Correct. Please spell: " + _wordList[_position]);
                    _progressLabel.setText("Please spell word "+(_position+1)+" of "+_wordList.length);
                }
            } else {//===========================================================================================FAILED

                //UPDATE SCORE 0pts FAILED
                _results[_position] = "FAILED";
                _score+=_failedScore;

                //MOVE ONTO NEXT WORD
                _position++;
                if( _position == _wordList.length ){
                    completed = true;
                    read("Incorrect");
                }else {
                    read("Incorrect. Please spell: " + _wordList[_position]);
                    _progressLabel.setText("Please spell word "+(_position+1)+" of "+_wordList.length);
                }
            }
            _status = Status.FIRSTATTEMPT;
        }

        //set progress bar
        _progressBar.setProgress((double)(_position)/_wordList.length);
        //System.out.println("SCORE:"+_score);
        //System.out.println("total:"+(_position)*4);

        //update accuracy rating
        double accuracy = ((double) _score / (_position * _masteredScore)) * 100;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        if(Double.isNaN(accuracy)){
            _accuracy.setText("Accuracy: " + 0.0 + "%");
        }else {
            _accuracy.setText("Accuracy: " + df.format(accuracy) + "%");
        }

        //end test and change screen
        if(completed){
            completeTestSaveData();
        }
    }

    /**
     * Saves the user's test scores to the SpellingDatabase object, then switches to the post quiz screen
     * and passes the results to the post quiz screen
     */
    public void completeTestSaveData(){
        int correctCount = 0;
        for(int i=0;i<_results.length;i++){
            if(_results[i].equals("MASTERED")){ // 4pts MASTERED
                //update mastered result in database
                _database.incrementMastered(_currentLevel, _wordList[i]);

                //Only counted as correct if mastered
                correctCount++;

                //if in revision mode, remove word from failed list
                if(_isRevision){
                    _database.removeFailedWord(_wordList[i],_currentLevel);
                }
            }else if(_results[i].equals("FAULTED")){ //2pts FAULTED
                //update faulted result in database
                _database.incrementFaulted(_currentLevel, _wordList[i]);

                //if in revision mode, remove word from failed list
                if(_isRevision){
                    _database.removeFailedWord(_wordList[i],_currentLevel);
                }
            }else{ //0pts FAILED
                //update failed result in database
                _database.incrementFailed(_currentLevel, _wordList[i]);

                //if in normal mode, add word to failed list
                if(!_isRevision){
                    _database.addFailedWord(_wordList[i],_currentLevel);
                }
            }
        }

        _database.addScore(_score,_wordList.length,_currentLevel);
        double accuracy = ((double) _score / (_results.length * _masteredScore)) * 100;
        if(Double.isNaN(accuracy)){
           accuracy = 0.0;
        }

        //get the PostQuizScreen Controller object
        PostQuizController nextScreen = ((PostQuizController)_myParentController.getScreenController(Main.Screen.POSTQUIZ));
        nextScreen.set_testResults(_levelInt,accuracy,correctCount,_wordList.length); //_currentLevel-"Level 5" or "small animals"
        nextScreen.showResults();

        //change screen
        _myParentController.setScreen(Main.Screen.POSTQUIZ);
        _textfield.setText("");
    }

    /**
     * Uses the Festival Service class to read the phrase.
     * @param phrase
     */
    public void read(String phrase) {
        System.out.println("FESTIVAL: " + phrase);
        FestivalFileWriter.getInstance().changeSpeechText(phrase);
        _festival.restart();
    }

    public static boolean is_enableInput() {
        return _enableInput.get();
    }

    public static void set_enableInput(boolean _enableInput) {
        QuizScreenController._enableInput.set(_enableInput);
    }
}
