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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
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
    @FXML
    private Button delBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Animate logo
        Animation.scaleAnimation(logo, ScaleTransition.INDEFINITE, 0.5);
        requestUserData();
        if (SessionData.getUserAvatar() != null) {
            userProfileImg.setImage(SessionData.getUserAvatar());
        }
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
                        Platform.runLater(() -> goToAvailableScreen());
                        break;
                    case "UserDeleted":
                        deleteMeResponce();
                        isReceving = false;
                        break;
                }
            } catch (IOException ex) {
                break;
            }
        }
    }

    private void terminateUserProfileScreen() {
        //show aleart the server is down
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Server Message");
        alert.setHeaderText("Server now is down!");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/commonStyle/alert-style.css").toExternalForm());
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

        } catch (IOException ex) {
            Logger.getLogger(UserProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void deleteMeResponce() throws IOException {

        // Clean up session and disconnect AFTER successful deletion
        SessionData.deleteDate();

        Request.getInstance().disconnectToServer();

        // Navigate to main screen LAST
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Delete Acctoun");
                alert.setHeaderText("Account Deleted Successfully!");
                alert.show();
                String mainScreenPath = "/tictactoe/client/main_screen/FXMLMainScreen.fxml";
                SceneNavigation.getInstance().nextScene(mainScreenPath, logo);
            } catch (IOException ex) {
                Logger.getLogger(UserProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @FXML
    private void handleDelAccountBtn(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Do you want to Delete Your Account!");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            try {
                SoundManager.playSoundEffect("click.wav");

                JSONObject request = new JSONObject();
                request.put("header", "delete_user");
                request.put("username", SessionData.getUsername());

                // Send delete request to server
                Request.getInstance().sendRequest(request.toString());

            } catch (IOException ex) {
                System.out.println("server is down!");
            }
        }
    }

    @FXML
    private void uploadAvatar() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tictactoe/client/userProfile/ImageSelection.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Choose Profile Image");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            ImageSelectionController controller = loader.getController();
            Image selectedImage = controller.getSelectedImage();

            if (selectedImage != null) {

                userProfileImg.setImage(selectedImage);
                SessionData.setUserImage(selectedImage);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void goToAvailableScreen() {
        String availableScreenPath = "/tictactoe/client/available_players/FXMLAvailablePlayers.fxml";
        try {
            SceneNavigation.getInstance().nextScene(availableScreenPath, logo);
        } catch (IOException ex) {
            Logger.getLogger(UserProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
