package gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * This class is the Controller for the levelSelectScreen.fxml. It transitions the to the quizScreen.fxml when a
 * level button is clicked. This screen also gives the user the option to set which Test mode they wish to enter
 * depending on their decision at the choicebox _quizType.
 * Created by Samule Li on 13/09/16.
 */
public class LevelScreenController implements ControlledScreen {
    
    ObservableList<String> _quizTypeList;
    private MasterController _myParentController;
    @FXML
    private ChoiceBox<String> _quizType;
    @FXML
    private Button _startQuiz;
    @FXML
    private Button b1;
    @FXML
    private Button b2;
    @FXML
    private Button b3;
    @FXML
    private Button b4;
    @FXML
    private Button b5;
    @FXML
    private Button b6;
    @FXML
    private Button b7;
    @FXML
    private Button b8;
    @FXML
    private Button b9;
    @FXML
    private Button b10;
    @FXML
    private Button b11;

    private String _selectedLevel;


    @Override
    public void setScreenParent(MasterController screenParent) {
        _myParentController = screenParent;
    }

    @Override
    public void setup() {
        _quizTypeList = FXCollections.observableArrayList("New Quiz","Revision Quiz");
        _quizType.setItems(_quizTypeList);
        _quizType.setValue("New Quiz");
    }

    @Override
    public void displayScreen() {
        //TODO: set visible for level 1 to 11 and add tool tip for each level -> string for key in hashmap for level select

        b1.setDisable(false);
        b2.setDisable(false);
        b3.setDisable(false);
        b4.setDisable(false);
        b5.setDisable(false);
        b6.setDisable(false);
        b7.setDisable(false);
        b8.setDisable(false);
        b9.setDisable(false);
        b10.setDisable(false);
        b11.setDisable(false);
        _startQuiz.setDisable(true);
        _quizType.setValue("New Quiz");
    }


    private String getChoice(ChoiceBox<String> _quizType){
        return _quizType.getValue();
    }

    public void levelButtonPressed(ActionEvent event){
        b1.setDisable(false);
        b2.setDisable(false);
        b3.setDisable(false);
        b4.setDisable(false);
        b5.setDisable(false);
        b6.setDisable(false);
        b7.setDisable(false);
        b8.setDisable(false);
        b9.setDisable(false);
        b10.setDisable(false);
        b11.setDisable(false);
        Button b = (Button)event.getSource();
        b.setDisable(true);
        _selectedLevel = b.getText();
        _startQuiz.setDisable(false);
    }

    public void enterNewQuiz(ActionEvent event){
        _startQuiz.setDisable(true);
        //switch into the quiz menu screen
        _myParentController.setScreen(Main.Screen.QUIZ);

        //extracting the text on the button
        String level = "Level "+_selectedLevel;
        boolean isRevision = false;
        //checking which option the user chose for the quiz type
        if( getChoice(_quizType).equals("Revision Quiz")){
            isRevision = true;
        }
        else if( getChoice(_quizType).equals("New Quiz")){
            isRevision = false;
        }

        //get the QuizScreen Controller and setup the test
        QuizScreenController nextScreen = (QuizScreenController) _myParentController.getScreenController(Main.Screen.QUIZ);
        if(isRevision) {
            nextScreen.setupTest(level, true);
        }else{
            nextScreen.setupTest(level, false);
        }

    }

    /**
     * This method is called when the back button is pressed and Switches the scene to the TITLE screen.
     */
    public void backButtonPressed(){
        _myParentController.setScreen(Main.Screen.TITLE);
    }



}
