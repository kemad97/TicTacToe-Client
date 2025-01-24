package tictactoe.client;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tictactoe.client.server_connection.Request;
import tictactoe.client.session_data.SessionData;
import tictactoe.client.soundManager.SoundManager;


public class TicTacToeClient extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("splashScreen/FXMLSplashScreen.fxml"));

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
            System.out.println("server is down");
        }
        SoundManager.stopBackgroundMusic();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
