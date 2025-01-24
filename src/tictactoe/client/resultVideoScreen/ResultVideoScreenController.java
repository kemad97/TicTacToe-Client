/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.resultVideoScreen;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import tictactoe.client.gameBoardWithFriend.GameBoardWithFriendController;
import tictactoe.client.mainScreen.FXMLMainScreenController;
import tictactoe.client.soundManager.SoundManager;

/**
 *
 * @author ayaah
 */
public class ResultVideoScreenController implements Initializable {

   @FXML
   private ImageView logo;
    
   @FXML
   private Label winnerLabel;
   
   @FXML
   private MediaView mediaView;
   
   @FXML
   private Button btnContinue ;
   
   @FXML
   private ImageView btnBack;

   private MediaPlayer mediaPlayer;
   
   
   
 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Animate logo
        ScaleTransition transition = new ScaleTransition();
        transition.setNode(logo);
        transition.setDuration(Duration.millis(1000));
        transition.setCycleCount(ScaleTransition.INDEFINITE);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setByX(0.5);
        transition.setByY(0.5);
        transition.setAutoReverse(true);
        transition.play();
        
        btnContinue.setVisible(false);
        btnBack.setVisible(false);
        
        try {
            
            URL videoUrl = getClass().getResource("/media/video/winner-video.mp4");
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
                    btnContinue.setVisible(true);
                    btnBack.setVisible(true);
                });

                pause.play();
            });

        } catch (Exception e) {
            
            e.printStackTrace();
            
        }

    }
    
    @FXML
    private void handleContinueButtonAction(ActionEvent event) {
        
        SoundManager.playSoundEffect("click.wav");
        
        System.out.println("Play another Match");

        mediaPlayer.stop();
            
        backToGameBoardWithFriend();
      
    }
    
    private void backToGameBoardWithFriend(){
        
        try {
             
            System.out.println("Back To Game Board With Friend Screen");

            Parent root = FXMLLoader.load(getClass().getResource("/tictactoe/client/gameBoardWithFriend/GameBoardWithFriend.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) logo.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            
            SoundManager.resumeBackgroundMusic();
                    
        } catch (IOException ex) {
                    
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void backToMainScreen(){
        
        try {
            
            SoundManager.playSoundEffect("click.wav");
            
            GameBoardWithFriendController.setxScore(0);
            GameBoardWithFriendController.setoScore(0);
             
            System.out.println("Back To Main Screen");
            
            mediaPlayer.stop();

            Parent root = FXMLLoader.load(getClass().getResource("/tictactoe/client/mainScreen/FXMLMainScreen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) logo.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            
            SoundManager.resumeBackgroundMusic();
                    
        } catch (IOException ex) {
                    
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
     

    
    public void setWinner(String winner) {
        
        winnerLabel.setStyle("-fx-font-weight: bold;");
        winnerLabel.setText("Winner is: " + winner);

    }
    
  
    

}