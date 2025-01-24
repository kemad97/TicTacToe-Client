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
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import tictactoe.client.animation.Animation;

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
    private Label win_matches;

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

    private void handleResponse() throws IOException {
        try {
            JSONObject jsonObject = Request.getInstance().recieve();
            if (jsonObject == null) {
                System.out.println("No response received from server.");
                return;
            }

            System.out.println("Response: " + jsonObject.toString());

            if (jsonObject.getString("header").equals("user_profile")) {
                Platform.runLater(() -> {
                    name.setText(jsonObject.getString("name"));
                    score.setText(jsonObject.getString("score"));
                    matches_no.setText(jsonObject.getString("matches_no"));
                    win_matches.setText(jsonObject.getString("won_matches"));
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
