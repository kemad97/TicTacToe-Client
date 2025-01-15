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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;
import session_data.SessionData;
import tictactoe.client.animation.Animation;
import tictactoe.client.main_screen.FXMLMainScreenController;
import tictactoe.client.server_connection.Request;
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
            alert.show();
            return;
        }

        disableUI();
        new Thread(() -> {
            try {
                Request.getInstance().registration(username.getText().trim(),
                        password.getText().trim().hashCode() + "");

                handleResponse(Request.getInstance().receve());
            } catch (IOException ex) {
                System.out.println("Error: can't connect to server.");
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

    private void handleResponse(JSONObject receve) {
        //reenable screen ui
        Platform.runLater(() -> enableUI());

        switch (receve.getString("header")) {
            case "success":

                //update session data
                SessionData.setUsername(receve.getString("message"));
                SessionData.setAuthenticated(true);

                Platform.runLater(() -> {
                    Alert success;
                    success = new Alert(Alert.AlertType.INFORMATION);
                    success.setContentText("Registration Successful: " + receve.getString("message"));

                    Optional<ButtonType> result = success.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        gotoMainScreen(username.getText().trim());
                    }
                });

                break;
            case "register_error":
            case "error":

                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(receve.getString("message"));
                    alert.show();
                });
                break;
        }
    }

    private void gotoMainScreen(String username) {
        try {
             
            SoundManager.playSoundEffect("click.wav");


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tictactoe/client/main_screen/FXMLMainScreen.fxml"));

            Parent root = loader.load();

            FXMLMainScreenController mainScreen = loader.getController();

            Platform.runLater(() -> {
                mainScreen.updateUsername(username);
            });

            Scene scene = new Scene(root);
            Stage stage = (Stage) logo.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLRegisterationScreenController.class.getName()).log(Level.SEVERE, null, ex);
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

            System.out.println("Back To Main Screen");

            Parent root = FXMLLoader.load(getClass().getResource("/tictactoe/client/main_screen/FXMLMainScreen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) logo.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {

            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}
