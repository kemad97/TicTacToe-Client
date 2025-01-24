package tictactoe.client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tictactoe.client.server_connection.Request;
import tictactoe.client.server_ip.ServerIP;
import tictactoe.client.session_data.SessionData;
import tictactoe.client.soundManager.SoundManager;

/**
 *
 * @author musta
 */
public class TicTacToeClient extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("splash_screen/FXMLSplashScreen.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        SoundManager.playBackgroundMusic();

    }

    @Override
    public void stop() {
        try {
            if (!SessionData.getOpponentName().isEmpty()) {
                Request.getInstance().exitMatch(SessionData.getOpponentName());
            }
            Request.getInstance().disconnectToServer();
        } catch (IOException ex) {
            System.out.println("server is dowen");
        }
        SoundManager.stopBackgroundMusic();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
