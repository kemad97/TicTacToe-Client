/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.userProfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.json.JSONObject;
import tictactoe.client.animation.Animation;
import tictactoe.client.available_players.FXMLAvailablePlayersController;
import tictactoe.client.server_connection.Request;
import tictactoe.client.session_data.SessionData;

/**
 *
 * @author ayaah
 */
public class UserProfileController implements Initializable {

    @FXML
    private ImageView logo;

    @FXML
    private TextField email;

    @FXML
    private TextField name;

    @FXML
    private TextField score;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Animate logo
        Animation.scaleAnimation(logo, ScaleTransition.INDEFINITE, 0.5);
        Platform.runLater(this::requestUserData);
    }

    private void requestUserData() {
        try {
            JSONObject request = new JSONObject();
            request.put("header", "get_user_profile");
            request.put("username", SessionData.getUsername());
            System.out.println("Request: " + request.toString());

            Request.getInstance().sendRequest(request.toString());
            System.out.println("Request sent.");

            JSONObject jsonObject = Request.getInstance().recieve();
            System.out.println("Response: " + jsonObject.toString());

            if (jsonObject.getString("header").equals("user_profile")) {
                Platform.runLater(() -> {
                    name.setText(jsonObject.getString("name"));
                    email.setText(jsonObject.getString("email"));
                    score.setText(jsonObject.getString("score"));
                });

            } else {
                System.out.println("Error: " + jsonObject.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
