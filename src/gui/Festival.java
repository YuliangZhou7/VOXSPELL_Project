package gui;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * The Festival class that takes the scm file and reads out a string provided by the QuizScreenController.
 * It should read out the words that need to be read for the spelling quiz. Uses Task to invoke methods for
 * saying the words through festival on the background thread to prevent the GUI from freezing.
 * Created by samule on 19/09/16.
 */
public class Festival extends Service<Void> {

    private String _cmd;
    private ProcessBuilder _pb;
    private Process process;

    /**
     *  Enables the submit and enter button on the QuizScreenController after the Task successfully finishes.
     */
    @Override
    protected void succeeded() {
        super.succeeded();
        //enables the enteredWord method and _submit method.
        QuizScreenController.set_enableInput(true);
        SettingsScreenController.set_enableInput(true);
    }

    /**
     *  Enables the submit and enter button on the QuizScreenController if the task fails
     */
    @Override
    protected void failed() {
        super.failed();
        //enables the enteredWord method and _submit method.
        QuizScreenController.set_enableInput(true);
        SettingsScreenController.set_enableInput(true);
    }

    /**
     * Reads out the phrase sent by the QuizScreenController and disables the submit and enter buttons, so the
     * festival voices won't overlap. You can only submit or enter a word once the festival voice finishes.
     * @return
     */
    @Override
    protected Task<Void> createTask() {

        return new Task<Void>(){

            @Override
            protected Void call() throws Exception {
                //disables the enteredWord method and _submit method.
                QuizScreenController.set_enableInput(false);
                SettingsScreenController.set_enableInput(false);
                //starts the process
                process = _pb.start();
                //waits for the process to finish before calling succeeded or failed methodss
                process.waitFor();
                return null;
            }
        };



    }

    /**
     * edits the final line of the Festival.scm, inserts the phrase that needs to be read out by festival into SayText.
     * The ProcessBuilder then reads the Festival.scm file executing all the commands inside.
     * @param phrase
     */
    public void set_phrase(String phrase) {
        _cmd = "sed -i \"3s/.*/(SayText \\\""+phrase+"\\\")/\" ./resources/festival.scm ; festival -b ./resources/festival.scm";
        _pb = new ProcessBuilder("/bin/bash","-c",_cmd);
    }
}
