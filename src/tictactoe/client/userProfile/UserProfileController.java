/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.userProfile;

import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.json.JSONException;
import org.json.JSONObject;
import tictactoe.client.server_connection.Request;
import tictactoe.client.session_data.SessionData;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import tictactoe.client.animation.Animation;
import tictactoe.client.available_players.FXMLAvailablePlayersController;
import tictactoe.client.scene_navigation.SceneNavigation;
import tictactoe.client.soundManager.SoundManager;

/**
 *
 * @author ayaah
 */
public class UserProfileController implements Initializable {

    @FXML
    private ImageView logo;

    @FXML
    private Label name;

    @FXML
    private Label score;

    @FXML
    private Label matches_no;
    
    @FXML
    private ImageView userProfileImg;

    @FXML
    private Label win_matches;
    private boolean isReceving;
    @FXML
    private ImageView btnBack;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Animate logo
        Animation.scaleAnimation(logo, ScaleTransition.INDEFINITE, 0.5);
        requestUserData();
    }

    private void requestUserData() {
        new Thread(() -> {
            try {
                sendRequest();

                isReceving = true;
                handleResponse();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }).start();
    }

    private void sendRequest() throws IOException {
        try {
            JSONObject request = new JSONObject();
            request.put("header", "get_user_profile");
            request.put("username", SessionData.getUsername());
            System.out.println("Request: " + request.toString());

            Request.getInstance().sendRequest(request.toString());
            System.out.println("Request sent");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleResponse() {
        while (isReceving) {
            try {
                JSONObject jsonObject = Request.getInstance().recieve();

                switch (jsonObject.getString("header")) {
                    case "user_profile":
                        Platform.runLater(() -> {
                            name.setText(jsonObject.getString("name"));
                            score.setText(jsonObject.getString("score"));
                            matches_no.setText(jsonObject.getString("matches_no"));
                            win_matches.setText(jsonObject.getString("won_matches"));
                        });
                        break;
                    case "server_down":
                        Platform.runLater(() -> terminateUserProfileScreen());
                        break;
                    case "your_state_available":
                        isReceving = false;
                        break;
                }
            } catch (IOException ex) {
                break;
            }
        }
    }

    private void terminateUserProfileScreen() {
        //show aleart the server is dowen
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Server Message");
        alert.setHeaderText("Server now is dowen!");
        alert.show();
        //close conniction with server
        try {
            Request.getInstance().disconnectToServer();
        } catch (IOException ex) {
            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //redirect all users to main screen
        String mainScenePath = "/tictactoe/client/main_screen/FXMLMainScreen.fxml";
        try {
            SceneNavigation.getInstance().nextScene(mainScenePath, score);
        } catch (IOException ex) {
            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        SessionData.deleteDate();
    }

    @FXML
    private void backToAvailablePlayers(MouseEvent event) {
        try {
            SoundManager.playSoundEffect("click.wav");
            Request.getInstance().askServerToMakeMeAvailable();
            
            String availableScreenPath = "/tictactoe/client/available_players/FXMLAvailablePlayers.fxml";
            SceneNavigation.getInstance().nextScene(availableScreenPath, logo);
        } catch (IOException ex) {
            Logger.getLogger(UserProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
