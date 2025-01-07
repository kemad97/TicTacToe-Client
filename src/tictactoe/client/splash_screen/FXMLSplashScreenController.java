package tictactoe.client.splash_screen;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FXMLSplashScreenController implements Initializable {

    @FXML
    ImageView logo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //animate logo
        ScaleTransition transition = new ScaleTransition();
        transition.setNode(logo);
        transition.setDuration(Duration.millis(1000));
        transition.setCycleCount(3);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setByX(1.4);
        transition.setByY(1.4);
        transition.setAutoReverse(true);
        transition.play();

        transition.setOnFinished((ActionEvent event) -> {
            try {
                AnchorPane root = FXMLLoader.load(getClass().getResource("/tictactoe/client/main_screen/FXMLMainScreen.fxml"));
                Scene scene = new Scene(root);

                Stage stage = (Stage) logo.getScene().getWindow();

                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLSplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

    }

}
