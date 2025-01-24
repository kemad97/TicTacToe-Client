package tictactoe.client.resultVideoScreenwithPC;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import tictactoe.client.animation.Animation;
import tictactoe.client.mainScreen.FXMLMainScreenController;
import tictactoe.client.soundManager.SoundManager;

public class ResultVideoScreenWithPCController implements Initializable {

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
   
   private String defaultVideo;
   
 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Animate logo
        Animation.scaleAnimation(logo, ScaleTransition.INDEFINITE, 0.5);
                
        btnContinue.setVisible(false);
        btnBack.setVisible(false);
        defaultVideo = "/media/video/winner-video.mp4";
        
        try {
            
            URL videoUrl = getClass().getResource(defaultVideo);
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
            Animation.scaleAnimation(mediaView, 2, 0.5);
            
            
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
             
            System.out.println("Back To Game Board With PC Screen");

            Parent root = FXMLLoader.load(getClass().getResource("/tictactoe/client/levels/levelsUI.fxml"));
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