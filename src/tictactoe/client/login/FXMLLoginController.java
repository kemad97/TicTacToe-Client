/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.login;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tictactoe.client.animation.Animation;
import tictactoe.client.main_screen.FXMLMainScreenController;

/**
 * FXML Controller class
 *
 * @author musta
 */
public class FXMLLoginController implements Initializable {

    @FXML
    private ImageView logo;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button LoginBtn;
    @FXML
    private Label Register_label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //animate logo
        Animation.scaleAnimation(logo, ScaleTransition.INDEFINITE, 0.5);
    }

    @FXML
    private void login(ActionEvent event) {
    }

    @FXML
    private void gotoRegisterationPage(MouseEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/tictactoe/client/register/FXMLRegisterationScreen.fxml"));
            Scene scene = new Scene(root);

            Stage stage = (Stage) logo.getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
