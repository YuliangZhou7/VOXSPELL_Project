package gui;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URISyntaxException;

/**
 * Created by Samule Li on 20/09/16.
 */
public class VideoPlayerController implements ControlledScreen{

    private MasterController _myParentScreensController;

    @FXML
    private MediaView _mediaView;
    private MediaPlayer _mediaPlayer;
    private Media _media;

    @FXML
    private Slider _volumeSlider;

    @Override
    public void setScreenParent(MasterController screenParent) {
        _myParentScreensController = screenParent;
    }

    @Override
    public void setup() {

    }

    @Override
    public void displayScreen() {
        play(null);
    }

    /**
     * This method is called by the PostQuizScreen and sets up the media video to play.
     */
    public void setCurrentVideo(int levelVideo) {
        if(levelVideo==1){
            try {
                _media = new Media(VideoPlayerController.class.getResource("/resources/videos/play_1.mp4").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else if(levelVideo==2){
            try {
                _media = new Media(VideoPlayerController.class.getResource("/resources/videos/play_2.mp4").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else if(levelVideo==3){
            try {
                _media = new Media(VideoPlayerController.class.getResource("/resources/videos/play_3.mp4").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else if(levelVideo==4){
            try {
                _media = new Media(VideoPlayerController.class.getResource("/resources/videos/play_4.mp4").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else if(levelVideo==5){
            try {
                _media = new Media(VideoPlayerController.class.getResource("/resources/videos/play_5.mp4").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else if(levelVideo==6){
            try {
                _media = new Media(VideoPlayerController.class.getResource("/resources/videos/play_6.mp4").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else if(levelVideo==7){
            try {
                _media = new Media(VideoPlayerController.class.getResource("/resources/videos/play_7.mp4").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else if(levelVideo==8){
            try {
                _media = new Media(VideoPlayerController.class.getResource("/resources/videos/play_8.mp4").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else if(levelVideo==9){
            try {
                _media = new Media(VideoPlayerController.class.getResource("/resources/videos/play_9.mp4").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else if(levelVideo==10){
            try {
                _media = new Media(VideoPlayerController.class.getResource("/resources/videos/play_10.mp4").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else if(levelVideo==11){
            try {
                _media = new Media(VideoPlayerController.class.getResource("/resources/videos/play_11.mp4").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        //set up media player view
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


}
