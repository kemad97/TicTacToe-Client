/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.userProfile;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import tictactoe.client.animation.Animation;

/**
 *
 * @author ayaah
 */
public class UserProfileController implements Initializable{
    
    @FXML
    private ImageView logo;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Animate logo
        Animation.scaleAnimation(logo, ScaleTransition.INDEFINITE, 0.5);
      
    }   
}
