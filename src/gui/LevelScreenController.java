package gui;

import data.SpellingDatabase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;

import java.util.List;
import java.util.Set;


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
    private ChoiceBox<String> _spellingLists;
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
    @FXML
    private Tooltip tt1;
    @FXML
    private Tooltip tt2;
    @FXML
    private Tooltip tt3;
    @FXML
    private Tooltip tt4;
    @FXML
    private Tooltip tt5;
    @FXML
    private Tooltip tt6;
    @FXML
    private Tooltip tt7;
    @FXML
    private Tooltip tt8;
    @FXML
    private Tooltip tt9;
    @FXML
    private Tooltip tt10;
    @FXML
    private Tooltip tt11;


    private int _selectedIntLevel;


    @Override
    public void setScreenParent(MasterController screenParent) {
        _myParentController = screenParent;
    }

    @Override
    public void setup() {
        _quizTypeList = FXCollections.observableArrayList("New Quiz","Revision Quiz");
        _quizType.setItems(_quizTypeList);
        _quizType.setValue("New Quiz");

        //when spelling list's value is changed update the current spelling model and update buttons
        _spellingLists.getSelectionModel().selectedItemProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue!=null) {
                    _myParentController.set_currentSpellingList((String) newValue);
                    setButtons();
                }
            }
        });
    }

    @Override
    public void displayScreen() {
        //gets all the spelling lists from the master controller and resets the buttons
        List<String> list = _myParentController.getSpellingListKeys();
        ObservableList obList = FXCollections.observableList(list);
        _spellingLists.setItems(obList);
        _spellingLists.setValue(_myParentController.get_currentSpellingList());
        setButtons();
    }

    /**
     * This method is called whenever the spelling ChoiceBox is updated. Updates which buttons are visible
     * And disables the currently selected level button.
     */
    public void setButtons(){
        //set the tool tip to name of level
        SpellingDatabase sd = _myParentController.getCurrentSpellilngModel();
        tt1.setText(sd.getActualKey(1));
        tt2.setText(sd.getActualKey(2));
        tt3.setText(sd.getActualKey(3));
        tt4.setText(sd.getActualKey(4));
        tt5.setText(sd.getActualKey(5));
        tt6.setText(sd.getActualKey(6));
        tt7.setText(sd.getActualKey(7));
        tt8.setText(sd.getActualKey(8));
        tt9.setText(sd.getActualKey(9));
        tt10.setText(sd.getActualKey(10));
        tt11.setText(sd.getActualKey(11));
        //reset all buttons to invisible
        b1.setVisible(false);
        b2.setVisible(false);
        b3.setVisible(false);
        b4.setVisible(false);
        b5.setVisible(false);
        b6.setVisible(false);
        b7.setVisible(false);
        b8.setVisible(false);
        b9.setVisible(false);
        b10.setVisible(false);
        b11.setVisible(false);
        //set only the buttons for levels which are contained by the current spelling list
        Set<Integer> s = _myParentController.getCurrentSpellilngModel().getLevelNumbers();
        for (Integer level :s){
            if(level==1){
                b1.setVisible(true);
            }else if(level==2){
                b2.setVisible(true);
            }else if(level==3){
                b3.setVisible(true);
            }else if(level==4){
                b4.setVisible(true);
            }else if(level==5){
                b5.setVisible(true);
            }else if(level==6){
                b6.setVisible(true);
            }else if(level==7){
                b7.setVisible(true);
            }else if(level==8){
                b8.setVisible(true);
            }else if(level==9){
                b9.setVisible(true);
            }else if(level==10){
                b10.setVisible(true);
            }else if(level==11){
                b11.setVisible(true);
            }
        }
        //set all buttons to initially enabled
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

    /**
     * Returns the type of quiz to commence
     * @param _quizType
     * @return
     */
    private String getChoice(ChoiceBox<String> _quizType){
        return _quizType.getValue();
    }

    /**
     * Sets the button for the level number to disabled, and all other buttons to enabled.
     * The start quiz button is also enabled once a level is selected.
     * @param event
     */
    public void levelButtonPressed(ActionEvent event){
        _myParentController.buttonClickSound();
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
        _selectedIntLevel = Integer.parseInt(b.getText());
        _startQuiz.setDisable(false);
    }

    /**
     * Gets the level selected and sets up the quiz, then transitions into the quiz screen.
     * @param event
     */
    public void enterNewQuiz(ActionEvent event){
        _myParentController.buttonClickSound();

        _startQuiz.setDisable(true);

        _myParentController.set_currentSpellingList(_spellingLists.getValue());

        boolean isRevision = false;
        //checking which option the user chose for the quiz type
        if( getChoice(_quizType).equals("Revision Quiz")){
            isRevision = true;
        }
        else if( getChoice(_quizType).equals("New Quiz")){
            isRevision = false;
        }
        boolean enabledQuiz;
        //get the QuizScreen Controller and setup the test
        QuizScreenController nextScreen = (QuizScreenController) _myParentController.getScreenController(Main.Screen.QUIZ);
        if(isRevision) {
            enabledQuiz = nextScreen.setupTest(_selectedIntLevel, true);
        }else{
            enabledQuiz = nextScreen.setupTest(_selectedIntLevel, false);
        }

        if(enabledQuiz){
            //switch into the quiz menu screen
            _myParentController.setScreen(Main.Screen.QUIZ);
        }else{
            DialogBox.errorDialogBox("VOXSPELL","No words to review :)");
            _startQuiz.setDisable(false);
        }

    }

    /**
     * This method is called when the back button is pressed and Switches the scene to the TITLE screen.
     */
    public void backButtonPressed(){
        _myParentController.buttonClickSound();
        _myParentController.setScreen(Main.Screen.TITLE);
    }



}
