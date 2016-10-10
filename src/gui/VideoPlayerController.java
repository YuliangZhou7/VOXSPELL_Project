package gui;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by samule on 20/09/16.
 */
public class VideoPlayerController implements Initializable, ControlledScreen{

    private MasterController _myParentScreensController;

    @FXML
    private MediaView _mediaView;
    private MediaPlayer _mediaPlayer;
    private Media _media;

    @FXML
    private Slider _volumeSlider;

    /**
     * Initialises media by locating the position of the media file that we wish to play.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //locates the file location of play.mp4
        File file = new File("./src/resources/play.mp4");
        //converts the media path to a URI.
        _media = new Media(file.toURI().toString());
        _mediaPlayer = new MediaPlayer(_media);
        _mediaView.setMediaPlayer(_mediaPlayer);
        //sets a volume of the mediaPlayer
        _volumeSlider.setValue(_mediaPlayer.getVolume() * 100);
        //makes the volume adjustable via a slider.
        _volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                _mediaPlayer.setVolume(_volumeSlider.getValue() / 100);
            }
        });
    }

    /**
     * plays the video at speed 1.
     * @param event
     */
    public void play(ActionEvent event){
        _mediaPlayer.play();
        _mediaPlayer.setRate(1);
    }

    /**
     * pauses the video.
     * @param event
     */
    public void pause(ActionEvent event){
        _mediaPlayer.pause();
    }

    /**
     * fastforwards the video.
     * @param event
     */
    public void fastforward(ActionEvent event){
        _mediaPlayer.setRate(2);
    }

    /**
     * slows down the video.
     * @param event
     */
    public void slowDown(ActionEvent event){
        _mediaPlayer.setRate(0.5);
    }

    /**
     * starts the video from the beginnning again.
     * @param event
     */
    public void reload(ActionEvent event){
        _mediaPlayer.seek(_mediaPlayer.getStartTime());
        _mediaPlayer.play();
        _mediaPlayer.setRate(1);
    }

    /**
     * returns the creen back into the post screen and stops the video.
     * @param event
     */
    public void returnToPostQuizScreen(ActionEvent event) {
        _mediaPlayer.stop();
        _myParentScreensController.setScreen(Main.Screen.POSTQUIZ);
    }

    @Override
    public void setScreenParent(MasterController screenParent) {
        _myParentScreensController = screenParent;
    }

    @Override
    public void setup() {

    }
}
