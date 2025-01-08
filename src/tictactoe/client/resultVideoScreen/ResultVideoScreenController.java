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

    //static String winner;


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
        
        
       // winnerLabel.setText("Winner is: " + this.winner);
       
    }
    
    public void setWinner(String winner) {
        
        //this.winner = winner;
        
        if (winnerLabel != null) {
            
            winnerLabel.setText("Winner is: " + winner);
    
        } else {
        
            System.err.println("winnerLabel is null. Check FXML binding.");
    
        }
    }
    
    
}

