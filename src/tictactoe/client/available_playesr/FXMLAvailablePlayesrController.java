package tictactoe.client.available_playesr;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import scene_navigation.SceneNavigation;
import session_data.SessionData;
import tictactoe.client.server_connection.Request;
import tictactoe.client.soundManager.SoundManager;

public class FXMLAvailablePlayesrController implements Initializable {

    @FXML
    private Label username;
    @FXML
    private Label score;
    @FXML
    private ImageView logo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void logout(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Do you want to logout!");
        
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            String mainScreenPath = "/tictactoe/client/main_screen/FXMLMainScreen.fxml";

            try {
                //cloase connection with server
                Request.getInstance().disconnectToServer();
            } catch (IOException ex) {
                System.out.println("server is dowen!");
            }

            //update session data
            SessionData.setAuthenticated(false);
            SessionData.setUsername(null);

            //goto main screen
            try {
                SceneNavigation.getInstance().nextScene(mainScreenPath, score);
            } catch (IOException ex) {
                Logger.getLogger(FXMLAvailablePlayesrController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
