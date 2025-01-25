/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.register;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;
import tictactoe.client.scene_navigation.SceneNavigation;
import tictactoe.client.session_data.SessionData;
import tictactoe.client.animation.Animation;
import tictactoe.client.login.FXMLLoginController;
import tictactoe.client.main_screen.FXMLMainScreenController;
import tictactoe.client.server_connection.Request;
import tictactoe.client.server_ip.ServerIP;
import tictactoe.client.soundManager.SoundManager;

/**
 * FXML Controller class
 *
 * @author musta
 */
public class FXMLRegisterationScreenController implements Initializable {

    @FXML
    private ImageView logo;
    @FXML
    private Button regesterationBtn;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Label registeration_label;
    @FXML
    private ImageView btnBack;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //animate logo
        Animation.scaleAnimation(logo, ScaleTransition.INDEFINITE, 0.5);
    }

    @FXML
    private void registerUser(ActionEvent event) {

        SoundManager.playSoundEffect("click.wav");

        if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must fill username and password!");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/commonStyle/alert-style.css").toExternalForm());
            alert.show();
            return;
        }

        disableUI();
        new Thread(() -> {
            try {
                Request.getInstance().registration(username.getText().trim(),
                        password.getText().trim().hashCode() + "");

                handleResponse(Request.getInstance().recieve());
            } catch (IOException ex) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("can't connect to server.");
                    alert.getDialogPane().getStylesheets().add(getClass().getResource("/commonStyle/alert-style.css").toExternalForm());
                    alert.show();
                    enableUI();
                });
            }
        }).start();

    }

    @FXML
    private void goToLoginScreen(MouseEvent event) {
        Parent root;
        try {

            SoundManager.playSoundEffect("click.wav");

            root = FXMLLoader.load(getClass().getResource("/tictactoe/client/login/FXMLLogin.fxml"));
            Scene scene = new Scene(root);

            Stage stage = (Stage) logo.getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleResponse(JSONObject recieve) {
        //reenable screen ui
        Platform.runLater(() -> enableUI());

        switch (recieve.getString("header")) {
            case "success":
                //update session data
                SessionData.setAuthenticated(true);
                SessionData.setUsername(recieve.getString("username"));
                SessionData.setScore(recieve.getInt("score"));

                Platform.runLater(() -> {
                    Alert success;
                    success = new Alert(Alert.AlertType.INFORMATION);
                    success.setContentText("Registration Successful: " + recieve.getString("username"));
                    success.getDialogPane().getStylesheets().add(getClass().getResource("/commonStyle/alert-style.css").toExternalForm());
                    Optional<ButtonType> result = success.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        try {
                            SoundManager.playSoundEffect("click.wav");
                            String availablePlayersPath = "/tictactoe/client/available_players/FXMLAvailablePlayers.fxml";
                            SceneNavigation.getInstance().nextScene(availablePlayersPath, logo);
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                break;
            case "register_error":
            case "error":

                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(recieve.getString("message"));
                    alert.getDialogPane().getStylesheets().add(getClass().getResource("/commonStyle/alert-style.css").toExternalForm());
                    alert.show();
                });
                break;
        }
    }

    private void disableUI() {
        username.disableProperty().set(true);
        password.disableProperty().set(true);
        regesterationBtn.disableProperty().set(true);
        registeration_label.disableProperty().set(true);
    }

    private void enableUI() {
        username.disableProperty().set(false);
        password.disableProperty().set(false);
        regesterationBtn.disableProperty().set(false);
        registeration_label.disableProperty().set(false);
    }

    @FXML
    private void backToMainScreen() {

        try {
            SoundManager.playSoundEffect("click.wav");
            String mainScrenePath = "/tictactoe/client/main_screen/FXMLMainScreen.fxml";
            SceneNavigation.getInstance().nextScene(mainScrenePath, logo);
        } catch (IOException ex) {

            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void showIpEdit(MouseEvent event) {
        SoundManager.playSoundEffect("click.wav");

        TextInputDialog dialog = new TextInputDialog(ServerIP.getIP());
        dialog.setTitle("Update server IP");
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/commonStyle/alert-style.css").toExternalForm());
        dialog.showAndWait().ifPresent(string -> updateIP(string.trim()));
    }

    private void updateIP(String ip) {
        if (!ServerIP.getIP().equals(ip)) {
            if (ServerIP.isValidIP(ip)) {
                ServerIP.saveIP(ip);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("New IP updated");
                alert.getDialogPane().getStylesheets().add(getClass().getResource("/commonStyle/alert-style.css").toExternalForm());
                alert.show();
            } else if (!ServerIP.getIP().equals(ip)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid ip format");
                alert.getDialogPane().getStylesheets().add(getClass().getResource("/commonStyle/alert-style.css").toExternalForm());
                alert.show();
            }
        }
    }

}
