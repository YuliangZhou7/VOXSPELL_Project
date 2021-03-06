package gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Main application launcher. Loads in all the screens and sets the initial screen to
 * the title screen
 *
 * Created by Yuliang Zhou 24/9/2016
 */
public class Main extends Application {

    private MasterController _mainContainer;

    private static Stage _stage;

    //Set enums for each screen that has been loaded.
    public enum Screen{TITLE,QUIZ,LEVELSELECT,POSTQUIZ,SETTINGS,STATS,VIDEO,HELP};

    public static final String titleScreenFXML = "/resources/fxml/titleScreen.fxml";
    public static final String quizScreenFXML = "/resources/fxml/quizScreen.fxml";
    public static final String statsScreenFXML = "/resources/fxml/statsScreen.fxml";
    public static final String settingsScreenFXML = "/resources/fxml/settingsScreen.fxml";
    public static final String levelScreenFXML = "/resources/fxml/levelSelectScreen.fxml";
    public static final String postQuizScreenFXML = "/resources/fxml/postQuizScreen.fxml";
    public static final String videoPlayerFXML = "/resources/fxml/videoPlayer.fxml";
    public static final String helpScreenFXML = "/resources/fxml/helpScreen.fxml";


    @Override
    public void start(Stage primaryStage) throws Exception{
        _stage = primaryStage;
        //load in all the screens in the main container.
        _mainContainer = new MasterController();
        _mainContainer.loadScreen(Screen.TITLE,titleScreenFXML);
        _mainContainer.loadScreen(Screen.QUIZ,quizScreenFXML);
        _mainContainer.loadScreen(Screen.STATS,statsScreenFXML);
        _mainContainer.loadScreen(Screen.SETTINGS,settingsScreenFXML);
        _mainContainer.loadScreen(Screen.LEVELSELECT,levelScreenFXML);
        _mainContainer.loadScreen(Screen.POSTQUIZ,postQuizScreenFXML);
        _mainContainer.loadScreen(Screen.VIDEO,videoPlayerFXML);
        _mainContainer.loadScreen(Screen.HELP,helpScreenFXML);

        //set the screen on launch to the TITLE screen.
        _mainContainer.setScreen(Screen.TITLE);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume(); //prevents window from automatically closing
                _mainContainer.confirmCloseProgram();
            }
        });

        // Creates a node that contains an ObservableList of children in order.
        Group root = new Group();
        root.getChildren().add(_mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setTitle("VOXSPELL Spelling App");
        //Setting the first stage as the titleScreen.
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static Stage getStage() {
        return _stage;
    }

    /**
     * Application calls this method when program is closed. Does final wrapping up.
     * Saves object state
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        System.out.println("Exiting...");
        _mainContainer.saveData();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
