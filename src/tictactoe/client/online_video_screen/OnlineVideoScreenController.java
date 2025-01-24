/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.online_video_screen;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import tictactoe.client.animation.Animation;
import tictactoe.client.gameBoardWithFriend.GameBoardWithFriendController;
import tictactoe.client.main_screen.FXMLMainScreenController;
import tictactoe.client.scene_navigation.SceneNavigation;
import tictactoe.client.server_connection.Request;
import tictactoe.client.session_data.SessionData;
import tictactoe.client.soundManager.SoundManager;

/**
 * FXML Controller class
 *
 * @author musta
 */
public class OnlineVideoScreenController implements Initializable {

    @FXML
    private ImageView logo;

    @FXML
    private MediaView mediaView;

    @FXML
    private Label UserLabel;

    @FXML
    private ImageView btnBack;

    private static String videoPath;
    private static boolean isWinner;
    private static String opponentName;

    private MediaPlayer mediaPlayer;

    public static void setIsWinner(boolean is_winner) {
        isWinner = is_winner;
        if (is_winner) {
            videoPath = "/media/video/winner-video.mp4";
        } else {
            videoPath = "/media/video/loser-video.mp4";
        }
    }

    public static void setOpponentName(String opponent_name) {
        opponentName = opponent_name;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //animate logo
        Animation.scaleAnimation(logo, ScaleTransition.INDEFINITE, 0.5);

        btnBack.setVisible(false);
        UserLabel.setText(SessionData.getUsername());

        try {

            URL videoUrl = getClass().getResource(videoPath);
            if (videoUrl == null) {
                System.err.println("Can't found  video");
            }

            String videoPath = videoUrl.toExternalForm();

            Media media = new Media(videoPath);

            mediaPlayer = new MediaPlayer(media);

            mediaView.setMediaPlayer(mediaPlayer);

            mediaView.setPreserveRatio(true);

            mediaPlayer.setAutoPlay(true);

            // Animate Video
            ScaleTransition transition2 = new ScaleTransition();
            transition2.setNode(mediaView);
            transition2.setDuration(Duration.millis(1000));
            transition2.setCycleCount(2);
            transition2.setInterpolator(Interpolator.LINEAR);
            transition2.setByX(0.5);
            transition2.setByY(0.5);
            transition2.setAutoReverse(true);
            transition2.play();

            mediaPlayer.setOnReady(() -> {

                Duration videoDuration = mediaPlayer.getMedia().getDuration();

                PauseTransition pause = new PauseTransition(videoDuration);
                pause.setOnFinished(event -> {
                    SoundManager.resumeBackgroundMusic();
                    btnBack.setVisible(true);
                });

                pause.play();
            });

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @FXML
    private void backToMainScreen() {

        try {
            SoundManager.playSoundEffect("click.wav");
            
            mediaPlayer.stop();
            
            //send to server to change player status from in game to available
            Request.getInstance().endPlayerGame();

            String mainScrenePath = "/tictactoe/client/available_players/FXMLAvailablePlayers.fxml";
            SceneNavigation.getInstance().nextScene(mainScrenePath, logo);

            SoundManager.resumeBackgroundMusic();

        } catch (IOException ex) {
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
