package gui;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * The Festival class uses festival in linux machines to read out the contents of the scm file. Must use
 * FestivalFileWriter singleton class to change the text to read before reading the text.
 * Created by Yuliang Zhou on 19/09/16.
 */
public class Festival extends Service<Void> {

    /**
     *  Enables the submit and enter button on the QuizScreenController after the Task successfully finishes.
     */
    @Override
    protected void succeeded() {
        super.succeeded();
        QuizScreenController.set_enableInput(true);
        SettingsScreenController.set_enableInput(true);
    }

    /**
     *  Enables the submit and enter button on the QuizScreenController if the task fails
     */
    @Override
    protected void failed() {
        super.failed();
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
                String cmd = "festival -b .festival.scm";
                ProcessBuilder pb = new ProcessBuilder("/bin/bash","-c",cmd);

                //disable input while reading
                QuizScreenController.set_enableInput(false);
                SettingsScreenController.set_enableInput(false);

                Process process = pb.start();
                process.waitFor();
                return null;
            }
        };



    }

}
