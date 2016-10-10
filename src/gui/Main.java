package gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    private MasterController _mainContainer;

    //Set enums for each screen that has been loaded.
    public enum Screen{TITLE,QUIZ,LEVELSELECT,POSTQUIZ,SETTINGS,STATS,VIDEO};

    public static final String titleScreenFXML = "titleScreen.fxml";
    public static final String quizScreenFXML = "quizScreen.fxml";
    public static final String statsScreenFXML = "statsScreen.fxml";
    public static final String settingsScreenFXML = "settingsScreen.fxml";
    public static final String levelScreenFXML = "levelSelectScreen.fxml";
    public static final String postQuizScreenFXML = "postQuizScreen.fxml";
    public static final String videoPlayerFXML = "videoPlayer.fxml";


    @Override
    public void start(Stage primaryStage) throws Exception{
        //loading in all the screens in the main container.
        _mainContainer = new MasterController();
        _mainContainer.loadScreen(Screen.TITLE,titleScreenFXML);
        _mainContainer.loadScreen(Screen.QUIZ,quizScreenFXML);
        _mainContainer.loadScreen(Screen.STATS,statsScreenFXML);
        _mainContainer.loadScreen(Screen.SETTINGS,settingsScreenFXML);
        _mainContainer.loadScreen(Screen.LEVELSELECT,levelScreenFXML);
        _mainContainer.loadScreen(Screen.POSTQUIZ,postQuizScreenFXML);
        _mainContainer.loadScreen(Screen.VIDEO,videoPlayerFXML);

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
        
        primaryStage.setMaxHeight(425);
        primaryStage.setMaxWidth(675);
        primaryStage.setMinHeight(425);
        primaryStage.setMinWidth(650);

        primaryStage.show();

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
