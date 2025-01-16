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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import scene_navigation.SceneNavigation;
import session_data.SessionData;
import tictactoe.client.animation.Animation;
import tictactoe.client.soundManager.SoundManager;

public class FXMLMainScreenController implements Initializable {

    @FXML
    private ImageView logo;

    @FXML
    private Pane onlineBtn;
    @FXML
    private Pane offlineFriendBtn;
    @FXML
    private Pane offlinePCBtn;
    @FXML
    private ImageView logsBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //animate logo
        Animation.scaleAnimation(logo, ScaleTransition.INDEFINITE, 0.5);
    }

    @FXML
    private void startOnlineMatch(MouseEvent event) {

        SoundManager.playSoundEffect("click.wav");

        System.out.println("start online match");

        if (!SessionData.isAuthenticated()) {
            String loginFXMLPath = "/tictactoe/client/login/FXMLLogin.fxml";
            try {
                SceneNavigation.getInstance().nextScene(loginFXMLPath, logo);
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //if user logged in goto available palayers
        }
    }

    @FXML
    private void startOfflineMatchVSFriend(MouseEvent event) {

        try {

            SoundManager.playSoundEffect("click.wav");

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

            SoundManager.playSoundEffect("click.wav");

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
    private void showGameLogs(MouseEvent event) {
        Parent root;
        try {
            SoundManager.playSoundEffect("click.wav");

            root = FXMLLoader.load(getClass().getResource("/tictactoe/client/RecScreen/RecScreen.fxml"));
            Scene scene = new Scene(root);

            Stage stage = (Stage) logo.getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
