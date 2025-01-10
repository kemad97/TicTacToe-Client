package tictactoe.client.main_screen;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tictactoe.client.animation.Animation;
import tictactoe.client.server_ip.ServerIP;


public class FXMLMainScreenController implements Initializable {

    @FXML
    private ImageView logo;
    @FXML
    private TextField inputIP;
    @FXML
    private Button authentication;
    @FXML
    private Label username;
    @FXML
    private Pane onlineBtn;
    @FXML
    private Pane offlineFriendBtn;
    @FXML
    private Pane offlinePCBtn;
    @FXML
    private Button updateIPBtn;

    
    public void updateUsername(String username){
        this.username.setText(username);
        this.authentication.setText("Logout");
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //animate logo
        Animation.scaleAnimation(logo, ScaleTransition.INDEFINITE, 0.5);

        //initialize server ip
        new Thread(() -> {
            inputIP.setText(ServerIP.getIP());
        }).start();

        username.setText("");

    }

    @FXML
    private void startOnlineMatch(MouseEvent event) {
        System.out.println("start online match");
    }

    @FXML
    private void startOfflineMatchVSFriend(MouseEvent event) {

        try {

            System.out.println("start offline match vs. Friend");

            Parent root = FXMLLoader.load(getClass().getResource("/tictactoe/client/gameBoardWithFriend/GameBoardWithFriend.fxml"));
            Scene scene = new Scene(root);

            Stage stage = (Stage) logo.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void startOfflineMatchVSPC(MouseEvent event) {
        try {
            System.out.println("start offline match vs. PC");

            Parent root = FXMLLoader.load(getClass().getResource("/tictactoe/client/levels/levelsUI.fxml"));
            Scene scene = new Scene(root);

            Stage stage = (Stage) logo.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void updateIP(ActionEvent event) {
        String ip = inputIP.getText().trim();

        if (!ServerIP.getIP().equals(ip)) {
            if (ServerIP.isValidIP(ip)) {

                ServerIP.saveIP(ip);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("New IP updated");
                alert.show();
            } else if (!ServerIP.getIP().equals(ip)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid ip format");
                alert.show();
            }
        }
    }

    @FXML
    private void authenticate(ActionEvent event) {
        if (authentication.getText().equals("Login")) {

            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("/tictactoe/client/login/FXMLLogin.fxml"));
                Scene scene = new Scene(root);

                Stage stage = (Stage) logo.getScene().getWindow();

                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            authentication.setText("Login");
            username.setText("");

            //send to server to remove this user from login list
        }
    }

}
