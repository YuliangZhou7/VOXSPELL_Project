package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * TextArea help menu which gives an example of the correct formatting of custom spelling lists
 * and how to play the VOXSPELL spelling game.
 * Created by Yuliang Zhou 24/10/2016
 */
public class HelpScreenController implements ControlledScreen {

    private MasterController _myParentController;

    @FXML
    private TextArea _customListExample;
    @FXML
    private TextArea _howToPlay;

    @Override
    public void setScreenParent(MasterController screenParent) {
        _myParentController = screenParent;
    }

    @Override
    public void setup() {
        _customListExample.setText("%domestic\n" +
                "cat\n" +
                "dog\n" +
                "mouse\n" +
                "hamster\n" +
                "%mammals\n" +
                "elephant\n" +
                "lion\n" +
                "deer\n" +
                "goat\n" +
                "cow\n" +
                "tiger\n" +
                "buffalo\n" +
                "whale\n" +
                "%deadly animals\n" +
                "snake\n" +
                "shark\n" +
                "bear\n" +
                "octopus\n" +
                "vulture\n" +
                "bull\n" +
                "crocodile\n" +
                "\n");
        _howToPlay.setText("=============================================================================================\n" +
                "================================= Welcome to The Amazing VOXSPELL ===================================\n" +
                "=============================================================================================\n" +
                "\n" +
                "To begin a quiz, click the “Begin Quiz” button in the main title screen.\n" +
                "\n" +
                "Then select a spelling list and a corresponding level to start from. For the “Default” \n" +
                "spelling list there will be 11 levels in total. For custom spelling lists there are up to 11 \n" +
                "levels.\n" +
                "\n" +
                "Once in a quiz, the user will hear which word to spell through Festival Text-To-Speech. \n" +
                "The user will get two attempts to spell the word correctly. They can rehear the word using \n" +
                "the “Repeat Word” button, and can also change the voice and speed settings using the cog \n" +
                "icon on the bottom right. \n" +
                "The user is given feedback on whether they get a word correct; if this is the case, the\n" +
                "program will play a “correct” sound and display “Correct” in green shown below, whereas\n" +
                "if they get it wrong it will play an “incorrect” sound and display “Try again” in orange \n" +
                "if it was their first attempt. Otherwise it will show “Incorrect” in red as shown below.\n" +
                "\n" +
                "Once the quiz is completed it will lead the user to the screen shown below. If the user \n" +
                "scored 9 or 10 correct then they can immediately progress to the next level, or play the\n" +
                "next part of the video story. Each level will play a different segment of the story of \n" +
                "Big Buck Bunny, with “Level 11” being the additional bonus clip.\n");
    }

    @Override
    public void displayScreen() {

    }

    /**
     * This method is called when the back button is pressed and Switches the scene to the TITLE screen.
     */
    public void backButtonPressed(){
        _myParentController.buttonClickSound();
        _myParentController.setScreen(Main.Screen.TITLE);
    }



}
