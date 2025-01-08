/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.resultVideoScreen;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

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

   private MediaPlayer mediaPlayer;
    //private String winner;


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
        
        /*winner = "O";
        winnerLabel.setText("Winner is: " + winner);
        if (winner != null) {
       
           winnerLabel.setText("Winner is: " + winner);
        }*/
        
        
        try {
            
            URL videoUrl = getClass().getResource("/media/video/winner-video.mp4");
            if (videoUrl == null) {
                System.err.println("Can't found  video");
            }

            String videoPath = videoUrl.toExternalForm();
            System.out.println("Video path: " + videoPath); 

            Media media = new Media(videoPath);
            
            mediaPlayer = new MediaPlayer(media);

            mediaView.setMediaPlayer(mediaPlayer);
        
            mediaView.setPreserveRatio(true);
                 
            mediaPlayer.setAutoPlay(true);

            


           

            

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setWinner(String winner) {
    
        if (winnerLabel != null) {
        
            System.out.println("Setting winnerLabel with: " + winner);
        
            winnerLabel.setText("Winner is: " + winner);
    
        } else {
        
            System.err.println("winnerLabel is null. Check FXML binding in ResultVideoScreen.fxml.");
    
        }

    }

    

}