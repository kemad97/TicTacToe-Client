package tictactoe.client.scene_navigation;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tictactoe.client.online_game_board.FXMLOnlineGameBoardController;
import tictactoe.client.online_video_screen.OnlineVideoScreenController;
import tictactoe.client.resultVideoScreen.ResultVideoScreenController;

public class SceneNavigation {

    private static SceneNavigation instatnce;

    private SceneNavigation() {
    }

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

    public void gotoVideoScreen(Node node,
            boolean isWinner,
            String opponentName) throws IOException {

        OnlineVideoScreenController.setIsWinner(isWinner);
        OnlineVideoScreenController.setOpponentName(opponentName);

        String fxmlPath = "/tictactoe/client/online_video_screen/OnlineVideoScreen.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root);
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void gotoOnlineBoard(Node node, String opponentName, Boolean isMyTurn) throws IOException {

        String fxmlPath = "/tictactoe/client/online_game_board/FXMLOnlineGameBoard.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

        Parent root = loader.load();
        Scene scene = new Scene(root);

        FXMLOnlineGameBoardController onlineController;
        onlineController = loader.getController();
        onlineController.setOpponentName(opponentName);
        onlineController.setMyTurn(isMyTurn);

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }
}
