package tictactoe.client.splash_screen;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class FXMLSplashScreenController implements Initializable {

    @FXML
    ImageView logo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ScaleTransition transition = new ScaleTransition();
        transition.setNode(logo);
        transition.setDuration(Duration.millis(1000));
        transition.setCycleCount(5);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setByX(1.4);
        transition.setByY(1.4);
        transition.setAutoReverse(true);
        transition.play();
        
        transition.setOnFinished((ActionEvent event)->{
            System.out.println("hello");
        });

    }

}
