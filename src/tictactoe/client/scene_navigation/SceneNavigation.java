package tictactoe.client.scene_navigation;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tictactoe.client.online_game_board.FXMLOnlineGameBoardController;

public class SceneNavigation {

    private static SceneNavigation instatnce;

    private SceneNavigation() {}

    public static SceneNavigation getInstance() {
        if (instatnce == null) {
            instatnce = new SceneNavigation();
        }
        return instatnce;
    }

    public void nextScene(String fxmlPath, Node node) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root);

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }
    
    public void gotoOnlineBoard(String fxmlPath, Node node, String opponentName, Boolean isPlayerTurn) throws IOException {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        
        Parent root = loader.load();
        Scene scene = new Scene(root);

        FXMLOnlineGameBoardController onlineController;
        onlineController = loader.getController();
        onlineController.setOpponentName(opponentName);
        onlineController.serOpponentTurn(isPlayerTurn);
        
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }
}
